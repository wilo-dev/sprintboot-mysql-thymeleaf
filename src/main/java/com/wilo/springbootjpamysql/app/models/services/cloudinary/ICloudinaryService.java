package com.wilo.springbootjpamysql.app.models.services.cloudinary;

import com.wilo.springbootjpamysql.app.models.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ICloudinaryService extends CrudRepository<Client, Long> {
}
