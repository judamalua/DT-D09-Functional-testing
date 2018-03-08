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

import services.CategoryService;
import services.ConfigurationService;
import services.ServiceService;
import domain.Category;
import domain.Configuration;
import domain.DomainService;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	@Autowired
	private CategoryService			categoryService;

	@Autowired
	private ServiceService			serviceService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public CategoryController() {
		super();
	}

	// Listing  ---------------------------------------------------------------    

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page, @RequestParam(required = false) final Integer categoryId, @RequestParam(required = false) final Integer serviceId) {

		ModelAndView result;
		Page<Category> categories;
		Pageable pageable;
		Configuration configuration;
		Category category;
		DomainService service;

		try {
			result = new ModelAndView("category/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			Assert.isTrue(categoryId == null || serviceId == null);

			if (categoryId == null && serviceId == null) {
				categories = this.categoryService.findFirstLevelCategories(pageable);
				result.addObject("requestURI", "category/list.do?");
			} else if (categoryId == null && serviceId != null) {
				service = this.serviceService.findOne(serviceId);
				categories = this.serviceService.findCategoriesByService(service, pageable);
				result.addObject("requestURI", "category/list.do?serviceId=" + serviceId + "&");
			} else {
				category = this.categoryService.findOne(categoryId);
				categories = this.categoryService.findSubCategories(category, pageable);
				result.addObject("requestURI", "category/list.do?categoryId=" + categoryId + "&");
				if (category.getFatherCategory() != null)
					result.addObject("categoryId", category.getFatherCategory().getId());
			}

			result.addObject("categories", categories.getContent());

			result.addObject("page", page);
			result.addObject("pageNum", categories.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
