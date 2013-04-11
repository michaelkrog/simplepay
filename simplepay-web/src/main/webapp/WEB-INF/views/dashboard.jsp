<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="area" value="dashboard"/>
<c:set var="subarea" value="dashboard"/>
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

                    <h2><i class="ico-credit-card ico-white"></i>Dashboard</h2>

                </div>
                <!-- end: Container  -->

            </div>	

        </div>
        <!-- end: Page Title -->

        <!--start: Wrapper-->
        <div id="wrapper">

            <!--start: Container -->
            <div class="container">
                <div class="row">
                    <div class="span3">
                        <div data-spy="affix" data-offset-top="0">
                            <%@include file="inc/menu-left.jsp" %>
                        </div>
                    </div>
                    <span class="span9">
                        <!-- start: Row -->
			<div class="row">
				
				<div class="span9">
				
					<h3><i class="fa-icon-bar-chart"></i> Gross Volume</h3>
					<div id="chart-grossvolume" class="center" style="height:250px"></div>
				
				</div>
				
			</div>
			<!-- end: Row -->
			
			<hr>
			
			<!-- start: Row -->
			<div class="row">
				
				<div class="span4">
				
					<h3><span class="fa-icon-facebook"><i></i></span> Successful Charges</h3>
					<div id="chart-successfulcharges" style="height:200px" ></div>
				
				</div>
				
				<div class="span5">
				
					<h3><span class="fa-icon-twitter"><i></i></span> Payment Types</h3>
					<div id="chart-paymenttypes" style="height:200px" ></div>
				
				</div>
				
			</div>
			<!-- end: Row -->	
                    </span>
                </div>
            </div>
            <!--end: Container-->

        </div>
        <!-- end: Wrapper  -->			

        <%@include file="inc/footer_menu.jsp" %>
        <%@include file="inc/footer.jsp" %>

        <%@include file="inc/post_body.jsp" %>
        <script src="<c:url value="/api.js"/>"></script>
        <script src="<c:url value="/js/jquery.flot.js"/>"></script>
        <script src="<c:url value="/js/jquery.flot.pie.js"/>"></script>
        <script src="<c:url value="/js/jquery.flot.resize.js"/>"></script>
        <script>
            var paymentTypeData = [];
            paymentTypeData[0] = {label:"Visa", data:200};
            paymentTypeData[1] = {label:"Dankort", data:500};
            paymentTypeData[2] = {label:"Mastercard", data:50};
            
            function labelFormatter(label, series) {
		return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + Math.round(series.percent) + "%</div>";
            }
            
            $.plot('#chart-paymenttypes', paymentTypeData, {
                series: {
                    pie: {
                        show: true,
                        radius: 500,
                        label: {
                            show: true,
                            formatter: labelFormatter,
                            threshold: 0.0
                        }
                    }
                },
                legend: {
                    show: false
                }
            });
        </script>
        
        
    </body>
</html>
<!-- Localized -->
