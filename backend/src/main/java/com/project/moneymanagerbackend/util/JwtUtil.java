package com.project.moneymanagerbackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * <p>
 * Provides methods to generate, validate, and extract information from JWTs.
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing and validating JWTs.
     * <p>
     * Injected from application properties: {@code jwt.secret}.
     * Should be a sufficiently long random string (at least 256 bits for HS256).
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT token
     * @return the username/email embedded in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token using a resolver function.
     *
     * @param token the JWT token
     * @param claimsResolver function to resolve a specific claim
     * @param <T> type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT token and retrieves all claims.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates a JWT token against a given {@link UserDetails}.
     *
     * @param token the JWT token
     * @param userDetails the user details to validate against
     * @return true if the token is valid and matches the user, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Generates a new JWT token for a given email.
     *
     * @param email the email to be set as the token's subject
     * @return the signed JWT token
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
