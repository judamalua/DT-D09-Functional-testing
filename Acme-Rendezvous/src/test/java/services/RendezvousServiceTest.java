
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
import org.springframework.util.Assert;

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
	@Autowired
	private UserService			userService;


	// Tests ------------------------------------------------------------------

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is authenticated as a user must be able to create a rendezvous, which he’s implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as they aren't saved them in final mode.
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
		final Date currentDatePlusOneDay = new Date(System.currentTimeMillis() + oneDay);
		final Date currentDateMinusOneDay = new Date(System.currentTimeMillis() - oneDay);
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can create a rendezvous
				"User1", "Test", "Test", currentDatePlusOneDay, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, null
			}, {
				// This test checks that authenticated users can create a rendezvous with an empty photo URL
				"User1", "Test", "Test", currentDatePlusOneDay, "", "123.12,123.12", false, false, null
			}, {
				// This test checks that authenticated users can create a rendezvous in final mode
				"User1", "Test", "Test", currentDatePlusOneDay, "", "123.12,123.12", true, false, null
			}, {
				// This test checks that authenticated users can create a rendezvous as adult only
				"User1", "Test", "Test", currentDatePlusOneDay, "", "123.12,123.12", false, true, null
			}, {
				// This test checks that authenticated users can create a rendezvous in final mode and as adult only
				"User1", "Test", "Test", currentDatePlusOneDay, "", "123.12,123.12", true, true, null
			}, {
				// This test checks that unauthenticated users cannot create a rendezvous
				null, "Test", "Test", currentDatePlusOneDay, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, IllegalArgumentException.class
			}, {
				// This test checks that actors not logged as users cannot create a rendezvous
				"Admin1", "Test", "Test", currentDatePlusOneDay, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, ClassCastException.class
			}, {
				// This test checks that a rendezvous with empty name cannot be created
				"User1", "", "Test", currentDatePlusOneDay, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with empty description cannot be created
				"User1", "Test", "", currentDatePlusOneDay, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with null moment cannot be created
				"User1", "Test", "", null, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with past moment cannot be created
				"User1", "Test", "", currentDateMinusOneDay, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "123.12,123.12", false, false, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that a rendezvous with empty GPS Coordinates cannot be created
				"User1", "Test", "", currentDatePlusOneDay, "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg", "", false, false, javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6], (Boolean) testingData[i][7],
				(Class<?>) testingData[i][8]);
	}

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is authenticated as a user must be able to create a rendezvous, which he’s implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as they aren't saved them in final mode.
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

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is authenticated as a user must be able to create a rendezvous, which he’s implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as they aren't saved them in final mode.
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
	public void driverDeleteRendezvous() {
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can delete a draft mode rendezvous they created
				"User1", "Rendezvous8", null
			}, {
				// This test checks that authenticated admins can delete a final mode rendezvous
				"Admin1", "Rendezvous1", null
			}, {
				// This test checks that authenticated admins can delete a draft mode rendezvous
				"Admin1", "Rendezvous8", null
			}, {
				// This test checks that authenticated users cannot delete a draft mode rendezvous they did not create
				"User2", "Rendezvous8", java.lang.NullPointerException.class
			}, {
				// This test checks that authenticated users cannot delete a final mode rendezvous they did not create
				"User2", "Rendezvous1", java.lang.NullPointerException.class
			}, {
				// This test checks that unauthenticated actors cannot delete a draft mode rendezvous
				null, "Rendezvous8", java.lang.IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated actors cannot delete a final mode rendezvous
				null, "Rendezvous1", java.lang.IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot delete a draft mode rendezvous
				"Manager1", "Rendezvous8", java.lang.NullPointerException.class
			}, {
				// This test checks that authenticated actors that are not users cannot delete a final mode rendezvous
				"Manager1", "Rendezvous1", java.lang.NullPointerException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is authenticated as a user must be able to create a rendezvous, which he’s implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as they aren't saved them in final mode.
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
	public void driverRSVPRendezvous() {
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can RSVP a final mode rendezvous
				"User4", "Rendezvous1", null
			}, {
				// This test checks that admins cannot RSVP a rendezvous
				"Admin1", "Rendezvous2", java.lang.ClassCastException.class
			}, {
				// This test checks that managers cannot RSVP a rendezvous
				"Manager1", "Rendezvous2", java.lang.ClassCastException.class
			}, {
				// This test checks that unauthenticated users cannot RSVP a rendezvous
				null, "Rendezvous2", java.lang.IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRSVP((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * This driver checks several tests regarding functional requirement number 16.4: An actor who is authenticated as a user must be able to
	 * link one of the rendezvouses that he or she's created to other similar rendezvouses
	 * 
	 * @author Juanmi
	 */
	@Test
	public void driverSimilars() {
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can link a final mode rendezvous to a draft mode rendezvous they created
				"User1", "Rendezvous8", "Rendezvous1", "", "", null
			}, {
				// This test checks that authenticated users can link two final mode rendezvouses to a draft mode rendezvous they created
				"User1", "Rendezvous8", "Rendezvous1", "Rendezvous2", "", null
			}, {
				// This test checks that authenticated users can link three final mode rendezvouses to a draft mode rendezvous they created
				"User1", "Rendezvous8", "Rendezvous1", "Rendezvous2", "Rendezvous3", null
			}, {
				// This test checks that authenticated users can link a final mode rendezvous to a final mode rendezvous they created
				"User1", "Rendezvous1", "Rendezvous2", "", "", null
			}, {
				// This test checks that authenticated users can link two final mode rendezvouses to a final mode rendezvous they created
				"User1", "Rendezvous1", "Rendezvous2", "Rendezvous3", "", null
			}, {
				// This test checks that authenticated users can link three final mode rendezvouses to a final mode rendezvous they created
				"User1", "Rendezvous1", "Rendezvous2", "Rendezvous3", "Rendezvous4", null
			}, {
				// This test checks that authenticated users cannot link rendezvouses to a rendezvous they did not create
				"User2", "Rendezvous1", "Rendezvous2", "Rendezvous3", "Rendezvous4", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				// This test checks that unauthenticated users cannot modify similars of a draft mode rendezvous
				null, "Rendezvous8", "Rendezvous2", "Rendezvous3", "Rendezvous4", java.lang.IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated users cannot modify similars of a final mode rendezvous
				null, "Rendezvous1", "Rendezvous2", "Rendezvous3", "Rendezvous4", java.lang.IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSimilars((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * This test checks that functional requirement 5.5 works properly: An actor who is authenticated as a user must be able to list the rendezvouses that he
	 * or she's RSVPd
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testFindRSVPed() {

		int userId;
		Collection<Rendezvous> rsvpedRendezvouses;
		User user;

		userId = super.getEntityId("User1");
		user = this.userService.findOne(userId);

		super.authenticate("User1");
		rsvpedRendezvouses = this.rendezvousService.findRSVPRendezvouses(user);

		Assert.notNull(rsvpedRendezvouses);

		super.unauthenticate();
	}

	/**
	 * This test checks that when a rendezvous is created, findAll method returns one more rendezvous than before the creation
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testFindAllCreatingRendezvous() {
		final Rendezvous rendezvous;
		User user;
		Collection<Question> questions;
		Collection<Rendezvous> similars;
		Collection<Announcement> announcements;
		Collection<Comment> comments;
		Collection<User> users;
		Collection<Request> requests;
		Long oneDay;
		Collection<Rendezvous> allRendezvousesBeforeCreation, allRendezvousesAfterCreation;

		super.authenticate("User1");

		allRendezvousesBeforeCreation = this.rendezvousService.findAll();

		oneDay = TimeUnit.DAYS.toMillis(1);
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

		rendezvous.setName("Test");
		rendezvous.setDescription("Test");
		rendezvous.setMoment(new Date(System.currentTimeMillis() + oneDay));
		rendezvous.setPictureUrl("");
		rendezvous.setGpsCoordinates("123.12,123.12");
		rendezvous.setFinalMode(false);
		rendezvous.setAdultOnly(false);

		this.rendezvousService.save(rendezvous);
		this.rendezvousService.flush();

		allRendezvousesAfterCreation = this.rendezvousService.findAll();

		Assert.isTrue(allRendezvousesBeforeCreation.size() + 1 == allRendezvousesAfterCreation.size());

		super.unauthenticate();

	}

	/**
	 * This test checks that when a rendezvous is deleted by an admin, findAll method returns one less rendezvous than before the deletion
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testFindAllDeletingRendezvous() {
		int rendezvousId;
		Rendezvous rendezvous;
		final Collection<Rendezvous> allRendezvousesBeforeDeletion, allRendezvousesAfterDeletion;

		super.authenticate("Admin1");

		allRendezvousesBeforeDeletion = this.rendezvousService.findAll();

		rendezvousId = super.getEntityId("Rendezvous1");

		rendezvous = this.rendezvousService.findOne(rendezvousId);

		this.rendezvousService.delete(rendezvous);
		this.rendezvousService.flush();

		allRendezvousesAfterDeletion = this.rendezvousService.findAll();

		Assert.isTrue(allRendezvousesBeforeDeletion.size() - 1 == allRendezvousesAfterDeletion.size());

		super.unauthenticate();

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

	protected void templateDelete(final String username, final String rendezvousPopulateName, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		Rendezvous rendezvous;

		caught = null;

		try {
			super.authenticate(username);

			rendezvousId = super.getEntityId(rendezvousPopulateName);

			rendezvous = this.rendezvousService.findOne(rendezvousId);

			this.rendezvousService.delete(rendezvous);
			this.rendezvousService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void templateRSVP(final String username, final String rendezvousPopulateName, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId;
		Rendezvous rendezvous;

		caught = null;

		try {
			super.authenticate(username);

			rendezvousId = super.getEntityId(rendezvousPopulateName);

			rendezvous = this.rendezvousService.findOne(rendezvousId);

			this.rendezvousService.RSVP(rendezvous);
			this.rendezvousService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void templateSimilars(final String username, final String rendezvousPopulateName, final String similarOneName, final String similarTwoName, final String similarThreeName, final Class<?> expected) {
		Class<?> caught;
		int rendezvousId, similarOneId, similarTwoId, similarThreeId;
		Rendezvous rendezvous;
		final Rendezvous similarOne, similarTwo, similarThree;
		final Collection<Rendezvous> similars = new HashSet<Rendezvous>();

		caught = null;

		try {
			super.authenticate(username);

			rendezvousId = super.getEntityId(rendezvousPopulateName);
			if (similarOneName != "") {
				similarOneId = super.getEntityId(similarOneName);
				similarOne = this.rendezvousService.findOne(similarOneId);
				similars.add(similarOne);
			}

			if (similarTwoName != "") {
				similarTwoId = super.getEntityId(similarTwoName);
				similarTwo = this.rendezvousService.findOne(similarTwoId);
				similars.add(similarTwo);
			}

			if (similarThreeName != "") {
				similarThreeId = super.getEntityId(similarThreeName);
				similarThree = this.rendezvousService.findOne(similarThreeId);
				similars.add(similarThree);
			}

			rendezvous = this.rendezvousService.findOne(rendezvousId);

			rendezvous.setSimilars(similars);

			this.rendezvousService.save(rendezvous);
			this.rendezvousService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
