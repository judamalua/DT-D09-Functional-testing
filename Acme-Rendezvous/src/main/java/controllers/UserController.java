/*
 * RendezvousController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.RendezvousService;
import services.UserService;
import domain.Configuration;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public UserController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	/**
	 * That method returns a model and view with the system users list
	 * 
	 * @param page
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final boolean anonymous, @RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<User> users;
		Pageable pageable;
		Configuration configuration;

		result = new ModelAndView("user/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());
		users = this.userService.getUsers(pageable);

		result.addObject("users", users.getContent());
		result.addObject("anonymous", anonymous);
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
	public ModelAndView display(@RequestParam(required = false) final Integer userId, @RequestParam final boolean anonymous, @RequestParam(defaultValue = "0") final int rsvpPage, @RequestParam(defaultValue = "0") final int createdRendezvousPage) {
		ModelAndView result;
		User user;
		Page<Rendezvous> rsvpRendezvouses;
		Page<Rendezvous> createdRendezvouses;
		Pageable rsvpPageable, createdPageable;
		Configuration configuration;

		result = new ModelAndView("actor/display");
		if (userId != null)
			user = (User) this.actorService.findOne(userId);
		else
			user = (User) this.actorService.findActorByPrincipal();

		//Getting pages of collections
		configuration = this.configurationService.findConfiguration();
		rsvpPageable = new PageRequest(rsvpPage, configuration.getPageSize());
		createdPageable = new PageRequest(createdRendezvousPage, configuration.getPageSize());

		createdRendezvouses = this.rendezvousService.findCreatedRendezvouses(user, createdPageable);
		rsvpRendezvouses = this.rendezvousService.findRSVPRendezvouses(user, rsvpPageable);

		result.addObject("user", user);
		result.addObject("anonymous", anonymous);
		result.addObject("rsvpRendezvouses", rsvpRendezvouses);
		result.addObject("createdRendezvouses", createdRendezvouses);

		result.addObject("createdRendezvousPage", rsvpRendezvouses.getNumber());
		result.addObject("rsvpPage", rsvpRendezvouses.getNumber());
		result.addObject("rsvpPageNum", rsvpRendezvouses.getTotalPages());
		result.addObject("createdPageNum", createdRendezvouses.getTotalPages());

		return result;
	}

	//	// Displaying  ---------------------------------------------------------------		
	//
	//	/**
	//	 * That method returns a model and view with the system users list
	//	 * 
	//	 * @param page
	//	 * @return ModelandView
	//	 * @author MJ
	//	 */
	//	@RequestMapping("/display-principal")
	//	public ModelAndView display(@RequestParam final boolean anonymous, @RequestParam(defaultValue = "0") final int rsvpPage, @RequestParam(defaultValue = "0") final int createdRendezvousPage) {
	//		ModelAndView result;
	//		User actor;
	//		Page<Rendezvous> rsvpRendezvouses;
	//		Page<Rendezvous> createdRendezvouses;
	//		Pageable rsvpPageable, createdPageable;
	//		Configuration configuration;
	//
	//		result = new ModelAndView("actor/display");
	//		actor = (User) this.actorService.findActorByPrincipal();
	//
	//		//Getting pages of collections
	//		configuration = this.configurationService.findConfiguration();
	//		rsvpPageable = new PageRequest(rsvpPage, configuration.getPageSize());
	//		createdPageable = new PageRequest(createdRendezvousPage, configuration.getPageSize());
	//
	//		createdRendezvouses = this.rendezvousService.findCreatedRendezvouses(actor, createdPageable);
	//		rsvpRendezvouses = this.rendezvousService.findRSVPRendezvouses(actor, rsvpPageable);
	//
	//		result.addObject("actor", actor);
	//		result.addObject("anonymous", anonymous);
	//		result.addObject("rsvpRendezvouses", rsvpRendezvouses);
	//		result.addObject("createdRendezvouses", createdRendezvouses);
	//
	//		result.addObject("createdRendezvousPage", rsvpRendezvouses.getNumber());
	//		result.addObject("rsvpPage", rsvpRendezvouses.getNumber());
	//		result.addObject("rsvpPageNum", rsvpRendezvouses.getTotalPages());
	//		result.addObject("createdPageNum", createdRendezvouses.getTotalPages());
	//
	//		return result;
	//	}
}
