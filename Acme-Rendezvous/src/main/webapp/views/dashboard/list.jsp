<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<ul class="collapsible popout" data-collapsible="accordion">

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.rendezvous.user"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${rendezvousUserAverage == \"null\" ? 0 : rendezvousUserAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${rendezvousUserStandardDeviation == \"null\" ? 0 : rendezvousUserStandardDeviation}"></jstl:out></p>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.users.create.ratio"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioCreatedRendezvouses == \"null\" ? 0 : ratioCreatedRendezvouses}"></jstl:out></p>
<div class = "ratio element">
<div class="progress progress-striped
     active" aria-valuemin="0">
  <div class="bar"
       style="width: ${ratioCreatedRendezvouses*100}%;"><jstl:out value="${ratioCreatedRendezvouses*100}%" /></div>
</div>
</div>
</span></div>
</li>



<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.user.rendezvous.info"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${userRendezvousAverage == \"null\" ? 0 : userRendezvousAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${userRendezvousStandardDeviation == \"null\" ? 0 : userRendezvousStandardDeviation}"></jstl:out></p>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.RSVP.user"/></div>
<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageRSVPedPerUser == \"null\" ? 0 : averageRSVPedPerUser}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${standardDeviationRSVPedPerUser == \"null\" ? 0 : standardDeviationRSVPedPerUser}"></jstl:out></p>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.top.ten.rendezvouses"/></div>
	
<div class="collapsible-body"><span>	
	<display:table id = "rendezvous" name = "topTenRendezvouses" requestURI="dashboard/admin/list.do" pagesize="${pagesize}">
		<spring:message var = "titleRendezvous" code = "dashboard.rendezvous.title"/>
		<display:column title = "${titleRendezvous}">${rendezvous.name}</display:column>
		<spring:message var = "titleNumRSVP" code = "dashboard.rendezvous.numRSVP"/>
		<display:column title = "${titleNumRSVP}">${fn:length(rendezvous.users)}</display:column>
	
	</display:table>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.announcements.rendezvous"/></div>
<div class="collapsible-body"><span>	
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${announcementsRendezvousAverage == \"null\" ? 0 : announcementsRendezvousAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${announcementsRendezvousStandardDeviation == \"null\" ? 0 : announcementsRendezvousStandardDeviation}"></jstl:out></p>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.rendezvouses.above.seventyfive"/></div>
	<div class="collapsible-body"><span>
	<display:table id = "row" name = "rendezvousesWithAnnouncementAboveSeventyFivePercent" requestURI="dashboard/admin/list.do" pagesize="${pagesize}">
		
		<spring:message var = "titleRendezvous" code = "dashboard.rendezvous.title"/>
		<display:column title = "${titleRendezvous}">${row.name}</display:column>
	
	</display:table>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.rendezvous.most.linked"/></div>
	
	<div class="collapsible-body"><span>
	<display:table id = "row" name = "rendezvousesMostLinked" requestURI="dashboard/admin/list.do" pagesize="${pagesize}">
		
		<spring:message var = "titleRendezvous" code = "dashboard.rendezvous.title"/>
		<display:column title = "${titleRendezvous}">${row.name}</display:column>
	
	</display:table>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.questions.rendezvous"/></div>
<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${questionsRendezvousAverage == \"null\" ? 0 : questionsRendezvousAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${questionsRendezvousStandardDeviation == \"null\" ? 0 : questionsRendezvousStandardDeviation}"></jstl:out></p>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.answers.rendezvous"/></div>
<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageAnswersPerRendezvous == \"null\" ? 0 : averageAnswersPerRendezvous}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${standardDeviationAnswersPerRendezvous == \"null\" ? 0 : standardDeviationAnswersPerRendezvous}"></jstl:out></p>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.replies.comment"/></div>
<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${repliesCommentAverage == \"null\" ? 0 : repliesCommentAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${repliesCommentStandardDeviation == \"null\" ? 0 : repliesCommentStandardDeviation}"></jstl:out></p>
</span></div>
</li>

</ul>
