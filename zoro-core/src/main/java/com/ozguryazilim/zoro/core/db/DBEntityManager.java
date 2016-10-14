package com.ozguryazilim.zoro.core.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.hibernate.collection.internal.PersistentBag;
import org.jboss.seam.transaction.Transactional;
import org.jboss.solder.core.ExtensionManaged;
import org.jboss.solder.reflection.Reflections;
import org.jboss.solder.servlet.WebApplication;
import org.jboss.solder.servlet.event.Initialized;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Singleton
public class DBEntityManager implements Serializable {
	private static final long			serialVersionUID			= 2927134765822000555L;

	private static final Logger			LOGGER						= Logger.getLogger(DBEntityManager.class.getName());

	@Produces
	@ApplicationScoped
	@ExtensionManaged
	@Default
	@PersistenceUnit(unitName = "finance")
	private EntityManagerFactory		entityManagerFactory;

	@Inject
	private EntityManager				entityManager;

	private static EntityManagerFactory	globalEntityManagerFactory	= Persistence.createEntityManagerFactory("finance");

	private static DBEntityManager		instance;

	/**
	 * Initializes db connection
	 */
	@PostConstruct
	protected void postConstruct() {
		instance = this;
	}

	/**
	 * Returns the instance for db entity manager
	 * 
	 * @return
	 */
	public static DBEntityManager getInstance() {
		return instance;
	}

	/**
	 * Returns the entity manager
	 * 
	 * @return
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Transactional
	public void save(BaseEntity entity) {
		if (entity.getId() == null || entity.getId().length() == 0) {
			entityManager.persist(entity);
		}
		else {
			entityManager.merge(entity);
		}
	}

	/**
	 * Persists a new entity
	 * 
	 * @param entity
	 */
	@Transactional
	public void persist(BaseEntity entity) {
		entityManager.persist(entity);
	}

	/**
	 * Merges the already existing entity to the db
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional
	public Object merge(BaseEntity entity) {
		return entityManager.merge(entity);
	}

	/**
	 * Returns the element with given id for the given classes
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public Object find(Class<? extends BaseEntity> clazz, String id) {
		return entityManager.find(clazz, id);
	}

	/**
	 * Deletes the given entity from db
	 * 
	 * @param entity
	 */
	@Transactional
	public void delete(BaseEntity entity) {
		if (Hibernate.isInitialized(entity)) {
			entity = entityManager.find(entity.getClass(), entity.getId());
		}

		if (entity != null) {
			entityManager.remove(entity);
		}
	}

	/**
	 * Returns the list for the given class instances for the given starting
	 * index and pageSize
	 * 
	 * @param clazz
	 * @param firstResult
	 * @param pageSize
	 * @param multiSortMeta
	 * @return
	 */
	public List<?> list(Class<?> clazz, int firstResult, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		String query = createQuery(clazz, multiSortMeta, filters);

		Query createQuery = entityManager.createQuery(query);

		int queryParamIndex = 0;
		for (String filterKey : filters.keySet()) {
			Object value = filters.get(filterKey);
			Object valueToUser = value;
			if (value instanceof String) {
				valueToUser = "%" + value + "%";
			}
			createQuery.setParameter(++queryParamIndex, valueToUser);
		}

		createQuery.setFirstResult(firstResult);
		createQuery.setMaxResults(pageSize);

		return createQuery.getResultList();
	}

	/**
	 * Returns the list for the given class instances for the given starting
	 * index and pageSize
	 * 
	 * @param query
	 * @param params
	 * @param firstResult
	 * @param pageSize
	 * @param multiSortMeta
	 * @return
	 */
	public List<?> list(String query, Object[] params, int firstResult, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		StringBuffer queryBuffer = new StringBuffer(query);
		queryBuffer = addFilterParameters(queryBuffer, filters);

		Query createQuery = entityManager.createQuery(queryBuffer.toString());

		int queryParamIndex = 0;
		if (params != null) {
			for (; queryParamIndex < params.length; queryParamIndex++) {
				createQuery.setParameter(queryParamIndex + 1, params[queryParamIndex]);
			}
		}

		for (String filterKey : filters.keySet()) {
			Object value = filters.get(filterKey);
			Object valueToUser = value;
			if (value instanceof String) {
				valueToUser = "%" + value + "%";
			}

			createQuery.setParameter(++queryParamIndex, valueToUser);
		}

		createQuery.setFirstResult(firstResult);
		createQuery.setMaxResults(pageSize);

		return createQuery.getResultList();
	}

	/**
	 * Returns the list for the given class instances for the given starting
	 * index and pageSize
	 * 
	 * @param query
	 * @param params
	 * @param firstResult
	 * @param pageSize
	 * @return
	 */
	public List<?> list(String query, Object[] params, int firstResult, int pageSize) {
		return list(query, params, firstResult, pageSize, new ArrayList<SortMeta>(), new HashMap<String, Object>());
	}

