<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@include file="/frontend/advisor/templates/header.jsp"%>
<%
	ArrayList<String> equipements = (ArrayList<String>) request.getSession().getAttribute("equipments");
	ArrayList<String> sites = (ArrayList<String>) request.getSession().getAttribute("sites");
	HashMap<String, ArrayList<String>> g800 = (HashMap<String, ArrayList<String>>) request.getSession().getAttribute("g800");
%>
<script>
<% 
	if(!sites.isEmpty())
		out.println("strUser = '"+sites.get(0).substring(sites.get(0).indexOf("#")+1)+"'");
	if(!equipements.isEmpty())
	out.println("strUserEquip = '"+equipements.get(0).substring(equipements.get(0).indexOf("#")+1)+"'");
%>
function changedSiteCB(){
	e = document.getElementById("selected");
	strUser = e.options[e.selectedIndex].value;
}
function changedEquipmentCB(){
	e = document.getElementById("selected_equip");
	strUserEquip = e.options[e.selectedIndex].value;
}
</script>

<div class="row">

	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>Visualization Options
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
									out.println("<td style=\"margin-left:15px;\">No site available");
								}else{ %>
									<td style="padding-left: 30px;">
										<a class="btn btn-success" 
											href="open_network_visualization.htm?visualization=allSites"> <i
												class="icon-zoom-in"></i>
										</a> 
								<% }
							%>
						</td>
					</tr>
					<tr>
						<td><b>All Equipment</b></td>
						<%
							if(equipements.isEmpty()){
								out.println("<td style=\"margin-left:15px;\">No Equipments available");
							}else{ %>
								<td style="padding-left: 30px;">
									<a class="btn btn-success" 
										href="open_network_visualization.htm?visualization=allEquipments"> <i
											class="icon-zoom-in"></i>
									</a> 
							<% }
						%>
					</tr>
					<tr>
						<td><b>All G.800 Elements</b></td>
							<%
								if(g800.isEmpty()){
									out.println("<td style=\"margin-left:15px;\">No G800 elements available");
								}else{ %>
									<td style="padding-left: 30px;">
										<a class="btn btn-success" 
											href="open_network_visualization.htm?visualization=allG800"> <i
												class="icon-zoom-in"></i>
										</a> 
								<% }
							%>
					</tr>
					<tr>
						<td>
							<b>Elements from a specific Site</b>
						</td>
						<td>
							
									<% 
										if(sites.isEmpty()){
									%>
											No site available
									<%
										}else{
											%>
											<table style="border-collapse:collapse; border:0; margin-left:15px;">
												<tr>
													<td style="border-top:0;">
														<a class="btn btn-success" 
															href="open_equipment_visualization_from_site.htm" onclick="this.href = 'open_equipment_visualization_from_site.htm?selected='+strUser"> <i
																class="icon-zoom-in"></i>
														</a>
													</td>
													<td style="border-top:0;">
											<%
												out.println("<select name=\"selected\" id=\"selected\" value=\"Select some site\" onchange=\"changedSiteCB()\">");
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
						<td>
								<% 
									if(equipements.isEmpty()){
								%>
										No equipment available
								<%
									}else{
										%>
										<table style="border-collapse:collapse; border:0; margin-left:15px;">
											<tr>
												<td style="border-top:0;">
													<a class="btn btn-success" 
														href="open_g800_visualization_from_equip.htm" onclick="this.href = 'open_g800_visualization_from_equip.htm?selected='+strUserEquip"> <i
															class="icon-zoom-in"></i>
													</a>
												</td>
												<td style="border-top:0;">
										<%
											out.println("<select name=\"selected_equip\" id=\"selected_equip\" value=\"Select some Equipment\" onchange=\"changedEquipmentCB()\">");
											for(int i = 0; i < equipements.size(); i++){
												out.println("<option value=\""+equipements.get(i).substring(equipements.get(i).indexOf("#")+1)+"\"  "+((i == 0)?"selected_equip":"")+" >"+equipements.get(i).substring(equipements.get(i).indexOf("#")+1)+"</option>");
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
						<td><b>All network information</b></td>
						<td style="padding-left: 30px;">
							<a class="btn btn-success" 
								href="open_network_visualization.htm?visualization=allElements"> <i
									class="icon-zoom-in" ></i>
							</a> 
						</td>
					</tr>
					
				</table>

			</div>
		</div>
	</div>
	<!--/col-->

		<div class="col-lg-12">
			<p>Instructions:</p>
			<div class="tooltip-demo well">
				<p class="muted" style="margin-bottom: 0;">
					Select the desired visualization from the six options presented above. The visualization represents the network elements and its relations.
				</p>
			</div>
		</div>

</div>
<%@include file="/frontend/advisor/templates/footer.jsp"%>