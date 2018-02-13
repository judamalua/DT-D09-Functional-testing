
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.RendezvousRepository;
import domain.Rendezvous;

@Component
@Transactional
public class StringToRendezVousConverter implements Converter<String, Rendezvous> {

	@Autowired
	RendezvousRepository	rendezvousRepository;


	@Override
	public Rendezvous convert(final String text) {
		Rendezvous result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.rendezvousRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
