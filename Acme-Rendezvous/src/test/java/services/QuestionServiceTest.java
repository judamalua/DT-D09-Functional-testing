
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
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
	 * This test checks that authenticated users can create questions to a rendezvous they created saved as draft mode
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testCreateQuestionToCreatedRendezvous() {
		// Functional requirement number 21.1: An actor who is authenticated as a user must be able to manage (add, edit, delete) the questions
		// that are associated with a rendezvous on draft mode that he or she’s created previously
		int rendezvousId;
		Question question;
		Rendezvous rendezvous;

		rendezvousId = super.getEntityId("Rendezvous8");
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		super.authenticate("User1");

		question = this.questionService.create();
		question.setText("Test");

		this.questionService.save(question, rendezvous);

		super.unauthenticate();
	}
}
