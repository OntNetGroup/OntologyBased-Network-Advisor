var CommonInspectorGroups = {
    data: { label: 'Data', index: 1 }
};


var InspectorDefs = {

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

    // Interface
    // -----

    'basic.Rect': {

    	inputs: {
    		name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of interface.' } } },
    	},
    	groups: CommonInspectorGroups
    },
    
    'basic.Circle': {

    	inputs: {
    		name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of interface.' } } },
    	},
    	groups: CommonInspectorGroups
    },

    // Transport Function
    // -----
    
    'basic.Path': {
    	inputs: {
    		name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of transport function.' } } },
    	},
    	groups: CommonInspectorGroups
    }
};
