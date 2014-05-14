<%@include file="../templates/header.jsp"%>
<%
	String arborStructure = (String) request.getSession().getAttribute("valuesGraph");
	int width  = (Integer) request.getSession().getAttribute("width");
	int height = (Integer) request.getSession().getAttribute("height");
	String hashEquipIntOut = (String) request.getSession().getAttribute("hashEquipIntOut");
	String hashTypes = (String) request.getSession().getAttribute("hashTypes");
%>
<script>
		graph = {};
		$(document).ready(function() {
			canClickable = true;
			canShowContextMenu = true;
			targetURL = "open_equipment_elements_visualization\?equip=";
			popupMessage = "Go to Equipment\'s components";

			initHash();

			graph = startArbor("#viewport", 0.91);
			addNodes(graph);
		});

		function loading()
		{
		 	var maskHeight = $(document).height();
			var maskWidth = "100%";//$(document).width();
		
			//Define largura e altura do div#maskforloading iguais ás dimensões da tela
			$('#maskforloading').css({'width':maskWidth,'height':maskHeight});
			
			//efeito de transição
			$('#maskforloading').show();
		}
		
		function resetSelection(){
			for (key in hash) {
				graph.getNode(key).data.shape = "Equip_AZUL";
			}
			currentSelection = false;
		}
		
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

		hashEquipIntIn = new Array();
		hashEquipIntOut = new Array();

		function initHash() {
			<%
				out.print(hashEquipIntOut);
			%>
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
					if(hashEquipIntIn[equip][key] == "false"){
						o[key] = {disabled:true};
					}else{
						o[key] = {
							onclick : function(menuItem, menu) {
								srcEquipBindsClicked = equip;
								srcInterfaceBindsClicked = menuItem.textContent;
								loading();
								$.ajax({
									url : "connect_equip_binds?equip_source="+srcEquipBindsClicked+"&interface_source="+srcInterfaceBindsClicked+"&equip_target="+trgEquipBindsClicked+"&interface_target="+trgInterfaceBindsClicked,
									type : "GET",
									success : function(result) {
										if(result == "false")
										{
											alert("Something wrong happened.");
										} else {
											$("#resetSelection").prop( "disabled", true);
											//set disable the hashs
											hashEquipIntOut[trgEquipBindsClicked][trgInterfaceBindsClicked] = "true";
											
											currentSelection = false;
											var edges = graph.getEdges(srcEquipBindsClicked,trgEquipBindsClicked);
											if(edges.length != 0){
												edges[0].data.name += ",binds:"+srcInterfaceBindsClicked+"-"+trgInterfaceBindsClicked;
											}else{
												//create a new edge
												var edgeName = "binds:"+srcInterfaceBindsClicked+"-"+trgInterfaceBindsClicked;
											
												graph.addEdge(graph.addNode(trgEquipBindsClicked, {
													shape : "Equip_AZUL"
												}), graph.addNode(srcEquipBindsClicked, {
													shape : "Equip_AZUL"
												}), {
													name : edgeName
												});
											}
											//return the start color of nodes
											for(var nodeKey in hash){
												graph.getNode(nodeKey).data.shape = "Equip_AZUL";
											}
											graph.renderer.redraw();
										}
										$('#maskforloading').hide();
									},
									error:function(){
										alert("Erro");
									}					
								});
							}
						};
					}
				} else {
					if(hashEquipIntOut[equip][key] == "true"){
						o[key] = {disabled:true};
					}else{
						o[key] = {
							onclick : function(menuItem, menu) {
								trgEquipBindsClicked = equip;
								trgInterfaceBindsClicked = menuItem.textContent;
								//alert("Selected " + equip + ":"+ menuItem.innerText);
								currentSelection = true;
								//Get Possible input interfaces
								
								$.ajax({
									url : "get_input_interfaces_from?equip="+trgEquipBindsClicked+"&interf="+trgInterfaceBindsClicked,
									type : "GET",
									success : function(result) {
										$("#resetSelection").prop( "disabled", false);
										var str = result;
										var lines = str.split(";");
										hashEquipIntIn = new Array();
										for(var i = 0; i < lines.length; i++){
											if(lines[i] == "")
												continue;
											var hsh = lines[i].split("#");
											if (!(hashEquipIntIn[hsh[0]] instanceof Array)) {
												hashEquipIntIn[hsh[0]] = new Array();
											}	
											//hashEquipIntIn[equip][int_in] = boolean
											hashEquipIntIn[hsh[0]][hsh[1]] = hsh[2];
											
											//change the graph node color
											if(hsh[2] == "true"){
												graph.getNode(hsh[0]).data.shape = "Equip_VERDE";												
											}else{
												if(graph.getNode(hsh[0]).data.shape != "Equip_VERDE"){
													graph.getNode(hsh[0]).data.shape = "Equip_ROXO";
												}
											}
										}
										//change the first clicked node
										graph.getNode(trgEquipBindsClicked).data.shape = "Equip_AZUL";
										graph.renderer.redraw();
									},
									error:function(){
										alert("Something wrong happened.");
									}					
								});
							}
						};
					}
				}
				equipMenu.push(o);
			}
			if(equipMenu.length == 0){
				return [{"No interfaces available":{disabled:true} }];
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
	<button type="button" id="resetSelection" disabled="disabled" onclick="resetSelection();" style="margin-bottom:10px;">Reset Selection</button>
	<button type="button" id="doAutoBinds" onclick="doAutoBinds();" style="margin-bottom:10px;">Do Automatic Binds</button>
	<button type="button" id="doAutoConnects" onclick="doAutoConnects();" style="margin-bottom:10px;">Do Automatic Connects</button>
	<button type="button" id="doAutoBindsAndConnects" onclick="doAutoBindsAndConnects();" style="margin-bottom:10px;">Do Automatic Binds and Connects</button>
	<br>
<%
		out.println("<div style=\"width:"+(width+520)+"px\">");
	%>
	<div style="float: left; border: 1px solid black;">
			<%
				out.println("<canvas id=\"viewport\" width=\""+width+"\" height=\""+height+"\" style=\"background-color:white;\"></canvas>");
				
			%>
	</div>
	<div style="float: right;">
		<div id="currentNode">Select a node to visualize information
			about it.</div>
			<br>
		<img id="sub" src="Assets/img/subtitles/Provisoning.png"></img>
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
											var $drag = $(this).addClass(
													'draggable');
										} else {
											var $drag = $(this).addClass(
													'active-handle').parent()
													.addClass('draggable');
										}
										var z_idx = $drag.css('z-index'), drg_h = $drag
												.outerHeight(), drg_w = $drag
												.outerWidth(), pos_y = $drag
												.offset().top
												+ drg_h - e.pageY, pos_x = $drag
												.offset().left
												+ drg_w - e.pageX;
										$drag
												.css('z-index', 1000)
												.parents()
												.on(
														"mousemove",
														function(e) {
															$('.draggable')
																	.offset(
																			{
																				top : e.pageY
																						+ pos_y
																						- drg_h,
																				left : e.pageX
																						+ pos_x
																						- drg_w
																			})
																	.on(
																			"mouseup",
																			function() {
																				$(
																						this)
																						.removeClass(
																								'draggable')
																						.css(
																								'z-index',
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
											$(this)
													.removeClass(
															'active-handle')
													.parent().removeClass(
															'draggable');
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