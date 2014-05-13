<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta charset="utf-8">
<title>Ontology-based Network Advisor</title>
<meta name="description" content="Ontology-based Network Advisor">
	<meta name="author" content="Fábio Coradini, Pedro Paulo Barcelos, Vitcor Amorim, Freddy Brasileiro, Cássio Reginato">
<!-- end: Meta -->

<!-- start: Mobile Specific -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- end: Mobile Specific -->


<!-- start: CSS -->

<link href="Assets/css/bootstrap.min.css" rel="stylesheet">
<link href="Assets/css/style.min.css" rel="stylesheet">
<link href="Assets/css/retina.min.css" rel="stylesheet">

<!--end: CSS -->


<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<link id="ie-style" href="Assets/ie.css" rel="stylesheet">
	<![endif]-->

<!--[if IE 9]>
		<link id="ie9style" href="Assets/ie9.css" rel="stylesheet">
	<![endif]-->

<!-- start: Favicon and Touch Icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="Assets/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="Assets/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="Assets/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="Assets/ico/apple-touch-icon-57-precomposed.png">
<link rel="shortcut icon"
	href="Assets/ico/favicon.png">
<!-- end: Favicon and Touch Icons -->

<!-- start: JavaScript-->
<script
	src="Assets/js/jquery-1.10.2.min.js"></script>
<script
	src="Assets/js/jquery-migrate-1.2.1.min.js"></script>
<script
	src="Assets/js/jquery-ui-1.10.3.custom.min.js"></script>
<script
	src="Assets/js/jquery.ui.touch-punch.js"></script>
<script src="Assets/js/modernizr.js"></script>
<script
	src="Assets/js/bootstrap.min.js"></script>
<script
	src="Assets/js/jquery.cookie.js"></script>
<script
	src='Assets/js/fullcalendar.min.js'></script>
<script
	src='Assets/js/jquery.dataTables.min.js'></script>
<script
	src='Assets/js/dataTables.bootstrap.min.js'></script>
<script src="Assets/js/excanvas.js"></script>
<script src="Assets/js/jquery.flot.js"></script>
<script
	src="Assets/js/jquery.flot.pie.js"></script>
<script
	src="Assets/js/jquery.flot.stack.js"></script>
<script
	src="Assets/js/jquery.flot.resize.min.js"></script>
<script
	src="Assets/js/jquery.flot.time.js"></script>

<script
	src="Assets/js/jquery.chosen.min.js"></script>
<script
	src="Assets/js/jquery.uniform.min.js"></script>
<script
	src="Assets/js/jquery.cleditor.min.js"></script>
<script src="Assets/js/jquery.noty.js"></script>
<script
	src="Assets/js/jquery.elfinder.min.js"></script>
<script
	src="Assets/js/jquery.raty.min.js"></script>
<script
	src="Assets/js/jquery.iphone.toggle.js"></script>
<script
	src="Assets/js/jquery.uploadify-3.1.min.js"></script>
<script
	src="Assets/js/jquery.gritter.min.js"></script>
<script
	src="Assets/js/jquery.imagesloaded.js"></script>
<script
	src="Assets/js/jquery.masonry.min.js"></script>
<script
	src="Assets/js/jquery.knob.modified.js"></script>
<script
	src="Assets/js/jquery.sparkline.min.js"></script>
<script src="Assets/js/counter.min.js"></script>
<script
	src="Assets/js/raphael.2.1.0.min.js"></script>
<script
	src="Assets/js/justgage.1.0.1.min.js"></script>
<script
	src="Assets/js/jquery.autosize.min.js"></script>
<script src="Assets/js/retina.js"></script>
<script
	src="Assets/js/jquery.placeholder.min.js"></script>
<script src="Assets/js/wizard.min.js"></script>
<script src="Assets/js/core.min.js"></script>
<script src="Assets/js/charts.min.js"></script>
<script src="Assets/js/custom.min.js"></script>
<script src="Assets/js/jquery.paulund_modal_box.js"></script>
<!-- end: JavaScript-->

<!-- Sindel includes -->

<script src="Assets/js/jison/sindel-parser.js"></script>
<script src="Assets/js/jison/condel-parser.js"></script> 
	
<!-- Code mirror includes - used in Sindel -->

<link rel="stylesheet" href="Assets/code_mirror/lib/codemirror.css">
<link rel="stylesheet" href="Assets/code_mirror/addon/show-hint.css">
<link rel="stylesheet" href="Assets/code_mirror/theme/neat.css">

<script src="Assets/code_mirror/lib/codemirror.js"></script> 
<script src="Assets/code_mirror/addon/matchbrackets.js"></script> 
<script src="Assets/code_mirror/addon/closebrackets.js"></script> 			
<script src="Assets/code_mirror/addon/show-hint.js"></script> 
<script src="Assets/code_mirror/sindel_custom/sindel-hint.js"></script> 
<script src="Assets/code_mirror/sindel_custom/sindel-highlight.js"></script> 
<script src="Assets/code_mirror/condel_custom/condel-hint.js"></script> 
<script src="Assets/code_mirror/condel_custom/condel-highlight.js"></script>

<!-- END Syntax Highlight -->

