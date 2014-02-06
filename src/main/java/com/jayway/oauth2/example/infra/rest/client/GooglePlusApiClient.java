package com.jayway.oauth2.example.infra.rest.client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.jackson.JacksonFeature;

public class GooglePlusApiClient {
	private final WebTarget userInfoTarget;

	public GooglePlusApiClient() {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		userInfoTarget = client
				.target("https://www.googleapis.com/oauth2/v2/userinfo");
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> retrieveUserInfo(String token) {
		Map<String, String> response = userInfoTarget.request(APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).get(Map.class);
		return response;
	}
}
