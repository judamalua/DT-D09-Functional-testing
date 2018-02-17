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

import java.util.ArrayList;
import java.util.Collection;

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
import services.CommentService;
import services.ConfigurationService;
import services.RendezvousService;
import domain.Actor;
import domain.Comment;
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

	@Autowired
	private CommentService			commentService;


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
		Actor actor;
		final int age;

		try {
			result = new ModelAndView("rendezvous/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			/**
			 * Age control
			 */
			if (!anonymous) {//Checks if there is the user is listing logged
				actor = this.actorService.findActorByPrincipal();
				age = this.actorService.getAge(actor);
				if (this.actorService.checkUserIsAdult(age))//If he has 18 or more he list all Final Rendezvouses
					rendezvouses = this.rendezvousService.findFinalRendezvouses(pageable);
				else
					// If he has less than 18 then he only list the final Rendezvouses without adult content
					rendezvouses = this.rendezvousService.findFinalWithoutAdultRendezvouses(pageable);
			} else
				//If no one is logged then list all final Rendezvouses
				rendezvouses = this.rendezvousService.findFinalRendezvouses(pageable);

			result.addObject("rendezvouses", rendezvouses.getContent());
			result.addObject("anonymous", anonymous);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Detailing ---------------------------------------------------------------		

	@RequestMapping("/detailed-rendezvous")
	public ModelAndView detailing(@RequestParam final int rendezvousId, @RequestParam final boolean anonymous) {
		ModelAndView result;
		Rendezvous rendezvous;
		Collection<User> users;
		Actor actor;
		User user;
		int age;
		boolean userHasCreatedRendezvous = false;
		boolean userHasRVSPdRendezvous = false;

		try {
			result = new ModelAndView("rendezvous/detailed-rendezvous");
			rendezvous = this.rendezvousService.findOne(rendezvousId);

			if (!anonymous) {
				actor = this.actorService.findActorByPrincipal();

				/**
				 * Age control
				 */
				if (rendezvous.getAdultOnly()) {//Checks if there is the user is listing logged
					actor = this.actorService.findActorByPrincipal();
					age = this.actorService.getAge(actor);
					Assert.isTrue(this.actorService.checkUserIsAdult(age));//The age must be 18 or more
				}

				if (actor instanceof User) {
					user = (User) actor;
					//Variable to check if button to see Questions must be shown in detailed rendezvous.
					userHasCreatedRendezvous = user.getCreatedRendezvouses().contains(rendezvous);
					userHasRVSPdRendezvous = rendezvous.getUsers().contains(user);
				}
			}

			users = new ArrayList<>();

			for (final Comment comment : rendezvous.getComments())
				users.add(this.commentService.getUserFromComment(comment));

			result.addObject("rendezvous", rendezvous);
			result.addObject("userHasCreatedRendezvous", userHasCreatedRendezvous);
			result.addObject("userHasRVSPdRendezvous", userHasRVSPdRendezvous);
			result.addObject("commentUsers", users);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:misc/403");
		}

		return result;
	}
}
