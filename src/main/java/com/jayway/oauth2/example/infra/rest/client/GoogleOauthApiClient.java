package com.jayway.oauth2.example.infra.rest.client;

import static javax.ws.rs.client.Entity.form;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

import org.glassfish.jersey.jackson.JacksonFeature;

public class GoogleOauthApiClient {
	private final String clientId;
	private final String clientSecret;
	private final WebTarget tokenTarget;

	public GoogleOauthApiClient(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		tokenTarget = client
				.target("https://accounts.google.com/o/oauth2/token");
	}

	public String authUrl() {
		return "https://accounts.google.com/o/oauth2/auth"
				+ "?redirect_uri=https%3A%2F%2Flocalhost%3A8443%2Foauth2%2Fcallback"
				+ "&response_type=code" //
				+ "&client_id=" + clientId //
				+ "&state={{NONCE}}"
				// + "&access_type=offline" //
				// + "&approval_prompt=force" //
				+ "&scope=email+profile";
	}

	@SuppressWarnings("unchecked")
	public String retrieveToken(String code) {
		Form f = new Form();
		f.param("code", code);
		f.param("redirect_uri", "https://localhost:8443/oauth2/callback");
		f.param("client_id", clientId);
		f.param("client_secret", clientSecret);
		f.param("grant_type", "authorization_code");

		Map<String, String> response = tokenTarget.request(APPLICATION_JSON)
				.post(form(f), Map.class);

		return response.get("access_token");
	}
}
