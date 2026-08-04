package me.devw.wallet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    private static final String SECRET = "my-super-secret-key-my-super-secret-key";
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        System.out.println("Path: " + path);
        // ROTAS PÚBLICAS — NÃO BLOQUEAR
        if (path.equals("/auth/signup") || path.equals("/auth/login") || path.equals("auth/logout")) {
            return;
        }

        // Se quiser permitir também OPTIONS:
        if (requestContext.getMethod().equals("OPTIONS")) {
            return;
        }

        System.out.println("Cookies recebidos:");
        requestContext.getCookies().forEach((k, v) ->
                System.out.println("COOKIE: " + k + " = " + v.getValue())
        );

        System.out.println("JwtAuthFilter executed");

        var cookie = requestContext.getCookies().get("access_token");
        if (cookie == null || cookie.getValue().isBlank()) {
            System.out.println("token: "+ "bloqueado");

            return; // ou abortWith(401) se quiser bloquear tudo
        }

        String token = cookie.getValue();
        System.out.println("token: "+ token);


        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.getSubject();
            String username = claims.get("upn", String.class);

            List<String> groups = claims.get("groups", List.class);
            if (groups == null) groups = List.of();

            SecurityContextHolder.set(
                    new SecurityContextHolder.UserPrincipal(
                            userId,
                            username,
                            Set.copyOf(groups)
                    )
            );

            System.out.println("User autenticated: " + username);

        } catch (Exception e) {
            e.printStackTrace();

            requestContext.abortWith(
                    jakarta.ws.rs.core.Response.status(401)
                            .entity("Invalid or expired token")
                            .build()
            );
        }
    }
}