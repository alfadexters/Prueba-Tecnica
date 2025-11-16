package com.devops.prueba_tecnica.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF para facilitar pruebas con curl / Postman
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Permitimos acceso sin autenticación a /DevOps
                        .requestMatchers("/DevOps/**").permitAll()
                        // Si quieres puedes dejar todo lo demás abierto también
                        .anyRequest().permitAll()
                );

        // También desactivamos el login por formulario y basic auth si se activan
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