	/**
	 * Executes the given query
	 * 
	 * @param query
	 * @return
	 */
	@Transactional
	public int executeNativeUpdateQuery(String query, Object[] params) {
		Query createNativeQuery = entityManager.createNamedQuery(query);

		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				createNativeQuery.setParameter(i + 1, params[i]);
			}
		}

		return createNativeQuery.executeUpdate();
	}

	/**
	 * Executes the given query
	 * 
	 * @param query
	 * @return
	 */
	@Transactional
	public int executeUpdateQuery(String query, Object[] params) {
		Query createQuery = entityManager.createQuery(query);

		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				createQuery.setParameter(i + 1, params[i]);
			}
		}

		return createQuery.executeUpdate();
	}

	/**
	 * Adds the filter parameters to the query and createQuery
	 * 
	 * @param query
	 * @param filters
	 */
	private StringBuffer addFilterParameters(StringBuffer query, Map<String, Object> filters) {
		if (filters.size() > 0) {
			if (query.indexOf("WHERE") == -1) {
				query.append(" WHERE ");
			}
			else {
				query.append(" AND ");
			}

			for (String filterKey : filters.keySet()) {
				query.append(" c.");
				query.append(filterKey);
				if (filters.get(filterKey) instanceof String) {

					query.append(" LIKE ");
				}
				else {
					query.append(" = ");
				}
				query.append(" ?");
				query.append(" AND ");
			}

			query = new StringBuffer(query.substring(0, query.length() - 5));
		}

		return query;
	}

	public int count(Class<?> clazz, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		String query = createCountQuery(clazz, multiSortMeta, filters);

		Query createQuery = entityManager.createQuery(query);

		Long count = (Long) createQuery.getSingleResult();

		return count.intValue();
	}

	public int count(String query, Object[] params, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		String countQuery = query.replaceAll("^(SELECT)[^(FROM)]*", "SELECT COUNT(*) ");
		Query createQuery = entityManager.createQuery(countQuery);

		if (params != null) {
			for (int queryParamIndex = 0; queryParamIndex < params.length; queryParamIndex++) {
				createQuery.setParameter(queryParamIndex + 1, params[queryParamIndex]);
			}
		}

		Long count = (Long) createQuery.getSingleResult();

		return count.intValue();
	}

	/**
	 * Creates the query string for the given parameters
	 * 
	 * @param clazz
	 * @param multiSortMeta
	 * @param filters
	 * @return
	 */
	private String createQuery(Class<?> clazz, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		StringBuffer query = new StringBuffer("SELECT c FROM " + clazz.getSimpleName() + " c");

		query = addFilterParameters(query, filters);

		if (multiSortMeta != null) {
			for (SortMeta meta : multiSortMeta) {
				SortOrder sortOrder = meta.getSortOrder();
				String sortField = meta.getSortField();
				if (!sortOrder.equals(SortOrder.UNSORTED)) {
					if (query.indexOf("ORDER BY") == -1) {
						query.append(" ORDER BY ");
					}

					query.append(" ");
					query.append(sortField);
					query.append(" ");
					query.append(sortOrder.equals(SortOrder.ASCENDING) ? " ASC" : " DESC");
				}
			}
		}

		return query.toString();
	}

	/**
	 * Creates the query string for the given parameters
	 * 
	 * @param clazz
	 * @param multiSortMeta
	 * @param filters
	 * @return
	 */
	private String createCountQuery(Class<?> clazz, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		StringBuffer query = new StringBuffer("SELECT COUNT(c) FROM " + clazz.getSimpleName() + " c");

		return query.toString();
	}

	/**
	 * Executes the query with the given parameters
	 * 
	 * @param query
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List executeParameterizedSelectQuery(String query, Object[] params) {
		Query createQuery = entityManager.createQuery(query);

		for (int i = 0; i < params.length; i++) {
			createQuery.setParameter(i + 1, params[i]);
		}

		return createQuery.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public void fetchLazyExceptionField(BaseEntity entity, String fieldName) {
		try {
			Field declaredField = entity.getClass().getDeclaredField(fieldName);
			declaredField.setAccessible(true);

			Object object = declaredField.get(entity);
			if (Hibernate.isInitialized(object) && !(object instanceof PersistentBag)) {
				return;
			}

			String query = "SELECT c." + fieldName + " FROM " + entity.getClass().getSimpleName() + " c WHERE c.id = ?";

			Query createQuery = entityManager.createQuery(query);

			createQuery.setParameter(1, entity.getId());

			List resultList = createQuery.getResultList();

			Reflections.setFieldValue(declaredField, entity, resultList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates an entity manager
	 * 
	 * @return
	 */
	public static EntityManager createAEntityManager() {
		EntityManager createEntityManager = globalEntityManagerFactory.createEntityManager();
		return createEntityManager;
	}

	/**
	 * Flushes the current state
	 */
	public void flush() {
		entityManager.flush();
	}

	/**
	 * At startup of web application logs the message, it is used to initialize
	 * the bean at startup
	 * 
	 * @param webapp
	 */
	public void startup(@Observes @Initialized WebApplication webapp) {
		LOGGER.info("DBEntityManager initialized at " + new Date(webapp.getStartTime()));
	}
}
