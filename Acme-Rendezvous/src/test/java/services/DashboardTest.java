
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

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private UserService			userService;


	//******************************************Methods*******************************************************************

	/**
	 * The average and the standard deviation of rendezvouses created per user
	 * 
	 * @author Luis
	 * */
	@Test
	public void infoRendezvousesCreatedPerUserDashboardTest() {

		this.checkResults("2.4444,1.8324913896653188", this.rendezvousService.getUsersInfoFromRendezvous());

	}
	/**
	 * The ratio of users who have ever created a rendezvous versus the users who have never created any rendezvouses
	 * */
	@Test
	public void ratioUserCreatedRendezvousDashboardTest() {

		this.checkResults("0.125", this.userService.getRatioCreatedRendezvouses());

	}

	/**
	 * The average and the standard deviation of users per rendezvous.
	 * */

	@Test
	public void infoUsersPerRendezvousDashboardTest() {

		this.checkResults("2.4444,1.8324913896653188", this.rendezvousService.getUsersInfoFromRendezvous());

	}

	/**
	 * The average and the standard deviation of rendezvouses that are RSVPd per user.
	 * */
	@Test
	public void infoUsersRsvpRendezvousDashboardTest() {

		this.checkResults("2.75,2.817356917396161", this.userService.getRSVPedInfoFromRendezvous());

	}

	/**
	 * 
	 * The top-10 rendezvouses in terms of users who have RSVPd them.
	 * 
	 */

	@Test
	public void rendezvousesCreatedPerUserDashboardTest() {

		Assert.isTrue(this.rendezvousService.getTopTenRendezvouses().size() == 9);

	}
	/**
	 * 
	 * The average and the standard deviation of announcement per rendezvous
	 * */

	@Test
	public void infoAnnouncementsCreatedPerRendezvousDashboardTest() {

		this.checkResults("1.0,2.211083193369259", this.rendezvousService.getAnnouncementsInfoFromRendezvous());

	}

	/**
	 * Rendezvouses above 75% average number of announcement
	 * 
	 */

	@Test
	public void rendezvousesAbove75PercentAnnouncementDashboardTest() {

		Assert.isTrue(this.rendezvousService.getRendezvousesWithAnnouncementAboveSeventyFivePercent().size() == 2);

	}

	/**
	 * The rendezvouses that are linked to a number of rendezvouses that is greater than the average plus 10%.
	 * 
	 */

	@Test
	public void rendezvousesLinkedMore10PercentThanAverageDashboardTest() {

		Assert.isTrue(this.rendezvousService.getRendezvousesMostLinked().size() == 2);

	}

	/**
	 * The best-selling services.
	 * 
	 */

	@Test
	public void bestSellingServicesDashboardTest() {

		Assert.isTrue(this.serviceService.findBestSellingServices().size() == 2);

	}

	/**
	 * The managers who provide more services than the average.
	 * 
	 */

	@Test
	public void bestManagerProvidersDashboardTest() {

		Assert.isTrue(this.managerService.findBestProviderThanAverageManager().size() == 2);

	}

	/**
	 * The managers who have got more services cancelled.
	 * 
	 */

	@Test
	public void managersWithMoreCancelledServicesDashboardTest() {

		Assert.isTrue(this.managerService.findMoreCancelledServicesManager().size() == 1);

	}

	/**
	 * The average number of categories per rendezvous.
	 * 
	 */

	@Test
	public void categoriesAveragePerRendezvousDashboardTest() {

		this.checkResults("2.5", this.rendezvousService.getAverageNumberCategoriesPerRendezvous());

	}

	/**
	 * The average ratio of services in each category.
	 * 
	 */

	@Test
	public void averageRatioServicesInEachCategoryDashboardTest() {

		this.checkResults("0.28571429", this.categoryService.getAverageRatioServicesInCategory());

	}

	/**
	 * The average, the minimum, the maximum, and the standard deviation of services requested per rendezvous.
	 * 
	 */

	@Test
	public void infoServicesRequestedDashboardTest() {

		this.checkResults("0.2222,0,2,0.6285393607797229", this.rendezvousService.getInfoFromServicesRequestedPerRendezvous());

	}

	private void checkResults(final String expected, final String result) {

		Assert.isTrue(expected.equals(result));
	}

}
