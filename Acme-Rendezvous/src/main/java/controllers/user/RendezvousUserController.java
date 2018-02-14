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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
	public ModelAndView list(@RequestParam final int userId, @RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<Rendezvous> rendezvouses;
		User user;
		Pageable pageable;
		Configuration configuration;

		result = new ModelAndView("rendezvous/list");
		configuration = this.configurationService.findConfiguration();
		user = this.userService.findOne(userId);
		Assert.notNull(user);
		pageable = new PageRequest(page, configuration.getPageSize());
		rendezvouses = this.rendezvousService.findCreatedRendezvouses(user, pageable);

		result.addObject("rendezvouses", rendezvouses.getContent());
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
			user = (User) this.actorService.findActorByPrincipal();
			Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));
			result = this.createEditModelAndView(rendezvous);
			result.addObject("rendezvous", rendezvous);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:misc/403");
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
			result = new ModelAndView("redirect:misc/403");
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

		result = new ModelAndView("rendezvous/edit");
		result.addObject("message", messageCode);
		result.addObject("rendezvous", rendezvous);

		return result;

	}
}
