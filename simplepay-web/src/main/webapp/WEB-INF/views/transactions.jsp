<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>SimplePay</title>
        <%@include file="inc/head.jsp" %>
    </head>
    <body>

        <!--start: Header -->
        <header>

            <!--start: Container -->
            <div class="container">
                <%@include file="inc/navigation.jsp" %>
            </div>
            <!--end: Container-->			

        </header>
        <!--end: Header-->

        <!-- start: Page Title -->
        <div id="page-title">

            <div id="page-title-inner">

			<!-- start: Container -->
			<div class="container">

				<h2><i class="ico-italic ico-white"></i>Transactions</h2>

			</div>
			<!-- end: Container  -->

		</div>	

        </div>
        <!-- end: Page Title -->

        <!--start: Wrapper-->
        <div id="wrapper">

            <!--start: Container -->
            <div class="container">

                <c:forEach var="e" items="${entities}">
                    ${e.id}
                </c:forEach>

            </div>
            <!--end: Container-->

        </div>
        <!-- end: Wrapper  -->			

        <%@include file="inc/footer_menu.jsp" %>
        <%@include file="inc/footer.jsp" %>

        <%@include file="inc/post_body.jsp" %>

    </body>
</html>
<!-- Localized -->