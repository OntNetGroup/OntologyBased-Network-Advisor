//var CommonInspectorInputs = {

//size: {
//width: { type: 'number', min: 1, max: 500, group: 'geometry', label: 'width', index: 1 },
//height: { type: 'number', min: 1, max: 500, group: 'geometry', label: 'height', index: 2 }
//},
//position: {
//x: { type: 'number', min: 1, max: 2000, group: 'geometry', label: 'x', index: 3 },
//y: { type: 'number', min: 1, max: 2000, group: 'geometry', label: 'y', index: 4 }
//},
//custom: { type: 'text', group: 'data', index: 1, label: 'Custom data', attrs: { 'label': { 'data-tooltip': 'An example of setting custom data via Inspector.' } } }
//};

//var CommonInspectorGroups = {

//text: { label: 'Text', index: 1 },
//presentation: { label: 'Presentation', index: 2 },
//geometry: { label: 'Geometry', index: 3 },
//data: { label: 'Data', index: 4 }
//};

var CommonInspectorGroups = {

		data: { label: 'General properties:', index: 1 , attrs: { 'data': { 'data-tooltip': 'General properties of the equipment.' } }},
		specific: { label: 'Specific properties:', index: 2 , attrs: { 'data': { 'data-tooltip': 'Specific properties of the equipment.' } }}
};

//var CommonInspectorTextInputs = {
//'text': { type: 'textarea', group: 'text', text: 'jesus eh o senhor', index: 1 },
//'font-size': { type: 'range', min: 5, max: 80, unit: 'px', group: 'text', index: 2 },
//'font-family': { type: 'select', options: ['Arial', 'Helvetica', 'Times New Roman', 'Courier New', 'Georgia', 'Garamond', 'Tahoma', 'Lucida Console', 'Comic Sans MS'], group: 'text', index: 3 },
//'font-weight': { type: 'range', min: 100, max: 900, step: 100, defaultValue: 400, group: 'text', index: 4 },
//'fill': { type: 'color', group: 'text', index: 5 },
//'stroke': { type: 'color', group: 'text', index: 6, defaultValue: '#000000' },
//'stroke-width': { type: 'range', min: 0, max: 5, step: .5, defaultValue: 0, unit: 'px', group: 'text', index: 7 },
//'ref-x': { type: 'range', min: 0, max: .9, step: .1, defaultValue: .5, group: 'text', index: 8 },
//'ref-y': { type: 'range', min: 0, max: .9, step: .1, defaultValue: .5, group: 'text', index: 9 }
//};

//var InputDefs = {
//text: { type: 'textarea', defaultValue:'asdfasfas', label: 'Textadhsr' },
//'font-size': { type: 'range', min: 5, max: 80, unit: 'px', label: 'Font size' },
//'font-family': { type: 'select', options: ['Arial', 'Helvetica', 'Times New Roman', 'Courier New', 'Georgia', 'Garamond', 'Tahoma', 'Lucida Console', 'Comic Sans MS'], label: 'Font family' },
//'font-weight': { type: 'range', min: 100, max: 900, step: 100, defaultValue: 400, label: 'Font weight' },
//'fill': { type: 'color', label: 'Fill color' },
//'stroke': { type: 'color', defaultValue: '#000000', label: 'Stroke' },
//'stroke-width': { type: 'range', min: 0, max: 5, step: .5, defaultValue: 0, unit: 'px', label: 'Stroke width' },
//'ref-x': { type: 'range', min: 0, max: .9, step: .1, defaultValue: .5, label: 'Horizontal alignment' },
//'ref-y': { type: 'range', min: 0, max: .9, step: .1, defaultValue: .5, label: 'Vertical alignment' },
//'ref-dx': { type: 'range', min: 0, max: 50, step: 1, defaultValue: 0, label: 'Horizontal offset' },
//'ref-dy': { type: 'range', min: 0, max: 50, step: 1, defaultValue: 0, label: 'Vertical offset' },
//'dx': { type: 'range', min: 0, max: 50, step: 1, defaultValue: 0, label: 'Horizontal distance' },
//'dy': { type: 'range', min: 0, max: 50, step: 1, defaultValue: 0, label: 'Vertical distance' },
//'stroke-dasharray': { type: 'select', options: ['0', '1', '5,5', '5,10', '10,5', '3,5', '5,1', '15,10,5,10,15'], label: 'Stroke dasharray' },
//rx: { type: 'range', min: 0, max: 30, defaultValue: 1, unit: 'px', label: 'X-axis radius' },
//ry: { type: 'range', min: 0, max: 30, defaultValue: 1, unit: 'px', label: 'Y-axis radius' },
//'xlink:href': { type: 'text', label: 'Image URL' }
//};

