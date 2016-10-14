package com.ozguryazilim.zoro.core.auth.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Entity
@Table(name = "ROLE")
public class ZoroRole extends BaseEntity {
	private static final long						serialVersionUID	= -2289082672722903649L;

	@Column(name = "NAME", length = 64)
	private String									name;

	@ElementCollection
	@CollectionTable(name = "ROLE_USERNAME", joinColumns = @JoinColumn(name = "ROLE_REF"))
	@Column(name = "USERNAME")
	private List<String>							usernames			= new ArrayList<String>();

	/**
	 * Holds only the menu access is permitted applications
	 */
	@OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PERMISSION_HOLDER_REF")
	private List<ZoroApplicationPermissionHolder>	applications		= new ArrayList<ZoroApplicationPermissionHolder>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the usernames
	 */
	public List<String> getUsernames() {
		return usernames;
	}

	/**
	 * @param usernames
	 *            the usernames to set
	 */
	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}

	/**
	 * @return the usernames
	 */
	public String[] getUsernamesArray() {
		return usernames.toArray(new String[] {});
	}

	/**
	 * @param usernames
	 *            the usernames to set
	 */
	public void setUsernamesArray(String[] usernamesArray) {
		usernames.clear();
		CollectionUtils.addAll(usernames, usernamesArray);
	}

	/**
	 * @return the applications
	 */
	public List<ZoroApplicationPermissionHolder> getApplications() {
		return applications;
	}

	/**
	 * @param applications
	 *            the applications to set
	 */
	public void setApplications(List<ZoroApplicationPermissionHolder> applications) {
		this.applications = applications;
	}
}
