<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	//Get the parameters from controller
	String error = (String) request.getSession().getAttribute(
			"errorMensage");
	String loadOk = (String) request.getSession()
			.getAttribute("loadOk");
%>

<%@include file="../templates/header.jsp"%>

<script>
	$(document).ready(function() {

		$('.btn-editor-sindel').click(function(event) {

			window.location.href = "/tnokco/sindel";

		}); //End load sindel file

		$('.btn-editor-condel').click(function(event) {

			window.location.href = "/tnokco/condel";

		}); //End load sindel file

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


<h1>Welcome to the Ontology-based Network Advisor</h1>

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
							
									<form action="selectReasoner" class="form-horizontal"
										enctype="multipart/form-data" method="POST">

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
													id="loadReasonerFirstCheckbox" checked="checked" />
												 Use reasoner in load
										</label>
										
										<div class="" style="margin-top: 5px; margin-left:20px">
											 <input type="submit" class="btnload"
												name="submit" value="Select Reasoner" />
												</div>
										
									</form>
						</div>
						<h2>Network Input Information:</h2>
						<div class="tooltip-demo well">
							<ol>
								<!-- Sindel -->
								
								<li >Load Sindel file.
									<form action="uploadSindel" class="form-horizontal"
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
								
								<!-- Condel -->
								
								<li style="margin-top: 20px;">Load Condel file.
									<form action="uploadCondel" class="form-horizontal"
										enctype="multipart/form-data" method="POST">
										<div class="controls">
											<input name="file" type="file"> <input type="submit" class="btnload"
												name="submit" value="Upload" />
										</div>
									</form>
								</li>
								<li style="margin-top: 20px;">
									<div>Write and load a Condel description</div>
									<button type="button" class="btn btn-pre btn-editor-condel">
										<i class="icon-arrow-right"></i> Go to editor
									</button>

								</li>
								
								<!-- OKCo -->
								<li style="margin-top: 20px;">
									<form action="uploadOwl" class="form-horizontal"
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
									<p class="muted" style="margin-bottom: 0;">Lorem ipsum
										dolor sit amet, consectetur adipiscing elit. Ut mattis aliquam
										libero et fermentum. Donec vitae lorem quis mauris imperdiet
										rutrum ut sed arcu. Sed et nisi libero. Nulla ultricies ut
										nulla vitae consectetur. Nunc feugiat tellus sit amet
										fermentum vestibulum. Maecenas volutpat, mauris et hendrerit
										tempor, lectus augue cursus lorem, vel blandit mi ipsum ac
										lectus. Suspendisse in mi mauris. Maecenas venenatis, libero
										eu iaculis venenatis, ante urna tristique lectus, in hendrerit
										sapien eros eget sapien. In eget est egestas, placerat mauris
										sed, ullamcorper urna. Mauris a arcu felis. Maecenas dignissim
										eleifend enim, sed ornare nunc luctus non. Sed eget ultricies
										tortor. Nam viverra commodo lorem, ut ullamcorper ipsum
										viverra et. Aenean viverra enim a turpis bibendum, non mattis
										ante egestas. Donec in mauris ut dolor vestibulum porttitor
										tristique ac dolor. Proin vel bi</p>
								</div>
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

<%@include file="../templates/footer.jsp"%>