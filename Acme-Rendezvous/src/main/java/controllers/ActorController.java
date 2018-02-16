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

	// Registering administrator ------------------------------------------------------------
	@RequestMapping(value = "/register-user", method = RequestMethod.GET)
	public ModelAndView registerAdmin() {
		ModelAndView result;
		User user;

		user = this.userService.create();

		result = this.createEditModelAndView(user);

		result.addObject("actionURL", "actor/register-admin.do");

		return result;
	}

	//Saving user ---------------------------------------------------------------
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
				this.actorService.registerActor(user);
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
}
