
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;
import domain.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select u from User u join u.comments c where c = ?1")
	public User getUserFromComment(Comment comment);
}
