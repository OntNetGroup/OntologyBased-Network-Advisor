nopen.provisioning.OWL = Backbone.Model.extend({
	
	model : undefined,
	
	initialize : function() {
		
	},
	
	//Method to set model
	setModel : function(model) {
		this.model = model;
	},
	
	//Method to parse card JSON file to generate OWL instances
	parseCardToOWL : function(equipment, card) {
		
		var $this = this;
		
		/*
		 * Supervisor > Equipment
		 * Supervisor > Card
		 * Layer > TTF
		 * Card > Layer
		 * Card > AF
		 * Card > Matrix
		 * Card > Input/Output
		 * TTF/AF/Matrix > Input/Output
		 */
		
		var elements = [];
		var links = [];
		
		//Supervisor
		var supervisor = {
				"type" : "Supervisor",
				"id" : card.SupervisorID,
				"name" : card.Supervisor,
		};
		elements.push(supervisor);
		
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
				"name" : card.subType,
		};
		elements.push(equipCard);
		
		//Supervisor (S) > Equipment (E)
		var linkSE = {
				"sourceType" : "Supervisor",
				"targetType" : "Equipment",
				"source" : card.SupervisorID,
				"target" : equipment.id,
		};
		links.push(linkSE);
		
		//Supervisor (S) > Card (C)
		var linkSC = {
				"sourceType" : "Supervisor",
				"targetType" : "Link",
				"source" : card.SupervisorID,
				"target" : card.id,
		};
		links.push(linkSC);
		
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
						"name" : element.subtype,
				}
				elements.push(ttf);
				
				//Layer > TTF
				var link = {
						"sourceType" : $this.getElementType(cardCells, element.parent),
						"targetType" : element.subtype,
						"source" : element.source,
						"target" : element.target
				}
			}
			//Adaptation_Function
			else if (element.subtype === 'Adaptation_Function') {
			
				var af = {
						"type" : element.subtype,
						"id" : element.id,
						"name" : element.subtype,
				}
				elements.push(af);
				
				//Card_layer > AF
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.subtype,
				};
				links.push(link);
				
			}
			//Matrix
			else if (element.subtype === 'Matrix') {
				
				var matrix = {
						"type" : element.subtype,
						"id" : element.id,
						"name" : element.subtype,
				}
				elements.push(matrix);
				
				//Card_layer > Matrix
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.subtype,
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
				
				//Card_layer > Input_Card/Output_Card
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.subtype,
				};
				links.push(link);
				
			}
			//Links
			else if(element.subtype === 'link') {
				
				var link = {
						"sourceType" : $this.getElementType(cardCells, element.source),
						"targetType" : $this.getElementType(cardCells, element.target),
						"source" : element.source,
						"target" : element.target
				}
				links.push(link);
				
			}
			
			
		});
		
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
			   console.log('PARSE OK!')
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
//		console.log('Elements: ' + JSON.stringify(elements));
//		console.log('Links: ' + JSON.stringify(links));
//		console.log('Equipment: ' + JSON.stringify(equipment));
//		console.log('Card: ' + JSON.stringify(card));
		
	},
	
	getElementType : function(elements, elementId) {
		
		$.each(elements, function(index, element) {
			
			if(element.id == elementId) {
				if(element.subtype) {
					return element.subtype;
				}
				else if(element.subType) {
					return element.subType;
				}
			}
			
		});
		
	},
	
	//Method to get inputs from OWL file
	getInputs : function() {
		
	},
	
	//Method to get outputs from OWL file
	getOutputs : function() {
		
	},
	
	
	
});