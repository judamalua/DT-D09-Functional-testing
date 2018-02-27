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

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/similar/user")
public class SimilarUserController extends AbstractController {

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------------

	public SimilarUserController() {
		super();
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
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Rendezvous similar, final BindingResult binding) {
		ModelAndView result;
		User user;

		try {
			similar = this.rendezvousService.reconstructSimilar(similar, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
			return result;
		}

		if (binding.hasErrors())
			result = this.createEditModelAndView(similar, "rendezvous.params.error");
		else
			try {
				user = (User) this.actorService.findActorByPrincipal();
				this.rendezvousService.save(similar);

				result = new ModelAndView("redirect:/rendezvous/user/list.do");
				result.addObject("userId", user.getId());
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("You must be over 18 to save a Rendezvous with adultOnly"))
					result = this.createEditModelAndView(similar, "rendezvous.adult.error");
				else
					result = this.createEditModelAndView(similar, "rendezvous.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Rendezvous similar) {
		ModelAndView result;
		User user;
		result = this.createEditModelAndView(similar, null);

		//Se le pasa el parametro adult que indicara si el usuario es mayor a 18 años
		user = (User) this.actorService.findActorByPrincipal();
		result.addObject("adult", user.getBirthDate().before((new DateTime()).minusYears(18).toDate()));

		return result;
	}

	protected ModelAndView createEditModelAndView(final Rendezvous similar, final String messageCode) {
		ModelAndView result;
		Collection<Rendezvous> similars;

		result = new ModelAndView("rendezvous/edit");
		similars = this.rendezvousService.findFinalRendezvouses();
		similars.remove(similar);

		result.addObject("message", messageCode);
		result.addObject("rendezvouses", similars);
		result.addObject("rendezvous", similar);
		result.addObject("requestURI", "similar/user/edit.do");

		return result;

	}
}
