package com.ozguryazilim.zoro.core.loader;

import java.io.Serializable;
import java.util.List;

import com.ozguryazilim.zoro.core.auth.AuthorizationService;
import com.ozguryazilim.zoro.core.auth.entity.ZoroApplicationPermission;

public interface IApplicationFunctionalityAuthorizer extends Serializable {
	/**
	 * For the given url checks whether the current user has the access
	 * priviledge
	 * 
	 * @param url
	 * @return
	 */
	public boolean isAuthorizedForURL(AuthorizationService authorizationService, String username, String url);

	/**
	 * Returns all the possible permission type for the application
	 * 
	 * @return
	 */
	public List<ZoroApplicationPermission> getAvailablePermissions();
}
