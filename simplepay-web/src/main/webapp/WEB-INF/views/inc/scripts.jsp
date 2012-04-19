<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="scripts" scope='request'>jquery-js,prettify-js,bootstrap-js,bootstrap-modal-js,bootstrap-datepicker-js,application-js</c:set>
<c:forTokens var="script" items="${scripts}" delims=",">
    <c:set var="jsUrl"><spring:theme code='${script}'/></c:set>
    <script src="<c:url value='${jsUrl}'/>"></script>
</c:forTokens>