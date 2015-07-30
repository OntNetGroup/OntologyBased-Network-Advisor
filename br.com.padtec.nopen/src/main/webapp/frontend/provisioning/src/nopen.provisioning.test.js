

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
	
	getConnections : function() {
		
		var connections = {
				"d6248eef-a611-40c5-9a2b-61af5b73bd60" : {
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
				},
				"be4a87fd-3911-4987-b0f3-5ce00d1197a4" : {
				    "ODUk" : [ {
				    	"id" : 231,
						"name" : "Out_4",
				    }],
				    "OPUk" : [{
				    	"id" : 564,
						"name" : "Out_5",
				    },
				    {
				    	"id" : 897,
						"name" : "Out_6",
				    }],
				},
//				"4609668b-e061-4e01-85b6-e9b063da8850" : {
//					"ODUk" : [ {
//				    	"id" : 123,
//						"name" : "In_1",
//				    }],
//				    "OPUk" : [{
//				    	"id" : 456,
//						"name" : "In_2",
//				    },
//				    {
//				    	"id" : 789,
//						"name" : "In_3",
//				    }],
//				},
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
	
});