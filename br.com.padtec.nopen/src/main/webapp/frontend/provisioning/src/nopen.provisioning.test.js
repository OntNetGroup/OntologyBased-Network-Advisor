

nopen.provisioning.Test = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function() {
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	execute : function(app) {
		
		var model = this.app.model;
		var graph = app.graph;
		
		Stencil = {
				
				createLayerNetwork : function(tech, upLayer) {
					
					return new joint.shapes.provisioning.Layer({
				        technology: tech,
				        upmostLayer: upLayer,
						position: {x: 250, y: 100},
						attrs: {
							'.': { magnet: false },
							'.header': { fill: '#5799DA' }
						},
						lanes: { 
							label: tech
						}
					});
					
				},
		
				createSubnetwork: function() {
					return new joint.shapes.provisioning.Subnetwork({
						subtype: 'Subnetwork',
						size: { width: 400, height: 150 },
					    attrs: {
							'.': { magnet: false },
					        text: { text: 'Subnetwork', fill: '#000000', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 }
					    }
					});
				},
				
				createAccessGroup: function(equipmentID) {
					return new joint.shapes.basic.Circle({
						size: { width: 30, height: 30 },
						id: equipmentID,
						subtype: 'Access_Group',
					    attrs: {
							'.': { magnet: false }
					    }
					});
					
					return newAccessGroup;
				}
				
		};
		
		
		/* 
		 * PROVISORIO
		 * author: missael
		 */
		var implementedTechnologies = model.getImplementedTechnologies();
		
		_.each(implementedTechnologies, function(technology, index) {
			
			var uppermostLayer = model.getUppermostLayer(technology);
			var layerNetwork = Stencil.createLayerNetwork(technology, uppermostLayer);
			
			graph.addCell(layerNetwork);
			var layerPosition = {
					x : layerNetwork.attributes.position.x,
					y : layerNetwork.attributes.position.y,
			}
			
			var subnetwork = Stencil.createSubnetwork();
			graph.addCell(subnetwork);
			
			var subnetworkPosition = {
					x : layerPosition.x + 55,
					y : layerPosition.y + 25,
			}
			
			subnetwork.translate(subnetworkPosition.x, subnetworkPosition.y);
			layerNetwork.embed(subnetwork);

			var offset = {
					x : undefined,
					y : undefined,
			}
			
			var constraint = subnetwork;
			
			var equipmentIDs = model.getEquipmentsByLayer(uppermostLayer);
			_.each(equipmentIDs, function(equipmentID, index) {
				
				switch(index) {
					case 0 :
						console.log('1 EQUIP INDEX:' + index);
						offset.x = 185;
						offset.y = 15;
						break;
					case 1 :
						console.log('2 EQUIP INDEX:' + index);
						offset.x = 235;
						offset.y = 10;
						break;
					case 2 :
						console.log('3 EQUIP INDEX:' + index);
						offset.x = 285;
						offset.y = 5;
						break;
					case 3 :
						offset.x = 285;
						offset.y = 5;
						break;
					default:
						offset.x = 285;
						offset.y = 5;
						break;
				}
				
				
				var accessGroup = Stencil.createAccessGroup(equipmentID);
				graph.addCell(accessGroup);
				accessGroup.translate(subnetworkPosition.x + offset.x, subnetworkPosition.y - offset.y);
				
				
				
				
				subnetwork.embed(accessGroup);
				
			}, this);
			
		}, this);
		
		
	},
	
});