
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/law")
public class LawController extends AbstractController {

	@RequestMapping("/termsAndConditions")
	public ModelAndView termsAndConditions() {
		ModelAndView result;

		result = new ModelAndView("law/termsAndConditions");

		return result;
	}

}
