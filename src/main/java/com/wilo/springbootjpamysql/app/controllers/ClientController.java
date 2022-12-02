package com.wilo.springbootjpamysql.app.controllers;


import com.wilo.springbootjpamysql.app.models.entity.Client;
import com.wilo.springbootjpamysql.app.models.services.IClientService;
import com.wilo.springbootjpamysql.app.models.services.uploadImg.IUploadsService;
import com.wilo.springbootjpamysql.app.util.pagination.PageRender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Map;

@Controller("clientController")
@SessionAttributes("client")
public class ClientController {


    @Autowired
    private IClientService clientService;

    @Autowired
    private IUploadsService uploadsService;

    /*
  ================================================================================
        Uploads programáticamente en respuesta HTTP(otra manera de subir foto)
        see: https://www.baeldung.com/spring-mvc-pathvariable-dot
  ================================================================================
  ***/
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
    public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
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
                                     SAVE
 ================================================================================
 ***/
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

        // validar q no sea vacio
        if (!photo.isEmpty()) {
            Path uploads = Path.of("uploads");
            if (client.getId() != null
                    && client.getId() > 0
                    && client.getPhoto() != null
                    && client.getPhoto().length() > 0) {

                uploadsService.deleteImg(client.getPhoto());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadsService.copy(photo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            flash.addFlashAttribute("info",
                    "Has subido correctamente la foto '" + uniqueFilename + "'");
            client.setPhoto(uniqueFilename);
        }

        String msjFlash = client.getId() != null ? "Cliente editado con éxito" : "Cliente creado con éxito";

        clientService.save(client);
        status.setComplete();
        flash.addFlashAttribute("success", msjFlash);
        return "redirect:/list";
    }

    /*
     ================================================================================
                                         DELETE CLIENT
     ================================================================================
     se elimina el client se elimina la photo.
     ***/
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes flash) {

        Client client = clientService.findById(id);

        if (id > 0) {
            clientService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito");

            if (uploadsService.deleteImg(client.getPhoto())) {
                flash.addFlashAttribute("info",
                        "foto " + client.getPhoto() + " eliminada con exito");
            }
        }
        return "redirect:/list";
    }
}

