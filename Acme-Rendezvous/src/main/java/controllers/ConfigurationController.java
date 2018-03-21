
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.ConfigurationService;

@Controller
@RequestMapping("/configuration")
public class ConfigurationController {

	@Autowired
	ConfigurationService	configurationService;


	/**
	 * Sends the business name, if an exeption occurs, it sends null
	 * 
	 * @author Daniel Diment
	 * 
	 * @return
	 *         The business name separated by commas
	 */
	@RequestMapping(value = "/getBusinessName", method = RequestMethod.GET)
	public @ResponseBody
	String getBusinessName() {
		String result;
		try {
			result = this.configurationService.findConfiguration().getBusinessNameFirst() + "," + this.configurationService.findConfiguration().getBusinessNameLast();
		} catch (final Exception e) {
			result = "null";
		}
		return result;
	}
}
