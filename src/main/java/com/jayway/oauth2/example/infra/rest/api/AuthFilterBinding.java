package com.jayway.oauth2.example.infra.rest.api;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class AuthFilterBinding implements DynamicFeature {
	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		Class<?> resourceClass = resourceInfo.getResourceClass();
		if (RootResource.class.equals(resourceClass))
			context.register(AuthFilter.class);
	}
}
