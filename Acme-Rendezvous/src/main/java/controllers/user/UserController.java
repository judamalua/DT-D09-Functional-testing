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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.ConfigurationService;
import services.UserService;
import controllers.AbstractController;
import domain.Configuration;
import domain.User;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public UserController() {
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

	// Registering user ------------------------------------------------------------
	/**
	 * That method registers an user in the system and saves it.
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerExplorer() {
		ModelAndView result;
		final User user;

		user = this.userService.create();

		result = this.createEditModelAndView(user);

		result.addObject("actionURL", "actor/register.do");

		return result;
	}

	//Edit an User
	/**
	 * That method edits the profile of a user
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/user/edit", method = RequestMethod.GET)
	public ModelAndView editUser() {
		ModelAndView result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();
		Assert.notNull(user);
		result = this.createEditModelAndView(user);

		return result;
	}

	//Saving user ---------------------------------------------------------------------
	/**
	 * That method saves an user in the system
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = {
		"save", "confirmPassword"
	})
	public ModelAndView registerUser(@ModelAttribute("user") @Valid final User user, final BindingResult binding, @RequestParam("confirmPassword") final String confirmPassword) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndView(user, "user.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.USER);
				Assert.isTrue(user.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(confirmPassword.equals(user.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(user);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndView(user, "user.params.confirm.error");
				else
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

		return result;

	}
}
