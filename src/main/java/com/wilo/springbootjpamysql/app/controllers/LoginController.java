package com.wilo.springbootjpamysql.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@SessionAttributes("")
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, Principal principal, RedirectAttributes flash) {

        //principal.- para ver si hizo ya login
        if (principal != null) {
            flash.addFlashAttribute("info", "ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }
        return "login";
    }
}

//see video 9