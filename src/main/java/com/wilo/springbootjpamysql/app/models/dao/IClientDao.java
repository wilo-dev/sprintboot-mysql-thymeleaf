package com.wilo.springbootjpamysql.app.models.dao;

import com.wilo.springbootjpamysql.app.models.entity.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClientDao extends PagingAndSortingRepository<Client, Long> {

    /*
    traiga toda la consulta de una,
    - validamos cuando el cliente no tenga factura
    - agregamos un left.- esto significa que cuando tengo
    o no tenga factura tiene que mostrame el client de todos modos
    **/

    @Query("select c from Client c left join fetch c.facturas f where  c.id=?1")
    public Client fetchByIdWithFactura(Long id);

}
