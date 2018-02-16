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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	protected ModelAndView createEditModelAndView(final User user) {
		ModelAndView result;

		result = this.createEditModelAndView(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final User user, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("actor", user);
		result.addObject("message", messageCode);
		result.addObject("requestUri", "actor/user/edit.do");

		return result;

	}
	protected ModelAndView createEditModelAndView(final Administrator admin) {
		ModelAndView result;

		result = this.createEditModelAndView(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Administrator admin, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("actor", admin);
		result.addObject("message", messageCode);
		result.addObject("requestUri", "actor/user/edit.do");

		return result;

	}
}
