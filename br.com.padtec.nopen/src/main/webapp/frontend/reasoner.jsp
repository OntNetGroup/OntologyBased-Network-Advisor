<%@include file="/frontend/template/index-top.jsp"%>
<%@ page import="java.util.List"%>

<%
	//Get the parameters from controller
// 	String[] techs = (String[]) request.getSession().getAttribute("techs");
// 	List<String> defaultTechs = (List<String>) request.getSession().getAttribute("defaultTechs");

// 	String[][] layers = (String[][]) request.getSession().getAttribute("layers");
// 	List<String> defaultLayers = (List<String>) request.getSession().getAttribute("defaultLayers");
	
// 	String[] services = (String[]) request.getSession().getAttribute("services");
// 	List<String> defaultServices = (List<String>) request.getSession().getAttribute("defaultServices");
%>

<script src="frontend/template/js/jquery.nestable.min.js"></script>
			
<div class="row">

	<div class="col-sm-4">	
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-laptop"></i>Inference Settings</h2>
				<div class="box-icon">					
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>													
				</div>
			</div>	
			<div class="box-content clearfix" style="">			
				<h2>Default reasoning used for running inferences:</h2>								
			    <div class="form-group">
					<label class="control-label" for="selectError3"></label>
					<div class="controls">
					  <select id="selectError3" class="form-control">
						<option>Hermit</option>
						<option>Pellet</option>						
					  </select>
					</div>
				 </div>
			</div>				
	    </div>    
	</div>	
						
</div><!--/row-->	
	
	
<!-- page scripts -->
<script src="/nopen/frontend/template/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.sparkline.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.chosen.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.autosize.min.js"></script>
<script src="/nopen/frontend/template/js/jquery.placeholder.min.js"></script>
<script src="/nopen/frontend/template/js/wizard.min.js"></script>
		
<%@include file="/frontend/template/index-bottom.jsp"%>

