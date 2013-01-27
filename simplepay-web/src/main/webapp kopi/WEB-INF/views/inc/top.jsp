<%@page import="dk.apaq.simplepay.security.SecurityHelper"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            
            <a href="/" class="brand">simple<span class="blue">pay</span></a>
            
            <div class="pull-right">
                <% if(SecurityHelper.isAnonymousUser()) { %>
                   <a href="dashboard" class="btn btn-primary">Log ind</a>
                <% } else { %>
                   <a href="/logout" class="btn btn-primary">Log ud</a>
                <% } %>
            </div>
            
            <div class="nav-collapse pull-right">

                <ul class="nav">
                    <li><a href="dashboard">Administration</a></li>
                    <li><a href="doc">Dokumentation</a></li>
                    <li><a href="help">Hjælp og support</a></li>
                    <li class="divider-vertical"></li>
                </ul>
            
                
            </div>
        </div>
    </div>
</div>