package com.bmi.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmi.mm.web.tool.AppConfig;

/**
 * @author Haryanto Muhamad Rizal
 *
 */
public abstract class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected final AppConfig appConfig = AppConfig.getInstance();
	
	public static final String LIST_PAGE = "_list";
	public static final String CREATE_PAGE = "_create";
	public static final String EDIT_PAGE = "_edit";
	public static final String PAGING_START = "_start";

	public static final String AJAX_LIST_PAGE = ".ajax.list";
	public static final String AJAX_DETAIL_PAGE = ".ajax.detail";

	protected String MODEL_NAME = null;
	
	public BaseController(String modelName) {
		this.MODEL_NAME = modelName;
	}

//	protected List<KeyValueBean> getOrderProperties(Class<?> model) {
//		List<KeyValueBean> orderList = new LinkedList<KeyValueBean>();
//		List<String> orderableList = AppConfig.getStrings(AppConstants.ORDERABLE + model.getSimpleName());
//		for (String key : orderableList) {
//			KeyValueBean keyValueBean = new KeyValueBean();
//			keyValueBean.setKey(key);
//			keyValueBean.setValue(StringTools.insertSpaceInCamelCase(true, key));
//			orderList.add(keyValueBean);
//		}
//		return orderList;
//	}
	
	protected String createJsonResponseForAutocomplete(String query, List<String> suggestions, List<String> data) {
		StringBuilder json = new StringBuilder();
		json.append("{query:'" + query + "',");
		
		json.append("suggestions:[");
		int i = 0;
		for (String string : suggestions) {
			if (i == (suggestions.size() - 1))
				json.append("'" + string + "'");
			else 
				json.append("'" + string + "',");
			i++;
		}
		json.append("],");
		
		i = 0;
		json.append("data:[");
		for (String string : data) {
			if (i == (data.size() - 1))
				json.append("'" + string + "'");
			else 
				json.append("'" + string + "',");
			i++;
		}		
		json.append("]}");
		
		logger.debug("AUTOCOMPLETE JSON RESPONSE >> \n {}", json.toString());
		return json.toString();
	}
	
	protected String getResponsePage(String packageName, String pageName) {
		StringBuilder sb = new StringBuilder();
		sb.append(packageName);
		sb.append("/");
		sb.append(MODEL_NAME);
		sb.append(pageName);
		return sb.toString();
	}
	
	protected String redirectPath(String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("redirect:");
		sb.append(path);
		return sb.toString();
	}

	protected String redirectToIndex() {
		return "redirect:/index.jsp";
	}

}
