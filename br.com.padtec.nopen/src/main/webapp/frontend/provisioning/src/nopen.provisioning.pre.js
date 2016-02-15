nopen.provisioning.PreProvisioning = Backbone.Model.extend({
	
	app : undefined,
	
	inititalize : function() {
		
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	start : function(app, subnetworks) {
		
		var graph = app.graph;
		var owl = this.app.owl;
		var file = this.app.file;
		var model = this.app.model;
		var util = this.app.util;
		
		var test = this.app.test;
		
		var links = [];
		
		if(graph.getLinks().length === 0 ) {
			model.generateProvisioning(app, subnetworks);
			return;
		}
		
		$.each(graph.getLinks(), function(index, value) {
			
			var link = graph.getCell(value.id);
			var source = graph.getCell(link.get('source').id);
			var target = graph.getCell(link.get('target').id);
			
			var sourceCards = model.getCardsInPreProvisioning(source);
			var targetCards = model.getCardsInPreProvisioning(target);
			
			links.push({
				'id' : link.id,
				'source' : source,
				'target' : target,
			});
			
			//console.log('SOURCE' + index + ": " + model.getElementName(source));
			//console.log('TARGET' + index + ": " + model.getElementName(target));
			
		});
		
		var dialog = new joint.ui.Dialog({
			width: 500,
			type: 'neutral',
			closeButton: false,
			title: 'Pre Provisioning',
			clickOutside: false,
			modal: false,
			content: 'Unconnected links were detected, click in the <b>Next</b> button to connect them.',
			buttons: [
			          { action: 'showImage', content: 'Show/Hide Image', position: 'right' },
			          { action: 'next', content: 'Next', position: 'right' },
			]
		});
		dialog.on('action:next', next);
		dialog.on('action:showImage', showImage);
		dialog.open();

		var currentLinkIndex = 1;
		$('.viewport').hide();
		$('.dialog .controls .control-button[data-action="showImage"]').hide();
		
		//block outside
		$('#black_overlay').show();
		
		function showImage() {
			if($('.dialog .fg .image').is(':visible')) {
				$('.dialog .fg .image').hide();
			}
			else {
				$('.dialog .fg .image').show();
			}
		};
		
		var connectionType = undefined;
		var hasPossibleConnection = false;
		
		var source = undefined;
		var target = undefined;
		
		var sourcePort = undefined;
		var targetPort = undefined;
		
		function next() {
			
			if(hasPossibleConnection && source && sourcePort && target && targetPort) {
				
				model.connectPortsInPreProvisioning(source, sourcePort, target, targetPort);
				
				//add horizontal or vertical connection
				
				var tfSource = model.getTFElementConnectedToPortInPreProvisioning(source, sourcePort.id);
				var tfTarget = model.getTFElementConnectedToPortInPreProvisioning(target, targetPort.id);
				
				if(connectionType === 'Horizontal') {
					owl.addHorizontalConnection(tfSource, tfTarget , sourcePort , targetPort);
				}
				else {
//					owl.addVerticalConnection(tfSource, tfTarget);
				}
				
				var sourcePortElement = owl.createElement(sourcePort.type, sourcePort.id, sourcePort.name);
				var targetPortElement = owl.createElement(targetPort.type, targetPort.id, targetPort.name);
				
				owl.connectPortsWithoutReasoning(sourcePortElement, targetPortElement);
			}
			
			$('.dialog .controls .control-button[data-action="showImage"]').show();
			
			//finish pre provisioning
			if (currentLinkIndex > links.length) {
				dialog.close();
				$('#black_overlay').hide();
				
				//execute reasoning
//				owl.executeReasoning();
				
				//generate provisioning
				model.generateProvisioning(app, subnetworks);
				$('.viewport').show();
				$('#btn-zoom-to-fit').click();
				return;
			}
			
			//disable button
			$('.dialog .controls .control-button[data-action="next"]').attr("disabled", true);
			
			var content = '';
			
			var link = links[currentLinkIndex - 1];
			
			source = graph.getCell(link.source.id);
			target = graph.getCell(link.target.id);
			
			console.log(model);
			var sourceName = model.getEquipmentName(link.source);
			console.log(sourceName);
			var targetName = model.getEquipmentName(link.target);
			
			if(currentLinkIndex <= links.length) {
				
				if(currentLinkIndex === links.length) {
					$('.dialog .controls .control-button[data-action="next"]').text('Finish');
				}
				
//				var outputs = owl.getOutputsFromOWL(source.id);
				
				connectionType = owl.getConnectionTypeFromOWL(source.id, target.id);
				var connections = owl.getPossibleConnectionsFromOWL(connectionType, source.id, target.id);
				
				//if there aren't ports to connected, go to next step
				if(Object.keys(connections[target.id]).length === 0 || Object.keys(connections[source.id]).length === 0) {
					
					var sourceName = model.getEquipmentName(link.source);
					var targetName = model.getEquipmentName(link.target);
					
					content = "No possible connection between " + sourceName + " and " + targetName + ".";
					$('.dialog .body').html(content);
					$('.dialog .fg .image').remove();
					$('.dialog .fg').append('<div class="image">' + file.getTopologySVG() + '</div>');
					
					$('.dialog .controls .control-button[data-action="next"]').attr("disabled", false);
					
					//set has possible connection to false
					hasPossibleConnection = false;
					
					//remove link from graph
					var removeLink = graph.getCell(link.id);
					removeLink.remove();
					
				}
				//if there are ports to connect, connect them
				else {
					//console.log('connections: ' + JSON.stringify(connections));
					content = createConnectionContent(source, target, connectionType, connections)
					
					$('.dialog .body').html(content);
					$('.dialog .fg .image').remove();
					$('.dialog .fg').append('<div class="image">' + file.getTopologySVG() + '</div>');
					
					util.prepareList('.sourceList');
					util.prepareList('.targetList');
					
					//generate events to connect ports
					generateConnectionEvents();
					
					//change topology svg image color
					changeTopologySVGColor(sourceName, targetName);
					
					//set has possible connection to true
					hasPossibleConnection = true;
				}
				
				currentLinkIndex++;
			}
			
		};
		
		//create connection content
		function createConnectionContent(source, target, connectionType, connections) {
			
			var sourceId = source.id
			var targetId = target.id;
			
			var sourceName = model.getEquipmentName(source);
			var targetName = model.getEquipmentName(target);
			
			var content = 
				'<div class="preProvisioningContainer">' +
					'<div class="connectionType"><b>Connection Type:</b> ' + connectionType + '</div>' +
					'<div class="sourceList"><div id="listContainer">' +
				    '<ul id="expList" class="list"> <li class="sourcePorts">' + sourceName + '</li> ' ;
			
			$.each(connections[sourceId], function(layer, value) {
				
				content = content + 
				    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
				
				$.each(connections[sourceId][layer], function(key, port) {
					
					content = content +
						'<ul style="display: block;">' +
			                '<li class="sourcePort" id="' + port.id + '" value="' + port.type + '" class="collapsed expanded" namer="' + port.name + '">' + port.exname + '</li>' +
			            '</ul>';
					
				});
				
				content = content + '</li>';
				
			});
			
			content = content + '</ul></div></div>';
			content = content + 
			'<div class="targetList"><div id="listContainer">' +
		    	'<ul id="expList" class="list"> <li class="targetPorts">' + targetName + '</li>';
			
			$.each(connections[targetId], function(layer, value) {
				
				content = content + 
				    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
				
				$.each(connections[targetId][layer], function(key, port) {
					
					console.log('PORT: ' + JSON.stringify(port));
					
					content = content +
						'<ul style="display: block;">' +
			                '<li class="targetPort" id="' + port.id + '" value="' + port.type + '" class="collapsed expanded" namer="' + port.name + '">' + port.exname + '</li>' +
			            '</ul>';
					
				});
				
				content = content + '</li>';
				
			});
			
			content = content + '</ul></div></div></div>';
			
			return content;
			
		};
		
		//generate events to connections list
		function generateConnectionEvents() {
			
			$('.sourceList #expList').find('li.sourcePort').click( function(event) {
				
				$('.sourceList #expList li').removeClass('active');
				$(this).toggleClass('active');

				sourcePort = {
						"type": $(this).attr('value'),
						"id": $(this).attr('id'),
						"name": $(this).attr('namer'),
						"edge": "source",
				}
				
				if($('.targetList #expList li').hasClass('active')) {
					$('.dialog .controls .control-button[data-action="next"]').attr("disabled", false);
				}
				
			});
			
			$('.targetList #expList').find('li.targetPort').click( function(event) {
				
				$('.targetList #expList li').removeClass('active');
				$(this).toggleClass('active');
				
				targetPort = {
						"type": $(this).attr('value'),
						"id": $(this).attr('id'),
						"name": $(this).text(),
						"edge": "target",
				}
				
				if($('.sourceList #expList li').hasClass('active')) {
					$('.dialog .controls .control-button[data-action="next"]').attr("disabled", false);
				}
				
			});
		}
		
		//change SVG color of topology
		function changeTopologySVGColor(sourceName, targetName) {
			
			//Set SVG elements
			$('.rotatable').each(function(){
				if($(this).text() === targetName || $(this).text() === sourceName) {
					var id = $(this).attr('id');
					$('#' + id + ' circle').attr('fill', "#FFF200");
				}
				else {
					var id = $(this).attr('id');
					$('#' + id + ' circle').attr('fill', "#00c6ff");
				}
			})
			
		};
		
	},
		
//		//create content
//		function createContent(sourceName, targetName, outputs) {
//			
//			var content = 
//				'<div class="preProvisioningContainer"><div class="outputsList"><div id="listContainer">' +
//				    '<ul id="expList" class="list"> <li class="outputs">' + sourceName + '</li> ' ;
//			
//			$.each(outputs, function(layer, value) {
//				
//				content = content + 
//				    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
//				
//				$.each(outputs[layer], function(key, output) {
//					
//					content = content +
//						'<ul style="display: block;">' +
//			                '<li class="outputItem" id="' + output.id + '" title="Output" value="' + output.name + '" class="collapsed expanded">' + output.name + '</li>' +
//			            '</ul>';
//					
//				});
//				
//				content = content + '</li>';
//				
//			});
//			
//			content = content + '</ul></div></div>';
//			content = content + 
//				'<div class="inputsList"><div id="listContainer">' +
//			    	'<ul id="expList" class="list"> <li class="inputs">' + targetName + '</li></ul>' + 
//			    '</div></div></div>';
//			
//			//content = content + '<div class="image">' + file.getTopologySVG() + '</div>'
//
//			return content;
//			
//		};
//		
//		//generate events to outputs list
//		function generateOutputEvents() {
//			
//			$('.outputsList #expList').find('li.outputItem').click( function(event) {
//				
//				$('.dialog .controls .control-button[data-action="next"]').attr("disabled", true);
//				
//				$('.outputsList #expList li').removeClass('active');
//				$(this).toggleClass('active');
//				
//				$('.inputsList #expList li:not(:first)').remove();
//				
//				var inputs = owl.getInputsFromOWL(target.id);
//				
//				var content = "";
//				$.each(inputs, function(layer, value) {
//					
//					content = content + 
//					    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
//					
//					$.each(inputs[layer], function(key, input) {
//						
//						content = content +
//							'<ul style="display: block;">' +
//				                '<li class="inputItem" id="' + input.id + '" title="Input" value="' + input.name + '" class="collapsed expanded">' + input.name + '</li>' +
//				            '</ul>';
//						
//					});
//					
//					content = content + '</li>';
//					
//				});
//				
//				$('.inputsList #expList').append(content);
//				util.prepareList('.inputsList');
//				
//				generateInputEvents();
//				
//			});
//			
//		};
//		
//		//generate events to inputs list
//		function generateInputEvents() {
//			
//			$('.inputsList #expList').find('li.inputItem').click(function(event) {
//				
//				$('.inputsList #expList li').removeClass('active');
//				$(this).toggleClass('active');
//				
//				$('.dialog .controls .control-button[data-action="next"]').attr("disabled", false);
//				
//			});
//			
//			$('.dialog .controls .control-button[data-action="next"]').click(function(event) {
//				
//				var output = {
//						"id": $('.outputItem.active').attr('id'),
//						"name": $('.outputItem.active').attr('value'),
//						"type": "Output_Card"
//				};
//				
//				var input = {
//						"id": $('.inputItem.active').attr('id'),
//						"name": $('.inputItem.active').attr('value'),
//						"type": "Input_Card"
//				};
//				
//				model.connectPortsInPreProvisioning(source, output, target, input);
//				
////				console.log('Out: ' + JSON.stringify(output));
////				console.log('In: ' + JSON.stringify(input));
//				
//			});
//			
//		};
//		
//	},
	
	
});