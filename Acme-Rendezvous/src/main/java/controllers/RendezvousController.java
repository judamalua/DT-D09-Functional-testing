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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.RendezvousService;
import domain.Configuration;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous")
public class RendezvousController extends AbstractController {

	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public RendezvousController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<Rendezvous> rendezvouses;
		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("rendezvous/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());
		rendezvouses = this.rendezvousService.findFinalRendezvouses(pageable);

		result.addObject("rendezvouses", rendezvouses.getContent());
		return result;
	}
	// Detailing ---------------------------------------------------------------		

	@RequestMapping("/detailed-rendezvous")
	public ModelAndView detailing(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;

		result = new ModelAndView("rendezvous/detailed-rendezvous");
		rendezvous = this.rendezvousService.findOne(rendezvousId);

		result.addObject("rendezvous", rendezvous);

		return result;
	}
}
