
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.CommentRepository;
import domain.Comment;

@Component
@Transactional
public class StringToCommentConverter implements Converter<String, Comment> {

	// Service --------------
	@Autowired
	private CommentRepository	commentRepository;


	@Override
	public Comment convert(final String string) {
		Comment result;
		int commentId;

		try {
			if (StringUtils.isEmpty(string))
				result = null;
			else {
				commentId = Integer.valueOf(string);
				result = this.commentRepository.findOne(commentId);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
