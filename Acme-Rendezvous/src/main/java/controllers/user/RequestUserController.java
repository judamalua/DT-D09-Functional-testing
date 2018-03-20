
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CreditCardService;
import services.RendezvousService;
import services.RequestService;
import services.UserService;
import controllers.AbstractController;
import domain.CreditCard;
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

	@Autowired
	private CreditCardService	creditCardService;


	// Constructors -----------------------------------------------------------
	public RequestUserController() {
		super();
	}

	//List-------------------------------------------------------------------
	/**
	 * This method can receive the ID of a Rendezvous. If it does, returns a ModelAndView listing all the Requests
	 * of that Rendezvous. If it doesn't, returns a ModelAndView listing all the Requests made by the Principal.
	 * 
	 * @return ModelAndView
	 * @param rendezvousId
	 * @author Antonio
	 */
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
	public ModelAndView create(@RequestParam final int serviceId) {
		ModelAndView result;
		Request request;
		CreditCard creditCard;

		try {
			request = this.requestService.createByService(serviceId);
			creditCard = this.creditCardService.create();
			request.setCreditCard(creditCard);
			result = this.createEditModelAndView(request);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	/**
	 * This method receives the ID of a rendezvous, and tries to create a new empty Request for
	 * the user principal, who must be the owner of that rendezvous.
	 * 
	 * @return ModelAndView of the detailed rendezvous if everything worked.
	 * @param rendezvousId
	 * @author Antonio
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveRequest(Request request, final BindingResult binding, @ModelAttribute("rendezvous") final int rendezvousId) {
		ModelAndView result;

		try {
			request = this.requestService.reconstruct(request, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		if (binding.hasErrors())
			result = this.createEditModelAndView(request);
		else
			try {
				this.checkRendezvousBelongsToPrincipal(rendezvousId);
				this.creditCardService.checkCreditCardBelongsToPrincipal(request.getCreditCard());

				this.requestService.saveNewRequest(request, rendezvousId);

				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("CreditCard expiration Date error"))
					result = this.createEditModelAndView(request, "request.creditcard.expiration.error");
				else
					result = new ModelAndView("redirect:/misc/403");
			}
		return result;
	}

	//AJAX Credit Card---------------------------------------
	/**
	 * This method receives a cookie token and sends a string with the last four numbers of a credit card and the credit card number, if something fails returns a null string.
	 * 
	 * @param cookieToken
	 *            The token to test
	 * @author Daniel Diment
	 * @return
	 *         The string
	 */
	@RequestMapping(value = "/ajaxCard", method = RequestMethod.GET)
	public @ResponseBody
	String ajaxCard(@RequestParam final String cookieToken) {
		String result = "null";
		CreditCard creditCard;
		try {
			creditCard = this.creditCardService.findByCookieToken(cookieToken);
			result = creditCard.getNumber().substring(creditCard.getNumber().length() - 4) + creditCard.getId();
		} catch (final Throwable e) {
		}
		return result;
	}

	//Ancilliary methods-------------------------------------------------------------------
	private ModelAndView createEditModelAndView(final Request request) {
		ModelAndView result;

		result = this.createEditModelAndView(request, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Request request, final String message) {
		ModelAndView result;
		Collection<Rendezvous> myRendezvouses;
		User user;

		user = (User) this.actorService.findActorByPrincipal();
		myRendezvouses = this.rendezvousService.getRendezvousesAvailableForRequest(user.getId(), request.getService().getId());
		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("message", message);
		result.addObject("rendezvouses", myRendezvouses);

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
