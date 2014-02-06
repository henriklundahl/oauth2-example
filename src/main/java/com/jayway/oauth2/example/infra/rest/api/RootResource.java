package com.jayway.oauth2.example.infra.rest.api;

import static com.jayway.oauth2.example.infra.IoUtils.readTextResource;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.jayway.oauth2.example.infra.rest.client.GooglePlusApiClient;

@Path("")
public class RootResource {
	private final GooglePlusApiClient googlePlusApiClient;
	private final String indexPage;

	public RootResource(GooglePlusApiClient googlePlusApiClient)
			throws IOException {
		this.googlePlusApiClient = googlePlusApiClient;
		indexPage = readTextResource("/static/index.html");
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response root(@Context HttpServletRequest request,
			@Context UriInfo uriInfo) {
		String token = (String) request.getSession(false).getAttribute("token");
		try {
			String givenName = googlePlusApiClient.retrieveUserInfo(token).get(
					"given_name");
			return Response.ok(indexPage.replace("{{GIVEN_NAME}}", givenName))
					.build();
		} catch (NotAuthorizedException e) {
			return Response.seeOther(URI.create("/login")).build();
		}
	}
}
