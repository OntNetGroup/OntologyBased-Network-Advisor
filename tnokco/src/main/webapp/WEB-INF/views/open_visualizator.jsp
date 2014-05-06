<%@page import="java.util.ArrayList"%>
<%@include file="../templates/header.jsp"%>
<%
	ArrayList<String> sites = (ArrayList<String>) request.getSession().getAttribute("sites");
%>
<script>
<% 
	if(!sites.isEmpty())
		out.println("strUser = '"+sites.get(0).substring(sites.get(0).indexOf("#")+1)+"'");
%>
function changedSiteCB(){
	e = document.getElementById("selected_site");
	strUser = e.options[e.selectedIndex].value;
}
</script>

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
						
							<%
								if(sites.isEmpty()){
									out.println("<td style=\"margin-left:15px;\">No sites");
								}else{ %>
									<td style="padding-left: 30px;">
										<a class="btn btn-success" 
											href="/tnokco/open_network_visualization?visualization=allSites"> <i
												class="icon-zoom-in"></i>
										</a> 
								<% }
							%>
						</td>
					</tr>
					<tr>
						<td><b>All Equipment</b></td>
						<td style="padding-left: 30px;">
							<a class="btn btn-success" 
								href="/tnokco/open_equipment_visualization"> <i
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
						<td>
							<b>Elements from a specific Site</b>
						</td>
						<td>
							
									<% 
										if(sites.isEmpty()){
									%>
											No sites
									<%
										}else{
											%>
											<table style="border-collapse:collapse; border:0; margin-left:15px;">
												<tr>
													<td style="border-top:0;">
														<a class="btn btn-success" 
															href="/tnokco/open_equipment_visualization_from_site" onclick="this.href = '/tnokco/open_equipment_visualization_from_site?selected_site='+strUser"> <i
																class="icon-zoom-in"></i>
														</a>
													</td>
													<td style="border-top:0;">
											<%
												out.println("<select name=\"selected_site\" id=\"selected_site\" value=\"Select some site\" onchange=\"changedSiteCB()\">");
												for(int i = 0; i < sites.size(); i++){
													out.println("<option value=\""+sites.get(i).substring(sites.get(i).indexOf("#")+1)+"\"  "+((i == 0)?"selected":"")+" >"+sites.get(i).substring(sites.get(i).indexOf("#")+1)+"</option>");
												}
												out.println("</select>");
											%>
													</td>
												</tr>
											</table> 
											<%
										}
									%> 
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