var Stencil = {};

Stencil.groups = {
		layers: { index: 1, label: 'Layers'},
		transportFunctions: { index: 2, label: 'Transport Functions' },
		interfaces: { index: 3, label: 'Interfaces'},
};

Stencil.shapes = {
	
	layers: [],

	transportFunctions: [
	
		new joint.shapes.basic.Path({
			subtype: 'AF',
			name: '',
			attrs: {
			        path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: '#8e44ad' },
			    	text: { text: 'AF', 'font-size': 12, display: '', 'ref-y': .2, fill: 'white'  }
			}
		}),

		new joint.shapes.basic.Path({
			subtype: 'TTF',
			name: '',
			attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: '#8e44ad' },
			    	text: { text: 'TTF', 'font-size': 12, display: '', 'ref-y': .2, fill: 'white'  }
			}
		})
		
	],
	
	/* RF: Inserir portas de entrada e saída aos nós */
	interfaces: [
		//porta de entrada
		new joint.shapes.basic.Circle({
			subtype: 'in',
			name: '',
		    attrs: {
				'.': { magnet: true },
		        circle: { fill: '#f1c40f' },
		        text: { text: 'in', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
		    }
		}),
		
		// porta de saída
		new joint.shapes.basic.Rect({
			subtype: 'out',
			name: '',
		    attrs: {
				'.': { magnet: true },
		        rect: {
		            rx: 2, ry: 2,
		            fill: '#e9967a'
		        },
		        text: { text: 'out', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
		    }
		})
	]
};