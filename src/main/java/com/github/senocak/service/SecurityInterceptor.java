package com.github.senocak.service;

import com.github.senocak.entity.User;
import com.github.senocak.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class SecurityInterceptor implements ContainerRequestFilter {
    @Context SecurityContext securityCtx;
    @Inject JsonWebToken jwt;
    @Inject UserService userService;

    @Override
    public void filter(ContainerRequestContext context) {
        String userName;
        if (securityCtx.getUserPrincipal() != null && hasJwt()) {
            if (!securityCtx.getUserPrincipal().getName().equals(jwt.getName())) {
                throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
            } else {
                userName = securityCtx.getUserPrincipal().getName();
            }

            if (securityCtx.getAuthenticationScheme().equals("Bearer")) {
                try {
                    User userDetails = userService.findByUsername(userName);
                } catch (ServerException e) {
                    context.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("not auth").build());
                }
            }
        }
        //jwt.getClaim("sub").toString();
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}
