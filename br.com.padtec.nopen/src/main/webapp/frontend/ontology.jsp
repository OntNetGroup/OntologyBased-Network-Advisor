<%@include file="/frontend/template/index-top.jsp"%>

<%
	String ontology = (String)request.getSession().getAttribute("ontology");
	String name = (String)request.getSession().getAttribute("name");
%>

<div class="row">
	<div class="col-sm-12">	
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-file-text"></i><% out.print(name); %></h2>
				<div class="box-icon">					
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>													
				</div>
			</div>	
			<div class="box-content clearfix" style="">		
				<%
				if(ontology != null && ontology != "")
				{
					out.println("<textarea style=\"width: 100%; overflow: hidden; word-wrap: break-word; resize: horizontal; height: 90006px;\"> "+ontology+" </textarea>");					
				} else {					
					out.println("<p> No ontology loaded </p>");
				}
				%>
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