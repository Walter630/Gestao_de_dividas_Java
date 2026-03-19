package com.Gestao_de_Contas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecuriyConfi {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    //colocar o resto das rotas http
                    auth.requestMatchers("/auth/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder(); }
}
