
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

	// Dashboard queries

	/**
	 * Level A query 2
	 * 
	 * @return The average and the standard deviation of the number of answers to the questions per rendezvous.
	 * @author Juanmi
	 */
	@Query("select avg(q.answers.size), sqrt(sum(q.answers.size * q.answers.size) / count(q.answers.size) - (avg(q.answers.size) * avg(q.answers.size))) from Question q")
	String getAnswersInfoFromQuestion();

}
