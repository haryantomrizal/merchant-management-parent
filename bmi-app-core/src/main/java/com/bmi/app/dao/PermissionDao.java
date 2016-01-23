package com.bmi.app.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bmi.app.domain.Permission;

@Repository
public class PermissionDao extends AuditableBaseDao<Permission, String> {

//	@Override
//	public Permission findById(Session session, String id, boolean init) {
//		Permission permission = null;
//    	try {
//    		session = getSession();
//	        permission = (Permission) session.get(entityClass, id);
//	        // different approach with Role to init the child, because more than one
//	        if (init && permission != null) {
//	            Hibernate.initialize(permission.getParentMenu());
//	            Hibernate.initialize(permission.getChildMenuList());
//	        }
//    	} catch (RuntimeException e) {
//    		logger.error(e.getLocalizedMessage(), e.getCause());
//		}
//        return permission;
//	}

	@Override
	public void initialize(Permission proxy) {
		Hibernate.initialize(proxy.getParent());
		Hibernate.initialize(proxy.getChildMenuList());
	}

	@SuppressWarnings("unchecked")
	public List<Permission> getChildList(Permission parentMenu) {
    	List<Permission> result = null;
    	Session session = null;
    	
    	try {
    		session = getSession();
    		
    		StringBuilder stringQuery = new StringBuilder();
    		stringQuery.append("FROM ");
    		stringQuery.append(Permission.class.getName());
    		stringQuery.append(" menu ");
    		stringQuery.append("WHERE menu.parentMenu = :parentMenu AND menu.enabled = true");
    		stringQuery.append("ORDER BY menu.name, ");
    		stringQuery.append("menu.menuOrder, ");
    		stringQuery.append("menu.httpPath, ");
    		stringQuery.append("menu.httpMethod ");
    		
	        Query query = session.createQuery(stringQuery.toString());
	        
	        query.setEntity("parentMenu", parentMenu);
	        result = (List<Permission>) query.list();
    	} catch (RuntimeException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

    	return (result != null) ? result : Collections.EMPTY_LIST;
    }

	@SuppressWarnings("unchecked")
    public List<Permission> getNullParent(Boolean enabled) {
    	List<Permission> result = null;
    	Session session = null;
    	
    	try {
    		session = getSession();
    		
    		StringBuilder stringQuery = new StringBuilder();
    		stringQuery.append("FROM ");
    		stringQuery.append(Permission.class.getName());
    		stringQuery.append(" menu ");
    		stringQuery.append("WHERE menu.parentMenu IS NULL AND menu.enabled = :enabled ");
    		stringQuery.append("ORDER BY menu.name, ");
    		stringQuery.append("menu.menuOrder, ");
    		stringQuery.append("menu.httpPath, ");
    		stringQuery.append("menu.httpMethod ");

	        Query query = session.createQuery(stringQuery.toString());
	        query.setBoolean("enabled", enabled);
	        result = (List<Permission>) query.list();
    	} catch (RuntimeException e) {
			logger.error(e.getLocalizedMessage(), e.getCause());
		}

    	return (result != null) ? result : Collections.EMPTY_LIST;
    }
}
