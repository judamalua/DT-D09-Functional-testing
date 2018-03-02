
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.DomainService;

@Component
@Transactional
public class ServiceToStringConverter implements Converter<DomainService, String> {

	@Override
	public String convert(final DomainService service) {
		String result;

		if (service == null)
			result = null;
		else
			result = String.valueOf(service.getId());

		return result;
	}

}
