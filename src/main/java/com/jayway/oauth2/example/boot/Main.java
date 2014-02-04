package com.jayway.oauth2.example.boot;

import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;

import java.security.KeyStore;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.jayway.oauth2.example.infra.rest.RootResource;

public class Main {
	public static void main(String[] args) throws Exception {
		Server s = new Server();

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
		s.setConnectors(new Connector[] { connector });

		ServletContextHandler context = new ServletContextHandler(SESSIONS);
		context.setContextPath("/rest");
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(JacksonFeature.class);
		resourceConfig.register(RootResource.class);
		ServletHolder servletHolder = new ServletHolder(new ServletContainer(
				resourceConfig));
		servletHolder.setInitOrder(1);
		context.addServlet(servletHolder, "/*");

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase("static");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { context, resourceHandler,
				new DefaultHandler() });
		s.setHandler(handlers);

		s.start();
		s.join();
	}
}
