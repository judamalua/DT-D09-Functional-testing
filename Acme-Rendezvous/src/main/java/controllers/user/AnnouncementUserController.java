
package controllers.user;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.AnnouncementService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/announcement/user")
public class AnnouncementUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	AnnouncementService			announcementService;
	@Autowired
	ActorService			actorService;
	@Autowired
	RendezvousService		rendezvousService;
	@Autowired
	ConfigurationService	configurationService;


	// Listing ----------------------------------------------------

	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;
		Collection<Announcement> announcements = new HashSet<Announcement>();
		Collection<Rendezvous> rendezvouses;
		User user;
		try {

			result = new ModelAndView("announcement/list");
			user = (User) this.actorService.findActorByPrincipal();
			rendezvouses = user.getCreatedRendezvouses();
		
			for (Rendezvous rd : rendezvouses){
			announcements.addAll(rd.getAnnouncements());
			}

			result.addObject("announcements", announcements);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int announcementId) {
		ModelAndView result;
		Announcement announcement;
		int rendezvousId;

		try {
			announcement = this.announcementService.findOne(announcementId);
			Assert.notNull(announcement);
			rendezvousId = this.rendezvousService.getRendezvousByAnnouncement(announcementId).getId();
			
			//Check the user rspv that rendezvous.
			//TODO: Make a global method for all classes for this purpose
			User user = (User) actorService.findActorByPrincipal();
			this.rendezvousService.getRendezvousByAnnouncement(announcementId).getUsers().contains(user);
			

			result = this.createEditModelAndView(announcement);
			result.addObject("rendezvousId", rendezvousId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Announcement announcement;
		Rendezvous rendezvous;
		User user;

		try {
			user = (User) this.actorService.findActorByPrincipal();
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			announcement = this.announcementService.create();
			result = this.createEditModelAndView(announcement);

			Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));

			result.addObject("rendezvousId", rendezvousId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
			result.addObject("rendezvousId", rendezvousId);
		}

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam final int rendezvousId, @Valid final Announcement announcement, final BindingResult binding) {
		ModelAndView result;
		Rendezvous rendezvous;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(announcement, "announcement.params.error");
			result.addObject("rendezvousId", rendezvousId);
		} else
			try {
				rendezvous = this.rendezvousService.findOne(rendezvousId);
				Assert.notNull(rendezvous);
				this.announcementService.save(announcement, rendezvousId);
				result = new ModelAndView("redirect:list.do?rendezvousId=" + rendezvousId);

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(announcement, "announcement.commit.error");
				result.addObject("rendezvousId", rendezvousId);
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@RequestParam final int rendezvousId, final Announcement announcement, final BindingResult binding) {
		ModelAndView result;

		try {
			this.announcementService.delete(announcement);
			result = new ModelAndView("redirect:list.do?rendezvousId=" + rendezvousId);

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(announcement, "announcement.commit.error");
			result.addObject("rendezvousId", rendezvousId);
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Announcement announcement) {
		ModelAndView result;

		result = this.createEditModelAndView(announcement, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Announcement announcement, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("announcement/edit");
		result.addObject("announcement", announcement);

		result.addObject("message", messageCode);

		return result;

	}
}
