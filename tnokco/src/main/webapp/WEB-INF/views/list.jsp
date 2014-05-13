<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="br.ufes.inf.nemo.okco.model.Instance" %>
<%@ page import="java.util.ArrayList" %>


<% 
	// Get the parameters from controller
	
	ArrayList<Instance> ListAllInstances = (ArrayList<Instance>)request.getSession().getAttribute("listInstances");
%>

<%@include file="../templates/header.jsp" %>

<script type="text/javascript">

	$(document).ready(function() {

		//Enforce Sub Relations
		$('#EnforceSubRelation').click(function(event) {

			window.location.href = "/tnokco/EnforceSubRelation";
				
		});	//End load sindel file

		// Run reasoner
		$('#runReasonerForm').submit(function(event) {

				$.ajax({
					url : $("#runReasonerForm").attr("action"),
					//data : JSON.stringify(json),
					type : "POST",

					beforeSend : function(xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					},
					success : function(data) {

						if(data.result == "ok")
						{
							//Redirect to instance page
							window.location.href = "list";
							
						} else if(data.result == "nothing") {

							alert("Not happens");
							
						} else {

							//Huston we have a problem
							var html = "<div class=\"alert alert-danger\">" +
											"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
											"<strong>" + "Error! " + "</strong>"+ data.result + 
										"</div>";

							$("#content").prepend(html);
						}
					}
				});

			event.preventDefault();
						
		}); // end submmit
		
	});

</script>

	<h1>Transport Network OWL Knowledge Completer (TN-OKCo)</h1>
	
	<div class="row" style="margin-right:0px;margin-left:0px">
		<div id="boxViewAll" style="float:left">
			<a class="btn btn-success" target="_blank" href="/tnokco/graphVisualizer?typeView=ALL&id=0"> <i class="icon-zoom-in"></i> </a>
			VIEW ALL GRAPH			
		</div>
		
		<form id="runReasonerForm" style="float:right" action="runReasoner" method="POST">

			<button id="EnforceSubRelation" type="button" class="btn btn-pre btnload"> <i class="icon-arrow-right"></i> Enforce Sub Relation(s)</button>	
			<button type="submit"  class="btn btn-pre btnload"> <i class="icon-arrow-right"></i> Run reasoner</button>
			
		
		</form>
		
	</div>
				
	
	
	<br/>

	<div class="row">		
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header" data-original-title>
					<h2><i class="icon-user"></i><span class="break"></span>Instances</h2>
					<div class="box-icon">
						<a href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
					</div>
				</div>
				<div class="box-content">
					<table class="table table-striped table-bordered bootstrap-datatable datatable">
					  <thead>
						  <tr>
							  <th>Instance name</th>
							  <th>Class name</th>
							  <th>State</th>
							  <th>Actions</th>
						  </tr>
					  </thead>   
					  <tbody>
					  
					  	<%
					  		String listClass = "";
						  	for (Instance i : ListAllInstances) {
						  		
						  		out.println("<tr>");
						  		
							  		out.println("<td title=\"" + i.ns + i.name + "\">" + i.name + "</td>");
							  		out.println("<td class=\"center\">");
							  			out.println("<ul>");
								  		for(String c : i.ListClasses)
								  		{
								  			out.println("<li title=\"" + c + "\">" + c.split("#")[1] + "</li>");
								  		}
							  			out.println("</ul>");
							  		out.println("</td>");
							  		
							  		if( i.isModified == true )
							  		{
							  			out.println("<td class=\"center\">	<span class=\"label label-important\" style=\"background:#67c2ef\">Modified</span> </td>");
							  			
							  		} else {
							  			
							  			if(i.haveKnwologeToComplete() == true)
								  		{
								  			out.println("<td class=\"center\">	<span class=\"label label-important\">Not Satisfied</span> </td>");

								  		} else {
								  			
								  			if(i.is_Semi_Complete() == true)
								  			{
								  				out.println("<td class=\"center\">	<span class=\"label label-warning\">Possible Refinements</span> </td>");
								  				
								  			} else {
								  				
								  				out.println("<td class=\"center\">	<span class=\"label label-success\">Satisfied</span> </td>");
								  			}
								  		}
							  			
							  		}
							  		
							  		
							  		
							  		out.println("<td class=\"center\">" + 
							  					"<a class=\"btn btn-success\" target=\"_blank\" href=\"/tnokco/graphVisualizer?typeView=OUT&id=" + i.id + "\"> <i class=\"icon-zoom-in\"></i> </a> " + 
							  					"<a class=\"btn btn-info\" title=\"Manually Complete\" href=\"/tnokco/details?id=" + i.id + "\"> <i class=\"icon-edit\"></i> </a>" + "&nbsp;" +
						  								"<a class=\"btn btn-info\" title=\"Auto Complete\" href=\"/tnokco/completeInstanceAuto?idInstance="+ i.id + "\"> <i class=\"icon-edit\"></i> </a>" +
							  					"</td>");
						  									  		
						  		out.println("</tr>");
						  		
								
							}
					  	
					  	%>
						
					  </tbody>
				  </table>            
				</div>
			</div>
		</div><!--/col-->
	
	</div><!--/row-->
	
	<div class="row">
		 <div class="col-lg-12">
			<p>Description of page:</p>
			<div class="tooltip-demo well">
			  	<p class="muted" style="margin-bottom: 0;">Tight pants next level keffiyeh <a href="#" data-rel="tooltip" data-original-title="first tooltip">you probably</a> haven't heard of them. Photo booth beard raw denim letterpress vegan messenger bag stumptown. Farm-to-table seitan, mcsweeney's fixie sustainable quinoa 8-bit american appadata-rel <a href="#" data-rel="tooltip" data-original-title="Another tooltip">have a</a> terry richardson vinyl chambray. Beard stumptown, cardigans banh mi lomo thundercats. Tofu biodiesel williamsburg marfa, four loko mcsweeney's cleanse vegan chambray. A <a href="#" data-rel="tooltip" data-original-title="Another one here too">really ironic</a> artisan whatever keytar, scenester farm-to-table banksy Austin <a href="#" data-rel="tooltip" data-original-title="The last tip!">twitter handle</a> freegan cred raw denim single-origin coffee viral.
			  	</p>
			</div>                                  
		 </div>
	</div>	
	<!-- /row -->
			
<%@include file="../templates/footer.jsp" %></html>
