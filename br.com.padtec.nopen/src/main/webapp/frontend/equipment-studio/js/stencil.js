var Stencil = {};

Stencil.groups = {
		equipment: { index: 1, label: 'Equipment Holder' },
		cards: { index: 2, label: 'Cards' }
};

Stencil.shapes = {


		equipment: [
		            
		            //Rack
		            new joint.shapes.basic.Rect({
		            	subType : 'Rack' ,
		            	size: { width:40 , height: 80 },
		            	attrs: {
		            		rect: {
		            			fill: '#767572'
		            		},
		            		//text: { text: 'Rack', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),

		            //Shelf
		            new joint.shapes.basic.Rect({
		            	size: { width: 60, height: 45 },
		            	subType : 'Shelf' ,
		            	attrs: {
		            		rect: {
		            			fill: '#C7C7C5',
		            		},
		            		//text: { text: 'Shelf', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),
		            //Slot
		            new joint.shapes.basic.Rect({
		            	size: { width: 15, height: 35 },
		            	subType : 'Slot' ,
		            	attrs: {
		            		rect: {
		            			//fill: '#B3CBCE'
		            			fill: '#7ad7fd',
		            		},
		            		//text: { text: 'Slot', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		            	}
		            }),

		            ],

		            cards: [
		                 
		                    //Supervisor
		                    new joint.shapes.basic.Rect({
		                    	subType : 'Supervisor' ,
		                    	size: { width: 10, height: 20 },
		                    	attrs: {
		                    		rect: {
		                    			fill: '#516949'
		                    				//fill: '#000000'
		                    		},
		                    		//text: { text: 'Supervisor Card', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
		                    	}
		                    }),
		                    
		                  //Card
				            new joint.shapes.equipment.Card({
				            	size: { width: 10, height: 20 },
				            	subType : 'Card' ,
				            	Supervisor: '' ,
				            	SupervisorID: '' ,
				            	inPorts: {},
				            	outPorts: {},
				            	connectedPorts: {},
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
				            		//text: { text: 'Card', fill: '#000000', 'font-size': 15, stroke: '#000000', 'stroke-width': 0 }
				            	}
				            })
		                    
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
};

function circlePath(cx, cy, r){
	return 'M '+cx+' '+cy+' m -'+r+', 0 a '+r+','+r+' 0 1,0 '+(r*2)+',0 a '+r+','+r+' 0 1,0 -'+(r*2)+',0';
};

function squarePath(size) {
	return 'M 0 0 L ' +size+ ' 0 L ' +size+ ' ' +size+ ' L 0 ' +size+ ' Z';
};
