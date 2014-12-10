<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="br.com.padtec.common.dto.DtoInstance" %>
<%@ page import="java.util.List" %>

<%
	/** Get the parameters from controller */
	@SuppressWarnings("unchecked")
	List<DtoInstance> ListAllInstances = (List<DtoInstance>)request.getSession().getAttribute("listInstances");
	@SuppressWarnings("unchecked")
	List<String> listModifedInstances = (List<String>)request.getSession().getAttribute("listModifedInstances");
%>

<%@include file="../templates/header.jsp" %>

<script type="text/javascript">

	$(document).ready(function() {
		$(".btn btn-info").click(function(){
			loading();
		});	
		
	}); // End - document ready;

	$(document).ready(function()
	{
		 
		/** Run reasoner */
		$('#runReasonerForm').submit(function(event)
		{
			loading();
			$.ajax({
				url : $("#runReasonerForm").attr("action"),
				//data : JSON.stringify(json),
				type : "POST",
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function(json) 
				{						
					if(!json.error)
					{
						window.location.href = "list";
						$(document).ajaxStop(function() { location.reload(true); });
					}else{
						//Huston we have a problem
						var html = "<div class=\"alert alert-danger\">" +
						"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
						"<strong>" + "Erro! " + "</strong>"+ "Couldn't execute the reasoner." + 
						"</div>";
						$("#content").prepend(html);
					}
				}
			});
			event.preventDefault();						
		}); // end submmit		
	});

</script>
	<div class="row" style="margin-right:0px;margin-left:0px">	
		<div id="boxViewAll" style="float:left">
			<a class="btn btn-success" target="_blank" href="/br.com.padtec.okco/graphVisualizer?typeView=ALL&id=0"> <i class="icon-zoom-in"></i> </a>
			VIEW ALL GRAPH			
		</div>
		<form id="runReasonerForm" style="float:right" action="runReasoner" method="POST">
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
							  <th class="state">State</th>
							  <th class="actions">Actions</th>
						  </tr>
					  </thead>   
					  <tbody>					  
					  	<%
					  	String listClass = "";
  					  	for (DtoInstance i : ListAllInstances) 
  					  	{  					  		
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
  						  	
  						  	String stateClass = "";
  						  	String stateLabel = "";
  						  	if(listModifedInstances.contains(i.ns+i.name))
  						  	{
  						  		stateClass = "label label-important";
  						  		stateLabel = "Modified";
  						  		//out.println("<td class=\"center\">	<span class=\"label label-important\" style=\"background:#f63f2d\">Modified</span> </td>");
  						  	}else{  						  			
  						  		if(i.haveKnwologeToComplete() == true)
  							  	{
  						  			stateClass = "label label-important";
  						  			stateLabel = "Not Satisfied";
  							  		//out.println("<td class=\"center\">	<span class=\"label label-important\">Not Satisfied</span> </td>");
						  		}else{  							  			
  							  		if(i.is_Semi_Complete() == true)
  							  		{
  							  			stateClass = "label label-warning";
  							  			stateLabel = "Possible Refinements";
  							  			//out.println("<td class=\"center\">	<span class=\"label label-warning\">Possible Refinements</span> </td>");
  							  		}else{
  							  			stateClass = "label label-success";
  							  			stateLabel = "Satisfied";
  							  			//out.println("<td class=\"center\">	<span class=\"label label-success\">Satisfied</span> </td>");
  							  		}
  							  	}
  						  	}	
  						 	out.println("<td class=\"state\">	<span class=\""+stateClass+"\">"+stateLabel+"</span> </td>");
  						  	out.println("<td class=\"actions\">" + 
  						  	"<a class=\"btn btn-success\" target=\"_blank\" href=\"/br.com.padtec.okco/graphVisualizer?typeView=IN&uri=" + i.uriEncoded + "\"> <i class=\"icon-zoom-in\"></i> </a> " + 
  						  	"<a class=\"btn btn-info\" title=\"Manually Complete\" href=\"/br.com.padtec.okco/details?uri=" + i.uriEncoded + "\" onclick=\"loading()\"> <i class=\"icon-hand-up\"> &nbsp;Manually Complete</i> </a>" + "&nbsp;" +
  					  		"<a class=\"btn btn-info\" title=\"Auto Complete\" href=\"/br.com.padtec.okco/completeInstanceAuto?uriInstance="+ i.uriEncoded + "\" onclick=\"loading()\"> <i class=\"icon-cogs\">&nbsp;Auto Complete</i> </a>" +
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
				<p class="muted" style="margin-bottom: 0;">
					This page shows all instances that exists in the OWL model, as well as its states and belonging classes. The possible states are:
					<br>
					<ul>
						<li>Satisfied: instances that satisfies all the related ontology axioms.</li>
						<li>Possible Refinement: instances that satisfies all the related ontology axioms, but that can be improved.</li>
						<li>Not Satisfied: instances that do not satisfies all the related ontology axioms.</li>
						<li>Modified: instances changed by the user and still not verified by the reasoner.</li>
					</ul>
					<br>
					There exist two ways for instance competition:
					<br>
					<ul>
						<li>Manually Completion: for user competition.</li>
						<li>Auto Competition: for machine competition.</li>
					</ul>
					<br>
					All instances relations can be visualized clicking on the magnifier icon.
					Finally, the user can run the reasoned to check consistency and apply inferences at any time by clicking on the "Run reasoner" button. In case of inconsistencies, the last consistent state of the model is returned.							                               
		 	</div>
		</div>	
	</div>	
	<!-- /row -->
			
<%@include file="../templates/footer.jsp" %></html>

