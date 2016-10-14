package com.ozguryazilim.zoro.core;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.context.RequestContext;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;
import com.ozguryazilim.zoro.core.util.ReflectionUtility;

public abstract class BaseViewController<T extends BaseEntity> extends UtilityViewController implements Serializable {
	private static final long	serialVersionUID	= -9143409182504831028L;
	public static final String	DEFAULT_BUNDLE		= "default";

	protected T					entity;

	protected List<T>			entityList;

	protected boolean			editMode			= false;

	@PostConstruct
	public void postConstruct() {
		clearEntity();
	}

	public void openEditorForEdit() {
		editMode = true;
		entityChanged();
	}

	/**
	 * Clears the entity and sets show editor to true
	 */
	public void openEditorForAdd() {
		editMode = false;
		clearEntity();
	}

	/**
	 * Override this function for any validation
	 * 
	 * @return
	 */
	protected boolean isValid() {
		return true;
	}

	/**
	 * Adds the filter to the data holder
	 */
	public void save() {
		boolean validationSuccess = true;
		if (isValid()) {
			validationSuccess = true;
			if (entity != null) {
				persist();
			}

			editMode = false;

			afterAdd();

			clearEntity();
		}
		else {
			validationSuccess = false;
		}

		RequestContext.getCurrentInstance().addCallbackParam("validationFailed", !validationSuccess);
	}

	protected void persist() {
		if (!editMode) {
			entity.setId(UUID.randomUUID().toString());

			entityList.add(entity);
		}
	}

	/**
	 * Overriden to add functionality after successful add operation
	 */
	protected void afterAdd() {

	}

	/**
	 * Removes the filter from the data holder
	 */
	public void delete() {
		entityList.remove(entity);

		afterDelete();
	}

	/**
	 * Overriden to add functionality after delete operation
	 */
	protected void afterDelete() {

	}

	/**
	 * Creates a copy of the current entity and sets it to it
	 */
	@SuppressWarnings({ "unchecked" })
	public void copyAs() {
		try {
			T newEntity = (T) getGenericType().newInstance();

			PropertyUtils.copyProperties(newEntity, entity);

			entity = (T) newEntity;

			beforeSetIdsToNull();

			setIdsToNull(entity);

			afterCopyAs();

			entityChanged();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		editMode = false;
	}

	/**
	 * Called before sets id of the entity to null
	 * 
	 * @param baseEntity
	 * @throws IllegalAccessException
	 */
	protected void beforeSetIdsToNull() {

	}

	/**
	 * Sets id of the baseEntity to null
	 * 
	 * @param baseEntity
	 * @throws IllegalAccessException
	 */
	protected void setIdsToNull(BaseEntity baseEntity) throws IllegalAccessException {
		baseEntity.setId(null);
	}

	/**
	 * Called after a successful copy as
	 */
	protected void afterCopyAs() {

	}

	/**
	 * Resets the entity as a new instance and calls entityChanged function
	 */
	@SuppressWarnings({ "unchecked" })
	protected void clearEntity() {
		try {
			entity = (T) getGenericType().newInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		entityChanged();
	}

	/**
	 * Override for entity changed operations, its called after postconstruct,
	 * openEditorForAdd, openEditorForEdit
	 */
	protected void entityChanged() {

	}

	/**
	 * Returns the class of the generic type
	 * 
	 * @return
	 */
	public Class<?> getGenericType() {
		return ReflectionUtility.getGenericType(this.getClass());
	}

	/**
	 * @return the entityList
	 */
	public List<T> getEntityList() {
		return entityList;
	}

	/**
	 * Sets entity list
	 * 
	 * @param entityList
	 */
	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}

	/**
	 * @return the entity
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(T entity) {
		this.entity = entity;
	}
}
