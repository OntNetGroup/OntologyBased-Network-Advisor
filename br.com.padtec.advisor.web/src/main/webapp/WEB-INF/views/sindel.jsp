<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ page import="br.ufes.inf.padtec.tnokco.business.Code"%> --%>
<%@ page import="java.util.ArrayList"%>

<%
	String sindelValue = (String)request.getSession().getAttribute("txtSindelCode");
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

						var open = 1;
						$(".uploader").hide();
						$(".btnUpload").hide();
						$('.btn-load').click(function(event) {

							if(open == 1)
							{
								$(".uploader").show();
								$(".btnUpload").show();
								open = 0;	
							} else {
								$(".uploader").hide();
								$(".btnUpload").hide();
								open = 1;
							}
							

						}); //End load sindel file


						$('#getSindelForm')
						.submit(
								function(event) {

									loading();

									event.preventDefault();
									var sReturn = "";

									try {

										//clean up hashs
										cleanUpHashs();

										//Used to get the value of the CodeMirror editor
										parser.parse(editor.getValue());
										
										var json = {
												
											"ok" : true,
											"result" : editor.getValue()											
										};

										$
												.ajax({
													url : $(
															"#getSindelForm")
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

															window.location.href = "getSindel";
															window.open('getSindel','_blank'); 
															

														}

													},
													error : function(x,
															e) {

														$("#maskforloading").hide();
														alert("Error");
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
														url : "cleanSindelCode",
														type : "GET",
														success : function(dto) {

															if (dto.ok == true)
																window.location.href = "/tnokco/sindel";

														},
														error : function(x, e) {

															alert("Problem to clean sindel code");
														},
													});

										}); //End load sindel file

						$('.btn-save')
								.click(
										function(event) {

											loading();

											var sReturn = "";

											try {

												//clean up hashs
												cleanUpHashs();

												//Used to get the value of the CodeMirror editor
												parser.parse(editor.getValue());

												sReturn = printHashTypeVar(hashTypeVar);
												sReturn += printHashRelations(hashRelation);
												sReturn += printHashAttribute(hashAttribute);

												var json = {
													"result" : sReturn,
													"ok" : true,
												};

												$
														.ajax({
															url : "saveSindelCode",
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
																	url : "selectSindelCode?id=" + $(this).attr("id"),
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

						$('#sindelForm')
								.submit(
										function(event) {

											loading();

											event.preventDefault();
											var sReturn = "";

											try {

												//clean up hashs
												cleanUpHashs();

												//Used to get the value of the CodeMirror editor
												parser.parse(editor.getValue());

												sReturn = printHashTypeVar(hashTypeVar);
												sReturn += printHashRelations(hashRelation);
												sReturn += printHashAttribute(hashAttribute);

												var json = {
													"result" : sReturn,
													"ok" : true
												};

												$
														.ajax({
															url : $(
																	"#sindelForm")
																	.attr(
																			"action"),
															data : JSON
																	.stringify(json),
															type : "POST",
															contentType : "application/json; charset=utf-8",
															dataType : "json",

															success : function(dto) {

																$("#maskforloading").hide();

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

																	/*var html = "<div class=\"alert alert-warning\">"
																		+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">-</button>"
																		+ dto.result + "<br>"
																		+ "<strong>"
																		+ "Go to Transport Network OWL Knowledge Completer (TN-OKCo) <a href=\"list\">Go</a>"
																		+ "</strong>"
																		+ "</div>";

																$("#boxerro").prepend(html); */
																
																	//load list instances
																	if(dto.result != "")
																	{
																		alert("Sindel tranformation messages:" + " " + dto.result);
																	}
																	
																	window.location.href = "list";

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

	function cleanUpHashs() {
		
		currentLine = 0;

		warning = "";

		hashVarType = new Array();
		hashTypeVar = new Array();
		hashUsedRelation = new Array();

		hashUsedVariable = new Array();

		hashComposition = new Array();

		hashRelation = new Array();
		hashRelation["binds"] = new Array();
		hashRelation["connects"] = new Array();
		hashRelation["maps"] = new Array();
		hashRelation["client"] = new Array();
		hashRelation["component_of"] = new Array();

		hashAttribute = new Array();
		hashAttribute['str_location'] = new Array();
		hashAttribute['geo_location'] = new Array();
		hashAttribute['tf_type'] = new Array();
	}

	function printHashTypeVar(hash) {
		
		var s = "elements" + "#";

		for ( var key in hash) {
			s += key + ":" + hash[key];
			s = s.substring(0, s.length) + ";";
		}
		s += "!";
		return s;
	}

	function printHashRelations(hash) {
		
		var s = "";

		for ( var key in hash) {
			if (key == "component_of") {
				s += key + "#";
				for (var i = 0; i < hash[key].length; i++) {
					s += hash[key][i] + ";";
				}
				s = s.substring(0, s.length - 1);
				s += "!";
			} else {
				s += key + "#";
				s += hash[key] + "!";
			}
		}
		return s;
	}

	function printHashAttribute(hash) {
		var s = "";

		for ( var key in hash) {
			s += key + "#";
			for (var i = 0; i < hash[key].length; i++) {
				s += hash[key][i] + ";";
			}
			s = s.substring(0, s.length - 1);
			s += "!";
		}
		return s;
	}

	// Code Mirror
</script>

<div id="boxerro"></div>

<h1>Sindel Editor</h1>

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>Sindel input
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


					<li class="active"><a id="0" href="#">Code</a></li>
				</ul>

				<!-- ------------------- -->
				<!-- Content Sindel here -->
				<!-- ------------------- -->
				

					<div style="border: 1px solid black; width: 100%;">

						<!-- TEXTA AREA -->
						<%
							if(sindelValue == null)
												{
													out.println("<textarea rows=\"20\" id=\"code\" name=\"code\" style=\"width: 100%; padding:5px;\">");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("\n");
													out.println("</textarea>");
												}else {
													
													out.println("<textarea id=\"code\" name=\"code\" style=\"width: 100%; padding:5px;\">");
													out.println(sindelValue);
													out.println("</textarea>");
													
												}
						%>
						<!-- TEXTA AREA -->

						<form action="uploadSindel" style="float:left; margin-right:5px;" class="form-horizontal" enctype="multipart/form-data" method="POST">
						
								<input type="button" class="btn btn-pre btn-load" value="Open file" /> 
							
								<div class="uploader">
									<input name="file" type="file"><span class="filename" style="-webkit-user-select: none;">No file selected</span>
									<span class="action" style="-webkit-user-select: none;">Choose File</span>
									
								</div>
								
								<input type="submit" class="btnUpload" name="submit" value="Upload" /> 
								
								
							
						</form>
						
						<form id="getSindelForm" style="float:left" method="POST" action="getSindelCode">
							<input type="button" class="btn btn-pre btn-clean" value="Clean" />
							<input type="submit" class="btn btn-pre btn-export" value="Save File" />
						</form>
						  
						<!-- <input type="button" class="btn btn-pre btn-save" value="Save" /> --> 
					
						<form id="sindelForm" style="float:left; margin-left:3px;" method="POST" action="runSindel">
							<input type="submit" class="btn btn-pre" value="Run" />
						</form>
						
						<div style="clear:both;"></div>

						<script>
							CodeMirror.commands.autocomplete = function(cm) {
								CodeMirror.showHint(cm,
										CodeMirror.hint.sindel_hint);
							};
							var editor = CodeMirror.fromTextArea(document
									.getElementById("code"), {
								lineNumbers : true,
								matchBrackets : true,
								mode : "text/sindel",
								extraKeys : {
									"Ctrl-Space" : "autocomplete"
								}
							});
							editor.setOption("theme", "neat");
						</script>
						<div id="res"></div>
						<div id="res2" style="border: 2px solid black;"></div>

						<!-- ------------------------ -->
						<!-- END -Content Sindel here -->
						<!-- ------------------------ -->

					</div>

				

			</div>

			<div class="row">
				<div class="col-lg-12">
					<h2>
						How can you use <i>Sindel?</i>
					</h2>
					<p>Instructions:</p>
					<div class="tooltip-demo well">
						<p class="muted" style="margin-bottom: 0;">
							The Simple Network Description Language (Sindel) is a network declaration Domain Specific
							Language built over the Advisor's knowledge base. The Sindel language is intended to be an 
							easy and quick way to instantiate the ontology with a network description.<br>
							The Sindel's editor provides semantic checking, syntax checking, highlight keywords, and auto-
							completion (pressing ctrl + space). <br>
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