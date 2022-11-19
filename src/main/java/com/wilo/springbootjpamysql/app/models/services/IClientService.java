package com.wilo.springbootjpamysql.app.models.services;

import com.wilo.springbootjpamysql.app.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component("iClientService")
public interface IClientService {

    // muestra todos los client
    public List<Client> findAll();

    // pagination
    public Page<Client> findAll(Pageable pageable);

    // guarda clientes
    public void save(Client client);

    // edit
    public Client findById(Long id);

    // delete
    public void delete(Long id);


}
