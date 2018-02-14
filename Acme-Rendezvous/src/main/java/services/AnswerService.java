//
// package services;
//
// import java.util.Collection;
//
// import javax.transaction.Transactional;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.util.Assert;
//
// import repositories.AnswerRepository;
// import domain.Answer;
// import domain.Question;
// import domain.User;
//
// @Service
// @Transactional
// public class AnswerService {
//
// // Managed repository --------------------------------------------------
//
// @Autowired
// private AnswerRepository answerRepository;
//
// // Supporting services --------------------------------------------------
//
// @Autowired
// private QuestionService questionService;
//
// @Autowired
// private UserService userService;
//
//
// // Simple CRUD methods --------------------------------------------------
//
// /**
// * Creates a new answer
// *
// * @author Daniel Diment
// * @return the new answer
// */
// public Answer create() {
// Answer result;
//
// result = new Answer();
//
// return result;
// }
//
// /**
// * Gets all the answers of the database
// *
// * @author Daniel Diment
// * @return The collection containing the all the answers
// */
// public Collection<Answer> findAll() {
//
// Collection<Answer> result;
//
// Assert.notNull(this.answerRepository);
// result = this.answerRepository.findAll();
// Assert.notNull(result);
//
// return result;
//
// }
// /**
// * Gets the answer of the database that has that id
// *
// * @param answerId
// * The id you want to search
// * @author Daniel Diment
// * @return The answer with that id
// */
// public Answer findOne(final int answerId) {
//
// Answer result;
//
// result = this.answerRepository.findOne(answerId);
//
// return result;
//
// }
//
// /**
// * Saves an answer to the database
// *
// * @param answer
// * The answer you want to save
// * @author Daniel Diment
// * @return The saved answer
// */
// public Answer save(final Answer answer) {
//
// assert answer != null;
//
// Answer result;
//
// final Question question = this.questionService.getQuestionByAnswerId(answer.getId()); //We get the question that is related to this answer
// Assert.notNull(question);
//
// final User user = this.userService.getUserFromAnswerId(answer.getId()); //We get the user that is related to this answer
// Assert.notNull(user);
//
// result = this.answerRepository.save(answer);
//
// if (question.getAnswers().contains(answer))
// question.getAnswers().remove(answer); //Delete the answer if the question contained a previous version
//
// if (user.getAnswers().contains(answer))
// user.getAnswers().remove(answer); //Delete the answer if the user contained a previous version
//
// question.getAnswers().add(result);
// this.questionService.save(question); //Add and save the question with the answer inside
//
// user.getAnswers().add(result);
// this.userService.save(user); //Add and save the user with the answer inside
//
// return result;
//
// }
//
// /**
// *
// * @param answer
// */
// public void delete(final Answer answer) {
//
// assert answer != null;
// assert answer.getId() != 0;
//
// Assert.isTrue(this.answerRepository.exists(answer.getId()));
//
// final Question question = this.questionService.getQuestionByAnswerId(answer.getId()); //We get the question that is related to this answer
// question.getAnswers().remove(answer); //Delete the answer from the question
// this.questionService.save(question); //Save the question with the answer deleted
//
// final User user = this.userService.getUserFromAnswerId(answer.getId()); //We get the user that is related to this answer
// user.getAnswers().remove(answer); //Delete the answer from the user
// this.userService.save(user); //Save the user with the answer deleted
//
// this.answerRepository.delete(answer);
//
// }
//}
