package com.programming.streaming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecureBackup {

    // Security
    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.ignoringRequestMatchers("/register", "/login2")) // CSRF disabled for specific endpoints
                .cors(Customizer.withDefaults()) // Enable CORS with default configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/error", "/login2", "/send-verification-email").permitAll()
                        .requestMatchers("/video/**", "/file/**", "/comments/**", "/hello-world").permitAll()
                        .requestMatchers("/listUser", "/listUserbyId/**", "/updateProfile/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession())
                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()) // Enforce HTTPS
                .build();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Increase strength of BCrypt
    }
}
