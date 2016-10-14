package com.ozguryazilim.zoro.core.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.ozguryazilim.zoro.core.auth.entity.ZoroApplicationPermission;
import com.ozguryazilim.zoro.core.util.SessionUtility;

public abstract class AuthorizationController implements Serializable {
	private static final long		serialVersionUID	= -727884001811099601L;

	private Map<String, Boolean>	permissions			= new HashMap<String, Boolean>();

	@Inject
	private ZoroCredentials		credentials;

	@Inject
	private AuthorizationService	authorizationService;

	@Inject
	private SessionUtility			userSessionUtility;

	/**
	 * @return the accessMenu
	 */
	public boolean isAccessMenu() {
		return hasPermissionForApplication();
	}

	protected boolean hasPermissionFor(String permissionStr) {
		if (!credentials.isLoggedIn()) {
			return false;
		}
		else if (credentials.isAdmin()) {
			return true;
		}

		String applicationName = getApplicationName();
		String hashKey = applicationName + "_" + permissionStr;
		Boolean permission = permissions.get(hashKey);

		if (permission == null) {
			permission = authorizationService.isAuthorizedForFunction(credentials.getUsername(), applicationName, new ZoroApplicationPermission(permissionStr));

			permissions.put(hashKey, permission);
		}

		return permission;
	}

	private boolean hasPermissionForApplication() {
		if (!credentials.isLoggedIn()) {
			return false;
		}
		else if (credentials.isAdmin()) {
			return true;
		}

		String applicationName = getApplicationName();
		String hashKey = applicationName + "_accessMenu";
		Boolean permission = permissions.get(hashKey);

		if (permission == null) {
			List<String> authorizedAppsOfUser = userSessionUtility.getAuthorizedAppsOfUser();
			permission = authorizedAppsOfUser.contains(applicationName);

			permissions.put(hashKey, permission);
		}

		return permission;
	}

	protected abstract String getApplicationName();
}
