<%@ page import="br.ufes.inf.nemo.okco.controller.HomeController"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="br.ufes.inf.nemo.okco.model.Instance"%>
<%@ page import="br.ufes.inf.nemo.okco.model.DtoCompleteClass"%>
<%@ page import="br.ufes.inf.nemo.okco.model.EnumPropertyType"%>
<%@ page import="br.ufes.inf.nemo.okco.model.DtoPropertyAndSubProperties"%>
<%@ page import="br.ufes.inf.nemo.okco.model.DtoDefinitionClass"%>
<%@ page import="br.ufes.inf.nemo.okco.model.DtoInstanceRelation"%>
<%@ page import="java.util.ArrayList"%>


<%
	// Get the parameters from controller
	
	Instance instance = (Instance)request.getSession().getAttribute("instanceSelected");
	ArrayList<Instance> ListAllInstances = (ArrayList<Instance>)request.getSession().getAttribute("listInstances");
	ArrayList<DtoInstanceRelation> InstanceListRelations = (ArrayList<DtoInstanceRelation>)request.getSession().getAttribute("instanceListRelations");	
	
	ArrayList<DtoPropertyAndSubProperties> ListSpecializationProperties = (ArrayList<DtoPropertyAndSubProperties>)request.getSession().getAttribute("ListSpecializationProperties");	
	ArrayList<String> listClassesMembersTmp = (ArrayList<String>)request.getSession().getAttribute("listClassesMembersTmp");
	
	ArrayList<DtoDefinitionClass> listSomeClassDefinition = (ArrayList<DtoDefinitionClass>)request.getSession().getAttribute("listSomeClassDefinition");
	ArrayList<DtoDefinitionClass> listMinClassDefinition = (ArrayList<DtoDefinitionClass>)request.getSession().getAttribute("listMinClassDefinition");
	ArrayList<DtoDefinitionClass> listMaxClassDefinition = (ArrayList<DtoDefinitionClass>)request.getSession().getAttribute("listMaxClassDefinition");
	ArrayList<DtoDefinitionClass> listExactlyClassDefinition = (ArrayList<DtoDefinitionClass>)request.getSession().getAttribute("listExactlyClassDefinition");
	
%>

<%@include file="../templates/header.jsp"%>

