package com.wilo.springbootjpamysql.app;

import com.wilo.springbootjpamysql.app.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler successHandler;

    public SpringSecurityConfig(LoginSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // public routes
        http.authorizeRequests()
                .antMatchers("/",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/list").permitAll()
                // private routes
                .antMatchers("/ver/**").hasAnyRole("USER")
                .antMatchers("/uploads/**").hasAnyRole("USER")
                .antMatchers("/form/**").hasAnyRole("ADMIN")
                .antMatchers("/delete/**").hasAnyRole("ADMIN")
                .antMatchers("/factura/**").hasAnyRole("ADMIN")
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

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // los users se guardan en memoria
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {

        PasswordEncoder encoder = passwordEncoder();
        UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("123456").roles("ADMIN", "USER"))
                .withUser(users.username("william").password("123456").roles("USER"));
    }
}

