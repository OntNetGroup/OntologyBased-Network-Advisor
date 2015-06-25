
var Stencil = {};



Stencil.groups = {		
    np: { index: 1, label: 'Provisioning' }
};

Stencil.shapes = {

		np: [
		     new joint.shapes.basic.Rect ({
		    	subtype : "Node", 
			    size: { width: 5, height: 5 },
			    attrs: {
			    	circle: { fill: '#00c6ff' },
			    	text: { text: '', 'font-size': 8, display: '', 'ref-y': .5  }
			    }
			})
		]
		
}

var Stencil = {
		
		createContainer: function(tech, upLayer) {
			var newContainer = new Container({
		        technology: tech,
		        upmostLayer: upLayer,
				attrs: {
					'.': { magnet: false },
					'.header': { fill: '#5799DA' }
				},
				lanes: { 
					label: tech
				}
			});
			
			return newContainer;
		},
		
		createSubnetwork: function() {
			var newSubnetwork = new joint.shapes.basic.Circle({
			    attrs: {
					'.': { magnet: true },
			        circle: { fill: '#ffffff', r: 100 },
			        text: { text: 'Subnetwork', fill: '#000000', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 }
			    }
			});
			
			return newSubnetwork;
		},
		
		createNode: function() {
			var newNode = new joint.shapes.basic.Circle({
			    attrs: {
					'.': { magnet: true },
			        circle: { fill: '#ffffff', r: 10 }
			    }
			});
			
			return newNode;
		}
};

