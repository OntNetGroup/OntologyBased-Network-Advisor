<%@include file="/frontend/template/index-top.jsp"%>
<%@ page import="java.util.List"%>

<%
	//Get the parameters from controller
 	String reasoner = (String) request.getSession().getAttribute("defaultReasoner");

%>

<script src="frontend/template/js/jquery.nestable.min.js"></script>

<script>	
	$('#selectReasoner').change(function(){
		alert(this.options[this.selectedIndex].value);	
	});
</script>
			
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
					<label class="control-label" for="selectReasoner"></label>
					<div class="controls">
					<select id="selectReasoner" class="form-control">
					<%						
						if(reasoner.equals("hermit")) out.println("<option value=\"hermit\" selected> Hermit</option>");
						else out.println("<option value=\"hermit\"> Hermit</option>");
						if(reasoner.equals("pellet")) out.println("<option value=\"pellet\" selected> Pellet</option>");
						else out.println("<option value=\"pellet\"> Pellet</option>");		
					%>
					</select>  
					</div>
				 </div>
				 <h2>About reasoners...</h2>		
				 <div class="row">
					<div class="col-lg-12">						
						<div class="tooltip-demo well">		
						<p>							
							Reasoners have the capability of classify and check the consistency of ontologies written with the Web Ontology Language (OWL).  
						</p>
						<P>	
							NOPEn's set of functionalities incorporate two different reasoners: <a target="_blank" href="http://clarkparsia.com/pellet/"><b><u>Pellet</u></b> (version 2.3.1)</a> and <a target="_blank" href="http://hermit-reasoner.com/"><b><u>HermiT</u></b> (version 1.3.8)</a>.
						</p>
						<p> 
							The existence of two different options is justified by different characteristics of these reasoners. 
							For instance, Pellet implements a tableau-based algorithm whilst HermiT implements the hypertableau calculus, which is faster algorithm <a target="_blank" href="http://semantic-web-journal.org/sites/default/files/swj120.pdf"><u>[Link]</u></a>. 
						</p>
						<p>
							In addition, Pellet has an incremental reasoning which supports incremental classification and  consistency check for additions and removals of elements <a target="_blank" href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.153.8799&rep=rep1&type=pdf"><u>[Link]</u></a>.
						</p>							
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

