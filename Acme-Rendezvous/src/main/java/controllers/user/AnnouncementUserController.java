
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AnnouncementService;
import services.ConfigurationService;
import services.RendezvousService;
import services.UserService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/announcement/user")
public class AnnouncementUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	AnnouncementService		announcementService;
	@Autowired
	ActorService			actorService;
	@Autowired
	RendezvousService		rendezvousService;
	@Autowired
	ConfigurationService	configurationService;
	@Autowired
	UserService				userService;


	// Listing ----------------------------------------------------
	/**
	 * List the announcements of RSVP Rendezvouses
	 * 
	 * @return a ModelAndView object with all the announcements of the RSVP Rendezvouses
	 * @author MJ
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Announcement> announcements = new ArrayList<Announcement>();
		final Collection<Rendezvous> associatedRendezvouses = new ArrayList<Rendezvous>();
		Collection<Announcement> rendezvousAnnouncements;
		Collection<Rendezvous> rendezvouses;
		User user;
		try {

			result = new ModelAndView("announcement/list");
			user = (User) this.actorService.findActorByPrincipal();
			rendezvouses = user.getRsvpRendezvouses();

			/**
			 * Adding the associated rendezvouses to an announcement
			 */
			for (final Rendezvous rendezvous : rendezvouses) {
				rendezvousAnnouncements = rendezvous.getAnnouncements();
				announcements.addAll(rendezvous.getAnnouncements());
				if (!rendezvousAnnouncements.isEmpty())
					for (int i = 0; i < rendezvousAnnouncements.size(); i++)
						associatedRendezvouses.add(rendezvous);
			}

			result.addObject("announcements", announcements);
			result.addObject("requestURI", "announcement/user/list.do");
			result.addObject("associatedRendezvouses", associatedRendezvouses);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	/**
	 * List the announcements of created Rendezvouses
	 * 
	 * @return a ModelAndView object with all the announcements of the created Rendezvouses
	 * @author MJ
	 */
	@RequestMapping(value = "/list-created")
	public ModelAndView listcreated() {
		ModelAndView result;
		final Collection<Announcement> announcements = new ArrayList<Announcement>();
		Collection<Rendezvous> rendezvouses;
		final Collection<Rendezvous> associatedRendezvouses = new ArrayList<Rendezvous>();
		Collection<Announcement> rendezvousAnnouncements;
		User user;
		try {

			result = new ModelAndView("announcement/list-created");
			user = (User) this.actorService.findActorByPrincipal();
			rendezvouses = user.getCreatedRendezvouses();

			/**
			 * Adding the associated rendezvouses to an announcement
			 */
			for (final Rendezvous rendezvous : rendezvouses) {
				rendezvousAnnouncements = rendezvous.getAnnouncements();
				announcements.addAll(rendezvous.getAnnouncements());
				if (!rendezvousAnnouncements.isEmpty())
					for (int i = 0; i < rendezvousAnnouncements.size(); i++)
						associatedRendezvouses.add(rendezvous);
			}
			result.addObject("announcements", announcements);
			result.addObject("requestURI", "announcement/user/list-created.do");
			result.addObject("associatedRendezvouses", associatedRendezvouses);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Editing ---------------------------------------------------------

	/**
	 * Gets the form to edit a existing Announcement
	 * 
	 * @param announcementId
	 * @return a ModelAndView object with the form of an Announcement
	 * @author MJ
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int announcementId) {
		ModelAndView result;
		Announcement announcement;
		Rendezvous rendezvous;
		final User user;

		try {
			announcement = this.announcementService.findOne(announcementId);
			Assert.notNull(announcement);
			rendezvous = this.rendezvousService.getRendezvousByAnnouncement(announcementId);

			Assert.isTrue(rendezvous.getMoment().after(new Date()));

			//Check the user rsvp that rendezvous.
			//TODO: Make a global method for all classes for this purpose
			user = (User) this.actorService.findActorByPrincipal();
			this.rendezvousService.getRendezvousByAnnouncement(announcementId).getUsers().contains(user);

			result = this.createEditModelAndView(announcement);
			result.addObject("rendezvousId", rendezvous.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------
	/**
	 * 
	 * Gets the form to create a new Announcement associated to the Rendezvous with id rendezvousId
	 * 
	 * @param rendezvousId
	 * @return a ModelAndView object with a form to create a new Announcement assocated to
	 *         the Rendezvous with id rendezvousId
	 * @author MJ
	 */
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
	/**
	 * Saves the Announcement passed as parameter into the rendezvous with id rendezvousId
	 * 
	 * @param rendezvousId
	 * @param announcement
	 * @param binding
	 * 
	 * @return a ModelAndView object with an error if exists or with a list of RSVP Rendezvouses
	 * @author MJ
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam final int rendezvousId, Announcement announcement, final BindingResult binding) {
		ModelAndView result;

		announcement = this.announcementService.reconstruct(announcement, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(announcement, "announcement.params.error");
			result.addObject("rendezvousId", rendezvousId);
		} else
			try {

				this.announcementService.save(announcement, rendezvousId);
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