//function inp(defs) {
//var ret = {};
//_.each(defs, function(def, attr) {

//ret[attr] = _.extend({}, InputDefs[attr], def);
//});
//return ret;
//}

var InspectorDefs = {

		'Rack': {
			inputs: {
				//name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of the rack.' } } },
				attrs: {
					name: {
						text: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'input': {'disabled' : 'disabled'}, 'label': { 'data-tooltip': 'Name of the equipment.' } } },
					}
//					text: {
//					text: { type: 'text', group: 'data', label: 'Name', index: 1 , attrs: { 'label': { 'data-tooltip': 'Name of the equipment.' }} }
//					}
				},
				subType: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Type of the equipment.' } } },
//				id: { type: 'text', group: 'data', index: 3, label: 'ID', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'ID of the equipment.' } } }
			},
			groups: CommonInspectorGroups

		},

		'Shelf': {
			inputs: {
//				name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of the Shelf.' } } },
				attrs: {
					name: {
						text: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'input': {'disabled' : 'disabled'}, 'label': { 'data-tooltip': 'Name of the equipment.' } } },
					}
//				   text: {
//				       text: { type: 'text', group: 'data', label: 'Name', index: 1 , attrs: { 'label': { 'data-tooltip': 'Name of the equipment.' }}}
//				      }
				    },
				subType: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Type of the equipment.' } } },
//				id: { type: 'text', group: 'data', index: 3, label: 'ID', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'ID of the equipment.' } } }
			},
			groups: CommonInspectorGroups
		},


		'Slot' : {
			inputs: {
//				name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of the Slot.' } } },
				attrs: {
					name: {
						text: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'input': {'disabled' : 'disabled'}, 'label': { 'data-tooltip': 'Name of the equipment.' } } },
					}
//				text: {
//				text: { type: 'text', group: 'data', label: 'Name', index: 1 , attrs: { 'label': { 'data-tooltip': 'Name of the equipment.' }} }
//				}
				},
				subType: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Type of the equipment.' } } },
//				id: { type: 'text', group: 'data', index: 3, label: 'ID', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'ID of the equipment.' } } }
			},
			groups: CommonInspectorGroups
		},


		'Card': {
			inputs: {
//				name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of the Card.' } } },
				attrs: {
					name: {
						text: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'input': {'disabled' : 'disabled'}, 'label': { 'data-tooltip': 'Name of the equipment.' } } },
					}
//				text: {
//				text: { type: 'text', group: 'data', label: 'Name', index: 1 , attrs: { 'label': { 'data-tooltip': 'Name of the equipment.' }}}
//				}
				},
				subType: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Type of the equipment.' } } },
				Supervisor: {type: 'text', group: 'specific', index: 3, label: 'Supervisor', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Name of the Supervisor of the Card.' } }},
//				id: { type: 'text', group: 'data', index: 3, label: 'ID', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'ID of the equipment.' } } },
			},
			groups: CommonInspectorGroups

		},

		'Supervisor': {
			inputs: {
//				name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of the Supervisor.' } } },
				attrs: {
					name: {
						text: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'input': {'disabled' : 'disabled'}, 'label': { 'data-tooltip': 'Name of the equipment.' } } },
					}
//				text: {
//				text: { type: 'text', group: 'data', label: 'Name', index: 1 , attrs: { 'label': { 'data-tooltip': 'Name of the equipment.' }} }
//				}
				},
				subType: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Type of the equipment.' } } },
				tech: { type : 'text' , group: 'specific', index: 3, label: 'Technology', attrs: {'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Technology of the Supervisor.' }}},
