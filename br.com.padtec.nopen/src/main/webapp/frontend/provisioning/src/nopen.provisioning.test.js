

nopen.provisioning.Test = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function() {
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	executeMatrixDialog : function(app) {
		
		var graph = new joint.dia.Graph;
		var paper = new joint.dia.Paper({ 
			width: 500, 
			height: 320, 
			model: graph, 
			gridSize: 1,
			defaultLink: new joint.dia.Link(),
			validateConnection: function(cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
//	            if (magnetS && magnetS.getAttribute('type') === 'input') return false;
//	            if (cellViewS === cellViewT) return false;
				
//				if(magnetT && magnetT.getAttribute('type') === 'output') return true;
	            return magnetT && magnetT.getAttribute('type') === 'output';
	        },
	        markAvailable: true
		});

		var dialog = new joint.ui.Dialog({
		    width: 500,
		    closeButton: false,
		    title: 'Test Matrix',
		    clickOutside: false,
			modal: false,
		    content: paper.$el,
		    buttons: [
			          { action: 'next', content: 'Next', position: 'right' },
			]
		});
		dialog.on('action:next', next);
		dialog.open();
		
		//block outside
		$('#black_overlay').show();
		
		function next() {
			dialog.close();
			$('#black_overlay').hide();
		};

		var outputs = [];
		outputs.push('out_1');
		outputs.push('out_2');
		
		var c1 = new joint.shapes.provisioning.Matrix({
		    position: {
		        x: 90,
		        y: 10
		    },
		    size: {
		        width: 300,
		        height: 300
		    },
		    inPorts: ['in_1', 'in_2', 'in_3', 'in_4', 'in_5', 'in_6', 'in_7', 'in_8'],
		    outPorts: outputs,
		    attrs: {
	            '.inPorts circle': {
//	                magnet: 'passive',
	                type: 'input'
	            },
	            '.outPorts circle': {
	            	magnet: 'passive',
	                type: 'output'
	            }
	        }
		});
		
		graph.addCell(c1);
		
//		$('circle').bind("contextmenu",function(e){
//			alert(this.id);
//	    });
		
		$('.port-label').attr('pointer-events', 'normal');
		$('.port-label').attr('cursor', 'pointer');
				
		$('.port-label').hover(function(){
			$(this).attr("fill", "blue");
		}, function(){
			$(this).attr("fill", "black");
	    });
		
		$('.port-label').click(function(){
			alert(this.id);
	    });
		
		var commandManager = new joint.dia.CommandManager({ graph: graph });
		var validator = new joint.dia.Validator({ commandManager: commandManager });
		
		validator.validate('change:target change:source', function(err, command, next) {

			var cell = graph.getCell(command.data.id);
			
			console.log(JSON.stringify(cell));
			
			if(!cell.attributes.target.id || cell.attributes.target.port === cell.attributes.source.port){
				cell.remove();
				return;
			}
			
			$.each($('.port-label'), function(index, value) {
				
				if(cell.attributes.source.port === $(this).text()) {
					
					console.log($(this).closest('.port-body').id);
					
				}
				
			});
			
			
		});
		
		paper.on('cell:pointerdown', function(cellView, evt) {
        	var cell = graph.getCell(cellView.model.id);
        	if(cell.get('type') === 'link') return;
        	
        	position = cell.get('position');
        });
		
        paper.on('cell:pointerup', function(cellView, evt) {
        	var cell = graph.getCell(cellView.model.id);
        	if(cell.get('type') === 'link') return;
        	
        	cell.set('position', position);
        });
		//graph.addCells([c1, a1, a2, a3]);
		
	},
	
	execute : function(app) {
		
		var $this = this;
		var model = this.app.model;
		var graph = app.graph;
		var paper = app.paper;
		
		// Garantir que as interfaces de entrada e saída permaneçam contidas em suas respectivas barras
		var position = undefined;
        paper.on('cell:pointerdown', function(cellView, evt) {
        	var cell = cellView.model;
        	position = cell.get('position');
        	
        	console.log(JSON.stringify(cell.get('position')));
        	
	        //cell.transition('position', cell.transition('position'), {});
	        //cell.transition('position', cell.transition('position'), {});
        });
		
        paper.on('cell:pointerup', function(cellView, evt) {
        	var cell = cellView.model;
        	cell.set('position', position);
        });
        
		var implementedTechnologies = model.getImplementedTechnologies();
		
		_.each(implementedTechnologies, function(technology, index) {
			
			var uppermostLayer = model.getUppermostLayer(technology);
			var layerNetwork = Stencil.createLayerNetwork(technology, uppermostLayer);
			
			layerNetwork.attributes.position = $this.getLayerOffset(index);
			
			graph.addCell(layerNetwork);
			var layerPosition = {
					x : layerNetwork.attributes.position.x,
					y : layerNetwork.attributes.position.y,
			}
			
			var subnetwork = Stencil.createSubnetwork(technology);
			graph.addCell(subnetwork);
			
			var subnetworkPosition = {
					x : layerPosition.x + 55,
					y : layerPosition.y + 25,
			}
			
			subnetwork.translate(subnetworkPosition.x, subnetworkPosition.y);
			//layerNetwork.embed(subnetwork);

			var equipmentIDs = model.getEquipmentsByLayer(uppermostLayer);
			
			var equipmentOffset = 0;
			if(equipmentIDs.length > 0 && equipmentIDs.length <= 16) {
				equipmentOffset = Math.floor(16/equipmentIDs.length)
			}
			
			_.each(equipmentIDs, function(equipmentID, index) {
				
				var offset = $this.getEquipmentOffset(index * equipmentOffset);
				
				var accessGroup = Stencil.createAccessGroup(equipmentID);
				graph.addCell(accessGroup);
				accessGroup.translate(subnetworkPosition.x + offset.x, subnetworkPosition.y - offset.y);
				
				//subnetwork.embed(accessGroup);
				
			}, this);
			
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
	
	getConnections : function(sourceId, targetId) {
		
		var connections = [];
		
		connections[sourceId] = {
				"ODUk" : [ {
			    	"id" : joint.util.uuid(),
					"name" : "Out_1",
			    }],
			    "OPUk" : [{
			    	"id" : joint.util.uuid(),
					"name" : "Out_2",
			    },
			    {
			    	"id" : joint.util.uuid(),
					"name" : "Out_3",
			    }],	
			};
		
		connections[targetId] = {
				"ODUk" : [ {
			    	"id" : joint.util.uuid(),
					"name" : "Out_4",
			    }],
			    "OPUk" : [{
			    	"id" : joint.util.uuid(),
					"name" : "Out_5",
			    },
			    {
			    	"id" : joint.util.uuid(),
					"name" : "Out_6",
			    }],
			}
		
		return connections;
	},
	
	getInputsTest : function() {
		
		var inputs = {
		    "ODUk" : [ {
		    	"id" : 123,
				"name" : "In_1",
		    }],
		    "OPUk" : [{
		    	"id" : 456,
				"name" : "In_2",
		    },
		    {
		    	"id" : 789,
				"name" : "In_3",
		    }],
		}
		
		return inputs;
	},
	
	getOutputsTest : function() {
		
		var outputs = {
		    "ODUk" : [ {
		    	"id" : 321,
				"name" : "Out_1",
		    }],
		    "OPUk" : [{
		    	"id" : 654,
				"name" : "Out_2",
		    },
		    {
		    	"id" : 987,
				"name" : "Out_3",
		    }],
		}
		
		return outputs;
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
		
		//ITU Elements
		var cardCells = card.attrs.data.cells;
		
		$.each(cardCells, function(index, element) {
			
			//Card_Layer
			if(element.subtype === 'Card_Layer') {
				//console.log('Layer: ' + JSON.stringify(element));
				var layer = {
						"type" : element.subtype,
						"id" : element.id,
						"name" : element.lanes.label,
				};
				elements.push(layer);
				
				//Card > Card_Layer
				var link = {
						"sourceType" : card.subType,
						"targetType" : element.subtype,
						"source" : card.id,
						"target" : element.id,
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
						"source" : element.parent,
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
							"sourceType" : 'AP',
							"targetType" : 'Adaptation_Function_Output',
							"source" : element.id,
							"target" : af_out.id,
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
							"sourceType" : 'FP',
							"targetType" : 'Matrix_Output',
							"source" : element.id,
							"target" : matrix_out.id,
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
		
	},
	
	
});