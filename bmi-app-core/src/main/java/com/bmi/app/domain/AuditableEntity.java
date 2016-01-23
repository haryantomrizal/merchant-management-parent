/**
 * 
 */
package com.bmi.app.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bmi.hibernate.domain.Entity;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
@MappedSuperclass
public abstract class AuditableEntity<ID extends Serializable> extends Entity<ID> {

	private static final long serialVersionUID = 7349096081473824835L;

	private String createBy;
	private Date createDate;
	private String lastUpdateBy;
	private Date lastUpdateDate;

	private int index;
	private String orderBy;
	private String orderColumn;
	private String orderMethod;
	private String searchKeyword;

	@Column(name = "create_date")
	@Temporal(TemporalType.DATE)
	public Date getCreationDate() {
		return createDate;
	}

	@Column(name = "created_by", length = 20)
	public String getCreateBy() {
		return createBy;
	}

	@Column(name = "last_updated_date")
	@Temporal(TemporalType.DATE)
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	@Column(name = "last_update_by", length = 20)
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public int getIndex() {
		return index;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public String getOrderMethod() {
		return orderMethod;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setCreationDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public void setOrderMethod(String orderMethod) {
		this.orderMethod = orderMethod;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}