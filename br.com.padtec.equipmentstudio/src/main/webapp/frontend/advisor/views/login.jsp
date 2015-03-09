<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- start: Meta -->
<meta charset="utf-8">
<title>Ontology-based Network Advisor</title>
<meta name="description" content="Ontology-based Network Advisor">
<meta name="author"
	content="Fábio Coradini, Pedro Paulo Barcelos, Vitcor Amorim, Freddy Brasileiro, Cássio Reginato">
<!-- end: Meta -->

<!-- start: Mobile Specific -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- end: Mobile Specific -->

<!-- start: CSS -->

<link href="frontend/advisor/css/bootstrap.min.css" rel="stylesheet">
<link href="frontend/advisor/css/style.min.css" rel="stylesheet">
<link href="frontend/advisor/css/retina.min.css" rel="stylesheet">

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
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="frontend/advisor/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="frontend/advisor/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="frontend/advisor/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="frontend/advisor/ico/apple-touch-icon-57-precomposed.png">
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

</head>
<%
	String login = (String)request.getSession().getAttribute("login");
	if(login == null)
	{
		login = "";
	}
%>

<body>
	<div class="container">
		<div class="row">

			<div class="row">
				<div class="login-box">

					<a class="navbar-brand col-lg-2 col-sm-1 col-xs-12" href="#"
						style="position: static; width: 100%; margin-bottom: 10px; font-size: 23px; -webkit-border-radius: 2px; -moz-border-radius: 2px; border-radius: 2px;">
						<span>Ontology-based Network Advisor</span>
					</a>

					<h2>Login</h2>
					<form class="form-horizontal" action="login.htm"
						enctype="multipart/form-data" method="post">
						<fieldset>

							<input class="input-large col-xs-12" name="username"
								id="username" type="text" placeholder="type username" /> <input
								class="input-large col-xs-12" name="password" id="password"
								type="password" placeholder="type password" />

							<div class="clearfix"></div>

							<button type="submit" class="btn btn-primary col-xs-12">Login</button>
						</fieldset>

						<%
						
						if(login.equals("false"))
						{
							out.println("<hr>");
							out.println("<h3 style=\"color:red\">Invalid Username or Password.</h3>");
						}
						
						%>

					</form>
					<hr>

				</div>
			</div>
			<!--/row-->

		</div>
		<!--/row-->

	</div>
	<!--/container-->

</body>
</html>


