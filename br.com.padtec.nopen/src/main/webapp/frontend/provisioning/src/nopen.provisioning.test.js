

nopen.provisioning.Test = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function() {
		
	},
	
	setApp : function(app) {
		this.app = app;
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
				offset.y = 300;
				break;
			case 2 :
				offset.x = 250;
				offset.y = 500;
				break;
			case 3 :
				offset.x = 250;
				offset.y = 700;
				break;
			case 4 :
				offset.x = 250;
				offset.y = 900;
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
		
	}
	
});