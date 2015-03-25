<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	
	<meta charset="utf8"/>
	<title>N-OPEn</title>
		
	<%@include file="/frontend/template/header-head.jsp"%>	
</head>

<body>
	<%@include file="/frontend/template/header-body.jsp"%>
	
	<div class="container">
		<div class="row">					
			<!-- start: Main Menu -->
			<div id="sidebar-left" class="col-lg-2 col-sm-1">
				<br/>
				<div class="nav-collapse sidebar-nav collapse navbar-collapse bs-navbar-collapse">				
					<ul class="nav nav-tabs nav-stacked main-menu">	
						<li>
						<a class="dropmenu drop" href="#"><i class="icon-edit"></i><span class="hidden-sm">&nbsp;&nbsp; Edit</span> <span class="label"></span></a>
						<ul class="dropmenuLevel1">									
							<li><a  class="submenu" href="network-topology.htm" class=""><b class="">(NT)</b><span class="hidden-sm">&nbsp;&nbsp; Network Topology</span></a></li>
							<li><a  class="submenu" href="equipment-studio.htm" class=""><b class="">(ES)</b><span class="hidden-sm">&nbsp;&nbsp; Equipment Studio</span></a></li>
							<li><a  class="submenu" href="provisioning.htm" class=""><b class="">(PR)</b><span class="hidden-sm">&nbsp;&nbsp; Provisioning</span></a></li>
							<li><a  class="submenu" href="advisor.htm" class=""><b class="">(NA)</b><span class="hidden-sm">&nbsp;&nbsp; Network Advisor</span></a></li>
						</ul>	
						</li>												
						<li><a  href="options.htm" class="icon-laptop"><b class=""></b><span class="hidden-sm">&nbsp;&nbsp; Options </span></a></li>																																						
						<li>
						<a class="dropmenu drop" href="#"><i class="icon-question-sign"></i><span class="hidden-sm">&nbsp;&nbsp; Help</span> <span class="label"></span></a>
						<ul class="dropmenuLevel1">									
							<li><a  class="submenu" href="info.htm" class=""><i class="icon-exclamation-sign"></i><span class="hidden-sm">&nbsp;&nbsp; About</span></a></li>
							<li><a  class="submenu" href="questions.htm" class=""><i class="icon-question-sign"></i><span class="hidden-sm">&nbsp;&nbsp; Frequently Asked Questions (FAQ)</span></a></li>
						</ul>	
						</li>												
					</ul>
				</div>
			</div>
			<!-- end: Main Menu -->			
			<!-- start: Content -->
			<div id="content" class="col-lg-10 col-sm-11">		