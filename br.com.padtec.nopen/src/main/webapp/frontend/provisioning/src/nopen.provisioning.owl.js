nopen.provisioning.OWL = Backbone.Model.extend({
	
	app : undefined,
	relationships : undefined,
	
	initialize : function() {
		
		var $this = this;
		
		//get model relationships from OWL
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getModelRelationships.htm",
		   dataType: 'json',
		   success: function(relationships){
			   
			   //delete INV relation if normal relation exist to the same source and target. 
			   $.each(relationships, function(source, sourceValue) {
				   $.each(relationships[source], function(target, targetValue) {
					   
					   var relations = [];
					   
					   $.each(relationships[source][target], function(key, relation) {
						   if(relation) {
							   if(relation === "INV.binds.Adaptation_Function.Trail_Termination_Function" || relation === "INV.binds.Trail_Termination_Function.Adaptation_Function") {
								   relationships[source][target].splice(key, 1);
							   }
							   else if(relation.indexOf("INV.") >= 0){
								   //check if has inverse, if yes, delete it.
								   if($this.hasInverse(relationships[source][target], relation)){
									   relationships[source][target].splice(key, 1);
								   }
							   }
						   }
					   });
				   });
			   });
			   
			   $this.setRelationships(relationships);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	hasInverse : function(relations, relation) {
		
		var hasInverse = false;
		
		$.each(relations, function(key, value) {
			
			if(relation && value) {
				if(relation.substring(4, relation.lenght) === value.substring(0, value.lenght)) {
					hasInverse = true;
				}
			}
			
		});
		
		return hasInverse;
	},
	
	//Method to set model relationships 
	setRelationships : function(relationships) {
		
		this.relationships = relationships;
		console.log(JSON.stringify(relationships));
		
	},
	
	//Methos to set the application variable
	setApp : function(app) {
		this.app = app;
	},
	
	//Method to parse card JSON file to generate OWL instances
	parseCardToOWL : function(equipment, card) {
		
		var $this = this;
		var relationships = this.relationships;
		
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
		var ttf_out_counter = 0, ttf_in_counter = 0, af_out_counter = 0, af_in_counter = 0, matrix_out_counter = 0, matrix_in_counter = 0, pm_counter = 0;
		
		//Equipment
		var equip = $this.createElement("Equipment", equipment.id, equipment.attributes.attrs.text.text);
		elements.push(equip);
		
		//Card
		var equipCard = $this.createElement(card.subType, card.id, card.attrs.name.text); 
		elements.push(equipCard);
		
		//Equipment (E) > Card (C)
		var linkEC = $this.createLink(equipment.id, "Equipment", card.id, card.subType); 
		links.push(linkEC);
		
		//ITU Elements
		var cardCells = card.attrs.data.cells;
		
		$.each(cardCells, function(index, element) {
			
			//Card_Layer
			if(element.subtype === 'Card_Layer') {
				//console.log('Layer: ' + JSON.stringify(element));
				var layer = $this.createElement(element.subtype, element.id, element.lanes.label); 
				elements.push(layer);
				
				//Card > Card_Layer
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);
			}
			//Trail_Termination_Function
			else if (element.subtype === 'Trail_Termination_Function') {
				
				var ttf = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(ttf);
				
				//Card_Layer > TTF
				var cardLayerType = $this.getElementType(cardCells, element.parent);
				
				var link = $this.createLink(element.parent, cardLayerType, element.id, element.subtype);
				links.push(link);
			}
			//Adaptation_Function
			else if (element.subtype === 'Adaptation_Function') {
			
				var af = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(af);
				
				//Card > AF
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);
				
			}
			//Matrix
			else if (element.subtype === 'Matrix') {
				
				var matrix = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(matrix);
				
				//Card > Matrix
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);
				
			}
			//Input_Card / Output_Card
			else if (element.subtype === 'Input_Card' || element.subtype === 'Output_Card') {
				
				var inOut = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(inOut);
				
				//Card > Input_Card/Output_Card
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);
				
			}
			
			//Links
			else if(element.type === 'link') {
				
				var sourceType = $this.getElementType(cardCells, element.source.id);
				var targetType = $this.getElementType(cardCells, element.target.id);
				
				var ttf_out = undefined, ttf_in = undefined, af_out = undefined, af_in = undefined, matrix_out = undefined, matrix_in = undefined;
				
				if(sourceType === 'Trail_Termination_Function') {
					
					//Trail_Termination_Function_Output (TTF_OUT)
					ttf_out = $this.createElement("Trail_Termination_Function_Output", element.source.id + "-out-" + ttf_out_counter, "Trail_Termination_Function_Output_" + ttf_out_counter); 
					elements.push(ttf_out);
					ttf_out_counter++;
					
					//Trail_Termination_Function (TTF) > Trail_Termination_Function_Output (TTFOUT)
					var linkTTF_TTFOUT = $this.createLink(element.source.id, sourceType, ttf_out.id, ttf_out.type); 
					links.push(linkTTF_TTFOUT);
					
				}
				
				if(targetType === 'Trail_Termination_Function') {
				
					//Trail_Termination_Function_Input (TTF_IN)
					ttf_in = $this.createElement("Trail_Termination_Function_Input", element.target.id + "-in-" + ttf_in_counter, "Trail_Termination_Function_Input_" + ttf_in_counter);
					elements.push(ttf_in);
					ttf_in_counter++;
				
					//Trail_Termination_Function (TTF) > Trail_Termination_Function_Intput (TTFIN)
					var linkTTF_TTFIN = $this.createLink(element.target.id, targetType, ttf_in.id, ttf_in.type);
					links.push(linkTTF_TTFIN);
					
				}
				
				if(sourceType === 'Adaptation_Function') {
					
					//Adaptation_Function_Output (AF_OUT)
					af_out = $this.createElement("Adaptation_Function_Output", element.source.id + "-out-" + af_out_counter, "Adaptation_Function_Output_" + af_out_counter);
					elements.push(af_out);
					af_out_counter++;
					
					//Adaptation_Function (AF) > Adaptation_Function_Output (AFOUT)
					var linkAF_AFOUT = $this.createLink(element.source.id, sourceType, af_out.id, af_out.type);
					links.push(linkAF_AFOUT);
					
				}
				
				if(targetType === 'Adaptation_Function') {
					
					//Adaptation_Function_Input (AF_IN)
					af_in = $this.createElement("Adaptation_Function_Input", element.target.id + "-in-" + af_in_counter, "Adaptation_Function_Input" + af_in_counter);
					elements.push(af_in);
					af_in_counter++;
					
					//Adaptation_Function (AF) > Adaptation_Function_Input (AFIN)
					var linkAF_AFIN = $this.createLink(element.target.id, targetType, af_in.id, af_in.type);
					links.push(linkAF_AFIN);
					
				}
				
				if(sourceType === 'Matrix') {
					
					//Matrix_Output (M_OUT)
					matrix_out = $this.createElement("Matrix_Output", element.source.id + "-out-" + matrix_out_counter, "Matrix_Output_" + matrix_out_counter);
					elements.push(matrix_out);
					matrix_out_counter++;
					
					//Matrix (M) > Matrix_Output (MOUT)
					var linkM_MOUT = $this.createLink(element.source.id, sourceType, matrix_out.id, matrix_out.type);
					links.push(linkM_MOUT);
					
				}
				
				if(targetType === 'Matrix') {
					
					//Matrix_Input (M_IN)
					matrix_in = $this.createElement("Matrix_Input", element.target.id + "-in-" + matrix_in_counter, "Matrix_Input_" + matrix_in_counter);
					elements.push(matrix_in);
					matrix_in_counter++;
					
					//Matrix (M) > Matrix_Input (MIN)
					var linkM_MIN = $this.createLink(element.target.id, targetType, matrix_in.id, matrix_in.type);
					links.push(linkM_MIN);
					
				}
				
				if(sourceType === 'Trail_Termination_Function' && (targetType === 'Adaptation_Function' || targetType === 'Matrix')) {
					//Reference_Point FEP (FEP)
					var rp = $this.createElement("FEP", element.id, "FEP_" + fep_counter);
					elements.push(rp);
					fep_counter++;
					
					//TTF_OUT (TTFOUT) > FEP (FEP)
					var linkTTFOUT_FEP = $this.createLink(element.id, "FEP", ttf_out.id, ttf_out.type);
					links.push(linkTTFOUT_FEP);
					
					if(targetType === 'Adaptation_Function') {
						
						//FEP (FEP) > AF_IN (AFIN)
						var linFEP_AFIN = $this.createLink(element.id, "FEP", af_in.id, af_in.type);
						links.push(linFEP_AFIN);
						
					}
					else {
						
						//FEP (FEP) > MATRIX_IN (MIN)
						var linkFEP_MIN = $this.createLink(element.id, "FEP", matrix_in.id, matrix_in.type);
						links.push(linkFEP_MIN);
						
					}
					
				}
				
				if(sourceType === 'Adaptation_Function' && targetType === 'Trail_Termination_Function') {
					//Reference_Point AP (AP)
					var rp = $this.createElement("AP", element.id, "AP_" + ap_counter);
					elements.push(rp);
					ap_counter++;
					
					//AF_OUT (AFOUT) > AP (AP)
					var linAFOUT_AP = $this.createLink(element.id, "AP", af_out.id, af_out.type);
					links.push(linAFOUT_AP);
					
					//AP (AP) > TTF_IN (TTFIN)
					var linkAP_TTFIN = $this.createLink(element.id, "AP", ttf_in.id, ttf_in.type);
					links.push(linkAP_TTFIN);
					
				}
				
				if(sourceType === 'Matrix' && targetType === 'Adaptation_Function') {
					//Reference_Point FP (FP)
					var rp = $this.createElement("FP", element.id, "FP_" + fp_counter);
					elements.push(rp);
					fp_counter++;
					
					//FP (FP) > M_OUT (MOUT)
					var linkMOUT_FP = $this.createLink(element.id, "FP", matrix_out.id, matrix_out.type);
					links.push(linkMOUT_FP);
					
					//FP (FP) > AFIN (AFIN)
					var linkFP_AFIN = $this.createLink(element.id, "FP", af_in.id, af_in.type);
					links.push(linkFP_AFIN);
					
				}
				
				var link = $this.createLink(element.source.id, sourceType, element.target.id, targetType);
				links.push(link);
				
			}
			
		});
		
		//Input_Card/Output_Card > Input_Card/Output_Card
		$.each(card.connectedPorts, function(portId, connectedPort) {
			
			console.log('connectedPort: ' + JSON.stringify(connectedPort));
			
			if(card.outPorts[portId]) {
				if(connectedPort.edge === "target") {
					var link = $this.createLink(portId, "Output_Card", connectedPort.id, connectedPort.type);
					links.push(link);
				}
			}
			else if(card.inPorts[portId]) {
				var link = $this.createLink(portId, "Input_Card", connectedPort.id, connectedPort.type);
				links.push(link);
			}
			
		});
		
		var pElements = elements;
		var pLinks = [];
		
		$.each(links, function(key, link){
			$.each(link, function(k, l){
				pLinks.push(l)
			});
		});
		
		console.log('Elements: ' + JSON.stringify(pElements));
		console.log('Links: ' + JSON.stringify(pLinks));
		
		//execute parse
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "parseCardToOWL.htm",
		   data: {
			   'elements' : JSON.stringify(pElements),
			   'links' : JSON.stringify(pLinks),
		   },
		   success: function(){
			   //console.log('PARSE OK!')
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
	
	
	},
	
	parseConnectedPorts : function(graph) {
		
		var $this = this;
		var model = this.app.model;
		
		//Reference Point Counter
		var fep_counter = 0, ap_counter = 0, fp_counter = 0;
		//TF OUT/IN Counter
		var ttf_out_counter = 0, ttf_in_counter = 0, af_out_counter = 0, af_in_counter = 0, matrix_out_counter = 0, matrix_in_counter = 0, pm_counter = 0;
		
		var elements = [];
		var links = [];
		
		//generate owl instances
		$.each(graph.getElements(), function(index, equipment) {

			if(equipment.get('subtype') === 'Access_Group') {

				var cards = model.getCards(equipment);
				$.each(cards, function(key, card) {

					console.log('PARSE CARD CONNECTED PORTS');
					
					//Input_Card/Output_Card > Input_Card/Output_Card
					$.each(card.connectedPorts, function(portId, connectedPort) {
						
						console.log('connectedPort: ' + JSON.stringify(connectedPort));
						
						if(card.outPorts[portId]) {
							
							if(connectedPort.type === "Input_Card") {
								
								var tfSource = $this.getConnectedTFElement(graph, portId);
								var tfTarget = $this.getConnectedTFElement(graph, connectedPort.id);
								
								var link = $this.createLink(portId, "Output_Card", connectedPort.id, connectedPort.type);
								links.push(link);
							}
							else if(connectedPort.edge === "target") {
								
								var linkId = $this.getConnectedLinkId(cardCells, portId, connectedPort.id);
								
								var tfSource = $this.getConnectedTFElement(graph, portId);
								var tfTarget = $this.getConnectedTFElement(graph, connectedPort.id);
								
								console.log('linkId: ' + linkId);
								console.log('tfSource: ' + JSON.stringify(tfSource));
								console.log('tfTarget: ' + JSON.stringify(tfTarget));
								
								
								if(tfSource.type === 'Trail_Termination_Function' || tfTarget.type === 'Trail_Termination_Function') {
									
									//Physical_Media PM (PM)
									var pm = $this.createElement("Physical_Media", linkId, "PM_" + pm_counter);
									elements.push(pm);
									
									//Physical_Media PM (PM)
									var pm_so = $this.createElement("PM_Input_So", linkId + '-so' + pm_counter, "PM_" + pm_counter);
									elements.push(pm);
									
									//Physical_Media PM (PM)
									var pm_sk = $this.createElement("PM_Input_Sk", linkId + '-sk' + pm_counter, "PM_" + pm_counter);
									elements.push(pm);
									
									pm_counter++;
									
									//Physical_Media (PM) > PM_Input_So (PMSO) 
									var link_PM_PMSO = $this.createLink(pm.id, "Physical_Media", pm_so.id, pm_so.type);
									links.push(link_PM_PMSO);
									
									//Physical_Media (PM) > PM_Input_Sk (PMSK)
									var link_PM_PMSK = $this.createLink(pm.id, "Physical_Media", pm_sk.id, pm_sk.type);
									links.push(link_PM_PMSK);
									
									
									//Reference_Point FEP (FEP)
									var fep_pmso = $this.createElement("FEP", linkId + '-fep-pmso', "FEP_" + fep_counter);
									elements.push(fep_pmso);
									fep_counter++;
									
									//Trail_Termination_Function_Output (TTF_OUT)
									var ttf_out_source = $this.createElement("Trail_Termination_Function_Output", tfSource.id + "-fep-pmso-out" + ttf_out_counter, "Trail_Termination_Function_Output_" + ttf_out_counter); 
									elements.push(ttf_out_source);
									ttf_out_counter++;
									
									//TTF > Trail_Termination_Function_Output (TTFOUT)
									var link_TTF_TTFOUT = $this.createLink(tfSource.id, tfSource.type, ttf_out_source.id, ttf_out_source.type);
									links.push(link_TTF_TTFOUT);
									
									//FEP > PM_Input_So (PMSO)
									var link_FEP_PMSO = $this.createLink(fep_pmso.id, fep_pmso.type, pm_so.id, pm_so.type);
									links.push(link_FEP_PMSO);
									
									//FEP > Trail_Termination_Function_Output (TTFOUT)
									var link_FEP_TTFOUT = $this.createLink(fep_pmso.id, fep_pmso.type, ttf_out_source.id, ttf_out_source.type);
									links.push(link_FEP_TTFOUT);
									
									
									
									
									//Reference_Point FEP (FEP)
									var fep_pmsk = $this.createElement("FEP", linkId + '-fep-pmsk', "FEP_" + fep_counter);
									elements.push(fep_pmsk);
									fep_counter++;
									
									//Trail_Termination_Function_Output (TTF_OUT)
									ttf_out_target = $this.createElement("Trail_Termination_Function_Output", tfTarget.id + "-fep-pmsk-out" + ttf_out_counter, "Trail_Termination_Function_Output_" + ttf_out_counter); 
									elements.push(ttf_out_target);
									ttf_out_counter++;

									//TTF > Trail_Termination_Function_Output (TTFOUT)
									var link_TTF_TTFOUT = $this.createLink(tfTarget.id, tfTarget.type, ttf_out_target.id, ttf_out_target.type);
									links.push(link_TTF_TTFOUT);
									
									//FEP > PM_Input_Sk (PMSK)
									var link_FEP_PMSK = $this.createLink(fep_pmsk.id, fep_pmsk.type, pm_sk.id, pm_sk.type);
									links.push(link_FEP_PMSK);
									
									//FEP > Trail_Termination_Function_Output (TTFOUT)
									var link_FEP_TTFOUT = $this.createLink(fep_pmsk.id, fep_pmsk.type, ttf_out_target.id, ttf_out_target.type);
									links.push(link_FEP_TTFOUT);
									
								}
								
								var link = $this.createLink(portId, "Output_Card", connectedPort.id, connectedPort.type);
								links.push(link);
							}
						}
//						else if(card.inPorts[portId]) {
//							var link = $this.createLink(portId, "Input_Card", connectedPort.id, connectedPort.type);
//							links.push(link);
//						}
					});
					
				});

			}

		});
		
	},
	
	getConnectedLinkId : function(elements, sourceId, targetId) {
		
		var linkId = undefined;
		
		$.each(elements, function(index, element) {
			if(element.type === 'link') {
				if(element.source.id === sourceId && element.target.id === targetId) {
					linkId = element.id;
				}
			}
		});
		
		return linkId;
	},
	
	getConnectedTFElement : function(graph, portId) {
		
		var model = this.app.model;
		var $this = this;
		var tfElement = undefined;
		
		//generate owl instances
		$.each(graph.getElements(), function(index, equipment) {

			if(equipment.get('subtype') === 'Access_Group') {

				var cards = model.getCards(equipment);
				$.each(cards, function(key, card) {

					//ITU Elements
					var cardCells = card.attrs.data.cells;
					
					$.each(cardCells, function(key, element) {
						//Links
						if(element.type === 'link') {
							if(element.target.id === portId) {
								tfElement = {
										'id' : element.source.id,
										'type' : $this.getElementType(cardCells, element.source.id),
								}
							}
						}
					});
				});
			}
		});
		
		return tfElement;
	},
	
	//create a JOSN link.
	createLink : function(subject, subjectType, object, objectType) {
		
		var links = [];
		var relationships = this.relationships;
		
		$.each(relationships[subjectType][objectType], function(key, predicate) {
			
			links.push({
				"subjectType" : subjectType,
				"subject" : subject,
				"predicate" : predicate,
				"objectType" : objectType,
				"object" : object,
			})
			
		});
		
		return links;
		
	},
	
	//create a JSON element.
	createElement : function(type, id, name) {
		
		return element = {
				"type" : type,
				"id" : id,
				"name" : name,
		}
		
	},
	
	//create connection between ports in OWL
	connectPorts : function(sourcePort, targetPort) {
		
		var $this = this;
		var relationships = this.relationships;
		
		var links = [];
		var elements = [];
		
		elements.push(sourcePort);
		elements.push(targetPort);
		
		links.push({
			"subjectType" : sourcePort.type,
			"subject" : sourcePort.id,
			"predicate" : relationships[sourcePort.type][targetPort.type][0],
			"objectType" : targetPort.type,
			"object" : targetPort.id,
		})
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "connectPortsInOWL.htm",
		   data: {
			   'elements' : JSON.stringify(elements),
			   'links' : JSON.stringify(links),
		   },
		   success: function(){
			   //execute reasoning
			   $this.executeReasoning();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	//execute reasoning
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
	
	//Methos to get the layer by layer id
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
	
	//Method do get possible connection ports (interfaces) from OWL
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