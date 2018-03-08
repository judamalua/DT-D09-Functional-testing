
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import controllers.AbstractController;
import domain.DomainService;
import domain.Request;

@Controller
@RequestMapping("/request/manager")
public class RequestManagerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ServiceService	serviceService;


	// Constructor ---------------------------------------------------------------
	public RequestManagerController() {
		super();
	}

	//List-------------------------------------------------------------------
	/**
	 * This method returns a ModelAndView containing the list of Request of a Service, passed as a param
	 * 
	 * @param serviceId
	 *            The id of the service
	 * @author Antonio
	 * @return
	 *         The view of Requests of the Service
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listByService(@RequestParam final int serviceId) {
		ModelAndView result;
		Collection<Request> requests;
		DomainService service;

		try {
			service = this.serviceService.findOne(serviceId);
			requests = service.getRequests();

			result = new ModelAndView("request/list");
			result.addObject("requests", requests);
			result.addObject("requestURI", "request/manager/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}

}
