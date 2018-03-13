
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.CommentService;
import services.ManagerService;
import services.QuestionService;
import services.RendezvousService;
import services.ServiceService;
import services.UserService;
import controllers.AbstractController;
import domain.DomainService;
import domain.Manager;
import domain.Rendezvous;

@Controller
@RequestMapping("/dashboard/admin")
public class DashboardAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	RendezvousService	rendezvousService;

	@Autowired
	QuestionService		questionService;

	@Autowired
	CommentService		commentService;

	@Autowired
	ServiceService		serviceService;

	@Autowired
	ManagerService		managerService;

	@Autowired
	CategoryService		categoryService;

	@Autowired
	UserService			userService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		final ModelAndView result;
		final String rendezvousesInfoFromUsers, ratioCreatedRendezvouses, usersInfoFromRendezvous, announcementsInfoFromRendezvous, questionsInfoFromRendezvous, repliesInfoFromComment, RSVPedInfoFromRendezvous, answersInfoFromQuestion, numberCategoriesPerRendezvousAverage, averageRatioServicesCategories, requestedServicesPerRendezvousInfo;
		final Collection<Rendezvous> topTenRendezvouses, rendezvousesWithAnnouncementAboveSeventyFivePercent, rendezvousesMostLinked;
		final Collection<DomainService> bestSellingServices;
		final Collection<Manager> bestProviderManagers, moreCancelledManagers;

		rendezvousesInfoFromUsers = this.userService.getRendezvousesInfoFromUsers();
		ratioCreatedRendezvouses = this.userService.getRatioCreatedRendezvouses();

		usersInfoFromRendezvous = this.rendezvousService.getUsersInfoFromRendezvous();
		RSVPedInfoFromRendezvous = this.userService.getRSVPedInfoFromRendezvous();
		topTenRendezvouses = this.rendezvousService.getTopTenRendezvouses();

		announcementsInfoFromRendezvous = this.rendezvousService.getAnnouncementsInfoFromRendezvous();
		rendezvousesWithAnnouncementAboveSeventyFivePercent = this.rendezvousService.getRendezvousesWithAnnouncementAboveSeventyFivePercent();
		rendezvousesMostLinked = this.rendezvousService.getRendezvousesMostLinked();

		questionsInfoFromRendezvous = this.rendezvousService.getQuestionsInfoFromRendezvous();
		answersInfoFromQuestion = this.questionService.getAnswersInfoFromQuestion();

		repliesInfoFromComment = this.commentService.getRepliesInfoFromComment();

		//------
		bestSellingServices = this.serviceService.findBestSellingServices();

		bestProviderManagers = this.managerService.findBestProviderThanAverageManager();
		moreCancelledManagers = this.managerService.findMoreCancelledServicesManager();

		numberCategoriesPerRendezvousAverage = this.rendezvousService.getAverageNumberCategoriesPerRendezvous();
		averageRatioServicesCategories = this.categoryService.getAverageRatioServicesInCategory();
		requestedServicesPerRendezvousInfo = this.rendezvousService.getInfoFromServicesRequestedPerRendezvous();

		result = new ModelAndView("dashboard/list");
		result.addObject("rendezvousUserAverage", rendezvousesInfoFromUsers.split(",")[0]);
		result.addObject("rendezvousUserStandardDeviation", rendezvousesInfoFromUsers.split(",")[1]);

		result.addObject("ratioCreatedRendezvouses", ratioCreatedRendezvouses);

		result.addObject("userRendezvousAverage", usersInfoFromRendezvous.split(",")[0]);
		result.addObject("userRendezvousStandardDeviation", usersInfoFromRendezvous.split(",")[1]);

		result.addObject("averageRSVPedPerUser", RSVPedInfoFromRendezvous.split(",")[0]);
		result.addObject("standardDeviationRSVPedPerUser", RSVPedInfoFromRendezvous.split(",")[1]);

		result.addObject("topTenRendezvouses", topTenRendezvouses);

		result.addObject("announcementsRendezvousAverage", announcementsInfoFromRendezvous.split(",")[0]);
		result.addObject("announcementsRendezvousStandardDeviation", announcementsInfoFromRendezvous.split(",")[1]);

		result.addObject("rendezvousesWithAnnouncementAboveSeventyFivePercent", rendezvousesWithAnnouncementAboveSeventyFivePercent);
		result.addObject("rendezvousesMostLinked", rendezvousesMostLinked);

		result.addObject("questionsRendezvousAverage", questionsInfoFromRendezvous.split(",")[0]);
		result.addObject("questionsRendezvousStandardDeviation", questionsInfoFromRendezvous.split(",")[1]);

		result.addObject("averageAnswersPerRendezvous", answersInfoFromQuestion.split(",")[0]);
		result.addObject("standardDeviationAnswersPerRendezvous", answersInfoFromQuestion.split(",")[1]);

		result.addObject("repliesCommentAverage", repliesInfoFromComment.split(",")[0]);
		result.addObject("repliesCommentStandardDeviation", repliesInfoFromComment.split(",")[1]);

		result.addObject("bestSellingServices", bestSellingServices);
		result.addObject("bestProviderManagers", bestProviderManagers);
		result.addObject("moreCancelledManagers", moreCancelledManagers);
		result.addObject("numberCategoriesPerRendezvousAverage", numberCategoriesPerRendezvousAverage);
		result.addObject("averageRatioServicesCategories", averageRatioServicesCategories);
		result.addObject("requestedServicesPerRendezvousInfo", requestedServicesPerRendezvousInfo.split(","));

		return result;
	}
}
