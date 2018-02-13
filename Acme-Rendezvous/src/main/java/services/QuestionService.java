
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


	// Simple CRUD methods --------------------------------------------------

	public Question create() {
		this.actorService.checkUserLogin();

		Question result;
		Collection<Answer> answers;

		answers = new HashSet<Answer>();

		result = new Question();
		result.setAnswers(answers);

		return result;
	}

	public Collection<Question> findAll() {

		Collection<Question> result;

		Assert.notNull(this.questionRepository);
		result = this.questionRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Question findOne(final int questionId) {

		Question result;

		result = this.questionRepository.findOne(questionId);

		return result;

	}

	public Question save(final Question question) {
		this.actorService.checkUserLogin();

		assert question != null;

		Question result;
		User user;
		Rendezvous rendezvous;

		user = (User) this.actorService.findActorByPrincipal();

		// We get the rendezvous that has a question whose ID is question.getId if the ID is != 0
		if (question.getId() != 0)
			rendezvous = this.rendezvousService.getRendezvousByQuestion(question.getId());

		result = this.questionRepository.save(question);

		//If the ID is == 0, we get the rendezvous by means of the result.
		if (question.getId() == 0)
			rendezvous = this.rendezvousService.getRendezvousByQuestion(result.getId());

		// We check that the rendezvous that has been saved is contained in the principal's created rendezvouses
		Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));

		return result;

	}

	public void delete(final Question question) {

		assert question != null;
		assert question.getId() != 0;

		Assert.isTrue(this.questionRepository.exists(question.getId()));

		this.questionRepository.delete(question);

	}

	// Other business methods --------------------------------------------------------

	/**
	 * This method finds the question which contains the answer whose ID is the given in the parameters
	 * 
	 * @param answerId
	 * @return Question which contains the answer whose ID is given by parameters
	 * @author Juanmi
	 */
	public Question getQuestionByAnswerId(final int answerId) {
		Question result;

		Assert.isTrue(answerId != 0);

		result = this.questionRepository.getQuestionByAnswerId(answerId);

		Assert.notNull(result);

		return result;
	}
}
