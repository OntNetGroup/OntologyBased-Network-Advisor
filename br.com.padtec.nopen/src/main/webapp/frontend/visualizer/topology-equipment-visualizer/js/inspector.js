var CommonInspectorGroups = {
		
//		data: { label: 'General properties:', index: 1 , attrs: { 'data': { 'data-tooltip': 'General properties of the equipment.' } }},
//		specific: { label: 'Specific properties:', index: 2 , attrs: { 'data': { 'data-tooltip': 'Specific properties of the equipment.' } }},
//        attributes: { label: 'Attributes:', index: 3 , attrs: { 'data': { 'data-tooltip': 'Attributes of the card.' } }},
        gcc0: {label:'GCC0-tp-grouping' , index: 1 ,attrs:{'data': {'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}} },
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
		'gcc0-tp':{
			inputs: {
				directionality: {type: 'select' , option:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
		        application: {type: 'text' ,index: 2 , label: "Application", attrs: {'label' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}}
			},
		},
		'gcc12-tp':{
			
		},
		'och-client-ctp':{
			
		},
		'och-ctp':{
			
		},
		'och-nim':{
		},
		'ochr-ctp':{
			
		},
		'ochr-ttp':{
			
		},
		'och-ttp':{
			
		},
		'octk-nim':{
			
		},
		'oduk-client-ctp':{
			
		},
		'oduk-ctp':{
			
		},
		'oduk-nim':{
			
		},
		'odukt-nim':{
			
		},
		'odukt-nim':{
			
		},
		'oduk-ttp':{
			
		},
		'odukt-ttp':{
			
		},
		'omsn-ctp':{
			
		}, 'omnsnp':{
			
		}, 
		'omsn-ttp':{
			
		},
		'opsn-ttp':{
			
		}, 
		'opsn-ttp':{
			
		}, 
		'otmn-entity':{
			
		}, 
		'otsn-ttp':{
			
		},
		'otuk-ctp':{
			
		},
		'otuk-ttp':{
			
		},
		
};


var gcc0tp{
		
		var CommonInspectorGroups = {
				data:{label: 'gcc0-tp-grouping', index : 1, attrs: {'data': { 'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}}},
		};
//		"gcc0-tp-grouping": 
//				"_comment":
//					"This entity represents the function of terminating 
//					and/or originating of the GCC0 channels.",
//												
//				"directionality":
//				{
//					"type": "select",
//					"option": ["sink","source","bidirectional"],
//					"label": "Directionality",
//					
//					"attrs":
//					{
//						"input":
//						{
//							"data-tooltip": 
//								"This attribute indicates the directionality of the<br> 
//								termination point. Valid values are sink, source,<br> 
//								and bidirectional. This attribute is read-only."
//						}
//					}
//				},
//						
//				"application":
//				{
//					"type": "text",
//					"label": "Application",
//					
//					"attrs":
//					{
//						"input":
//						{
//							"data-tooltip": 
//								"This attribute indicates the applications<br> 
//								transported by the GCC channel. Example applications<br> 
//								are ECC, (user data channel). Valid values are<br> 
//								string. This attribute is read-only."
//						}
//					}			
//				},		
			
//		}	
};