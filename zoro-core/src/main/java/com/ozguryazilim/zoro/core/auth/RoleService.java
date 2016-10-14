package com.ozguryazilim.zoro.core.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.ozguryazilim.zoro.core.auth.entity.ZoroRole;
import com.ozguryazilim.zoro.core.db.DBEntityManager;

@Singleton
@Named
public class RoleService implements Serializable {
	private static final long	serialVersionUID			= -7843186089028498548L;

	private static final String	ROLE_WITH_NAME_QUERY		= "SELECT r FROM ZoroRole r WHERE r.name = ?";
	private static final String	ROLES_OF_USERNAME_QUERY		= "SELECT r FROM ZoroRole r LEFT JOIN r.usernames as user WHERE user = ?";
	private static final String	APPLICATIONS_OF_USER_QUERY	= "SELECT app.applicationName FROM ZoroRole r LEFT JOIN r.applications as app LEFT JOIN r.usernames as user WHERE user = ?";

	@Inject
	private DBEntityManager		entityManager;

	@SuppressWarnings("unchecked")
	public boolean isRoleWithSameNameExist(ZoroRole newRole) {
		List<ZoroRole> rolesOfUser = (List<ZoroRole>) entityManager.executeParameterizedSelectQuery(ROLE_WITH_NAME_QUERY, new Object[] { newRole.getName() });

		return rolesOfUser.size() > 0;
	}

	/**
	 * Returns the roles of the user if there is no role for the user returns an
	 * empty list
	 * 
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ZoroRole> getRolesOfUser(String username) {
		if (username == null) {
			return new ArrayList<ZoroRole>();
		}

		List<ZoroRole> rolesOfUser = (List<ZoroRole>) entityManager.executeParameterizedSelectQuery(ROLES_OF_USERNAME_QUERY, new Object[] { username });

		return rolesOfUser;
	}

	/**
	 * Returns the all authorized apps of the user
	 * 
	 * @param username
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getAuthorizedAppsOfUser(String username) {
		List<String> authorizedApps = new ArrayList<String>();
		if (username != null) {
			List resultingList = entityManager.executeParameterizedSelectQuery(APPLICATIONS_OF_USER_QUERY, new Object[] { username });

			for (Object objAppName : resultingList) {
				authorizedApps.add((String) objAppName);
			}
		}

		return authorizedApps;
	}

}
