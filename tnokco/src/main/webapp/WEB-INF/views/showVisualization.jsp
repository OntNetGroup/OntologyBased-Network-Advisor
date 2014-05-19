<%@page import="com.hp.hpl.jena.sparql.util.StringUtils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@include file="../templates/header.jsp"%>
<%
	String arborStructure = (String) request.getSession().getAttribute("valuesGraph");
	int width  = (Integer) request.getSession().getAttribute("width");
	int height = (Integer) request.getSession().getAttribute("height");
	String hashEquipIntOut = (String) request.getSession().getAttribute("hashEquipIntOut");
	String hashTypes = (String) request.getSession().getAttribute("hashTypes");
	String nameSpace = (String) request.getSession().getAttribute("nameSpace");
	Boolean canClick = (Boolean) request.getSession().getAttribute("canClick");
	int size = (Integer) request.getSession().getAttribute("size");
	
%>
<script>
		graph = {};
		$(document).ready(function() {
			<%
				if(canClick){
					%>
					canClickable = true;
					targetURL = "<%out.print(request.getSession().getAttribute("targetURL"));%>";
					popupMessage = "<%out.print(request.getSession().getAttribute("popupMessage"));%>";
					<%
				}else{
					out.print("canClickable = false;");
				}
			if(size == 1)
				out.println("graph = startArbor(\"#viewport\", 1.0);");
			else
				out.println("graph = startArbor(\"#viewport\", 0.91);");
			%>
			
			addNodes(graph);
		});

		
		function addNodes(graph) {
			<%
				out.print(arborStructure);
			%>

		}
		function getHash() {
			hash = {};
			<%
				out.print(hashTypes);
			%>
			return hash;
		}
</script>
	<div style="padding-left: 15px; margin-bottom:20px;">
		<button onclick="window.history.back();" style="float:left;" type="button" class="btn btn-prev"> <i class="icon-arrow-left"></i> Go back </button>
		<div style="clear:both"></div>
	</div>

	<%
		out.println("<div style=\"width:"+(width+320)+"px\">");
	%>
<div style="float: left; border: 1px solid black;">
	<%
				out.println("<canvas id=\"viewport\" width=\""+width+"\" height=\""+height+"\" style=\"background-color:white;\"></canvas>");
				
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
	
	<!-- Images of ITU elements -->
<%@include file="../templates/tnokco_images_visualization.jsp"%>
<%@include file="../templates/footer.jsp"%>