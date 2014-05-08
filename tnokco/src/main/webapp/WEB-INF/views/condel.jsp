<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ page import="br.ufes.inf.padtec.tnokco.business.Code"%> --%>
<%@ page import="java.util.ArrayList"%>

<%
	String condelValue = (String)request.getSession().getAttribute("txtCondelCode");
// 	ArrayList<Code> ListCodes = (ArrayList<Code>)request.getSession().getAttribute("ListCodes");
%>
<%@include file="../templates/header.jsp"%>

<script>
	$(document)
			.ready(
					function() {

						// Function loading
						function loading() {
							var maskHeight = $(document).height();
							var maskWidth = "100%";//$(document).width();

							//Define largura e altura do div#maskforloading iguais ás dimensões da tela
							$('#maskforloading').css({
								'width' : maskWidth,
								'height' : maskHeight
							});

							//efeito de transição
							$('#maskforloading').show();
						}

						$('.btn-load').click(function(event) {

							window.location.href = "/tnokco/welcome";

						}); //End load condel file


						$('#getCondelForm')
						.submit(
								function(event) {

									loading();

									event.preventDefault();

									try {

										//Used to get the value of the CodeMirror editor
										var result = condelParser.parse(editor.getValue());
										
										var json = {
												
											"ok" : true,
											"result" : result											
										};

										$
												.ajax({
													url : $(
															"#getCondelForm")
															.attr("action"),
													data : JSON
															.stringify(json),
													type : "POST",
													contentType : "application/json; charset=utf-8",
													dataType : "json",

													success : function(
															dto) {

														$(
																"#maskforloading")
																.hide();

														if (dto.ok == false) {
															var html = "<div class=\"alert alert-danger\">"
																	+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
																	+ "<strong>"
																	+ "</strong>"
																	+ dto.result
																	+ "</div>";

															$(
																	"#boxerro")
																	.prepend(
																			html);

														} else {

															//window.location.href = "getCondel";
															 window.open('getCondel','_blank'); 
															

														}

													},
													error : function(x,
															e) {

														$(
																"#maskforloading")
																.hide();
														alert("Erro");
													},
												});

									} catch (e) {

										$("#maskforloading").hide();

										var html = "<div class=\"alert alert-danger\">"
												+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
												+ "<strong>"
												+ "</strong>"
												+ e.message + "</div>";

										$("#boxerro").prepend(html);

									}
								});

						$('.btn-clean')
								.click(
										function(event) {

											$
													.ajax({
														url : "cleanCondelCode",
														type : "GET",
														success : function(dto) {

															if (dto.ok == true)
																window.location.href = "/tnokco/condel";

														},
														error : function(x, e) {

															alert("Problem to clean condel code");
														},
													});

										}); //End load condel file

						$('.btn-save')
								.click(
										function(event) {

											loading();

											try {

												//Used to get the value of the CodeMirror editor
												var result = condelParser.parse(editor.getValue());

												var json = {
													"result" : result,
													"ok" : true,
												};

												$
														.ajax({
															url : "saveCondelCode",
															data : JSON
																	.stringify(json),
															type : "GET",
															contentType : "application/json; charset=utf-8",
															dataType : "json",

															success : function(dto) {

																$(
																		"#maskforloading")
																		.hide();

																if (dto.ok == false) {
																	var html = "<div class=\"alert alert-danger\">"
																			+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
																			+ "<strong>"
																			+ "</strong>"
																			+ dto.result
																			+ "</div>";

																	$(
																			"#boxerro")
																			.prepend(
																					html);

																} else {

																	//ok

																	var html = "<li class=\"active\"><a id=\"" + +"\" href=\"#\">" + "tf:x;" + "</a></li>";
																	$("#TabCode").prepend(html);

																}

															},
															error : function(x,
																	e) {

																$(
																		"#maskforloading")
																		.hide();
																alert("Erro");
															},
														});

											} catch (e) {

												$("#maskforloading").hide();

												var html = "<div class=\"alert alert-danger\">"
														+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
														+ "<strong>"
														+ "</strong>"
														+ e.message + "</div>";

												$("#boxerro").prepend(html);

											}

										}); //End save
										
										$('.btn-select')
										.click(
												function(event) {

													loading();

														$.ajax({
																	url : "selectCondelCode?id=" + $(this).attr("id"),
																	type : "GET",
																	contentType : "application/json; charset=utf-8",
																	dataType : "json",

																	success : function(dto) {

																		$(
																				"#maskforloading")
																				.hide();

																		if (dto.ok == false) {
																			var html = "<div class=\"alert alert-danger\">"
																					+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
																					+ "<strong>"
																					+ "</strong>"
																					+ dto.result
																					+ "</div>";

																			$(
																					"#boxerro")
																					.prepend(
																							html);

																		} else {

																			//SELECT THE RIGHT CODE
																			

																		}

																	},
																	error : function(x,
																			e) {

																		$(
																				"#maskforloading")
																				.hide();
																		alert("Erro");
																	},
																});

												}); //End select

						$('#condelForm')
								.submit(
										function(event) {

											loading();

											event.preventDefault();

											try {

												//Used to get the value of the CodeMirror editor
												var result = condelParser.parse(editor.getValue());

												var json = {
													"result" : result,
													"ok" : true
												};

												$
														.ajax({
															url : $(
																	"#condelForm")
																	.attr(
																			"action"),
															data : JSON
																	.stringify(json),
															type : "POST",
															contentType : "application/json; charset=utf-8",
															dataType : "json",

															success : function(
																	dto) {

																$(
																		"#maskforloading")
																		.hide();

																if (dto.ok == false) {
																	var html = "<div class=\"alert alert-danger\">"
																			+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
																			+ "<strong>"
																			+ "</strong>"
																			+ dto.result
																			+ "</div>";

																	$(
																			"#boxerro")
																			.prepend(
																					html);

																} else {

																	//load list instances
																	window.location.href = "list";
																	//window.location.href = "open_visualizator";

																}

															},
															error : function(x,
																	e) {

																$(
																		"#maskforloading")
																		.hide();
																alert("Erro");
															},
														});

											} catch (e) {

												$("#maskforloading").hide();

												var html = "<div class=\"alert alert-danger\">"
														+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
														+ "<strong>"
														+ "</strong>"
														+ e.message + "</div>";

												$("#boxerro").prepend(html);

											}
										});

					}); // end document read
	// Code Mirror
