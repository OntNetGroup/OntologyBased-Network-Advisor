<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<title>N-Open</title>

<%@include file="/frontend/template/header.jsp"%>

<div class="container">
	<div class="row">		
		
		<!-- start: Main Menu -->
		<div id="sidebar-left" class="col-lg-2 col-sm-1">
			<br/>
			<div class="nav-collapse sidebar-nav collapse navbar-collapse bs-navbar-collapse">				
				<ul class="nav nav-tabs nav-stacked main-menu">					
					<li>
						<a href="network-topology.htm" class=""><i class="icon-edit"></i><span class="hidden-sm">&nbsp;&nbsp; Network Topology</span></a>
					</li>						
					<li>
						<a href="equipment-studio.htm" class=""><i class="icon-edit"></i><span class="hidden-sm">&nbsp;&nbsp; Equipment Studio</span></a>
					</li>
					<li>
						<a href="provisioning.htm" class=""><i class="icon-edit"></i><span class="hidden-sm">&nbsp;&nbsp; Provisioning</span></a>
					</li>														
					<li>
						<a href="advisor.htm" class=""><i class="icon-edit"></i><span class="hidden-sm">&nbsp;&nbsp; Network Advisor</span></a>
					</li>
				</ul>
			</div>
		</div>
		<!-- end: Main Menu -->
		
		<!-- start: Content -->
		<div id="content" class="col-lg-10 col-sm-11">			
		
		</div>
		<!-- end: Content -->
							
	</div><!--/row-->		
</div><!--/container-->
	
<%@include file="/frontend/template/footer.jsp"%>

