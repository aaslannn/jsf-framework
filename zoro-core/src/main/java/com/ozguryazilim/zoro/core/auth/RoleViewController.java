package com.ozguryazilim.zoro.core.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortMeta;

import com.ozguryazilim.zoro.core.JSFViewController;
import com.ozguryazilim.zoro.core.auth.entity.ZoroApplicationPermission;
import com.ozguryazilim.zoro.core.auth.entity.ZoroApplicationPermissionHolder;
import com.ozguryazilim.zoro.core.auth.entity.ZoroRole;
import com.ozguryazilim.zoro.core.loader.Application;
import com.ozguryazilim.zoro.core.loader.ModuleController;
import com.ozguryazilim.zoro.core.util.JSFUtility;
import com.ozguryazilim.zoro.core.util.MessageUtility;

@ViewScoped
@Named
public class RoleViewController extends JSFViewController<ZoroRole>
{
	private static final long								serialVersionUID		= -3682190973263488285L;

	@Inject
	private ModuleController								moduleController;

	@Inject
	private AuthorizationService							authorizationService;

	@Inject
	private AuthenticationService							authenticationService;

	@Inject
	private RoleService										roleService;

	private List<ZoroApplicationPermissionHolder>			applicationPermissions	= new ArrayList<ZoroApplicationPermissionHolder>();

	private Map<String, List<ZoroApplicationPermission>>	availablePermissionsMap	= new HashMap<String, List<ZoroApplicationPermission>>();

	private DualListModel<String>							usernamesModel			= new DualListModel<String>();

	private String											usernameFilter			= null;

	@Override
	protected void entityChanged()
	{
		super.entityChanged();

		updateApplicationPermissions();

		updateUsernamesModel();

		availablePermissionsMap = new HashMap<String, List<ZoroApplicationPermission>>();
	}

	public String getPermissionsAsStr(ZoroApplicationPermissionHolder permissionHolder)
	{
		List<ZoroApplicationPermission> permissions = permissionHolder.getPermissions();
		if (permissions == null)
		{
			return "";
		}

		List<String> bundles = new ArrayList<String>();
		for (ZoroApplicationPermission permission : permissions)
		{
			bundles.add(MessageUtility.get("role", permission.getName()));
		}

		return StringUtils.join(bundles.toArray(), ",");
	}

	public List<ZoroApplicationPermission> getAvailablePermissions(String applicationName)
	{
		List<ZoroApplicationPermission> availableApplications = availablePermissionsMap.get(applicationName);
		if (availableApplications == null)
		{
			availableApplications = new ArrayList<ZoroApplicationPermission>();
			Application application = authorizationService.getApplication(applicationName);

			if (application != null)
			{
				for (ZoroApplicationPermission permission : application.getAvailablePermissions())
				{
					availableApplications.add(new ZoroApplicationPermission(permission.getName()));
				}
			}

			availablePermissionsMap.put(applicationName, availableApplications);
		}

		return availableApplications;
	}

	/**
	 * Initializes the all application permission list for the current entity
	 */
	private void updateApplicationPermissions()
	{
		applicationPermissions = new ArrayList<ZoroApplicationPermissionHolder>();

		if (entity != null)
		{
			List<ZoroApplicationPermissionHolder> permittedApplications = entity.getApplications();

			for (Application application : moduleController.getApplications())
			{
				ZoroApplicationPermissionHolder applicationPermission = getPermittedApplications(permittedApplications, application);
				if (applicationPermission == null)
				{
					applicationPermission = new ZoroApplicationPermissionHolder();
					applicationPermission.setApplicationName(application.getName());
				}

				applicationPermissions.add(applicationPermission);
			}
		}
	}

	/**
	 * Initializes the username items for the view
	 */
	private void updateUsernamesModel()
	{
		if (entity != null)
		{
			usernameFilter = null;

			LDAPLazyDataModel ldapLazyDataModel = authenticationService.getLdapDataModel();
			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("entity", getUsernameFilter());

			usernamesModel = new DualListModel<String>();

			List<String> copyLdapUsers = new ArrayList<String>();
			copyLdapUsers.addAll(ldapLazyDataModel.load(0, 1000, new ArrayList<SortMeta>(), filters));

			for (String target : entity.getUsernames())
			{
				copyLdapUsers.remove(target);
			}

			usernamesModel.setSource(copyLdapUsers);
			usernamesModel.setTarget(entity.getUsernames());
		}
	}

	public void filterLdapUsers()
	{
		List<String> targetUsernames = usernamesModel.getTarget();
		LDAPLazyDataModel ldapLazyDataModel = authenticationService.getLdapDataModel();
		HashMap<String, String> filters = new HashMap<String, String>();
		filters.put("entity", getUsernameFilter());

		List<String> copyLdapUsers = new ArrayList<String>();
		copyLdapUsers.addAll(ldapLazyDataModel.load(0, 1000, new ArrayList<SortMeta>(), filters));

		for (String target : targetUsernames)
		{
			copyLdapUsers.remove(target);
		}

		usernamesModel.setSource(copyLdapUsers);
	}

	/**
	 * Returns the permitted application permission instance for the given
	 * application if exists otherwise returns null
	 * 
	 * @param permittedApplications
	 * @param application
	 * @return
	 */
	private ZoroApplicationPermissionHolder getPermittedApplications(List<ZoroApplicationPermissionHolder> permittedApplications, Application application)
	{
		for (ZoroApplicationPermissionHolder permittedApp : permittedApplications)
		{
			if (permittedApp.getApplicationName().equals(application.getName()))
			{
				return permittedApp;
			}
		}

		return null;
	}

	@Override
	public boolean isValid()
	{
		if (!editMode)
		{
			if (roleService.isRoleWithSameNameExist(entity))
			{
				JSFUtility.addErrorMessage(null, MessageUtility.get("role", "errorSameName"));
				return false;
			}
		}

		if (usernamesModel.getTarget().size() == 0)
		{
			JSFUtility.addErrorMessage(null, MessageUtility.get("role", "errorUserSelection"));
			return false;
		}

		/**
		 * Add the menu access permitted applications to the roles list
		 */
		List<ZoroApplicationPermissionHolder> rolePermittedApplications = entity.getApplications();
		rolePermittedApplications.clear();
		for (ZoroApplicationPermissionHolder applicationPermission : applicationPermissions)
		{
			if (applicationPermission.isAccessMenu())
			{
				rolePermittedApplications.add(applicationPermission);
			}
		}
		entity.setUsernames(usernamesModel.getTarget());

		return true;
	}

	/**
	 * @return the applicationPermissions
	 */
	public List<ZoroApplicationPermissionHolder> getApplicationPermissions()
	{
		return applicationPermissions;
	}

	/**
	 * @param applicationPermissions
	 *            the applicationPermissions to set
	 */
	public void setApplicationPermissions(List<ZoroApplicationPermissionHolder> applicationPermissions)
	{
		this.applicationPermissions = applicationPermissions;
	}

	/**
	 * @return the usernamesModel
	 */
	public DualListModel<String> getUsernamesModel()
	{
		return usernamesModel;
	}

	/**
	 * @param usernamesModel
	 *            the usernamesModel to set
	 */
	public void setUsernamesModel(DualListModel<String> usernamesModel)
	{
		this.usernamesModel = usernamesModel;
	}

	/**
	 * @return the usernameFilter
	 */
	public String getUsernameFilter()
	{
		return usernameFilter;
	}

	/**
	 * @param usernameFilter
	 *            the usernameFilter to set
	 */
	public void setUsernameFilter(String usernameFilter)
	{
		this.usernameFilter = usernameFilter;
	}
}
