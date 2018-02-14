/*
 * CustomerController.java
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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.RendezvousService;
import domain.Configuration;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/rendezvous")
public class RendezvousController extends AbstractController {

	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public RendezvousController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page, @RequestParam final boolean anonymous) {
		ModelAndView result;
		Page<Rendezvous> rendezvouses;
		Pageable pageable;
		Configuration configuration;
		User user;
		final int age;

		result = new ModelAndView("rendezvous/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());

		/**
		 * Age control
		 */
		if (!anonymous) {//Checks if there is the user is listing logged
			user = (User) this.actorService.findActorByPrincipal();
			age = this.actorService.getAge(user);
			if (age >= 18)//If he has 18 or more he list all Final Rendezvouses
				rendezvouses = this.rendezvousService.findFinalRendezvouses(pageable);
			else
				// If he has less than 18 then he only list the final Rendezvouses without adult content
				rendezvouses = this.rendezvousService.findFinalWithoutAdultRendezvouses(pageable);
		} else
			//If no one is logged then list all final Rendezvouses
			rendezvouses = this.rendezvousService.findFinalRendezvouses(pageable);

		result.addObject("rendezvouses", rendezvouses.getContent());
		return result;
	}
	// Detailing ---------------------------------------------------------------		

	@RequestMapping("/detailed-rendezvous")
	public ModelAndView detailing(@RequestParam final int rendezvousId, @RequestParam final boolean anonymous) {
		ModelAndView result;
		Rendezvous rendezvous;
		User user;
		int age;
		try {
			result = new ModelAndView("rendezvous/detailed-rendezvous");
			rendezvous = this.rendezvousService.findOne(rendezvousId);

			/**
			 * Age control
			 */
			if (!anonymous) {//Checks if there is the user is listing logged
				user = (User) this.actorService.findActorByPrincipal();
				age = this.actorService.getAge(user);
				Assert.isTrue(age >= 18);//The age must be 18 or more
			}

			result.addObject("rendezvous", rendezvous);
		} catch (final Throwable oops) {
			result = new ModelAndView("misc/403");
		}

		return result;
	}
}
