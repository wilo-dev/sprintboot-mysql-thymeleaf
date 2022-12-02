package com.wilo.springbootjpamysql.app.models.services;

import com.wilo.springbootjpamysql.app.models.dao.IClientDao;
import com.wilo.springbootjpamysql.app.models.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("iClientService")
public class ClientServiceImpl implements IClientService {

    @Autowired
    private IClientDao clientDao;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return (List<Client>) clientDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        return clientDao.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Client client) {
        clientDao.save(client);

    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clientDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Client fetchByIdWithFactura(Long id) {
        return clientDao.fetchByIdWithFactura(id);
    }


}
