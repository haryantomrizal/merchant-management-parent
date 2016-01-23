/**
 * 
 */
package com.bmi.web.security.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bmi.app.domain.User;
import com.bmi.app.service.UserService;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
public class UserAuthenticationFilter extends BasicAuthenticationFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private UserService userService;

	/**
	 * @deprecated
	 */
	public UserAuthenticationFilter() {
		super();
	}

	public UserAuthenticationFilter(AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationManager, authenticationEntryPoint);
	}

	public UserAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
		super.onSuccessfulAuthentication(request, response, authResult);
		logger.debug("Get user '{}' from database", authResult.getName());
		User user = userService.findByUserName(authResult.getName());
		Hibernate.initialize(user.getRoles());

		logger.debug("Attaching user object into session attribute");
		request.getSession().setAttribute("USER", user);
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}