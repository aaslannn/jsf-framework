package com.ozguryazilim.zoro.core.auth.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Entity
@Table(name = "PERMISSION_HOLDER")
public class ZoroApplicationPermissionHolder extends BaseEntity {
	private static final long					serialVersionUID	= 4113924528885900650L;

	@Column(name = "applicationName")
	private String								applicationName;

	@Column(name = "accessMenu")
	private boolean								accessMenu;

	@OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PERMISSION_REF")
	private List<ZoroApplicationPermission>	permissions			= new ArrayList<ZoroApplicationPermission>();

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the accessMenu
	 */
	public boolean isAccessMenu() {
		return accessMenu;
	}

	/**
	 * @param accessMenu
	 *            the accessMenu to set
	 */
	public void setAccessMenu(boolean accessMenu) {
		this.accessMenu = accessMenu;
	}

	/**
	 * @return the permissions
	 */
	public List<ZoroApplicationPermission> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissionsArray(ZoroApplicationPermission[] permissionsArray) {
		permissions.clear();
		if (permissionsArray != null) {
			CollectionUtils.addAll(permissions, permissionsArray);
		}
	}

	/**
	 * @return the permissions
	 */
	public ZoroApplicationPermission[] getPermissionsArray() {
		return permissions.toArray(new ZoroApplicationPermission[] {});
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(List<ZoroApplicationPermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the application name
	 */
	@Override
	public String toString() {
		return applicationName;
	}
}
