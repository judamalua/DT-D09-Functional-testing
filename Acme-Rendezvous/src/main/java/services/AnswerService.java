package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import domain.Answer;

@Service
@Transactional
public class AnswerService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AnswerRepository	answerRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Answer create() {
		Answer result;

		result = new Answer();

		return result;
	}

	public Collection<Answer> findAll() {

		Collection<Answer> result;

		Assert.notNull(this.answerRepository);
		result = this.answerRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Answer findOne(final int answerId) {

		Answer result;

		result = this.answerRepository.findOne(answerId);

		return result;

	}

	public Answer save(final Answer answer) {

		assert answer != null;

		Answer result;

		result = this.answerRepository.save(answer);

		return result;

	}

	public void delete(final Answer answer) {

		assert answer != null;
		assert answer.getId() != 0;

		Assert.isTrue(this.answerRepository.exists(answer.getId()));

		this.answerRepository.delete(answer);

	}
}

