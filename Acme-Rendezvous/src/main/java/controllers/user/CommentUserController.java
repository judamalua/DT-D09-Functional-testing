
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------------
	public CommentUserController() {
		super();
	}
	//Create-----------------------------------------------------------

	/**
	 * This method returns a ModelAndView object with a new blank comment created by the principal, and
	 * added to a rendezvous, passed as a param.
	 * 
	 * @param rendezvousId
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Comment comment;
		Rendezvous rendezvous;
		User user;

		try {
			comment = this.commentService.create();
			rendezvous = this.rendezvousService.findOneForReplies(rendezvousId);
			Assert.isTrue(!rendezvous.getDeleted());

			user = (User) this.actorService.findActorByPrincipal();
			Assert.isTrue(rendezvous.getUsers().contains(user));

			result = this.createEditModelAndView(comment, rendezvousId);
			result.addObject("rendezvous", rendezvous);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	//Reply------------------------------------------------------------

	/**
	 * This method returns a ModelAndView object with a new blank reply of
	 * a comment passed as a param, and created by the principal.
	 * 
	 * 
	 * @param commentId
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(@RequestParam final int commentId) {
		ModelAndView result;
		Comment reply, replied, fatherComment;
		User user;
		Rendezvous rendezvous;

		try {
			reply = this.commentService.create();
			replied = this.commentService.findOne(commentId);
			user = (User) this.actorService.findActorByPrincipal();

			//This code and the assert below cheks that only an user that RVSPd the rendezvous can reply to its comments.
			rendezvous = this.rendezvousService.getRendezvousByCommentary(replied.getId());
			fatherComment = replied;
			while (rendezvous == null) {
				fatherComment = this.commentService.getFatherCommentFromReply(fatherComment);
				rendezvous = this.rendezvousService.getRendezvousByCommentary(fatherComment.getId());
			}
			Assert.isTrue(rendezvous.getUsers().contains(user));
			Assert.isTrue(!rendezvous.getDeleted());

			result = this.replyModelAndView(reply, replied, rendezvous);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}

	/**
	 * This method returns a ModelAndView object with the list of replies of a comment,
	 * after saving a new reply created by the principal
	 * 
	 * 
	 * @param commentReply
	 *            , binding, commentRepliedId
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST, params = "save")
	public ModelAndView reply(Comment comment, final BindingResult binding, @RequestParam final int repliedId) {
		ModelAndView result;
		Comment replied, fatherComment;
		User user;
		Rendezvous rendezvous;

		try {
			comment = this.commentService.reconstruct(comment, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		replied = this.commentService.findOne(repliedId);
		rendezvous = this.rendezvousService.getRendezvousByCommentary(replied.getId());

		if (binding.hasErrors())
			result = this.replyModelAndView(comment, replied, rendezvous);
		else
			try {

				user = (User) this.actorService.findActorByPrincipal();
				rendezvous = this.rendezvousService.getRendezvousByCommentary(replied.getId());
				fatherComment = replied;
				while (rendezvous == null) {
					fatherComment = this.commentService.getFatherCommentFromReply(fatherComment);
					rendezvous = this.rendezvousService.getRendezvousByCommentary(fatherComment.getId());
				}

				Assert.isTrue(rendezvous.getUsers().contains(user));

				this.commentService.reply(replied, comment);

				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
			} catch (final Throwable oops) {
				result = this.replyModelAndView(comment, replied, rendezvous, "comment.commit.error");
			}
		return result;
	}
	//Edit-----------------------------------------------------------

	/**
	 * This method returns a ModelAndView object with the list of comments of a rendezvous,
	 * after saving a new comment created by the principal
	 * 
	 * 
	 * @param comment
	 *            , binding, rendezvousId
	 * @return ModelAndView
	 * @author Antonio
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(Comment comment, final BindingResult binding, @RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		User user;

		rendezvous = this.rendezvousService.findOneForReplies(rendezvousId);
		comment = this.commentService.reconstruct(comment, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(comment, rendezvous.getId());
		else
			try {
				user = (User) this.actorService.findActorByPrincipal();
				Assert.isTrue(rendezvous.getUsers().contains(user));

				this.commentService.save(comment, rendezvous);

				result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvous.getId() + "&anonymous=false");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comment, "comment.commit.error", rendezvous.getId());
			}
		return result;
	}

	//Ancilliary methods for writting a comment-------------------------------------------
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
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);

		return result;

	}

	//Ancilliary methods for replying a comment-------------------------------------------
	private ModelAndView replyModelAndView(final Comment reply, final Comment replied, final Rendezvous rendezvous) {
		ModelAndView result;

		result = this.replyModelAndView(reply, replied, rendezvous, null);

		return result;
	}
	private ModelAndView replyModelAndView(final Comment reply, final Comment replied, final Rendezvous rendezvous, final String message) {
		ModelAndView result;
		String requestURI;

		requestURI = "comment/user/reply.do?repliedId=" + replied.getId();
		result = new ModelAndView("comment/edit");
		result.addObject("comment", reply);
		result.addObject("requestURI", requestURI);
		result.addObject("replied", replied);
		result.addObject("message", message);
		result.addObject("rendezvous", rendezvous);

		return result;
	}

}
