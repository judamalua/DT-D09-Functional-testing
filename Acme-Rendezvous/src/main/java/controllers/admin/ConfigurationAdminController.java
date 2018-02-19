
package controllers.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import controllers.AbstractController;
import domain.Configuration;

@Controller
@RequestMapping("/configuration/admin")
public class ConfigurationAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ConfigurationService	configurationService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		Configuration configuration;

		result = new ModelAndView("configuration/list");
		configuration = this.configurationService.findConfiguration();

		result.addObject("configuration", configuration);

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Configuration configuration;

		configuration = this.configurationService.findConfiguration();
		Assert.notNull(configuration);
		result = this.createEditModelAndView(configuration);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Configuration configuration, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(configuration, "configuration.params.error");
		else
			try {
				this.configurationService.save(configuration);
				result = new ModelAndView("redirect:/configuration/admin/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(configuration, "configuration.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Configuration configuration) {
		ModelAndView result;

		result = this.createEditModelAndView(configuration, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Configuration configuration, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("configuration/edit");
		result.addObject("configuration", configuration);

		result.addObject("message", messageCode);

		return result;

	}
}
