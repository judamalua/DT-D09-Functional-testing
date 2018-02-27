
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.QuestionRepository;
import domain.Actor;
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
	private Validator			validator;
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
		this.actorService.checkUserLogin();

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
		this.actorService.checkUserLogin();

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
	public Question save(final Question question, final Rendezvous rendezvous) {
		this.actorService.checkUserLogin();

		assert question != null;

		Question result;
		User user;

		Assert.isTrue(!rendezvous.getFinalMode());

		user = (User) this.actorService.findActorByPrincipal();

		// We check that the rendezvous that is going to be saved is contained in the principal's created rendezvouses
		Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));

		result = this.questionRepository.save(question);

		// Updating rendezvous 
		if (rendezvous.getQuestions().contains(question))
			rendezvous.getQuestions().remove(question);
		rendezvous.getQuestions().add(result);
		this.rendezvousService.save(rendezvous);

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
		this.actorService.checkUserLogin();
		Rendezvous rendezvous;
		Actor actor;

		assert question != null;
		assert question.getId() != 0;

		rendezvous = this.rendezvousService.getRendezvousByQuestion(question.getId());

		Assert.isTrue(this.questionRepository.exists(question.getId()));
		actor = this.actorService.findActorByPrincipal();
		if (actor instanceof User)
			this.checkUserCreatedRendezvousOfQuestion(question);

		rendezvous.getQuestions().remove(question);

		for (final Answer answer : this.answerService.getAnswersByQuestionId(question.getId()))
			this.answerService.delete(answer);

		this.questionRepository.delete(question);

	}

	// Other business methods --------------------------------------------------------

	/**
	 * This method reconstructs a pruned Question object received by paramenters
	 * 
	 * @param question
	 * @param binding
	 * @return reconstructed Question
	 * 
	 * @author Manu
	 */
	public Question reconstruct(final Question question, final BindingResult binding) {

		Question result;
		Rendezvous rendezvous;
		User user;
		Collection<Answer> answers;

		if (question.getId() == 0) {
			answers = new HashSet<Answer>();

			result = question;

			result.setAnswers(answers);
		} else {
			user = (User) this.actorService.findActorByPrincipal();
			rendezvous = this.rendezvousService.getRendezvousByQuestion(question.getId());
			Assert.isTrue(!rendezvous.getDeleted());
			Assert.isTrue(!rendezvous.getFinalMode());
			Assert.isTrue(!rendezvous.getAdultOnly() || this.actorService.checkUserIsAdult(user));

			result = this.questionRepository.findOne(question.getId());

			result.setAnswers(new HashSet<Answer>());
			result.setText(question.getText());
		}
		this.validator.validate(result, binding);
		return result;
	}

	/**
	 * This method checks if the Rendezvous that contains the question in parameters is contained in the created Rendezvouses of the principal
	 * 
	 * @param question
	 * @author Juanmi
	 */
	public void checkUserCreatedRendezvousOfQuestion(final Question question) {
		User user;
		Rendezvous rendezvous;

		user = (User) this.actorService.findActorByPrincipal();
		rendezvous = this.rendezvousService.getRendezvousByQuestion(question.getId());

		// Checking if user trying to delete this question is the creator of the Rendezvous
		Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));
	}

	// Dashboard queries.

	/**
	 * Level A query 2
	 * 
	 * @return The average and the standard deviation of the number of answers to the questions per rendezvous.
	 * @author Juanmi
	 */
	public String getAnswersInfoFromQuestion() {
		String result;

		result = this.questionRepository.getAnswersInfoFromQuestion();

		Assert.notNull(result);

		return result;
	}

}
