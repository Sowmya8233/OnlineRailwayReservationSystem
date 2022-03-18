package com.example.demo.Service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.util.JwtUtils;

import io.jsonwebtoken.Claims;




@Service
public class JwtFilterReq extends OncePerRequestFilter{

	@Autowired
	private JwtUtils jwtUtil;
	
	@Autowired
	private AdminService Adminservice;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader= request.getHeader("Authorization");
		
		String username=null;
		String jwtToken =null;
		String role =null;
		
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken= authorizationHeader.substring(7);
			username=jwtUtil.extractUsername(jwtToken);
			Claims claims=jwtUtil.getAllClaimsFromToken(jwtToken);
			//System.out.println(claims.toString());
			
			role = claims.get("Role").toString();
			
			}
		if (username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails currentUserDetails=Adminservice.loadUserByUsername(username);
			Boolean token= jwtUtil.validateToken(jwtToken, currentUserDetails);
			if (token) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
					new UsernamePasswordAuthenticationToken(currentUserDetails, null, currentUserDetails
							.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
					.buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}