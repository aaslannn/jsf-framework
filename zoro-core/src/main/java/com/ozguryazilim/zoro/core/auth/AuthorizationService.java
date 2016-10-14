package com.ozguryazilim.zoro.core.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.ozguryazilim.zoro.core.auth.entity.ZoroApplicationPermission;
import com.ozguryazilim.zoro.core.auth.entity.ZoroApplicationPermissionHolder;
import com.ozguryazilim.zoro.core.auth.entity.ZoroRole;
import com.ozguryazilim.zoro.core.db.DBEntityManager;
import com.ozguryazilim.zoro.core.loader.Application;
import com.ozguryazilim.zoro.core.loader.ModuleController;
import com.ozguryazilim.zoro.core.util.SessionUtility;

@Singleton
@Named
public class AuthorizationService implements Serializable {
	public static final String	APPLICATION_PERMISSIONS_QUERY	= "SELECT holder.permissions FROM ZoroApplicationPermissionHolder holder WHERE holder.id = ?";

	private static final long	serialVersionUID				= -4056582541119391316L;

	public static final String	ROLE_BACKUP_FILE				= "roles";

	@Inject
	private ModuleController	moduleController;

	@Inject
	private SessionUtility		userSessionUtility;

	@Inject
	private DBEntityManager		entityManager;

	/**
	 * Returns the application with given name
	 * 
	 * @param name
	 * @return
	 */
	public Application getApplication(String name) {
		List<Application> applications = moduleController.getApplications();
		for (Application application : applications) {
			if (application.getName().equals(name)) {
				return application;
			}
		}

		return null;
	}

	/**
	 * Checks whether the given username has access to given url
	 * 
	 * @param username
	 * @param url
	 * @return
	 */
	public boolean isAuthorizedFor(String username, String url) {
		if (url.equals("/index.xhtml") || url.equals("/statusLog.xhtml") || url.equals("/systemChanges.xhtml")) {
			return true;
		}
		else if (url.equals("/preRule.xhtml")) {
			url = "/labeling.xhtml";
		}

		List<String> authorizedApps = userSessionUtility.getAuthorizedAppsOfUser();
		if (authorizedApps != null) {
			for (Application application : moduleController.getApplications()) {
				if (application.getUrl().equals(url) || application.getAuthorizer().isAuthorizedForURL(this, username, url)) {
					return authorizedApps.contains(application.getName());
				}
			}
		}

		return false;
	}

	/**
	 * Checks whether the given username has access to functionality for the
	 * given application
	 * 
	 * @param username
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isAuthorizedForFunction(String username, String applicationName, ZoroApplicationPermission permissionType) {
		List<ZoroApplicationPermissionHolder> applicationPermissions = getApplicationPermissionsFor(username, applicationName);
		for (ZoroApplicationPermissionHolder authorizedApplication : applicationPermissions) {
			List<ZoroApplicationPermission> permissions = entityManager.executeParameterizedSelectQuery(APPLICATION_PERMISSIONS_QUERY,
					new Object[] { authorizedApplication.getId() });
			for (ZoroApplicationPermission permission : permissions) {
				if (permission.equals(permissionType)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns the permissions of the user for the given application name
	 * gathered from all roles of the user
	 * 
	 * @param username
	 * @param applicationName
	 * @return
	 */
	public List<ZoroApplicationPermissionHolder> getApplicationPermissionsFor(String username, String applicationName) {
		List<ZoroApplicationPermissionHolder> searchedApplications = new ArrayList<ZoroApplicationPermissionHolder>();
		List<ZoroRole> rolesOfUser = userSessionUtility.getRolesOfUser();

		for (ZoroRole role : rolesOfUser) {
			List<ZoroApplicationPermissionHolder> allApplications = role.getApplications();
			for (ZoroApplicationPermissionHolder authorizedApplication : allApplications) {
				if (authorizedApplication.getApplicationName().equals(applicationName)) {
					searchedApplications.add(authorizedApplication);
				}
			}
		}

		return searchedApplications;
	}
}
