<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                    <div class="slider">

                        <div id="flex1" class="flexslider" style="background: transparent">
                            <ul class="slides">

                                <li style="width: 100%; float: left; margin-right: -100%; position: relative; display: list-item;" class="flex-active-slide">
                                    <img src="img/slider/slider-logo.png" alt="">
                                    <div class="slide-caption n hidden-phone">
                                        <h3><spring:message code="landingpage.slider.title_1"/></h3>
                                        <p><spring:message code="landingpage.slider.description_1"/></p>
                                        <br/>
                                        <button class="btn btn-primary"><spring:message code="landingpage.slider.read_more"/></button>
                                    </div>
                                </li>

                                <li style="width: 100%; float: left; margin-right: -100%; position: relative; display: none;" class="">
                                    <img src="img/slider/slider-mobile.png" alt="">
                                    <div class="slide-caption hidden-phone">
                                        <h3><spring:message code="landingpage.slider.title_2"/></h3>
                                        <p><spring:message code="landingpage.slider.description_2"/></p>
                                        <br/>
                                        <button class="btn btn-primary"><spring:message code="landingpage.slider.read_more"/></button>
                                    </div>
                                </li>

                            </ul>
                            <ul class="flex-direction-nav"><li><a class="flex-prev" href="#">Previous</a></li><li><a class="flex-next" href="#">Next</a></li></ul></div>

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
                    <h3><spring:message code="landingpage.herounit.text"/></h3>
                    <p><a class="btn btn-primary btn-large">Learn more &raquo;</a></p>
                </div-->
                <!-- end: Hero Unit -->

                <hr>

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

                <hr>

                <!-- start: Row -->
                <div class="row">

                    <div class="span9">

                        <div class="title"><h3>Latest Works</h3></div>

                        <!-- start: Row -->
                        <div class="row">

                            <div class="span3">

                                <div class="picture">
                                    <a href="img/car.jpg" rel="image" title="mobile app">
                                        <img src="img/car.jpg" >
                                        <div class="image-overlay-zoom"></div>
                                    </a>
                                </div>
                                <div class="item-description">
                                    <h4><a href="#">Mobile App</a></h4>
                                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam</p>
                                </div>

                            </div>

                            <div class="span3">

                                <div class="picture">
                                    <a href="project.html">
                                        <img src="img/web_app1.jpg">
                                        <div class="image-overlay-link"></div>
                                    </a>
                                </div>
                                <div class="item-description">
                                    <h4><a href="#">Mobile App</a></h4>
                                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam</p>
                                </div>

                            </div>

                            <div class="span3">

                                <div class="picture">
                                    <a href="img/mobile_app1.jpg" rel="image" title="mobile app">
                                        <img src="img/mobile_app1.jpg">
                                        <div class="image-overlay-zoom"></div>
                                    </a>
                                </div>
                                <div class="item-description">
                                    <h4><a href="#">Mobile App</a></h4>
                                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam</p>
                                </div>

                            </div>

                        </div>
                        <!-- end: Row -->

                    </div>

                    <div class="span3">

                        <!-- start: Testimonials-->

                        <div class="testimonial-container">

                            <div class="title"><h3>What Client’s Say</h3></div>

                            <div class="testimonials-carousel" data-autorotate="3000">

                                <ul class="carousel">

                                    <li class="testimonial">
                                        <div class="testimonials">Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</div>
                                        <div class="testimonials-bg"></div>
                                        <div class="testimonials-author">Lucas Luck, <span>CEO</span></div>
                                    </li>

                                    <li class="testimonial">
                                        <div class="testimonials">Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. </div>
                                        <div class="testimonials-bg"></div>
                                        <div class="testimonials-author">Lucas Luck, <span>CTO</span></div>
                                    </li>

                                    <li class="testimonial">
                                        <div class="testimonials">Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</div>
                                        <div class="testimonials-bg"></div>
                                        <div class="testimonials-author">Lucas Luck, <span>COO</span></div>
                                    </li>

                                    <li class="testimonial">
                                        <div class="testimonials">Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</div>
                                        <div class="testimonials-bg"></div>
                                        <div class="testimonials-author">Lucas Luck, <span>CMO</span></div>
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
                <div class="clients-carousel">

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

                </div>
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