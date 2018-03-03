
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
				"User1", "Prueba", "Prueba", new Date(System.currentTimeMillis() + oneDay), "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
				(Class<?>) testingData[i][8]);
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

}
