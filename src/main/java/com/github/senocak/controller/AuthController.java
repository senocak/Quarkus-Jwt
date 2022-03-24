package com.github.senocak.controller;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import com.github.senocak.dto.auth.UserResponse;
import com.github.senocak.dto.auth.UserWrapperResponse;
import com.github.senocak.entity.User;
import com.github.senocak.exception.ServerException;
import com.github.senocak.dto.AuthRequest;
import com.github.senocak.service.DtoConverter;
import com.github.senocak.service.JsonSchemaValidator;
import com.github.senocak.service.UserService;
import com.github.senocak.util.OmaErrorMessageType;
import com.github.senocak.util.PBKDF2Encoder;
import com.github.senocak.util.TokenUtils;
import com.github.senocak.util.AppConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path(AuthController.URL)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
	public static final String URL = "/api/v1/auth";
	@Inject
	UserService userService;
	@Inject PBKDF2Encoder passwordEncoder;
	@Inject JsonSchemaValidator jsonSchemaValidator;

	@ConfigProperty(name = "jwt.duration") Long duration;
	@ConfigProperty(name = "jwt.issuer") String issuer;

	@POST
	@PermitAll
	@Path("/login")
	public Response login(AuthRequest authRequest) throws ServerException {
		validateJsonSchema(authRequest, AuthRequest.class);
		User user = userService.findByUsernameAndPassword(authRequest.username, passwordEncoder.encode(authRequest.password));
		UserResponse login = DtoConverter.convertEntityToDto(user);
		String generatedToken;
		try {
			generatedToken = TokenUtils.generateToken(user.getUsername(), user.getRoles(), duration, issuer);
		} catch (Exception e) {
			throw new ServerException(OmaErrorMessageType.GENERIC_SERVICE_ERROR,
					new String[]{"Exception occurred during generating token", e.getMessage()},
					Response.Status.INTERNAL_SERVER_ERROR);
		}

		UserWrapperResponse userWrapperResponse = new UserWrapperResponse();
		userWrapperResponse.setUserResponse(login);
		userWrapperResponse.setToken(generatedToken);

		return Response.ok(userWrapperResponse).build();
	}


	@GET
	@Path("/user")
	@RolesAllowed(AppConstants.USER)
	public String user(@Context SecurityContext ctx) {
		return "user";
	}

	@GET
	@Path("/admin")
	@RolesAllowed("ADMIN")
	public Response admin() {
		return Response.ok("Content for admin").build();
	}

	/**
	 * @param object -- an Object to validate
	 * @param cls -- a Class to be validated
	 * @throws ServerException -- throws ServerException
	 */
	private void validateJsonSchema(Object object, Class<?> cls) throws ServerException {
		jsonSchemaValidator.validateJsonSchema(object, cls);
	}
}
