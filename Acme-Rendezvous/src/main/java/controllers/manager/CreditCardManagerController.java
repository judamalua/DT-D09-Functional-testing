
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CreditCardService;
import services.ManagerService;
import controllers.AbstractController;
import domain.CreditCard;

@Controller
@RequestMapping("/creditcard/manager")
public class CreditCardManagerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private ManagerService		managerService;


	// Constructor ---------------------------------------------------------------
	public CreditCardManagerController() {
		super();
	}

	//Detailed credit card ----------------------------------------------------------------
	/**
	 * Returns a ModelAndView containing the detailed view of a CreditCard. Can only be
	 * used by Managers. The CreditCard must be part of a Request to one of the Services
	 * of the principal.
	 * 
	 * @param creditCardId
	 *            The id of the CreditCard
	 * @author Antonio
	 * @return
	 *         The view of the detailed CreditCard
	 */
	@RequestMapping(value = "/detailed", method = RequestMethod.GET)
	public ModelAndView detailedCreditCard(final int creditCardId) {
		ModelAndView result;
		CreditCard creditCard;

		try {
			creditCard = this.creditCardService.findOne(creditCardId);

			//Checks that the Manager has access to this CreditCard			
			this.managerService.checkManagerCreditCard(creditCard);

			result = new ModelAndView("creditcard/detailed");

			result.addObject("creditCard", creditCard);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	//Ancilliary methods ----------------------------------------------

}
