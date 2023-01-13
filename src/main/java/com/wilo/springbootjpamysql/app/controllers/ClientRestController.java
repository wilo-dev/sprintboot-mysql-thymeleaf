package com.wilo.springbootjpamysql.app.controllers;

import com.wilo.springbootjpamysql.app.models.services.IClientService;
import com.wilo.springbootjpamysql.app.view.xml.ClientList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientRestController {
    @Autowired
    private IClientService clientService;

    @GetMapping("/list")
    public ClientList list() {
        return new ClientList(clientService.findAll());
    }
}
