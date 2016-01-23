package com.bmi.app.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
public class Role extends AuditableEntity<String> implements Serializable {

	private static final long serialVersionUID = -2985807529241170140L;

	private String name;
	private String description;
	private List<User> users;
	private List<Permission> permissions;

	@Override
	public String getId() {
		return name;
	}

	@Override
	public void setId(String id) {
		this.name = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	// @Override
	// public String toString() {
	// return BeanTools.beanToString(this, true);
	// }
}