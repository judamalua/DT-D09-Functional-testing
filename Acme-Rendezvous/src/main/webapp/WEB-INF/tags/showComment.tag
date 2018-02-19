<%@ tag language="java" body-content="empty"%>
<%-- Taglibs --%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%-- Attributes --%>
<%@ attribute name="comment" required="true" type="domain.Comment"%>
<%@ attribute name="canUserComment" required="true"%>
<%@ attribute name="indent" required="true"%>
<%-- Definition --%>
<div style="border-left: 1px solid black; border-top: 1px solid black; text-indent: ${indent}px; float:clear;">
<div >
<p hidden="true" class="commentId">${comment.id}</p>
<h4>${comment.text}</h4>
<p>${comment.moment}
<a id="commentUser${comment.id}id" href="#"><i id="commentUser${comment.id}name"></i> <i id="commentUser${comment.id}surname"></i></a>
</p>
<img src="${comment.pictureUrl}"/>
</div>
<div>
<jstl:if test="${canUserComment}">
<a href="comment/user/reply.do?commentId=${comment.id}">
	<button class="btn">
	<spring:message code="rendezvous.comment.reply"/>
	</button>
</a>
</jstl:if>
<security:authorize access="hasRole('ADMIN')">
			<a href="comment/admin/delete.do?commentId=${comment.id}">
				<button class="btn">
					<spring:message code="rendezvous.delete"/>
				</button>
			</a>
	</security:authorize>
</div>
<jstl:if test="${comment.comments != null and fn:length(comment.comments)>0}">
<details>
<summary><spring:message code="rendezvous.comment.replies"/></summary>
	<jstl:forEach var="replyComment" items="${comment.comments}">
		<acme:showComment comment="${replyComment}" canUserComment="${canUserComment}" indent="${indent+30}"/>
	</jstl:forEach>
</details>
</jstl:if>
</div>

