package com.wilo.springbootjpamysql.app.models.dao;

import com.wilo.springbootjpamysql.app.models.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductDao extends CrudRepository<Product, Long> {

    // creamos un metodo sin implemetacion que podria ser (IProductServiceImpl)

    /*
    alternativa_1.- usando anotacion @Query. Implementamos la consulta
    con el like consulta a nivel de objeto entity (jpa),
    no de nivel de tabla (DB-mysql)
    **/
    @Query("select p from Product p where p.name like %?1%")
    public List<Product> findProductByName(String term);

    /*
    alternativa 2.- implemantando un metodo
    **/
    public List<Product> findByNameLikeIgnoreCase(String term);
}