<script type="text/javascript">

	//Variables to control specialization properties
	
	var ablePrev = false; //begnning
	var ableNext = true;  //begnning

	$(document).ready(function() {

		// Complete property	
		$('#completePropertyForm').submit(function(event) {

			var separatorValues = "%&&%";
			var id = $("#specValue").attr("value");
			
			var arraySubProp = "";
			$(this).find(".checked").each(function( index ) 
			{
				arraySubProp = arraySubProp + separatorValues + $(this).parent().parent().parent().parent().children("span").attr("title");		  
			});
			
			var json = {
				"arrayCls" : "",
				"arraySubProp" : arraySubProp,
				"id" : id,
			};

			$.ajax({
				url : $("#completePropertyForm").attr("action"),
				data : JSON.stringify(json),
				type : "POST",

				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function(data) {

					if(data.ok == true)
					{
						//alert("sucess. Refresh the page instance and remember the id");
						window.location.href=window.location.href;
						
					} else {

						//Huston we have a problem
						var html = "<div class=\"alert alert-danger\">" +
										"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
										"<strong>" + "Erro! " + "</strong>"+ data.result + 
									"</div>";

						$("#content").prepend(html);
					}
				}
			 });

			event.preventDefault();
			
		}); // End - Complete Property

		// Complete class	
		$('#completeClassForm').submit(function(event) {

			var separatorValues = "%&&%";
			
			var arrayCls = "";
			$(this).find(".checked").each(function( index ) 
			{
				arrayCls = arrayCls + separatorValues + $(this).parent().parent().parent().parent().children("span").attr("title");		  
			});
			
			var json = {
				"arrayCls" : arrayCls,
				"arraySubProp" : "",
				"id" : "",
			};

			$.ajax({
				url : $("#completeClassForm").attr("action"),
				data : JSON.stringify(json),
				type : "POST",

				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function(data) {

					if(data.ok == true)
					{
						window.location.href=window.location.href;
						//alert("sucess. Refresh the page instance and remember the id");
						
					} else {

						//Huston we have a problem
						var html = "<div class=\"alert alert-danger\">" +
										"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
										"<strong>" + "Erro! " + "</strong>"+ data.result + 
									"</div>";

						$("#content").prepend(html);
					}
				}
			 });

			event.preventDefault();
			
		}); // End - Complete Class		
		
	}); // End - document ready
	
	//Previous bottom click
	$(document).on("click", ".btn-prev",function() {	// because add dynamically	
		
		var id = $("#specValue").attr("value");
		if(ablePrev == true)
			ajaxSpecializationGetProperty(parseInt(id)-1, "prev");
		
	}); // End - btn-prev
	
	//Next bottom click
	$(document).on("click", ".btn-next",function() { // because add dynamically		
		
		var id = $("#specValue").attr("value");
		if(ableNext == true)
			ajaxSpecializationGetProperty(parseInt(id)+1, "next");
		
	}); // End - btn-next
	
	//Complete property change
	
	function ajaxSpecializationGetProperty(id, type)
	{
		$.ajax({
			url : "selectSpecializationProp" + "?id=" + id,
			//data : JSON.stringify(json),
			type : "GET",

			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
			},
			success : function(data) {					
				
				if(data.dto != null)
				{
					if(data.havePrev == true)
					{
						ablePrev = true;
						$("#completePropertyForm .btn-prev").addClass("btn-success");
						
					} else {
						ablePrev = false;
						$("#completePropertyForm .btn-prev").removeClass("btn-success");
					}

					if(data.haveNext == true)
					{
						ableNext = true;
						$("#completePropertyForm .btn-next").addClass("btn-success");
						
					} else {
						ableNext = false;
						$("#completePropertyForm .btn-next").removeClass("btn-success");
					}

					var respContent = "";

					respContent += 	"<h3>Classify relation <b>" + data.iSourceName + " -> " + data.dto.Property.split("#")[1] + " -> " + data.dto.iTargetName + "</b> as:</h3>" +
									"<input id=\"specValue\" type=\"hidden\" value=\""+ data.dto.id + "\">";
										
					if(data.dto.SubProperties.length > 0)
					{
						 var countSub = 0;
						 for (var i = 0; i < data.dto.SubProperties.length; i++) 
						 {
							 countSub++;
							 subProp = data.dto.SubProperties[i];
							 respContent += "<label class=\"checkbox inline checkboxMarc\">";
							 respContent += "<div class=\"checker\" id=\"uniform-inlineCheckbox" + countSub  +"\"><span class=\"\"><input type=\"checkbox\" id=\"inlineCheckbox" + countSub + "\" value=\"option" + countSub + "\"></span></div> <span title=\"" + subProp + "\">" + subProp.split("#")[1] + " -> " + data.dto.iTargetName;
							 respContent += "</label>";
						 }
					}

					$('#completePropertyForm .form-group').empty();
					$('#completePropertyForm .form-group').append(respContent);			
					
				} else {

					//Didn't find
				}
			}
		});
	} // - end complety property 


</script>

