<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="area" value="landingpage"/>
<!DOCTYPE html>
<html lang="${request.locale.language}">
    <head>
        <title><spring:message code="general.system_name"/></title>
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
                    <div class="container" style="height: 180px;">
                        <div class="pull-left" style="top: 50%;position: absolute;margin-top: -70px;padding-left:3%;">
                            <h2 style="text-shadow: 0px 1px 3px #3f3f3f;"><spring:message code="landingpage.teaser.title"/></h2>
                            <h3 style="color: whitesmoke;padding-left: 20px;margin-bottom: 10px;text-shadow: 0px 1px 3px #3f3f3f;"><spring:message code="landingpage.teaser.text"/></h3>

                            <a href="<c:url value="/signup"/>" class="btn" style="margin-left: 20px;"><spring:message code="landingpage.teaser.button"/></a>
                        </div>
                        <div class="pull-right visible-desktop" style="padding-right:3%">
                            <img src="<c:url value="/img/cardcloud.png"/>"  height="160px">
                        </div>
   
                    </div>
                </div>
                <!-- end: Container  -->

            </div>	

        </div>
        <!-- end: Page Title -->

        <!--start: Wrapper-->
        <div id="wrapper">

            <!--start: Container -->
            <div class="container">

                <!--hr-->

                <!-- start: Hero Unit - Main hero unit for a primary marketing message or call to action -->
                <!--div class="hero-unit">
                    <h3></h3>
                    <p><a class="btn btn-primary btn-large">Learn more &raquo;</a></p>
                </div>
                <hr-->
                <!-- end: Hero Unit -->


                <!-- start: Row -->
                <div class="row">

                    <!-- start: Icon Boxes -->
                    <div class="icons-box-vert-container">

                        <!-- start: Icon Box Start -->
                        <div class="span4">
                            <div class="icons-box-vert">
                                <i class="ico-ok ico-white circle-color-full"></i>
                                <div class="icons-box-vert-info">
                                    <h3><spring:message code="landingpage.iconboxes.title_1"/></h3>
                                    <p><spring:message code="landingpage.iconboxes.text_1"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <!-- end: Icon Box-->

                        <!-- start: Icon Box Start -->
                        <div class="span4">
                            <div class="icons-box-vert">
                                <i class="ico-shield ico-white circle-color-full"></i>
                                <div class="icons-box-vert-info">
                                    <h3><spring:message code="landingpage.iconboxes.title_2"/></h3>
                                    <p><spring:message code="landingpage.iconboxes.text_2"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <!-- end: Icon Box -->

                        <!-- start: Icon Box Start -->
                        <div class="span4">
                            <div class="icons-box-vert">
                                <i class="ico-ipad ico-white circle-color-full"></i>
                                <div class="icons-box-vert-info">
                                    <h3><spring:message code="landingpage.iconboxes.title_3"/></h3>
                                    <p><spring:message code="landingpage.iconboxes.text_3"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <!-- end: Icon Box -->

                    </div>
                    <!-- end: Icon Boxes -->
                    <div class="clear"></div>
                </div>
                <!-- end: Row -->
                
                <div class="row">

                    <!-- start: Icon Boxes -->
                    <div class="icons-box-vert-container">

                        <!-- start: Icon Box Start -->
                        <div class="span4">
                            <div class="icons-box-vert">
                                <i class="ico-group ico-white circle-color-full"></i>
                                <div class="icons-box-vert-info">
                                    <h3><spring:message code="landingpage.iconboxes.title_4"/></h3>
                                    <p><spring:message code="landingpage.iconboxes.text_4"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <!-- end: Icon Box-->

                        <!-- start: Icon Box Start -->
                        <div class="span4">
                            <div class="icons-box-vert">
                                <i class="ico-euro ico-white circle-color-full"></i>
                                <div class="icons-box-vert-info">
                                    <c:url var="acquirerUrl" value="/acquirers"/>
                                    <h3><spring:message code="landingpage.iconboxes.title_5"/></h3>
                                    <p><spring:message arguments="${acquirerUrl}" code="landingpage.iconboxes.text_5"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <!-- end: Icon Box -->

                        <!-- start: Icon Box Start -->
                        <div class="span4">
                            <div class="icons-box-vert">
                                <i class="ico-cogwheels ico-white circle-color-full"></i>
                                <div class="icons-box-vert-info">
                                    <h3><spring:message code="landingpage.iconboxes.title_6"/></h3>
                                    <p><spring:message code="landingpage.iconboxes.text_6"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <!-- end: Icon Box -->

                    </div>
                    <!-- end: Icon Boxes -->
                    <div class="clear"></div>
                </div>
                <!-- end: Row -->

                <hr>

                <!-- start: Row -->
                <div class="row">

                    <div class="span9">

                        <div class="title"><h3><spring:message code="landingpage.api.title"/></h3></div>

                        <!-- start: Row -->
                        <div class="row">

                            <div class="span9">

                             This is where the Simple API will be described

                            </div>

                        </div>
                        <!-- end: Row -->

                    </div>

                    <div class="span3">

                        <!-- start: Testimonials-->

                        <div class="testimonial-container">

                            <div class="title"><h3><spring:message code="landingpage.clients.title"/></h3></div>

                            <div class="testimonials-carousel" data-autorotate="3000">

                                <ul class="carousel">

                                    <li class="testimonial">
                                        <div class="testimonials"><spring:message code="landingpage.clients.first.text"/></div>
                                        <div class="testimonials-bg"></div>
                                        <div class="testimonials-author"><spring:message code="landingpage.clients.first.name"/>, <span><spring:message code="landingpage.clients.first.title"/></span></div>
                                    </li>

                                    <li class="testimonial">
                                        <div class="testimonials"><spring:message code="landingpage.clients.second.text"/></div>
                                        <div class="testimonials-bg"></div>
                                        <div class="testimonials-author"><spring:message code="landingpage.clients.second.name"/>, <span><spring:message code="landingpage.clients.second.title"/></span></div>
                                    </li>


                                </ul>

                            </div>

                        </div>

                        <!-- end: Testimonials-->

                    </div>

                </div>
                <!-- end: Row -->

                <hr>

                <!-- start Clients List -->	
                <!--div class="clients-carousel">

                    <ul class="slides clients">
                        <li><img src="img/logos/1.png" alt=""/></li>
                        <li><img src="img/logos/2.png" alt=""/></li>	
                        <li><img src="img/logos/3.png" alt=""/></li>
                        <li><img src="img/logos/4.png" alt=""/></li>
                        <li><img src="img/logos/5.png" alt=""/></li>
                        <li><img src="img/logos/6.png" alt=""/></li>
                        <li><img src="img/logos/7.png" alt=""/></li>
                        <li><img src="img/logos/8.png" alt=""/></li>
                        <li><img src="img/logos/9.png" alt=""/></li>
                        <li><img src="img/logos/10.png" alt=""/></li>
                        <li><img src="img/logos/1.png" alt=""/></li>
                        <li><img src="img/logos/2.png" alt=""/></li>	
                        <li><img src="img/logos/3.png" alt=""/></li>
                        <li><img src="img/logos/4.png" alt=""/></li>
                        <li><img src="img/logos/5.png" alt=""/></li>
                        <li><img src="img/logos/6.png" alt=""/></li>
                        <li><img src="img/logos/7.png" alt=""/></li>
                        <li><img src="img/logos/8.png" alt=""/></li>
                        <li><img src="img/logos/9.png" alt=""/></li>
                        <li><img src="img/logos/10.png" alt=""/></li>	
                    </ul>

                </div-->
                <!-- end Clients List -->

                <hr>

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