package com.bmi.app.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
public class Permission extends AuditableEntity<String> implements Serializable {

	private static final long serialVersionUID = 1666671536467949662L;

	private String id;
	private String name;
	private String title;
	private String description;
	private String httpPath;
	private String httpMethod;
	private String iconFile;
	private Boolean asMenu;
	private Integer menuOrder;
	private Permission parent;

	private transient List<Permission> childMenuList;
	private transient String tab;

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public List<Permission> getChildMenuList() {
		return childMenuList;
	}

	public void setChildMenuList(List<Permission> childMenuList) {
		this.childMenuList = childMenuList;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHttpPath() {
		return httpPath;
	}

	public void setHttpPath(String httpPath) {
		this.httpPath = httpPath;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getIconFile() {
		return iconFile;
	}

	public void setIconFile(String iconFile) {
		this.iconFile = iconFile;
	}

	public Boolean getAsMenu() {
		return asMenu;
	}

	public void setAsMenu(Boolean asMenu) {
		this.asMenu = asMenu;
	}

	public Integer getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	public Permission getParent() {
		return parent;
	}

	public void setParent(Permission parent) {
		this.parent = parent;
	}

	// @Override
	// public String toString() {
	// return BeanTools.beanToString(this, true);
	// }
}
