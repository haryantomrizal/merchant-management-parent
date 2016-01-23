/**
 * 
 */
package com.bmi.mm.web.controller;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Haryanto Muhamad Rizal
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	public static final String DEFAULT_ERROR_VIEW = "error";

	@ExceptionHandler({ Exception.class })
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e, Locale locale) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) throw e;

		// Otherwise setup and send the user to a default error-view.
		String viewName = DEFAULT_ERROR_VIEW;
		if (locale != null && (locale.getLanguage() != null || locale.getLanguage().trim() != "")) {
			viewName += "_"+ locale.getLanguage();
		}

		ModelAndView view = new ModelAndView(viewName);
		view.addObject("url", req.getRequestURL());
		view.addObject("timestamp", new Date());
		view.addObject("exception", e);
		return view;
	}
}