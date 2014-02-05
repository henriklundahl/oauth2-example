package com.jayway.oauth2.example.infra.rest.api;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class AuthFilter implements ContainerRequestFilter {
	@Context
	private HttpServletRequest webRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		if (!isAuthenticated(webRequest))
			requestContext.abortWith(Response.seeOther(URI.create("/login"))
					.build());
	}

	private boolean isAuthenticated(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session != null && session.getAttribute("token") != null;
	}
}
