<%@include file="../templates/header.jsp"%>
<%
	String arborStructure = (String) request.getSession().getAttribute(
			"valuesGraph");
	int width = (Integer) request.getSession().getAttribute("width");
	int height = (Integer) request.getSession().getAttribute("height");
	String hashEquipIntOut = (String) request.getSession()
			.getAttribute("hashEquipIntOut");
	String hashTypes = (String) request.getSession().getAttribute(
			"hashTypes");
	String hashAllowed = (String) request.getSession().getAttribute("hashAllowed");
	String hashRPEquip = (String) request.getSession().getAttribute("hashRPEquip");
	int size = (Integer) request.getSession().getAttribute("size");

	//Get the parameters from controller
	String error = (String) request.getSession().getAttribute(
			"errorMensage");
	String loadOk = (String) request.getSession()
			.getAttribute("loadOk");
	request.getSession().setAttribute("loadOk", "");
%>
<script>
	graph = {};
	hashAllowed = Array();
	hash = {};
	hashRPEquip = {};
	
	<%
		out.println(hashAllowed);
		out.println(hashRPEquip);
	%>
	
	function addNodes(graph) {
		<%out.print(arborStructure);%>
			}
	
			function getHash() {
		<%out.print(hashTypes);%>
			return hash;
			}
	
	$(document)
			.ready(
					function() {
						canClickable = true;
						canShowContextMenu = true;
						targetURL = "open_g800_visualization_from_equip\?selected=";
						popupMessage = "Go to Equipment\'s components";
						possiblePopUp = "Equip";
						initHash();
<%if (size == 1)
				out.println("graph = startArbor(\"#viewport\", 1.0);");
			else
				out.println("graph = startArbor(\"#viewport\", 0.91);");%>
	addNodes(graph);

						initializeElements();

						$('#autoConnects').click(function() {
							loading();
						});
					});

	function initializeElements() {
		for (var key in hash) {
			graph.getNode(key).data.shape = graph.getNode(key).data.shape.split("_")[0]+"_ROXO";
		}
		
		var gambi = false;
		for(var key in hashAllowed){
			graph.getNode(hashAllowed[key]).data.shape = graph.getNode(hashAllowed[key]).data.shape.split("_")[0]+"_VERDE";
			gambi = false;
			for ( var interf in hashEquipIntOut[hashAllowed[key]]) {
				if (hashEquipIntOut[hashAllowed[key]][interf] == "true") {
					gambi = true;
					break;
				}
			}
			if (gambi) {
				graph.getNode(hashAllowed[key]).data.shape = graph.getNode(hashAllowed[key]).data.shape.split("_")[0]+"_ROXO";
			} 
		}
		
		graph.renderer.redraw();
	}

	function loading() {
		var maskHeight = $(document).height();
		var maskWidth = "100%";//$(document).width();

		//Define largura e altura do div#maskforloading iguais ás dimensões da tela
		$('#maskforloading').css({
			'width' : maskWidth,
			'height' : maskHeight
		});

		//efeito de transição
		$('#maskforloading').show();
	}

	function resetSelection() {
		initializeElements();
		currentSelection = false;
	}

	hashEquipIntIn = new Array();
	hashEquipIntOut = new Array();

	function initHash() {
<%out.print(hashEquipIntOut);%>
	}

	function requestRPs(){
		currentSelection = true;
		//Get Possible input interfaces

		$
			.ajax({
				url : "get_possible_connections?rp="
						+ srcRP,
				type : "GET",
				success : function(result) {
					$("#resetSelection").prop(
							"disabled", false);
					var str = result;
					var lines = str.split(";");
					hashEquipIntIn = new Array();

					//Set all equipments unavailable
					for (key in hash) {
						graph.getNode(key).data.shape = graph.getNode(key).data.shape.split("_")[0]+"_ROXO";
					}

					//Set the availability of each equipment
					for (var i = 0; i < lines.length; i++) {
						if (lines[i] == "")
							continue;
						var hsh = lines[i].split("#");
						
						hashEquipIntIn[hsh[0]] = hsh[1];

						//change the graph node color
						graph.getNode(hsh[0]).data.shape = graph.getNode(hsh[0]).data.shape.split("_")[0]+"_VERDE";
					}
					
					//verify if was a equip or a rp clicked
					//change the first clicked node
					if(typeof graph.getNode(srcRP) === "undefined") {
						graph.getNode(rpEquip).data.shape = graph.getNode(rpEquip).data.shape.split("_")[0]+"_LARANJA";							
					}else{
						graph.getNode(srcRP).data.shape = graph.getNode(srcRP).data.shape.split("_")[0]+"_LARANJA";
					}
					
					graph.renderer.redraw();
				},
				error : function() {
					alert("Something wrong happened.");
				}
			});
	}
	
	function doConnects(){
		loading();
		$
				.ajax({
					url : "do_connects?rp_src="
							+ srcRP
							+ "&rp_trg="
							+ trgRP
							+ "&rp_type="
							+ rpType,
					type : "GET",
					success : function(result) {
						if (result == "false") {
							alert("Something wrong happened.");
						} else {
							$("#resetSelection").prop(
									"disabled", true);
							var src, trg;
							
							if(typeof hashRPEquip[srcRP] === "undefined") 
								src = srcRP;
							else
								src = hashRPEquip[srcRP];
							
							if(typeof hashRPEquip[trgRP] === "undefined") 
								trg = trgRP;
							else
								trg = hashRPEquip[trgRP];
							

							//set disable the hashs
							if(typeof hashEquipIntOut[src] !== "undefined")
								hashEquipIntOut[src][trg] = "true";

							hashAllowed.splice(hashAllowed.indexOf(src),1);								
							
							currentSelection = false;
								//create a new edge
								var edgeName = "connects";
								graph
									.addEdge(
											graph
													.addNode(
															src,
															{
																shape :  graph.getNode(src).data.shape.split("_")[0]+"_VERDE"
															}),
											graph
													.addNode(
															trg,
															{
																shape : graph.getNode(trg).data.shape.split("_")[0]+"_VERDE"
															}),
											{
												name : edgeName
											});
							resetSelection();
						}
						$('#maskforloading').hide();
					},
					error : function() {
						alert("Erro");
					}
				});
	}
	
	function generateContextMenu(equip, src) {
		var equipMenu = new Array();
		var o = {};
		
		//If is not a Green node (possible node)
		if(graph.getNode(equip).data.shape.indexOf("_VERDE") == -1){
			return [ {
				"No connection available" : {
					disabled : true
				}
			} ];
		}
		
		if (src){
			
			if(typeof hashRPEquip[equip] === "undefined") {
				trgRP = equip;
				rpType = hashEquipIntIn[equip];
				doConnects();
				return;
			}
			
			equipHash = hashEquipIntIn[equip];
		}else{
			if(typeof hashEquipIntOut[equip] === "undefined") {
				srcRP = equip;
				requestRPs();
				return o[equip] = {
						disabled : true
				};
			}
			
			equipHash = hashEquipIntOut[equip];
		}

		for (key in equipHash) {
			o = {};
			if (src) {
				if (hashEquipIntIn[equip][key] == "false") {
					o[key] = {
						disabled : true
					};
				} else {
					o[key] = {
						onclick : function(menuItem, menu) {
							srcEquip = equip;
							srcRP = menuItem.textContent;
							
						}
					};
				}
			} else {
				if (hashEquipIntOut[equip][key] == "true") {
					o[key] = {
						disabled : true
					};
				} else {
					o[key] = {
						onclick : function(menuItem, menu) {
							rpEquip = equip;
							srcRP = menuItem.textContent;
							requestRPs();
						}
					};
				}
			}
			equipMenu.push(o);
		}
		
		if (equipMenu.length == 0) {
			return [ {
				"No connection available" : {
					disabled : true
				}
			} ];
		}
		return equipMenu;
	}

	currentSelection = false;
	srcRP = "";
	trgRP = "";
	typeRP = "";
	rpEquip = "";
