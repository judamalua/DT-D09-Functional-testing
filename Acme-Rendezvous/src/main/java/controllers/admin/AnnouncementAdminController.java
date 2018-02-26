
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.ConfigurationService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Rendezvous;

@Controller
@RequestMapping("/announcement/admin")
public class AnnouncementAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	AnnouncementService		announcementService;
	@Autowired
	RendezvousService		rendezvousService;
	@Autowired
	ConfigurationService	configurationService;


	// Deleting ------------------------------------------------------------------------

	/**
	 * Delete the announcement with id announcementId
	 * 
	 * @param announcementId
	 * @return a ModelAndView with error if any or to the detailedView of the rendezvous
	 * @author MJ
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam("announcementId") final int announcementId) {

		Announcement announcement;
		ModelAndView result;
		Rendezvous rendezvous;

		try {
			announcement = this.announcementService.findOne(announcementId);			//Checks that the rendezvous is valid
			rendezvous = this.rendezvousService.getRendezvousByAnnouncement(announcement.getId());
			Assert.notNull(announcement);
			this.announcementService.delete(announcement);
			result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
		} catch (final Throwable oops) {
			//If any error is made during whole process, it will make the user go to the 403 page
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

}
