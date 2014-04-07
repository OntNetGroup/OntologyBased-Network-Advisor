<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@include file="../templates/header.jsp"%>


<script>
	$(document).ready(function(){
		graph = startArbor("#viewport");
		addNodes(graph);			
	});
	
	<%
		String values = (String)request.getSession().getAttribute("valuesGraph");
		out.println(values);
	%>
	
	function addNodes(graph){
		graph.addEdge(graph.addNode("equip6", {shape:"default", color:"#0000FF"}),graph.addNode("equip5", {shape:"default", color:"#0000FF"}), {name:'INV.relacao,relacao', inverse:'true'});
		graph.addEdge(graph.addNode("equip1", {shape:"default", color:"#0000FF"}),graph.addNode("equip4", {shape:"default", color:"#0000FF"}), {name:'relacao,INV.relacao', inverse:'true'});
		graph.addEdge(graph.addNode("equip1", {shape:"default", color:"#0000FF"}),graph.addNode("equip3", {shape:"default", color:"#0000FF"}), {name:'relacao,INV.relacao', inverse:'true'});
		graph.addEdge(graph.addNode("equip4", {shape:"default", color:"#0000FF"}),graph.addNode("equip3", {shape:"default", color:"#0000FF"}), {name:'INV.relacao,relacao', inverse:'true'});
		graph.addEdge(graph.addNode("equip4", {shape:"default", color:"#0000FF"}),graph.addNode("equip5", {shape:"default", color:"#0000FF"}), {name:'relacao,INV.relacao', inverse:'true'});

				}
		function getHash(){		
			var hash = {};
		}

		equip['']
		
	
</script>


<div width="800px" height="600px" style="border:1px solid red;">
	<canvas id=viewport width="800px" height="600px"></canvas>
</div>

<%@include file="../templates/footer.jsp"%>