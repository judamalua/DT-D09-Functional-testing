
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
	private UserService			userService;

	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods --------------------------------------------------

	public Comment create() {
		Comment result;
		Date now;
		Collection<Comment> replies;

		now = new Date(System.currentTimeMillis() - 1);
		replies = new ArrayList<Comment>();

		result = new Comment();
		result.setMoment(now);
		result.setComments(replies);

		return result;
	}

	public Collection<Comment> findAll() {

		Collection<Comment> result;

		Assert.notNull(this.commentRepository);
		result = this.commentRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Comment findOne(final int commentId) {
		Assert.isTrue(commentId != 0);

		Comment result;

		result = this.commentRepository.findOne(commentId);

		return result;

	}

	public Comment save(final Comment comment) {

		Assert.notNull(comment);

		Comment result;

		result = this.commentRepository.save(comment);

		return result;

	}

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

		/*
		 * //Comentario solo en el rendezvous
		 * if (rendezvous != null && comment.getComments().size() == 0) {
		 * rendezvous.getComments().remove(comment);
		 * this.rendezvousService.save(rendezvous);
		 * 
		 * user.getComments().remove(comment);
		 * this.userService.save(user);
		 * 
		 * this.commentRepository.delete(comment);
		 * 
		 * //Comentario solo en el reply
		 * } else if (rendezvous == null && comment.getComments().size() == 0) {
		 * user.getComments().remove(comment);
		 * this.userService.save(user);
		 * 
		 * fatherComment = this.getFatherCommentFromReply(comment);
		 * fatherComment.getComments().remove(comment);
		 * this.save(fatherComment);
		 * 
		 * this.commentRepository.delete(comment);
		 * 
		 * //Comentario con replies en el rendezvous
		 * } else if (rendezvous != null && comment.getComments().size() > 0) {
		 * for (final Comment c : comment.getComments())
		 * this.delete(c);
		 * 
		 * //rendezvous.getComments().remove(comment);
		 * //this.rendezvousService.save(rendezvous);
		 * 
		 * user.getComments().remove(comment);
		 * this.userService.save(user);
		 * 
		 * this.commentRepository.delete(comment);
		 * 
		 * }
		 */

		//Comentario con replies como reply

		/*
		 * 
		 * if (comment.getComments().isEmpty()) {//It doesn't have replies
		 * fatherComment = this.getFatherCommentFromReply(comment);
		 * 
		 * if (fatherComment != null) {
		 * fatherComment.getComments().remove(comment);
		 * this.save(fatherComment);
		 * }
		 * 
		 * rendezvous = this.rendezvousService.getRendezvousByCommentary(comment.getId());
		 * if (rendezvous != null) {
		 * rendezvous.getComments().remove(comment);
		 * this.rendezvousService.save(rendezvous);
		 * }
		 * 
		 * user = this.getUserFromComment(comment);
		 * user.getComments().remove(comment);
		 * this.actorService.save(user);
		 * 
		 * this.commentRepository.delete(comment);
		 * 
		 * } else { // It has replies
		 * for (final Comment c : comment.getComments())
		 * this.delete(c);
		 * 
		 * this.commentRepository.delete(comment);
		 * }
		 */

	}
	//Queries ----------------------------------------------
	public User getUserFromComment(final Comment comment) {
		Assert.isTrue(comment.getId() != 0);

		User result;

		result = this.commentRepository.getUserFromComment(comment);

		return result;
	}

	public Comment getFatherCommentFromReply(final Comment reply) {
		Assert.isTrue(reply.getId() != 0);

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
	public String getQuestionsInfoFromRendezvous() {
		String result;

		result = this.commentRepository.getQuestionsInfoFromRendezvous();

		Assert.notNull(result);

		return result;
	}
}
