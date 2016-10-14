package com.ozguryazilim.zoro.core.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Entity
@Table(name = "PERMISSION")
public class ZoroApplicationPermission extends BaseEntity {
	private static final long	serialVersionUID	= 8803639065827164977L;

	@Column(name = "NAME")
	private String				name;

	public ZoroApplicationPermission() {

	}

	public ZoroApplicationPermission(String name) {
		this.name = name;
	}

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

	@Override
	public boolean equals(Object obj) {
		ZoroApplicationPermission permission = (ZoroApplicationPermission) obj;
		return this.name.equals(permission.getName());
	}
}
