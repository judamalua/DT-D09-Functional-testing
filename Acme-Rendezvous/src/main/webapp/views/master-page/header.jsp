<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class = "crop">
	<a href="welcome/index.do"> <img class="banner img-responsive" src="https://i.ytimg.com/vi/BzX6jMalUck/maxresdefault.jpg"
		alt="Acme Co., Inc." />
	</a>
</div>

<div class="navbar">
	<a class="brand" href="#">&#160;&#160;Acme&#160;<img width = "24" src = "images/teamwork.png"/>&#160;Rendezvous</a>
	<div class="navbar-inner">
		<div class="container">
				

			<ul class="nav">
				<!-- id="jMenu" -->
				<!-- Do not forget the "fNiv" class for the first level links !! -->

				<security:authorize access="hasRole('USER')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><img src = "images/menu.png"/> <spring:message
								code="master.page.user" /><span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li class="arrow"></li>
							<li><a href="rendezvous/user/list.do"><spring:message
										code="master.page.createdRendezvous" /></a></li>

							<li><a href="announcement/user/list.do"><spring:message
										code="master.page.announcements" /></a></li>
							
						</ul></li>
				</security:authorize>

				<security:authorize access="hasRole('ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><img src = "images/menu.png"/> <spring:message
								code="master.page.admin" /><span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li class="arrow"></li>
							<li><a href="actor/admin/register.do"><spring:message
										code="master.page.createAdmin" /></a></li>
							<li><a href="dashboard/admin/list.do"><spring:message
										code="master.page.dashboardList" /></a></li>
							<li><a href="configuration/admin/list.do"><spring:message
										code="master.page.configuration" /></a></li>
						</ul>
						
								
						</li>
				</security:authorize>

				<security:authorize access="isAnonymous()">
					<li><a class="fNiv" href="security/login.do"><img src = "images/login.png"/> <spring:message
								code="master.page.login" /></a></li>
					<li><a class="fNiv" href="actor/register.do"><img src = "images/register.png"/> <spring:message
								code="master.page.registerUser" /></a></li>
					<li><a class="fNiv" href="rendezvous/list.do?anonymous=true"><img src = "images/rendezvouses.png"/> <spring:message
								code="master.page.rendevouzList" /></a></li>
					<li><a class="fNiv" href="user/list.do?anonymous=true"><img src = "images/users.png"/> <spring:message
								code="master.page.userList" /></a></li>
								</ul>
				</security:authorize>

				<security:authorize access="isAuthenticated()">

					<li><a class="fNiv" href="rendezvous/list.do?anonymous=false"><img src = "images/rendezvouses.png"/> <spring:message
								code="master.page.rendevouzList" /></a></li>
					<li><a class="fNiv" href="user/list.do?anonymous=false"><img src = "images/users.png"/> <spring:message
								code="master.page.userList" /></a></li>
					
						</ul>
						<ul class = "nav pull-right">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><img src = "images/settings.png"/> <security:authentication
								property="principal.username" /><span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li class="arrow"></li>
							<security:authorize access="hasRole('ADMIN')">
							<li><a href="actor/admin/edit.do"><spring:message
										code="master.page.actorEdit" /></a></li>
							<li><a href="actor/display.do"><spring:message
										code="master.page.actorProfile" /></a></li>
							</security:authorize>
							<security:authorize access="hasRole('USER')">
							<li><a href="actor/user/edit.do"><spring:message
										code="master.page.actorEdit" /></a></li>
							<li><a href="user/display.do?anonymous=false"><spring:message
										code="master.page.actorProfile" /></a></li>
							</security:authorize>
							<li><a href="j_spring_security_logout"><spring:message
										code="master.page.logout" /> </a></li>
						</ul></li>
						</ul>
				</security:authorize>
			
		</div>
	</div>
</div>

<div class = "lang">
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

