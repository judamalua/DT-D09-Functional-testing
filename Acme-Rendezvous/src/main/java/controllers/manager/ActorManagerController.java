
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ManagerService;
import controllers.AbstractController;
import domain.Manager;
import forms.ManagerForm;

@Controller
@RequestMapping("/actor/manager")
public class ActorManagerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ManagerService	managerService;
	@Autowired
	private ActorService	actorService;


	// Constructor ---------------------------------------------------------------
	public ActorManagerController() {
		super();
	}

	//Edit manager ----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editManager() {
		ModelAndView result;
		Manager manager;
		ManagerForm managerForm;

		manager = (Manager) this.actorService.findActorByPrincipal();
		Assert.notNull(manager);
		managerForm = this.managerService.deconstruct(manager);

		result = this.createEditModelAndView(managerForm);

		return result;
	}

	//Updating profile of a manager --------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView updateManager(@ModelAttribute("actor") final ManagerForm managerForm, final BindingResult binding) {
		ModelAndView result;
		Manager manager = null;
		try {
			manager = this.managerService.reconstruct(managerForm, binding);
		} catch (final Throwable oops) {//Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(managerForm, "actor.params.error");
		else
			try {
				this.actorService.save(manager);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(managerForm, "actor.commit.error");
			}

		return result;
	}

	//Ancillary methods ----------------------------------------------------------------
	private ModelAndView createEditModelAndView(final ManagerForm managerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(managerForm, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final ManagerForm managerForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("actor", managerForm);
		result.addObject("message", message);
		result.addObject("principalIsManager", true);

		return result;
	}

}
