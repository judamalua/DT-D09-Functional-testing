
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/comment/administrator")
public class CommentAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors -----------------------------------------------------------
	public CommentAdministratorController() {
		super();
	}

	//Delete -----------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteComment(@RequestParam final int commentId) {
		ModelAndView result;
		Comment comment;
		Rendezvous rendezvous;

		comment = this.commentService.findOne(commentId);
		rendezvous = this.rendezvousService.getRendezvousByCommentary(commentId);

		try {
			this.commentService.delete(comment);
			result = new ModelAndView("redirect:comment/list.do?rendezvousId=" + rendezvous.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:comment/list.do?rendezvousId=" + rendezvous.getId());
		}
		return result;
	}
}
