var Stencil = {};


Stencil.groups = {		
    ttf: { index: 1, label: 'TTF' },
    af: { index: 2, label: 'AF' },
    lpf: { index: 3, label: 'LPF' },
    matriz: { index: 4, label: 'Matriz' },
    input: { index: 5, label: 'Input' },
    output: { index: 6, label: 'Output' }
};

Stencil.shapes = {

		ttf: [
		     new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: 'white' },
			    	text: { text: 'TTF', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: 'yellow' },
			    	text: { text: 'TTF-BI', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: 'lightpink' },
			    	text: { text: 'TTF-SINK', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			        path: { d: 'M 0 0 L 0.5 1 L 1 0 z', fill: 'greenyellow' },
			    	text: { text: 'TTF-SOURCE', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			})		
		],
		
		af: [
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: 'white'},
			    	text: { text: 'AF', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: 'yellow'},
			    	text: { text: 'AF-BI', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: 'lightpink'},
			    	text: { text: 'AF-SINK', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 50 0 L 0 0 L 25 50 L 75 50 L 100 0  z', fill: 'greenyellow'},
			    	text: { text: 'AF-SOURCE', 'font-size': 6, display: '', 'ref-y': .2  }
			    }
			})
		],
		
		lpf: [
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 2 L 2 4 L 4 2 L 3 0 L 1 0 L 0 2  z', fill: 'white'},
			    	text: { text: 'LBF', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 2 L 2 4 L 4 2 L 3 0 L 1 0 L 0 2  z', fill: 'yellow'},
			    	text: { text: 'LBF-BI', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 2 L 2 4 L 4 2 L 3 0 L 1 0 L 0 2  z', fill: 'lightpink'},
			    	text: { text: 'LBF-SINK', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 2 L 2 4 L 4 2 L 3 0 L 1 0 L 0 2  z', fill: 'greenyellow'},
			    	text: { text: 'LBF-SOURCE', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			})
		],
		
		matriz: [
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 0 L 0 2 L 2 2 L 2 0 L 0 0  z', fill: 'white'},
			    	text: { text: 'Matriz', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 0 L 0 2 L 2 2 L 2 0 L 0 0  z', fill: 'yellow'},
			    	text: { text: 'Matriz-BI', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 0 L 0 2 L 2 2 L 2 0 L 0 0  z', fill: 'lightpink'},
			    	text: { text: 'Matriz-SINK', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 0 L 0 2 L 2 2 L 2 0 L 0 0  z', fill: 'greenyellow'},
			    	text: { text: 'Matriz-SOURCE', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			})
		],
		
		
		input: [
		
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 3 1 L 2 2 L 0 2 L 0 0 L 2 0 L 3 1 z', fill: 'lightsalmon'},
			    	text: { text: 'INPUT', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 1 L 1.5 1 L 1.5 0 L 3 1.5 L 1.5 3 L 1.5 2 L 0 2 L 0 1 z', fill: 'lightsalmon'},
			    	text: { text: 'IN-INTERFACE', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			})   
		],
		
		output: [
				
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 3 1 L 2 2 L 0 2 L 0 0 L 2 0 L 3 1 z', fill: 'greenyellow'},
			    	text: { text: 'OUTPUT', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			}),
			new joint.shapes.basic.Path ({
			    size: { width: 5, height: 5 },
			    attrs: {
			    	path: { d: 'M 0 1 L 1.5 1 L 1.5 0 L 3 1.5 L 1.5 3 L 1.5 2 L 0 2 L 0 1 z', fill: 'greenyellow'},
			    	text: { text: 'OUT-INTERFACE', 'font-size': 6, display: '', 'ref-y': .5  }
			    }
			})   
		]

		
}