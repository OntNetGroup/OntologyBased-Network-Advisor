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
		'Card':{
//				inputs: {
////					name: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'label': { 'data-tooltip': 'Name of the Card.' } } },
//					attrs: {
//						name: {
//							text: { type: 'text', group: 'data', index: 1, label: 'Name', attrs: { 'input': {'disabled' : 'disabled'}, 'label': { 'data-tooltip': 'Name of the equipment.' } } },
//						}
////					text: {
////					text: { type: 'text', group: 'data', label: 'Name', index: 1 , attrs: { 'label': { 'data-tooltip': 'Name of the equipment.' }}}
////					}
//					},
//					subType: { type: 'text', group: 'data', index: 2, label: 'Type', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Type of the equipment.' } } },
//					Supervisor: {type: 'text', group: 'specific', index: 3, label: 'Supervisor', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'Name of the Supervisor of the Card.' } }},
////					id: { type: 'text', group: 'data', index: 3, label: 'ID', attrs: { 'input': {'disabled' : 'disabled'},'label': { 'data-tooltip': 'ID of the equipment.' } } },
//					attributes: {type: 'text' , group: 'attributes' ,index: 4, label: 'Attributes' , attrs: {'label' : {'data-tooltip': 'www.' } } }, 
//				},			
			'gcc0-tp':{
				inputs: {
					directionality: {type: 'select' , option:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
			        application: {type: 'text' ,index: 2 , label: "Application", attrs: {'label' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}}
				},
				label: {'label':{'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}
				}
			},
			'gcc12-tp':{
				inputs:{
					directionality: {type: 'select' , option:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
					codirectional: {type: 'toggle' ,index: 2 , label: "Codirectional", attrs: { 'label': {'data-tooltip': 'This attribute specifies the directionality of the GCC12_TP with respect to the associated ODUk_CTP.	The value of TRUE means that the sink part of the GCC12_TP terminates the same signal direction as the sink part of the ODUk_CTP. The Source part behaves similarly. This attribute is meaningful only on objects instantiated under ODUk_CTP, and at least	one among ODUk_CTP and the subordinate object has Directionality equal to Bidirectional. This attribute is read-only.'}}},
					gccaccess: {type: 'select' , option:['gg1','gcc2','gcc1-plus-gcc2'],index: 3 , label: "GCC Access", attrs: { 'label': {'data-tooltip': 'This attribute indicates the GCC access represented	by this entity. Valid values are: 1) GCC1 2) GCC2 3) GCC1 + GCC2.This attribute is read-only.'}}},
					gccpassthrough : {type: 'toggle' ,index: 4 , label: "GCC Pass Through", attrs: { 'label': {'data-tooltip': 'This attribute controls the selected GCC overhead<br> whether it is passed through or modified. Valid<br> 	values are TRUE and FALSE. The value of TRUE means<br> that the GCC overhead shall pass through unmodified<br> 	from the ODUk CTP input to the ODUk CTP output.<br> 	Otherwise shall be set to all 0s at the ODUk CTP<br> output after the extraction of the COMMS data. This<br> attribute is not meaningful on objects instantiated<br> under ODUk_TTP, and on objects with Directionality<br> equals to Source.'}}},
					application: {type: 'text' ,index: 5 , label: "Application", attrs: {'label' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}}
				},
				label: {'label':{'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC1 or GCC2 channels.'}}
			},
			'och-client-ctp':{
				inputs:{
					adaptativetype: {type: 'number' ,min: '5', max: '15' ,index: 1 , label: "Adaptative Type", attrs: {'label' : {'data-tooltip': 'This attribute indicates the type of client signal<br> currently supported by the OCh adaptation function.<br> Valid values are integers between 1 and 15, representing:<br>1) CBR_2G5;<br>2) CBR_10G;<br>3) CBR_40G;<br>4) RSn.<br>This attribute is read-only.'}}},
			        sinkadaptactive: {type: 'number' ,min: '5', max: '15' ,index: 2 , label: "Adaptative Type", attrs: {'label' : {'data-tooltip': 'This attribute indicates the type of client signal<br> currently supported by the OCh adaptation function.<br> Valid values are integers between 1 and 15, representing:<br>1) CBR_2G5;<br>2) CBR_10G;<br>3) CBR_40G;<br>4) RSn.<br>This attribute is read-only.'}}},
                    sourceadaptactive:{type: 'toggle',index: 3 , label: "Source Adapt Active", attrs: {'label' : {'data-tooltip': 'This attribute allows for activation or deactivation<br> the source adaptation function. The value of TRUE<br> means activate. This attribute is read-write.'}}},
                    payloadtypeac:{type: 'number',min: '0', max: '2147483647' ,index: 4 , label: "Payload Type AC", attrs: {'label' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}}},
                    directionality: {type: 'select' , option:['sink','source','bidirectional'],index: 5 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
                    operationalstate: {type: 'select' , option:['enabled','disabled'],index: 6 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
                    currentproblemlist:{type: 'number',min: '0', max: '2147483647' ,index: 7 , label: "Current Problem List", attrs: {'label' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}},

				},
				label:{'label':{'data-tooltip': 'This entity represents the OCh to client adaptation function, which performs the adaptation between the OCh layer adapted information and the client layer characteristic information. The OCh layer is the server layer. This entity can be inherited for defining the client layer CTP.'}}	
				}
			},
			'och-ctp':{
				inputs:{
					directionality: {type: 'select' , option:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
				},
				label:{'label':{'data-tooltip': 'This entity repreents the functions of terminating and/or originating a link connection in the OCh layer network. The combining of the unidirectional sink and source function forms a bidirectional function.'}
				}
			},
			'och-nim':{
				inputs:{
					 operationalstate: {type: 'select' , option:['enabled','disabled'],index: 1 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
	                  currentproblemlist:{type: 'number',min: '0', max: '2147483647' ,index: 2 , label: "Current Problem List", attrs: {'label' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}}},
				},
				label:{
					'label':{'data-tooltip': 'This entity represents the OCh non-intrusive monitoring function at the OCh_CTP. This function can be activated and deactivated at the OCh_CTP.'}
				}
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
						
		}
};


//var gcc0tp{
//		

			
//		}	
//};