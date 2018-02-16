
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.QuestionService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/question/user")
public class QuestionUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	QuestionService			questionService;
	@Autowired
	ActorService			actorService;
	@Autowired
	RendezvousService		rendezvousService;
	@Autowired
	ConfigurationService	configurationService;


	// Listing ----------------------------------------------------

	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Question> questions;
		Rendezvous rendezvous;
		User user;
		String rendezvousName;
		boolean rendezvousIsInFinalMode;

		try {

			result = new ModelAndView("question/list");
			user = (User) this.actorService.findActorByPrincipal();
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			Assert.notNull(rendezvous);
			// Checking if the user trying to access is the creator of this Rendezvous
			Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));
			rendezvousName = rendezvous.getName();
			questions = rendezvous.getQuestions();
			rendezvousIsInFinalMode = rendezvous.getFinalMode();

			result.addObject("rendezvousName", rendezvousName);
			result.addObject("rendezvousId", rendezvousId);
			result.addObject("questions", questions);
			result.addObject("rendezvousIsInFinalMode", rendezvousIsInFinalMode);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int questionId) {
		ModelAndView result;
		Question question;
		int rendezvousId;

		try {
			question = this.questionService.findOne(questionId);
			Assert.notNull(question);
			rendezvousId = this.rendezvousService.getRendezvousByQuestion(questionId).getId();

			this.questionService.checkUserCreatedRendezvousOfQuestion(question);

			result = this.createEditModelAndView(question);
			result.addObject("rendezvousId", rendezvousId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Question question;
		Rendezvous rendezvous;
		User user;

		try {
			user = (User) this.actorService.findActorByPrincipal();
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			question = this.questionService.create();
			result = this.createEditModelAndView(question);

			Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous));

			result.addObject("rendezvousId", rendezvousId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
			result.addObject("rendezvousId", rendezvousId);
		}

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam final int rendezvousId, @Valid final Question question, final BindingResult binding) {
		ModelAndView result;
		Rendezvous rendezvous;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(question, "question.params.error");
			result.addObject("rendezvousId", rendezvousId);
		} else
			try {
				rendezvous = this.rendezvousService.findOne(rendezvousId);
				Assert.notNull(rendezvous);
				this.questionService.save(question, rendezvous);
				result = new ModelAndView("redirect:list.do?rendezvousId=" + rendezvousId);

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(question, "question.commit.error");
				result.addObject("rendezvousId", rendezvousId);
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@RequestParam final int rendezvousId, final Question question, final BindingResult binding) {
		ModelAndView result;

		try {
			this.questionService.delete(question);
			result = new ModelAndView("redirect:list.do?rendezvousId=" + rendezvousId);

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(question, "question.commit.error");
			result.addObject("rendezvousId", rendezvousId);
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Question question) {
		ModelAndView result;

		result = this.createEditModelAndView(question, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Question question, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("question/edit");
		result.addObject("question", question);

		result.addObject("message", messageCode);

		return result;

	}
}
