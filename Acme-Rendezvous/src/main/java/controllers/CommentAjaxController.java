
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import services.CommentService;
import domain.Comment;
import domain.User;

@Controller
@RequestMapping("/comment/ajax")
public class CommentAjaxController {

	@Autowired
	CommentService	commentService;


	// Constructors -----------------------------------------------------------

	public CommentAjaxController() {
		super();
	}

	/**
	 * From a comment id returns the name of the user who made that comment.
	 * 
	 * @param commentId
	 *            The id to check
	 * @author Daniel Diment
	 * @return
	 *         The name of the user
	 */
	@RequestMapping(value = "/name", method = RequestMethod.GET)
	public @ResponseBody
	String name(@RequestParam final int commentId) {
		String result;
		try {
			final Comment comment = this.commentService.findOne(commentId);
			final User user = this.commentService.getUserFromComment(comment);
			result = user.getName();
		} catch (final Exception e) {
			result = "";
		}
		return result;
	}
	/**
	 * From a comment id returns the surname of the user who made that comment.
	 * 
	 * @param commentId
	 *            The id to check
	 * @author Daniel Diment
	 * @return
	 *         The surname of the user
	 */
	@RequestMapping(value = "/surname", method = RequestMethod.GET)
	public @ResponseBody
	String surname(@RequestParam final int commentId) {
		String result;
		try {
			final Comment comment = this.commentService.findOne(commentId);
			final User user = this.commentService.getUserFromComment(comment);
			result = user.getSurname();
		} catch (final Exception e) {
			result = "";
		}
		return result;
	}

	/**
	 * From a comment id returns the id of the user who made that comment.
	 * 
	 * @param commentId
	 *            The id to check
	 * @author Daniel Diment
	 * @return
	 *         The id of the user
	 */
	@RequestMapping(value = "/id", method = RequestMethod.GET)
	public @ResponseBody
	String id(@RequestParam final int commentId) {
		String result;
		try {
			final Comment comment = this.commentService.findOne(commentId);
			final User user = this.commentService.getUserFromComment(comment);
			result = String.valueOf(user.getId());
		} catch (final Exception e) {
			result = "";
		}
		return result;
	}
}
