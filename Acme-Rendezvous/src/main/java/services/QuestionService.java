package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import domain.Question;

@Service
@Transactional
public class QuestionService {

	// Managed repository --------------------------------------------------

	@Autowired
	private QuestionRepository	questionRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Question create() {
		Question result;

		result = new Question();

		return result;
	}

	public Collection<Question> findAll() {

		Collection<Question> result;

		Assert.notNull(this.questionRepository);
		result = this.questionRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Question findOne(final int questionId) {

		Question result;

		result = this.questionRepository.findOne(questionId);

		return result;

	}

	public Question save(final Question question) {

		assert question != null;

		Question result;

		result = this.questionRepository.save(question);

		return result;

	}

	public void delete(final Question question) {

		assert question != null;
		assert question.getId() != 0;

		Assert.isTrue(this.questionRepository.exists(question.getId()));

		this.questionRepository.delete(question);

	}
}

