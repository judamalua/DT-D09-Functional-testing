
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DashboardTest extends AbstractTest {

	// The SUT --------------------------------------------------------------
	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private AnnouncementService		announcementService;

	@Autowired
	private AnswerService			answerService;

	@Autowired
	private CategoryService			categoryService;

	@Autowired
	private CommentService			commentService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private QuestionService			questionService;

	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private ServiceService			serviceService;

	@Autowired
	private UserService				userService;


	//******************************************Positive Methods*******************************************************************

	/**
	 * The average and the standard deviation of rendezvouses created per user
	 * 
	 * @author Luis
	 * */
	@Test
	public void driverDashboardTest() {

		//The average and the standard deviation of rendezvouses created per user
		//this.checkResults("2.3333,1.8856180835766057", this.rendezvousService.getUsersInfoFromRendezvous());

	}
	/**
	 * The ratio of users who have ever created a rendezvous versus the users who have never created any rendezvouses
	 * */

	/**
	 * The average and the standard deviation of users per rendezvous.
	 * */

	/**
	 * The average and the standard deviation of rendezvouses that are RSVPd per user.
	 * */

	/**
	 * 
	 * The top-10 rendezvouses in terms of users who have RSVPd them.
	 * */

	/**
	 * 
	 * The average and the standard deviation of rendezvouses created per user
	 * */

	/**
	 * The average and the standard deviation of rendezvouses created per user
	 * */

	/**
	 * The average and the standard deviation of rendezvouses created per user
	 * */

	//Private Methods
	private void checkResults(final Double expected, final Double result) {

		Assert.isTrue(expected == result);
	}

	private void checkResults(final String expected, final String result) {

		Assert.isTrue(expected.equals(result));
	}

}
