package com.jayway.oauth2.example.infra.rest.api;

import static com.jayway.oauth2.example.infra.rest.IoUtils.readTextResource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
	public String root(@Context HttpServletRequest request,
			@Context UriInfo uriInfo) {
		String token = (String) request.getSession(false).getAttribute("token");
		String userName = googlePlusApiClient.retrieveUserInfo(token).get(
				"name");
		return indexPage.replace("{{USER_NAME}}", userName);
	}
}
