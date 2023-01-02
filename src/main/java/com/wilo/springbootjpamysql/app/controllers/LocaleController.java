package com.wilo.springbootjpamysql.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {

    @GetMapping("/locale")
    public String locale(HttpServletRequest request) {
        // obtengo la ultimo url de la pagina
        String lastUrl = request.getHeader("referer");
        return "redirect:".concat(lastUrl);
    }
}
