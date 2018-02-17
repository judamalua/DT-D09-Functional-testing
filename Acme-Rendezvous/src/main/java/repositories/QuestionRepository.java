
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

	// Dashboard queries.

	/**
	 * Level A query 2
	 * 
	 * @return The average and the standard deviation of the number of answers to the questions per rendezvous.
	 * @author Juanmi
	 */
	//	@Query("select a from Answer a where a.question.id = (select q.id from Question q)")
	//	String getAnswersInfoFromQuestion();
	//TODO Ask on monday

}
