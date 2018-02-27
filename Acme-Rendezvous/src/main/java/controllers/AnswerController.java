
package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import domain.Actor;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/answer")
public class AnswerController {

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

	public AnswerController() {
		super();
	}

	// Listing --------------------------------------------
	/**
	 * Giving a rendezvous, it gives a view containing all the user joined and the answers they gave to join in
	 * 
	 * @param rendezvousId
	 *            The rendezvous to check
	 * @author Daniel Diment
	 * @return
	 *         The view
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		Actor actor;

		try {
			actor = this.actorService.findActorByPrincipal();
		} catch (final Throwable oops) {
			actor = null;
		}
		try {
			rendezvous = this.rendezvousService.findOne(rendezvousId);			//Checks that the rendezvous is valid
			Assert.notNull(rendezvous);
			Assert.isTrue(rendezvous.getFinalMode());							//Checks that the rendezvous is in final mode
			Assert.isTrue(!rendezvous.getDeleted());							//Checks that the rendezvous is not deleted
			if (rendezvous.getAdultOnly()) {
				Assert.notNull(actor);
				Assert.isTrue(this.actorService.checkUserIsAdult(actor));		//Checks that the actor is old enough to see the rendezvous answers
			}
		} catch (final Throwable oops) {
			return new ModelAndView("redirect:/misc/403");					//If any of the asserts fails, the user is sent to the 403 page
		}

		result = new ModelAndView("answer/user/list");									//Redirects to the view of list
		result.addObject("creator", this.userService.getCreatorUser(rendezvousId));		//Adds the creator of the rendezvous
		result.addObject("usersAndAnswers", this.generateUsersAndAnswers(rendezvous));	//Adds the users that are going to assists with their answers
		if (actor == null)
			result.addObject("anonymous", true);
		else
			result.addObject("anonymous", false);
		return result;
	}
	/**
	 * Giving a rendezvous, it creates a map containing all the users and the list of answers he gave to join it
	 * 
	 * @param rendezvous
	 *            The rendezvous to check
	 * @author Daniel Diment
	 * @return
	 *         The Map of users and list of answers
	 */
	private Map<User, List<Answer>> generateUsersAndAnswers(final Rendezvous rendezvous) {
		final Map<User, List<Answer>> usersAndAnswers = new HashMap<User, List<Answer>>();
		for (final User joinedUser : rendezvous.getUsers())
			if (!joinedUser.equals(this.userService.getCreatorUser(rendezvous.getId()))) {
				usersAndAnswers.put(joinedUser, new ArrayList<Answer>());
				for (final Question question : rendezvous.getQuestions()) {
					final Answer answer = this.answerService.getAnswerByUserIdAndQuestionId(joinedUser.getId(), question.getId());
					usersAndAnswers.get(joinedUser).add(answer);
				}
			}
		return usersAndAnswers;
	}
}
