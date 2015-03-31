var Stencil = {};

Stencil.groups = {
		equipment: { index: 1, label: 'Equipment Holder' },

};

Stencil.shapes = {

		equipment: [
		            //Rack
		            new joint.shapes.basic.Rect({
		            	subType : 'rack' ,
		            	size: { width:80 , height: 160 },

		            	attrs: {
		            		rect: {
		            			rx: 2, ry: 2,
		            			fill: '#767572'
		            		},
		            		//text: { text: 'rack', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),
		            //Shelf
		            new joint.shapes.basic.Rect({
		            	size: { width: 60, height: 45 },
		            	subType : 'shelf' ,
		            	attrs: {
		            		rect: {
		            			rx: 2, ry: 2,
		            			fill: '#C7C7C5'
		            		},
		            		//text: { text: 'shelf', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),
		            //Slot
		            new joint.shapes.basic.Rect({
		            	size: { width: 15, height: 35 },
		            	subType : 'slot' ,
		            	attrs: {
		            		rect: {
		            			rx: 2, ry: 2,
		            			fill: '#B3CBCE'
		            		},
		            		//text: { text: 'slot', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),
		            //Card
		            new joint.shapes.basic.Rect({
		            	size: { width: 10, height: 20 },
		            	subType : 'card' ,
		            	attrs: {
		            		rect: {
		            			rx: 2, ry: 2,
		            			fill: '#ADC2A7'
		            		},
		            		//text: { text: 'card', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),
		            //Supervisor
		            new joint.shapes.basic.Rect({
		            	subType : 'supervisor' ,
		            	size: { width: 10, height: 20 },

		            	attrs: {
		            		rect: {
		            			rx: 2, ry: 2,
		            			fill: '#42563D'
		            		},
		            		//text: { text: 'supervisor card', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),		            
		            ],
};
//referencias
/* new joint.shapes.devs.Atomic({
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
        }),*/


/*new joint.shapes.bpmn.Pool({
			attrs: {
				'.': { magnet: false },
				'.header': { fill: '#5799DA' }
			},
			lanes: { label: 'Pool' }
		})
    ],*/

/*        itu: [

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

		                  /* RF: Inserir portas de entrada e saída aos nós 
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
		                  ]*/

