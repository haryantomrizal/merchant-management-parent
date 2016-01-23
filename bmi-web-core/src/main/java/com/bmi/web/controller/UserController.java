package com.bmi.web.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bmi.app.domain.Role;
import com.bmi.app.domain.User;
import com.bmi.app.service.RoleService;
import com.bmi.app.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController extends CrudController<User> {

	@Autowired
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	public UserController() {
		super("user");
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView listPage() {
		ModelAndView mv = new ModelAndView("app/user_list");
		mv.addObject("allUsers", userService.find(new User()));
		return mv;
	}

	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(HttpServletRequest req, HttpServletResponse rsp, @RequestParam Long id) {
		userService.softDelete(id);
		return "redirect:/user/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView createPage(final User user) {
		ModelAndView mv = new ModelAndView("app/user_create");
		mv.addObject("allRoles", roleService.find(new Role()));
		return mv;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(final User user, final BindingResult bindingResult, HttpServletRequest req,
			HttpServletResponse rsp) {
		String resultPath = "redirect:/user/list";

		readRole(req, user);

		// Map<String, String> errorMessage = validateInput(user, true);
		Map<String, String> errorMessage = new HashMap<String, String>();

		logger.debug("USER ROLE SIZE: {}", user.getRoles().size());
		for (Role role : user.getRoles()) {
			logger.debug("User Role: {}", role.getName());
		}

		if (errorMessage.size() > 0) {
			// setReferenceData(req);
			req.setAttribute("errorMessage", errorMessage);
			resultPath = "app/user_create";
		} else {
			setCreatedBy(req, user);
			user.setPassword(passwordEncoder.encode("password"));
			userService.insert(user);
			// userService.saveOrUpdate(user);
		}

		return resultPath;
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView editPage(final User user, @RequestParam Long id, HttpServletRequest req,
			HttpServletResponse rsp) {
		ModelAndView mv = new ModelAndView("app/user_edit");
		userService.load(user, id, true);
//		List<Role> roles = new LinkedList<Role>();
		for (Role userRole : user.getRoles()) {
			roleService.load(userRole, userRole.getId());
		}
		mv.addObject("allRoles", roleService.find(new Role()));
		return mv;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(final User user, HttpServletRequest req, HttpServletResponse rsp, @RequestParam Long id) {
		String resultPath = "redirect:/user/list";
		User entity = new User();
		
		if (id != null) {
			userService.load(entity, id);
		}
		baseReadParameter(req, entity);
		readRole(req, entity);

		List<Role> roles = roleService.find(new Role());
		for (Role role : roles) {
			logger.debug("ROLE: {}, ROLE NAME: {}, hash= {}", role, role.getName(), role.hashCode());
			logger.debug("USER ROLES contains ROLE: {}", entity.getRoles().contains(role));
			for (Role ur : entity.getRoles()) {
				logger.debug("USER ROLE NAME: {}", ur.getName());
				logger.debug("USER ROLE: {}, hash= {}", ur, ur.hashCode());
			}
		}
		// Map<String, String> errorMessage = validateInput(user, false);
		Map<String, String> errorMessage = new HashMap<String, String>();

		if (errorMessage.size() > 0) {
			// setReferenceData(req);
			req.setAttribute("errorMessage", errorMessage);
			resultPath = "app/user_edit";
		} else {
			setLastUpdatedBy(req, entity);
			userService.update(entity);
		}

		req.setAttribute(MODEL_NAME, entity);

		return resultPath;
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.GET)
	public String resetPassword(HttpServletRequest req, HttpServletResponse rsp, @RequestParam Long id) {
		User user = userService.findById(id);
		if (user != null) {
			setLastUpdatedBy(req, user);
			user.setPassword(passwordEncoder.encode("password"));
			userService.update(user);
			logger.debug("user {} password has been reset ", user.getUserName());
		}
		return "redirect:/user/list";
	}

	private void readRole(HttpServletRequest req, User user) {
		List<Role> list = new LinkedList<Role>();
		List<Role> roles = (List<Role>) roleService.find(new Role());
		if (roles != null) {
			for (Role role : roles) {
				if (req.getParameter("role_" + role.getId()) != null) {
					list.add(role);
				}
			}
		}
		user.setRoles(list);
	}
}
