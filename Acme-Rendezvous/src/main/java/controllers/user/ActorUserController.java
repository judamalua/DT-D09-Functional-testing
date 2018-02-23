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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.UserService;
import controllers.AbstractController;
import domain.User;

@Controller
@RequestMapping("/actor/user")
public class ActorUserController extends AbstractController {

	@Autowired
	private ActorService	actorService;
	@Autowired
	private UserService		userService;


	// Constructors -----------------------------------------------------------

	public ActorUserController() {
		super();
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

	//Updating profile of a user ---------------------------------------------------------------------
	/**
	 * That method update the profile of a user.
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView updateUser(@ModelAttribute("actor") User user, final BindingResult binding) {
		ModelAndView result;
		try {
			user = this.userService.reconstruct(user, binding);
		} catch (final Throwable oops) {//Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(user, "user.params.error");
		else
			try {
				this.actorService.save(user);
				result = new ModelAndView("redirect:/user/display.do?anonymous=false");
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

		result = new ModelAndView("actor/edit");
		result.addObject("message", messageCode);
		result.addObject("actor", user);

		return result;

	}

}
