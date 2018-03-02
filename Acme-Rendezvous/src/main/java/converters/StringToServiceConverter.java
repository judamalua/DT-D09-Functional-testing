
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ServiceRepository;
import domain.DomainService;

@Component
@Transactional
public class StringToServiceConverter implements Converter<String, DomainService> {

	@Autowired
	ServiceRepository	serviceRepository;


	@Override
	public DomainService convert(final String text) {
		DomainService result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.serviceRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
