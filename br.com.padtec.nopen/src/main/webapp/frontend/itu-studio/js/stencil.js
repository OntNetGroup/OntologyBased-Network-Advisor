var Stencil = {};

Stencil.groups = {
		layers: { index: 1, label: 'Layers'},
		itu: { index: 2, label: 'ITU' },
		basic: { index: 3, label: 'Basic shapes' }
};

Stencil.shapes = {
	
	layers: [
			new MyPool({
				subtype: 'OTS',
				attrs: {
					'.': { magnet: false },
					'.header': { fill: '#5799DA' }
				},
				lanes: { label: 'OTS' }
			}),
			
			new MyPool({
				subtype: 'OTU',
				attrs: {
					'.': { magnet: false },
					'.header': { fill: '#5799DA' }
				},
				lanes: { label: 'OTU' }
			}),
			
			new MyPool({
				subtype: 'ODU',
				attrs: {
					'.': { magnet: false },
					'.header': { fill: '#5799DA' }
				},
				lanes: { label: 'ODU' }
			}),
			
			new MyPool({
				subtype: 'OCh',
				attrs: {
					'.': { magnet: false },
					'.header': { fill: '#5799DA' }
				},
				lanes: { label: 'OCh' }
			}),
			
			new MyPool({
				subtype: 'OMS',
				attrs: {
					'.': { magnet: false },
					'.header': { fill: '#5799DA' }
				},
				lanes: { label: 'OMS' }
			})
	],

    itu: [
	
		new joint.shapes.basic.Path({
			subtype: 'AF',
			attrs: {
			        path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: '#8e44ad' },
			    	text: { text: 'AF', 'font-size': 9, display: '', 'ref-y': .2, fill: 'white'  }
			}
		}),

		new joint.shapes.basic.Path({
			subtype: 'TTF',
			attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: '#8e44ad' },
			    	text: { text: 'TTF', 'font-size': 9, display: '', 'ref-y': .2, fill: 'white'  }
			}
		}),
		
		/* RF: Inserir portas de entrada e saída aos nós */
		// porta de saída
        new joint.shapes.basic.Circle({
        	subtype: 'out',
            attrs: {
                circle: { fill: '#f1c40f' },
                text: { text: 'in', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
            }
        }),
		
		// porta de entrada
		new joint.shapes.basic.Rect({
			subtype: 'in',
            attrs: {
                rect: {
                    rx: 2, ry: 2,
                    fill: '#e9967a'
                },
                text: { text: 'out', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
            }
        })
	],
	
	basic: [
	        new joint.shapes.basic.Rect({
	            size: { width: 100, height: 60 },
	            attrs: {
	                rect: {
	                    rx: 2, ry: 2,
	                    fill: '#27AE60'
	                },
	                text: { text: 'rect', fill: '#ffffff', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
	            }
	        }),
	        new joint.shapes.basic.Circle({
	            size: { width: 100, height: 60 },
	            attrs: {
	                circle: { fill: '#E74C3C' },
	                text: { text: 'ellipse', fill: '#ffffff', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
	            }
	        })
	    ]
};
