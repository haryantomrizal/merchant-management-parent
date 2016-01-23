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
import com.bmi.app.domain.Role;
import com.bmi.app.service.PermissionService;
import com.bmi.app.service.RoleService;
import com.bmi.mm.web.tool.WebTools;

@Controller
@RequestMapping("/role")
public class RoleController extends CrudController<Role> {

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	public RoleController() {
		super("role");
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView listPage() {
		ModelAndView mv = new ModelAndView("app/role_list");
		mv.addObject("roles", roleService.find(new Role()));
		return mv;
	}

	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(HttpServletRequest req, HttpServletResponse rsp, @RequestParam String id) {
		roleService.softDelete(id);
		return "redirect:/role/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView createPage(final Role role) {
		ModelAndView mv = new ModelAndView("app/role_create");
		mv.addObject("allPermissions", WebTools.orderPermission(permissionService.find(new Permission())));
		return mv;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ModelAndView create(final Role role, final BindingResult bindingResult, HttpServletRequest req,
			HttpServletResponse rsp) {
		ModelAndView mv = new ModelAndView("redirect:/role/list");
		if (bindingResult.hasErrors()) {
			mv.setViewName("app/role_create");
			return mv;
		}
		setCreatedBy(req, role);
		roleService.insert(role);
		return mv;
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView editPage(final Role role, HttpServletRequest req, HttpServletResponse rsp,
			@RequestParam String id) {
		ModelAndView mv = new ModelAndView("app/role_edit");
		roleService.load(role, id, true);
		mv.addObject("allPermissions", WebTools.orderPermission(permissionService.find(new Permission())));
		return mv;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public ModelAndView edit(final Role role, @RequestParam String id, final BindingResult bindingResult,
			HttpServletRequest req, HttpServletResponse rsp) {
		ModelAndView resultPath = new ModelAndView("redirect:/role/list");
		if (bindingResult.hasErrors()) {
			resultPath.setViewName("app/role_edit");
		}
		setLastUpdatedBy(req, role);
		roleService.update(role);
		return resultPath;
	}
}
