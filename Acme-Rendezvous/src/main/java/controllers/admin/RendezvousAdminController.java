
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.RendezvousService;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous/admin")
public class RendezvousAdminController {

	// Services -------------------------------------------------------
	@Autowired
	RendezvousService		rendezvousService;

	@Autowired
	ActorService			actorService;

	@Autowired
	ConfigurationService	configurationService;


	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam final int rendezvousId) {
		ModelAndView result;
		final Rendezvous rendezvous;

		try {
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			this.rendezvousService.delete(rendezvous);
			result = new ModelAndView("redirect:/rendezvous/list.do?anonymous=false");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}
}
