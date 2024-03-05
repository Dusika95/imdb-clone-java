package org.imdb.clone.security.config;

import org.imdb.clone.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                // Public routes
                .antMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                .antMatchers(HttpMethod.GET,  "/movies/**", "/names/**","/reviews/**","/ratings/**","/search").permitAll()
                // authentication routes
                .antMatchers(HttpMethod.GET, "/users/**").authenticated()
                .antMatchers(HttpMethod.GET, "/profile").authenticated()
                .antMatchers(HttpMethod.PUT, "/profile").authenticated()
                // authorization routes
                .antMatchers(HttpMethod.POST, "/movies","/names").hasAuthority("Editor")
                .antMatchers(HttpMethod.PUT, "/movies","names").hasAuthority("Editor")
                .antMatchers(HttpMethod.POST, "/reviews","/ratings").hasAuthority("User")
                .antMatchers(HttpMethod.DELETE,"/profile").hasAuthority("User")
                .antMatchers(HttpMethod.PUT, "/reviews/**","/ratings/**").hasAuthority("User")
                .antMatchers(HttpMethod.POST, "/users").hasAnyAuthority("Admin")
                .antMatchers(HttpMethod.DELETE, "/reviews/**").hasAnyAuthority("User","Moderator")
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority("Moderator")
                .antMatchers(HttpMethod.DELETE, "/ratings/**").hasAnyAuthority("Moderator")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors();
        return http.build();
    }
}
