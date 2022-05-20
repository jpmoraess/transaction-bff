package br.com.moraesit.transactionbff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    public static final String ROLE = "SCOPE_CoffeAndITRole";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        http.exceptionHandling()
                .and().redirectToHttps()
                .and().csrf().disable().formLogin().disable()
                .httpBasic().disable().authorizeExchange()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/transaction/**", "/limites/**").access(
                        (authentication, object) -> authentication.flatMap(authentication1 ->
                                Mono.just(new AuthorizationDecision(
                                        ((JwtAuthenticationToken) authentication1).getAuthorities().contains(new SimpleGrantedAuthority(ROLE))
                                ))
                        )
                ).anyExchange().authenticated().and().oauth2ResourceServer().jwt();
        return http.build();
    }
}
