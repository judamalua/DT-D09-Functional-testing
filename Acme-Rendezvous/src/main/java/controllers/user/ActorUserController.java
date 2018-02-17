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
import controllers.AbstractController;
import domain.User;

@Controller
@RequestMapping("/actor/user")
public class ActorUserController extends AbstractController {

	@Autowired
	private UserService		userService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public ActorUserController() {
		super();
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

	//Edit an User
	/**
	 * That method edits the profile of a user
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
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
			result = this.createEditModelAndViewRegister(user, "user.params.error");
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
					result = this.createEditModelAndViewRegister(user, "user.password.error");
				else
					result = this.createEditModelAndViewRegister(user, "user.commit.error");
			}

		return result;
	}

	//Updating profile of a user ---------------------------------------------------------------------
	/**
	 * That method update the profile of a user.
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = {
		"save"
	})
	public ModelAndView updateUser(@ModelAttribute("user") @Valid final User user, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(user, "user.params.error");
		else
			try {
				this.actorService.save(user);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndView(user, "user.password.error");
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
	protected ModelAndView createEditModelAndViewRegister(final User user) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final User user, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("user/edit");
		result.addObject("message", messageCode);
		result.addObject("user", user);

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
