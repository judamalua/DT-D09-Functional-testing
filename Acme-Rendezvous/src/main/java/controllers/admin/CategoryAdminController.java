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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Category;

@Controller
@RequestMapping("/category/admin")
public class CategoryAdminController extends AbstractController {

	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public CategoryAdminController() {
		super();
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView result;
		Category category;
		Actor actor;

		try {
			category = this.categoryService.findOne(categoryId);
			Assert.notNull(category);
			actor = this.actorService.findActorByPrincipal();
			Assert.isTrue(actor instanceof Administrator);

			result = this.createEditModelAndView(category);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Category category;
		Actor actor;

		try {
			actor = this.actorService.findActorByPrincipal();
			Assert.isTrue(actor instanceof Administrator);

			category = this.categoryService.create();
			result = this.createEditModelAndView(category);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Category category, final BindingResult binding) {
		ModelAndView result;

		try {
			category = this.categoryService.reconstruct(category, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
			return result;
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(category, "category.params.error");
		else
			try {
				this.categoryService.save(category);
				result = new ModelAndView("redirect:/category/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(category, "category.commit.error");
			}

		return result;
	}
	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Category service, final BindingResult binding) {
		ModelAndView result;

		try {
			service = this.categoryService.reconstruct(service, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
			return result;
		}
		try {
			this.categoryService.delete(service);
			result = new ModelAndView("redirect:/category/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(service, "category.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Category category) {
		ModelAndView result;

		result = this.createEditModelAndView(category, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String messageCode) {
		ModelAndView result;
		Collection<Category> fatherCategories;

		fatherCategories = this.categoryService.findAll();
		result = new ModelAndView("category/edit");
		result.addObject("category", category);
		result.addObject("categories", fatherCategories);
		result.addObject("message", messageCode);

		return result;

	}
}
