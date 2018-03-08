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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.ServiceService;
import domain.Actor;
import domain.Configuration;
import domain.DomainService;
import domain.Manager;

@Controller
@RequestMapping("/service")
public class ServiceController extends AbstractController {

	@Autowired
	private ServiceService			serviceService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public ServiceController() {
		super();
	}

	// Listing  ---------------------------------------------------------------    

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page, @RequestParam final boolean anonymous) {

		ModelAndView result;
		Page<DomainService> services;
		Pageable pageable;
		Configuration configuration;
		Actor actor;
		Collection<Boolean> managedServices;

		try {

			managedServices = new ArrayList<Boolean>();

			result = new ModelAndView("service/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			services = this.serviceService.findNotCancelledServices(pageable);

			if (!anonymous) {
				actor = this.actorService.findActorByPrincipal();
				//Get a list of boolean indicating if the manager manages this service
				if (actor instanceof Manager)
					for (final DomainService service : services.getContent())
						managedServices.add(((Manager) actor).getServices().contains(service));
			}
			result.addObject("services", services.getContent());
			result.addObject("managedServices", managedServices);
			result.addObject("requestURI", "service/list.do");
			result.addObject("page", page);
			result.addObject("pageNum", services.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
