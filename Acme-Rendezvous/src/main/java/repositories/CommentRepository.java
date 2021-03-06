
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

	@Query("select c from Comment c join c.comments c1 where c1 = ?1")
	public Comment getFatherCommentFromReply(Comment reply);

	// Dashboard queries

	/**
	 * Level A query 3
	 * 
	 * @return The average and the standard deviation of replies per comment.
	 * @author Juanmi
	 */
	@Query("select avg(c.comments.size), sqrt(sum(c.comments.size * c.comments.size) / count(c.comments.size) - (avg(c.comments.size) * avg(c.comments.size))) from Comment c")
	String getRepliesInfoFromComment();
}
