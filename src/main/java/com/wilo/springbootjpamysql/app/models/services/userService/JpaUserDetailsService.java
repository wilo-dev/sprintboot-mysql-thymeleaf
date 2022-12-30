package com.wilo.springbootjpamysql.app.models.services.userService;

import com.wilo.springbootjpamysql.app.models.dao.IUserDao;
import com.wilo.springbootjpamysql.app.models.entity.Role;
import com.wilo.springbootjpamysql.app.models.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Autowired
    private IUserDao userDao;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        // validar user no existe en la db
        if (user == null) {
            log.error("Error, no existe el user".concat(username));
            throw new UsernameNotFoundException("Username".concat(username) + "no existe");
        }


        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            log.info("Role:".concat(role.getRol()));
            authorities.add(new SimpleGrantedAuthority(role.getRol()));
        }

        // user existe pero no tiene roles asignados
        if (authorities.isEmpty()) {
            log.error("Error, usuario".concat(username) + "no tiene roles asignado!");
            throw new UsernameNotFoundException("Error, usuario".concat(username) + "no tiene roles asignado!");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnable(),
                true,
                true,
                true,
                authorities);
    }
}

/*
No necesitamos implementar una interfaz propia,
ya q la provee spring security
**/


