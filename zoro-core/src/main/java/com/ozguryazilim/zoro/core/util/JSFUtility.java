package com.ozguryazilim.zoro.core.util;

import java.util.Map;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class JSFUtility
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getBean(Class clazz)
	{
		BeanManager beanManager = (BeanManager) ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
				.getAttribute("javax.enterprise.inject.spi.BeanManager");

		Bean bean = (Bean) beanManager.getBeans(clazz).iterator().next();
		CreationalContext ctx = beanManager.createCreationalContext(bean);
		return beanManager.getReference(bean, clazz, ctx);
	}

	public static Map<String, Object> getSessionMap()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	}

	public static void addErrorMessage(String component, String message)
	{
		addMessage(component, message, FacesMessage.SEVERITY_ERROR);
	}

	public static void addInfoMessage(String component, String message)
	{
		addMessage(component, message, FacesMessage.SEVERITY_INFO);
	}

	public static void addWarnMessage(String component, String message)
	{
		addMessage(component, message, FacesMessage.SEVERITY_WARN);
	}

	public static void addMessage(String component, String message, Severity severity)
	{
		FacesMessage facesMessage = new FacesMessage(severity, message, null);

		FacesContext.getCurrentInstance().addMessage(component, facesMessage);
	}
}
