package com.bmi.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bmi.app.domain.Permission;
import com.bmi.app.service.PermissionService;
import com.bmi.mm.web.tool.WebTools;

@Controller
@RequestMapping("/permission")
public class PermissionController extends CrudController<Permission> {

	@Autowired
	private PermissionService permissionService;

	public PermissionController() {
		super("permission");
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView listPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("app/permission_list");
		mv.addObject("permissions", permissionService.find(new Permission()));
		return mv;
	}

	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
		permissionService.softDelete(id);
		return "redirect:/permission/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView createPage(final Permission permission) {
		ModelAndView mv = new ModelAndView("app/permission_create");
		mv.addObject("allPermissions", WebTools.orderPermission(permissionService.find(new Permission())));
		return mv;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ModelAndView create(final Permission permission, @RequestParam String id,
			final BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("redirect:/permission/list");
		if (bindingResult.hasErrors()) { return createPage(permission); }
		setCreatedBy(request, permission);
		permissionService.insert(permission);
		return mv;
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView editPage(final Permission permission, final @RequestParam String id) {
		ModelAndView mv = new ModelAndView("app/permission_edit");
		permissionService.load(permission, id, true);
		mv.addObject("allPermissions", WebTools.orderPermission(permissionService.find(new Permission())));
		return mv;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public ModelAndView edit(final Permission permission, final @RequestParam String id,
			final BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("redirect:/permission/list");
		if (bindingResult.hasErrors()) {
			mv.setViewName("app/permission_edit");
			mv.addObject("allPermissions", WebTools.orderPermission(permissionService.find(new Permission())));
			return mv;
		}
		setLastUpdatedBy(request, permission);
		permissionService.update(permission);
		return mv;
	}
}
