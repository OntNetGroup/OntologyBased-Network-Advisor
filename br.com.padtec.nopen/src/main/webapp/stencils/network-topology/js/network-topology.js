var Stencil = {};



Stencil.groups = {		
    np: { index: 1, label: 'Network Topology' }
};

Stencil.shapes = {

		np: [
		     new joint.shapes.basic.Circle ({
		    	subtype : "Node", 
			    size: { width: 5, height: 5 },
			    attrs: {
			    	circle: { fill: '#00c6ff' },
			    	text: { text: 'NODE', 'font-size': 8, display: '', 'ref-y': .5  }
			    }
			})
		]
		
}