//				id: { type: 'text', group: 'data', index: 3, label: 'ID', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'ID of the equipment.' } } }
			},
			groups: CommonInspectorGroups
		},





		'link': {

			inputs: {
				attrs: {
					'.connection': {
						'stroke-width': { type: 'range', min: 0, max: 50, defaultValue: 1, unit: 'px', group: 'connection', label: 'stroke width', index: 1 },
						'stroke': { type: 'color', group: 'connection', label: 'stroke color', index: 2 },
						'stroke-dasharray': { type: 'select', options: ['0', '1', '5,5', '5,10', '10,5', '5,1', '15,10,5,10,15'], group: 'connection', label: 'stroke dasharray', index: 3 }
					},
					'.marker-source': {
						transform: { type: 'range', min: 1, max: 15, unit: 'x scale', defaultValue: 'scale(1)', valueRegExp: '(scale\\()(.*)(\\))', group: 'marker-source', label: 'source arrowhead size', index: 1 },
						fill: { type: 'color', group: 'marker-source', label: 'soure arrowhead color', index: 2 }
					},
					'.marker-target': {
						transform: { type: 'range', min: 1, max: 15, unit: 'x scale', defaultValue: 'scale(1)', valueRegExp: '(scale\\()(.*)(\\))', group: 'marker-target', label: 'target arrowhead size', index: 1 },
						fill: { type: 'color', group: 'marker-target', label: 'target arrowhead color', index: 2 }
					}
				},
				smooth: { type: 'toggle', group: 'connection', index: 4 },
				manhattan: { type: 'toggle', group: 'connection', index: 5 },
				labels: {
					type: 'list',
					group: 'labels',
					attrs: {
						label: { 'data-tooltip': 'Set (possibly multiple) labels for the link' }
					},
					item: {
						type: 'object',
						properties: {
							position: { type: 'range', min: 0.1, max: .9, step: .1, defaultValue: .5, label: 'position', index: 2, attrs: { label: { 'data-tooltip': 'Position the label relative to the source of the link' } } },
							attrs: {
								text: {
									text: { type: 'text', label: 'text', defaultValue: 'label', index: 1, attrs: { label: { 'data-tooltip': 'Set text of the label' } } }
								}
							}
						}
					}
				}

			},
			groups: {
				labels: { label: 'Labels', index: 1 },
				'connection': { label: 'Connection', index: 2 },
				'marker-source': { label: 'Source marker', index: 3 },
				'marker-target': { label: 'Target marker', index: 4 }
			}
		},

		// Basic
		// -----

//		'basic.Rect': {

//		inputs: _.extend({
//		attrs: {
//		text: inp({
//		text: { group: 'text', index: 1 },
//		'font-size': { group: 'text', index: 2 },
//		'font-family': { group: 'text', index: 3 },
//		'font-weight': { group: 'text', index: 4 },
//		fill: { group: 'text', index: 5 },
//		stroke: { group: 'text', index: 6 },
//		'stroke-width': { group: 'text', index: 7 },
//		'ref-x': { group: 'text', index: 8 },
//		'ref-y': { group: 'text', index: 9 }
//		}),
//		rect: inp({
//		fill: { group: 'presentation', index: 1 },
//		'stroke-width': { group: 'presentation', index: 2, min: 0, max: 30, defaultValue: 1 },
//		'stroke-dasharray': { group: 'presentation', index: 3 },
//		rx: { group: 'presentation', index: 4 },
//		ry: { group: 'presentation', index: 5 }
//		}),

//		}
//		}, CommonInspectorInputs),
//		groups: CommonInspectorGroups
//		},

//		'basic.Circle': {

//		inputs: _.extend({
//		attrs: {
//		text: inp({
//		text: { group: 'text', index: 1 },
//		'font-size': { group: 'text', index: 2 },
//		'font-family': { group: 'text', index: 3 },
//		'font-weight': { group: 'text', index: 4 },
//		fill: { group: 'text', index: 5 },
//		stroke: { group: 'text', index: 6 },
//		'stroke-width': { group: 'text', index: 7 },
//		'ref-x': { group: 'text', index: 8 },
//		'ref-y': { group: 'text', index: 9 }
//		}),
//		circle: inp({
//		fill: { group: 'presentation', index: 1 },
//		'stroke-width': { group: 'presentation', index: 2, min: 0, max: 30, defaultValue: 1 },
//		'stroke-dasharray': { type: 'select', options: ['0', '1', '5,5', '5,10', '10,5', '5,1', '15,10,5,10,15'], group: 'presentation', index: 3 }
//		})
//		}
//		}, CommonInspectorInputs),
//		groups: CommonInspectorGroups
//		},

