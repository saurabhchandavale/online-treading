package com.example.demo.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		if(jwt != null) {
			//bearer token
			jwt = jwt.substring(7);
			
			try {
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRETE_KEY.getBytes());
				Claims claim = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				String email = String.valueOf(claim.get("email"));
				System.out.println("SCE32 : email -> " + email);
				String authorities = String.valueOf(claim.get("authorities"));
				List<GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication auth = new UsernamePasswordAuthenticationToken(email, null,authoritiesList);
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
				throw new RuntimeException("Invalid Token...");
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
