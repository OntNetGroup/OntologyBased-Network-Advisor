<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Ontology Knowledge Complete - Graph Visualizer</title>

<style>
#currentNode {
	border: 1px solid #B8B8B8;
	width: 300px;
	height: 250px;
	padding-left: 15px;
	padding-top: 15px;
	top: 20px;
	overflow-y: auto;
	max-width: 290px;
	max-height: 210px;
	color: #fff;
	background: #222 linear-gradient(#444, #222);
	font-family: verdana, arial, sans-serif;
	font-size: 14px;
}

#subtitle {
	border: 1px solid #B8B8B8;
	width: 270px;
	height: 240px;
	padding-left: 10px;
	padding-top: 10px;
	overflow-y: auto;
	background: #222 linear-gradient(#444, #222);
}
</style>

<script src="Assets/js/jquery-1.10.2.min.js"></script>
<script src="Assets/js/graph/arbor.js"></script>
<script src="Assets/js/graph/graphics.js"></script>
<script src="Assets/js/graph/main.js"></script>

</head>
<body>

<script>
	$(document).ready(function(){
		graph = startArbor("#viewport");
		addNodes(graph);			
	});
	
	<%
		String values = (String)request.getSession().getAttribute("valuesGraph");
		out.println(values);
	%>
</script>
	<%
		int width  = (Integer)request.getSession().getAttribute("width");
		out.println("<div style=\"width:"+(width+320)+"px\">");
	%>
	<div style="float: left; border: 1px solid black;">
		<%
				int height = (Integer)request.getSession().getAttribute("height");
				out.println("<canvas id=\"viewport\" width=\""+width+"\" height=\""+height+"\"></canvas>");
				
			%>
	</div>
	<div style="float: right;">
		<div id="subtitle">
			<%
					String subtitle = (String)request.getSession().getAttribute("subtitle");
					out.println("<img id=\"subtitle_table\" src=\"Assets/img/subtitles/"+subtitle+"\">");
			%>
		</div>
		<br>
		<div id="currentNode">Select a node to visualize information
			about it.</div>
	</div>

	<script>
	(function($) {
		$.fn.drags = function(opt) {

			opt = $.extend({handle:"",cursor:"move"}, opt);

			if(opt.handle === "") {
				var $el = this;
			} else {
				var $el = this.find(opt.handle);
			}

			return $el.css('cursor', opt.cursor).on("mousedown", function(e) {
				if(opt.handle === "") {
					var $drag = $(this).addClass('draggable');
				} else {
					var $drag = $(this).addClass('active-handle').parent().addClass('draggable');
				}
				var z_idx = $drag.css('z-index'),
					drg_h = $drag.outerHeight(),
					drg_w = $drag.outerWidth(),
					pos_y = $drag.offset().top + drg_h - e.pageY,
					pos_x = $drag.offset().left + drg_w - e.pageX;
				$drag.css('z-index', 1000).parents().on("mousemove", function(e) {
					$('.draggable').offset({
						top:e.pageY + pos_y - drg_h,
						left:e.pageX + pos_x - drg_w
					}).on("mouseup", function() {
						$(this).removeClass('draggable').css('z-index', z_idx);
					});
				});
				e.preventDefault(); // disable selection
			}).on("mouseup", function() {
				if(opt.handle === "") {
					$(this).removeClass('draggable');
				} else {
					$(this).removeClass('active-handle').parent().removeClass('draggable');
				}
			});

		}
	})(jQuery);

	$('#currentNode').drags();
	$('#subtitle').drags();
	</script>
	
	<img id="AF_AZUL" src="Assets/shapes/AF_AZUL.png" hidden>
	<img id="Binding_AZUL" src="Assets/shapes/Binding_AZUL.png" hidden>
	<img id="Datatype_AZUL" src="Assets/shapes/Datatype_AZUL.png" hidden>
	<img id="Equip_AZUL" src="Assets/shapes/Equip_AZUL.png" hidden>
	<img id="FORWARDING_AZUL" src="Assets/shapes/FORWARDING_AZUL.png" hidden>
	<img id="FWR_RULE_AZUL" src="Assets/shapes/FWR_RULE_AZUL.png" hidden>
	<img id="Input_AZUL" src="Assets/shapes/Input_AZUL.png" hidden>
	<img id="INT_IN_AZUL" src="Assets/shapes/INT_IN_AZUL.png" hidden>
	<img id="INT_OUT_AZUL" src="Assets/shapes/INT_OUT_AZUL.png" hidden>
	<img id="Layer_AZUL" src="Assets/shapes/Layer_AZUL.png" hidden>
	<img id="LPF_AZUL" src="Assets/shapes/LPF_AZUL.png" hidden>
	<img id="Matrix_AZUL" src="Assets/shapes/Matrix_AZUL.png" hidden>
	<img id="Output_AZUL" src="Assets/shapes/Output_AZUL.png" hidden>
	<img id="PM_AZUL" src="Assets/shapes/PM_AZUL.png" hidden>
	<img id="Process_AZUL" src="Assets/shapes/Process_AZUL.png" hidden>
	<img id="RP_AZUL" src="Assets/shapes/RP_AZUL.png" hidden>
	<img id="SITE_AZUL" src="Assets/shapes/SITE_AZUL.png" hidden>
	<img id="SN_AZUL" src="Assets/shapes/SN_AZUL.png" hidden>
	<img id="TE_AZUL" src="Assets/shapes/TE_AZUL.png" hidden>
	<img id="TF_AZUL" src="Assets/shapes/TF_AZUL.png" hidden>

	<img id="AF_ROXO" src="Assets/shapes/AF_ROXO.png" hidden>
	<img id="Binding_ROXO" src="Assets/shapes/Binding_ROXO.png" hidden>
	<img id="Datatype_ROXO" src="Assets/shapes/Datatype_ROXO.png" hidden>
	<img id="Equip_ROXO" src="Assets/shapes/Equip_ROXO.png" hidden>
	<img id="FORWARDING_ROXO" src="Assets/shapes/FORWARDING_ROXO.png" hidden>
	<img id="FWR_RULE_ROXO" src="Assets/shapes/FWR_RULE_ROXO.png" hidden>
	<img id="Input_ROXO" src="Assets/shapes/Input_ROXO.png" hidden>
	<img id="INT_IN_ROXO" src="Assets/shapes/INT_IN_ROXO.png" hidden>
	<img id="INT_OUT_ROXO" src="Assets/shapes/INT_OUT_ROXO.png" hidden>
	<img id="Layer_ROXO" src="Assets/shapes/Layer_ROXO.png" hidden>
	<img id="LPF_ROXO" src="Assets/shapes/LPF_ROXO.png" hidden>
	<img id="Matrix_ROXO" src="Assets/shapes/Matrix_ROXO.png" hidden>
	<img id="Output_ROXO" src="Assets/shapes/Output_ROXO.png" hidden>
	<img id="PM_ROXO" src="Assets/shapes/PM_ROXO.png" hidden>
	<img id="Process_ROXO" src="Assets/shapes/Process_ROXO.png" hidden>
	<img id="RP_ROXO" src="Assets/shapes/RP_ROXO.png" hidden>
	<img id="SITE_ROXO" src="Assets/shapes/SITE_ROXO.png" hidden>
	<img id="SN_ROXO" src="Assets/shapes/SN_ROXO.png" hidden>
	<img id="TE_ROXO" src="Assets/shapes/TE_ROXO.png" hidden>
	<img id="TF_ROXO" src="Assets/shapes/TF_ROXO.png" hidden>
	
	<img id="AF_VERDE" src="Assets/shapes/AF_VERDE.png" hidden>
	<img id="Binding_VERDE" src="Assets/shapes/Binding_VERDE.png" hidden>
	<img id="Datatype_VERDE" src="Assets/shapes/Datatype_VERDE.png" hidden>
	<img id="Equip_VERDE" src="Assets/shapes/Equip_VERDE.png" hidden>
	<img id="FORWARDING_VERDE" src="Assets/shapes/FORWARDING_VERDE.png" hidden>
	<img id="FWR_RULE_VERDE" src="Assets/shapes/FWR_RULE_VERDE.png" hidden>
	<img id="Input_VERDE" src="Assets/shapes/Input_VERDE.png" hidden>
	<img id="INT_IN_VERDE" src="Assets/shapes/INT_IN_VERDE.png" hidden>
	<img id="INT_OUT_VERDE" src="Assets/shapes/INT_OUT_VERDE.png" hidden>
	<img id="Layer_VERDE" src="Assets/shapes/Layer_VERDE.png" hidden>
	<img id="LPF_VERDE" src="Assets/shapes/LPF_VERDE.png" hidden>
	<img id="Matrix_VERDE" src="Assets/shapes/Matrix_VERDE.png" hidden>
	<img id="Output_VERDE" src="Assets/shapes/Output_VERDE.png" hidden>
	<img id="PM_VERDE" src="Assets/shapes/PM_VERDE.png" hidden>
	<img id="Process_VERDE" src="Assets/shapes/Process_VERDE.png" hidden>
	<img id="RP_VERDE" src="Assets/shapes/RP_VERDE.png" hidden>
	<img id="SITE_VERDE" src="Assets/shapes/SITE_VERDE.png" hidden>
	<img id="SN_VERDE" src="Assets/shapes/SN_VERDE.png" hidden>
	<img id="TE_VERDE" src="Assets/shapes/TE_VERDE.png" hidden>
	<img id="TF_VERDE" src="Assets/shapes/TF_VERDE.png" hidden>
</body>
</html>
