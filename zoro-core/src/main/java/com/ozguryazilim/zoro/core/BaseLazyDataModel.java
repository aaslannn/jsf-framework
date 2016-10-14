package com.ozguryazilim.zoro.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.ozguryazilim.zoro.core.db.DBEntityManager;
import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

public class BaseLazyDataModel<T extends BaseEntity> extends LazyDataModel<T> {
	private static final long	serialVersionUID	= -4819118752115649795L;

	private Class<T>			type;

	private DBEntityManager		entityManager;

	private String				customQuery;

	private Object[]			customQueryParams;

	public BaseLazyDataModel(Class<T> type, DBEntityManager entityManager) {
		this.type = type;
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, String> filters) {
		Class<?> genericType = getEntityClass();

		Map<String, Object> filterObjectMap = getFilterObjectMap(filters);
		List<T> result;
		if (customQuery == null) {
			result = (List<T>) entityManager.list(genericType, first, pageSize, multiSortMeta, filterObjectMap);

			setRowCount(entityManager.count(genericType, multiSortMeta, filterObjectMap));
		}
		else {
			result = (List<T>) entityManager.list(customQuery, customQueryParams, first, pageSize, multiSortMeta, filterObjectMap);

			setRowCount(entityManager.count(customQuery, customQueryParams, multiSortMeta, filterObjectMap));
		}

		return result;
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		List<SortMeta> multiSortMeta = new ArrayList<SortMeta>();
		if (!sortOrder.equals(SortOrder.UNSORTED) && sortField != null) {
			multiSortMeta.add(new SortMeta(null, sortField, sortOrder, null));
		}

		return load(first, pageSize, multiSortMeta, filters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getRowData(String rowKey) {
		List<BaseEntity> wrappedData = (List<BaseEntity>) getWrappedData();
		for (BaseEntity entity : wrappedData) {
			if (entity.getId().equals(rowKey)) {
				return (T) entity;
			}
		}

		return null;
	}

	public Object getRowKey(T object) {
		BaseEntity entity = (BaseEntity) object;

		return entity.getId();
	}

	/**
	 * Creates a object map to better use in queries
	 * 
	 * @param filters
	 * @return
	 */
	private Map<String, Object> getFilterObjectMap(Map<String, String> filters) {
		Map<String, Object> objectMap = getExtraFilters();
		for (String key : filters.keySet()) {
			objectMap.put(key, getValueOfFilter(key, filters.get(key)));
		}

		return objectMap;
	}

	/**
	 * Returns the extra filter values
	 * 
	 * @return
	 */
	protected Map<String, Object> getExtraFilters() {
		return new HashMap<String, Object>();
	}

	/**
	 * Returns the value of the given filter
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected Object getValueOfFilter(String name, String value) {
		return value;
	}

	/**
	 * @return the customQuery
	 */
	public String getCustomQuery() {
		return customQuery;
	}

	/**
	 * @param customQuery
	 *            the customQuery to set
	 */
	public void setCustomQuery(String customQuery, Object[] customQueryParams) {
		this.customQuery = customQuery;
		this.customQueryParams = customQueryParams;
	}

	/**
	 * @return the customQueryParams
	 */
	public Object[] getCustomQueryParams() {
		return customQueryParams;
	}

	protected Class<?> getEntityClass() {
		return type;
	}
}
