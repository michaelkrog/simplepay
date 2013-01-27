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

        <!-- start: Slider -->
        <div class="slider-wrapper">

            <div id="da-slider" class="da-slider">
                <div class="da-slide">
                    <h2><spring:message code="landingpage.slider.title_1"/></h2>
                    <p><spring:message code="landingpage.slider.description_1"/></p>
                    <a href="#" class="da-link"><spring:message code="landingpage.slider.read_more"/></a>
                    <div class="da-img"><img src="img/parallax-slider/macbook.png" alt="image01" /></div>
                </div>
                <div class="da-slide">
                    <h2><spring:message code="landingpage.slider.title_2"/></h2>
                    <p><spring:message code="landingpage.slider.description_2"/></p>
                    <a href="#" class="da-link"><spring:message code="landingpage.slider.read_more"/></a>
                    <div class="da-img"><img src="img/parallax-slider/responsive.png" alt="image02" /></div>
                </div>
                <nav class="da-arrows">
                    <span class="da-arrows-prev"></span>
                    <span class="da-arrows-next"></span>
                </nav>
            </div>

        </div>
        <!-- end: Slider -->

        <!--start: Wrapper-->
        <div id="wrapper">

            <!--start: Container -->
            <div class="container">

                <hr>

                <!-- start: Hero Unit - Main hero unit for a primary marketing message or call to action -->
                <div class="hero-unit">
                    <h3><spring:message code="landingpage.herounit.text"/></h3>
                    <p><a class="btn btn-primary btn-large">Learn more &raquo;</a></p>
                </div>
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
                                    <h3>Easy to use</h3>
                                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <!-- end: Icon Box-->

                        <!-- start: Icon Box Start -->
                        <div class="span4">
                            <div class="icons-box-vert">
                                <i class="ico-cup  ico-white circle-color-full"></i>
                                <div class="icons-box-vert-info">
                                    <h3>Best choice</h3>
                                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
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
                                    <h3>Fully Responsive</h3>
                                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
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