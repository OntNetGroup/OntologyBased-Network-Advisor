<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<!-- start: Meta --> 
	<meta charset="utf-8">
	<title>N-Open Environment</title>
	<!-- end: Meta -->

	<!-- start: Mobile Specific -->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- end: Mobile Specific -->

	<!-- start: CSS -->
	<link href="/nopen/frontend/template/css/okco.css" rel="stylesheet">
	<link href="/nopen/frontend/template/css/advisor.css" rel="stylesheet">
	<link href="/nopen/frontend/template/css/bootstrap.min.css" rel="stylesheet">
	<link href="/nopen/frontend/template/css/style.min.css" rel="stylesheet">
	<link href="/nopen/frontend/template/css/retina.min.css" rel="stylesheet">
	<!-- Code mirror includes -->
	<link rel="stylesheet" href="/nopen/frontend/template/code_mirror/lib/codemirror.css">
	<link rel="stylesheet" href="/nopen/frontend/template/code_mirror/addon/show-hint.css">
	<link rel="stylesheet" href="/nopen/frontend/template/code_mirror/theme/neat.css">
	<!--end: CSS -->

	<!-- start: Favicon and Touch Icons -->
	<link rel="shortcut icon" href="/nopen/frontend/template/ico/favicon.png">
	<!-- end: Favicon and Touch Icons -->

	<!-- start: JavaScript-->
	<script src="/nopen/frontend/template/js/jquery-1.10.2.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery-migrate-1.2.1.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.ui.touch-punch.js"></script>
	<script src="/nopen/frontend/template/js/modernizr.js"></script>
	<script src="/nopen/frontend/template/js/bootstrap.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.cookie.js"></script>
	<script src='/nopen/frontend/template/js/fullcalendar.min.js'></script>
	<script src='/nopen/frontend/template/js/jquery.dataTables.min.js'></script>
	<script src='/nopen/frontend/template/js/dataTables.bootstrap.min.js'></script>
	<script src="/nopen/frontend/template/js/excanvas.js"></script>
	<script src="/nopen/frontend/template/js/jquery.flot.js"></script>
	<script src="/nopen/frontend/template/js/jquery.flot.pie.js"></script>
	<script src="/nopen/frontend/template/js/jquery.flot.stack.js"></script>
	<script src="/nopen/frontend/template/js/jquery.flot.resize.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.flot.time.js"></script>
	<script src="/nopen/frontend/template/js/jquery.chosen.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.uniform.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.cleditor.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.noty.js"></script>
	<script src="/nopen/frontend/template/js/jquery.elfinder.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.raty.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.iphone.toggle.js"></script>
	<script src="/nopen/frontend/template/js/jquery.uploadify-3.1.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.gritter.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.imagesloaded.js"></script>
	<script src="/nopen/frontend/template/js/jquery.masonry.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.knob.modified.js"></script>
	<script src="/nopen/frontend/template/js/jquery.sparkline.min.js"></script>
	<script src="/nopen/frontend/template/js/counter.min.js"></script>
	<script src="/nopen/frontend/template/js/raphael.2.1.0.min.js"></script>
	<script src="/nopen/frontend/template/js/justgage.1.0.1.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.autosize.min.js"></script>
	<script src="/nopen/frontend/template/js/retina.js"></script>
	<script src="/nopen/frontend/template/js/jquery.placeholder.min.js"></script>
	<script src="/nopen/frontend/template/js/wizard.min.js"></script>
	<script src="/nopen/frontend/template/js/core.min.js"></script>
	<script src="/nopen/frontend/template/js/charts.min.js"></script>
	<script src="/nopen/frontend/template/js/custom.min.js"></script>
	<script src="/nopen/frontend/template/js/jquery.paulund_modal_box.js"></script>
	<!-- end: JavaScript-->

	<!-- Sindel includes -->
	<script src="/nopen/frontend/template/js/jison/sindel-parser.js"></script>
	<script src="/nopen/frontend/template/code_mirror/lib/codemirror.js"></script> 
	<script src="/nopen/frontend/template/code_mirror/addon/matchbrackets.js"></script> 
	<script src="/nopen/frontend/template/code_mirror/addon/closebrackets.js"></script> 			
	<script src="/nopen/frontend/template/code_mirror/addon/show-hint.js"></script> 
	<script src="/nopen/frontend/template/code_mirror/sindel_custom/sindel-hint.js"></script> 
	<script src="/nopen/frontend/template/code_mirror/sindel_custom/sindel-highlight.js"></script> 
	<script src="/nopen/frontend/template/js/okco.js"></script>
	<script src="/nopen/frontend/template/js/advisor.js"></script>
	<!-- END Syntax Highlight -->

	<script src="/nopen/frontend/template/js/graph/arbor.js"></script>
	<script src="/nopen/frontend/template/js/graph/graphics.js"></script>
	<script src="/nopen/frontend/template/js/graph/main.js"></script>
	<script src="/nopen/frontend/template/js/graph/jquery.contextmenu.js"></script>
	<link rel="stylesheet" type="text/css" href="/nopen/frontend/template/js/graph/contextMenu.css"	media="all">

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
		            window.location.href = "home.htm";
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
	}); 
</script>

<body>
	<!-- Nao remova o div mask, pois ele e necessario para preencher toda a janela -->
	<div id="mask"></div>

	<!-- Na remova o div mask forloading, pois ele e necessario para preencher toda a janela -->
	<div id="maskforloading">
		<img src="/nopen/frontend/template/img/loading.gif" height="100px">
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
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a id="main-menu-toggle" class="hidden-xs open"><i class="icon-reorder"></i></a>
				<div class="row">
					<a class="navbar-brand col-lg-2 col-sm-1 col-xs-12" href="home.htm"> <span>N-Open Environment</span> </a>
				</div>
				<!-- start: Header Menu -->
				<div class="nav-no-collapse header-nav"></div>
				<!-- end: Header Menu -->
			</div>
		</div>
	</div>
	<!-- end: Header -->
			
			