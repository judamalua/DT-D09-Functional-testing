/*
 * RendezvousController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.admin;

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
import services.AdministratorService;
import controllers.AbstractController;
import domain.Administrator;

@Controller
@RequestMapping("/actor/admin")
public class ActorAdminController extends AbstractController {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private AdministratorService	administratorService;


	// Constructors -----------------------------------------------------------

	public ActorAdminController() {
		super();
	}

	// Registering admin ------------------------------------------------------------
	/**
	 * That method registers an admin in the system and saves it.
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerAdmin() {
		ModelAndView result;
		final Administrator admin;

		admin = this.actorService.createAdmin();

		result = this.createEditModelAndViewRegister(admin);

		result.addObject("actionURL", "admin/register.do");

		return result;
	}

	//Edit an admin
	/**
	 * That method edits the profile of a admin
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editUser() {
		ModelAndView result;
		Administrator admin;

		admin = (Administrator) this.actorService.findActorByPrincipal();
		Assert.notNull(admin);
		result = this.createEditModelAndView(admin);

		return result;
	}

	//Saving admin ---------------------------------------------------------------------
	/**
	 * That method saves an admin in the system
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = {
		"save", "confirmPassword"
	})
	public ModelAndView registerAdministrator(@ModelAttribute("admin") Administrator admin, final BindingResult binding, @RequestParam("confirmPassword") final String confirmPassword) {
		ModelAndView result;
		Authority auth;

		admin = this.administratorService.reconstruct(admin, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(admin, "admin.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.ADMIN);
				Assert.isTrue(admin.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(confirmPassword.equals(admin.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(admin);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndViewRegister(admin, "admin.username.error");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndViewRegister(admin, "admin.password.error");
				else
					result = this.createEditModelAndViewRegister(admin, "admin.commit.error");
			}

		return result;
	}

	//Updating profile of a admin ---------------------------------------------------------------------
	/**
	 * 
	 * 
	 * That method update the profile of a user.
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView updateAdministrator(@ModelAttribute("actor") Administrator admin, final BindingResult binding) {
		ModelAndView result;

		try {
			admin = this.administratorService.reconstruct(admin, binding);
		} catch (final Throwable oops) { //Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(admin, "actor.params.error");
		else
			try {
				this.actorService.save(admin);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(admin, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Administrator admin) {
		ModelAndView result;

		result = this.createEditModelAndView(admin, null);

		return result;
	}
	protected ModelAndView createEditModelAndViewRegister(final Administrator admin) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Administrator admin, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("message", messageCode);
		result.addObject("actor", admin);

		return result;

	}

	protected ModelAndView createEditModelAndViewRegister(final Administrator admin, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("admin/register");
		result.addObject("message", messageCode);
		result.addObject("admin", admin);

		return result;

	}
}
