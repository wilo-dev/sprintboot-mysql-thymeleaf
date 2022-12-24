package com.wilo.springbootjpamysql.app.controllers;


import com.cloudinary.utils.ObjectUtils;
import com.wilo.springbootjpamysql.app.helpers.CloudinaryConfig;
import com.wilo.springbootjpamysql.app.models.entity.Client;
import com.wilo.springbootjpamysql.app.models.services.IClientService;
import com.wilo.springbootjpamysql.app.models.services.uploadImg.IUploadsService;
import com.wilo.springbootjpamysql.app.util.pagination.PageRender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Controller("clientController")
@SessionAttributes("client")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);


    @Autowired
    private IClientService clientService;

    @Autowired
    private IUploadsService uploadsService;

    @Autowired
    CloudinaryConfig clouConfig;

    /*
  ================================================================================
        Uploads programáticamente en respuesta HTTP(otra manera de subir foto)
        see: https://www.baeldung.com/spring-mvc-pathvariable-dot
  ================================================================================
  ***/
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<UrlResource> seePhoto(@PathVariable String filename) {

        UrlResource resource = null;

        try {
            resource = uploadsService.getImg(filename);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /*
    ================================================================================
                            Mostrar la img en la vista
    ================================================================================
    ***/
    @Secured("ROLE_USER")
    @PreAuthorize("hasRole('ROLE_USER')") // otra manera de seguridad
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable("id") Long id, Map<String, Object> model,
                      RedirectAttributes flash) {

//        Client client = clientService.findById(id);
        Client client = clientService.fetchByIdWithFactura(id);

        // validar si existe client
        if (client == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la DB ");
            return "redirect:/list";
        }
        model.put("client", client);
        model.put("title", "Detalle client " + client.getName());
        return "ver";

    }


    /*
   ================================================================================
                                   GET LIST
   ================================================================================
   ***/
    @GetMapping({"/", "/list"})
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       Model model, Authentication authentication, HttpServletRequest request) {
        /*
        En vez de pasar el Authentication por argumento al metodo handler, inyectando el
        Authentication.
        tambien podemos usar metodos estatico.- esto permite que se puedad usar en cualquier
        parte de la aplicacion
        **/

        // verificamos la autorizacion del rol
        if (authentication != null) {
            log.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
        }

        // verificamos la autorizacion del rol, metodo estatica
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("Utilizando de forma estatica, Hola usuario autenticado, tu username es: ".concat(auth.getName()));
        }

        // metodo_1, validar el rol, de forma manual
        if (hasRole("ROLE_ADMIN")) {
            assert auth != null;
            log.info("usando hasRole de forma manual, Hola ".concat(auth.getName().concat(" tienes acceso!")));
        } else {
            assert auth != null;
            log.info("usando hasRole de forma manual, Hola ".concat(auth.getName().concat(" no tienes acceso! de super usuario")));
        }

        // metodo_2, validar el rol, usando SecurityContextHolderAwareRequestWrapper
        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
        if (securityContext.isUserInRole("ROLE_ADMIN")) {
            log.info("usando SecurityContextHolderAwareRequestWrapper, Hola ".concat(auth.getName().concat(" tienes acceso!")));
        } else {
            log.info("usando SecurityContextHolderAwareRequestWrapper, Hola ".concat(auth.getName().concat(" no tienes acceso! de super usuario")));
        }

        // metodo_3, validar el rol, forma nativa usando HttpServletRequest
        if (request.isUserInRole("ROLE_ADMIN")) {
            log.info("usando el HttpServletRequest, Hola ".concat(auth.getName().concat(" tienes acceso!")));
        } else {
            log.info("usando el HttpServletRequest, Hola ".concat(auth.getName().concat(" no tienes acceso! de super usuario")));
        }


        // paginacion
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Client> clientPage = clientService.findAll(pageRequest);
        PageRender<Client> pageRender = new PageRender<>("/list", clientPage);

        model.addAttribute("title", "Listado de clientes");
        model.addAttribute("clients", clientPage);
        model.addAttribute("page", pageRender);
