package com.bezkoder.spring.jpa.h2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/security/public").permitAll()
        .requestMatchers("/api/security/admin").hasRole("ADMIN")
        .requestMatchers("/api/security/user", "/api/security/me").hasAnyRole("USER", "ADMIN")
        .requestMatchers("/api/tutorials/**").permitAll()
        .requestMatchers("/h2-ui/**").permitAll()
        .anyRequest().authenticated())
      .httpBasic(Customizer.withDefaults())
      .formLogin(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails user = User.withUsername("user")
      .password(passwordEncoder.encode("password"))
      .roles("USER")
      .build();

    UserDetails admin = User.withUsername("admin")
      .password(passwordEncoder.encode("admin123"))
      .roles("ADMIN")
      .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
