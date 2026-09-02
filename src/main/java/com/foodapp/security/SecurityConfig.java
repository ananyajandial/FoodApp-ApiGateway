package com.foodapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.http.HttpMethod;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	
	@Bean
	public MapReactiveUserDetailsService userDetailsService() {

	    UserDetails admin =
	            User.withUsername("admin")
	                    .password("{noop}admin123")
	                    .roles("ADMIN")
	                    .build();

	    UserDetails user =
	            User.withUsername("user")
	                    .password("{noop}user123")
	                    .roles("USER")
	                    .build();

	    return new MapReactiveUserDetailsService(
	            admin,
	            user
	    );
	}
	
	@Bean
	public SecurityWebFilterChain securityFilterChain(
	        ServerHttpSecurity http) {

	    return http
	            .cors(Customizer.withDefaults())
	            .csrf(ServerHttpSecurity.CsrfSpec::disable)

	            .authorizeExchange(exchanges -> exchanges
	            		
	            		.pathMatchers(HttpMethod.OPTIONS)
	            		.permitAll()

	            		
	            		.pathMatchers("/me")
	            		.authenticated()
	            		
	            		.pathMatchers("/users/all")
	            		.hasRole("ADMIN")

	            		.pathMatchers("/orders/allOrders")
	            		.hasRole("ADMIN")

	                    .pathMatchers("/admin/**")
	                    .hasRole("ADMIN")

	                    .pathMatchers("/users/**")
	                    .hasAnyRole("USER", "ADMIN")

	                    .pathMatchers("/orders/**")
	                    .hasAnyRole("USER", "ADMIN")

	                    .anyExchange()
	                    .authenticated()
	            )

	            .httpBasic(Customizer.withDefaults())

	            .build();
	}
	
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsWebFilter(source);
	}
	

	

}
