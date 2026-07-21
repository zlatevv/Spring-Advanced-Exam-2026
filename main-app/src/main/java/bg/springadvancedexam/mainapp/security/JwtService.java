package bg.springadvancedexam.mainapp.security;

import bg.springadvancedexam.mainapp.exception.security.InvalidResetTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("purpose", "password-reset")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractResetEmail(String token) {
        Claims claims = parseClaims(token);
        if (!"password-reset".equals(claims.get("purpose"))) {
            throw new InvalidResetTokenException("Invalid reset token.");
        }
        return claims.getSubject();
    }
}
