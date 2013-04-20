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
        <script src="<c:url value="/js/jquery.flot.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.flot.pie.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.flot.resize.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.flot.time.min.js"/>"></script>
        <script>
            var timeStart = new Date(2013, 3, 20).getTime();
            var timeEnd = new Date(2013, 3, 21).getTime();
            var xaxisDef = {
                mode: "time",
                timezone:"browser",
                minTickSize: [1, "hour"],
                min: timeStart,
                max: timeEnd
            };
            
            var grossVolumeData = [];
            var count = 0;
            for(var i=timeStart;i<=timeEnd;i+=3600000) {
                grossVolumeData[count++] = [i, parseInt(count*40+ (60*Math.random()))];
            }
            
            var successfulChargesData = [];
            count = 0;
            for(var i=timeStart;i<=timeEnd;i+=3600000) {
                successfulChargesData[count++] = [i, parseInt(count*20+ (20*Math.random()))];
            }

            
            var paymentTypeData = [];
            paymentTypeData[0] = {label:"Visa", data:200};
            paymentTypeData[1] = {label:"Dankort", data:500};
            paymentTypeData[2] = {label:"Mastercard", data:50};
            
            function labelFormatter(label, series) {
                return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + Math.round(series.percent) + "%</div>";
            }
            
            $.plot('#chart-grossvolume', [ { data: grossVolumeData, label: "Volume" } ], {
                series: {
                    lines: { show: true,
                        lineWidth: 2,
                        fill: true, fillColor: { colors: [ { opacity: 0.5 }, { opacity: 0.2 } ] }
                    },
                    points: { show: true },
                    shadowSize: 1
                },
                grid: { hoverable: true, 
                    clickable: true, 
                    tickColor: "#eee",
                    borderWidth: 0
                },
                legend: {
                    show: false
                },
                colors: ["#414141"],
                xaxis: xaxisDef,
                yaxis: {tickFormatter: formatCurrency}
            });
            
            $("#chart-grossvolume").bind("plothover", function (event, pos, item) {
                if (item) {
                    if (!previousPoint || previousPoint != item.dataIndex) {

                        previousPoint = item.dataIndex;

                        $("#tooltip").remove();
                        var x = item.datapoint[0].toFixed(2),
                        y = item.datapoint[1].toFixed(2);
                        
                        showTooltip(item.pageX, item.pageY, formatCurrency(y));
                    }
                } else {
                    $("#tooltip").remove();
                    previousPoint = null;            
                }
			
            });
            
            var plot = $.plot($("#chart-successfulcharges"),
            [ { data: successfulChargesData, label: "Charges"} ], {
                series: {
                    lines: { show: true,
                        lineWidth: 2,
                        fill: true, fillColor: { colors: [ { opacity: 0.5 }, { opacity: 0.2 } ] }
                    },
                    points: { show: true, 
                        lineWidth: 2 
                    },
                    shadowSize: 0
                },
                grid: { hoverable: true, 
                    clickable: true, 
                    tickColor: "#ddd",
                    borderWidth: 0
                },
                colors: ["#1BB2E9"],
                xaxis: xaxisDef,
                yaxis: {ticks:3, tickDecimals: 0}
            });

            
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
            
            function formatCurrency(v) {
                return v + "DKK";
            }
            
            function showTooltip(x, y, contents) {
                $("<div id='tooltip'>" + contents + "</div>").css({
                    position: "absolute",
                    display: "none",
                    top: y - 40,
                    left: x - 30,
                    color: "white",
                    border: "1px solid #fdd",
                    padding: "2px",
                    "background-color": "#29a9df",
                    opacity: 0.80
                }).appendTo("body").fadeIn(100);
            }
        </script>


    </body>
</html>
<!-- Localized -->
