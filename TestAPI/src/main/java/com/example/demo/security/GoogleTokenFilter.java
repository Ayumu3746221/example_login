package com.example.demo.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GoogleTokenFilter extends OncePerRequestFilter{

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader("Authorization");
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			
			try {
				//GoogleのAPIに問い合わせてトークンを検証
				String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + token;
				Map<String, Object> tokenInfo = restTemplate.getForObject(url, Map.class);
				
				if (tokenInfo == null || tokenInfo.containsKey("error")) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
				
				String userId = (String) tokenInfo.get("sub");
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null, null);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		
		}
		
		filterChain.doFilter(request, response);
	}
}
