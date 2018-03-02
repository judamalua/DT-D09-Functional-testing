
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnswerServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private AnswerService		answerService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private Validator			validator;


	// Tests ------------------------------------------------------------------

	/**
	 * This test checks that authenticated users can answer to questions
	 */
	@Test
	public void testAnswerQuestions() {
		// Functional requirement number 21.2: An actor who is authenticated as a user must be able to Answer the questions that are associated
		// with a rendezvous that he or she’s RSVPing now.
		Collection<Question> questions;
		int rendezvousId;
		Rendezvous rendezvous;
		Answer answer, savedAnswer;
		User user;

		rendezvousId = super.getEntityId("Rendezvous1");
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		questions = rendezvous.getQuestions();

		super.authenticate("User2");

		user = (User) this.actorService.findActorByPrincipal();

		for (final Question question : questions) {
			answer = this.answerService.create();
			answer.setText("Test");
			answer.setUser(user);
			savedAnswer = this.answerService.save(answer);
			question.getAnswers().add(savedAnswer);
		}

		super.unauthenticate();
	}

	/**
	 * This test checks that authenticated users cannot add empty answers to questions
	 */
	@Test(expected = IllegalArgumentException.class)
	//TODO WAIT FOR DANI TO MAKE HIS CHANGES TO FIX THIS
	public void testEmptyAnswerQuestions() {
		// Functional requirement number 21.2: An actor who is authenticated as a user must be able to Answer the questions that are associated
		// with a rendezvous that he or she’s RSVPing now.
		Collection<Question> questions;
		int rendezvousId;
		Rendezvous rendezvous;
		Answer answer, savedAnswer;
		User user;

		rendezvousId = super.getEntityId("Rendezvous1");
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		questions = rendezvous.getQuestions();

		super.authenticate("User2");

		user = (User) this.actorService.findActorByPrincipal();

		for (final Question question : questions) {
			answer = this.answerService.create();
			answer.setText("");
			answer.setUser(user);
			savedAnswer = this.answerService.save(answer);
			question.getAnswers().add(savedAnswer);
		}

		super.unauthenticate();
	}

	/**
	 * This test checks that unauthenticated users cannot answer to questions
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUnauthenticatedAnswerQuestions() {
		// Functional requirement number 21.2: An actor who is authenticated as a user must be able to Answer the questions that are associated
		// with a rendezvous that he or she’s RSVPing now.
		Collection<Question> questions;
		int rendezvousId;
		Rendezvous rendezvous;
		Answer answer, savedAnswer;
		User user;

		rendezvousId = super.getEntityId("Rendezvous1");
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		questions = rendezvous.getQuestions();

		super.authenticate(null);

		user = (User) this.actorService.findActorByPrincipal();

		for (final Question question : questions) {
			answer = this.answerService.create();
			answer.setText("Test");
			answer.setUser(user);
			savedAnswer = this.answerService.save(answer);
			question.getAnswers().add(savedAnswer);
		}

		super.unauthenticate();
	}

}
