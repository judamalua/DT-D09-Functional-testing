
package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Comment;
import domain.Rendezvous;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors -----------------------------------------------------------
	public CommentUserController() {
		super();
	}
	//Create-----------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Comment comment;
		Rendezvous rendezvous;

		comment = this.commentService.create();
		rendezvous = this.rendezvousService.findOne(rendezvousId);

		result = this.createEditModelAndView(comment, rendezvousId);
		result.addObject("rendezvous", rendezvous);

		return result;
	}

	//Edit-----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final Comment comment, final BindingResult binding, @RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		Comment saved;

		rendezvous = this.rendezvousService.findOne(rendezvousId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(comment, rendezvous.getId());
		else
			try {

				saved = this.commentService.save(comment);
				rendezvous.getComments().add(saved);
				this.rendezvousService.save(rendezvous);
				result = new ModelAndView("redirect:comment/list.do?rendezvousId=" + rendezvous.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comment, "comment.commit.error", rendezvous.getId());
			}
		return result;
	}

	//Ancilliary methods -------------------------------------------
	private ModelAndView createEditModelAndView(final Comment comment, final int rendezvousId) {
		ModelAndView result;

		result = this.createEditModelAndView(comment, null, rendezvousId);

		return result;
	}
	private ModelAndView createEditModelAndView(final Comment comment, final String message, final int rendezvousId) {
		ModelAndView result;
		String requestURI;

		requestURI = "comment/user/edit.do?rendezvousId=" + rendezvousId;
		result = new ModelAndView("comment/edit");
		result.addObject("comment", comment);
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);

		return result;

	}

}
