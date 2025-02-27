package com.StudentMS.config;

import com.StudentMS.entity.Student;
import com.StudentMS.serviceImpl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/login")) // Ignore CSRF for login..
                .authorizeHttpRequests(auth -> auth
                        // Public Access (No authentication required)
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/favicon.ico", "students/save", "teachers/save").permitAll()

                        .requestMatchers("/students", "/teachers", "/dashboard", "/contact", "/departments", "/courses")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        // Specific Access for Students
                        .requestMatchers("/students/**").hasAnyRole("ADMIN", "TEACHER") // General students access

                        .requestMatchers("/students/edit/**").permitAll()
                        // Specific Access for Teachers
                        .requestMatchers("/teachers/edit/**").hasAnyRole("ADMIN", "TEACHER")

                        // Admin-Only Access (Wildcard)
                        .requestMatchers("/teachers/**", "/departments/**", "/courses/**", "/users/**").hasRole("ADMIN")

                        // General Access (Dashboard, Contact, Departments, Courses, Students, Teachers)
                        // Catch-All Rule (All other requests require authentication)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler()) // Add this
                        .defaultSuccessUrl("/dashboard")
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}