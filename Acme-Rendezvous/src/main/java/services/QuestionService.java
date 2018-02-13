
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class QuestionService {

	// Managed repository --------------------------------------------------

	@Autowired
	private QuestionRepository	questionRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService		actorService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private AnswerService		answerService;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * Creates a Question
	 * 
	 * @return the created Question
	 * @author Juanmi
	 */
	public Question create() {
		this.actorService.checkUserLogin();

		Question result;

		result = new Question();

		return result;
	}

	/**
	 * Finds every question in the system
	 * 
	 * @return the collection of every Question
	 * @author Juanmi
	 */
	public Collection<Question> findAll() {

		Collection<Question> result;

		Assert.notNull(this.questionRepository);
		result = this.questionRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * Finds one question in the system
	 * 
	 * @param questionId
	 * @return the question with the id given
	 */
	public Question findOne(final int questionId) {

		Question result;

		result = this.questionRepository.findOne(questionId);

		return result;

	}

	/**
	 * Saves the question in parameters
	 * 
	 * @param question
	 *            to be saved
	 * @return the question saved
	 */
	public Question save(final Question question) {
		this.actorService.checkUserLogin();

		assert question != null;

		Question result;
		User user;
		Rendezvous rendezvous;
		Collection<Answer> answers;

		user = (User) this.actorService.findActorByPrincipal();
		if (question.getId() != 0)
			answers = this.answerService.getAnswersByQuestionId(question.getId());
		else
			answers = new HashSet<Answer>();

		result = this.questionRepository.save(question);
		if (!answers.isEmpty())
			// Updating questions of the answers the the question is saved.
			for (final Answer a : answers)
				a.setQuestion(result);

		if (question.getId() != 0)
			rendezvous = this.rendezvousService.getRendezvousByQuestion(question.getId());
		else
			rendezvous = this.rendezvousService.getRendezvousByQuestion(result.getId());

		// We check that the rendezvous that has been saved is contained in the principal's created rendezvouses
		Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));

		return result;

	}
	/**
	 * Deletes the question in parameters
	 * 
	 * @param question
	 *            to be deleted
	 * @author Juanmi
	 */
	public void delete(final Question question) {

		assert question != null;
		assert question.getId() != 0;

		Assert.isTrue(this.questionRepository.exists(question.getId()));

		this.questionRepository.delete(question);

	}

	// Other business methods --------------------------------------------------------
}