<div class="row">

	<div style="padding-left: 15px; margin-bottom:20px;">
		<button onclick="window.location = '/okco/list';" style="float:left;" type="button" class="btn btn-prev"> <i class="icon-arrow-left"></i> Back to list</button>
		<div style="clear:both"></div>
	</div>
			
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>Instance informations
				</h2>
				<div class="box-icon">
					 <a	href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-bordered table-striped">
					<tr>
						<td>Name</td>
						<td style="padding-left: 30px;">
							<%
								out.println("<label title=\"" + instance.ns +instance.name + "\">" + instance.name + "</label>");
							%>
						</td>
					</tr>
					<tr>
						<td>Same instances</td>
						<td>
							<ul style="margin: 0">
							<%
								for(String iName: instance.ListSameInstances)
								{
									Instance i = HomeController.ManagerInstances.getInstance(ListAllInstances, iName);
									out.println("<li> <a title=\"" + i.ns + i.name  + "\" href=\"/okco/details?id=" + i.id + "\">" + i.name + "</a> </li>");
								}
							%>
							</ul>
						</td>
						
					</tr>
					<tr>
						<td>Different instances</td>
						<td>
							<ul style="margin: 0">
							<%
								for(String iName: instance.ListDiferentInstances)
								{
									Instance i = HomeController.ManagerInstances.getInstance(ListAllInstances, iName);
									out.println("<li> <a title=\"" + i.ns + i.name  + "\" href=\"/okco/details?id=" + i.id + "\">" + i.name + "</a> </li>");
								}
							%>
							</ul>
						</td>
					</tr>
					<tr>
						<td>Classes</td>
						<td>
							<ul style="margin: 0">
							<%
						  		for(String c : instance.ListClasses)
						  		{
						  			out.println("<li title=\"" + c + "\">" + c.split("#")[1] + "</li>");
						  		}
							%>
							</ul>
						</td>
					</tr>
					<tr>
						<td>Relations</td>
						<td>
							<ul style="margin: 0">
							<%
						  		for(DtoInstanceRelation dto : InstanceListRelations)
						  		{
						  			if(dto.Target.contains("^^"))
						  			{
						  				out.println("<li title=\"" + dto.Property + " -> " + dto.Target + "\">" + dto.Property.split("#")[1] + " -> " + dto.Target.split("\\^\\^")[0] + "</li>");
						  			}else{
						  				out.println("<li title=\"" + dto.Property + " -> " + dto.Target + "\">" + dto.Property.split("#")[1] + " -> " + dto.Target.split("#")[1] + "</li>");
						  			}
						  		}
							%>
							</ul>
						</td>
					</tr>
					<tr>
						<td>Visualizations</td>
						<td>
							<ul style="margin: 0">
								<%
									out.println("<li><a class=\"btn btn-success\" target=\"_blank\" href=\"/okco/graphVisualizer?typeView=IN&id=" + instance.id + "\"> <i class=\"icon-zoom-in\"></i> </a> IN</li>");
									out.println("<li style=\"margin-top:3px;\"><a class=\"btn btn-success\" target=\"_blank\" href=\"/okco/graphVisualizer?typeView=OUT&id=" + instance.id + "\"> <i class=\"icon-zoom-in\"></i> </a> OUT</li>");
								%>
							</ul>
						</td>
					<tr>
				</table>

			</div>
		</div>
	</div>
	<!--/col-->
	
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>Specializations
				</h2>
				<div class="box-icon">
					 <a	href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
			
				<ul class="nav tab-menu nav-tabs" style="padding-right: 24px;" id="myTab">
					<li class=""><a href="#properties">Properties</a></li>
					<li class="active"><a href="#classes">Classes</a></li>
				</ul>
				
				<div id="myTabContent" class="tab-content">
					
					<div class="tab-pane" id="classes">
						
						<h3>Classify instance <i> <% out.println(instance.name); %></i> as:</h3>
						<form id="completeClassForm" action="classifyInstanceClasses" method="POST">
							<div class="form-group" style="margin-top: 20px;">					
							
							<%	
								int count = 0;
								if(listClassesMembersTmp.size() > 0)
								{
										out.println("<div class=\"controls\">");								
										for (String clsComplete : listClassesMembersTmp) 
										{
											count++;
											out.println("<label class=\"checkbox inline checkboxMarc\">");
											out.println("<div class=\"checker\" id=\"uniform-inlineCheckbox" + count  +"\"><span class=\"\"><input type=\"checkbox\" id=\"inlineCheckbox" + count + "\" value=\"option" + count + "\"></span></div> <span title=\"" + clsComplete + "\">" + clsComplete.split("#")[1]);
											out.println("</label>");
										}
										out.println("</div>");
									
									if(count > 0)
									{
										out.println("<div class=\"form-actions\">" +
												"<button type=\"submit\" class=\"btn btn-primary\">Classify</button>" +
												"</div>");
									} else {
										
										out.println("<h3>* No class specializations.</h3>");		
									}
								
								}
							
							%>
								
							</div>
						</form>

					</div>
					<!-- /classes -->
					
					<div class="tab-pane" id="properties">
						<form id="completePropertyForm" action="classifyInstanceProperty" method="POST">
							
							<%
							if(ListSpecializationProperties.size() > 0)
							{
								int countDtos = 0;
								DtoPropertyAndSubProperties dto = ListSpecializationProperties.get(0);
								
								out.println("<div class=\"form-group\" style=\"margin-top: 20px;\">");
								if(dto.iTargetNs.contains("^^"))
					  			{
									out.println("<h3>Classify relation <b>" + instance.name + " -> " + dto.Property.split("#")[1] + " -> " + dto.iTargetNs.split("\\^\\^")[0] + "</b> as:</h3>");							
					  			}else{
					  				out.println("<h3>Classify relation <b>" + instance.name + " -> " + dto.Property.split("#")[1] + " -> " + dto.iTargetName + "</b> as:</h3>");	
					  			}
								
									out.println("<input id=\"specValue\" type=\"hidden\" value=\""+ dto.id + "\">");
									
									 if(dto.SubProperties.size() > 0)
									 {
										 int countSub = 0;
										 for (String subProp: dto.SubProperties) 
										 {
											countSub++;
											
											if(dto.iTargetNs.contains("^^"))
								  			{
												out.println("<label class=\"checkbox inline\">");
												out.println("<div class=\"checker\" id=\"uniform-inlineCheckbox" + countSub  +"\"><span class=\"\"><input type=\"checkbox\" id=\"inlineCheckbox" + countSub + "\" value=\"option" + countSub + "\"></span></div> <span title=\"" + subProp + "\">" + subProp.split("#")[1] + " -> " + dto.iTargetNs.split("\\^\\^")[0]);
												out.println("</label>");						
								  			}else{
								  				out.println("<label class=\"checkbox inline\">");
												out.println("<div class=\"checker\" id=\"uniform-inlineCheckbox" + countSub  +"\"><span class=\"\"><input type=\"checkbox\" id=\"inlineCheckbox" + countSub + "\" value=\"option" + countSub + "\"></span></div> <span title=\"" + subProp + "\">" + subProp.split("#")[1] + " -> " + dto.iTargetName);
												out.println("</label>");	
								  			}
											 
										 }
									 }
								 
								 out.println("</div>");
								 
								 out.println("<div id=\"MyWizard\" class=\"wizard\">");
								 	out.println("<div class=\"actions\">");
										 out.println("<button type=\"button\" class=\"btn btn-prev\"> <i class=\"icon-arrow-left\"></i> Prev relation</button>");
										 if(ListSpecializationProperties.size() > 1)
										 	out.println("<button type=\"button\" class=\"btn btn-success btn-next\" data-last=\"Finish\">Next relations<i class=\"icon-arrow-right\"></i></button>");
										 else
											 out.println("<button type=\"button\" class=\"btn btn-next\" data-last=\"Finish\">Next relations<i class=\"icon-arrow-right\"></i></button>");
									out.println("</div>");
								 out.println("</div>");
								 out.println("<div class=\"form-actions\">");
								 out.println("<button type=\"submit\" class=\"btn btn-primary\">Classify</button>");
								 out.println("</div>");
								 
							} else {
								 out.println("<h3>* No property specializations.</h3>");
							}
							%>
								
							
						</form>
					</div>
					<!-- /properties -->
					
				</div>
				<!-- /myTabContent -->
								
			</div>
			<!-- /box content -->
			
		</div>
		<!-- /box -->
		
	</div>
	<!--/col-->
	
