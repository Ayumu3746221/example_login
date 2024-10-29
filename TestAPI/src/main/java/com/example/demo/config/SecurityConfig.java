package com.example.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.GoogleTokenAuthenticationProvider;
import com.example.demo.security.GoogleTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private GoogleTokenAuthenticationProvider googleTokenAuthenticationProvider;
	private GoogleTokenFilter googleTokenFilter;

	public SecurityConfig(GoogleTokenAuthenticationProvider googleTokenAuthenticationProvider,
			GoogleTokenFilter googleTokenFilter) {
		super();
		this.googleTokenAuthenticationProvider = googleTokenAuthenticationProvider;
		this.googleTokenFilter = googleTokenFilter;
	}
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(googleTokenAuthenticationProvider);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf((csrf) -> csrf
					.disable()
					)
			.cors((cors) -> cors
					.configurationSource(corsConfigurationSource())
					)
			.authorizeHttpRequests((authorize) -> authorize
						.anyRequest().authenticated()
					)
			.addFilterBefore(googleTokenFilter, UsernamePasswordAuthenticationFilter.class);
			
//			.oauth2ResourceServer((oauth2) -> oauth2
//						.jwt(jwt -> {
//							jwt.jwtAuthenticationConverter(authenticationConverter());
//						})
					
		
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList( "GET" , "POST" , "OPTIONS" ));
		configuration.setAllowedHeaders(Arrays.asList("Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
	
//	private JwtAuthenticationConverter authenticationConverter() {
//		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//			System.out.println("JWT内容：" + jwt.getClaims());
//			return new ArrayList<>();
//		});
//		
//		return converter;
//	}
	
}
