
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import domain.Question;
import domain.Rendezvous;

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


	// Constructors -----------------------------------------------------------

	public AnswerUserController() {
		super();
	}

	// Editing --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;

		try {
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			Assert.notNull(rendezvous);
			result = this.createEditModelAndView(rendezvous.getQuestions(), rendezvousId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Saving --------------------------------------------

	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	//	public ModelAndView saveDraft(@RequestParam("answers[]") final List<String> answers, @RequestParam("rendezvousId") final int rendezvousId) {
	//		Rendezvous rendezvous;
	//		ModelAndView result;
	//		try {
	//			rendezvous = this.rendezvousService.findOne(rendezvousId);
	//			Assert.notNull(rendezvous);
	//		} catch (final Throwable oops) {
	//			return new ModelAndView("redirect:/misc/403");
	//		}
	//		for (final String s : answers)
	//			if (s.equals("") || s == null)
	//				return this.createEditModelAndView(rendezvous.getQuestions(), rendezvousId, "answer.emptyAnswer");
	//		final int i=0;
	//		for(final Question q : rendezvous.getQuestions())
	//			Answer a = this.answerService.create();
	//			a.setText(answers.get(i));
	//			final a.s
	//			i++;
	//		try {
	//				this.auditRecordService.save(auditRecord);
	//				result = new ModelAndView("redirect:list.do");
	//			} catch (final Throwable oops) {
	//				result = this.createEditModelAndView(auditRecord, "auditRecord.commit.error");
	//
	//			}
	//
	//		return result;
	//	}
	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final Collection<Question> questions, final int rendezvousId) {
		ModelAndView result;

		result = this.createEditModelAndView(questions, rendezvousId, null);

		return result;
	}
	protected ModelAndView createEditModelAndView(final Collection<Question> questions, final int rendezvousId, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("answer/edit");
		result.addObject("questions", questions);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
