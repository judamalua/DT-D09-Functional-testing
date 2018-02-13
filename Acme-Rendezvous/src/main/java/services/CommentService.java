package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Comment;

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

		result = new Comment();

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

		Comment result;

		result = this.commentRepository.findOne(commentId);

		return result;

	}

	public Comment save(final Comment comment) {

		assert comment != null;

		Comment result;

		result = this.commentRepository.save(comment);

		return result;

	}

	public void delete(final Comment comment) {

		assert comment != null;
		assert comment.getId() != 0;

		Assert.isTrue(this.commentRepository.exists(comment.getId()));

		this.commentRepository.delete(comment);

	}
}

