package me.devw.wallet.security;

import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SecurityCleanupFilter implements ContainerResponseFilter {

    @Override
    public void filter(jakarta.ws.rs.container.ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {
        SecurityContextHolder.clear();
    }
}