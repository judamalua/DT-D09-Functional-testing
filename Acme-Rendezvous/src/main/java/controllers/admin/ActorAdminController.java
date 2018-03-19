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
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.AdministratorService;
import controllers.AbstractController;
import domain.Administrator;
import forms.UserAdminForm;

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
		UserAdminForm admin;

		admin = new UserAdminForm();

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
		final UserAdminForm adminForm;

		admin = (Administrator) this.actorService.findActorByPrincipal();
		Assert.notNull(admin);
		adminForm = this.actorService.deconstruct(admin);
		result = this.createEditModelAndView(adminForm);

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
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView registerAdministrator(@ModelAttribute("admin") final UserAdminForm actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;
		Administrator admin = null;
		try {
			admin = this.administratorService.reconstruct(actor, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "admin.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.ADMIN);
				Assert.isTrue(admin.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(actor.getConfirmPassword().equals(admin.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(admin);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndViewRegister(actor, "admin.username.error");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndViewRegister(actor, "admin.password.error");
				else
					result = this.createEditModelAndViewRegister(actor, "admin.commit.error");
			}

		return result;
	}

	//Updating profile of a admin ---------------------------------------------------------------------
	/**
	 * 
	 * 
	 * That method update the profile of a administrator.
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView updateAdministrator(@ModelAttribute("actor") final UserAdminForm actor, final BindingResult binding) {
		ModelAndView result;
		Administrator admin = null;

		try {
			admin = this.administratorService.reconstruct(actor, binding);
		} catch (final Throwable oops) { //Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(actor, "actor.params.error");
		else
			try {
				this.actorService.save(admin);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actor, "actor.commit.error");
			}

		return result;
	}
	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final UserAdminForm admin) {
		ModelAndView result;

		result = this.createEditModelAndView(admin, null);

		return result;
	}
	protected ModelAndView createEditModelAndViewRegister(final UserAdminForm admin) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserAdminForm admin, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("message", messageCode);
		result.addObject("actor", admin);

		return result;

	}

	protected ModelAndView createEditModelAndViewRegister(final UserAdminForm admin, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("admin/register");
		result.addObject("message", messageCode);
		result.addObject("admin", admin);

		return result;

	}
}
