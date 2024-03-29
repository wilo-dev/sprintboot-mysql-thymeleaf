package com.wilo.springbootjpamysql.app;

import com.wilo.springbootjpamysql.app.auth.handler.LoginSuccessHandler;
import com.wilo.springbootjpamysql.app.models.services.userService.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler successHandler;

    public SpringSecurityConfig(LoginSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private JpaUserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // public routes
        http.authorizeRequests()
                .antMatchers("/",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/list**",
                        "/api/client/**",
                        "/locale").permitAll()
                // private routes
                // seguridad a controladores mediante forma programatica
//                .antMatchers("/ver/**").hasAnyRole("USER")
//                .antMatchers("/uploads/**").hasAnyRole("USER")
//                .antMatchers("/form/**").hasAnyRole("ADMIN")
//                .antMatchers("/delete/**").hasAnyRole("ADMIN")
//                .antMatchers("/factura/**").hasAnyRole("ADMIN")

                // seguridad a controladores mediante anotaciones
                // se usa directamente en ele controlador


                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(successHandler)
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403");


    }


    // los users se guardan en memoria
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {

        // configuration JDBC authentication
//        builder.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(passwordEncoder)
//                .usersByUsernameQuery("select username, password, enable from users where username=?") // consulta sql nativa
//                .authoritiesByUsernameQuery("select u.username, r.rol from roles r inner join users u on (r.user_id=u.id) where u.username=?");

        // configuration JPA authentication
        // creamos IUserDao
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        // memoria de autenticacion
//        PasswordEncoder encoder = this.passwordEncoder;
//        UserBuilder users = User.builder().passwordEncoder(encoder::encode);
//
//        builder.inMemoryAuthentication()
//                .withUser(users.username("admin").password("123456").roles("ADMIN", "USER"))
//                .withUser(users.username("william").password("123456").roles("USER"));
    }
}
