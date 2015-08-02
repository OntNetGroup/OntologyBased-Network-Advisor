<%@include file="/frontend/template/index-top.jsp"%>
<%@ page import="java.util.List"%>

<%
	//Get the parameters from controller
	String[] techs = (String[]) request.getSession().getAttribute("techs");
	List<String> defaultTechs = (List<String>) request.getSession().getAttribute("defaultTechs");

	String[][] layers = (String[][]) request.getSession().getAttribute("layers");
	List<String> defaultLayers = (List<String>) request.getSession().getAttribute("defaultLayers");
	
	String[] services = (String[]) request.getSession().getAttribute("services");
	List<String> defaultServices = (List<String>) request.getSession().getAttribute("defaultServices");
%>

<script src="frontend/template/js/jquery.nestable.min.js"></script>
	
<div class="row">

	<div id="layer-error" class="box-content alerts" style="display:none;">
		<div class="alert alert-danger">
			<button type="button" class="close" data-dismiss="alert">×</button>
			<strong>Not allowed! </strong> In order to add a new layer, select first a Technology.
		</div>
	</div>

	<div id="service-error" class="box-content alerts" style="display:none;">
		<div class="alert alert-danger">
			<button type="button" class="close" data-dismiss="alert">×</button>
			<strong>Not allowed!</strong> In order to add a new Service, please select first a layer
		</div>
	</div>
	
	<!-- ======================================================================= -->	
	<div class="col-sm-6">	
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-laptop"></i>Technologies</h2>
				<div class="box-icon">
					<a href="ui-nestable-list.html#" class="btn-tech-plus"><i class="icon-plus"></i></a>
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>													
				</div>
			</div>	
			<div class="box-content clearfix" style="">																
				<div class="dd" id="tech" style="width:100%">				
				<ol id="tech-dd-list" class="dd-list">
					<%		            	
	            	int i=0;
            		for(String tech: techs){           				
           				out.println("<li class=\"dd-item dd3-item\" data-id=\""+i+"\">");
           				out.print("<div class=\"dd-handle dd3-handle\">Drag</div>"+"<div class=\"dd3-content tech\">"+(tech+""));
           				/*if(!defaultTechs.contains(tech))*/ out.print("<a href=\"ui-nestable-list.html#\" class=\"icon-trash\" del-type=\"tech-"+tech+"\" style=\"float:right\"></a>");
           				out.println("</div>");
			           	out.println("</li>");            			            			
	            		i++;
            		}
	            	%>							       	
			  	</ol>
				</div>
			</div>				
	    </div>    
	</div>
	<!-- ======================================================================= -->
	
	<!-- ======================================================================= -->				   
	<div class="col-sm-6">
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-laptop"></i>Layers and Services</h2>
				<div class="box-icon">
					<a href="ui-nestable-list.html#" class="btn-layer-plus"><i class="icon-plus"></i></a>
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>								
				</div>
			</div>
			<div class="box-content clearfix" style="">																
				<div class="dd" id="layer" style="width:100%">
				<p>These are the registered layers ordered regarding who is client/server of whom.
				   The top layer is client of the layer beneath it and thus successively. 								
				   Conversely, the bottom layer is server of the layer above it and thus successively. </p>
				<ol id="layer-dd-list" class="dd-list">					
					<%
	            	int j=0;					
					for(String tech: techs){
						
						out.println("<div id=\""+tech+"\" class=\"x\" style=\"display:none;\">");
						for(String layer: layers[j])
						{	
	           				out.println("<li class=\"dd-item dd3-item\"  data-id=\""+j+"\" >");
	           				out.print("<div class=\"dd-handle dd3-handle\">Drag</div>"+"<div class=\"dd3-content\">"+(layer+""));
	           				/*if(!defaultLayers.contains(layer))*/ out.print("<a href=\"ui-nestable-list.html#\" class=\"icon-trash\" del-type=\"layer-"+layer+"\" style=\"float:right\"></a>");	           				
							out.println("</div>");
				           	out.println("</li>");				         
	            		}
						
						out.println("<select id=\"selectError10\" class=\"form-control\" style=\"width:100%;height:500\" multiple data-rel=\"chosen\">");							  	
					  	int k=0;
						for(String service: services){
					  		if(k==0) out.println("<option value=\""+service+"\" selected>"+service+"</option>");
					  		else out.println("<option value=\""+service+"\">"+service+"</option>");
					  		k++;
					  	}
					  	out.println("</select>");
					  	
						out.println("</div>");
						
					  	
						j++;
					}
	            	%>							       	
	
				</div>				
			</div>						
       </div>
	</div>
	<!-- ======================================================================= -->
	
</div><!--/row-->	
	
<div class="modal fade" id="techModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">New Technology</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="control-label" for="focusedInput">Name:</label>
					<div class="controls">
					  <input class="form-control focused" id="tech-input" type="text" value="" autofocus>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="create-tech">Add</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
	
<div class="modal fade" id="layerModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">New Layer</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="control-label" for="focusedInput">Name:</label>
					<div class="controls">
					  <input class="form-control focused" id="layer-input" type="text" value="" autofocus>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="create-layer">Add</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!--<menu id="nestable-menu"> -->
