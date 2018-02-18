<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<details>
<summary><spring:message code="dashboard.rendezvous.user"/></summary><br/>

<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${rendezvousUserAverage == \"null\" ? 0 : rendezvousUserAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${rendezvousUserStandardDeviation == \"null\" ? 0 : rendezvousUserStandardDeviation}"></jstl:out></p>
</details><br/>

<details>
<summary><spring:message code="dashboard.users.create.ratio"/></summary><br/>

<p class = "element"><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioCreatedRendezvouses == \"null\" ? 0 : ratioCreatedRendezvouses}"></jstl:out></p>
<div class = "ratio element">
<div class="progress progress-striped
     active" aria-valuemin="0">
  <div class="bar"
       style="width: ${ratioCreatedRendezvouses*100}%;"><jstl:out value="${ratioCreatedRendezvouses*100}%" /></div>
</div>
</div>
</details><br/>


<details>
<summary><spring:message code="dashboard.user.rendezvous.info"/></summary><br/>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${userRendezvousAverage == \"null\" ? 0 : userRendezvousAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${userRendezvousStandardDeviation == \"null\" ? 0 : userRendezvousStandardDeviation}"></jstl:out></p>
</details><br/>

<details>
<summary><spring:message code="dashboard.RSVP.user"/></summary><br/>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageRSVPedPerUser == \"null\" ? 0 : averageRSVPedPerUser}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${standardDeviationRSVPedPerUser == \"null\" ? 0 : standardDeviationRSVPedPerUser}"></jstl:out></p>
</details><br/>

<details>
<summary><spring:message code="dashboard.top.ten.rendezvouses"/></summary>
	
	<display:table id = "row" name = "topTenRendezvouses">
		
		<spring:message var = "titleRendezvous" code = "dashboard.rendezvous.title"/>
		<display:column title = "${titleRendezvous}">${row.name}</display:column>
	
	</display:table>
</details><br/>

<details>
<summary><spring:message code="dashboard.announcements.rendezvous"/></summary><br/>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${announcementsRendezvousAverage == \"null\" ? 0 : announcementsRendezvousAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${announcementsRendezvousStandardDeviation == \"null\" ? 0 : announcementsRendezvousStandardDeviation}"></jstl:out></p>
</details><br/>

<details>
<summary><spring:message code="dashboard.rendezvouses.above.seventyfive"/></summary>
	
	<display:table id = "row" name = "rendezvousesWithAnnouncementAboveSeventyFivePercent">
		
		<spring:message var = "titleRendezvous" code = "dashboard.rendezvous.title"/>
		<display:column title = "${titleRendezvous}">${row.name}</display:column>
	
	</display:table>
</details><br/>

<details>
<summary><spring:message code="dashboard.rendezvous.most.linked"/></summary>
	
	<display:table id = "row" name = "rendezvousesMostLinked">
		
		<spring:message var = "titleRendezvous" code = "dashboard.rendezvous.title"/>
		<display:column title = "${titleRendezvous}">${row.name}</display:column>
	
	</display:table>
</details><br/>

<details>
<summary><spring:message code="dashboard.questions.rendezvous"/></summary><br/>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${questionsRendezvousAverage == \"null\" ? 0 : questionsRendezvousAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${questionsRendezvousStandardDeviation == \"null\" ? 0 : questionsRendezvousStandardDeviation}"></jstl:out></p>
</details><br/>

<details>
<summary><spring:message code="dashboard.answers.rendezvous"/></summary><br/>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageAnswersPerRendezvous == \"null\" ? 0 : averageAnswersPerRendezvous}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${standardDeviationAnswersPerRendezvous == \"null\" ? 0 : standardDeviationAnswersPerRendezvous}"></jstl:out></p>
</details><br/>

<details>
<summary><spring:message code="dashboard.replies.comment"/></summary><br/>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${repliesCommentAverage == \"null\" ? 0 : repliesCommentAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${repliesCommentStandardDeviation == \"null\" ? 0 : repliesCommentStandardDeviation}"></jstl:out></p>
</details><br/>
