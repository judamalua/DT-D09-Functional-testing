
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Announcement;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private AnnouncementService		announcementService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Tests ------------------------------------------------------------------
	/**
	 * This driver checks that announcement can be added and findAll return the new value also.
	 * 
	 * 
	 * @author Alejandro
	 */
	@Test
	public void testCaseList(){
		int prevSize = this.announcementService.findAll().size();
		// Create a new announcement with creation template
		this.templateCreate("User1", super.getEntityId("Rendezvous4"), new Date(System.currentTimeMillis()-1), "Test Title", "Test Description", null);
		Assert.isTrue(this.announcementService.findAll().size()-prevSize == 1);
	}
	
	@Test
	public void driverDeleteAnnouncement(){
		
		//TODO: Preguntar sobre la modificacion de announcement create/delete
		final Object testingData[][] = {
				{null, "Announcement1", IllegalArgumentException.class},
				{"User2", "Announcement1", IllegalArgumentException.class},
				{"User1","Announcement1", null},
				{"Admin1", "Announcement2", null},
				
				{null, "Announcement4", IllegalArgumentException.class},
				{"User2", "Announcement4", IllegalArgumentException.class},
				{"User1","Announcement4", null},
				{"Admin1", "Announcement5", null},
		};
		for (int i = 0; i < testingData.length; i++){
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
		}
	}
	/**
	 * This driver checks several tests regarding functional requirement number 21.1: An actor who is authenticated as a user must be able to manage
	 * (add, edit, delete) the announcements that are associated with a rendezvous on draft mode that he or she has created previously, tests are explained inside
	 * 
	 * @author Alejandro
	 */
	@Test
	public void driverCreateAnnouncement() {
		Date currentDate = new Date(System.currentTimeMillis()-1); // Current date 
		// Functional requirement number 16.3: An actor who is authenticated as a user must be able to: Create an announcement regarding
		// one of the rendezvouses that he or she’s creat-ed previously.
		final Object testingData[][] = {
			{
				// This test checks that authenticated users cannot create announcement to a rendezvous already finished.
				"User1", "Rendezvous1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			},
			{
			// This test checks that authenticated users can add an announcement to a Rendezvous that they have created in final mode
				"User1", "Rendezvous4", currentDate, "Test Title", "Test Description", null
			},
			{
				// This test checks that unauthenticated users cannot create announcement to a rendezvous already finished
				null, "Rendezvous1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated users cannot create questions to a rendezvous not finished
				null, "Rendezvous4", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create announcement to a rendezvous in draft mode
				"Admin1", "Rendezvous8", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create announcement to a rendezvous in final mode
				"Admin1", "Rendezvous1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create announcement to a rendezvous in final mode
				"Admin1", "Rendezvous4", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create announcement to a draft mode rendezvous they did not create
				"User2", "Rendezvous8", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create announcement to a final mode rendezvous they did not create
				"User2", "Rendezvous1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			},
			{
				// This test checks that authenticated users cannot create announcement to a final mode rendezvous they did not create
				"User2", "Rendezvous4", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			},{
				// This test checks that announcement with empty texts cannot be saved
				"User1", "Rendezvous8", currentDate, "", "", javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that announcement with empty texts cannot be saved
				"User1", "Rendezvous8", currentDate, "Test Title", "", javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that announcement with empty texts cannot be saved
				"User1", "Rendezvous8", currentDate, "", "Test Description", javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create announcement with empty texts to a rendezvous
				"Admin1", "Rendezvous1", currentDate, "", "Test Description", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++){
			this.templateCreate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Date) testingData[i][2],(String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
		}
		}

	// Ancillary methods ------------------------------------------------------

	protected void templateCreate(final String username, final int rendezvousId, final Date moment, final String title, final String description, final Class<?> expected) {
		Class<?> caught;
		Announcement announcement;
		Rendezvous rendezvous;
		
		caught = null;

		try {
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			super.authenticate(username);

			announcement = this.announcementService.create();
			announcement.setMoment(moment);
			announcement.setTitle(title);
			announcement.setDescription(description);

			this.announcementService.save(announcement, rendezvous.getId());
			this.announcementService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	
	protected void templateDelete(final String username, final int announcementId, final Class<?> expected) {
		Class<?> caught;
		Announcement announcement;
		caught = null;

		try {
			announcement = this.announcementService.findOne(announcementId);
			super.authenticate(username);

			this.announcementService.delete(announcement);
			this.announcementService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}


}
