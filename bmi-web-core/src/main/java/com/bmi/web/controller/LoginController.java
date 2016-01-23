/**
 * 
 */
package com.bmi.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Haryanto Muhamad Rizal
 *
 */
@Controller
public class LoginController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		if (error != null) {
			logger.debug("AN ERROR: {}", error);
			model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION", error));
		}
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		ModelAndView model = new ModelAndView();
		model.addObject("message", messageSource.getMessage("logout.success", null, locale));
		model.setViewName("login");
		/*try {
			logger.info("invalidating session");
			request.getSession().invalidate();
		} catch(IllegalStateException ex) {
			logger.info("Session already invalidated");
		}*/
		return model;
	}

	private String getErrorMessage(HttpServletRequest request, String key, String defaultVal) {
		Exception exception = (Exception) request.getSession().getAttribute(key);
		return (exception != null) ? exception.getMessage() : defaultVal;
	}
}