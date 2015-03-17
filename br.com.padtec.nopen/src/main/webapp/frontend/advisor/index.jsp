<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	//Get the parameters from controller
	String error = (String) request.getSession().getAttribute(
			"errorMensage");
	String loadOk = (String) request.getSession()
			.getAttribute("loadOk");
%>

<%@include file="/frontend/advisor/templates/header.jsp"%>

<script>
	$(document).ready(function() {

		$('.btn-editor-sindel').click(function(event) {

			window.location.href = "sindel";

		}); //End load sindel file

		//Chose pellet
		$('#optionsRadios1').click(function(event) {

			//Pellet
			
			$.ajax({
				url : "selectReasoner" + "?reasoner=" + "pellet",
				//data : JSON.stringify(json),
				type : "GET",

				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function(result) {

					if(result == "error")
					{
						var html = "<div class=\"alert alert-danger\">"
							+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
							+ "<strong>"
							+ "</strong>"
							+ "Error: Select avalible reasoner." 
							+ "</div>";

					$(
							"#boxerro")
							.prepend(
									html);
					}

				}
			});	

		});

		//Chose hermit
		$('#optionsRadios2').click(function(event) {

			$.ajax({
				url : "selectReasoner" + "?reasoner=" + "hermit",
				//data : JSON.stringify(json),
				type : "GET",

				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function(result) {

					if(result == "error")
					{
						var html = "<div class=\"alert alert-danger\">"
							+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
							+ "<strong>"
							+ "</strong>"
							+ "Error: Select avalible reasoner." 
							+ "</div>";

					$(
							"#boxerro")
							.prepend(
									html);
					}

				}
			});

		});

		//Chose load first
		$('#loadReasonerFirstCheckbox').click(function(event) {
			
			if($("#loadReasonerFirstCheckbox").prop('checked'))
			{
				
				//Check

				$.ajax({
					url : "loadFirst" + "?loadFirst=" + "true",
					//data : JSON.stringify(json),
					type : "GET",

					beforeSend : function(xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					},
					success : function(result) {

						if(result == "error")
						{
							var html = "<div class=\"alert alert-danger\">"
								+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
								+ "<strong>"
								+ "</strong>"
								+ "Error: Check/Uncheck problem happens. Try again." 
								+ "</div>";

						$(
								"#boxerro")
								.prepend(
										html);
						}

					}
				});
			}
			
			else {
				
				
				//Uncheck
				
				$.ajax({
					url : "loadFirst" + "?loadFirst=" + "false",
					//data : JSON.stringify(json),
					type : "GET",

					beforeSend : function(xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					},
					success : function(result) {

						if(result == "error")
						{
							var html = "<div class=\"alert alert-danger\">"
								+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
								+ "<strong>"
								+ "</strong>"
								+ "Error: Check/Uncheck problem happens. Try again." 
								+ "</div>";

						$(
								"#boxerro")
								.prepend(
										html);
						}

					}
				});
			}

		});
		

	}); //end document ready
</script>

<%
	if (error != null && !error.equals("")) {
		String htmlError = "<div class=\"alert alert-danger\">"
				+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>"
				+ "<strong>" + "Error! " + "</strong>" + error
				+ "</div>";
		out.println(htmlError);
	}

	if (loadOk != null && !loadOk.equals("true")) {
		String htmlLoad = "<div class=\"alert alert-info\">"
				+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>"
				+ "<strong>Hey!</strong> Tou need to load owl file first or run Sindel."
				+ "</div>";
		out.println(htmlLoad);
	}
%>


<h1>Welcome to the Network Advisor</h1>

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>
				</h2>
				<div class="box-icon">
					<a
						href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				<div class="row">
					<div class="col-lg-12">
						
						<h2>Reasoner settings:</h2>
						<div class="tooltip-demo well">

							<div class="controls" style="margin-bottom:8px">

								<label class="radio"> <span class=""><input
										type="radio" name="optionsReasoner" id="optionsRadios1"
										value="pellet"></span> Pellet
								</label>
								<div style="clear: both"></div>
								<label class="radio"> <span class="checked"><input
										type="radio" name="optionsReasoner" id="optionsRadios2"
										value="hermit" checked="checked"></span> Hermit
								</label>
								<div style="clear: both"></div>

							</div>
							
							<label
								class="checkbox inline">
								
									<input type="checkbox" name="loadReasonerFirstCheckbox"
										id="loadReasonerFirstCheckbox" />
									 Use reasoner in load
							</label>
										
									
						</div>
						<h2>Network Input Information:</h2>
						<div class="tooltip-demo well">
							<ol>
								<!-- Sindel -->
								
								<li >Load Sindel file.
									<form action="uploadSindel.htm" class="form-horizontal"
										enctype="multipart/form-data" method="POST">
										<div class="controls">
											<input name="file" type="file"> <input type="submit" class="btnload"
												name="submit" value="Upload" />
											
										</div>
										
									</form>
								</li>
								<li style="margin-top: 20px;" style="margin-top: 10px;">
									<div>Write and load a Sindel description</div>
									<button type="button" class="btn btn-pre btn-editor-sindel">
										<i class="icon-arrow-right"></i> Go to editor
									</button>

								</li>
								
								<!-- OKCo -->
								
								<li style="margin-top: 20px;">
									<form action="uploadOwl.htm" class="form-horizontal"
										enctype="multipart/form-data" method="POST">
										
										<div>Load OWL file:</div>
										<div class="controls">
											<input name="file" type="file"> <input type="submit" class="btnload"
												name="submit" value="Upload" />
										</div>
										
									</form>
								</li>
								
							</ol>
						</div>

						<div class="row">
							<div class="col-lg-12">
								<p>Instructions:</p>
								<div class="tooltip-demo well">
								
									<p class="muted" style="margin-bottom: 0;">
									In the Start Page two settings must be provided: the reasoner settings and the Network Input Information.
									</p>
										<br>
									<p class="muted" style="margin-bottom: 0;">
										<b>Reasoner settings:</b>
										<br>
										The Ontology-based Network Advisor uses the OWL reasoners capabilities of classification, consistency checking and inference to 
										support the Advisor's functionalities. Two different reasoner options are provided: <a target="_blank" href="http://clarkparsia.com/pellet/">Pellet (version 2.3.1)</a> and <a target="_blank" href="http://hermit-reasoner.com/">HermiT (version 1.3.8)</a>.
										<br> 
										The existence of two different options is justified by the different characteristics of these reasoners. 
										Pellet implements a tableau-based algorithm, while HermiT implements a faster algorithm, the hypertableau calculus <a target="_blank" href="http://semantic-web-journal.org/sites/default/files/swj120.pdf">[reference]</a>. 
										However, Pellet has an incremental reasoning, i.e., it supports incremental classification and incremental consistency check for additions and removals <a target="_blank" href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.153.8799&rep=rep1&type=pdf">[reference]</a>.
									</p>
										<br>
									<p class="muted" style="margin-bottom: 0;">
										<b>Network Input Information:</b>
										<br>
										Network information must be provided to the Ontology-based Network Advisor. 
										The specific functionalities of the Network Advisor will process the provided network information.
										</br>
										The information can be provided in five different ways: 
										(i) by loading a file with a Sindel specification, 
										(ii) by writing a Sindel specification, 
										(iii) by loading an OWL file with instances (be careful: this action will overwrite the application's default knowledge-base).
										
									</p>
									
									
									</p>
							</div>
						</div>
						<!-- /row -->
					</div>
				</div>

			</div>

		</div>

	</div>
	<!--/col-->

</div>
<!--/row-->

<%@include file="/frontend/advisor/templates/footer.jsp"%>