<%@include file="/frontend/template/index-top.jsp"%>

<%
	//Get the parameters from controller
	String[] techs = (String[]) request.getSession().getAttribute("techs");
	String[][] layers = (String[][]) request.getSession().getAttribute("layers");
	String[] allServices = (String[]) request.getSession().getAttribute("allServices");
%>

<script src="frontend/template/js/jquery.nestable.min.js"></script>
	
<div class="row">			
									
	<!-- ======================================================================= -->	
	<div class="col-sm-3">	
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-laptop"></i>Technologies</h2>
				<div class="box-icon">
					<a href="" class="btn-plus"><i class="icon-plus"></i></a>
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>													
				</div>
			</div>	
			<div class="box-content clearfix" style="">																
				<div class="dd" id="tech" style="width:330">
				<p>Registered:</p>
				<ol class="dd-list">
					<%		            	
	            	int i=0;
            		for(String tech: techs){           				
           				out.println("<li class=\"dd-item dd3-item\" data-id=\""+i+"\">");
           				out.println("<div class=\"dd-handle dd3-handle\">Drag</div><div class=\"dd3-content tech\">"+(tech+"")+"</div>");
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
	<div class="col-sm-3">
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-laptop"></i>Layers</h2>
				<div class="box-icon">
					<a href="" class="btn-plus"><i class="icon-plus"></i></a>
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>								
				</div>
			</div>
			<div class="box-content clearfix" style="">																
				<div class="dd" id="layer" style="width:330">
				<p>Registered:</p>
				<ol class="dd-list">					
					<%
	            	int j=0;					
					for(String tech: techs){
						out.println("<div id=\""+tech+"\"; class=\"x\" style=\"display:none;\">");
						for(String layer: layers[j])
						{	
	           				out.println("<li class=\"dd-item dd3-item\" data-id=\""+j+"\">");
	           				out.println("<div class=\"dd-handle dd3-handle\">Drag</div><div class=\"dd3-content\">"+(layer+"")+"</div>");
				           	out.println("</li>");				         
	            		}
					  	out.println("</div>");
						j++;
					}
	            	%>							       	
			  	</ol>
				</div>
			</div>
       </div>
	</div>
	<!-- ======================================================================= -->

	<!-- ======================================================================= -->
    <div class="col-sm-3">	
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-laptop"></i>Services</h2>
				<div class="box-icon">
					<a href="" class="btn-plus"><i class="icon-plus"></i></a>
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>								
				</div>
			</div>
			<div class="box-content clearfix" style="">			
				<!-- <label class="control-label" for="selectError10">Service of Selected Layer</label> -->
				<div class="controls" style="width:330;float:right">
					<p>All available services:</p>	
					<%
// 	            		int k=0;					
// 						for(String tech: techs){							
// 							for(String layer: layers[k])
// 							{
// 								out.println("<div id=\""+layer+"\"; class=\"y\" style=\"display:none;\">");
								out.println("<select id=\"selectError10\" class=\"form-control\" style=\"width:300;height:500\" multiple data-rel=\"chosen\">");							  	
							  	for(String service: allServices){
							  		out.println("<option>"+service+"</option>");
							  	}
							  	out.println("</select>");
// 							  	out.println("</div>");
// 							}
// 							k++;
// 						}
					%>				  	
				</div>							
				<!-- ======================================================================= -->			
		</div>		
	</div><!--/col-->
	</div>		
</div><!--/row-->
					
<!--<menu id="nestable-menu"> -->
<!--<button class="btn btn-info" type="button" data-action="expand-all">Expand All</button> -->
<!--<button class="btn btn-danger" type="button" data-action="collapse-all">Collapse All</button> -->
<!--</menu>	 -->
	
<div class="modal fade" id="myModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">Modal title</h4>
			</div>
			<div class="modal-body">
				<p>Here settings can be configured...</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary">Save changes</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
	
<!-- page scripts -->
<script src="/nopen/frontend/template/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.sparkline.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.chosen.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.autosize.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.placeholder.min.js"></script>
<script src="/nopen/frontend/template/js/wizard.min.js"></script>

<script>

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

    $('.tech').on('click', function(e) {
		var tech = e.target.innerText;
		
		$(".x").css("display","none");
		$("#"+tech).css("display","block");		
    });
    
//     $('.layer').on('click', function(e) {
// 		var layer = e.target.innerText;
		
// 		$(".y").css("display","none");
// 		$("#"+layer).css("display","block");	
//     });
    
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
	
</script>
	
<%@include file="/frontend/template/index-bottom.jsp"%>

