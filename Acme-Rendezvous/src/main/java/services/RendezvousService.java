package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RendezvousRepository;
import domain.Rendezvous;

@Service
@Transactional
public class RendezvousService {

	// Managed repository --------------------------------------------------

	@Autowired
	private RendezvousRepository	rendezvousRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Rendezvous create() {
		Rendezvous result;

		result = new Rendezvous();

		return result;
	}

	public Collection<Rendezvous> findAll() {

		Collection<Rendezvous> result;

		Assert.notNull(this.rendezvousRepository);
		result = this.rendezvousRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Rendezvous findOne(final int rendezvousId) {

		Rendezvous result;

		result = this.rendezvousRepository.findOne(rendezvousId);

		return result;

	}

	public Rendezvous save(final Rendezvous rendezvous) {

		assert rendezvous != null;

		Rendezvous result;

		result = this.rendezvousRepository.save(rendezvous);

		return result;

	}

	public void delete(final Rendezvous rendezvous) {

		assert rendezvous != null;
		assert rendezvous.getId() != 0;

		Assert.isTrue(this.rendezvousRepository.exists(rendezvous.getId()));

		this.rendezvousRepository.delete(rendezvous);

	}
}