</script>

<%
	if (error != null && !error.equals("")) {
		String htmlError = "<div class=\"alert alert-danger\">"
				+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>"
				+ "<strong>" + "Error! " + "</strong>" + error
				+ "</div>";
		out.println(htmlError);
	}

	if (loadOk != null && !loadOk.equals("")) {
		String htmlLoad = "<div class=\"alert alert-info\">"
				+ "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>"
				+ "<strong>Hey!</strong> " + loadOk + "</div>";
		out.println(htmlLoad);
	}
%>

<div class="col-lg-12">

	<h2>Provisioning settings:</h2>
	<div class="tooltip-demo well">
		<table>
			<tr>
				<td style="padding-right: 5px; padding-left: 20px;">
					<button type="button" id="resetSelection" disabled="disabled"
						onclick="resetSelection();">Reset Selection</button>
				</td>
				<td style="padding-right: 5px;">
					<form action="autoConnects" class="form-horizontal" method="POST"
						enctype="multipart/form-data">
						<input type="submit" id="autoConnects" name="submit"
							value="Automatic Connects" />
					</form>
				</td>
			</tr>
		</table>
		<div style="clear: both"></div>
	</div>
</div>


<br>
<%
	out.println("<div style=\"width:" + (width + 520) + "px\">");
%>
<div style="float: left; border: 1px solid black;">
	<%
		out.println("<canvas id=\"viewport\" width=\"" + width
				+ "\" height=\"" + height
				+ "\" style=\"background-color:white;\"></canvas>");
	%>
</div>
<div style="float: right;">
	<div id="currentNode">Select a node to visualize information
		about it.</div>
	<br> <img id="sub" src="Assets/img/subtitles/Provisoning.png"></img>
</div>
</div>

<script>
	(function($) {
		$.fn.drags = function(opt) {

			opt = $.extend({
				handle : "",
				cursor : "move"
			}, opt);

			if (opt.handle === "") {
				var $el = this;
			} else {
				var $el = this.find(opt.handle);
			}

			return $el
					.css('cursor', opt.cursor)
					.on(
							"mousedown",
							function(e) {
								if (opt.handle === "") {
									var $drag = $(this).addClass('draggable');
								} else {
									var $drag = $(this).addClass(
											'active-handle').parent().addClass(
											'draggable');
								}
								var z_idx = $drag.css('z-index'), drg_h = $drag
										.outerHeight(), drg_w = $drag
										.outerWidth(), pos_y = $drag.offset().top
										+ drg_h - e.pageY, pos_x = $drag
										.offset().left
										+ drg_w - e.pageX;
								$drag.css('z-index', 1000).parents().on(
										"mousemove",
										function(e) {
											$('.draggable').offset({
												top : e.pageY + pos_y - drg_h,
												left : e.pageX + pos_x - drg_w
											}).on(
													"mouseup",
													function() {
														$(this).removeClass(
																'draggable')
																.css('z-index',
																		z_idx);
													});
										});
								e.preventDefault(); // disable selection
							}).on(
							"mouseup",
							function() {
								if (opt.handle === "") {
									$(this).removeClass('draggable');
								} else {
									$(this).removeClass('active-handle')
											.parent().removeClass('draggable');
								}
							});

		}
	})(jQuery);

	$('#currentNode').drags();
	$('#sub').drags();
</script>

<!-- Images of ITU elements -->
<%@include file="../templates/tnokco_images_visualization.jsp"%>
<%@include file="../templates/footer.jsp"%>