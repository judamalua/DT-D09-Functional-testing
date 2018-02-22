
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import services.RendezvousService;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------------
	public CommentController() {
		super();
	}

	//List -----------------------------------------------------------
	/**
	 * This method returns a ModelAndView object with all the comments of a rendezvous,
	 * passed as a param.
	 * 
	 * @param rendezvousId
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/listFromRendezvous", method = RequestMethod.GET)
	public ModelAndView listFromRendezvous(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Comment> comments;
		Collection<User> users;
		Rendezvous rendezvous;
		String requestURI;

		try {
			users = new ArrayList<>();
			rendezvous = this.rendezvousService.findOneForReplies(rendezvousId);
			comments = rendezvous.getComments();

			for (final Comment comment : comments)
				users.add(this.commentService.getUserFromComment(comment));

			requestURI = "comment/list.do";

			result = new ModelAndView("comment/list");
			result.addObject("comments", comments);
			result.addObject("requestURI", requestURI);
			result.addObject("users", users);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	/**
	 * This method returns a ModelAndView object with all the replies of a comment,
	 * passed as a param.
	 * 
	 * @param commentId
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/listFromComment", method = RequestMethod.GET)
	public ModelAndView listFromComment(@RequestParam final int commentId) {
		ModelAndView result;
		Collection<Comment> replies;
		Collection<User> users;
		Comment comment;
		String requestURI;
		Boolean userHasRVSPdRendezvous = false;
		Rendezvous rendezvous;

		comment = this.commentService.findOne(commentId);
		users = new ArrayList<>();
		replies = comment.getComments();
		requestURI = "comment/list.do";

		for (final Comment commentary : replies)
			users.add(this.commentService.getUserFromComment(commentary));

		result = new ModelAndView("comment/list");
		result.addObject("comments", replies);
		result.addObject("requestURI", requestURI);

		try {
			User user;
			Comment fatherComment;

			user = (User) this.actorService.findActorByPrincipal();

			rendezvous = this.rendezvousService.getRendezvousByCommentary(commentId);

			fatherComment = comment;
			while (rendezvous == null) {
				fatherComment = this.commentService.getFatherCommentFromReply(comment);
				rendezvous = this.rendezvousService.getRendezvousByCommentary(fatherComment.getId());
			}

			userHasRVSPdRendezvous = rendezvous.getUsers().contains(user);

			result.addObject("userHasRVSPdRendezvous", userHasRVSPdRendezvous);
			result.addObject("users", users);
		} catch (final Throwable oops) {

		}

		return result;
	}
	//Display -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commentId) {
		ModelAndView result;
		Collection<Comment> replies;
		Comment comment;
		User user;

		try {
			comment = this.commentService.findOne(commentId);
			replies = comment.getComments();

			user = this.commentService.getUserFromComment(comment);
			result = new ModelAndView("comment/display");
			result.addObject("comment", comment);
			result.addObject("user", user);
			result.addObject("comments", replies);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
