package com.bmi.app.dao;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bmi.app.domain.Role;

@Repository
public class RoleDao extends AuditableBaseDao<Role, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleDao.class);

	@Override
	public void initialize(Role proxy) {
		Hibernate.initialize(proxy.getUsers());
		Hibernate.initialize(proxy.getPermissions());
	}

//	@Override
//	public Role findById(Session session, String id, boolean init) {
//    	Role role = null;
//    	try {
//    		session = getSession();
//	        role = (Role) session.get(entityClass, id);
//	        // different approach with Role to init the child, because more than one
//	        if (init && role != null) {
//	        	/**
//	        	 * TODO
//	        	 */
//	            Hibernate.initialize(role.getUsers());
//	            Hibernate.initialize(role.getPermissions());
//	        }
//    	} catch (RuntimeException e) {
//    		LOGGER.error(e.getLocalizedMessage(), e.getCause());
//		}
//        return role;
//	}
}