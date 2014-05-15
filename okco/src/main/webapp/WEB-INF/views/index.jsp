<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	// Get the parameters from controller
	String error = (String)request.getSession().getAttribute("errorMensage");
	String loadOk = (String)request.getSession().getAttribute("loadOk");
%>

<%@include file="../templates/header.jsp"%>


<script type="text/javascript">

	$(document).ready(function() {

		
		
	}); // End - document ready;

</script>

<%
		if(error != null && !error.equals(""))
		{
			String htmlError = "<div class=\"alert alert-danger\">" +
					"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>" + 
					"<strong>" + "Error! " + "</strong>"+ error + 
				"</div>";
			out.println(htmlError);
		}
	
		if(loadOk != null && !loadOk.equals("true"))
		{
			String htmlLoad = "<div class=\"alert alert-info\">" +
				"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>" +
				"<strong>Hey!</strong> You need to load owl first." +
			"</div>";
			out.println(htmlLoad);
		}
	
	%>

<h1>Welcome to W-OKCo</h1>

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>OKCo configuration
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-setting"><i class="icon-wrench"></i></a> <a
						href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				<form action="upload" class="form-horizontal"
					enctype="multipart/form-data" method="POST">
					<fieldset class="col-sm-12">

						<div class="form-group">
							<label class="control-label">Select Reasoner:</label>
							<div class="controls">

								<label class="radio"> <span class="checked"><input
										type="radio" name="optionsReasoner" id="optionsRadios1"
										value="pellet" checked="checked"></span> Pellet
								</label>
								<div style="clear: both"></div>
								<label class="radio"> <span class=""><input
										type="radio" name="optionsReasoner" id="optionsRadios2"
										value="hermit" ></span> Hermit
								</label>

							</div>
							<br /> <label class="checkbox inline"> <input
								type="checkbox" name="loadReasonerFirstCheckbox"
								id="loadReasonerFirstCheckbox" checked="checked" /> Use
								reasoner in load
							</label> <br /> <label class="control-label">File Upload:</label>
							<div class="controls">
								<input name="file" type="file"> <input type="submit" class="btnload"
									name="submit" value="Upload" />
							</div>
						</div>

					</fieldset>
				</form>

			</div>
		</div>
	</div>
	<!--/col-->

</div>
<!--/row-->

<div class="row">
	<div class="col-lg-12">
		<p>Description of OKCo:</p>
		<div class="tooltip-demo well">
			<p class="muted" style="margin-bottom: 0;">
				Tight pants next level keffiyeh <a href="#" data-rel="tooltip"
					data-original-title="first tooltip">you probably</a> haven't heard
				of them. Photo booth beard raw denim letterpress vegan messenger bag
				stumptown. Farm-to-table seitan, mcsweeney's fixie sustainable
				quinoa 8-bit american appadata-rel <a href="#" data-rel="tooltip"
					data-original-title="Another tooltip">have a</a> terry richardson
				vinyl chambray. Beard stumptown, cardigans banh mi lomo thundercats.
				Tofu biodiesel williamsburg marfa, four loko mcsweeney's cleanse
				vegan chambray. A <a href="#" data-rel="tooltip"
					data-original-title="Another one here too">really ironic</a>
				artisan whatever keytar, scenester farm-to-table banksy Austin <a
					href="#" data-rel="tooltip" data-original-title="The last tip!">twitter
					handle</a> freegan cred raw denim single-origin coffee viral.
			</p>
		</div>
	</div>
</div>
<!-- /row -->

<%@include file="../templates/footer.jsp"%>