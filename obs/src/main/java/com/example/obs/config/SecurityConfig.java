package com.example.obs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/book-images/**", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/", "/login", "/book/**", "/search").permitAll()
                .requestMatchers("/cart", "/cart/**").permitAll()
                .requestMatchers("/checkout", "/checkout/**").permitAll()
                .requestMatchers("/verify-email", "/resend-verification").permitAll()
                .requestMatchers("/forgot-password", "/reset-password").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("uniqueAndSecret") // Change this to a secure secret key in production
                .tokenValiditySeconds(30 * 24 * 60 * 60) // 30 days
                .userDetailsService(userDetailsService)
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("remember-me-cookie")
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me-cookie")
                .permitAll()
            )
            .exceptionHandling(handling -> handling
                .accessDeniedPage("/login?denied=true")
            );
            
        return http.build();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configure database authentication with proper password encoding
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
        
        // Keep the in-memory admin user for now
        auth.inMemoryAuthentication()
            .withUser("admin")
            .password(passwordEncoder.encode("password"))
            .roles("ADMIN");
    }
} 