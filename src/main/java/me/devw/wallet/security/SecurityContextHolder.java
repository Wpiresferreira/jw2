package me.devw.wallet.security;

import java.security.Principal;
import java.util.Set;

public class SecurityContextHolder {

    private static final ThreadLocal<UserPrincipal> currentUser = new ThreadLocal<>();

    public static void set(UserPrincipal user) {
        currentUser.set(user);
    }

    public static UserPrincipal get() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }

    public record UserPrincipal(String userId, String username, Set<String> roles)
            implements Principal {
        @Override
        public String getName() {
            return username;
        }
    }
}