
package services;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class QuestionServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private QuestionService		questionService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Tests ------------------------------------------------------------------

	/**
	 * This test checks that authenticated users can edit questions of the rendezvouses they created saved as draft mode, related to functional
	 * requirement number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions that are associated
	 * with a rendezvous on draft mode that he or she's created previously
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testEditQuestionToCreatedDraftModeRendezvous() {
		// Functional requirement number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions
		// that are associated with a rendezvous on draft mode that he or she's created previously
		int rendezvousId, questionId;
		Question question;
		Rendezvous rendezvous;

		rendezvousId = super.getEntityId("Rendezvous8");
		questionId = super.getEntityId("Question6");
		rendezvous = this.rendezvousService.findOne(rendezvousId);

		super.authenticate("User1");
		question = this.questionService.findOne(questionId);

		question.setText("Test");

		this.questionService.save(question, rendezvous);
		this.questionService.flush();

		super.unauthenticate();
	}

	/**
	 * This test checks that authenticated users can delete questions of the rendezvouses they created saved as draft mode related to functional requirement
	 * number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions that are associated with a
	 * rendezvous on draft mode that he or she's created previously
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testDeleteQuestionToCreatedDraftModeRendezvous() {
		// Functional requirement number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions
		// that are associated with a rendezvous on draft mode that he or she's created previously
		int questionId;
		Question question;

		questionId = super.getEntityId("Question6");

		super.authenticate("User1");
		question = this.questionService.findOne(questionId);

		this.questionService.delete(question);
		this.questionService.flush();

		super.unauthenticate();
	}

	/**
	 * This driver checks several tests regarding functional requirement number 21.1: An actor who is authenticated as a user must be able to manage
	 * (add, edit, delete) the questions that are associated with a rendezvous on draft mode that he or she has created previously, tests are explained inside
	 * 
	 * @author Juanmi
	 */
	@Test
	public void driverCreateQuestion() {
		// Functional requirement number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions
		// that are associated with a rendezvous on draft mode that he or she has created previously
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can create questions to a rendezvous they created saved as draft mode
				"User1", "Rendezvous8", "Test", null
			}, {
				// This test checks that authenticated users cannot create questions to the rendezvouses they created saved as final mode
				"User1", "Rendezvous1", "Test", IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated users cannot manage questions of a rendezvous in draft mode
				null, "Rendezvous8", "Test", IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated users cannot create questions to a rendezvous in final mode
				null, "Rendezvous1", "Test", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create questions to a rendezvous in draft mode
				"Admin1", "Rendezvous8", "Test", ClassCastException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create questions to a rendezvous in final mode
				"Admin1", "Rendezvous1", "Test", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create questions to a draft mode rendezvous they did not create
				"User2", "Rendezvous8", "Test", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create questions to a final mode rendezvous they did not create
				"User2", "Rendezvous1", "Test", IllegalArgumentException.class
			}, {
				// This test checks that questions with empty texts cannot be saved
				"User1", "Rendezvous8", "", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * This test checks that when a question is created, findAll method returns one more question than before the creation
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testFindAllCreatingQuestion() {
		Question question;
		int rendezvousId;
		Rendezvous rendezvous;
		Collection<Answer> answers;
		Collection<Question> allQuestionsBeforeCreation, allQuestionsAfterCreation;

		answers = new HashSet<Answer>();

		rendezvousId = super.getEntityId("Rendezvous8");
		rendezvous = this.rendezvousService.findOne(rendezvousId);

		super.authenticate("User1");

		allQuestionsBeforeCreation = this.questionService.findAll();

		question = this.questionService.create();
		question.setAnswers(answers);
		question.setText("Test");

		this.questionService.save(question, rendezvous);
		this.questionService.flush();

		allQuestionsAfterCreation = this.questionService.findAll();

		Assert.isTrue(allQuestionsBeforeCreation.size() + 1 == allQuestionsAfterCreation.size());

		super.unauthenticate();

	}

	/**
	 * This test checks that when a question is deleted, findAll method returns one less question than before the deletion
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testFindAllDeletingQuestion() {
		int questionId;
		Question question;
		final Collection<Question> allQuestionsBeforeDeletion, allQuestionsAfterDeletion;

		questionId = super.getEntityId("Question6");

		super.authenticate("User1");
		allQuestionsBeforeDeletion = this.questionService.findAll();

		question = this.questionService.findOne(questionId);

		this.questionService.delete(question);
		this.questionService.flush();

		allQuestionsAfterDeletion = this.questionService.findAll();

		Assert.isTrue(allQuestionsBeforeDeletion.size() - 1 == allQuestionsAfterDeletion.size());

		super.unauthenticate();
	}

	// Ancillary methods ------------------------------------------------------

	/**
	 * This method is a template of tests executed with parameters given in driverCreateQuestion
	 * 
	 * @param username
	 *            of the user in populateDatabase.xml
	 * @param rendezvousId
	 *            of the rendezvous to add the question
	 * @param text
	 *            of the question
	 * @param expected
	 *            exception, or null if no exception is expected
	 * 
	 * @author Juanmi
	 */
	protected void templateCreate(final String username, final int rendezvousId, final String text, final Class<?> expected) {
		Class<?> caught;
		Question question;
		Rendezvous rendezvous;
		Collection<Answer> answers;

		answers = new HashSet<Answer>();
		caught = null;

		try {
			rendezvous = this.rendezvousService.findOne(rendezvousId);

			super.authenticate(username);

			question = this.questionService.create();
			question.setAnswers(answers);
			question.setText(text);

			this.questionService.save(question, rendezvous);
			this.questionService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
