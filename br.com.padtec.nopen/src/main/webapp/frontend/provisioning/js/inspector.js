

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
    		subtype: { type: 'text', group: 'data', index: 1, label: 'Type', attrs: { 'input': {'disabled': 'disabled'} } }
    	},
    	groups: CommonInspectorGroups
    },

    // Subnetwork
    // -----
    
    'Subnetwork': {
    	inputs: {
    		subtype: { type: 'text', group: 'data', index: 1, label: 'Type', attrs: { 'input': {'disabled': 'disabled'} } }
    	},
    	groups: CommonInspectorGroups
    },

    // Layer Network
    // -----
    
    'Layer_Network': {
    	inputs: {
    		uppermostLayer: { type: 'text', group: 'data', index: 1, label: 'Layer', attrs: { 'input': {'disabled': 'disabled'} } },
    		lanes: {
				label: { type: 'text', group: 'data', label: 'Technology', index: 2, attrs: { 'input': {'disabled' : 'disabled'} } }
    		}
    	},
    	groups: CommonInspectorGroups
    }
};