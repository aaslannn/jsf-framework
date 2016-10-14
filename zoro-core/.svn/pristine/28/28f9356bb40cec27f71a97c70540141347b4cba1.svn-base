package com.ozguryazilim.zoro.core.util;

import java.util.Locale;

import javax.faces.context.FacesContext;

import com.sun.faces.application.view.MultiViewHandler;

public class CustomViewHandler extends MultiViewHandler {

	@Override
	public Locale calculateLocale(FacesContext context) {
		SessionUtility utility = (SessionUtility) JSFUtility.getBean(SessionUtility.class);
		return utility.getLocale();
	}

}