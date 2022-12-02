package com.wilo.springbootjpamysql.app.models.dao;

import com.wilo.springbootjpamysql.app.models.entity.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
    /*
    Viene todas las relaciones de una
    - Tiene que existir datos en ambas tablas de la relacion
   ***/

    @Query("select f from Factura " +
            "f join fetch f.client " +
            "c join fetch f.items " +
            "ip join fetch ip.product " +
            "where f.id=?1")
    public Factura fetchByIdWithClientWithItemWithProduct(Long Id);
}
