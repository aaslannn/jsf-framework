package com.ozguryazilim.zoro.core.util;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.ozguryazilim.zoro.core.auth.RoleService;
import com.ozguryazilim.zoro.core.auth.ZoroCredentials;
import com.ozguryazilim.zoro.core.auth.entity.ZoroRole;

@Named
@SessionScoped
public class SessionUtility implements Serializable {
	private static final long	serialVersionUID	= -2025739553035094071L;

	public static final Locale	TR_LOCALE			= new Locale("tr", "TR");

	private Locale				locale				= TR_LOCALE;

	private List<ZoroRole>		userRoles			= null;

	private List<String>		userApplications	= null;

	@Inject
	private MenuUtility			menuUtility;

	@Inject
	private RoleService			roleService;

	@Inject
	private ZoroCredentials		zoroCredentials;

	public List<ZoroRole> getRolesOfUser() {
		if (userRoles == null) {
			userRoles = roleService.getRolesOfUser(zoroCredentials.getUsername());
		}

		return userRoles;
	}

	public List<String> getAuthorizedAppsOfUser() {
		if (userApplications == null) {
			userApplications = roleService.getAuthorizedAppsOfUser(zoroCredentials.getUsername());
		}

		return userApplications;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String setTurkishLocale() {
		locale = TR_LOCALE;

		clearSession();

		return null;
	}

	public String setEnglishLocale() {
		locale = Locale.US;

		clearSession();

		return null;
	}

	public boolean isTurkishLocale() {
		return !locale.equals(Locale.US);
	}

	public void clearSession() {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		menuUtility.reConstructMenu();
	}
}
