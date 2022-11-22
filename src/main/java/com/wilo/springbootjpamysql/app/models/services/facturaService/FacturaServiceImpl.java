package com.wilo.springbootjpamysql.app.models.services.facturaService;

import com.wilo.springbootjpamysql.app.models.dao.IFacturaDao;
import com.wilo.springbootjpamysql.app.models.dao.IProductDao;
import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("iFacturaService")
public class FacturaServiceImpl implements IFacturaService {

    @Autowired
    private IFacturaDao facturaDao;

    @Autowired
    private IProductDao productDao;

    @Override
    @Transactional
    public void save(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }
}

/*
@Transactional(readOnly = true).- como solo es una consulta y no modifica la DB
por eso es readOnly= true

* **/