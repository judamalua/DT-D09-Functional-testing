
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.AdministratorService;
import controllers.AbstractController;
import domain.Administrator;

@Controller
@RequestMapping("/actor/administrator")
public class ActorAdminController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;


	// Registering administrator ------------------------------------------------------------
	@RequestMapping(value = "/register-admin", method = RequestMethod.GET)
	public ModelAndView registerAdmin() {
		ModelAndView result;
		Administrator administrator;

		administrator = this.administratorService.create();

		result = this.createEditModelAndView(administrator);
		result.addObject("requestURI", "actor/administrator/register-admin.do");

		return result;
	}

	//Saving administrator ---------------------------------------------------------------------
	@RequestMapping(value = "/register-admin", method = RequestMethod.POST, params = {
		"save", "confirmPassword"
	})
	public ModelAndView registerAdmin(@ModelAttribute("actor") @Valid final Administrator admin, final BindingResult binding, @RequestParam("confirmPassword") final String confirmPassword) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndView(admin, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.ADMIN);
				Assert.isTrue(admin.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(confirmPassword.equals(admin.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(admin);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match")) {
					result = this.createEditModelAndView(admin, "actor.password.error");
					result.addObject("requestURI", "actor/administrator/register-admin.do");
				} else {
					result = this.createEditModelAndView(admin, "actor.commit.error");
					result.addObject("requestURI", "actor/administrator/register-admin.do");
				}
			}

		return result;
	}
	//Edit an Administrator
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editAdministrator() {
		ModelAndView result;
		Administrator admin;

		admin = (Administrator) this.actorService.findActorByPrincipal();
		Assert.notNull(admin);
		result = this.createEditModelAndView(admin);
		result.addObject("requestURI", "actor/administrator/edit.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Administrator administrator, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(administrator, "administrator.params.error");
		else
			try {
				this.actorService.save(administrator);
				result = new ModelAndView("redirect:/");
				result.addObject("requestURI", "actor/administrator/edit.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(administrator, "administrator.commit.error");
				result.addObject("requestURI", "actor/administrator/edit.do");
			}
		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Administrator administrator, final BindingResult binding) {
		ModelAndView result;

		try {
			this.actorService.delete(administrator);
			result = new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(administrator, "administrator.commit.error");
			result.addObject("requestURI", "actor/administrator/edit.do");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Administrator administrator) {
		ModelAndView result;

		result = this.createEditModelAndView(administrator, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Administrator administrator, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("administrator/edit");
		result.addObject("message", messageCode);
		result.addObject("administrator", administrator);

		return result;

	}

}
