
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.ManagerService;
import domain.Manager;

@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ManagerService	managerService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------
	public ManagerController() {
		super();
	}
	//Register a new manager-----------------------------------------------------------
	/**
	 * This method returns a ModelAndView object with a new manager ready to be created.
	 * 
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerManager() {
		ModelAndView result;
		Manager manager;

		try {
			manager = this.managerService.create();

			result = this.createEditModelAndView(manager);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;

	}

	/**
	 * This method returns a ModelAndView object when trying to save a new manager into the system.
	 * 
	 * @param
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerManager(@Valid Manager manager, final BindingResult binding, @RequestParam("confirmPassword") final String confirmPassword) {
		ModelAndView result;
		final Authority auth;

		try {
			manager = this.managerService.reconstruct(manager, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		if (binding.hasErrors())
			result = this.createEditModelAndView(manager, "manager.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.MANAGER);
				Assert.isTrue(manager.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(confirmPassword.equals(manager.getUserAccount().getPassword()), "Passwords do not match");

				this.actorService.registerActor(manager);

				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndView(manager, "manager.username.error");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndView(manager, "manager.password.error");
				else
					result = this.createEditModelAndView(manager, "manager.commit.error");
			}
		return result;
	}

	//Ancilliary methods -----------------------------------------------------------
	private ModelAndView createEditModelAndView(final Manager manager) {
		ModelAndView result;

		result = this.createEditModelAndView(manager, null);

		return result;
	}
	private ModelAndView createEditModelAndView(final Manager manager, final String message) {
		ModelAndView result;

		result = new ModelAndView("manager/register");
		result.addObject("manager", manager);
		result.addObject("message", message);

		return result;
	}

}
