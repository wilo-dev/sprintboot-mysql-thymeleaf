package com.wilo.springbootjpamysql.app.view.xml;

import com.wilo.springbootjpamysql.app.models.entity.Client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "clientes")
public class ClientList {

    @XmlElement(name = "cliente")
    public List<Client> clientes;

    public ClientList() {
    }

    public ClientList(List<Client> clientes) {
        this.clientes = clientes;
    }

    public List<Client> getClientes() {
        return clientes;
    }

}

