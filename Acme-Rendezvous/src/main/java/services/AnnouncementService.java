package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AnnouncementRepository;
import domain.Announcement;

@Service
@Transactional
public class AnnouncementService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AnnouncementRepository	announcementRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Announcement create() {
		Announcement result;

		result = new Announcement();

		return result;
	}

	public Collection<Announcement> findAll() {

		Collection<Announcement> result;

		Assert.notNull(this.announcementRepository);
		result = this.announcementRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Announcement findOne(final int announcementId) {

		Announcement result;

		result = this.announcementRepository.findOne(announcementId);

		return result;

	}

	public Announcement save(final Announcement announcement) {

		assert announcement != null;

		Announcement result;

		result = this.announcementRepository.save(announcement);

		return result;

	}

	public void delete(final Announcement announcement) {

		assert announcement != null;
		assert announcement.getId() != 0;

		Assert.isTrue(this.announcementRepository.exists(announcement.getId()));

		this.announcementRepository.delete(announcement);

	}
}

