
package controllers.user;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/answer/user")
public class AnswerUserController {

	@Autowired
	UserService			userService;
	@Autowired
	QuestionService		questionService;
	@Autowired
	AnswerService		answerService;
	@Autowired
	RendezvousService	rendezvousService;
	@Autowired
	ActorService		actorService;


	// Constructors -----------------------------------------------------------

	public AnswerUserController() {
		super();
	}

	// Editing --------------------------------------------

	/**
	 * This method is called when an user tries to join a rendezvous, the user is the one logged in and the rendezvous is the id that it receives as a parameter
	 * 
	 * @param rendezvousId
	 *            The id of the rendezvous to join
	 * @author Daniel Diment
	 * @return
	 *         The next view showed to the user
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		User user;
		try {
			user = (User) (this.actorService.findActorByPrincipal());
			rendezvous = this.rendezvousService.findOne(rendezvousId);			//Checks that the rendezvous is valid
			Assert.notNull(rendezvous);
			Assert.isTrue(rendezvous.getFinalMode());							//Checks that the rendezvous is in final mode
			Assert.isTrue(!rendezvous.getDeleted());							//Checks that the rendezvous is not deleted
			Assert.isTrue(rendezvous.getMoment().after(new Date()));			//Checks that the rendezvous is not already over
			Assert.isTrue(!rendezvous.getUsers().contains(user));				//Checks the user hasn't already joined to the rendezvous
			Assert.isTrue(!user.getCreatedRendezvouses().contains(rendezvous));	//Checks that the user that created the rendezvous is not attempting to join it
			if (rendezvous.getAdultOnly())
				Assert.isTrue(this.actorService.checkUserIsAdult(user));		//Checks that the user is old enough to join the rendezvous
			if (rendezvous.getQuestions().isEmpty()) {
				this.rendezvousService.RSVP(rendezvous);	//If the rendezvous has no questions, it just lets the user join the rendezvous directly
				return new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");
			}
			result = this.createEditModelAndView(rendezvous.getQuestions(), rendezvousId);		//If it has any questions, the user is sent to the answering form
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");					//If any of the asserts fails, the user is sent to the 403 page
		}
		return result;
	}

	// Saving --------------------------------------------

	/**
	 * This method is called when the user returns from the answering form and lets the user join the rendezvous
	 * 
	 * @param answers
	 *            An array containing the text of the different answers that the user answered
	 * @param rendezvousId
	 *            The id of the rendezvous the user is trying to join
	 * @author Daniel Diment
	 * @return The next view the user should be redirected into
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam("answers") final List<String> answers, @RequestParam("rendezvousId") final int rendezvousId) {
		Rendezvous rendezvous;
		ModelAndView result;
		User user;
		try {
			user = (User) (this.actorService.findActorByPrincipal());
			rendezvous = this.rendezvousService.findOne(rendezvousId);			//Checks that the rendezvous is valid
			Assert.notNull(rendezvous);
			Assert.isTrue(rendezvous.getFinalMode());							//Checks that the rendezvous is in final mode
			Assert.isTrue(!rendezvous.getDeleted());							//Checks that the rendezvous is not deleted
			Assert.isTrue(rendezvous.getMoment().after(new Date()));			//Checks that the rendezvous is not already over
			Assert.isTrue(!rendezvous.getUsers().contains(user));			 	//Checks the user hasn't already joined to the rendezvous
			Assert.isTrue(!user.getCreatedRendezvouses().contains(rendezvous));	//Checks that the user that created the rendezvous is not attempting to join it
			Assert.isTrue(answers.size() == rendezvous.getQuestions().size());	//Checks that the number of question received is the same than the questions that the rendezvous contains
			if (rendezvous.getAdultOnly())
				Assert.isTrue(this.actorService.checkUserIsAdult(user));		//Checks that the user is old enough to join the rendezvous
		} catch (final Throwable oops) {
			return new ModelAndView("redirect:/misc/403");						//If any of the checks fails, the system will redirect the user to the 403 page
		}

		//This whole for block checks that every answer given by the user is not empty or null
		//if any of them are, they are returned to the form so they can complete it properly.
		//The regular expression checks if the answer is not just white spaces
		final Pattern allWhitespaces = Pattern.compile("\\s");
		for (final String answerText : answers) {
			final Matcher matcher = allWhitespaces.matcher(answerText);
			if (answerText.equals("") || answerText == null || matcher.matches())
				return this.createEditModelAndView(rendezvous.getQuestions(), rendezvousId, "answer.emptyAnswer");
		}

		int iterator = 0; //This pointer helps to locate every answer in the List that contains the text of all the answer

		try {
			//This for block creates and saves the answer for every single question it's made
			for (final Question question : rendezvous.getQuestions()) {
				Answer answer;
				answer = this.answerService.create();
				answer.setText(answers.get(iterator));
				answer.setQuestion(question);
				answer.setUser(user);
				this.answerService.save(answer);
				iterator++;
			}
			this.rendezvousService.RSVP(rendezvous);	//Finally here, the user get's added to the rendezvous
			result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");
		} catch (final Throwable oops) {
			//If any error is made during the commit, it will make the user return to the form
			result = this.createEditModelAndView(rendezvous.getQuestions(), rendezvousId, "answer.commit.error");
		}

		return result;
	}
	// Deleting ----------------------------------------------

	/**
	 * The following method is called to make the user not to assist to a rendezvous and delete all his answers to the question of that rendezvous
	 * 
	 * @param rendezvousId
	 *            The id of the rendezvous the user is not going to assist
	 * @author Daniel Diment
	 * @return
	 *         The next view that is going to be shown to the user
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam("rendezvousId") final int rendezvousId) {
		Rendezvous rendezvous;
		ModelAndView result;
		final User user = (User) this.actorService.findActorByPrincipal();
		try {
			rendezvous = this.rendezvousService.findOne(rendezvousId);			//Checks that the rendezvous is valid
			Assert.notNull(rendezvous);
			Assert.isTrue(rendezvous.getFinalMode());							//Checks that the rendezvous is in final mode
			Assert.isTrue(!rendezvous.getDeleted());							//Checks that the rendezvous is not deleted
			Assert.isTrue(rendezvous.getMoment().after(new Date()));			//Checks that the rendezvous is not already over
			Assert.isTrue(rendezvous.getUsers().contains(user));				//Checks the user has already joined to the rendezvous
			Assert.isTrue(!user.getCreatedRendezvouses().contains(rendezvous));	//Checks that the user that created the rendezvous is not attempting to leave it

			//This for block finds every answer the user gave to join the rendezvous and deletes it
			for (final Question question : rendezvous.getQuestions()) {
				final Answer answer = this.answerService.getAnswerByUserIdAndQuestionId(user.getId(), question.getId());
				this.answerService.delete(answer);
			}
			this.rendezvousService.disRSVP(rendezvous);	//Here the user is not assisting anymore to the rendezvous
			result = new ModelAndView("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");
		} catch (final Throwable oops) {
			//If any error is made during whole process, it will make the user go to the 403 page
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Ancillary methods -------------------------------------

	/**
	 * Creates a view of questions for the user to answer
	 * 
	 * @param questions
	 *            The questions to answer
	 * @param rendezvousId
	 *            The id of the rendezvous to join
	 * @author Daniel Diment
	 * @return
	 *         The view itself
	 */
	protected ModelAndView createEditModelAndView(final Collection<Question> questions, final int rendezvousId) {
		ModelAndView result;

		result = this.createEditModelAndView(questions, rendezvousId, null);

		return result;
	}
	/**
	 * Creates a view of questions for the user to answer
	 * 
	 * @param questions
	 *            The questions to answer
	 * @param rendezvousId
	 *            The id of the rendezvous to join
	 * @param messageCode
	 *            The message is going to be shown to the user
	 * @author Daniel Diment
	 * @return
	 *         The view itself
	 */
	protected ModelAndView createEditModelAndView(final Collection<Question> questions, final int rendezvousId, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("answer/user/edit");
		result.addObject("questions", questions);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
