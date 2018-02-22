/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import services.UserService;
import domain.Actor;
import domain.User;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	ActorService	actorService;
	@Autowired
	UserService		userService;


	// Constructors -----------------------------------------------------------

	public ActorController() {
		super();
	}

	//Saving --------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Actor actor;

		try {
			actor = this.actorService.findActorByPrincipal();

			result = new ModelAndView("actor/edit");
			result.addObject("actor", actor);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Displaying  ---------------------------------------------------------------		

	/**
	 * That method returns a model and view with the system users list
	 * 
	 * @param page
	 * @return ModelandView
	 * @author MJ
	 */
	@RequestMapping("/display")
	public ModelAndView display() {
		ModelAndView result;
		Actor actor;

		try {
			result = new ModelAndView("actor/display");
			actor = this.actorService.findActorByPrincipal();

			result.addObject("actor", actor);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

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

		result = this.createEditModelAndViewRegister(user);

		result.addObject("actionURL", "actor/register.do");

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
	public ModelAndView registerUser(@ModelAttribute("user") User user, final BindingResult binding, @RequestParam("confirmPassword") final String confirmPassword) {
		ModelAndView result;
		Authority auth;

		user = this.userService.reconstruct(user, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(user, "user.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.USER);
				Assert.isTrue(user.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(confirmPassword.equals(user.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(user);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndViewRegister(user, "user.username.error");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndViewRegister(user, "user.password.error");
				else
					result = this.createEditModelAndViewRegister(user, "user.commit.error");
			}

		return result;
	}

	//Ancillary methods ----------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;

		result = this.createEditModelAndView(actor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("actor", actor);

		result.addObject("message", messageCode);

		return result;
	}
	protected ModelAndView createEditModelAndViewRegister(final User user) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewRegister(final User user, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("user/register");
		result.addObject("message", messageCode);
		result.addObject("user", user);

		return result;

	}
}
