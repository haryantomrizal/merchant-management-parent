package com.bmi.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.bmi.app.domain.AuditableEntity;
import com.bmi.app.domain.User;
import com.bmi.hibernate.domain.Entity;
import com.bmi.mm.web.tool.AppConstants;
import com.bmi.mm.web.tool.StringTools;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
@SuppressWarnings({ "rawtypes" })
public abstract class CrudController<ET extends AuditableEntity> extends BaseController {

	public CrudController(String modelName) {
		super(modelName);
	}

	protected User getLoginUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(AppConstants.LOGIN_USER);
	}

	protected void setCreatedBy(HttpServletRequest request, AuditableEntity bean) {
		User user = getLoginUser(request);
		bean.setCreateBy(user != null ? user.getUserName() : AppConstants.DEFAULT_USER);
		bean.setCreateDate(new Date());
	}

	protected void setLastUpdatedBy(HttpServletRequest request, AuditableEntity bean) {
		User user = getLoginUser(request);
		bean.setLastUpdateBy(user != null ? user.getUserName() : AppConstants.DEFAULT_USER);
		bean.setLastUpdateDate(new Date());
	}

	// protected ET readParameterList(HttpServletRequest request, ET bean) {
	//
	// String order = ServletRequestUtils.getStringParameter(request, "order", "");
	// String beanProperty = ServletRequestUtils.getStringParameter(request, "beanProperty", "");
	//
	// String defProp = AppConstants.DEFAULT_POJO_PROPERTY_ORDER;
	// String defOrder = AppConstants.DEFAULT_ORDER;
	//
	// if (!StringTools.isEmptyString(beanProperty)) defProp = beanProperty;
	// if (!StringTools.isEmptyString(order)) defOrder = order;
	//
	// bean.setOrderBy(" ORDER BY " + MODEL_NAME + "." + defProp + " " + defOrder + " ");
	//
	// String search = ServletRequestUtils.getStringParameter(request, "searchKeyword", null);
	//
	// if (!StringTools.isEmptyString(search)) search = "%" + search.toLowerCase() + "%";
	// else search = null;
	//
	// bean.setOrderColumn(MODEL_NAME + "." + defProp);
	// bean.setDeleted(true);
	// bean.setSearchKeyword(search);
	//
	// return bean;
	// }

	protected void baseReadParameter(HttpServletRequest request, ET model) {

		Map<String, Object> propertyMap = null;
		Method[] methods = model.getClass().getDeclaredMethods();

		try {
			propertyMap = (Map<String, Object>) PropertyUtils.describe(model);
		} catch (IllegalAccessException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (NoSuchMethodException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		if (!propertyMap.isEmpty()) {
			for (Entry<String, Object> entry : propertyMap.entrySet()) {
				if (!entry.getKey().equals("class")) {

					Method getMethod = null;
					for (Method method : methods) {
						if (method.getName().equals("get" + StringTools.upperCaseFirstLetter(entry.getKey()))
								|| method.getName().equals(
										"is" + StringTools.upperCaseFirstLetter(entry.getKey()))) {
							getMethod = method;
							break;
						}
					}

					if (getMethod != null) {
						try {

							logger.trace("getMethod.getReturnType() = {}", getMethod.getReturnType()
									.getName());

							if (getMethod.getReturnType().equals(String.class)) {
								String value = ServletRequestUtils.getStringParameter(request,
										entry.getKey(), null);
								if (value != null) {
									PropertyUtils.setProperty(model, entry.getKey(), value);
								}
								logger.trace("set getKey = {}, class String with value = {}", entry.getKey(),
										value);

							} else if (getMethod.getReturnType().equals(Integer.class)) {
								Integer value = ServletRequestUtils.getIntParameter(request, entry.getKey());
								if (value != null) {
									PropertyUtils.setProperty(model, entry.getKey(), value);
								}
								logger.trace("set getKey = {}, class Integer with value = {}",
										entry.getKey(), value);

							} else if (getMethod.getReturnType().equals(Long.class)) {
								Long value = ServletRequestUtils.getLongParameter(request, entry.getKey());
								if (value != null) {
									PropertyUtils.setProperty(model, entry.getKey(), value);
								}
								logger.trace("set getKey = {}, class Long with value = {}", entry.getKey(),
										value);

							} else if (getMethod.getReturnType().equals(Float.class)) {
								Float value = ServletRequestUtils.getFloatParameter(request, entry.getKey());
								if (value != null) {
									PropertyUtils.setProperty(model, entry.getKey(), value);
								}
								logger.trace("set getKey = {}, class Float with value = {}", entry.getKey(),
										value);

							} else if (getMethod.getReturnType().equals(Double.class)) {
								Double value = ServletRequestUtils
										.getDoubleParameter(request, entry.getKey());
								if (value != null) {
									PropertyUtils.setProperty(model, entry.getKey(), value);
								}
								logger.trace("set getKey = {}, class Double with value = {}", entry.getKey(),
										value);

							} else if (getMethod.getReturnType().equals(Date.class)) {
								Date value = null;
								String dateString = ServletRequestUtils.getStringParameter(request,
										entry.getKey(), null);
								if (!StringTools.isEmptyString(dateString)) {
									value = AppConstants.SDF_DEFAULT.parse(dateString);
								}
								if (value != null) {
									PropertyUtils.setProperty(model, entry.getKey(), value);
								}
								logger.trace("set getKey = {}, class Date with value = {}", entry.getKey(),
										value);

							} else if (getMethod.getReturnType().equals(Boolean.class)) {
								String booleanString = request.getParameter(entry.getKey());
								if (!StringTools.isEmptyString(booleanString)) {
									PropertyUtils.setProperty(model, entry.getKey(), Boolean.TRUE);
								} else {
									PropertyUtils.setProperty(model, entry.getKey(), Boolean.FALSE);
								}
								logger.trace("set getKey = {}, class Boolean with value = {}",
										entry.getKey(), booleanString);

							} else if (Entity.class.isAssignableFrom(getMethod.getReturnType())) {
								logger.trace("setting getKey = {}, class {} ...", entry.getKey(),
										getMethod.getReturnType());

								// String id = request.getParameter(entry.getKey());
								// if (!StringTools.isEmptyString(id)) {
								// Session session =
								// HibernateFactory.getSessionFactory().openSession();
								// Object baseBean = session.get(getMethod.getReturnType(), id);
								// if (baseBean != null) {
								// PropertyUtils.setProperty(model, entry.getKey(), baseBean);
								// }
								// if (session != null) session.close();
								// logger.trace("set getKey = {}, class {}", entry.getKey(),
								// getMethod.getReturnType().toString());
								// }
							}

						} catch (IllegalAccessException e) {
							logger.error(e.getLocalizedMessage(), e);
						} catch (InvocationTargetException e) {
							logger.error(e.getLocalizedMessage(), e);
						} catch (NoSuchMethodException e) {
							logger.error(e.getLocalizedMessage(), e);
						} catch (ServletRequestBindingException e) {
							logger.error(e.getLocalizedMessage(), e);
						} catch (ParseException e) {
							logger.error(e.getLocalizedMessage(), e);
						} catch (NumberFormatException e) {
						}
					}
				}
			}
		}
	}
}