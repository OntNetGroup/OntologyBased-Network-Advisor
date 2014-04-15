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
		graph = startArbor("#viewport",0.9);
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
	
	<!-- Images of ITU elements -->
<%@include file="../templates/tnokco_images_visualization.jsp"%>

</body>
</html>
