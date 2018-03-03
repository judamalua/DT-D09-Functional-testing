
package controllers.user;

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

import services.ActorService;
import services.RendezvousService;
import services.RequestService;
import services.UserService;
import controllers.AbstractController;
import domain.Rendezvous;
import domain.Request;
import domain.User;

@Controller
@RequestMapping("/request/user")
public class RequestUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------
	public RequestUserController() {
		super();
	}

	//List-------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listRequests(@RequestParam(required = false) final Integer rendezvousId) {
		ModelAndView result;
		Collection<Request> requests;
		Rendezvous rendezvous;
		String requestURI;

		try {
			if (rendezvousId == null) {
				requests = this.requestService.getAllRequestFromUserPrincipal();
				requestURI = "request/user/list.do";
			} else {
				rendezvous = this.rendezvousService.findOne(rendezvousId);
				requests = rendezvous.getRequests();
				requestURI = "request/user/list.do?rendezvousId=" + rendezvousId;
			}

			result = new ModelAndView("request/list");
			result.addObject("requests", requests);
			result.addObject("requestURI", requestURI);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}

	//Create-------------------------------------------------------------------
	/**
	 * This method receives the ID of a rendezvous, and tries to create a new empty Request for
	 * the user principal, who must be the owner of that rendezvous.
	 * 
	 * @return ModelAndView
	 * @param rendezvousId
	 * @author Antonio
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Request request;

		try {
			request = this.requestService.create();

			//Checks that the User connected is the owner of the rendezvous
			this.checkRendezvousBelongsToPrincipal(rendezvousId);

			result = this.createEditModelAndView(request, rendezvousId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveRequest(@Valid final Request request, final BindingResult binding, @RequestParam final int rendezvousId) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(request, rendezvousId);
		else
			try {
				this.checkRendezvousBelongsToPrincipal(rendezvousId);
				this.requestService.saveNewRequest(request, rendezvousId);

				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/misc/403");
			}
		return result;
	}
	//Ancilliary methods-------------------------------------------------------------------
	private ModelAndView createEditModelAndView(final Request request, final int rendezvousId) {
		ModelAndView result;

		result = this.createEditModelAndView(request, rendezvousId, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Request request, final int rendezvousId, final String message) {
		ModelAndView result;

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("message", message);
		result.addObject("rendezvousId", rendezvousId);

		return result;
	}

	/**
	 * This method checks that the user connected to the system (the principal) is the owner of
	 * the rendezvous from he wants to make the request. This method is only called by
	 * the create and edit request controllers, so it is already in a try/catch block.
	 * 
	 * @param rendezvousId
	 * @author Antonio
	 */
	private void checkRendezvousBelongsToPrincipal(final int rendezvousId) {
		User userPrincipal, rendezvousOwner;

		userPrincipal = (User) this.actorService.findActorByPrincipal();
		rendezvousOwner = this.userService.getCreatorUser(rendezvousId);

		Assert.isTrue(userPrincipal.equals(rendezvousOwner));
	}
}
