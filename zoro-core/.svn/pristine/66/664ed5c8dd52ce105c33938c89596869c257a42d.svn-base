package com.ozguryazilim.zoro.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jboss.seam.transaction.Transactional;
import org.primefaces.model.LazyDataModel;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;
import com.ozguryazilim.zoro.core.util.ReflectionUtility;

public class JSFViewController<T extends BaseEntity> extends BaseViewController<T> {
	private static final long		serialVersionUID	= -7132472407543093632L;

	protected BaseLazyDataModel<T>	model				= null;

	/**
	 * Returns the model created for the view controller
	 * 
	 * @return
	 */
	public LazyDataModel<T> getDataModel() {
		if (model == null) {
			model = new JSFLazyDataModel<T>(this);
		}

		return model;
	}

	@Override
	@Transactional
	protected void persist() {
		entityManager.save(entity);
	}

	@Override
	@Transactional
	public void delete() {
		entityManager.delete(entity);

		afterDelete();
	}

	/**
	 * Nulls model to re-construct the list
	 */
	public void clearModel() {
		model = null;
	}

	/**
	 * Sets id of the baseEntity and each id of the sub entity of the baseEntity
	 * 
	 * @param baseEntity
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void setIdsToNull(BaseEntity baseEntity) throws IllegalAccessException {
		List<Field> allFields = ReflectionUtility.getAllFields(baseEntity.getClass());
		for (Field field : allFields) {
			if (field.getType().equals(List.class)) {
				field.setAccessible(true);

				entityManager.fetchLazyExceptionField(baseEntity, field.getName());
				List list = (List) field.get(baseEntity);
				if (list.size() > 0) {
					if (list.get(0) instanceof BaseEntity) {
						List oldList = new ArrayList();
						oldList.addAll(list);
						list.clear();
						// Only set first level lists or recursively it should
						// be set
						for (Object item : oldList) {
							try {
								BaseEntity newEntity = (BaseEntity) item.getClass().newInstance();
								PropertyUtils.copyProperties(newEntity, item);

								setIdsToNull(newEntity);

								list.add(newEntity);
							}
							catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		baseEntity.setId(null);
	}

	protected Map<String, Object> getFilters() {
		return new HashMap<String, Object>();
	}

	/**
	 * Override to get the actual value from the given string
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public Object getValueOfFilter(String name, String value) {
		return value;
	}
}
