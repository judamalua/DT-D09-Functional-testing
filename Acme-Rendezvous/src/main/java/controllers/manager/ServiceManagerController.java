/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.manager;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import services.ConfigurationService;
import services.ManagerService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Actor;
import domain.Category;
import domain.Configuration;
import domain.DomainService;
import domain.Manager;

@Controller
@RequestMapping("/service/manager")
public class ServiceManagerController extends AbstractController {

	@Autowired
	private ServiceService			serviceService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private CategoryService			categoryService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public ServiceManagerController() {
		super();
	}

	// Listing  ---------------------------------------------------------------    

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) {

		ModelAndView result;
		final Page<DomainService> services;
		Pageable pageable;
		Configuration configuration;
		Manager manager;
		Collection<Boolean> managedServices;

		try {

			managedServices = new ArrayList<Boolean>();

			result = new ModelAndView("service/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			manager = (Manager) this.actorService.findActorByPrincipal();
			services = this.managerService.findServicesByManager(manager, pageable);

			//Get a list of boolean indicating if the manager manages this service
			for (final DomainService service : services.getContent())
				managedServices.add(manager.getServices().contains(service));

			result.addObject("services", services.getContent());
			result.addObject("managedServices", managedServices);
			result.addObject("requestURI", "service/manager/list.do?");
			result.addObject("page", page);
			result.addObject("pageNum", services.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int serviceId) {
		ModelAndView result;
		DomainService service;
		Manager manager;

		try {
			service = this.serviceService.findOne(serviceId);
			Assert.notNull(service);
			manager = (Manager) this.actorService.findActorByPrincipal();
			Assert.isTrue(manager.getServices().contains(service));
			Assert.isTrue(service.getRequests().size() == 0);

			result = this.createEditModelAndView(service);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		DomainService service;
		Actor actor;

		try {
			actor = this.actorService.findActorByPrincipal();
			Assert.isTrue(actor instanceof Manager);

			service = this.serviceService.create();
			result = this.createEditModelAndView(service);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving -------------------------------------------------------------------

	//Added transactional for the save and delete methods
	@Transactional
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("service") DomainService service, final BindingResult binding) {
		ModelAndView result;
		DomainService savedService = null;
		Actor actor;

		try {
			savedService = service;
			service = this.serviceService.reconstruct(service, binding);
		} catch (final Throwable oops) {//Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(savedService, "service.params.error");
		else
			try {
				actor = this.actorService.findActorByPrincipal();
				Assert.isTrue(actor instanceof Manager);
				Assert.isTrue(service.getRequests().size() == 0);
				if (service.getId() == 0)
					Assert.isTrue(!service.getCancelled());
				else
					Assert.isTrue(this.serviceService.findOne(service.getId()) != null);
				this.serviceService.save(service);

				if (savedService.getId() != 0) {
					savedService = this.serviceService.findOne(savedService.getId());
					this.serviceService.delete(savedService);
				}
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(service, "service.commit.error");
			}

		return result;
	}
	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("service") DomainService service, final BindingResult binding) {
		ModelAndView result;

		try {
			Assert.notNull(service);
			service = this.serviceService.findOne(service.getId());
			Assert.isTrue(service.getRequests().size() == 0);
			this.serviceService.delete(service);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(service, "service.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final DomainService service) {
		ModelAndView result;

		result = this.createEditModelAndView(service, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final DomainService service, final String messageCode) {
		ModelAndView result;
		Collection<Category> categories = null;

		categories = this.categoryService.findAll();

		result = new ModelAndView("service/edit");
		result.addObject("service", service);
		result.addObject("message", messageCode);
		result.addObject("categories", categories);

		return result;

	}
}
