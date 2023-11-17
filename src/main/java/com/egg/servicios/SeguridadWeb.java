package com.egg.servicios;

import com.egg.servicios.servicios.ClienteServicio;
import com.egg.servicios.servicios.ProveedorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb {

    @Autowired
    private ProveedorServicio proveedorServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(proveedorServicio).passwordEncoder(new BCryptPasswordEncoder());
    }


    //chequear si al loguearnos nos lleva al index o a una pagina de inicio

    //Esto seria login para sprint version 5 o menor
    @Bean
    protected SecurityFilterChain Login(HttpSecurity http) throws Exception {

        http.
                authorizeHttpRequests((authz) -> {
                            try {
                                authz.anyRequest().
                                        permitAll()
                                        .and().formLogin()
                                        .loginPage("/login")
                                        .loginProcessingUrl("/logincheck")
                                        .usernameParameter("correo")
                                        .passwordParameter("contrasenia")
                                        .defaultSuccessUrl("/index")
                                        .permitAll()
                                        .and().logout()
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/")  //pienso que aqui seria una redireccion al index
                                        .permitAll();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .httpBasic(withDefaults());
        return http.build();

    }
//para version de spring 6
   /* @Bean
    protected SecurityFilterChain login(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/admin/*")
                        .hasRole("ADMIN")
                        .anyRequest()
                        .permitAll())
                .formLogin(
                        formLogin-> formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/logincheck")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/inicio")
                                .permitAll())
                .logout( logout-> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login").
                        permitAll() )
                //  .httpBasic(withDefaults())
                .csrf(csrf->csrf.disable())
                .httpBasic(withDefaults());
        return http.build();
    }*/
}
