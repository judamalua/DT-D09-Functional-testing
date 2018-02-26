
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import controllers.AbstractController;
import domain.Comment;

@Controller
@RequestMapping("/comment/admin")
public class CommentAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CommentService	commentService;


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
	public ModelAndView deleteComment(@RequestParam final int commentId, @RequestParam final int rendezvousId) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.findOne(commentId);

		try {
			this.commentService.delete(comment);

			result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
