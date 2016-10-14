package com.ozguryazilim.zoro.core.util;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("uiUtility")
@ApplicationScoped
public class UIUtility implements Serializable
{
	private static final long	serialVersionUID	= -540939553035094071L;

	public String getWidgetVar(String clientId)
	{
		return clientId.replaceAll(":", "_");
	}
}
