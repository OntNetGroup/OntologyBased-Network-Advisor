<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta charset="utf-8">
<title>Network Advisor</title>
<!-- end: Meta -->

<!-- start: Mobile Specific -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- end: Mobile Specific -->


<!-- start: CSS -->

<link href="frontend/advisor/css/okco.css" rel="stylesheet">
<link href="frontend/advisor/css/advisor.css" rel="stylesheet">
<link href="frontend/advisor/css/bootstrap.min.css" rel="stylesheet">
<link href="frontend/advisor/css/style.min.css" rel="stylesheet">
<link href="frontend/advisor/css/retina.min.css" rel="stylesheet">

<!-- Code mirror includes - used in Sindel -->
<link rel="stylesheet" href="frontend/advisor/code_mirror/lib/codemirror.css">
<link rel="stylesheet" href="frontend/advisor/code_mirror/addon/show-hint.css">
<link rel="stylesheet" href="frontend/advisor/code_mirror/theme/neat.css">

<!--end: CSS -->

<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<link id="ie-style" href="frontend/advisor/ie.css" rel="stylesheet">
	<![endif]-->

<!--[if IE 9]>
		<link id="ie9style" href="frontend/advisor/ie9.css" rel="stylesheet">
	<![endif]-->

<!-- start: Favicon and Touch Icons -->
<link rel="shortcut icon" href="frontend/advisor/ico/favicon.png">
<!-- end: Favicon and Touch Icons -->

<!-- start: JavaScript-->
<script src="frontend/advisor/js/jquery-1.10.2.min.js"></script>
<script src="frontend/advisor/js/jquery-migrate-1.2.1.min.js"></script>
<script src="frontend/advisor/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="frontend/advisor/js/jquery.ui.touch-punch.js"></script>
<script src="frontend/advisor/js/modernizr.js"></script>
<script src="frontend/advisor/js/bootstrap.min.js"></script>
<script src="frontend/advisor/js/jquery.cookie.js"></script>
<script src='frontend/advisor/js/fullcalendar.min.js'></script>
<script src='frontend/advisor/js/jquery.dataTables.min.js'></script>
<script src='frontend/advisor/js/dataTables.bootstrap.min.js'></script>
<script src="frontend/advisor/js/excanvas.js"></script>
<script src="frontend/advisor/js/jquery.flot.js"></script>
<script src="frontend/advisor/js/jquery.flot.pie.js"></script>
<script src="frontend/advisor/js/jquery.flot.stack.js"></script>
<script src="frontend/advisor/js/jquery.flot.resize.min.js"></script>
<script src="frontend/advisor/js/jquery.flot.time.js"></script>
<script src="frontend/advisor/js/jquery.chosen.min.js"></script>
<script src="frontend/advisor/js/jquery.uniform.min.js"></script>
<script src="frontend/advisor/js/jquery.cleditor.min.js"></script>
<script src="frontend/advisor/js/jquery.noty.js"></script>
<script src="frontend/advisor/js/jquery.elfinder.min.js"></script>
<script src="frontend/advisor/js/jquery.raty.min.js"></script>
<script src="frontend/advisor/js/jquery.iphone.toggle.js"></script>
<script src="frontend/advisor/js/jquery.uploadify-3.1.min.js"></script>
<script src="frontend/advisor/js/jquery.gritter.min.js"></script>
<script src="frontend/advisor/js/jquery.imagesloaded.js"></script>
<script src="frontend/advisor/js/jquery.masonry.min.js"></script>
<script src="frontend/advisor/js/jquery.knob.modified.js"></script>
<script src="frontend/advisor/js/jquery.sparkline.min.js"></script>
<script src="frontend/advisor/js/counter.min.js"></script>
<script src="frontend/advisor/js/raphael.2.1.0.min.js"></script>
<script src="frontend/advisor/js/justgage.1.0.1.min.js"></script>
<script src="frontend/advisor/js/jquery.autosize.min.js"></script>
<script src="frontend/advisor/js/retina.js"></script>
<script src="frontend/advisor/js/jquery.placeholder.min.js"></script>
<script src="frontend/advisor/js/wizard.min.js"></script>
<script src="frontend/advisor/js/core.min.js"></script>
<script src="frontend/advisor/js/charts.min.js"></script>
<script src="frontend/advisor/js/custom.min.js"></script>
<script src="frontend/advisor/js/jquery.paulund_modal_box.js"></script>
<!-- end: JavaScript-->

<!-- Sindel includes -->
<script src="frontend/advisor/js/jison/sindel-parser.js"></script>
<script src="frontend/advisor/code_mirror/lib/codemirror.js"></script> 
<script src="frontend/advisor/code_mirror/addon/matchbrackets.js"></script> 
<script src="frontend/advisor/code_mirror/addon/closebrackets.js"></script> 			
<script src="frontend/advisor/code_mirror/addon/show-hint.js"></script> 
<script src="frontend/advisor/code_mirror/sindel_custom/sindel-hint.js"></script> 
<script src="frontend/advisor/code_mirror/sindel_custom/sindel-highlight.js"></script> 
<script src="frontend/advisor/js/okco.js"></script>
<script src="frontend/advisor/js/advisor.js"></script>

<!-- END Syntax Highlight -->
<script src="frontend/advisor/js/graph/arbor.js"></script>
<script src="frontend/advisor/js/graph/graphics.js"></script>
<script src="frontend/advisor/js/graph/main.js"></script>
<script src="frontend/advisor/js/graph/jquery.contextmenu.js"></script>
<link rel="stylesheet" type="text/css" href="frontend/advisor/js/graph/contextMenu.css"	media="all">

