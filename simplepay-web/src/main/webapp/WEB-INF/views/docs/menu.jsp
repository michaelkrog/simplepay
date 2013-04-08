<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="widget">
    <c:forEach var="section" items="${menusections}">
        <div class="title"><h3>${section.name}</h3></div>
        <ul class="links-list-alt">
            <c:forEach var="page" items="${section.pages}">
                <c:set var="docpath" value="${section.name}/${page}"/>
                <li <c:if test="${docfile == docpath}">class="active"</c:if>><a href="<c:url value="/docs/${docpath}.html"/>">${page}</a></li>
            </c:forEach>
        </ul>
        <hr/>
    </c:forEach>
    <!--div class="title"><h3>Documentation</h3></div>
    <ul class="links-list-alt">
        <li><a href="<c:url value="/docs"/>">Getting Started</a></li>
        <li><a href="<c:url value="/docs/charging_cards.html"/>">Charging Cards</a></li>
    </ul>
    <hr/>
    <div class="title"><h3>References</h3></div>
    <ul class="links-list-alt">
        <li><a href="<c:url value="/docs/charging_cards.html"/>">Full API reference</a></li>
    </ul-->
</div>