var Stencil = {};

Stencil.groups = {
    basic: { index: 1, label: 'Basic shapes' },
	itu: { index: 2, label: 'ITU' }
};

Stencil.shapes = {

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
        }),
        new joint.shapes.devs.Atomic({
            size: { width: 90, height: 60 },
            inPorts: ['in1','in2'],
            outPorts: ['out'],
            attrs: {
	        rect: { fill: '#8e44ad', rx: 2, ry: 2 },
                '.label': { text: 'model', fill: '#ffffff', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 },
				'.inPorts circle': { fill: '#f1c40f', opacity: 0.9 },
                '.outPorts circle': { fill: '#f1c40f', opacity: 0.9 },
				'.inPorts text, .outPorts text': { 'font-size': 9 }
            }
        }),
		
		new joint.shapes.bpmn.Pool({
			attrs: {
				'.': { magnet: false },
				'.header': { fill: '#5799DA' }
			},
			lanes: { label: 'Pool' }
		})
    ],
	
	itu: [
	
		new joint.shapes.basic.Path({
			type: 'AF',
			size: { width: 80, height: 80 },
			attrs: {
			        path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: '#8e44ad' },
			    	text: { text: 'AF', 'font-size': 9, display: '', 'ref-y': .2, fill: 'white'  }
			}
		}),

		new joint.shapes.basic.Path({
			type: 'TTF',
			size: { width: 80, height: 80 },
			attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: '#8e44ad' },
			    	text: { text: 'TTF', 'font-size': 9, display: '', 'ref-y': .2, fill: 'white'  }
			}
		}),
		
		/* RF: Inserir portas de entrada e saída aos nós */
		// porta de saída
        new joint.shapes.basic.Circle({
            size: { width: 30, height: 30 },
            attrs: {
                circle: { fill: '#f1c40f' },
                text: { text: 'in', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
            }
        }),
		
		// porta de entrada
		new joint.shapes.basic.Rect({
            size: { width: 30, height: 30 },
            attrs: {
                rect: {
                    rx: 2, ry: 2,
                    fill: '#e9967a'
                },
                text: { text: 'out', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
            }
        })
	]
};
