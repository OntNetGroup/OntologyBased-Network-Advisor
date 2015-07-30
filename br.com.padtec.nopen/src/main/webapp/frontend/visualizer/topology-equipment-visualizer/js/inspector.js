var CommonInspectorGroups = {
		
		data: { label: 'General properties:', index: 1 , attrs: { 'data': { 'data-tooltip': 'General properties of the equipment.' } }},
		specific: { label: 'Specific properties:', index: 2 , attrs: { 'data': { 'data-tooltip': 'Specific properties of the equipment.' } }},
        attributes: { label: 'Attributes:', index: 3 , attrs: { 'data': { 'data-tooltip': 'Attributes of the card.' } }},
//    equipment: { label: 'Equipment', index: 2 },	
//    presentation: { label: 'Presentation', index: 3 },
//    geometry: { label: 'Geometry', index: 4 },
//    data: { label: 'Data', index: 5 }
};

var InspectorDefs = {
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
				attributes: {type: 'text' , group: 'attributes' ,index: 4, label: 'Attributes' , attrs: {'label' : {'data-tooltip': 'www.' } } }, 
			},
			groups: CommonInspectorGroups

		},
};
