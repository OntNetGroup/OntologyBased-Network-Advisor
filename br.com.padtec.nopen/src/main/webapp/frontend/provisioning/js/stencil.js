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