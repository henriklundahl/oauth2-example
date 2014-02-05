package com.jayway.oauth2.example.boot;

import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.jayway.oauth2.example.infra.rest.api.AuthFilterBinding;
import com.jayway.oauth2.example.infra.rest.api.AuthResource;
import com.jayway.oauth2.example.infra.rest.api.RootResource;
import com.jayway.oauth2.example.infra.rest.client.GoogleOauthApiClient;
import com.jayway.oauth2.example.infra.rest.client.GooglePlusApiClient;

public class Main {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out
					.println("Usage:\n\n<program> googleClientId googleClientSecret");
		}
		String googleClientId = args[0];
		String googleClientSecret = args[1];

		Server s = new Server();

		s.setConnectors(new Connector[] { createSslConnector(s) });

		ServletContextHandler context = new ServletContextHandler(SESSIONS);
		context.setContextPath("/");
		ServletHolder servletHolder = new ServletHolder(createJerseyServlet(
				googleClientId, googleClientSecret));
		servletHolder.setInitOrder(1);
		context.addServlet(servletHolder, "/*");
		s.setHandler(context);

		s.start();
		s.join();
	}

	private static ServerConnector createSslConnector(Server s)
			throws KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(Main.class.getResourceAsStream("/localhost.jks"),
				"localhost".toCharArray());
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStore(keyStore);
		sslContextFactory.setKeyManagerPassword("localhost");
		sslContextFactory.setTrustAll(true);
		ServerConnector connector = new ServerConnector(s, sslContextFactory);
		connector.setHost("localhost");
		connector.setPort(8443);
		return connector;
	}

	private static ServletContainer createJerseyServlet(String googleClientId,
			String googleClientSecret) throws IOException {
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(new AuthResource(new GoogleOauthApiClient(
				googleClientId, googleClientSecret)));
		resourceConfig.register(new RootResource(new GooglePlusApiClient()));
		resourceConfig.register(AuthFilterBinding.class);
		resourceConfig.register(JacksonFeature.class);
		ServletContainer servletContainer = new ServletContainer(resourceConfig);
		return servletContainer;
	}
}
