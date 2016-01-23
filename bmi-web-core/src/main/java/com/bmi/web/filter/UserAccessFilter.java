package com.bmi.web.filter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bmi.app.domain.Permission;
import com.bmi.app.domain.User;
import com.bmi.mm.web.tool.AppConfig;
import com.bmi.mm.web.tool.AppConstants;
import com.bmi.mm.web.tool.SecurityTools;
import com.bmi.mm.web.tool.WebTools;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
@Component("userAccessFilter")
public class UserAccessFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(UserAccessFilter.class);

	private List<String> listAllAccessPath = null;
	private List<String> listNoSessionPath = null;
	private String projectName = null;
	private static boolean testUnit = false;

	private final AppConfig appConfig = AppConfig.getInstance();

	@Override
	public void init(FilterConfig config) throws ServletException {
		// set this Filter so it can be recognized by Spring container
		ServletContext servletContext = config.getServletContext();
		AutowireCapableBeanFactory autowireFactory = WebApplicationContextUtils.getWebApplicationContext(
				servletContext).getAutowireCapableBeanFactory();
		autowireFactory.configureBean(this, "userAccessFilter");

		testUnit = Boolean.valueOf(config.getInitParameter("testUnit"));
		logger.info("initParam testUnit = {}", testUnit);

//		projectName = appConfig.getString("project.name", "");
//		logger.info("initParam projectName = {}", projectName);

		WebTools.setContextPath(config.getServletContext().getContextPath());
		logger.info("init contextPath = {}", WebTools.getContextPath());

		WebTools.setRealPath(config.getServletContext().getRealPath("/"));
		logger.info("init resourcePath = {}", WebTools.getRealPath());

		initPath(config);
	}

	private void initPath(FilterConfig config) {
		String allAccessPath = config.getInitParameter("allAccessPath");
		listAllAccessPath = new LinkedList<String>();
		String[] allAccessPathArray = allAccessPath.split(";");
		for (int i = 0; i < allAccessPathArray.length; i++) {
			String string = allAccessPathArray[i];
			listAllAccessPath.add(string);
		}
		logger.info("initParam allAccessPath = {}", listAllAccessPath);

		String noSessionPath = config.getInitParameter("noSessionPath");
		listNoSessionPath = new LinkedList<String>();
		String[] noSessionPathArray = noSessionPath.split(";");
		for (int i = 0; i < noSessionPathArray.length; i++) {
			String string = noSessionPathArray[i];
			listNoSessionPath.add(string);
		}
		logger.info("initParam noSessionPath = {}", listNoSessionPath);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		logger.debug("access HTTP path {} and HTTP method {}", httpRequest.getServletPath(),
				httpRequest.getMethod());

		if (!testUnit) {
			boolean needSession = true;
			String requestPath = httpRequest.getServletPath();
			for (String baseUrl : listNoSessionPath) {
				if (requestPath.matches(baseUrl.replaceAll("\\*", "\\.\\*"))) {
					needSession = false;
					break;
				}
			}
			if (needSession) {
				// if (WebTools.isMainWebSessionActive(httpRequest)) {
				User loginUser = (User) httpRequest.getSession().getAttribute("login");
				if (loginUser != null) {

					List<Permission> userPermissionList = (List<Permission>) httpRequest.getSession()
							.getAttribute("permissionList");

//					if (loginUser != null && userPermissionList != null) {
					if (userPermissionList != null) {

						String path = httpRequest.getServletPath().replaceFirst("/", "").replaceAll("#", "");

						logger.debug("user {} trying to access HTTP path {} and HTTP method {}",
								loginUser.getUserName(), path, httpRequest.getMethod());

						boolean denied = SecurityTools.checkUserPermissionAccess(userPermissionList, path,
								httpRequest.getMethod());

						if (denied && listAllAccessPath.contains(httpRequest.getServletPath()) == false) {
							logger.debug("user permission {} denied", loginUser.getUserName());
							httpResponse.sendRedirect(WebTools.getContextPath() + "/403");
							return;
						}

						request.setAttribute(AppConstants.LOGIN_USER, loginUser);
						WebTools.updateMainWebSession(httpRequest);

					} else {
						logger.debug(
								"missing userPermissionList, userRoleList in {} session, will invalidate the session and redirect",
								loginUser.getUserName());

						httpRequest.getSession().invalidate();
						httpResponse.sendRedirect(WebTools.getContextPath() + "/"
								+ AppConstants.LOGIN_PAGE_URL);
						return;
					}
				} else {
					logger.debug("access path {} HAS FAILED BECAUSE NO SESSION, will redirect to {}",
							httpRequest.getServletPath(), AppConstants.LOGIN_PAGE_URL);

					httpResponse.sendRedirect(WebTools.getContextPath() + "/" + AppConstants.LOGIN_PAGE_URL);
					return;
				}
			}
		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		logger.info("destroying UserAccessFilter starting....");

		// LOGGER.info("destroying Web Hibernate resources....");
		// HibernateFactory.getSessionFactory().close();
		// LOGGER.info("destroying Hibernate COMPLETE");

		logger.info("destroying UserAccessFilter COMPLETE");
	}
	
	public static void main(String[] args) {
		String baseUrl = "/";
		String requestPath = "/";
		System.out.println(baseUrl.replaceAll("\\*", "\\.\\*"));
		System.out.println(requestPath.matches(baseUrl.replaceAll("\\*", "\\.\\*")));
	}
}
