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
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.UserService;
import domain.Actor;
import domain.User;
import forms.UserAdminForm;

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

	// Displaying  ---------------------------------------------------------------		

	/**
	 * That method returns the profile of the actor logged
	 * That method returns a model and view with personal info of an Actor(Not an user)
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
		UserAdminForm user;

		user = new UserAdminForm();

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
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView registerUser(@ModelAttribute("actor") final UserAdminForm actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;
		User user = null;

		try {
			user = this.userService.reconstruct(actor, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "user.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.USER);
				Assert.isTrue(user.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(actor.getConfirmPassword().equals(user.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(user);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndViewRegister(actor, "user.username.error");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndViewRegister(actor, "user.password.error");
				else
					result = this.createEditModelAndViewRegister(actor, "user.commit.error");
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
	protected ModelAndView createEditModelAndViewRegister(final UserAdminForm user) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewRegister(final UserAdminForm user, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("user/register");
		result.addObject("message", messageCode);
		result.addObject("actor", user);

		return result;

	}
}
