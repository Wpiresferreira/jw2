package me.devw.wallet.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import me.devw.wallet.entity.User;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    private static final String SECRET = "my-super-secret-key-my-super-secret-key";

    public String generate(User user) {

        if (user == null) {
        return Jwts.builder()
                    .subject("")
                    .claim("upn", "")
                    .claim("groups", Set.of(""))
                    .issuer("wallet")
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis()))
                    .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .compact();
        }

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("upn", user.getUsername())
                .claim("groups", Set.of(user.getRole().name()))
                .issuer("wallet")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24* 365))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}