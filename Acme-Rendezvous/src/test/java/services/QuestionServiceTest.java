
package services;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
	 * This test checks that authenticated users can edit questions of the rendezvouses they created saved as draft mode
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testEditQuestionToCreatedDraftModeRendezvous() {
		// Functional requirement number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions
		// that are associated with a rendezvous on draft mode that he or she’s created previously
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

		super.unauthenticate();
	}

	/**
	 * This test checks that authenticated users can delete questions of the rendezvouses they created saved as draft mode
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testDeleteQuestionToCreatedDraftModeRendezvous() {
		// Functional requirement number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions
		// that are associated with a rendezvous on draft mode that he or she’s created previously
		int questionId;
		Question question;

		questionId = super.getEntityId("Question6");

		super.authenticate("User1");
		question = this.questionService.findOne(questionId);

		this.questionService.delete(question);

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
				"User1", "Rendezvous8", null
			}, {
				// This test checks that authenticated users cannot create questions to the rendezvouses they created saved as final mode
				"User1", "Rendezvous1", IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated users cannot manage questions of a rendezvous in draft mode
				null, "Rendezvous8", IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated users cannot create questions to a rendezvous in final mode
				null, "Rendezvous1", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create questions to a rendezvous in draft mode
				"Admin1", "Rendezvous8", ClassCastException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create questions to a rendezvous in final mode
				"Admin1", "Rendezvous1", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create questions to a draft mode rendezvous they did not create
				"User2", "Rendezvous8", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create questions to a final mode rendezvous they did not create
				"User2", "Rendezvous1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateCreate(final String username, final int rendezvousId, final Class<?> expected) {
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
			question.setText("Test");

			this.questionService.save(question, rendezvous);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
