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

<script src="jquery.js"></script>
<script src="arbor.js"></script>
<script src="graphics.js"></script>
<script src="main.js"></script>
<script src="jquery.contextmenu.js"></script>
<link rel="stylesheet" type="text/css" href="contextMenu.css"
	media="all">

</head>
<body>

	<script>
		graph = {};
		$(document).ready(function() {
			canClickable = true;
			canShowContextMenu = true;
			targetURL = "open_equipment_elements_visualization\?equip=";
			popupMessage = "Go to Equipment\'s components";

			initHash();

			graph = startArbor("#viewport", 1.0);
			addNodes(graph);
		});

		$(document).keyup(function(e) {
			  if (e.keyCode == 27) { 
				//esc pressed
				curretSelection = false;
				}   
			});
		
		function addNodes(graph) {

			graph.addEdge(graph.addNode("eq1", {
				shape : "dot",
				color : "green"
			}), graph.addNode("eq2", {
				shape : "dot",
				color : "green"
			}), {
				name : 'binds:eq1.out1-eq2.in1'
			});
			graph.addEdge(graph.addNode("eq1", {
				shape : "dot",
				color : "green"
			}), graph.addNode("eq3", {
				shape : "dot",
				color : "green"
			}), {
				name : 'binds:eq1.out2-eq3.in1'
			});
			graph.addEdge(graph.addNode("eq3", {
				shape : "dot",
				color : "green"
			}), graph.addNode("eq2", {
				shape : "dot",
				color : "green"
			}), {
				name : 'binds:eq3.out1-eq2.in2'
			});
			graph.addEdge(graph.addNode("eq4", {
				shape : "dot",
				color : "green"
			}), graph.addNode("eq2", {
				shape : "dot",
				color : "green"
			}), {
				name : 'binds:eq4.out1-eq2.in3'
			});

		}
		function getHash() {
			var hash = {};
			hash["af2"] = "<b>af2 is an individual of classes: </b><br><ul><li>Adaptation_Sink</li><li>Adaptation_Function</li><li>Transport_Processing_Function</li></ul>";
			hash["in2"] = "<b>in2 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Input</li><li>Mapped_TF_Input</li></ul>";
			hash["af1"] = "<b>af1 is an individual of classes: </b><br><ul><li>Adaptation_Source</li><li>Adaptation_Function</li><li>Transport_Processing_Function</li></ul>";
			hash["in1"] = "<b>in1 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Input</li><li>Mapped_TF_Input</li></ul>";
			hash["s2"] = "<b>s2 is an individual of classes: </b><br><ul><li>Sink_Site</li><li>Site</li></ul>";
			hash["in4"] = "<b>in4 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Input</li><li>Mapped_TF_Input</li></ul>";
			hash["s1"] = "<b>s1 is an individual of classes: </b><br><ul><li>Site</li><li>Source_Site</li></ul>";
			hash["in3"] = "<b>in3 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Input</li><li>Mapped_TF_Input</li></ul>";
			hash["tf1"] = "<b>tf1 is an individual of classes: </b><br><ul><li>Termination_Source</li><li>Transport_Processing_Function</li><li>Termination_Function</li></ul>";
			hash["iin4"] = "<b>iin4 is an individual of classes: </b><br><ul><li>Mapped_Input_Interface</li><li>Input_Interface</li><li>Connected_Reference_Point</li></ul>";
			hash["out3"] = "<b>out3 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Mapped_TF_Output</li><li>Output</li></ul>";
			hash["out2"] = "<b>out2 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Mapped_TF_Output</li><li>Output</li></ul>";
			hash["iin2"] = "<b>iin2 is an individual of classes: </b><br><ul><li>Bound_Input_Interface_Equipment</li><li>Mapped_Input_Interface</li><li>Input_Interface</li><li>Bound_Input_Interface</li></ul>";
			hash["iin3"] = "<b>iin3 is an individual of classes: </b><br><ul><li>Bound_Input_Interface_Equipment</li><li>Mapped_Input_Interface</li><li>Input_Interface</li><li>Bound_Input_Interface</li></ul>";
			hash["out4"] = "<b>out4 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Mapped_TF_Output</li><li>Output</li></ul>";
			hash["iin1"] = "<b>iin1 is an individual of classes: </b><br><ul><li>Mapped_Input_Interface</li><li>Input_Interface</li></ul>";
			hash["out1"] = "<b>out1 is an individual of classes: </b><br><ul><li>Geographical_Element</li><li>Mapped_TF_Output</li><li>Output</li></ul>";
			hash["eq4"] = "<b>eq4 is an individual of classes: </b><br><ul><li>Equipment</li></ul>";
			hash["iout3"] = "<b>iout3 is an individual of classes: </b><br><ul><li>Output_Interface</li><li>Mapped_Output_Interface</li></ul>";
			hash["iout4"] = "<b>iout4 is an individual of classes: </b><br><ul><li>Output_Interface</li><li>Mapped_Output_Interface</li><li>Bound_Output_Interface_Equipment</li><li>Bound_Output_Interface</li></ul>";
			hash["iout1"] = "<b>iout1 is an individual of classes: </b><br><ul><li>Output_Interface</li><li>Mapped_Output_Interface</li><li>Bound_Output_Interface_Equipment</li><li>Bound_Output_Interface</li></ul>";
			hash["iout2"] = "<b>iout2 is an individual of classes: </b><br><ul><li>Output_Interface</li><li>Mapped_Output_Interface</li><li>Connected_Reference_Point</li></ul>";
			hash["eq3"] = "<b>eq3 is an individual of classes: </b><br><ul><li>Equipment</li></ul>";
			hash["tf2"] = "<b>tf2 is an individual of classes: </b><br><ul><li>Termination_Sink</li><li>Transport_Processing_Function</li><li>Termination_Function</li></ul>";
			hash["eq2"] = "<b>eq2 is an individual of classes: </b><br><ul><li>Equipment</li></ul>";
			hash["eq1"] = "<b>eq1 is an individual of classes: </b><br><ul><li>Equipment</li></ul>";
			return hash;
		}

		hashEquipIntIn = new Array();
		hashEquipIntOut = new Array();

		function initHash() {
			hashEquipIntIn['eq1'] = new Array();
			hashEquipIntIn['eq1']['eq1.in1'] = false;
			hashEquipIntIn['eq1']['eq1.in2'] = false;
			hashEquipIntIn['eq1']['eq1.in3'] = false;

			hashEquipIntIn['eq2'] = new Array();
			hashEquipIntIn['eq2']['eq2.in1'] = true;
			hashEquipIntIn['eq2']['eq2.in2'] = true;
			hashEquipIntIn['eq2']['eq2.in3'] = true;

			hashEquipIntIn['eq3'] = new Array();
			hashEquipIntIn['eq3']['eq3.in1'] = true;
			hashEquipIntIn['eq3']['eq3.in2'] = false;

			hashEquipIntIn['eq4'] = new Array();
			hashEquipIntIn['eq4']['eq4.in1'] = false;

			hashEquipIntOut['eq1'] = new Array();
			hashEquipIntOut['eq1']['eq1.out1'] = true;
			hashEquipIntOut['eq1']['eq1.out2'] = true;

			hashEquipIntOut['eq2'] = new Array();
			hashEquipIntOut['eq2']['eq2.out1'] = false;
			hashEquipIntOut['eq2']['eq2.out2'] = false;

			hashEquipIntOut['eq3'] = new Array();
			hashEquipIntOut['eq3']['eq3.out1'] = false;
			hashEquipIntOut['eq3']['eq3.out2'] = true;

			hashEquipIntOut['eq4'] = new Array();
			hashEquipIntOut['eq4']['eq4.out1'] = false;
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
					if(hashEquipIntIn[equip][key]){
						o[key] = {disabled:true}
					}else{
						o[key] = {
							onclick : function(menuItem, menu) {
								srcEquipBindsClicked = equip;
								srcInterfaceBindsClicked = menuItem.innerText;
								alert("Selected " + equip + ":"+ menuItem.innerText);
								
								//create ajax requisition
								var json = {
										"ok" : true
									};

								$.ajax({
									url : "connect_equip_binds?equip_source="+srcEquipBindsClicked+"&interface_source="+srcInterfaceBindsClicked+"&equip_target="+trgEquipBindsClicked+"&interface_target="+trgInterfaceBindsClicked,
									data : JSON.stringify(json),
									type : "GET",
									contentType: "application/json; charset=utf-8",
									dataType: "json",

									success : function(dto) {
										if(dto.ok == false)
										{
											alert(dto.result);
										} else {
											alert("binds created");
// 											currentSelection = false;
// 											var edgeName = graph.getEdges(srcEquipBindsClicked,trgEquipBindsClicked)[0].data.name+",binds:"+srcInterfaceBindsClicked+"-"+trgInterfaceBindsClicked;
											
// 											graph.addEdge(graph.addNode(srcEquipBindsClicked, {
// 												shape : "dot",
// 												color : "green"
// 											}), graph.addNode(trgEquipBindsClicked, {
// 												shape : "dot",
// 												color : "green"
// 											}), {
// 												name : edgeName
// 											});
										}
									},
									error:function(x,e){
// 										alert("Erro");
										//set disable the hashs
										hashEquipIntOut[srcEquipBindsClicked][srcInterfaceBindsClicked] = true;
										hashEquipIntIn[trgEquipBindsClicked][trgInterfaceBindsClicked] = true;
										
										currentSelection = false;
										var edges = graph.getEdges(srcEquipBindsClicked,trgEquipBindsClicked);
										if(edges !== "undefined")
											var edgeName = edges[0].data.name+",binds:"+srcInterfaceBindsClicked+"-"+trgInterfaceBindsClicked;
										else
											var edgeName = "binds:"+srcInterfaceBindsClicked+"-"+trgInterfaceBindsClicked;
										
										graph.addEdge(graph.addNode(srcEquipBindsClicked, {
											shape : "dot",
											color : "green"
										}), graph.addNode(trgEquipBindsClicked, {
											shape : "dot",
											color : "green"
										}), {
											name : edgeName
										});
									},					
								});
							}
						}
					}
				} else {
					if(hashEquipIntIn[equip][key]){
						o[key] = {disabled:true}
					}else{
						o[key] = {
							onclick : function(menuItem, menu) {
								trgEquipBindsClicked = equip;
								trgInterfaceBindsClicked = menuItem.innerText;
								alert("Selected " + equip + ":"+ menuItem.innerText);
								currentSelection = true;
							}
						}
					}
				}
				equipMenu.push(o)
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
	<div style="width: 1120px">

		<div style="float: left; border: 1px solid black;">
			<canvas id="viewport" width="800" height="600"
				style="background-color: white;"></canvas>

		</div>
		<div style="float: right;">
			<div id="currentNode">Select a node to visualize information
				about it.</div>
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
		</script>
</body>
</html>
