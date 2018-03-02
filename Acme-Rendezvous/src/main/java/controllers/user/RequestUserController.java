
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import services.RendezvousService;
import services.RequestService;
import controllers.AbstractController;

@Controller
@RequestMapping("/comment/user")
public class RequestUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors -----------------------------------------------------------
	public RequestUserController() {
		super();
	}

	//Create-----------------------------------------------------------

}
