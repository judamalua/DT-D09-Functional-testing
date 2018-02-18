
package controllers.admin;

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
@RequestMapping("/comment/admin")
public class CommentAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors --------------------------------------------------------
	public CommentAdministratorController() {
		super();
	}

	//Delete -----------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteComment(@RequestParam final int commentId) {
		ModelAndView result;
		Comment comment, father;
		Rendezvous rendezvous;

		comment = this.commentService.findOne(commentId);
		rendezvous = this.rendezvousService.getRendezvousByCommentary(commentId);

		try {
			this.commentService.delete(comment);

			if (rendezvous != null)
				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
			else {
				father = this.commentService.getFatherCommentFromReply(comment);
				result = new ModelAndView("redirect:/comment/listFromComment.do?commentId=" + father.getId());
			}
		} catch (final Throwable oops) {
			if (rendezvous != null)
				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
			else if (comment != null) {
				father = this.commentService.getFatherCommentFromReply(comment);
				result = new ModelAndView("redirect:/comment/listFromComment.do?commentId=" + father.getId());
			} else
				result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
