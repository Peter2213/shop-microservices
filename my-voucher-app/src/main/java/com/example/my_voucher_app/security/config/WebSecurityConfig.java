package com.example.my_voucher_app.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
// import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class WebSecurityConfig {
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
         http.httpBasic(Customizer.withDefaults())
             .authorizeHttpRequests(authz -> authz
                 .requestMatchers(HttpMethod.POST, "/auth/signup").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.GET, "/voucherapi/vouchers/**").hasAnyRole("USER","ADMIN")
                 .requestMatchers(HttpMethod.POST, "/voucherapi/vouchers").hasRole("ADMIN")
                 .anyRequest().authenticated())
             .csrf(csrf -> csrf.disable());
         return http.build();
    }

}