</script>

<div id="boxerro"></div>

<h1>Condel Editor</h1>

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>Condel input
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>

			<div class="box-content">
				<ul class="nav tab-menu nav-tabs" style="padding-right: 24px;"
					id="TabCode">

					<%
													
// 					for (Code cod : ListCodes) 
// 					{
// 						out.println("<li class=\"\"><a class=\"btn-select\" href=\"#\" id=\"" + cod.id + "\">" + cod.name + "</a></li>");	
// 					}
					
					%>


					<li class="active"><a id="0" href="#">Some Code</a></li>
				</ul>

				<!-- ------------------- -->
				<!-- Content Condel here -->
				<!-- ------------------- -->
				

					<div style="border: 1px solid black; width: 100%;">

						<!-- TEXTA AREA -->
						<%
							if(condelValue == null)
												{
													out.println("<textarea id=\"code\" name=\"code\" style=\"width: 100%; padding:5px;\">");
													out.println("</textarea>");
												}else {
													
													out.println("<textarea id=\"code\" name=\"code\" style=\"width: 100%; padding:5px;\">");
													out.println(condelValue);
													out.println("</textarea>");
													
												}
						%>
						<!-- TEXTA AREA -->

						
						<form id="getCondelForm" style="float:left" method="POST" action="getCondelCode">
							<input type="button" class="btn btn-pre btn-load" value="Load file" /> 
							<input type="button" class="btn btn-pre btn-clean" value="Clean" />
							<input type="submit" class="btn btn-pre btn-export" value="Save File" />
						</form>
						  
						<!-- <input type="button" class="btn btn-pre btn-save" value="Save" /> --> 
					
						<form id="condelForm" style="float:left; margin-left:3px;" method="POST" action="runCondel">
							<input type="submit" class="btn btn-pre" value="Run" />
						</form>
						
						<div style="clear:both;"></div>

						<script>
							CodeMirror.commands.autocomplete = function(cm) {
								CodeMirror.showHint(cm,
										CodeMirror.hint.condel_hint);
							};
							var editor = CodeMirror.fromTextArea(document
									.getElementById("code"), {
								lineNumbers : true,
								matchBrackets : true,
								mode : "text/condel",
								extraKeys : {
									"Ctrl-Space" : "autocomplete"
								}
							});
							editor.setOption("theme", "neat");
						</script>
						<div id="res"></div>
						<div id="res2" style="border: 2px solid black;"></div>

						<!-- ------------------------ -->
						<!-- END -Content Condel here -->
						<!-- ------------------------ -->

					</div>

				

			</div>

			<div class="row">
				<div class="col-lg-12">
					<h2>
						How can you use <i>Condel?</i>
					</h2>
					<p>Instructions:</p>
					<div class="tooltip-demo well">
						<p class="muted" style="margin-bottom: 0;">
							The Complete Network Description Language (Condel) is a network declaration Domain
							Specific Language that can be used by the Ontology-based Network Advisor as source of input,
							providing network declarations. It was designed to reduce information loss between the
							network declaration and the Advisor's knowledge base.<br>
							The Condel Editor provides syntax checking, highlight keywords, and auto-completion
							(pressing ctrl + space).<br>
							Once a network description is edited, press Run.
						</p>
					</div>
				</div>
			</div>

		</div>

	</div>

</div>
<!--/col-->

</div>
<!--/row-->

<%@include file="../templates/footer.jsp"%>