<!-- GraphVisualizer includes -->
<style>
#currentNode {
	border: 1px solid #B8B8B8;
	width: 300px;
	height: 250px;
	padding-left: 15px;
	padding-top: 15px;
	top: 20px;
	overflow-y: auto;
	max-width: 290px;
	max-height: 210px;
	color: #fff;
	background: #222 linear-gradient(#444, #222);
	font-family: verdana, arial, sans-serif;
	font-size: 14px;
}

#subtitle {
	border: 1px solid #B8B8B8;
	width: 270px;
	height: 240px;
	padding-left: 10px;
	padding-top: 10px;
	overflow-y: auto;
	background: #222 linear-gradient(#444, #222);
}
</style>

<script src="Assets/js/graph/arbor.js"></script>
<script src="Assets/js/graph/graphics.js"></script>
<script src="Assets/js/graph/main.js"></script>
<script src="Assets/js/graph/jquery.contextmenu.js"></script>
<link rel="stylesheet" type="text/css" href="Assets/js/graph/contextMenu.css"	media="all">

<!-- GraphVisualizer includes -->

<!-- Includes for loading.gif -->
<style type="text/css">

	#maskforloading 
	{
		position:absolute;
		z-index:9990;  
		background-color:#000; 
		display:none;
		width: 100%;	
		
		 filter:alpha(opacity=50);
     	opacity: 0.5;
     	-moz-opacity:0.5;
     	-webkit-opacity:0.5;	
	}
	
	#maskforloading img 
	{
		position:absolute;
		top:50%;
		left:50%;
	}
	
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

	 // Function loading
	 function loading()
	 {
	  	var maskHeight = $(document).height();
		var maskWidth = "100%";//$(document).width();
	
		//Define largura e altura do div#maskforloading iguais ás dimensões da tela
		$('#maskforloading').css({'width':maskWidth,'height':maskHeight});
	
		//efeito de transição
		$('#maskforloading').show();
	 }

	$(".btnload").click(function(){

		loading();
	});	

	$("#dialog").dialog({
		   autoOpen: false,
		   modal: true,
		   buttons : {
		        "Confirm" : function() {  
		            window.location.href = "/tnokco/welcome";
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
		<img src="Assets/img/loading.gif" height="100px">
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
						href="/tnokco/welcome">
							<span>Ontology-based Network Advisor</span>
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
					
						<li><a href="/oryx" target="_blank"><i class="icon-edit"></i><span
								class="hidden-sm"> Equipment Studio</span></a></li>
						<li>
							<a class="dropmenu" href="#"><i class="icon-laptop"></i><span class="hidden-sm"> Network Advisor</span> <span class="label">4</span></a>
							<ul class="dropmenuLevel1">
								<li><a class="submenu callConfirm" href="#"><i class="icon-check"></i><span class="hidden-sm">Start Page</span></a></li>
								
								<li>
									<a class="dropmenu drop" href="#"><i class="icon-download"></i><span class="hidden-sm"> Add Information</span> <span class="label">3</span></a>
									<ul class="dropmenuLevel2">								
										
										<li><a class="submenu" href="/tnokco/sindel"><i class="">S</i><span class="hidden-sm"> &nbsp;&nbsp; Sindel Editor</span></a></li>
										<li><a class="submenu" href="/tnokco/condel"><i class="">C</i><span class="hidden-sm"> &nbsp;&nbsp; Condel Editor</span></a></li>
										<li><a class="submenu" href="/tnokco/newEquipment"><i class="">E</i><span class="hidden-sm"> &nbsp;&nbsp; Equipmment Instances</span></a></li>
									</ul>	
								</li>
								
								<li>
									<a class="dropmenu" href="#"><i class="icon-lightbulb"></i><span class="hidden-sm"> Functionalites</span> <span class="label">3</span></a>
									<ul class="dropmenuLevel2">								
										
										<li><a class="submenu" href="/tnokco/list"><i class="icon-ok-circle"></i><span class="hidden-sm">&nbsp;&nbsp; Knowledge Completion <br> &nbsp;&nbsp; (TN-OKCo)</span></a></li>
										<li><a class="submenu" href="/tnokco/open_visualizator"><i class="icon-eye-open"></i><span class="hidden-sm">&nbsp;&nbsp; Visualization</span></a></li>
										<li><a class="submenu" href="/tnokco/provisoning_visualization"><i class="icon-play-circle"></i><span class="hidden-sm">&nbsp;&nbsp; Provisioning</span></a></li>
									</ul>	
								</li>
								
								<li>
									<a class="dropmenu" href="#"><i class="icon-save"></i><span class="hidden-sm"> Output Results</span> <span class="label">2</span></a>
									<ul class="dropmenuLevel2">								
										
										<li><a class="submenu" href="/tnokco/getModel"><i class="icon-save"></i><span class="hidden-sm">&nbsp;&nbsp; Save as OWL</span></a></li>
										<li><a class="submenu" href="/tnokco/getCondel"><i class="icon-save"></i><span class="hidden-sm">&nbsp;&nbsp; Save as Condel</span></a></li>
									</ul>	
								</li>
								
							</ul>	
						</li>
					
					
						<li><a href="/tnokco/faq" class=""><i class="icon-question-sign"></i><span
								class="hidden-sm"> FAQ</span></a></li>
								
						<li><a href="/tnokco/about" class=""><i class="icon-exclamation-sign"></i><span
								class="hidden-sm"> About</span></a></li>

					</ul>
				</div>
			</div>
			<!-- end: Main Menu -->

			<!-- start: Content -->
			<div id="content" class="col-lg-10 col-sm-11">