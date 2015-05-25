var Stencil = {};

Stencil.groups = {
		equipment: { index: 1, label: 'Equipment Holder' },
		card: { index: 2, label: 'Card'  }
};

Stencil.shapes = {

		equipment: [

		            //Shelf
		            new joint.shapes.basic.Rect({
		            	size: { width: 60, height: 45 },
		            	subType : 'shelf' ,
		            	attrs: {
		            		rect: {
		            			fill: '#C7C7C5',
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
		            			//fill: '#B3CBCE'
		            			fill: '#7ad7fd',
		            		},
		            		//text: { text: 'slot', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),
		            //Card
		            new joint.shapes.basic.Rect({
		            	size: { width: 10, height: 20 },
		            	subType : 'card' ,
		            	supervisor: '' ,
		            	supervisorID: '' ,
		            	attrs: {
		            		rect: {
		            			//fill: '#ADC2A7'
		            			fill: '#fffb82'
		            		},
		            		//text: { text: 'card', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),
		            //Supervisor
		            new joint.shapes.basic.Rect({
		            	subType : 'supervisor' ,
		            	tech : '' ,
		            	size: { width: 10, height: 20 },
		            	attrs: {
		            		rect: {
		            			fill: '#516949'
		            				//fill: '#000000'
		            		},
		            		//text: { text: 'supervisor card', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),		
		            //Rack
		            new joint.shapes.basic.Rect({
		            	subType : 'rack' ,
		            	size: { width:40 , height: 80 },
		            	attrs: {
		            		rect: {
		            			fill: '#767572'
		            		},
		            		//text: { text: 'rack', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),

		            ],

		            interfaces: [
		                         //porta de entrada
		                         new joint.shapes.basic.Circle({
		                        	 subType: 'in',
		                        	 attrs: {
		                        		 circle: { fill: '#f1c40f' },
		                        		 text: { text: 'in', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
		                        	 }
		                         }),

		                         // porta de saída
		                         new joint.shapes.basic.Rect({
		                        	 subType: 'out',
		                        	 attrs: {
		                        		 rect: {
		                        			 rx: 2, ry: 2,
		                        			 fill: '#e9967a'
		                        		 },
		                        		 text: { text: 'out', fill: '#000000', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 }
		                        	 }
		                         })
		                         ],

		            card: [

						new joint.shapes.equipment.Card({
							size: { width: 10, height: 20 },
							subType : 'card' ,
							supervisor: '' ,
							supervisorID: '' ,
							inPorts: {},
							outPorts: {},
							attrs: {
								'.body': {
									//fill: '#ADC2A7'
									fill: '#fffb82'
								},
								'.inPort': {
						            //fill: 'none',
						        },
						        '.outPort': {
						        	//fill: 'none',
						        },
								//text: { text: 'card', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
							}
}),
		            ]
};



function circlePath(cx, cy, r){
    return 'M '+cx+' '+cy+' m -'+r+', 0 a '+r+','+r+' 0 1,0 '+(r*2)+',0 a '+r+','+r+' 0 1,0 -'+(r*2)+',0';
};

function squarePath(size) {
	return 'M 0 0 L ' +size+ ' 0 L ' +size+ ' ' +size+ ' L 0 ' +size+ ' Z';
};


//referencias
//new joint.shapes.devs.Atomic({
//size: { width: 90, height: 60 },
//inPorts: ['in1','in2'],
//outPorts: ['out'],
//attrs: {
//rect: { fill: '#8e44ad', rx: 2, ry: 2 },
//'.label': { text: 'model', fill: '#ffffff', 'font-size': 10, stroke: '#000000', 'stroke-width': 0 },
//'.inPorts circle': { fill: '#f1c40f', opacity: 0.9 },
//'.outPorts circle': { fill: '#f1c40f', opacity: 0.9 },
//'.inPorts text, .outPorts text': { 'font-size': 9 }
//}
//}),


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

