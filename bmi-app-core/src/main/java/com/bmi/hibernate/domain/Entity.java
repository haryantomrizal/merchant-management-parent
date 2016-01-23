package com.bmi.hibernate.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
@MappedSuperclass
public abstract class Entity<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 8593047123552270020L;

	private boolean deleted;
	private Long version;

	@Id
	public abstract ID getId();

	public abstract void setId(ID id);

	@Column
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Version
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@SuppressWarnings({ "rawtypes" })
	public boolean equals(Object object) {
		if (!(object instanceof Entity)) { return false; }
		Entity rhs = (Entity) object;
		return new EqualsBuilder().append(this.getId(), rhs.getId()).append(this.deleted, rhs.deleted)
				.append(this.version, rhs.version).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-665150779, 1286593253).append(this.getId()).append(this.deleted)
				.append(this.version).toHashCode();
	}
}