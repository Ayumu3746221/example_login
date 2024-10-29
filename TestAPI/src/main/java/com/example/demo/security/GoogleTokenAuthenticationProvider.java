package com.example.demo.security;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleTokenAuthenticationProvider implements AuthenticationProvider{

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String token = (String) authentication.getCredentials();
		
		String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + token;
		Map<String, Object> response;
		
		try {
			
			response = restTemplate.getForObject(url, Map.class);
			if (response == null || response.containsKey("error")) {
				throw new BadCredentialsException("Token is invalid or expired");
			}
			
			String userId = (String) response.get("sub");
			String email = (String) response.get("email");
			
			return new UsernamePasswordAuthenticationToken(response.get("sub"), token , null);
			
		} catch (Exception e) {
			throw new BadCredentialsException("Failed to validate token", e);
		}
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
