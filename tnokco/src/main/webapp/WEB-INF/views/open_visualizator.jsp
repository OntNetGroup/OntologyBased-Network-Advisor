<%@include file="../templates/header.jsp"%>
<div class="row">

	<div style="padding-left: 15px; margin-bottom: 20px;">
		<button onclick="window.location = '/tnokco/welcome';" style="float: left;"
			type="button" class="btn btn-prev">
			<i class="icon-arrow-left"></i> Back to list
		</button>
		<div style="clear: both"></div>
	</div>

	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>Instance informations
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-bordered table-striped">
					<tr>
						<td><b>All Sites</b></td>
						<td style="padding-left: 30px;">
							<a class="btn btn-success" 
								href="/tnokco/open_network_visualization?visualization=allSites"> <i
									class="icon-zoom-in"></i>
							</a> 
						</td>
					</tr>
					<tr>
						<td><b>All Equipment</b></td>
						<td style="padding-left: 30px;">
							<a class="btn btn-success" 
								href="/tnokco/open_network_visualization?"> <i
									class="icon-zoom-in"></i>
							</a> 
						</td>
					</tr>
					<tr>
						<td><b>All G.800 Elements</b></td>
						<td style="padding-left: 30px;">
							<a class="btn btn-success" 
								href="/tnokco/open_network_visualization?"> <i
									class="icon-zoom-in"></i>
							</a> 
						</td>
					</tr>
					<tr>
						<td><b>Elements from a specific Site</b></td>
						<td style="padding-left: 30px;">
							<a class="btn btn-success" 
								href="/tnokco/open_network_visualization?"> <i
									class="icon-zoom-in"></i>
							</a> 
						</td>
					</tr>
					<tr>
						<td><b>G.800 elements from a specific Equipment</b></td>
						<td style="padding-left: 30px;">
							<a class="btn btn-success" 
								href="/tnokco/open_network_visualization?"> <i
									class="icon-zoom-in"></i>
							</a> 
						</td>
					</tr>
				</table>

			</div>
		</div>
	</div>
	<!--/col-->

</div>
<%@include file="../templates/footer.jsp"%>