//		'basic.Image': {

//		inputs: _.extend({
//		attrs: {
//		text: inp({
//		text: { group: 'text', index: 1 },
//		'font-size': { group: 'text', index: 2 },
//		'font-family': { group: 'text', index: 3 },
//		'font-weight': { group: 'text', index: 4 },
//		fill: { group: 'text', index: 5 },
//		stroke: { group: 'text', index: 6 },
//		'stroke-width': { group: 'text', index: 7 },
//		'ref-x': { group: 'text', index: 8 },
//		'ref-dy': { group: 'text', index: 9 }
//		}),
//		image: inp({
//		'xlink:href': { group: 'presentation', index: 1 }
//		})
//		}
//		}, CommonInspectorInputs),
//		groups: CommonInspectorGroups
//		},

//		// DEVS
//		// ----

//		'devs.Atomic': {

//		inputs: _.extend({
//		attrs: {
//		'.label': inp({
//		text: { group: 'text', index: 1 },
//		'font-size': { group: 'text', index: 2 },
//		'font-family': { group: 'text', index: 3 },
//		'font-weight': { group: 'text', index: 4 },
//		fill: { group: 'text', index: 5 },
//		stroke: { group: 'text', index: 6 },
//		'stroke-width': { group: 'text', index: 7 },
//		'ref-x': { group: 'text', index: 8 },
//		'ref-y': { group: 'text', index: 9, min: 0, max: 30, step: 1 }
//		}),
//		rect: inp({
//		fill: { group: 'presentation', index: 1 },
//		'stroke-width': { min: 0, max: 30, defaultValue: 1, unit: 'px', group: 'presentation', index: 2 },
//		'stroke-dasharray': { group: 'presentation', index: 3 },
//		'rx': { group: 'presentation', index: 4 },
//		'ry': { group: 'presentation', index: 5 }
//		}),
//		'.inPorts circle': inp({
//		fill: { group: 'presentation', index: 6, label: 'Input ports fill color' }
//		}),
//		'.outPorts circle': inp({
//		fill: { group: 'presentation', index: 7, label: 'Output ports fill color' }
//		})
//		},
//		inPorts: { type: 'list', item: { type: 'text' }, group: 'data', index: -2 },
//		outPorts: { type: 'list', item: { type: 'text' }, group: 'data', index: -1 }

//		}, CommonInspectorInputs),
//		groups: CommonInspectorGroups
//		},

//		'devs.Model': {

//		inputs: _.extend({
//		attrs: {
//		'.label': inp({
//		text: { group: 'text', index: 1 },
//		'font-size': { group: 'text', index: 2 },
//		'font-family': { group: 'text', index: 3 },
//		'font-weight': { group: 'text', index: 4 },
//		fill: { group: 'text', index: 5 },
//		stroke: { group: 'text', index: 6 },
//		'stroke-width': { group: 'text', index: 7 },
//		'ref-x': { group: 'text', index: 8 },
//		'ref-y': { group: 'text', index: 9, min: 0, max: 30, step: 1 }
//		}),
//		rect: inp({
//		fill: { group: 'presentation', index: 1 },
//		'stroke-width': { min: 0, max: 30, defaultValue: 1, unit: 'px', group: 'presentation', index: 2 },
//		'stroke-dasharray': { group: 'presentation', index: 3 },
//		'rx': { group: 'presentation', index: 4 },
//		'ry': { group: 'presentation', index: 5 }
//		}),
//		'.inPorts circle': inp({
//		fill: { group: 'presentation', index: 6, label: 'Input ports fill color' }
//		}),				
//		'.outPorts circle': inp({
//		fill: { group: 'presentation', index: 7, label: 'Output ports fill color' }
//		})
//		},
//		inPorts: { type: 'list',
//		item: { type: 'text' },
//		group: 'data', index: -2 },
//		outPorts: { type: 'list',
//		item: { type: 'text' },
//		group: 'data', index: -1 }

//		}, CommonInspectorInputs),
//		groups: CommonInspectorGroups
//		}
};
