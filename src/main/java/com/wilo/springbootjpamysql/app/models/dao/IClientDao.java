package com.wilo.springbootjpamysql.app.models.dao;

import com.wilo.springbootjpamysql.app.models.entity.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClientDao extends PagingAndSortingRepository<Client, Long> {


}
