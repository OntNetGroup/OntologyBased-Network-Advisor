nopen.provisioning.Model = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function() {
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	//Method to get equipment name
	getEquipmentName : function(equipment) {
		return equipment.attr('text/text')
	},
	
	getEquipmentTechnology : function(equipment) {
		
		var tech = '';
		
		$.each(equipment.cells, function(key, element) {
			if(element.subType == 'Supervisor') {
				tech = element.tech;
			}
		});
		
		return tech;
	},
	
	
	//Method to get a ITU element by the id of it.
	getITUElementByID : function(equipment, id) {
		
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				if(itu.id === id) {
					return itu;
				}
				
			});
			
		});
		
	},
	
	//Method do get inputs as array of object ( inputs = [{ "type" : "", "id" : "", "name" : "" },...] )
	getInputs : function(equipment) {
		
		var inputs = [];
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				//if Input_Card, add new object in array
				if(itu.subtype === "Input_Card") {
					var input = {
						"type" : itu.subtype,	
						"id" : itu.id,
						"name" : itu.attrs.text.text,
					};
					inputs.push(input);
				}
				
			});
			
			console.log('INPUTS: ' + JSON.stringify(inputs));
			
		});
		
		return inputs;
		
	},
	
	//Method do get outputs as array of object ( outputs = [{ "type" : "", "id" : "", "name" : "" },...] )
	getOutputs : function(equipment) {
		
		var outputs = [];
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				//if Output_Card, add new object in array
				if(itu.subtype === "Output_Card") {
					var output = {
						"type" : itu.subtype,	
						"id" : itu.id,
						"name" : itu.attrs.text.text,
					};
					outputs.push(output);
				}
				
			});
			
			console.log('OUTPUTS: ' + JSON.stringify(outputs));
			
		});
		
		return inputs;
		
	},
	
	//method to connect input/output ports in pre provisioning
	connectPortsInPreProvisioning : function(source, sourcePort, target, targetPort) {
		
		var cards = undefined;
		var connected = false;
		
		var sourcePortId = sourcePort.id;
		var targetPortId = targetPort.id;
		
		//connect input to output
		cards = this.getCardsInPreProvisioning(source);
		$.each(cards, function(index, card){
			
			if(card.outPorts[sourcePortId]) {
				card.connectedPorts[sourcePortId] = targetPort;
			}
			
			if(card.inPorts[sourcePortId]) {
				card.connectedPorts[sourcePortId] = targetPort;
			}
			
		});
		
		//connect output to input
		cards = this.getCardsInPreProvisioning(target);
		$.each(cards, function(index, card){
			
			if(card.outPorts[targetPortId]) {
				card.connectedPorts[targetPortId] = sourcePort;
			}
			
			if(card.inPorts[targetPortId]) {
				card.connectedPorts[targetPortId] = sourcePort;
			}
			
		});
		
	},
	
	//method to connect input/output ports
	connectPorts : function(source, sourcePort, target, targetPort) {
		
		var cards = undefined;
		var connected = false;
		
		var sourcePortId = sourcePort.id;
		var targetPortId = targetPort.id;
		
		//connect input to output
		cards = this.getCards(source);
		$.each(cards, function(index, card){
			
			if(card.outPorts[sourcePortId]) {
				card.connectedPorts[sourcePortId] = targetPort;
			}
			
			if(card.inPorts[sourcePortId]) {
				card.connectedPorts[sourcePortId] = targetPort;
			}
			
		});
		
		//connect output to input
		cards = this.getCards(target);
		$.each(cards, function(index, card){
			
			if(card.outPorts[targetPortId]) {
				card.connectedPorts[targetPortId] = sourcePort;
			}
			
			if(card.inPorts[targetPortId]) {
				card.connectedPorts[targetPortId] = sourcePort;
			}
			
		});
		
	},
	
	
	//Method do get layers as array of object ( layers = [{ "type" : "", "id" : "", "name" : "" },...] )
	getLayers : function(equipment) {
		
		var layers = [];
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				//if Card_Layer, add new object in array
				if(itu.subtype === "Card_Layer") {
					var layer = {
						"type" : itu.subtype,	
						"id" : itu.id,
						"name" : itu.lanes.label,
					};
					layers.push(layer);
				}
				
			});
			
			console.log('LAYERS: ' + JSON.stringify(layers));
			
		});
		
		return layers;
		
	},
	
	//Method to get cards of a equipment
	getCards : function(equipment) {
		
		var cards = [];
		
		//var equip = $.parseJSON(equipment);
		var cells = equipment.get('equipment').data.cells;
		
		//var cells = equipment.attributes.attrs.equipment.data.cells;
		//var cells = equipment.attr('equipment/data').cells;

		$.each(cells, function(index, cell) {
			
			if(cell.subType === 'Card') {
				cards.push(cell);
			}
			
		})
		
		return cards;
		
	},
	
	//Method to get cards of a equipment
	getCardsInPreProvisioning : function(equipment) {
		
		var cards = [];
		
		//var cells = equipment.attributes.attrs.equipment.data.cells;
		var cells = equipment.attr('equipment/data').cells;

		$.each(cells, function(index, cell) {
			
			if(cell.subType === 'Card') {
				cards.push(cell);
			}
			
		})
		
		return cards;
		
	},
	
	//Method to get connected ports
	getConnectedPorts : function(equipment) {
		
		var cards = this.getCards(equipment);
		var ports = {};
		
		$.each(cards, function(index, card) {
			
			$.each(card.connectedPorts, function(sourceId, target){
				ports[sourceId] = target;
			});
			
		});
		
//		console.log('Ports Connected: ' + JSON.stringify(ports));
		
		return ports;
		
	},
	
	//Method to get output connected ports
	getOutputConnectedPorts : function(card) {
		
		var outputsPorts = [];
		
		$.each(card.connectedPorts, function(sourceId, target){
			
			if(target.type === "Input_Card") {
				outputsPorts.push(sourceId);
			}
			
		});
		
		console.log('Outputs Connected: ' + JSON.stringify(outputsPorts));
		
		return outputsPorts;
		
	},
	
	//Method to get input connected ports
	getInputConnectedPorts : function(card) {
		
		
	},
	
	getImplementedTechnologies : function() {
		var result = "error";
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "getImplementedTechnologies.htm",
			   success: function(data){
				   result = data;
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		
		return result;
	},
	
	
	getUppermostLayer : function(tech) {
		var result = "error";
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "getUppermostLayer.htm",
			   data: {
				   'technology' : tech
			   },
			   success: function(data){
				   result = data;
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		
		return result;
	},
	
	
	getEquipmentsByLayer : function(clientMEF) {
		var result = "error";
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "getEquipmentsByLayer.htm",
			   data: {
				   'clientMEF' : clientMEF
			   },
			   success: function(data){
				   result = data;
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		
		return result;
	},
	
	generateProvisioning : function(app, subnetworks) {
		
		var $this = this;
		var graph = app.graph;
		var paper = app.paper;
		
		var links = graph.getLinks();

		//get cards in pre provisioning
		var preCards = {};
		$.each(graph.getElements(), function(index, equipment) {
			var cards = $this.getCardsInPreProvisioning(equipment);
			preCards[equipment.id] = cards;
		});
		
//		console.log('Pre Cards: ' + JSON.stringify(preCards));
		
		graph.clear();
		
        var index = 0;
        var initialTech = undefined;
		_.each(subnetworks, function(element, tech) {
			
			if(!initialTech) {
				initialTech = tech;
			}
			
			var uppermostLayer = $this.getUppermostLayer(tech);
			var layerNetwork = Stencil.createLayerNetwork(tech, uppermostLayer);
			
			layerNetwork.attributes.position = $this.getLayerOffset(index);
			
			graph.addCell(layerNetwork);
			var layerPosition = {
					x : layerNetwork.attributes.position.x,
					y : layerNetwork.attributes.position.y,
			}
			
			var subnetwork = Stencil.createSubnetwork(tech);
			graph.addCell(subnetwork);
			
			var subnetworkPosition = {
					x : layerPosition.x + 55,
					y : layerPosition.y + 25,
			}
			
			subnetwork.translate(subnetworkPosition.x, subnetworkPosition.y);
			layerNetwork.embed(subnetwork);

			var equipmentOffset = 0;
			if(subnetworks[tech].length > 0 && subnetworks[tech].length <= 16) {
				equipmentOffset = Math.floor(16/subnetworks[tech].length)
			}
			
			var equipIndex = 0;
			_.each(subnetworks[tech], function(equipment, equipmentID) {
				
				var equip = equipment.attributes.attrs.equipment;
				var accessGroup = Stencil.createAccessGroup(equipment.id, equip);
				graph.addCell(accessGroup);
				
				var offset = $this.getEquipmentOffset(equipIndex * equipmentOffset);
				var position = {
						x : subnetworkPosition.x + offset.x,
						y : subnetworkPosition.y - offset.y,
				}
				accessGroup.set('position', position);
				
				equipIndex ++;
				subnetwork.embed(accessGroup);
				
			}, this);
			
			index++;
			
		}, this);
		
		$.each(links, function(index, link) {
			
			//link.attr('.pointer-events:', 'visiblePainted');
			link.prop('z', 100);
//			console.log('LINK: ' + JSON.stringify(link));
			graph.addCell(link);
			
		});
		
		$.each(graph.getElements(), function(index, equipment) {
			
			if(equipment.get('subtype') === 'Access_Group') {
				
				var cells = equipment.get('equipment').data.cells;
				
				$.each(cells, function(index, cell) {
					
					if(cell.subType === 'Card') {
						$.each(preCards[equipment.id], function(index, card) {
							
							if(card.id === cell.id) {
								equipment.get('equipment').data.cells[index] = card;
//								console.log('CARD: ' + JSON.stringify(card));
//								console.log('SET NEW CARD! ' + JSON.stringify(equipment.get('equipment').data.cells[index]));
							}
							
						});
					}
					
				});
				
			}
			
		});
		
		//hide links
		$this.hideLinks();
		$this.showTech(graph, initialTech);
		
		
	},
	
	showTech : function(graph, tech) {

		var $this = this;
		
		$.each(graph.getElements(), function(index, element) {
			element.attr('.rotatable', { display : 'none' });
		});

		$.each(graph.getElements(), function(index, element) {
			
			if(element.get('subtype') === 'Layer_Network' && element.get('technology') === tech) {
				element.attr('.rotatable', { display : 'normal' });
				console.log('EMBEDS: ' + element.get('embeds')[0]);
				$this.showEmbeds(graph, element.get('embeds')[0]);
			}
			
		});
		
	},
	
	showEmbeds : function(graph, elementId) {
		
		var $this = this;
		var embeddedElement = graph.getCell(elementId);
		embeddedElement.attr('.rotatable', { display : 'normal' });
		
		if(embeddedElement.get('embeds')) {
			$.each(embeddedElement.get('embeds'), function(index, element) {
				$this.showEmbeds(graph, element);
			});
		}
		
	},
	
	hideLinks : function() {
		$('.link').hide();
		$('.handle.e.link').show();
	},
	
	showLink : function(linkId) {
		$('.link[model-id="' + linkId + '"]').show();
	},
	
	getLayerOffset : function(index) {
		
		var offset = {
				x : undefined,
				y : undefined,
		}
		
		switch(index) {
			case 0 :
				offset.x = 250;
				offset.y = 100;
				break;
			case 1 :
				offset.x = 250;
				offset.y = 320;
				break;
			case 2 :
				offset.x = 250;
				offset.y = 540;
				break;
			case 3 :
				offset.x = 250;
				offset.y = 760;
				break;
			case 4 :
				offset.x = 250;
				offset.y = 980;
				break;
			default :
				break;
		}
		
		return offset;
	},
	
	getEquipmentOffset : function(index) {
	
		var offset = {
				x : undefined,
				y : undefined,
		}
		
		switch(index) {
			case 0 :
				offset.x = 185;
				offset.y = 15;
				break;
			case 1 :
				offset.x = 245;
				offset.y = 12;
				break;
			case 2 :
				offset.x = 305;
				offset.y = 2;
				break;
			case 3 :
				offset.x = 355;
				offset.y = -20;
				break;
			case 4 :
				offset.x = 385;
				offset.y = -60;
				break;
			case 5 :
				offset.x = 355;
				offset.y = -100;
				break;
			case 6 :
				offset.x = 305;
				offset.y = -120;
				break;
			case 7 :
				offset.x = 245;
				offset.y = -132;
				break;
			case 8 :
				offset.x = 185;
				offset.y = -135;
				break;
			case 9 :
				offset.x = 125;
				offset.y = -130;
				break;
			case 10 :
				offset.x = 65;
				offset.y = -120;
				break;
			case 11 :
				offset.x = 15;
				offset.y = -100;
				break;
			case 12 :
				offset.x = -15;
				offset.y = -60;
				break;
			case 13 :
				offset.x = 15;
				offset.y = -20;
				break;
			case 14 :
				offset.x = 65;
				offset.y = 2;
				break;
			case 15 :
				offset.x = 125;
				offset.y = 12;
				break;
			default:
				break;
		}
		
		return offset;
		
	},
	
});