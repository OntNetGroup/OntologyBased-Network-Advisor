var Stencil = {};

Stencil.groups = {
		layers: { index: 1, label: 'Layers'},
		transportFunctions: { index: 2, label: 'Transport Functions' },
		interfaces: { index: 3, label: 'Interfaces'}
};

Stencil.shapes = {
	
	layers: [],

	/* ----- STENCIL RESTRICTION ----- */
	/* all transport functions, and only transport functions, must be of type 'basic.Path' */
	transportFunctions: [
	
		new joint.shapes.basic.Path({
			subtype: 'Adaptation_Function',
			attrs: {
			        path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: 'slateblue' },
			    	text: { text: 'AF', 'font-size': 12, display: '', 'ref-y': .2, fill: 'white'  }
			}
		}),

		new joint.shapes.basic.Path({
			subtype: 'Trail_Termination_Function',
			attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: 'tomato' },
			    	text: { text: 'TTF', 'font-size': 12, display: '', 'ref-y': .2, fill: 'white'  }
			}
		}),
		
		new joint.shapes.basic.Path({
			subtype: 'Matrix',
			attrs: {
			        path: { d: squarePath(50), fill: 'green' },
			    	text: { text: 'Matrix', 'font-size': 12, display: '', 'ref-y': .2, fill: 'white'  }
			}
		})
		
//		new joint.shapes.basic.Path({
//			subtype: 'Physical_Media',
//			attrs: {
//			        path: { transform: 'rotate(90 40 40)', d: 'M 0 0 A 50 10 0 0 0 100 0 A 50 10 0 0 0 0 0 M 100 0 L 100 100 A 50 10 0 0 1 0 100 L 0 0 A 50 10 0 0 0 100 0', fill: 'gray' },
//			    	text: { text: 'Physical Media', 'font-size': 12, display: '', 'ref-y': .5, fill: 'black'  }
//			}
//		})
		
	],
	
	/* RF: Inserir portas de entrada e saída aos nós */
	interfaces: [
		//porta de entrada
		new joint.shapes.basic.Circle({
			subtype: 'Input',
		    attrs: {
				'.': { magnet: true },
		        circle: { fill: '#f1c40f' },
		        text: { text: 'IN', fill: '#000000', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 }
		    }
		}),
		
		// porta de saída
		new joint.shapes.basic.Rect({
			subtype: 'Output',
		    attrs: {
				'.': { magnet: true },
		        rect: {
		            rx: 2, ry: 2,
		            fill: '#e9967a'
		        },
		        text: { text: 'OUT', fill: '#000000', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 }
		    }
		})
	]
};

function squarePath(size) {
	return 'M 0 0 L ' +size+ ' 0 L ' +size+ ' ' +size+ ' L 0 ' +size+ ' Z';
};