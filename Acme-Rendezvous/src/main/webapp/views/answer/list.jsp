<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<h2><spring:message code="answer.list.creator"/></h2>
<h3><a href="user/display.do?actorId=${creator.id}&anonymous=${anonymous}">${creator.surname}, ${creator.name}</a></h3>

<h2><spring:message code="answer.list.users"/></h2>
<jstl:forEach var="userAndAnswers" items="${usersAndAnswers}">
	<details>
  		<summary><a href="user/display.do?actorId=${userAndAnswers.key.id}&anonymous=${anonymous}">${userAndAnswers.key.surname}, ${userAndAnswers.key.name}</a></summary>
  		<jstl:forEach var="answer" items="${userAndAnswers.value}">
  			<h4>${answer.question.text}</h4>
  			<p>${answer.text}</p>
  		</jstl:forEach>
	</details>
</jstl:forEach>