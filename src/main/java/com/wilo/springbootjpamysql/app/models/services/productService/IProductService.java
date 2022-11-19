package com.wilo.springbootjpamysql.app.models.services.productService;

import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.Product;

import java.util.List;

public interface IProductService {

    public List<Product> findProductByName(String term);


}
