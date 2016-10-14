package com.ozguryazilim.zoro.core;

import java.net.URL;

import com.sun.faces.facelets.impl.DefaultResourceResolver;

public class FaceletsResourceResolver extends DefaultResourceResolver {

	public FaceletsResourceResolver() {
		super();
	}

	public URL resolveUrl(String path) {
		URL url = super.resolveUrl(path);
		if (url == null && path != null) {
			url = getClass().getResource("/META-INF/resources/pages" + path);
		}

		return url;
	}
}
