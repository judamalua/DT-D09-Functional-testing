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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.RendezvousService;
import services.UserService;
import controllers.AbstractController;
import domain.Configuration;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/rendezvous/user")
public class RendezvousUserController extends AbstractController {

	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public RendezvousUserController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false) final Integer userId, @RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<Rendezvous> rendezvouses;
		User user;
		Pageable pageable;
		Configuration configuration;
		try {
			if (userId != null) {
				user = this.userService.findOne(userId);
				Assert.notNull(user);
			} else
				user = (User) this.actorService.findActorByPrincipal();

			result = new ModelAndView("rendezvous/list");
			configuration = this.configurationService.findConfiguration();

			pageable = new PageRequest(page, configuration.getPageSize());
			rendezvouses = this.rendezvousService.findCreatedRendezvouses(user, pageable);

			result.addObject("rendezvouses", rendezvouses.getContent());
			result.addObject("requestURI", "rendezvous/user/list.do");
			result.addObject("pageNum", rendezvouses.getTotalPages());
			result.addObject("page", page);
			result.addObject("anonymous", false);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Editing ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		User user;

		try {
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			Assert.isTrue(!rendezvous.getFinalMode());
			user = (User) this.actorService.findActorByPrincipal();
			Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));
			result = this.createEditModelAndView(rendezvous);
			result.addObject("rendezvous", rendezvous);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;
		User user;

		try {
			rendezvous = this.rendezvousService.reconstruct(rendezvous, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
			return result;
		}
		//rendezvous.getSimilars().remove(null);
		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous, "rendezvous.params.error");
		else
			try {
				rendezvous.setFinalMode(!rendezvous.getFinalMode());
				user = (User) this.actorService.findActorByPrincipal();

				if (rendezvous.getId() != 0)
					Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));

				this.rendezvousService.save(rendezvous);

				result = new ModelAndView("redirect:list.do");
				result.addObject("userId", user.getId());
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("You must be over 18 to save a Rendezvous with adultOnly"))
					result = this.createEditModelAndView(rendezvous, "rendezvous.adult.error");
				else if (oops.getMessage().contains("Must be in future"))
					result = this.createEditModelAndView(rendezvous, "rendezvous.future.error");
				else
					result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
			}

		return result;
	}
	// Deleting ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;
		try {
			rendezvous = this.rendezvousService.reconstruct(rendezvous, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		try {
			this.rendezvousService.delete(rendezvous);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
		}

		return result;
	}

	// Creating ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Rendezvous rendezvous;

		try {
			rendezvous = this.rendezvousService.create();
			this.actorService.checkUserLogin();

			result = this.createEditModelAndView(rendezvous);
			result.addObject("rendezvous", rendezvous);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous) {
		ModelAndView result;
		result = this.createEditModelAndView(rendezvous, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous, final String messageCode) {
		ModelAndView result;
		Collection<Rendezvous> similars;
		User user;

		result = new ModelAndView("rendezvous/edit");
		similars = this.rendezvousService.findFinalRendezvouses();
		similars.remove(rendezvous);
		//Se le pasa el parametro adult que indicara si el usuario es mayor a 18 años
		user = (User) this.actorService.findActorByPrincipal();

		result.addObject("message", messageCode);
		result.addObject("rendezvouses", similars);
		result.addObject("rendezvous", rendezvous);
		result.addObject("adult", this.actorService.checkUserIsAdult(user));
		result.addObject("requestURI", "rendezvous/user/edit.do");

		return result;

	}
}
