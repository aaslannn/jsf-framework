package com.ozguryazilim.zoro.core.loader;

import java.io.Serializable;
import java.util.List;

import com.ozguryazilim.zoro.core.auth.entity.ZoroApplicationPermission;

public class Application implements Serializable {
	private static final long					serialVersionUID	= -4381909126877417029L;

	private String								name;
	private String								url;
	private int									order;
	private IApplicationFunctionalityAuthorizer	authorizer;
	private List<ZoroApplicationPermission>	availablePermissions;

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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	public List<ZoroApplicationPermission> getAvailablePermissions() {
		if (availablePermissions == null) {
			availablePermissions = authorizer.getAvailablePermissions();
		}

		return availablePermissions;
	}

	/**
	 * @return the authorizer
	 */
	public IApplicationFunctionalityAuthorizer getAuthorizer() {
		return authorizer;
	}

	/**
	 * @param authorizer
	 *            the authorizer to set
	 */
	public void setAuthorizer(IApplicationFunctionalityAuthorizer authorizer) {
		this.authorizer = authorizer;
	}
}