<style type="text/css">

	.container #sidebar-left {
		width: 18.422%;  /* +4 */
	}
	
	.container #content {
		width: 81.578%; /* -4 */
	}
	
	.submenu {
		color: #fff;
	}
	
	.dropmenuLevel1{
		margin-left: 10px !important;
	}
	
	.dropmenuLevel2 {
		padding-left: 0px !important;
	}
	
	.dropmenuLevel2 li {
		margin-top: 8px !important;
	}
	
	.dropmenuLevel2 li:first-child {
		margin-top: 0 !important;
	}	

	a.navbar-brand {
		width: 18.422%;
	}
	
	a#main-menu-toggle {
		left: 50px !important;
	}

</style>


</head>

<script type="text/javascript">
$(document).ready(function() {

	$("#maskforloading").hide();

	$(".btnload").click(function(){
		loading();
	});	

	$("#dialog").dialog({
		   autoOpen: false,
		   modal: true,
		   buttons : {
		        "Confirm" : function() {  
		            window.location.href = "welcome.htm";
		        },
		        "Cancel" : function() {
		          $(this).dialog("close");
		        }
		      }
		    });

		$(".callConfirm").on("click", function(e) {
			
		    e.preventDefault();
		    $("#dialog").dialog("open");
		});
	
}); // End - document ready;

</script>


<body>
	<!-- NÃ£o remova o div#mask, pois ele Ã© necessÃ¡rio para preencher toda a janela -->
	<div id="mask"></div>

	<!-- Não remova o div#maskforloading, pois ele é necessário para preencher toda a janela -->
	<div id="maskforloading">
		<img src="frontend/advisor/img/loading.gif" height="100px">
	</div>

	<div id="dialog" title="Confirmation Required">
	  All loaded information will be lost. Do you want to continue?
	</div>
	
	<!-- start: Header -->
	<div class="navbar">
		<div class="navbar-inner">
			<div class="container">
				<button class="navbar-toggle" type="button" data-toggle="collapse"
					data-target=".sidebar-nav.nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a id="main-menu-toggle" class="hidden-xs open"><i
					class="icon-reorder"></i></a>
				<div class="row">
					<a class="navbar-brand col-lg-2 col-sm-1 col-xs-12"
						href="welcome.htm">
							<span>Network Advisor</span>
 					</a>
				</div>
				<!-- start: Header Menu -->
				<div class="nav-no-collapse header-nav"></div>
				<!-- end: Header Menu -->

			</div>
		</div>
	</div>
	<!-- end: Header -->

	<div class="container">
		<div class="row">

			<!-- start: Main Menu -->
			<div id="sidebar-left" class="col-lg-2 col-sm-1">
				<br />
				<div class="nav-collapse sidebar-nav collapse navbar-collapse bs-navbar-collapse">
				
					<ul class="nav nav-tabs nav-stacked main-menu">
					
						<!-- <li><a href="/oryx" target="_blank"><i class="icon-edit"></i><span
								class="hidden-sm"> Equipment Studio</span></a></li> -->
						<li>
							<a class="dropmenu" href="#"><i class="icon-laptop"></i><span class="hidden-sm"> Network Advisor</span> <span class="label">4</span></a>
							<ul class="dropmenuLevel1">
								<li><a class="submenu callConfirm" href="#"><i class="icon-check"></i><span class="hidden-sm">Start Page</span></a></li>
								
								<li>
									<a class="dropmenu drop" href="#"><i class="icon-download"></i><span class="hidden-sm"> Add Information</span> <span class="label">3</span></a>
									<ul class="dropmenuLevel2">								
										
										<li><a class="submenu" href="sindel.htm"><i class="">S</i><span class="hidden-sm"> &nbsp;&nbsp; Sindel Editor</span></a></li>
										<li><a class="submenu" href="add-equipment.htm"><i class="">E</i><span class="hidden-sm"> &nbsp;&nbsp; Equipment Instances</span></a></li>
									</ul>	
								</li>
								
								<li>
									<a class="dropmenu" href="#"><i class="icon-lightbulb"></i><span class="hidden-sm"> Functionalites</span> <span class="label">3</span></a>
									<ul class="dropmenuLevel2">								
										
										<li><a class="submenu" href="okco-list.htm"><i class="icon-ok-circle"></i><span class="hidden-sm">&nbsp;&nbsp; Knowledge Completion <br> &nbsp;&nbsp; (OKCo)</span></a></li>
										<li><a class="submenu" href="open-visualizer.htm"><i class="icon-eye-open"></i><span class="hidden-sm">&nbsp;&nbsp; Visualization</span></a></li>
										<li><a class="submenu" href="binds.htm"><i class="">B</i><span class="hidden-sm"> &nbsp;&nbsp; Provisioning: Binds</span></a></li>
										<li><a class="submenu" href="connects.htm"><i class="">C</i><span class="hidden-sm"> &nbsp;&nbsp; Provisioning: Connects</span></a></li>
									</ul>
								</li>
								
								<li>
									<a class="dropmenu" href="#"><i class="icon-save"></i><span class="hidden-sm"> Output Results</span> <span class="label">2</span></a>
									<ul class="dropmenuLevel2">								
										
										<li><a class="submenu" href="getModel.htm"><i class="icon-save"></i><span class="hidden-sm">&nbsp;&nbsp; Save as OWL</span></a></li>
									</ul>	
								</li>
								
							</ul>	
						</li>
					
					
						<li><a href="faq.htm" class=""><i class="icon-question-sign"></i><span
								class="hidden-sm"> FAQ</span></a></li>
								
						<li><a href="about.htm" class=""><i class="icon-exclamation-sign"></i><span
								class="hidden-sm"> About</span></a></li>

					</ul>
				</div>
			</div>
			<!-- end: Main Menu -->

			<!-- start: Content -->
			<div id="content" class="col-lg-10 col-sm-11">