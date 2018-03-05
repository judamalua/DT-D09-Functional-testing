/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.DomainService;

@Controller
@RequestMapping("/service/admin")
public class ServiceAdminController extends AbstractController {

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public ServiceAdminController() {
		super();
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int serviceId) {
		ModelAndView result;
		DomainService service;
		Actor actor;

		try {
			service = this.serviceService.findOne(serviceId);
			Assert.notNull(service);
			actor = this.actorService.findActorByPrincipal();
			Assert.isTrue(actor instanceof Administrator);
			Assert.isTrue(!service.getCancelled());

			service.setCancelled(true);
			this.serviceService.save(service);
			result = new ModelAndView("redirect:/service/list.do?anonymous=false");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
