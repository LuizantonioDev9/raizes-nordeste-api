package com.testefinal.demofinal.infrastructure.config;

import com.testefinal.demofinal.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                    {
                                      "timestamp": "%s",
                                      "status": 403,
                                      "error": "ACESSO_NEGADO",
                                      "message": "Acesso negado. Usuário sem permissão para executar esta ação.",
                                      "path": "%s"
                                    }
                                    """.formatted(java.time.LocalDateTime.now(), request.getRequestURI()));
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                    {
                                      "timestamp": "%s",
                                      "status": 401,
                                      "error": "NÃO_AUTORIZADO",
                                      "message": "Token inválido, expirado ou ausente. Por favor, realize o login.",
                                      "path": "%s"
                                    }
                                    """.formatted(java.time.LocalDateTime.now(), request.getRequestURI()));
                        })
                )

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/clientes").permitAll()
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()

                                // UNIDADES
                                .requestMatchers(HttpMethod.POST, "/unidades/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/unidades/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/unidades/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/unidades/*/produtos").authenticated()
                                .requestMatchers(HttpMethod.GET, "/unidades/*/produtos").permitAll()

                                // CLIENTE
                                .requestMatchers(HttpMethod.POST, "/pedidos/**").hasAnyRole("CLIENTE", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/pedidos/**").hasAnyRole("CLIENTE", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/pedidos/**").hasAnyRole("CLIENTE", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/pedidos/meu-carrinho").hasAnyRole("CLIENTE", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/cupons").hasAnyRole("CLIENTE", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/cupons/**").hasAnyRole("CLIENTE", "ADMIN")
                                .requestMatchers("/pagamentos/**").hasAnyRole("CLIENTE", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/produtos/**").authenticated()

                                // FUNCIONÁRIO / ADMIN
                                .requestMatchers(HttpMethod.PUT, "/pedidos/*/preparar").hasAnyRole("FUNCIONARIO", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/pedidos/*/pronto").hasAnyRole("FUNCIONARIO", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/pedidos/*/entregar").hasAnyRole("FUNCIONARIO", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/estoques/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                                .requestMatchers(HttpMethod.GET, "/pedidos/**").hasAnyRole("FUNCIONARIO", "ADMIN")

                                // ADMIN
                                .requestMatchers(HttpMethod.POST, "/produtos/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/produtos/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/produtos/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/funcionarios").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/funcionarios/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/funcionarios/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/funcionarios/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/cupons/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/cupons/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/cupons/**").hasRole("ADMIN")
                                .requestMatchers("/estoques/**").hasRole("ADMIN")

                                // CONSULTAS
                                .requestMatchers(HttpMethod.GET, "/produtos/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/pedidos/**").authenticated()

                                .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
