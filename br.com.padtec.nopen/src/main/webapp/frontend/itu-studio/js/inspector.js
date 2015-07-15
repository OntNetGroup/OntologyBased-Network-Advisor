

var CommonInspectorGroups = {
		
    data: { label: 'Data', index: 1 }
};

var InspectorDefs = {

    'link': {

        inputs: {
            attrs: {
            },
    		type: { type: 'text', group: 'data', index: 1, label: 'Type', attrs: { 'input': {'disabled': 'disabled'} } }
        },
    	groups: CommonInspectorGroups
    },

    // Interface
    // -----

    'basic.Rect': {

    	inputs: {
    		attrs: {
				text: {
					text: { type: 'text', group: 'data', label: 'Name', index: 1 }
				}
    		}
    	},
    	groups: CommonInspectorGroups
    },
    
    'basic.Circle': {

    	inputs: {
			attrs: {
				text: {
					text: { type: 'text', group: 'data', label: 'Name', index: 1 }
				}
			}
    	},
    	groups: CommonInspectorGroups
    },

    // Transport Function
    // -----
    
    'basic.Path': {
    	inputs: {
			attrs: {
				text: {
					text: { type: 'text', group: 'data', label: 'Name', index: 1 }
				}
			},
    		subtype: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled': 'disabled'} } }
    	},
    	groups: CommonInspectorGroups
    },

    // Layer
    // -----
    
    'bpmn.Pool': {
    	inputs: {
    		lanes: {
				label: { type: 'text', group: 'data', label: 'Layer', index: 1, attrs: { 'input': {'disabled' : 'disabled'} } }
    		}
    	},
    	groups: CommonInspectorGroups
    }
};
