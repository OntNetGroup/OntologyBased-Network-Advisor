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
		//var cells = element.attributes.attrs.equipment.data.cells;
		var cells = equipment.attr('equipment/data').cells;
		
		$.each(cells, function(index, cell) {
			
			if(cell.subType === 'Card') {
				cards.push(cell);
			}
			
		})
		
		return cards;
		
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
		
		//console.log('SUB LEN:' + JSON.stringify(subnetworks));
		
		var $this = this;
		var graph = app.graph;
		var paper = app.paper;
		
		graph.clear();
		
		// Garantir que as interfaces de entrada e saída permaneçam contidas em suas respectivas barras
		var position = undefined;
        paper.on('cell:pointerdown', function(cellView, evt) {
        	var cell = graph.getCell(cellView.model.id);
        	if(cell.type === 'link') return;
        	
        	position = cell.get('position');
        });
		
        paper.on('cell:pointerup', function(cellView, evt) {
        	var cell = graph.getCell(cellView.model.id);
        	if(cell.type === 'link') return;
        	
        	cell.set('position', position);
        });
        
        var index = 0;
		_.each(subnetworks, function(element, tech) {
			
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
			//layerNetwork.embed(subnetwork);

			var equipmentOffset = 0;
			if(subnetworks[tech].length > 0 && subnetworks[tech].length <= 16) {
				equipmentOffset = Math.floor(16/subnetworks[tech].length)
			}
			
			console.log('SUB LEN:' + subnetworks[tech].length);
			
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
				//subnetwork.embed(accessGroup);
				
			}, this);
			
			index++;
			
		}, this);
		
		
		
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