
var Stencil = {};

var Stencil = {
		
		createLayerNetwork: function(tech, upLayer) {
			var newLayerNetwork = new LayerNetwork({
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
			
			return newLayerNetwork;
		},
		
		createSubnetwork: function() {
			var newSubnetwork = new joint.shapes.basic.Circle({
				subtype: 'Subnetwork',
				size: { width: 300, height: 120 },
			    attrs: {
					'.': { magnet: false },
			        text: { text: 'Subnetwork', fill: '#000000', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 }
			    }
			});
			
			return newSubnetwork;
		},
		
		createAccessGroup: function(equipmentID) {
			var newAccessGroup = new joint.shapes.basic.Circle({
				id: equipmentID,
				subtype: 'Access_Group',
				size: { width: 20, height: 20 },
			    attrs: {
					'.': { magnet: false }
			    }
			});
			
			return newAccessGroup;
		}
};

