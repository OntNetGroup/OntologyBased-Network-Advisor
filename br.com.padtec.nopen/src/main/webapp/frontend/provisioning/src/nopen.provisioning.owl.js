nopen.provisioning.OWL = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function() {
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	//Method to parse card JSON file to generate OWL instances
	parseCardToOWL : function(equipment, card) {
		
		var $this = this;
		
		/*
		 * Supervisor > Equipment
		 * Supervisor > Card
		 * Card_Layer > TTF
		 * Card > Card_Layer
		 * Card > AF
		 * Card > Matrix
		 * Card > Input/Output
		 * TTF/AF/Matrix > Input/Output
		 */
		
		var elements = [];
		var links = [];
		
		//Supervisor
//		var supervisor = {
//				"type" : "Supervisor",
//				"id" : card.SupervisorID,
//				"name" : card.Supervisor,
//		};
//		elements.push(supervisor);
		
		//Equipment
		var equip = {
				"type" : "Equipment",
				"id" : equipment.id,
				"name" : equipment.attributes.attrs.text.text,
		};
		elements.push(equip);
		
		//Card
		var equipCard = {
				"type" : card.subType,
				"id" : card.id,
				"name" : card.attrs.name.text,
		};
		elements.push(equipCard);
		
		//Equipment (E) > Card (C)
		var linkEC = {
				"sourceType" : "Equipment",
				"targetType" : card.subType,
				"source" : equipment.id,
				"target" : card.id,
		};
		links.push(linkEC);
		
		
		//Supervisor (S) > Equipment (E)
//		var linkSE = {
//				"sourceType" : "Supervisor",
//				"targetType" : "Equipment",
//				"source" : card.SupervisorID,
//				"target" : equipment.id,
//		};
//		links.push(linkSE);
		
		//Supervisor (S) > Card (C)
//		var linkSC = {
//				"sourceType" : "Supervisor",
//				"targetType" : card.subType,
//				"source" : card.SupervisorID,
//				"target" : card.id,
//		};
//		links.push(linkSC);
		
		//ITU Elements
		var cardCells = card.attrs.data.cells;
		
		
		
		$.each(cardCells, function(index, element) {
			
			//Card_Layer
			if(element.subtype === 'Card_Layer') {
				//console.log('Layer: ' + JSON.stringify(element));
				var layer = {
						"type" : element.subtype,
						"id" : element.lanes.label,
						"name" : element.lanes.label,
				};
				elements.push(layer);
				
				//Card > Card_Layer
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.lanes.label,
				};
				links.push(link);
				
			}
			//Trail_Termination_Function
			else if (element.subtype === 'Trail_Termination_Function') {
				
				var ttf = {
						"type" : element.subtype,
						"id" : element.id,
						"name" : element.attrs.text.text,
				}
				elements.push(ttf);
				
				//Card_Layer > TTF
				
				var link = {
						"sourceType" : $this.getElementType(cardCells, element.parent),
						"targetType" : element.subtype,
						"source" : $this.getLayerName(cardCells, element.parent),
						"target" : element.id
				}
				links.push(link);
			}
			//Adaptation_Function
			else if (element.subtype === 'Adaptation_Function') {
			
				var af = {
						"type" : element.subtype,
						"id" : element.id,
						"name" : element.attrs.text.text,
				}
				elements.push(af);
				
				//Card > AF
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.id,
				};
				links.push(link);
				
			}
			//Matrix
			else if (element.subtype === 'Matrix') {
				
				var matrix = {
						"type" : element.subtype,
						"id" : element.id,
						"name" : element.attrs.text.text,
				}
				elements.push(matrix);
				
				//Card > Matrix
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.id,
				};
				links.push(link);
				
			}
			//Input_Card / Output_Card
			else if (element.subtype === 'Input_Card' || element.subtype === 'Output_Card') {
				
				var inOut = {
						"type" : element.subtype,
						"id" : element.id,
						"name" : element.attrs.text.text,
				}
				elements.push(inOut);
				
				//Card > Input_Card/Output_Card
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.id,
				};
				links.push(link);
				
			}
			
			//Links
			else if(element.type === 'link') {
				
				var link = {
						"sourceType" : $this.getElementType(cardCells, element.source.id),
						"targetType" : $this.getElementType(cardCells, element.target.id),
						"source" : element.source.id,
						"target" : element.target.id
				}
				links.push(link);
				
			}
			
		});
		
		//Connected ports Output_Card > Input_Card
		$.each(card.connectedPorts, function(key, port) {
			
			//if target type === Input_Card
			if(card.connectedPorts[key]["type"] === 'Input_Card') {
				
				//Output_Card > Input_Card
				var link = {
						"sourceType" : "Output_Card",
						"targetType" : "Input_Card",
						"source" : key,
						"target" : card.connectedPorts[key]["id"]
				}
				links.push(link);
				
			}
			
		});
		
		console.log('Elements: ' + JSON.stringify(elements));
		console.log('Links: ' + JSON.stringify(links));
		
		//execute parse
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "parseCardToOWL.htm",
		   data: {
			   'elements' : JSON.stringify(elements),
			   'links' : JSON.stringify(links),
		   },
		   success: function(){
			   //console.log('PARSE OK!')
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		
//		console.log('Equipment: ' + JSON.stringify(equipment));
//		console.log('Card: ' + JSON.stringify(card));
		
	},
	
	getLayerName : function(elements, layerId) {
		
		var layerName = undefined;
		
		$.each(elements, function(index, element) {
			
			if(element.id === layerId) {
				if(element.subtype) {
					layerName = element.lanes.label;
				}
			}
			
		});
		
		return layerName;
		
	},
	
	//Method to get element type
	getElementType : function(elements, elementId) {
		
		var type = undefined;
		
		$.each(elements, function(index, element) {
			
			if(element.id === elementId) {
				
				if(element.subtype) {
					type = element.subtype;
				}
				else if(element.subType) {
					type = element.subType;
				}
			}
			
		});
		
		return type;
		
	},
	
	//Method to get inputs from OWL file
	getConnectionTypeFromOWL : function(equipmentSourceId, equipmentTargetId) {
		
		var connectionType = undefined;
		var $this = this;
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getConnectionTypeFromOWL.htm",
		   data: {
			   'equipmentSourceId' : equipmentSourceId,
			   'equipmentTargetId' : equipmentTargetId,
		   },
		   success: function(data){
			   connectionType = data;
			   console.log('connectionType: ' + connectionType)
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		return connectionType;
	},
	
	getPossibleConnectionsFromOWL : function(equipmentSourceId, equipmentTargetId) {
		
		var connectionType = undefined;
		var $this = this;
		var connections = undefined;
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getConnectionTypeFromOWL.htm",
		   data: {
			   'equipmentSourceId' : equipmentSourceId,
			   'equipmentTargetId' : equipmentTargetId,
		   },
		   success: function(connectionType){
			   //connectionType = data;
			   console.log('connectionType: ' + connectionType)
			   
			   $.ajax({
				   type: "POST",
				   async: false,
				   url: "getPossibleConnectionsFromOWL.htm",
				   data: {
					   'equipmentSourceId' : equipmentSourceId,
					   'equipmentTargetId' : equipmentTargetId,
					   'connectionType' : connectionType,
				   },
				   dataType: 'json',
				   success: function(connections){
					   console.log('connections: ' + JSON.stringify(connections))
				   },
				   error : function(e) {
					   alert("error: " + e.status);
				   }
				});
			   
			   
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		return connectionType;
		
	},
	
	//Method to get inputs from OWL file
	getInputsFromOWL : function(equipmentId) {
		
		var inputs = {};
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getInputsFromOWL.htm",
		   data: {
			   'equipmentId' : equipmentId,
		   },
		   dataType: 'json',
		   success: function(data){
			   inputs = data;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		return inputs;
	},
	
	//Method to get outputs from OWL file
	getOutputsFromOWL : function(equipmentId) {
		
		var outputs = {};
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getOutputsFromOWL.htm",
		   data: {
			   'equipmentId' : equipmentId,
		   },
		   dataType: 'json',
		   success: function(data){
			   outputs = data;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		return outputs;
	},
	
	
	
});