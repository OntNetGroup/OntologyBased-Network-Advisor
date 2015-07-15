

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

    // Access Group
    // -----
    
    'Access_Group': {
    	inputs: {
    		subtype: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled': 'disabled'} } }
    	},
    	groups: CommonInspectorGroups
    },

    // Layer Network
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