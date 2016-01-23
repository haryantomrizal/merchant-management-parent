package com.bmi.app.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bmi.app.domain.AuditableEntity;
import com.bmi.hibernate.dao.BaseDao;
import com.bmi.mm.web.tool.AppConstants;
import com.bmi.mm.web.tool.StringTools;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
@Repository
@SuppressWarnings({ "unchecked" })
public abstract class AuditableBaseDao<M extends AuditableEntity<ID>, ID extends Serializable> extends
		BaseDao<M, ID> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public AuditableBaseDao() {
		super();
	}

	public List<M> selectSearch(Session session, M model, List<String> searchProperties,
			String searchKeyword, boolean withOrderBy) {

		logger.debug("searchProperties = {}", searchProperties);
		logger.debug("searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(model, searchProperties, withOrderBy, false);

		logger.debug("selectSearch QUERY {}", query);

		Query q = session.createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		List<M> list = q.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<M> selectSearch(M model, List<String> searchProperties, String searchKeyword,
			boolean withOrderBy) {

		Session session = null;
		List<M> list = null;

		try {
			session = getSession();
			list = selectSearch(session, model, searchProperties, searchKeyword, withOrderBy);
		} catch (RuntimeException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<M> selectSearchPaged(Session session, M model, List<String> searchProperties,
			String searchKeyword, boolean withOrderBy, int start, int size) {

		logger.debug("PAGED searchProperties = {}", searchProperties);
		logger.debug("PAGED searchKeyword = {}", searchKeyword);

		String query = buildSearchQuery(model, searchProperties, withOrderBy, false);

		logger.debug("PAGED selectSearchPaged QUERY = {}", query);

		Query q = session.createQuery(query);
		if (!StringTools.isEmptyString(searchKeyword)) q
				.setString(AppConstants.SEARCH_KEYWORD, searchKeyword);
		q.setFirstResult(start);
		q.setMaxResults(size);

		List<M> list = q.list();

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public List<M> selectSearchPaged(M model, List<String> searchProperties, String searchKeyword,
			boolean withOrderBy, int start, int size) {

		Session session = null;
		List<M> list = null;

		try {
			session = getSession();
			list = selectSearchPaged(session, model, searchProperties, searchKeyword, withOrderBy, start,
					size);
		} catch (RuntimeException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		return (list == null) ? Collections.EMPTY_LIST : list;
	}

	public Criteria buildByExample(Session session, M model) {
		Criteria criteria = session.createCriteria(model.getClass());
		Order order = null;

		if (!StringTools.isEmptyString(model.getOrderColumn())) {
			if (StringTools.isEmptyString(model.getOrderMethod())) {
				order = Order.asc(model.getOrderColumn());
			} else {
				if (model.getOrderMethod().equals(AppConstants.ORDER_DESC)) {
					order = Order.desc(model.getOrderColumn());
				} else {
					order = Order.asc(model.getOrderColumn());
				}
			}
		}

		criteria.add(Example.create(model));

		if (order != null) {
			criteria.addOrder(order);
		}

		return criteria;
	}

	public String buildSearchQuery(M model, List<String> searchProperties, boolean withOrderBy,
			boolean isSelectCount) {
		StringBuilder query = new StringBuilder();
		final String modelName = StringTools.lowerCaseFirstLetter(model.getClass().getSimpleName());

		if (isSelectCount) {
			query.append("SELECT COUNT(" + modelName + ") FROM " + model.getClass().getName() + " "
					+ modelName);
		} else {
			query.append("FROM " + model.getClass().getName() + " " + modelName);
		}

		if (!StringTools.isEmptyString(model.getSearchKeyword())) {
			int index = 0;
			for (String property : searchProperties) {
				if (index == 0) {
					query.append(" WHERE (LOWER(" + modelName + "." + property + ") LIKE :"
							+ AppConstants.SEARCH_KEYWORD);
				} else {
					query.append(" OR LOWER(" + modelName + "." + property + ") LIKE :"
							+ AppConstants.SEARCH_KEYWORD);
				}
				index++;
			}
			query.append(" ) AND " + modelName + ".status = '" + AppConstants.DEFAULT_STATUS_ACTIVE + "' ");
		} else {
			query.append(" WHERE " + modelName + ".status = '" + AppConstants.DEFAULT_STATUS_ACTIVE + "' ");
		}

		if (withOrderBy) query.append(model.getOrderBy());

		return query.toString();
	}
}