package com.ozguryazilim.zoro.core.util;

import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@ApplicationScoped
public class MessageUtility implements Serializable
{
	private static final long	serialVersionUID	= 355347307207150672L;
	private static final String	BUNDLE_PREFIX		= "messages/";

	public static ResourceBundle getBundle(String baseName, Locale locale)
	{
		return ResourceBundle.getBundle(BUNDLE_PREFIX + baseName, locale);
	}

	public static String get(String bundleName, String key, Locale locale)
	{
		String value = null;

		try
		{
			value = getBundle(bundleName, locale).getString(key);
		}
		catch (MissingResourceException e)
		{
			value = "???" + key + "???";
		}

		return value;
	}

	public static String get(String bundleName, String key)
	{
		return get(bundleName, key, getLocale());
	}

	public static Locale getLocale()
	{
		if (FacesContext.getCurrentInstance() != null)
		{
			return FacesContext.getCurrentInstance().getViewRoot().getLocale();
		}

		return Locale.US;
	}
}