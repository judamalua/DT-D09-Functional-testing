
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;

@Controller
@RequestMapping("/cookie")
public class CookieController {

	@Autowired
	ConfigurationService	configurationService;


	@RequestMapping(value = "/policy", method = RequestMethod.GET)
	public ModelAndView policy() {
		return new ModelAndView("cookie/policy");
	}

	@RequestMapping(value = "/ajax/es", method = RequestMethod.GET)
	public @ResponseBody
	String es() {
		String result;
		try {
			result = this.configurationService.findConfiguration().getCookies_es();
		} catch (final Exception e) {
			result = "";
		}
		return result;
	}
	@RequestMapping(value = "/ajax/en", method = RequestMethod.GET)
	public @ResponseBody
	String en() {
		String result;
		try {
			result = this.configurationService.findConfiguration().getCookies_eng();
		} catch (final Exception e) {
			result = "";
		}
		return result;
	}
}
