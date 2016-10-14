package com.ozguryazilim.zoro.core;

import java.util.Map;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

public class JSFLazyDataModel<T extends BaseEntity> extends BaseLazyDataModel<T> {
	private static final long		serialVersionUID	= -4819118752115649795L;

	private JSFViewController<T>	viewController;

	@SuppressWarnings("unchecked")
	public JSFLazyDataModel(JSFViewController<T> viewController) {
		super((Class<T>) viewController.getGenericType(), viewController.getEntityManager());
		this.viewController = viewController;
	}

	/**
	 * Returns the view controllers filters
	 */
	@Override
	protected Map<String, Object> getExtraFilters() {
		return viewController.getFilters();
	}

	/**
	 * Returns the view controllers values
	 * 
	 */
	@Override
	protected Object getValueOfFilter(String name, String value) {
		return viewController.getValueOfFilter(name, value);
	}
}
