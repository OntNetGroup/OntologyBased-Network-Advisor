<%@include file="/frontend/template/index-top.jsp"%>

<script src="frontend/template/js/jquery.nestable.min.js"></script>
			
<div class="row">				
	<div class="col-sm-6">
		<div class="box">
 			
			<!-- ======================================================================= -->				
			<div class="box-header">
				<h2><i class="icon-laptop"></i>Configure: Technologies, Layers and Services</h2>
				<div class="box-icon">
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>								
				</div>
			</div>
			<!-- ======================================================================= -->
				
			<!-- ======================================================================= -->	
			<div class="box-content clearfix">				
				
			<p>Services:</p>
			<div class="box-content">							
				<form class="form-horizontal">							
						<div class="form-group">
							<!-- <label class="control-label" for="selectError10">Service of Selected Layer</label> -->
							<div class="controls">
							  	<select id="selectError10" class="form-control" multiple data-rel="chosen">
									<option>Service 1</option>
									<option selected>Service 2</option>
									<option>Service 3</option>
									<option>Service 4</option>
									<option>Service 5</option>
							  	</select>
							</div>
						</div>						
				</form>								
			</div>
					
			<p>Registered technologies and their layers:</p>									
			<div class="dd" id="nestable">
	            <ol class="dd-list">
	            	<%
	            	String[][] layers = {{"POUk", "ODUk","OTUk"},{"Subscribers", "MEN"}};		            	
	            	String[] techs = {"OTN","MEF"};		            	
	            	int i=0;
            		for(String tech: techs){
           				out.println(
          						"<li class=\"dd-item\" data-id=\""+i+"\">"
          					);
            			out.println(      			
   		                    "	<div class=\"dd-handle\">"+(tech+" Technology")+
   		                    		"<a href=\"ui-nestable-list.html#\" class=\"icon icon-pencil\"></a>"+
   		                    "	</div>"		    		                		            					
            			);
            			out.println(
           					"	<ol class=\"dd-list\">"
           				);
            			int j=0;
            			for(String layer: layers[i]){
            				out.println(
           						"	<li class=\"dd-item\" data-id=\""+2+"\">"+
           								"<div class=\"dd-handle\">"+(layer+" Layer")+"</div>"+
           						"	</li>"
            				);
            				j++;
            			}
	            		out.println(
           					"	</ol>"
	            		);
	            		out.println(
	            			"</li>"
	            		);
	            		i++;
            		}
	            	%>	            			                              
	            </ol>
	            <menu id="nestable-menu">
					<button class="btn btn-info" type="button" data-action="expand-all">Expand All</button>
					<button class="btn btn-danger" type="button" data-action="collapse-all">Collapse All</button>
				</menu>	
	        </div>				
			<!-- ======================================================================= -->
			
		</div>		
	</div><!--/col-->			
</div><!--/row-->
					
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
    $('#nestable').nestable({
        group: 1
    })

    // activate Nestable for list 2
    $('#nestable2').nestable({
        group: 1
    })

    $('#nestable3').nestable();

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

