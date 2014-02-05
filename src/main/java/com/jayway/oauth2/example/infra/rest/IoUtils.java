package com.jayway.oauth2.example.infra.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class IoUtils {
	public static byte[] readBinaryResource(String absoluteResourcePath)
			throws IOException {
		InputStream input = IoUtils.class
				.getResourceAsStream(absoluteResourcePath);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int count;
		byte[] buffer = new byte[1024];
		while ((count = input.read(buffer)) != -1)
			output.write(buffer, 0, count);
		return output.toByteArray();
	}

	public static String readTextResource(String absoluteResourcePath)
			throws IOException {
		InputStreamReader reader = new InputStreamReader(
				IoUtils.class.getResourceAsStream(absoluteResourcePath));
		StringWriter writer = new StringWriter();
		int count;
		char[] buffer = new char[1024];
		while ((count = reader.read(buffer)) != -1)
			writer.write(buffer, 0, count);
		return writer.toString();
	}
}
