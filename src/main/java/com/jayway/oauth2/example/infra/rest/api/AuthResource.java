package com.jayway.oauth2.example.infra.rest.api;

import static com.jayway.oauth2.example.infra.rest.IoUtils.readBinaryResource;
import static com.jayway.oauth2.example.infra.rest.IoUtils.readTextResource;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jayway.oauth2.example.infra.rest.client.GoogleOauthApiClient;

@Path("")
public class AuthResource {
	private final GoogleOauthApiClient googleOauthApiClient;
	private final String loginPage;
	private final byte[] loginButton;

	public AuthResource(GoogleOauthApiClient googleOauthApiClient)
			throws IOException {
		this.googleOauthApiClient = googleOauthApiClient;
		loginPage = readTextResource("/static/login.html").replace(
				"{{AUTH_URL}}", googleOauthApiClient.authUrl());
		loginButton = readBinaryResource("/static/images/Red-signin_Long_base_44dp.png");
	}

	@GET
	@Path("login")
	@Produces(MediaType.TEXT_HTML)
	public String login() {
		return loginPage;
	}

	@GET
	@Path("login/login_button.png")
	@Produces("image/png")
	public byte[] loginButtonImage() {
		return loginButton;
	}

	@GET
	@Path("oauth2/callback")
	public Response callback(@Context HttpServletRequest request,
			@QueryParam("code") String code, @QueryParam("error") String error) {
		if (code == null || error != null) {
			// TODO Add error message?
			return Response.seeOther(URI.create("/login")).build();
		} else {
			String token = googleOauthApiClient.retrieveToken(code);
			request.getSession().setAttribute("token", token);
			return Response.seeOther(URI.create("")).build();
		}
	}
}