<!--<button class="btn btn-info" type="button" data-action="expand-all">Expand All</button> -->
<!--<button class="btn btn-danger" type="button" data-action="collapse-all">Collapse All</button> -->
<!--</menu>	 -->
		
<!-- page scripts -->
<script src="/nopen/frontend/template/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.sparkline.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.chosen.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.autosize.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.placeholder.min.js"></script>
<script src="/nopen/frontend/template/js/wizard.min.js"></script>

<script>
	var tech = null;
	var layer = null;
	
	/* ---------- FuelUX Wizard ---------- */
	$('#myWizard').wizard();
	
	/* ---------- Datapicker ---------- */
	$('.datepicker').datepicker();

	/* ---------- Choosen ---------- */
	$('[data-rel="chosen"],[rel="chosen"]').chosen();

	/* ---------- Placeholder Fix for IE ---------- */
	$('input, textarea').placeholder();

	/* ---------- Auto Height texarea ---------- */
	$('textarea').autosize();   

	
    // activate Nestable for list 1
    $('#tech').nestable({
        group: 1
    });

    // activate Nestable for list 2
    $('#layer').nestable({
        group: 2
    });
	   
    $('.tech').live('click', function(e) {
		tech = e.target.innerText;		
		//$('div #+tech').css( "background-color", "red" );
					
		$(".x").css("display","none");
		$("#"+tech).css("display","inline");		
    });
    
    $('#layerModal').on('shown.bs.modal', function () {
   	  $('#layer-input').focus()
   	});
    
    $('#techModal').on('shown.bs.modal', function () {
   	  $('#tech-input').focus()
   	});
    	
	$('#nestable-menu').on('click', function(e) {
	        var target = $(e.target),
	            action = target.data('action');
	        if (action === 'expand-all') {
	            $('.dd').nestable('expandAll');
	        }
	        if (action === 'collapse-all') {
	            $('.dd').nestable('collapseAll');
	        }
	});
	
	$('.btn-tech-plus').click(function(e){
		e.preventDefault();
		$('#tech-input').val("");
		$('#techModal').modal('show');	
	});
	
	$('.btn-layer-plus').click(function(e){
		e.preventDefault();
		if(tech == null){
			$("#layer-error").show();
		}else{
			$('#layer-input').val("");
			$('#layerModal').modal('show');
		}
	});
	
	$('#create-layer').click(function(e){
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "createLayer.htm",
		   data: {
			   'layerName': $('#layer-input').val(),
			   'techName': tech
		   },
		   success: function(data){ 		   
			   //alert(data);
			   
			   var appendString = ""
			        +"<li class=\"dd-item dd3-item\" data-id=\""+$('#layer-dd-list li').size()+"\">"
      				+"      <div class=\"dd-handle dd3-handle\">Drag</div>"
      				+"      <div class=\"dd3-content\">"+$('#layer-input').val()+"<a href=\"ui-nestable-list.html#\" class=\"icon-trash\" del-type=\""+"layer-"+$('#layer-input').val()+"\" style=\"float:right\"></a>"
      				+"		</div>"
		           	+"</li>";				         
			   
			   $("#"+tech).append(appendString);
			   $("#"+tech).css("display","inline");
			   
			   $('#layer-input').val("");
			   $('#layerModal').modal('hide');
		   },
		   error : function(e) {
			   alert("Error: " + e.status);
		   }
		});		
	});
	
	$('#create-tech').click(function(e){
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "createTech.htm",
		   data: {
			   'techName': $('#tech-input').val()
		   },
		   success: function(data){ 		   
			   //alert(data);
			   
			   console.log("size: "+$('#tech-dd-list li').size());
			   var appendString = ""				  
			        +"<li class=\"dd-item dd3-item\" data-id=\""+$('#tech-dd-list li').size()+"\">"
      				+"      <div class=\"dd-handle dd3-handle\">Drag</div><div class=\"dd3-content tech\">"+$('#tech-input').val()
      				+"			<a href=\"ui-nestable-list.html#\" class=\"icon-trash\" del-type=\""+"tech-"+$('#tech-input').val()+"\" style=\"float:right\"></a>"
      				+"		</div>"
		           	+"</li>";				         
			  
			  console.log(appendString);
			  
			   $("#tech .dd-list").append(appendString);
			   
			   var layerAppend = "<div id=\""+$('#tech-input').val()+"\" class=\"x\" style=\"display:none;\"> </div>";
			   $("#layer-dd-list").append(layerAppend);
			   
			   $('#tech-input').val("");
			   $('#techModal').modal('hide');
		   },
		   error : function(e) {
			   alert("Error: " + e.status);
		   }
		});		
	});

	$('.icon-trash').live('click',function(e) {
		e.preventDefault();
		var target = $(e.target).attr("del-type");
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "deleteTechOrLayer.htm",
		   data: {
			   'elemName': target
		   },
		   success: function(data){ 	
			   $(e.target).parent().parent().remove();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
    });
	
</script>
	
<%@include file="/frontend/template/index-bottom.jsp"%>