//        model.addAttribute("clients", clientService.findAll());
        return "list";
    }

    /*
   ================================================================================
                                       CREATE
   ================================================================================
   ***/
    @Secured("ROLE_ADMIN")
    @GetMapping({"/form"})
    public String create(Map<String, Object> model) {

        Client client = new Client();
        model.put("client", client);
        model.put("title", "Formulario de client");

        return "formulary/form";
    }

    /*
 ================================================================================
                                     EDIT
 ================================================================================
 ***/
    @Secured("ROLE_ADMIN")
    @GetMapping("/form/{id}")
    public String edit(@PathVariable("id") Long id, Map<String, Object> model,
                       RedirectAttributes flash) {
        Client client = null;

        if (id > 0) {
            client = clientService.findById(id);
            if (client == null) {
                flash.addFlashAttribute("error", "El ID cliente no existe en la DB");
                return "redirect:/list";
            }
        } else {
            flash.addFlashAttribute("error", "El id no puede ser cero");
            return "redirect:/list";
        }

        model.put("client", client);
        model.put("title", "Edit client");
        return "formulary/form";
    }

    /*
 ================================================================================
     SAVE formulary Uploading Images CLOUDINARY
 ================================================================================
 ***/
    @Secured("ROLE_ADMIN")
    // validamos el form por cada input con @valid
    @PostMapping("/form")
    public String save(@Valid Client client, BindingResult result,
                       Model model,
                       @RequestParam("file") MultipartFile photo,
                       RedirectAttributes flash,
                       SessionStatus status) {

        if (result.hasErrors()) {
            // si falla pasamos el titulo y de forma automatica se pasa el client
            model.addAttribute("title", "Formulario de client");
            return "formulary/form";
        }

        // validar q no sea vacio (si photo no viene vacio)
        if (!photo.isEmpty()) {
            Path uploads = Path.of("uploads");

            // si existe el client
            if (client.getId() != null
                    && client.getId() > 0
                    && client.getPhoto() != null
                    && client.getPhoto().length() > 0) {

//                se elimina la img del server
                uploadsService.deleteImg(client.getPhoto());
            }

            String uniqueFilename = null;
            Map uploadResp = null;
            try {
                // change name img and save server
//                uniqueFilename = uploadsService.copy(photo);

                // save cloudinary
                uploadResp = clouConfig.uploadImg(photo.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "springBoot/actor/",
                                "resource_type", "auto"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            flash.addFlashAttribute("info",
                    "Has subido correctamente la foto '" + photo.getOriginalFilename() + "'");
//            client.setPhoto(uniqueFilename);


            client.setPhoto(uploadResp.get("url").toString());
        }

        String msjFlash = client.getId() != null ? "Cliente editado con éxito" : "Cliente creado con éxito";

        clientService.save(client);
        status.setComplete();
        flash.addFlashAttribute("success", msjFlash);
        return "redirect:/list";
    }

    /*
     ================================================================================
                                         DELETE CLIENT WITH IMG
     ================================================================================
     se elimina el client se elimina la photo.
     ***/
    @Secured("ROLE_ADMIN")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes flash) {

        Client client = clientService.findById(id);

        if (id > 0) {
            clientService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito");

            // delete photo
            if (uploadsService.deleteImg(client.getPhoto())) {
                flash.addFlashAttribute("info",
                        "foto " + client.getPhoto() + " eliminada con exito");
            }
        }
        return "redirect:/list";
    }

      /*
     ================================================================================
                                         validar roles
     ================================================================================
     ***/

    private boolean hasRole(String role) {

        SecurityContext contextHolder = SecurityContextHolder.getContext();
        if (contextHolder == null) {
            return false;
        }

        Authentication auth = contextHolder.getAuthentication();
        if (auth == null) {
            return false;
        }

//         todo role tiene que pasar por aqui
        Collection<? extends GrantedAuthority> autCollection = auth.getAuthorities();

        return autCollection.contains(new SimpleGrantedAuthority(role));

        // metodo_1, validar el rol, de forma manual
//        for (GrantedAuthority authority : autCollection) {
//            if (role.equals(authority.getAuthority())) {
//                log.info("Hola usuario ".concat(auth.getName()).concat(" tu role es ".concat(authority.getAuthority())));
//                return true;
//            }
//        }
//        return false;

    }
}


