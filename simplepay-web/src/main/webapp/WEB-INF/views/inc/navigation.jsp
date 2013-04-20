<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!--start: Navbar -->
<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <a class="brand" href="<c:url value="/"/>"><i class="ico-cloud circle"></i>Simple<span>Pay</span>.</a>
        <div class="nav-collapse collapse">
            <ul class="nav">
                <sec:authorize ifNotGranted="ROLE_MERCHANT">
                <li><a href="<c:url value="/signup"/>"><spring:message code="navigation.sign_up"/></a></li>
                </sec:authorize>
                <sec:authorize ifAnyGranted="ROLE_MERCHANT">
                <li <c:if test="${area=='dashboard'}">class="active"</c:if>><a href="<c:url value="/manage/transactions"/>"><spring:message code="navigation.dashboard"/></a></li>
                </sec:authorize>
                <li <c:if test="${area=='documentation'}">class="active"</c:if>><a href="<c:url value='/docs'/>"><spring:message code="navigation.documentation"/></a></li>
                <li <c:if test="${area=='support'}">class="active"</c:if>><a href="<c:url value='/support'/>"><spring:message code="navigation.help"/></a></li>
                <li class="divider-vertical"></li>
                <sec:authorize ifNotGranted="ROLE_MERCHANT">
                <li><a href="<c:url value="/manage/transactions"/>"><spring:message code="navigation.log_in"/></a></li>
                </sec:authorize>
                <sec:authorize ifAnyGranted="ROLE_MERCHANT">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><sec:authentication property="principal.username" />&nbsp;<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value="/account"/>"><spring:message code="navigation.my_account"/></a></li>
                        <li><a href="<c:url value="/logout"/>"><spring:message code="navigation.log_out"/></a></li>
                    </ul>
                </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</div>
<!--end: Navbar -->
