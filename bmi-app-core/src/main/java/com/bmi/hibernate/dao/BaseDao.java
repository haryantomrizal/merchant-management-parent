package com.bmi.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.bmi.hibernate.domain.Entity;
import com.bmi.mm.web.tool.AppConstants;
import com.bmi.mm.web.tool.PersistenceTools;
import com.bmi.mm.web.tool.StringTools;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
@Repository
@SuppressWarnings(value = { "unchecked" })
public abstract class BaseDao<ET extends Entity<ID>, ID extends Serializable> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected Class<ET> entityClass;

	@Autowired
	protected SessionFactory sessionFactory;

	public BaseDao() {
		Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments();
		entityClass = (Class<ET>) actualTypeArguments[0];
	}

	public Session getSession() {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
		} catch (Exception ex) {
			logger.warn("GetCurrentSession failed", ex);
			logger.info("Opening new hibernate session");
			session = getSessionFactory().openSession();
		}
		return session;
	}

	public ET findById(ID id) {
		return findById(id, false);
	}

	public ET findById(ID id, boolean init) {
		ET result = null;
		try {
			result = (ET) getSession().get(entityClass, id);
			if (init && result != null) initialize(result);
		} catch (HibernateException ex) {
			logger.error(ex.getLocalizedMessage(), ex);
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
		return result;
	}

	public Criteria buildByExample(Session session, ET entity) {
		Criteria criteria = session.createCriteria(entity.getClass());
		criteria.add(Example.create(entity));
		return criteria;
	}

	public List<ET> findAll() {
		return getSession().createCriteria(entityClass).list();
	}

	public List<ET> find(ET entity) {
		List<ET> list = null;
		try {
			list = buildByExample(getSession(), entity).list();
		} catch (HibernateException ex) {
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<ET> findPaged(ET entity, int start, int size) {
		List<ET> list = null;
		try {
			list = buildByExample(getSession(), entity).setFirstResult(start).setMaxResults(size).list();
		} catch (HibernateException ex) {
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<ET> find(ET entity, String orderBy) {
		if (entity == null) return Collections.EMPTY_LIST;
		StringBuilder filter = new StringBuilder();
		final String beanName = StringTools.lowerCaseFirstLetter(entity.getClass().getSimpleName());
		filter.append(PersistenceTools.hibernateBuildParameter(entity, true));
		if (filter.length() > 0) {
			// insert the word WHERE in the front of filter
			filter.insert(0, " WHERE ");
			// delete the last AND
			filter.delete(filter.length() - 4, filter.length());
		}
		if (!StringTools.isEmptyString(orderBy)) {
			filter.append(orderBy);
		}

		logger.debug("FILTER >> {}", filter.toString());
		Query query = getSession().createQuery(
				"FROM " + entity.getClass().getName() + " " + beanName + " " + filter.toString());
		query.setProperties(entity);

		List<ET> list = query.list();
		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	// TODO
	public List<ET> find(Class<ET> clazz, Map<String, Object> parameter, Session session) {
		if (parameter.isEmpty()) return Collections.EMPTY_LIST;

		StringBuilder filter = new StringBuilder();

		// lower case the first letter of the class name
		final String entityName = StringTools.lowerCaseFirstLetter(clazz.getSimpleName());

		filter.append(PersistenceTools.hibernateBuildParameter(clazz, parameter));

		if (filter.length() > 0) {
			// insert the word WHERE in front of filter
			filter.insert(0, " WHERE ");

			// delete the last AND
			filter.delete(filter.length() - 4, filter.length());
		}

		if (parameter.get(AppConstants.NAMING_ORDER_BY) != null) {
			filter.append((String) parameter.get(AppConstants.NAMING_ORDER_BY));
		}

		logger.debug("FILTER = {}", filter.toString());

		Query query = session.createQuery("FROM " + clazz.getName() + " " + entityName + " "
				+ filter.toString());
		query.setProperties(parameter);
		List<ET> list = query.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<ET> find(Class<ET> clazz, Map<String, Object> parameter) {
		Session session = null;
		List<ET> list = null;
		try {
			session = getSession();
			list = find(clazz, parameter, session);
		} catch (HibernateException ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		} finally {
			if (session != null) session.close();
		}
		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<ET> findPaged(ET entity, String orderBy, int start, int size) {
		if (entity == null) return Collections.EMPTY_LIST;

		StringBuilder filter = new StringBuilder();
		final String beanName = StringTools.lowerCaseFirstLetter(entity.getClass().getSimpleName());
		filter.append(PersistenceTools.hibernateBuildParameter(entity, true));

		if (filter.length() > 0) {
			// insert the word WHERE in the front of filter
			filter.insert(0, " WHERE ");

			// delete the last AND
			filter.delete(filter.length() - 4, filter.length());
		}

		if (!StringTools.isEmptyString(orderBy)) {
			filter.append(orderBy);
		}

		logger.debug("FILTER PAGED >> {}", filter.toString());

		Query query = getSession().createQuery(
				"FROM " + entity.getClass().getName() + " " + beanName + " " + filter.toString());
		query.setProperties(entity);
		query.setFirstResult(start);
		query.setMaxResults(size);

		List<ET> list = query.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public long selectCount(ET entity) {
		long result = 0;
		// if (entity == null) return result;
		//
		// StringBuilder filter = new StringBuilder();
		// // lowercase the first letter of the class name
		// final String entityName =
		// StringTools.lowerCaseFirstLetter(entity.getClass().getSimpleName());
		// filter.append(PersistenceTools.hibernateBuildParameter(entity, true));
		//
		// if (filter.length() > 0) {
		// // insert the word WHERE in front of filter
		// filter.insert(0, " WHERE ");
		//
		// // delete the last AND
		// filter.delete(filter.length() - 4, filter.length());
		// }
		//
		// logger.debug("COUNT FILTER = {}", filter.toString());
		//
		// Query query = getSession().createQuery("SELECT COUNT(" + entityName + ") FROM "
		// + entity.getClass().getName() + " " + entityName + " " + filter.toString());
		// query.setProperties(entity);

		Criteria criteria = getSession().createCriteria(entityClass).add(Example.create(entity))
				.setProjection(Projections.rowCount());
		result = (Long) criteria.uniqueResult();

		return result;
	}

	public List<ET> selectSearch(ET entity, String searchKeyword, String orderBy,
			List<String> searchProperties) {

		logger.debug("searchProperties = {}", searchProperties);
		logger.debug("searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(entity, searchProperties, searchKeyword, orderBy, false);

		logger.debug("selectSearch QUERY {}", query);

		Query q = getSession().createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		List<ET> list = q.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<ET> selectSearch(ET entity, String searchKeyword, String orderBy,
			List<String> searchProperties, boolean isApp) {

		logger.debug("searchProperties = {}", searchProperties);
		logger.debug("searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(entity, searchProperties, searchKeyword, orderBy, false, false);

		logger.debug("selectSearch QUERY {}", query);

		Query q = getSession().createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		List<ET> list = q.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public long selectSearchCount(ET entity, List<String> searchProperties, String searchKeyword) {
		long result = 0;

		logger.debug("COUNT searchProperties = {}", searchProperties);
		logger.debug("COUNT searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(entity, searchProperties, searchKeyword, null, true);

		logger.debug("COUNT selectSearchCount QUERY = {}", query);

		Query q = getSession().createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		result = (Long) q.uniqueResult();

		return result;
	}

	public long selectSearchCount(ET entity, List<String> searchProperties, String searchKeyword,
			boolean isApp) {
		long result = 0;

		if (isApp) { return selectSearchCount(entity, searchProperties, searchKeyword); }

		logger.debug("COUNT searchProperties = {}", searchProperties);
		logger.debug("COUNT searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(entity, searchProperties, searchKeyword, null, true, isApp);

		logger.debug("COUNT selectSearchCount QUERY = {}", query);

		Query q = getSession().createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		result = (Long) q.uniqueResult();

		return result;
	}

	public List<ET> selectSearchPaged(ET entity, List<String> searchProperties, String searchKeyword,
			String orderBy, int start, int size) {

		logger.debug("PAGED searchProperties = {}", searchProperties);
		logger.debug("PAGED searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(entity, searchProperties, searchKeyword, orderBy, false);

		logger.debug("PAGED selectSearchPaged QUERY = {}", query);

		Query q = getSession().createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		q.setFirstResult(start);
		q.setMaxResults(size);

		List<ET> list = q.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<ET> selectSearchPaged(ET entity, List<String> searchProperties, String searchKeyword,
			String orderBy, int start, int size, boolean isApp) {

		logger.debug("PAGED searchProperties = {}", searchProperties);
		logger.debug("PAGED searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(entity, searchProperties, searchKeyword, orderBy, false, isApp);

		logger.debug("PAGED selectSearchPaged QUERY = {}", query);

		Query q = getSession().createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		q.setFirstResult(start);
		q.setMaxResults(size);

		List<ET> list = q.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public void load(ET entity, ID id) {
		getSession().load(entity, id);
	}

	public void load(ET entity, ID id, boolean init) {
		getSession().load(entity, id);
		if (init && entity != null) initialize(entity); 
	}

	public void initialize(ET proxy) {
	}

	public ID insert(ET entity) {
		return (ID) getSession().save(entity);
	}

	/**
	 * Insert List of Model to the Database
	 * 
	 * @param entityList
	 *            list of entity
	 * @param onePhaseCommit
	 *            boolean expression to commit on one phase insert or not
	 * @return count of object that succesfully inserted
	 */
	// TODO
	public int insert(List<ET> entityList, boolean onePhaseCommit) {
		int insertedModel = 0;

		if (onePhaseCommit) {
			for (ET entity : entityList)
				insert(entity);
		} else {
			try {
				for (ET entity : entityList) {
					if (getSession().save(entity) != null) insertedModel++;
				}
			} catch (RuntimeException ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		}
		return insertedModel;
	}

	public void update(ET entity) {
		getSession().update(entity);
	}

	public void softDelete(ID id) {
		ET entity = (ET) getSession().load(entityClass, id);
		entity.setDeleted(true);
		getSession().update(entity);
	}

	public void saveOrUpdate(ET entity) {
		getSession().saveOrUpdate(entity);
	}

	/**
	 * Either save or update list of entity to the database based on unsaved value check
	 * 
	 * @param entityList
	 *            list of entity
	 * @param onePhaseCommit
	 *            boolean expression to commit on one phase insert or not
	 */
	// TODO
	public void saveOrUpdate(List<ET> entityList, boolean onePhaseCommit) {
		if (onePhaseCommit) for (ET entity : entityList)
			saveOrUpdate(entity);
		else {
			try {
				for (ET entity : entityList) {
					getSession().saveOrUpdate(entity);
				}
			} catch (RuntimeException ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		}
	}

	public ET deleteById(ID id) {
		ET entity = (ET) getSession().load(entityClass, id);
		getSession().delete(entity);
		return entity;
	}

	public void delete(ET entity) {
		getSession().delete(entity);
	}

	public ET merge(ET entity) {
		return (ET) getSession().merge(entity);
	}

	public String buildSearchQuery(ET entity, List<String> searchProperties, String searchKeyword,
			String orderBy, boolean isSelectCount) {
		StringBuilder query = new StringBuilder();
		final String entityName = StringTools.lowerCaseFirstLetter(entity.getClass().getSimpleName());

		if (isSelectCount) {
			query.append("SELECT COUNT(" + entityName + ") FROM " + entity.getClass().getName() + " "
					+ entityName);
		} else {
			query.append("FROM " + entity.getClass().getName() + " " + entityName);
		}

		if (!StringTools.isEmptyString(searchKeyword)) {
			int index = 0;
			for (String property : searchProperties) {
				if (index == 0) {
					query.append(" WHERE (LOWER(" + entityName + "." + property + ") LIKE :"
							+ AppConstants.SEARCH_KEYWORD);
				} else {
					query.append(" OR LOWER(" + entityName + "." + property + ") LIKE :"
							+ AppConstants.SEARCH_KEYWORD);
				}
				index++;
			}
			if (Entity.class.isAssignableFrom(entity.getClass())) {
				query.append(" ) AND " + entityName + ".status = '" + AppConstants.DEFAULT_STATUS_ACTIVE
						+ "' ");
			} else {
				query.append(" ) ");
			}
		} else {
			if (Entity.class.isAssignableFrom(entity.getClass())) {
				query.append(" WHERE " + entityName + ".status = '" + AppConstants.DEFAULT_STATUS_ACTIVE
						+ "' ");
			}
		}

		if (!StringTools.isEmptyString(orderBy)) query.append(orderBy);

		return query.toString();
	}

	public String buildSearchQuery(ET entity, List<String> searchProperties, String searchKeyword,
			String orderBy, boolean isSelectCount, boolean isApp) {
		StringBuilder query = new StringBuilder();
		final String entityName = StringTools.lowerCaseFirstLetter(entity.getClass().getSimpleName());

		if (isSelectCount) {
			query.append("SELECT COUNT(" + entityName + ") FROM " + entity.getClass().getName() + " "
					+ entityName);
		} else {
			query.append("FROM " + entity.getClass().getName() + " " + entityName);
		}

		if (!StringTools.isEmptyString(searchKeyword)) {
			int index = 0;
			for (String property : searchProperties) {
				if (index == 0) {
					query.append(" WHERE (LOWER(" + entityName + "." + property + ") LIKE :"
							+ AppConstants.SEARCH_KEYWORD);
				} else {
					query.append(" OR LOWER(" + entityName + "." + property + ") LIKE :"
							+ AppConstants.SEARCH_KEYWORD);
				}
				index++;
			}
			query.append(") ");
			if (isApp) {
				query.append(" AND " + entityName + ".status = '" + AppConstants.DEFAULT_STATUS_ACTIVE + "' ");
			}
		} else {
			if (isApp) {
				query.append(" WHERE " + entityName + ".status = '" + AppConstants.DEFAULT_STATUS_ACTIVE
						+ "' ");
			}
		}

		if (!StringTools.isEmptyString(orderBy)) query.append(orderBy);

		return query.toString();
	}

	public int countSummary(String tableName, String filter) {
		int result = 0;

		StringBuilder query = new StringBuilder("SELECT COUNT(1) FROM ");
		query.append(tableName);
		query.append(" ");
		query.append(filter);

		logger.debug("countSummary QUERY = {}", query.toString());

		Session session = getSession();

		try {
			result = ((BigDecimal) session.createSQLQuery(query.toString()).uniqueResult()).intValue();
		} catch (RuntimeException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		return result;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}