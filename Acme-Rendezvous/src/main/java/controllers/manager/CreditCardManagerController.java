
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CreditCardService;
import controllers.AbstractController;
import domain.CreditCard;

@Controller
@RequestMapping("/creditcard/manager")
public class CreditCardManagerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CreditCardService	creditCardService;


	// Constructor ---------------------------------------------------------------
	public CreditCardManagerController() {
		super();
	}

	//Detailed credit card ----------------------------------------------------------------
	@RequestMapping(value = "/detailed", method = RequestMethod.GET)
	public ModelAndView detailedCreditCard(final int creditCardId) {
		ModelAndView result;
		CreditCard creditCard;

		try {
			creditCard = this.creditCardService.findOne(creditCardId);

			result = new ModelAndView("creditcard/detailed");

			result.addObject("creditCard", creditCard);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
