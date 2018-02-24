<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- <div class = "crop">
	<a href="welcome/index.do"> <img class="banner img-responsive" src="https://i.ytimg.com/vi/BzX6jMalUck/maxresdefault.jpg"
		alt="Acme Co., Inc." />
	</a>
</div> -->

<nav>
    <div class="nav-wrapper">
        <a class="brand-logo" href="#">&#160;&#160;Acme&#160;<img
            width="24" src="images/people.png" />&#160;Rendezvous
        </a>


        <ul id="nav-mobile" class="right hide-on-med-and-down">
            <!-- id="jMenu" -->
            <!-- Do not forget the "fNiv" class for the first level links !! -->

            <security:authorize access="hasRole('USER')">

                <!-- Dropdown Structure -->
                <ul id="dropdownUserFunctions" class="dropdown-content">
                    <li><a href="rendezvous/user/list.do"><spring:message
                                code="master.page.createdRendezvous" /></a></li>
                    <li class="divider"></li>
                    <li><a href="announcement/user/list-created.do"><spring:message
                                code="master.page.announcementsCreated" /></a></li>
                </ul>

                <!-- Dropdown Trigger -->
                <li><a class="dropdown-button" href="#!"
                    data-activates="dropdownUserFunctions"><spring:message
                            code="master.page.user" /><i class="material-icons right">arrow_drop_down</i></a></li>
                            
                <li><a href="announcement/user/list.do"><spring:message
                                code="master.page.announcementsList" /></a></li>

            </security:authorize>

            <security:authorize access="hasRole('ADMIN')">
                <!-- Dropdown Structure -->
                <ul id="dropdownAdminFunctions" class="dropdown-content">
                    <li><a href="actor/admin/register.do"><spring:message
                                code="master.page.createAdmin" /></a></li>
                    <li class="divider"></li>
                    <li><a href="dashboard/admin/list.do"><spring:message
                                code="master.page.dashboardList" /></a></li>
                    <li class="divider"></li>
                    <li><a href="configuration/admin/list.do"><spring:message
                                code="master.page.configuration" /></a></li>
                </ul>

                <!-- Dropdown Trigger -->
                <li><a class="dropdown-button" href="#!"
                    data-activates="dropdownAdminFunctions"><spring:message
                            code="master.page.admin" /><i class="material-icons right">arrow_drop_down</i></a></li>
            </security:authorize>
            <security:authorize access="isAnonymous()">
                <li><a class="fNiv" href="security/login.do"> <spring:message
                            code="master.page.login" /></a></li>
                <li><a class="fNiv" href="actor/register.do"> <spring:message
                            code="master.page.registerUser" /></a></li>
                <li><a class="fNiv" href="rendezvous/list.do?anonymous=true">
                        <spring:message code="master.page.rendevouzList" />
                </a></li>
                <li><a class="fNiv" href="user/list.do?anonymous=true"> <spring:message
                            code="master.page.userList" /></a></li>
        </ul>
        </security:authorize>

        <security:authorize access="isAuthenticated()">
            <li><a class="fNiv" href="rendezvous/list.do?anonymous=false">
                    <spring:message code="master.page.rendevouzList" />
            </a></li>
            <li><a class="fNiv" href="user/list.do?anonymous=false"><spring:message
                        code="master.page.userList" /></a></li>


            <security:authorize access="hasRole('ADMIN')">
                <!-- Dropdown Structure -->
                <ul id="dropdownAdminProfile" class="dropdown-content">
                    <li><a href="actor/admin/edit.do"><spring:message
                                code="master.page.actorEdit" /></a></li>
                    <li class="divider"></li>
                    <li><a href="actor/display.do"><spring:message
                                code="master.page.actorProfile" /></a></li>
                    <li class="divider"></li>
                    <li><a href="j_spring_security_logout"><spring:message
                                code="master.page.logout" /> </a></li>
                </ul>

                <!-- Dropdown Trigger -->
                <li><a class="dropdown-button" href="#!"
                    data-activates="dropdownAdminProfile"><security:authentication
                            property="principal.username" /><i class="material-icons right">arrow_drop_down</i></a></li>

            </security:authorize>

            <security:authorize access="hasRole('USER')">


                <!-- Dropdown Structure -->
                <ul id="dropdownUserProfile" class="dropdown-content">
                    <li><a href="actor/user/edit.do"><spring:message
                                code="master.page.actorEdit" /></a></li>
                    <li class="divider"></li>
                    <li><a href="user/display.do?anonymous=false"><spring:message
                                code="master.page.actorProfile" /></a></li>
                    <li class="divider"></li>
                    <li><a href="j_spring_security_logout"><spring:message
                                code="master.page.logout" /> </a></li>
                </ul>

                <!-- Dropdown Trigger -->
                <li><a class="dropdown-button" href="#!"
                    data-activates="dropdownUserProfile"><security:authentication
                            property="principal.username" /><i class="material-icons right">arrow_drop_down</i></a></li>

            </security:authorize>


        </security:authorize>
    </div>
</nav>

<p></p>


