package com.bmi.app.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.bmi.app.domain.User;

@Repository
public class UserDao extends AuditableBaseDao<User, Long> {

//	@Override
//	public User findById(Session session, Long id, boolean init) {
//		User user = null;
//		try {
//			user = (User) getSession().get(entityClass, id);
//		} catch (RuntimeException e) {
//			logger.error(e.getLocalizedMessage(), e.getCause());
//		}
//		return user;
//	}

	@SuppressWarnings("unchecked")
	public User findByUserName(String username) {
		List<User> users = new ArrayList<User>();
		Session session = null;
		User user = null;
		try {
			session = getSession();
			users = session.createQuery("from User where userName=?").setParameter(0, username).list();
			if (users.size() > 0) {
				user = users.get(0);
				Hibernate.initialize(user.getRoles());
			}
		} catch (HibernateException ex) {
			logger.error("ERROR: {}", ex.getMessage(), ex);
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		} catch (Exception ex) {
			logger.error("ERROR: {}", ex.getMessage(), ex);
//		}  catch (HibernateException ex) {
//			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
		return user;
	}

	@Override
	public void initialize(User entity) {
		Hibernate.initialize(entity.getRoles());
		Hibernate.initialize(entity.getRoles());
	}
}