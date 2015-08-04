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
		
		//Reference Point Counter
		var fep_counter = 0, ap_counter = 0, fp_counter = 0;
		//TF OUT/IN Counter
		var ttf_out_counter = 0, ttf_in_counter = 0, af_out_counter = 0, af_in_counter = 0, matrix_out_counter = 0, matrix_in_counter = 0;
		
		
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
				
				var sourceType = $this.getElementType(cardCells, element.source.id);
				var targetType = $this.getElementType(cardCells, element.target.id);
				
				var ttf_out = undefined, ttf_in = undefined, af_out = undefined, af_in = undefined, matrix_out = undefined, matrix_in = undefined;
				
				if(sourceType === 'Trail_Termination_Function') {
					
					//Trail_Termination_Function_Output (TTF_OUT)
					ttf_out = {
						"type" : "Trail_Termination_Function_Output",
						"id" : joint.util.uuid(),
						"name" : "Trail_Termination_Function_Output_" + ttf_out_counter,
					};
					elements.push(ttf_out);
					ttf_out_counter++;
					
					//Trail_Termination_Function (TTF) > Trail_Termination_Function_Output (TTFOUT)
					var linkTTF_TTFOUT = {
							"sourceType" : 'Trail_Termination_Function',
							"targetType" : 'Trail_Termination_Function_Output',
							"source" : element.source.id,
							"target" : ttf_out.id,
					}
					links.push(linkTTF_TTFOUT);
					
				}
				
				if(targetType === 'Trail_Termination_Function') {
				
					//Trail_Termination_Function_Input (TTF_IN)
					ttf_in = {
						"type" : "Trail_Termination_Function_Input",
						"id" : joint.util.uuid(),
						"name" : "Trail_Termination_Function_Input_" + ttf_in_counter,
					};
					elements.push(ttf_in);
					ttf_in_counter++;
				
					//Trail_Termination_Function (TTF) > Trail_Termination_Function_Intput (TTFIN)
					var linkTTF_TTFIN = {
							"sourceType" : 'Trail_Termination_Function',
							"targetType" : 'Trail_Termination_Function_Input',
							"source" : element.target.id,
							"target" : ttf_in.id,
					}
					links.push(linkTTF_TTFIN);
					
				}
				
				if(sourceType === 'Adaptation_Function') {
					
					//Adaptation_Function_Output (AF_OUT)
					af_out = {
						"type" : "Adaptation_Function_Output",
						"id" : joint.util.uuid(),
						"name" : "Adaptation_Function_Output_" + af_out_counter,
					};
					elements.push(af_out);
					af_out_counter++;
					
					//Adaptation_Function (AF) > Adaptation_Function_Output (AFOUT)
					var linkAF_AFOUT = {
							"sourceType" : 'Adaptation_Function',
							"targetType" : 'Adaptation_Function_Output',
							"source" : element.source.id,
							"target" : af_out.id,
					}
					links.push(linkAF_AFOUT);
					
				}
				
				if(targetType === 'Adaptation_Function') {
					
					//Adaptation_Function_Input (AF_IN)
					af_in = {
						"type" : "Adaptation_Function_Input",
						"id" : joint.util.uuid(),
						"name" : "Adaptation_Function_Input_" + af_in_counter,
					};
					elements.push(af_in);
					af_in_counter++;
					
					//Adaptation_Function (AF) > Adaptation_Function_Input (AFIN)
					var linkAF_AFIN = {
							"sourceType" : 'Adaptation_Function',
							"targetType" : 'Adaptation_Function_Input',
							"source" : element.target.id,
							"target" : af_in.id,
					}
					links.push(linkAF_AFIN);
					
				}
				
				if(sourceType === 'Matrix') {
					
					//Matrix_Output (M_OUT)
					matrix_out = {
						"type" : "Matrix_Output",
						"id" : joint.util.uuid(),
						"name" : "Matrix_Output_" + matrix_out_counter,
					};
					elements.push(matrix_out);
					matrix_out_counter++;
					
					//Matrix (M) > Matrix_Output (MOUT)
					var linkM_MOUT = {
							"sourceType" : 'Matrix',
							"targetType" : 'Matrix_Output',
							"source" : element.source.id,
							"target" : matrix_out.id,
					}
					links.push(linkM_MOUT);
					
				}
				
				if(targetType === 'Matrix') {
					
					//Matrix_Input (M_IN)
					matrix_in = {
						"type" : "Matrix_Input",
						"id" : joint.util.uuid(),
						"name" : "Matrix_Input" + matrix_in_counter,
					};
					elements.push(matrix_in);
					matrix_in_counter++;
					
					//Matrix (M) > Matrix_Input (MIN)
					var linkM_MIN = {
							"sourceType" : 'Matrix',
							"targetType" : 'Matrix_Input',
							"source" : element.target.id,
							"target" : matrix_in.id,
					}
					links.push(linkM_MIN);
					
				}
				
				if(sourceType === 'Trail_Termination_Function' && (targetType === 'Adaptation_Function' || targetType === 'Matrix')) {
					//Reference_Point FEP (FEP)
					var rp = {
							"type" : "FEP",
							"id" : element.id,
							"name" : "FEP_" + fep_counter,
					};
					elements.push(rp);
					fep_counter++;
					
					//TTF_OUT (TTFOUT) > FEP (FEP)
					var linkTTFOUT_FEP = {
							"sourceType" : 'FEP',
							"targetType" : 'Trail_Termination_Function_Output',
							"source" : element.id,
							"target" : ttf_out.id,
					}
					links.push(linkTTFOUT_FEP);
					
					if(targetType === 'Adaptation_Function') {
						
						//FEP (FEP) > AF_IN (AFIN)
						var linFEP_AFIN = {
								"sourceType" : 'FEP',
								"targetType" : 'Adaptation_Function_Input',
								"source" : element.id,
								"target" : af_in.id,
						}
						links.push(linFEP_AFIN);
						
					}
					else {
						
						//FEP (FEP) > MATRIX_IN (MIN)
						var linkFEP_MIN = {
								"sourceType" : 'FEP',
								"targetType" : 'Matrix_Input',
								"source" : element.id,
								"target" : matrix_in.id,
						}
						links.push(linkFEP_MIN);
						
					}
					
				}
				
				if(sourceType === 'Adaptation_Function' && targetType === 'Trail_Termination_Function') {
					//Reference_Point AP (AP)
					var rp = {
							"type" : "AP",
							"id" : element.id,
							"name" : "AP_" + ap_counter,
					};
					elements.push(rp);
					ap_counter++;
					
					//AF_OUT (AFOUT) > AP (AP)
					var linAFOUT_AP = {
							"sourceType" : 'Adaptation_Function_Output',
							"targetType" : 'AP',
							"source" : af_out.id,
							"target" : element.id,
					}
					links.push(linAFOUT_AP);
					
					//AP (AP) > TTF_IN (TTFIN)
					var linkAP_TTFIN = {
							"sourceType" : 'AP',
							"targetType" : 'Trail_Termination_Function_Input',
							"source" : element.id,
							"target" : ttf_in.id,
					}
					links.push(linkAP_TTFIN);
					
				}
				
				if(sourceType === 'Matrix' && targetType === 'Adaptation_Function') {
					//Reference_Point FP (FP)
					var rp = {
							"type" : "FP",
							"id" : element.id,
							"name" : "FP" + fp_counter,
					};
					elements.push(rp);
					fp_counter++;
					
					//M_OUT (MOUT) > FP (FP)
					var linkMOUT_FP = {
							"sourceType" : 'Matrix_Output',
							"targetType" : 'FP',
							"source" : matrix_out.id,
							"target" : element.id,
					}
					links.push(linkMOUT_FP);
					
					//FP (FP) > MIN (MIN)
					var linkFP_MIN = {
							"sourceType" : 'FP',
							"targetType" : 'Adaptation_Function_Input',
							"source" : element.id,
							"target" : af_in.id,
					}
					links.push(linkFP_MIN);
					
				}
				
				var link = {
						"sourceType" : sourceType,
						"targetType" : targetType,
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
	
	executeReasoning : function() {
	
		//execute parse
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "executeReasoning.htm",
		   success: function(){
			   //console.log('PARSE OK!')
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
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
	
	getPossibleConnectionsFromOWL : function(connectionType, equipmentSourceId, equipmentTargetId) {
		
		var $this = this;
		var connections = undefined;

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
			success: function(data){
				console.log('connections: ' + JSON.stringify(data))
				connections = data;
			},
			error : function(e) {
				alert("error: " + e.status);
			}
		});

		return connections;
		
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