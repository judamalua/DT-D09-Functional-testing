
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
		//		Collection<Answer> answers;

		result = new Question();
		//		answers = new HashSet<>();

		//		result.setAnswers(answers);

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

		Collection<Answer> answers;
		Assert.isTrue(!rendezvous.getFinalMode());

		user = (User) this.actorService.findActorByPrincipal();

		// We check that the rendezvous that is going to be saved is contained in the principal's created rendezvouses
		Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));

		if (question.getId() != 0)
			answers = this.answerService.getAnswersByQuestionId(question.getId());
		else
			answers = new HashSet<Answer>();

		answers = new HashSet<Answer>();

		result = this.questionRepository.save(question);
		if (!answers.isEmpty())
			// Updating questions of the answers the the question is saved.
			for (final Answer a : answers)
				a.setQuestion(result);

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
	 * Level A query 2 part 1/2
	 * 
	 * @return The average of the number of answers to the questions per rendezvous as the first element of the array, total rendezvouses as the second element, and total answers as the third one.
	 * @author Juanmi
	 */
	public String[] getAverageAnswersPerRendezvous() {
		final String[] result = {
			"", "", ""
		};
		Float average;
		Collection<Rendezvous> allRendezvouses;

		Collection<Answer> allAnswers;

		allRendezvouses = this.rendezvousService.findAll();
		allAnswers = this.answerService.findAll();

		average = new Float(allAnswers.size()) / new Float(allRendezvouses.size());

		result[0] = average.toString();
		result[1] = new Integer(allRendezvouses.size()).toString();
		result[2] = new Integer(allAnswers.size()).toString();

		return result;
	}

	//sqrt(sum(q.answers.size * q.answers.size) / count(q.answers.size) - (avg(q.answers.size) * avg(q.answers.size)))
	/**
	 * Level A query 2 part 2/2
	 * 
	 * @return The standard deviation of the number of answers to the questions per rendezvous.
	 */
	public String getStandardDeviationAnswersPerRendezvous() {
		String[] averageTotalRendezvousesTotalAnswers = {
			"", "", ""
		};
		Float average, totalRendezvouses, totalAnswers, standardDeviation;
		String result;

		averageTotalRendezvousesTotalAnswers = this.getAverageAnswersPerRendezvous();

		average = new Float(averageTotalRendezvousesTotalAnswers[0]);
		totalRendezvouses = new Float(averageTotalRendezvousesTotalAnswers[1]);
		totalAnswers = new Float(averageTotalRendezvousesTotalAnswers[2]);

		standardDeviation = (float) ((Math.sqrt(totalAnswers * totalAnswers) / totalRendezvouses) - (average * average));

		result = standardDeviation.toString();

		return result;
	}

	public Question reconstruct(final Question question, final BindingResult binding) {

		Question result;
		Rendezvous rendezvous;
		User user;

		if (question.getId() == 0) {
			question.setAnswers(new HashSet<Answer>());
			result = question;
		} else {
			user = (User) this.actorService.findActorByPrincipal();
			rendezvous = this.rendezvousService.getRendezvousByQuestion(question.getId());
			Assert.isTrue(!rendezvous.getDeleted());
			Assert.isTrue(!rendezvous.getFinalMode());
			Assert.isTrue(!rendezvous.getAdultOnly() || this.actorService.checkUserIsAdult(user));

			result = this.questionRepository.findOne(question.getId());

			result.setAnswers(new HashSet<Answer>());
			result.setText(question.getText());

			this.validator.validate(result, binding);
		}

		return result;
	}
}
