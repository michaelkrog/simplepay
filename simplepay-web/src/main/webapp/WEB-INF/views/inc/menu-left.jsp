<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="widget">
    <div class="title"><h3>Menu</h3></div>
    <ul class="links-list-alt">
        <li <c:if test="${subarea=='dashboard'}">class="active"</c:if>><a href="<c:url value='/manage/dashboard'/>">Dashboard</a></li>
        <li <c:if test="${subarea=='payments'}">class="active"</c:if>><a href="<c:url value='/manage/transactions'/>">Payments</a></li>
        <li <c:if test="${subarea=='events'}">class="active"</c:if>><a href="<c:url value='/manage/events'/>">Events</a></li>
    </ul>
</div>