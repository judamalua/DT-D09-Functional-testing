
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Announcement;
import domain.Comment;
import domain.Question;
import domain.Rendezvous;
import domain.Request;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RendezvousServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private ActorService		actorService;


	// Tests ------------------------------------------------------------------

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is authenticated as a user must be able to create a rendezvous, which he’s implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as they aren’t saved them in final mode.
	 * Once a rendezvous is saved in final mode, it cannot be edited or deleted by the creator
	 * 
	 * 5.3: An actor who is authenticated as a user must be able to update or delete the rendezvouses that he or she’s created. Deletion is virtual, that
	 * is: the information is not removed from the database, but the rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * 5.4: An actor who is authenticated as a user must be able to RSVP a rendezvous or cancel it. When a user RSVPs a rendezvous, he or she is assumed to attend it.
	 * 
	 * @author Juanmi
	 */
	@Test
	public void driverCreateRendezvous() {
		final Long oneDay = TimeUnit.DAYS.toMillis(1);
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can create a rendezvous
				"User1", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, null
			}, {
				// This test checks that authenticated users can create a rendezvous with an empty photo URL
				"User1", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "", "123.12,123.12", false, false, null
			}, {
				// This test checks that authenticated users can create a rendezvous in final mode
				"User1", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "", "123.12,123.12", true, false, null
			}, {
				// This test checks that authenticated users can create a rendezvous as adult only
				"User1", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "", "123.12,123.12", false, true, null
			}, {
				// This test checks that authenticated users can create a rendezvous in final mode and as adult only
				"User1", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "", "123.12,123.12", true, true, null
			}, {
				// This test checks that unauthenticated users cannot create a rendezvous
				null, "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, IllegalArgumentException.class
			}, {
				// This test checks that actors not logged as users cannot create a rendezvous
				"Admin1", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, ClassCastException.class
			}, {
				// This test checks that a rendezvous with empty name cannot be created
				"User1", "", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with empty description cannot be created
				"User1", "Test", "", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with null moment cannot be created
				"User1", "Test", "", null, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with past moment cannot be created
				"User1", "Test", "", new Date(System.currentTimeMillis() - oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with empty GPS Coordinates cannot be created
				"User1", "Test", "", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "", false, false, javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
				(Class<?>) testingData[i][8]);
	}

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is authenticated as a user must be able to create a rendezvous, which he’s implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as they aren’t saved them in final mode.
	 * Once a rendezvous is saved in final mode, it cannot be edited or deleted by the creator
	 * 
	 * 5.3: An actor who is authenticated as a user must be able to update or delete the rendezvouses that he or she’s created. Deletion is virtual, that
	 * is: the information is not removed from the database, but the rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * 5.4: An actor who is authenticated as a user must be able to RSVP a rendezvous or cancel it. When a user RSVPs a rendezvous, he or she is assumed to attend it.
	 * 
	 * @author Juanmi
	 */
	@Test
	public void driverEditRendezvous() {
		final Long oneDay = TimeUnit.DAYS.toMillis(1);
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can edit a draft mode rendezvous they created
				"User1", "Rendezvous8", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, null
			},
			{
				// This test checks that authenticated users cannot edit a rendezvous they did not create
				"User2", "Rendezvous8", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false,
				org.springframework.dao.DataIntegrityViolationException.class
			},
			{
				// This test checks that authenticated actors that are not users cannot edit a draft mode rendezvous they created
				"Admin1", "Rendezvous8", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false,
				org.springframework.dao.DataIntegrityViolationException.class
			}, {
				// This test checks that unauthenticated users cannot edit a rendezvous
				null, "Rendezvous8", "Test", "Test", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Boolean) testingData[i][7],
				(Boolean) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateCreate(final String username, final String name, final String description, final Date moment, final String pictureUrl, final String gpsCoordinates, final boolean finalMode, final boolean adultOnly, final Class<?> expected) {
		Class<?> caught;
		final Rendezvous rendezvous;
		User user;
		Collection<Question> questions;
		Collection<Rendezvous> similars;
		Collection<Announcement> announcements;
		Collection<Comment> comments;
		Collection<User> users;
		Collection<Request> requests;

		caught = null;

		try {
			super.authenticate(username);

			rendezvous = this.rendezvousService.create();

			questions = new HashSet<Question>();
			similars = rendezvous.getSimilars();
			announcements = new HashSet<Announcement>();
			comments = new HashSet<Comment>();
			users = new HashSet<User>();
			requests = new HashSet<Request>();
			user = (User) this.actorService.findActorByPrincipal();
			users.add(user);

			if (similars == null)
				similars = new HashSet<Rendezvous>();

			similars.remove(null);
			rendezvous.setQuestions(questions);
			rendezvous.setAnnouncements(announcements);
			rendezvous.setComments(comments);
			rendezvous.setSimilars(similars);
			rendezvous.setUsers(users);
			rendezvous.setRequests(requests);

			rendezvous.setName(name);
			rendezvous.setDescription(description);
			rendezvous.setMoment(moment);
			rendezvous.setPictureUrl(pictureUrl);
			rendezvous.setGpsCoordinates(gpsCoordinates);
			rendezvous.setFinalMode(finalMode);
			rendezvous.setAdultOnly(adultOnly);

			this.rendezvousService.save(rendezvous);
			this.rendezvousService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void templateEdit(final String username, final String rendezvousPopulateName, final String name, final String description, final Date moment, final String pictureUrl, final String gpsCoordinates, final boolean finalMode,
		final boolean adultOnly, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		final Rendezvous rendezvous;

		caught = null;

		try {
			super.authenticate(username);

			rendezvousId = super.getEntityId(rendezvousPopulateName);

			rendezvous = this.rendezvousService.findOne(rendezvousId);

			rendezvous.setName(name);
			rendezvous.setDescription(description);
			rendezvous.setMoment(moment);
			rendezvous.setPictureUrl(pictureUrl);
			rendezvous.setGpsCoordinates(gpsCoordinates);
			rendezvous.setFinalMode(finalMode);
			rendezvous.setAdultOnly(adultOnly);

			this.rendezvousService.save(rendezvous);
			this.rendezvousService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
