
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import domain.Answer;

@Service
@Transactional
public class AnswerService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AnswerRepository	answerRepository;


	// Supporting services --------------------------------------------------W

	// Simple CRUD methods --------------------------------------------------

	/**
	 * Creates a new answer
	 * 
	 * @author Daniel Diment
	 * @return the new answer
	 */
	public Answer create() {
		Answer result;

		result = new Answer();

		return result;
	}

	/**
	 * Gets all the answers of the database
	 * 
	 * @author Daniel Diment
	 * @return The collection containing the all the answers
	 */
	public Collection<Answer> findAll() {

		Collection<Answer> result;

		Assert.notNull(this.answerRepository);
		result = this.answerRepository.findAll();
		Assert.notNull(result);

		return result;

	}
	/**
	 * Gets the answer of the database that has that id
	 * 
	 * @param answerId
	 *            The id you want to search
	 * @author Daniel Diment
	 * @return The answer with that id
	 */
	public Answer findOne(final int answerId) {

		Answer result;

		result = this.answerRepository.findOne(answerId);

		return result;

	}

	/**
	 * Saves an answer to the database
	 * 
	 * @param answer
	 *            The answer you want to save
	 * @author Daniel Diment
	 * @return The saved answer
	 */
	public Answer save(final Answer answer) {

		assert answer != null;

		Answer result;

		result = this.answerRepository.save(answer);

		return result;

	}

	/**
	 * Deletes an answer to the database
	 * 
	 * @author Daniel Diment
	 * @param answer
	 *            the answer to delete
	 */
	public void delete(final Answer answer) {

		assert answer != null;
		assert answer.getId() != 0;

		Assert.isTrue(this.answerRepository.exists(answer.getId()));

		this.answerRepository.delete(answer);

	}

	/**
	 * Using the question id gets the collection of answers that have that question
	 * 
	 * @param questionId
	 *            The id to check
	 * @author Daniel Diment
	 * @return The collection of answers
	 */
	public Collection<Answer> getAnswersByQuestionId(final int questionId) {
		Assert.isTrue(questionId != 0);
		Collection<Answer> result;

		result = this.answerRepository.getAnswersByQuestionId(questionId);

		return result;
	}

	/**
	 * Using the user id gets the collection of answers that that user have answer
	 * 
	 * @param questionId
	 *            The id to check
	 * @author Daniel Diment
	 * @return The collection of answers
	 */
	public Collection<Answer> getAnswersByUserId(final int userId) {
		Assert.isTrue(userId != 0);
		final Collection<Answer> result;

		result = this.answerRepository.getAnswersByUserId(userId);

		return result;
	}

	/**
	 * Using the id of the user and the id of the questions, returns the answer that that user gave when answering that question
	 * 
	 * @param userId
	 *            The id of the user
	 * @param questionId
	 *            The id of the question
	 * @author Daniel Diment
	 * @return The answer
	 */
	public Answer getAnswerByUserIdAndQuestionId(final int userId, final int questionId) {
		Assert.isTrue(userId != 0 && questionId != 0);
		Answer result;

		result = this.answerRepository.getAnswerByUserIdAndQuestionId(userId, questionId);

		return result;
	}

}
