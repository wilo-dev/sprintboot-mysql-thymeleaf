package com.wilo.springbootjpamysql.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

//    @Autowired
//    private HttpServletRequest request;


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model, Principal principal, RedirectAttributes flash) {

//        principal.- para ver si hizo ya login
        if (principal != null) {
            flash.addFlashAttribute("info", "ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }

        // Verifica el nombre de usuario y la contraseña
        if (error != null) {
            model.addAttribute("error", "Nombre o contraseña incorrectos, vuelva a intentar");
//            model.addAttribute("message",
//                    new AlertMessage(
//                            "error",
//                            "nombre o contraseña incorrecta, intente de nuevo"));
        }

        // Obtiene la dirección IP del cliente
//        String ipAddress = request.getLocalAddr();
//        log.info("ipAddress: ".concat(ipAddress));


        if (logout != null) {
            model.addAttribute("info", "Ha cerrada sesión con éxito!");
        }

        return "login";
    }
}

