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
	<img id="AF_AZUL" src="Assets/img/shapes/AF_AZUL.png" hidden>
	<img id="TTF_AZUL" src="Assets/img/shapes/TTF_AZUL.png" hidden>
	<img id="SN_AZUL" src="Assets/img/shapes/SN_AZUL.png" hidden>
	<img id="M_AZUL" src="Assets/img/shapes/M_AZUL.png" hidden>
	<img id="RP_AZUL" src="Assets/img/shapes/RP_AZUL.png" hidden>
	<img id="TE_AZUL" src="Assets/img/shapes/TE_AZUL.png" hidden>
	<img id="Layer_AZUL" src="Assets/img/shapes/Layer_AZUL.png" hidden>
	<img id="Binding_AZUL" src="Assets/img/shapes/Binding_AZUL.png" hidden>
	<img id="InfTransfer_AZUL" src="Assets/img/shapes/InfTransfer_AZUL.png" hidden>
	<img id="PM_AZUL" src="Assets/img/shapes/PM_AZUL.png" hidden>
	<img id="Process_AZUL" src="Assets/img/shapes/Process_AZUL.png" hidden>
	<img id="Input_AZUL" src="Assets/img/shapes/Input_AZUL.png" hidden>
	<img id="Output_AZUL" src="Assets/img/shapes/Output_AZUL.png" hidden>
	<img id="Datatype_AZUL" src="Assets/img/shapes/Datatype_AZUL.png" hidden>

	<img id="AF_VERDE" src="Assets/img/shapes/AF_VERDE.png" hidden>
	<img id="TTF_VERDE" src="Assets/img/shapes/TTF_VERDE.png" hidden>
	<img id="SN_VERDE" src="Assets/img/shapes/SN_VERDE.png" hidden>
	<img id="M_VERDE" src="Assets/img/shapes/M_VERDE.png" hidden>
	<img id="RP_VERDE" src="Assets/img/shapes/RP_VERDE.png" hidden>
	<img id="TE_VERDE" src="Assets/img/shapes/TE_VERDE.png" hidden>
	<img id="Layer_VERDE" src="Assets/img/shapes/Layer_VERDE.png" hidden>
	<img id="Binding_VERDE" src="Assets/img/shapes/Binding_VERDE.png" hidden>
	<img id="InfTransfer_VERDE" src="Assets/img/shapes/InfTransfer_VERDE.png" hidden>
	<img id="PM_VERDE" src="Assets/img/shapes/PM_VERDE.png" hidden>
	<img id="Process_VERDE" src="Assets/img/shapes/Process_VERDE.png" hidden>
	<img id="Datatype_VERDE" src="Assets/img/shapes/Datatype_VERDE.png" hidden>
	<img id="Input_VERDE" src="Assets/img/shapes/Input_VERDE.png" hidden>
	<img id="Output_VERDE" src="Assets/img/shapes/Output_VERDE.png" hidden>

	<img id="AF_ROXO" src="Assets/img/shapes/AF_ROXO.png" hidden>
	<img id="TTF_ROXO" src="Assets/img/shapes/TTF_ROXO.png" hidden>
	<img id="SN_ROXO" src="Assets/img/shapes/SN_ROXO.png" hidden>
	<img id="M_ROXO" src="Assets/img/shapes/M_ROXO.png" hidden>
	<img id="RP_ROXO" src="Assets/img/shapes/RP_ROXO.png" hidden>
	<img id="TE_ROXO" src="Assets/img/shapes/TE_ROXO.png" hidden>
	<img id="Layer_ROXO" src="Assets/img/shapes/Layer_ROXO.png" hidden>
	<img id="Binding_ROXO" src="Assets/img/shapes/Binding_ROXO.png" hidden>
	<img id="InfTransfer_ROXO" src="Assets/img/shapes/InfTransfer_ROXO.png" hidden>
	<img id="PM_ROXO" src="Assets/img/shapes/PM_ROXO.png" hidden>
	<img id="Process_ROXO" src="Assets/img/shapes/Process_ROXO.png" hidden>
	<img id="Datatype_ROXO" src="Assets/img/shapes/Datatype_ROXO.png" hidden>
	<img id="Input_ROXO" src="Assets/img/shapes/Input_ROXO.png" hidden>
	<img id="Output_ROXO" src="Assets/img/shapes/Output_ROXO.png" hidden>


<%@include file="../templates/footer.jsp"%>