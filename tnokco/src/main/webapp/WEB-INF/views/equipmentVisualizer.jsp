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
	hashAllowed = {};
	hash = {};
	
	<%
		out.println(hashAllowed);
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
						targetURL = "open_equipment_elements_visualization\?equip=";
						popupMessage = "Go to Equipment\'s components";

						initHash();
<%if (size == 1)
				out.println("graph = startArbor(\"#viewport\", 1.0);");
			else
				out.println("graph = startArbor(\"#viewport\", 0.91);");%>
	addNodes(graph);

						initializeElements();

						$('#autoBinds').click(function() {
							loading();
						});

						$('#autoConnects').click(function() {
							loading();
						});

						$('#autoBindsAndConnects').click(function() {
							loading();
						});

					});

	function initializeElements() {
		for (var key in hash) {
			graph.getNode(key).data.shape = graph.getNode(key).data.shape.split("_")[0]+"_ROXO";
		}
		
		var gambi = false;
		for(var key in hashAllowed){
			graph.getNode(key).data.shape = graph.getNode(key).data.shape.split("_")[0]+"_VERDE";
			gambi = false;
			for ( var interf in hashEquipIntOut[key]) {
				if (hashEquipIntOut[key][interf] == "true") {
					gambi = true;
					break;
				}
			}
			if (gambi) {
				graph.getNode(key).data.shape = graph.getNode(key).data.shape.split("_")[0]+"_ROXO";
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

	function generateContextMenu(equip, src) {
		var equipMenu = new Array();
		if (src)
			equipHash = hashEquipIntIn[equip];
		else
			equipHash = hashEquipIntOut[equip];

		for (key in equipHash) {
			var o = {};
			if (src) {
				if (hashEquipIntIn[equip][key] == "false") {
					o[key] = {
						disabled : true
					};
				} else {
					o[key] = {
						onclick : function(menuItem, menu) {
							srcEquipBindsClicked = equip;
							srcInterfaceBindsClicked = menuItem.textContent;
							loading();
							$
									.ajax({
										url : "connect_equip_binds?equip_source="
												+ srcEquipBindsClicked
												+ "&interface_source="
												+ srcInterfaceBindsClicked
												+ "&equip_target="
												+ trgEquipBindsClicked
												+ "&interface_target="
												+ trgInterfaceBindsClicked,
										type : "GET",
										success : function(result) {
											if (result == "false") {
												alert("Something wrong happened.");
											} else {
												$("#resetSelection").prop(
														"disabled", true);
												//set disable the hashs
												hashEquipIntOut[trgEquipBindsClicked][trgInterfaceBindsClicked] = "true";

												currentSelection = false;
												var edges = graph.getEdges(
														srcEquipBindsClicked,
														trgEquipBindsClicked);
												if (edges.length != 0) {
													edges[0].data.name += ",binds:"
															+ srcInterfaceBindsClicked
															+ "-"
															+ trgInterfaceBindsClicked;
												} else {
													//create a new edge
													var edgeName = "binds:"
															+ srcInterfaceBindsClicked
															+ "-"
															+ trgInterfaceBindsClicked;

													graph
															.addEdge(
																	graph
																			.addNode(
																					trgEquipBindsClicked,
																					{
																						shape :  graph.getNode(trgEquipBindsClicked).data.shape.split("_")[0]+"_VERDE"
																					}),
																	graph
																			.addNode(
																					srcEquipBindsClicked,
																					{
																						shape : graph.getNode(srcEquipBindsClicked).data.shape.split("_")[0]+"_VERDE"
																					}),
																	{
																		name : edgeName
																	});
												}
												//return the start color of nodes
// 												for ( var nodeKey in hash) {
// 													graph.getNode(nodeKey).data.shape = "Equip_VERDE";
// 												}
												resetSelection();
											}
											$('#maskforloading').hide();
										},
										error : function() {
											alert("Erro");
										}
									});
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
							trgEquipBindsClicked = equip;
							trgInterfaceBindsClicked = menuItem.textContent;
							//alert("Selected " + equip + ":"+ menuItem.innerText);
							currentSelection = true;
							//Get Possible input interfaces

							$
									.ajax({
										url : "get_input_interfaces_from?equip="
												+ trgEquipBindsClicked
												+ "&interf="
												+ trgInterfaceBindsClicked,
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
												if (!(hashEquipIntIn[hsh[0]] instanceof Array)) {
													hashEquipIntIn[hsh[0]] = new Array();
												}
												//hashEquipIntIn[equip][int_in] = boolean
												hashEquipIntIn[hsh[0]][hsh[1]] = hsh[2];

												//change the graph node color
												if (hsh[2] == "true") {
													graph.getNode(hsh[0]).data.shape = graph.getNode(hsh[0]).data.shape.split("_")[0]+"_VERDE";
												} else {
													if (graph.getNode(hsh[0]).data.shape.indexOf("VERDE") == -1) {
														graph.getNode(hsh[0]).data.shape = graph.getNode(hsh[0]).data.shape.split("_")[0]+"_ROXO";
													}
												}
											}
											//change the first clicked node
											graph.getNode(trgEquipBindsClicked).data.shape = graph.getNode(trgEquipBindsClicked).data.shape.split("_")[0]+"_AZUL";
											graph.renderer.redraw();
										},
										error : function() {
											alert("Something wrong happened.");
										}
									});
						}
					};
				}
			}
			equipMenu.push(o);
		}
		if (equipMenu.length == 0) {
			return [ {
				"No interfaces available" : {
					disabled : true
				}
			} ];
		}
		return equipMenu;
	}

	currentSelection = false;
	srcEquipBindsClicked = "";
	srcInterfaceBindsClicked = "";
	trgEquipBindsClicked = "";
	trgInterfaceBindsClicked = "";

	function selectIntOut(equip, interf) {
		alert(equip + ":" + interf);
	}

	function selectIntIn(equip, interf) {
		alert(equip + ":" + interf);
	}
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
				<td style="padding-right: 10px;">
				<div class="controls" style="margin-bottom:8px">
				<input type="radio"
					name="connectionType" value="bind"><b>Bind</b><br> <input
					type="radio" name="connectionType" value="connect"><b>Connect</b>
					</div>
				</td>
				<td style="padding-right: 5px; padding-left: 20px;">
					<button type="button" id="resetSelection" disabled="disabled"
						onclick="resetSelection();">Reset Selection</button>
				</td>
				<td style="padding-right: 5px;">
					<form action="autoBinds" class="form-horizontal" method="POST"
						enctype="multipart/form-data">
						<input type="submit" id="autoBinds" name="submit"
							value="Automatic Binds" />
					</form>
				</td>
				<td style="padding-right: 5px;">
					<form action="autoConnects" class="form-horizontal" method="POST"
						enctype="multipart/form-data">
						<input type="submit" id="autoConnects" name="submit"
							value="Automatic Connects" />
					</form>
				</td>
				<td>
					<form action="autoBindsAndConnects" class="form-horizontal"
						method="POST" enctype="multipart/form-data">
						<input type="submit" id="autoBindsAndConnects" name="submit"
							value="Automatic Binds and Connects" />
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