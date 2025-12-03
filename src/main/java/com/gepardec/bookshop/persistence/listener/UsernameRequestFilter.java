package com.gepardec.bookshop.persistence.listener;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class UsernameRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext ctx) {
        String username = ctx.getHeaderString("username");
        if (username == null) {
            username = "unknown";
        }
        UsernameHolder.set(username);
    }
}
