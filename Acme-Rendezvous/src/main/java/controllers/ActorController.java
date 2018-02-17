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

	//Saving --------------------------------------------------------------------
	@RequestMapping(value = "/edit-admin", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") @Valid final Administrator actor, final BindingResult binding) {
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

	//Saving --------------------------------------------------------------------
	@RequestMapping(value = "/edit-user", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") @Valid final User actor, final BindingResult binding) {
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
}
