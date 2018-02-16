/*
 * RendezvousController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.UserService;
import controllers.AbstractController;
import domain.Configuration;
import domain.User;

@Controller
@RequestMapping("/actor/user")
public class ActorUserController extends AbstractController {

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public ActorUserController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	/**
	 * That method returns a model and view with the system users list
	 * 
	 * @param page
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<User> users;
		Pageable pageable;
		Configuration configuration;

		result = new ModelAndView("user/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());
		users = this.userService.getUsers(pageable);

		result.addObject("users", users.getContent());
		return result;
	}

	//Edit an User
	/**
	 * That method edits the profile of a user
	 * 
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();
		Assert.notNull(user);
		result = this.createEditModelAndView(user);

		return result;
	}

	/**
	 * That method edits the profile of a user
	 * 
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final User user, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(user, "user.params.error");
		else
			try {
				this.userService.save(user);
				result = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(user, "user.commit.error");
			}
		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final User user, final BindingResult binding) {
		ModelAndView result;

		try {
			this.userService.delete(user);
			result = new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(user, "user.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final User user) {
		ModelAndView result;

		result = this.createEditModelAndView(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final User user, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("user/edit");
		result.addObject("message", messageCode);
		result.addObject("user", user);
		result.addObject("requestURI", "actor/user/edit.do");

		return result;

	}
}
