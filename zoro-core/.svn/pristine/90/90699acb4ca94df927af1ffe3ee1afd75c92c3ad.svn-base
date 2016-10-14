package com.ozguryazilim.zoro.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import com.ozguryazilim.zoro.core.auth.AuthorizationService;
import com.ozguryazilim.zoro.core.auth.ZoroCredentials;
import com.ozguryazilim.zoro.core.loader.Application;
import com.ozguryazilim.zoro.core.loader.ModuleController;

@Named
@SessionScoped
public class MenuUtility implements Serializable {
	private static final long		serialVersionUID	= -692683044230738796L;

	@Inject
	private AuthorizationService	authorizationService;

	@Inject
	private ZoroCredentials			credentials;

	@Inject
	private ModuleController		moduleController;

	private MenuModel				model;

	@PostConstruct
	public void postConstruct() {
		reConstructMenu();
	}

	public void reConstructMenu() {
		if (!credentials.isLoggedIn()) {
			return;
		}
		model = new DefaultMenuModel();

		if (credentials.isAdmin()) {
			Submenu administrationMenu = new Submenu();
			administrationMenu.setLabel(MessageUtility.get("default", "administration"));
			model.addSubmenu(administrationMenu);

			MenuItem authorization = new MenuItem();
			authorization.setUrl("/role/role.xhtml");
			authorization.setValue(MessageUtility.get("default", "roleManagement"));
			administrationMenu.getChildren().add(authorization);

			MenuItem systemSettings = new MenuItem();
			systemSettings.setUrl("/settings/settings.xhtml");
			systemSettings.setValue(MessageUtility.get("default", "settings"));
			administrationMenu.getChildren().add(systemSettings);

		}

		Submenu applicationsMenu = new Submenu();
		applicationsMenu.setLabel(MessageUtility.get("default", "applications"));

		List<MenuItem> applications = getApplications();
		applicationsMenu.getChildren().addAll(applications);

		if (applicationsMenu.getChildCount() > 0) {
			model.addSubmenu(applicationsMenu);
		}
	}

	public List<MenuItem> getApplications() {
		List<Application> applications = moduleController.getApplications();
		List<MenuItem> items = new ArrayList<MenuItem>();

		Collections.sort(applications, new Comparator<Application>() {

			@Override
			public int compare(Application o1, Application o2) {
				return ((Integer) o1.getOrder()).compareTo((Integer) o2.getOrder());
			}
		});

		if (applications.size() > 0) {
			Submenu applicationsMenu = new Submenu();
			applicationsMenu.setLabel(MessageUtility.get("default", "applications"));

			for (Application application : applications) {
				if (credentials.isAdmin() || authorizationService.isAuthorizedFor(credentials.getUsername(), application.getUrl())) {
					MenuItem twitter = new MenuItem();
					twitter.setUrl(application.getUrl());
					twitter.setValue(MessageUtility.get(application.getName(), application.getName()));

					items.add(twitter);
				}
			}
		}

		return items;
	}

	public String getContextPath() {
		return ((javax.servlet.http.HttpServletRequest) javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
	}

	/**
	 * @return the model
	 */
	public MenuModel getModel() {
		if (model == null) {
			reConstructMenu();
		}

		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(MenuModel model) {
		this.model = model;
	}
}
