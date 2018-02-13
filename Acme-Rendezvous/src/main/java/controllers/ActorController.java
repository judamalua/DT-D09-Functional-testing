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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import domain.Administrator;
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

	// Editing ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findActorByPrincipal();

		result = new ModelAndView("actor/edit");
		result.addObject("actor", actor);

		return result;
	}

	//Saving --------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") @Valid final Actor actor, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("actor/edit");
			result.addObject("actor", actor);
			result.addObject("message", "actor.params.error");
		} else
			try {
				this.actorService.save(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("actor/edit");
				result.addObject("actor", actor);
				result.addObject("message", "actor.params.error");
			}

		return result;
	}

	// Registering user ------------------------------------------------------------
	@RequestMapping(value = "/register-user", method = RequestMethod.GET)
	public ModelAndView registerExplorer() {
		ModelAndView result;
		Actor actor;

		actor = this.userService.create();

		result = this.createEditModelAndView(actor);

		result.addObject("actionURL", "actor/register-user.do");

		return result;
	}

	// Registering administrator ------------------------------------------------------------
	@RequestMapping(value = "/register-admin", method = RequestMethod.GET)
	public ModelAndView registerAdmin() {
		ModelAndView result;
		Actor actor;

		actor = this.userService.create();

		result = this.createEditModelAndView(actor);

		result.addObject("actionURL", "actor/register-admin.do");

		return result;
	}
	//Saving administrator ---------------------------------------------------------------------
	@RequestMapping(value = "/register-admin", method = RequestMethod.POST, params = {
		"save", "confirmPassword"
	})
	public ModelAndView registerAdmin(@ModelAttribute("actor") @Valid final Administrator admin, final BindingResult binding, @RequestParam("confirmPassword") final String confirmPassword) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndView(admin, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.USER);
				Assert.isTrue(admin.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(confirmPassword.equals(admin.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerAdmin(admin);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndView(admin, "actor.params.confirm.error");
				else
					result = this.createEditModelAndView(admin, "actor.commit.error");
			}

		return result;
	}
	//Saving user ---------------------------------------------------------------------
	@RequestMapping(value = "/register-user", method = RequestMethod.POST, params = {
		"save", "confirmPassword"
	})
	public ModelAndView registerUser(@ModelAttribute("actor") @Valid final User user, final BindingResult binding, @RequestParam("confirmPassword") final String confirmPassword) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndView(user, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.USER);
				Assert.isTrue(user.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(confirmPassword.equals(user.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerUser(user);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndView(user, "actor.params.confirm.error");
				else
					result = this.createEditModelAndView(user, "actor.commit.error");
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
		Administrator admin;

		if (actor instanceof User) {
			result = new ModelAndView("actor/register-user");
			result.addObject("authority", Authority.USER);
			result.addObject("requestUri", "actor/register-user.do");
			result.addObject("actor", actor);
		} else {
			admin = (Administrator) actor;
			result = new ModelAndView("actor/register-admin");
			result.addObject("authority", Authority.ADMIN);
			result.addObject("requestUri", "actor/register-admin.do");
			result.addObject("actor", admin);

		}

		result.addObject("message", messageCode);

		return result;
	}
}
