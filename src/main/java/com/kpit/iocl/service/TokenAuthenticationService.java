package com.kpit.iocl.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security
            .authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.stream.Collectors;


import java.util.Arrays;
import java.util.Collection;

public class TokenAuthenticationService {
  static final long EXPIRATIONTIME = 86400000; // 1 day
  static final String SECRET = "secret";
  static final String TOKEN_PREFIX = "Bearer";
  public static final String HEADER_STRING = "Authorization";
  static final String AUTHORITIES_KEY = "auth";
  
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationService.class);
    
  public static void addAuthentication(HttpServletResponse res, Authentication auth) {
	  String authorities = auth.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.joining(","));
	  
    String JWT = Jwts.builder()
        .setSubject(auth.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
  }

  public static Authentication getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);
    if (token != null) {
      // parse the token.
    	try {
      String user = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
          .getBody()
          .getSubject();

      Claims claims = Jwts.parser()
              .setSigningKey(SECRET)
              .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
              .getBody();

      Collection<? extends GrantedAuthority> authorities =
              Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                  .map(SimpleGrantedAuthority::new)
                  .collect(Collectors.toList());
      
      User principal = new User(claims.getSubject(), "", authorities);
      
      return user != null ?
          //new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
    	   new UsernamePasswordAuthenticationToken(principal, token, authorities) :
          null;
    	} catch (SignatureException e) {
      	  LOGGER.info("Invalid JWT signature.");
      	  LOGGER.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
      	  LOGGER.info("Invalid JWT token.");
      	  LOGGER.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
      	  LOGGER.info("Expired JWT token.");
      	  LOGGER.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
      	  LOGGER.info("Unsupported JWT token.");
      	  LOGGER.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
      	  LOGGER.info("JWT token compact of handler are invalid.");
      	  LOGGER.trace("JWT token compact of handler are invalid trace: {}", e);
        }
    }
    return null;
  }
}