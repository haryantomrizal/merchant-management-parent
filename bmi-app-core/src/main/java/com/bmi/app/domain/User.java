package com.bmi.app.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
public class User extends AuditableEntity<Long> implements Serializable {

	private static final long serialVersionUID = -5231190455273657285L;

	private Long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String phone;
	private Date lastLogin;
	private String lastLoginAddress;
	private String profilePicture;

	// @ExcludeGson
	private byte[] picture;

	// @ExcludeGson
	private List<Role> roles;

	private boolean enabled = true;
	private boolean accountNonExpired = true;
	private Date accountExpiryDate;
	private boolean credentialsNonExpired = true;
	private Date credentialsExpiryDate;
	private boolean accountNonLocked = true;

	public User() {}
	
	public User(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastLoginAddress() {
		return lastLoginAddress;
	}

	public void setLastLoginAddress(String lastLoginAddress) {
		this.lastLoginAddress = lastLoginAddress;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Date getAccountExpiryDate() {
		return accountExpiryDate;
	}

	public void setAccountExpiryDate(Date accountExpiryDate) {
		this.accountExpiryDate = accountExpiryDate;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Date getCredentialsExpiryDate() {
		return credentialsExpiryDate;
	}

	public void setCredentialsExpiryDate(Date credentialsExpiryDate) {
		this.credentialsExpiryDate = credentialsExpiryDate;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	// @Override
	// public String toString() {
	// return BeanTools.beanToString(this, true);
	// }
}