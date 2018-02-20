
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

	/**
	 * This method allows an Administrator to delete a comment
	 * 
	 * @param commentId
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteComment(@RequestParam final int commentId) {
		ModelAndView result;
		Comment comment, father;
		Rendezvous rendezvous;

		comment = this.commentService.findOne(commentId);
		rendezvous = this.rendezvousService.getRendezvousByCommentary(commentId);
		father = this.commentService.getFatherCommentFromReply(comment);

		try {
			this.commentService.delete(comment);

			if (rendezvous != null)
				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
			else {
				rendezvous = this.rendezvousService.getRendezvousByCommentary(father.getId());
				while (rendezvous == null) {
					father = this.commentService.getFatherCommentFromReply(father);
					rendezvous = this.rendezvousService.getRendezvousByCommentary(father.getId());
				}

				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
			}

		} catch (final Throwable oops) {
			if (rendezvous != null)
				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
			else if (comment != null) {
				rendezvous = this.rendezvousService.getRendezvousByCommentary(father.getId());
				while (rendezvous == null) {
					father = this.commentService.getFatherCommentFromReply(father);
					rendezvous = this.rendezvousService.getRendezvousByCommentary(father.getId());
				}

				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");

			} else
				result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
