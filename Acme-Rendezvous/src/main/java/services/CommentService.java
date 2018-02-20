
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class CommentService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CommentRepository	commentRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private UserService			userService;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * This method returns a new comment, with an empty list of replies and the actual moment
	 * 
	 * 
	 * @return Comment
	 * @author Antonio
	 */
	public Comment create() {
		Comment result;
		Date now;
		Collection<Comment> replies;

		now = new Date(System.currentTimeMillis() - 10);
		replies = new ArrayList<Comment>();

		result = new Comment();
		result.setMoment(now);
		result.setComments(replies);

		return result;
	}

	/**
	 * This method returns a collection of all the comments in the system.
	 * 
	 * 
	 * @return Collection<Comment>
	 * @author Antonio
	 */
	public Collection<Comment> findAll() {

		Collection<Comment> result;

		Assert.notNull(this.commentRepository);
		result = this.commentRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * This method returns the comment which ID is passed by means of a param
	 * 
	 * @param commentId
	 * @return Comment
	 * @author Antonio
	 */
	public Comment findOne(final int commentId) {
		Assert.isTrue(commentId != 0);

		Comment result;

		result = this.commentRepository.findOne(commentId);

		return result;

	}

	/**
	 * This method saves a comment passed as a param into the database.
	 * 
	 * @param comment
	 * @return Comment
	 * @author Antonio
	 */
	public Comment save(final Comment comment) {

		Assert.notNull(comment);

		Comment result;

		result = this.commentRepository.save(comment);

		return result;

	}

	/**
	 * This method deletes a comment passed as a param from the database.
	 * It also refresh the user's and rendezvou's comments list.
	 * 
	 * @param comment
	 * @author Antonio
	 */
	public void delete(final Comment comment) {
		Assert.notNull(comment);
		Assert.isTrue(comment.getId() != 0);

		Rendezvous rendezvous;
		User user;
		Comment fatherComment;

		rendezvous = this.rendezvousService.getRendezvousByCommentary(comment.getId());
		user = this.getUserFromComment(comment);
		fatherComment = this.getFatherCommentFromReply(comment);

		user.getComments().remove(comment);
		this.actorService.save(user);

		if (rendezvous != null) {
			rendezvous.getComments().remove(comment);
			this.rendezvousService.save(rendezvous);
		}

		if (fatherComment != null) {
			fatherComment.getComments().remove(comment);
			this.save(fatherComment);
		}

		this.commentRepository.delete(comment);

	}

	/**
	 * This method deletes a comment passed as a param. It's only useful when deleting a rendezvous.
	 * 
	 * @param comment
	 * @author Antonio
	 */
	public void deleteCommentFromRendezvous(final Comment comment) {
		Assert.notNull(comment);
		Assert.isTrue(comment.getId() != 0);

		User user;
		final Rendezvous rendezvous;

		user = this.getUserFromComment(comment);
		user.getComments().remove(comment);
		this.userService.save(user);

		rendezvous = this.rendezvousService.getRendezvousByCommentary(comment.getId());
		rendezvous.getComments().remove(comment);
		this.rendezvousService.save(rendezvous);

		this.commentRepository.delete(comment);

	}

	public void deleteCommentFromRendezvous2(final Rendezvous rendezvous) {
		Assert.notNull(rendezvous);
		Collection<Comment> comments;

		comments = rendezvous.getComments();

		for (final Comment c : comments)
			this.delete(c);
	}
	//Queries ----------------------------------------------
	/**
	 * This method returns the user that wrote a comment, passed as a param.
	 * 
	 * @param comment
	 * @return User
	 * @author Antonio
	 */
	public User getUserFromComment(final Comment comment) {
		User result;

		result = this.commentRepository.getUserFromComment(comment);

		return result;
	}

	/**
	 * This method returns the comment that has been replied by a comment, passed as a param.
	 * 
	 * @param comment
	 * @return Comment
	 * @author Antonio
	 */
	public Comment getFatherCommentFromReply(final Comment reply) {
		Comment result;

		result = this.commentRepository.getFatherCommentFromReply(reply);

		return result;
	}

	// Dasboard queries.

	/**
	 * Level A query 3
	 * 
	 * @return The average and the standard deviation of replies per comment.
	 * @author Juanmi
	 */
	public String getRepliesInfoFromComment() {
		String result;

		result = this.commentRepository.getRepliesInfoFromComment();

		Assert.notNull(result);

		return result;
	}

	public Comment reconstruct(final Comment comment, final BindingResult binding) {
		Comment result;
		final Collection<Comment> comments;

		if (comment.getId() == 0) {
			comments = new HashSet<Comment>();
			comment.setComments(comments);
			result = comment;
		} else {
			result = this.commentRepository.findOne(comment.getId());

			result.setText(comment.getText());
			result.setPictureUrl(comment.getPictureUrl());
			result.setMoment(comment.getMoment());
		}
		this.validator.validate(result, binding);
		return result;
	}
}
