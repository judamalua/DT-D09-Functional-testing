
package controllers.admin;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SliderService;
import controllers.AbstractController;
import domain.Slider;

@Controller
@RequestMapping("/slider/admin")
public class SliderAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	SliderService	sliderService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		Collection<Slider> sliders;

		result = new ModelAndView("slider/list");
		sliders = this.sliderService.findAll();

		result.addObject("sliders", sliders);

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Slider slider;

		try {
			slider = this.sliderService.create();
			result = this.createEditModelAndView(slider);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sliderId) {
		ModelAndView result;
		Slider slider;

		try {
			slider = this.sliderService.findOne(sliderId);
			Assert.notNull(slider);
			result = this.createEditModelAndView(slider);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Slider slider, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(slider, "slider.params.error");
		else
			try {
				this.sliderService.save(slider);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(slider, "slider.commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Slider slider, final BindingResult binding) {
		ModelAndView result;
		try {
			this.sliderService.delete(slider);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(slider, "slider.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Slider slider) {
		ModelAndView result;

		result = this.createEditModelAndView(slider, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Slider slider, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("slider/edit");
		result.addObject("slider", slider);

		result.addObject("message", messageCode);

		return result;

	}
}
