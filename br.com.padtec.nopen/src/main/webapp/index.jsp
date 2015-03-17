<html>
<head>
<link
	href="${pageContext.request.contextPath}/core/rappid_api/css/joint.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}//core/rappid_api/css/joint.ui.stencil.css"
	rel="stylesheet" type="text/css" />

<title>N-Open</title>

<style>
#paper {
	border: 1px solid gray;
	float: left;
	position: relative;
	width: 500px;
	height: 300px;
	overflow: hidden;
}

#stencil {
	position: relative;
	width: 200px;
	height: 200px;
	float: left;
}

.stencil>.content {
	border: 1px solid gray;
}
</style>
</head>
<body>

	<div id="stencil"></div>
	<div id="paper"></div>

	<div id="server_response" style="border: 1px solid red;"></div>

	<script
		src="${pageContext.request.contextPath}/core/rappid_api/js/joint.js"
		type="text/javascript"></script>

	<script
		src="${pageContext.request.contextPath}/core/rappid_api/js/handlebars.js"
		type="text/javascript"></script>

	<script
		src="${pageContext.request.contextPath}/core/rappid_api/js/template.js"
		type="text/javascript"></script>

	<script
		src="${pageContext.request.contextPath}/core/rappid_api/js/joint.ui.stencil.js"
		type="text/javascript"></script>

	<form id="target" action="dashboard.htm">
		<button id="btnn">Add</button>
		<input type="submit" value="Go">
	</form>

	<form id="advisor" action="advisor.htm">	
		<input type="submit" value="Open Advisor">
	</form>
		
	<form id="equipment-studio" action="equipment-studio.htm">	
		<input type="submit" value="Open Equipment Studio">
	</form>
	
	
	<script>
		/**
		Crating and setting the stencils for this pages
		 */
		graph = new joint.dia.Graph;
		var paper = new joint.dia.Paper({
			el : $('#paper'),
			width : 300,
			height : 200,
			gridSize : 1,
			model : graph
		});

		var stencil = new joint.ui.Stencil({
			graph : graph,
			paper : paper,
			width : 200,
			height : 200
		});
		$('#stencil').append(stencil.render().el);

		var r = new joint.shapes.basic.Rect({
			position : {
				x : 10,
				y : 10
			},
			size : {
				width : 50,
				height : 30
			},
			attrs : {
				rect : {
					fill : '#2ECC71'
				},
				text : {
					text : 'rect',
					fill : 'black'
				}
			}
		});
		var c = new joint.shapes.basic.Circle({
			position : {
				x : 70,
				y : 10
			},
			size : {
				width : 50,
				height : 30
			},
			attrs : {
				circle : {
					fill : '#9B59B6'
				},
				text : {
					text : 'circle',
					fill : 'white'
				}
			}
		});

		joint.shapes.basic.Diamond = joint.shapes.basic.Generic
				.extend({

					markup : '<g class="rotatable"><g class="scalable"><rect/></g><text/></g>',

					defaults : joint.util.deepSupplement({

						type : 'basic.Rect',
						attrs : {
							'rect' : {
								fill : '#FFFFFF',
								stroke : 'black',
								width : 1,
								height : 1,
								transform : 'rotate(45)'
							},
							'text' : {
								'font-size' : 14,
								text : '',
								'ref-x' : .5,
								'ref-y' : .5,
								ref : 'rect',
								'y-alignment' : 'middle',
								'x-alignment' : 'middle',
								fill : 'black',
								'font-family' : 'Arial, helvetica, sans-serif'
							}
						}

					}, joint.shapes.basic.Generic.prototype.defaults)
				});

		var diamond = new joint.shapes.basic.Diamond({
			position : {
				x : 100,
				y : 100
			},
			size : {
				width : 50,
				height : 50
			},
			attrs : {
				diamond : {
					width : 100,
					height : 30
				}
			}
		});
		diamond.attr({

			rect : {
				fill : '#cccccc',
				'stroke-width' : 2,
				stroke : 'black'
			},
			text : {
				text : 'Diamond',
				fill : '#3498DB',
				'font-size' : 12,
				'font-weight' : 'bold',
				'font-variant' : 'small-caps',
				'text-transform' : 'capitalize'
			}
		});

		var myPath = new joint.shapes.basic.Path({
			position : {
				x : 100,
				y : 50
			},
			size : {
				width : 60,
				height : 60
			},
			attrs : {
				path : {
					d : 'M 10 25 L 10 75 L 60 75 L 10 25'
				},
				//polygon: {points : "0,0 100,0 50,100"},
				text : {
					text : 'Triangle'
				},
			//'.svg-triangle polygon' : "fill:#98d02e, stroke:#65b81d, stroke-width:2"
			}
		});

		stencil.load([ r, c, myPath, diamond ]);

		// 				graph.on('add', function(cell, collection, opt) {
		// 					$.ajax({
		// 						type : "GET",
		// 						url : "node_added.htm",
		// 						data : {
		// 							'id' : cell.id,
		// 							'stencil' : opt.stencil
		// 						},
		// 						success: function (data) { alert(data); },
		// 				        error: function (e) { alert("error: "+e.status); }
		// 					});
		// 				});

		// 			console.log('New cell with id ' + cell.id
		// 					+ ' was added from the stencil with id ' + opt.stencil
		// 					+ '.');
		// 		});

		// 		graph.on('all', function(eventName, cell) {
		// 		    console.log(arguments);
		// 		});

		// 		graph.on('change:position', function(element) {
		// 		    alert("moved");
		// 			console.log(element.id, ':', element.get('position'));
		// 		});

		$("#btnn").click(function() {
			// 			$.ajax({
			// 				type : "GET",
			// 				url : "requestSomething.htm",
			// 				success : function(callback) {
			// 					eval(callback);
			// 				},
			// 				error : function(e) {
			// 					alert("error: " + e.status);
			// 				}
			// 			});
			$("#server_response").html(graph.toJSON());

			// 			$.ajax({
			// 				type : "POST",
			// 				url : "printJSON.htm",
			// 				data : {
			// 					'json' : graph.toJSON().cells
			// 				},
			// 				success : function(data) {
			// 					alert(data);
			// 				},
			// 				error : function(e) {
			// 					alert("error: " + e.status);
			// 				}
			// 			});

		});
	</script>
</body>
</html>