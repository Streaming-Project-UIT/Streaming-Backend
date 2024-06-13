package com.programming.streaming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Secure {

    // Security
    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/register", "/error").permitAll()
                        .requestMatchers("/listUser").permitAll()
                        .requestMatchers("/video/upload").permitAll()
                        .requestMatchers("/video/list").permitAll()
                        .requestMatchers("/file/upload").permitAll()
                        .requestMatchers("/file/list").permitAll()
                        .requestMatchers("/file/downloadZipFile").permitAll()
                        .requestMatchers("/comments/upload").permitAll()
                        .requestMatchers("/comments/**").permitAll()
                        .requestMatchers("/video/**").permitAll()
                        .requestMatchers("/login2").permitAll()
                        .requestMatchers("/listUserbyId/**").permitAll()
                        .requestMatchers("/updateProfile/**").permitAll()
                        .requestMatchers("/send-verification-email").permitAll()
                        .requestMatchers("/hello-world").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}