package com.wilo.springbootjpamysql.app.models.dao;

import com.wilo.springbootjpamysql.app.models.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User, Long> {

    public User findByUsername(String username);
}


/*
public User findByUsername(String username);
A través del nombre del metodo (query method name),
se ejecutara la consulta JPQL:
"select u from User u where u.username=?1"

otra forma seria poner otro nombre y anotar
con la anotacion query ejemplo:

@Query("select u from User u where u.username=?1")
    public User encuentraUsuario(String username);
**/
