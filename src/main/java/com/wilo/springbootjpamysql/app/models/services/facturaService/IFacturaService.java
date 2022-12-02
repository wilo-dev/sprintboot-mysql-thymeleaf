package com.wilo.springbootjpamysql.app.models.services.facturaService;

import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.Product;

import java.awt.*;

public interface IFacturaService {

    // save
    public void save(Factura factura);

    // buscar producto por id
    public Product findProductById(Long id);

    // ver la factura
    public Factura findFacturaById(Long id);

    // eiminar factura
    public void deleteFactura(Long id);

    // Viene todas las relaciones de una
    public Factura fetchFacturaByIdWithClientWithItemWithProduct(Long id);

}
