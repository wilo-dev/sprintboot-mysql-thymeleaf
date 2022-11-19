package com.wilo.springbootjpamysql.app.models.services.productService;

import com.wilo.springbootjpamysql.app.models.dao.IProductDao;
import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDao productDao;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductByName(String term) {
//        return productDao.findProductByName(term);
        return productDao.findByNameLikeIgnoreCase("%" + term + "%");
    }

 
}
