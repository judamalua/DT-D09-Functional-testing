
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
import domain.User;

@Service
@Transactional
public class CommentService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CommentRepository	commentRepository;


	// Supporting services --------------------------------------------------

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

		Assert.isTrue(this.commentRepository.exists(comment.getId()));

		this.commentRepository.delete(comment);

	}

	//Queries ----------------------------------------------
	public User getUserFromComment(final Comment comment) {
		Assert.isTrue(comment.getId() != 0);

		User result;

		result = this.commentRepository.getUserFromComment(comment);

		return result;
	}
}