</div>
<!--/row-->

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header" data-original-title>
				<h2>
					<i class="icon-user"></i><span class="break"></span>Object
					properties
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table
					class="table table-striped table-bordered bootstrap-datatable datatable">
					<thead>
						<tr>
							<th>Source</th>
							<th>Relation</th>
							<th>Type</th>
							<th>Target</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>

						<%
							for (DtoDefinitionClass dto : listSomeClassDefinition) {
															  		
											  		if(dto.PropertyType.equals(EnumPropertyType.OBJECT_PROPERTY))
											  		{
											  		
												  		out.println("<tr>");
												  		
													  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
													  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
													  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
													  		out.println("<td>" + "SOME" + "</td>");
													  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");									  									  		
													  		out.println("<td class=\"center\">" + 
													  						"<a class=\"btn btn-info\" title=\"Manually Complete\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "object" + "&propType=SOME" + "\"> <i class=\"icon-edit\"></i> </a>" + "&nbsp;" +
													  								"<a class=\"btn btn-info\" title=\"Auto Complete\" href=\"/okco/completePropertyAuto?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "object" + "&propType=SOME" + "\"> <i class=\"icon-edit\"></i> </a>" +
													  					"</td>");
												  									  		
												  		out.println("</tr>");
											  		}	
												}
											  	
												for (DtoDefinitionClass dto : listMinClassDefinition) {
											  		
													if(dto.PropertyType.equals(EnumPropertyType.OBJECT_PROPERTY))
											  		{
											  		
												  		out.println("<tr>");
													  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
													  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
													  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
													  		out.println("<td>" + "MIN " + dto.Cardinality + "</td>");
													  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");								  		
													  		out.println("<td class=\"center\">" + 
													  				"<a class=\"btn btn-info\" title=\"Manually Complete\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "object" + "&propType=MIN" + "\"> <i class=\"icon-edit\"></i> </a>" + "&nbsp;" +
											  								"<a class=\"btn btn-info\" title=\"Auto Complete\" href=\"/okco/completePropertyAuto?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "object" + "&propType=MIN" + "\"> <i class=\"icon-edit\"></i> </a>" +
													  					"</td>");
												  									  		
												  		out.println("</tr>");							  		
											  		}							  		
													
												}
												
												for (DtoDefinitionClass dto : listMaxClassDefinition) {
											  		
													if(dto.PropertyType.equals(EnumPropertyType.OBJECT_PROPERTY))
											  		{
											  		
												  		out.println("<tr>");
												  		
												  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
												  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
												  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
												  		out.println("<td>" + "MAX " + dto.Cardinality + "</td>");
												  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");							  		
													  	out.println("<td class=\"center\">" + 
													  			"<a class=\"btn btn-info\" title=\"Manually Complete\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "objectMax" + "&propType=MAX" + "\"> <i class=\"icon-edit\"></i> </a>" + "&nbsp;" +
										  								"<a class=\"btn btn-info\" title=\"Auto Complete\" href=\"/okco/completePropertyAuto?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "object" + "&propType=MAX" + "\"> <i class=\"icon-edit\"></i> </a>" +
													  					"</td>");
												  									  		
												  		out.println("</tr>");							  		
											  		}							  		
													
												}
											  	
												for (DtoDefinitionClass dto : listExactlyClassDefinition) {
											  		
													if(dto.PropertyType.equals(EnumPropertyType.OBJECT_PROPERTY))
											  		{
											  		
												  		out.println("<tr>");
												  		
												  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
												  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
												  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
												  		out.println("<td>" + "EXACTLY " + dto.Cardinality + "</td>");
												  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");					  		
													  	out.println("<td class=\"center\">" + 
													  			"<a class=\"btn btn-info\" title=\"Manually Complete\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "object" + "&propType=EXACTLY" + "\"> <i class=\"icon-edit\"></i> </a>" + "&nbsp;" +
										  								"<a class=\"btn btn-info\" title=\"Auto Complete\" href=\"/okco/completePropertyAuto?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "object" + "&propType=EXACTLY" + "\"> <i class=\"icon-edit\"></i> </a>" +
													  					"</td>");
												  									  		
												  		out.println("</tr>");							  		
											  		}							  		
													
												}
						%>

					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!--/col-->

	<div class="col-lg-12">
		<div class="box">
			<div class="box-header" data-original-title>
				<h2>
					<i class="icon-user"></i><span class="break"></span>Data properties
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table
					class="table table-striped table-bordered bootstrap-datatable datatable">
					<thead>
						<tr>
							<th>Source</th>
							<th>Relation</th>
							<th>Type</th>
							<th>Target</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>

						<%
							for (DtoDefinitionClass dto : listSomeClassDefinition) {
															  		
										  		if(dto.PropertyType.equals(EnumPropertyType.DATA_PROPERTY))
										  		{
										  		
											  		out.println("<tr>");
											  		
											  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
											  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
											  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
											  		out.println("<td>" + "SOME " + "</td>");
											  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");								  		
											  		out.println("<td class=\"center\">" + 
											  				"<a class=\"btn btn-info\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "data" + "&propType=SOME" + "\"> <i class=\"icon-edit\"></i> </a>" +
											  					"</td>");
											  									  		
											  		out.println("</tr>");							  		
										  		}							  		
												
											}
										  	
											for (DtoDefinitionClass dto : listMinClassDefinition) {
										  		
												if(dto.PropertyType.equals(EnumPropertyType.DATA_PROPERTY))
										  		{
										  		
											  		out.println("<tr>");
											  		
											  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
											  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
											  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
											  		out.println("<td>" + "MIN " + dto.Cardinality + "</td>");
											  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");						  		
												  	out.println("<td class=\"center\">" + 
												  			"<a class=\"btn btn-info\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "data" + "&propType=MIN" + "\"> <i class=\"icon-edit\"></i> </a>" +
												  					"</td>");
											  									  		
											  		out.println("</tr>");							  		
										  		}							  		
												
											}
											
											for (DtoDefinitionClass dto : listMaxClassDefinition) {
										  		
												if(dto.PropertyType.equals(EnumPropertyType.DATA_PROPERTY))
										  		{
										  		
											  		out.println("<tr>");
											  		
											  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
											  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
											  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
											  		out.println("<td>" + "MAX " + dto.Cardinality + "</td>");
											  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");							  		
											  		out.println("<td class=\"center\">" + 
											  				"<a class=\"btn btn-info\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "data" + "&propType=MAX" + "\"> <i class=\"icon-edit\"></i> </a>" +
											  					"</td>");
											  									  		
											  		out.println("</tr>");							  		
										  		}							  		
												
											}
										  	
											for (DtoDefinitionClass dto : listExactlyClassDefinition) {
										  		
												if(dto.PropertyType.equals(EnumPropertyType.DATA_PROPERTY))
										  		{
										  		
											  		out.println("<tr>");
											  		
											  		//out.println("<td title=\"" + dto.Source + "\">" + dto.Source.split("#")[1] + "</td>");
											  		out.println("<td title=\"" + instance.ns + instance.name + "\">" + instance.name + "</td>");
											  		out.println("<td title=\"" + dto.Relation + "\">" + dto.Relation.split("#")[1] + "</td>");
											  		out.println("<td>" + "EXACTLY " + dto.Cardinality + "</td>");
											  		out.println("<td title=\"" + dto.Target + "\">" + dto.Target.split("#")[1] + "</td>");
												  	out.println("<td class=\"center\">" + 
												  			"<a class=\"btn btn-info\" href=\"/okco/completeProperty?idInstance="+ instance.id + "&idDefinition=" + dto.id + "&type=" + "data" + "&propType=EXACTLY" + "\"> <i class=\"icon-edit\"></i> </a>" +
												  					"</td>");							  									  		
											  		out.println("</tr>");							  		
										  		}							  		
												
											}
						%>

					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!--/col-->

</div>
<!--/row-->

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

<div class="actions">
	<button onclick="window.location = '/okco/list';" type="button" class="btn btn-prev"> <i class="icon-arrow-left"></i> Back to list</button>
	<!-- <button type="button" class="btn btn-success btn-next" data-last="Finish">Next <i class="icon-arrow-right"></i></button> -->
</div>


<%@include file="../templates/footer.jsp"%>