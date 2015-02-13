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
	
	String visualizationType = (String) request.getSession().getAttribute("visualizationType");
	
%>
<script>
		graph = {};
		$(document).ready(function() {
			initCanvas();
			
			$('#main-menu-toggle').click(function(){
		    	resizeCanvas();
		    });	 
		    
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
</script>
	
<div class="row" id="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>Visualization: 
					<%
					if(visualizationType.equals("allSites")){
						out.println("All Sites");						
					}else if(visualizationType.equals("allEquipments")){
						out.println("All Equipment");
					}else if(visualizationType.equals("allG800")){
						out.println("All G.800 Elements");
					}else if(visualizationType.equals("fromSite")){
						String site = (String) request.getSession().getAttribute("site");
						out.println("Elements from a specific Site (" + site + ")");						
					}else if(visualizationType.equals("fromEquip")){
						String equip = (String) request.getSession().getAttribute("equip");
						out.println("G.800 elements from a specific Equipment (" + equip + ")");						
					}else if(visualizationType.equals("allElements")){
						out.println("All network information");
					}else{
						out.println("UNDEFINED VIEW (" + visualizationType + ")");
					}
					%>
				</h2>
				<div class="box-icon"> 
					<a href="#" class="btn-minimize"><i class="icon-chevron-up" id="hideEquipmentType"></i></a>
				</div>
			</div>
			<div class="tooltip-demo well" id="canvas-div">
				<div class="floatingDiv">
					<div style="padding-left: 15px; margin-bottom:20px;">
						<button onclick="window.history.back();" style="float:left;" type="button" class="btn btn-prev"> <i class="icon-arrow-left"></i> Go back </button>
						<div style="clear:both"></div>
					</div>
				</div>
				<div class="floatingDiv" style="right: 35px; top: 60px;">
					<div id="currentNode">Select a node to visualize information about it.</div>
				</div>
				<div style="clear: both"></div>
				<%
					out.println("<canvas id=\"viewport\" width=\""+width+"\" height=\""+height+"\" style=\"\"></canvas>");
					
				%>
			</div>
		</div>
	</div>
</div>
			
<!-- Images of ITU elements -->
<%@include file="../templates/tnokco_images_visualization.jsp"%>
<%@include file="../templates/footer.jsp"%>