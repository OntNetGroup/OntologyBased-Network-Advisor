var CommonInspectorGroups = {
		'gcc0':{
			gcc0: {label:'GCC0-tp-grouping' , index: 1 ,label:{'label': {'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}} },
		},
		'gcc12tp':{
			gcc12tp: {label:'GCC12-tp-grouping' , index: 3 ,label:{'label': {'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}} },
			 
		},
		data: { label: 'General properties:', index: 2 , attrs: { 'data': { 'data-tooltip': 'General properties of the equipment.' } }},
//		specific: { label: 'Specific properties:', index: 2 , attrs: { 'data': { 'data-tooltip': 'Specific properties of the equipment.' } }},
//        attributes: { label: 'Attributes:', index: 3 , attrs: { 'data': { 'data-tooltip': 'Attributes of the card.' } }},
        gcc12tp: {label:'GCC12-tp-grouping' , index: 3 ,label:{'label': {'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}} },
        //    equipment: { label: 'Equipment', index: 2 },	
//    presentation: { label: 'Presentation', index: 3 },
//    geometry: { label: 'Geometry', index: 4 },
//    data: { label: 'Data', index: 5 }
};

var InspectorDefs = {
		'Card':{
				groups: CommonInspectorGroups,	
				
				inputs: {
					directionality: {type: 'select' , options:['sink','source','bidirectional'],group: 'gcc0',index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
			        application: {type: 'text' ,group: 'gcc0',index: 2 , label: "Application", attrs: {'label' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}},
               		codirectional: {type: 'toggle',group: 'gcc12tp',index: 2 , label: "Codirectional", attrs: { 'label': {'data-tooltip': 'This attribute specifies the directionality of the GCC12_TP with respect to the associated ODUk_CTP.	The value of TRUE means that the sink part of the GCC12_TP terminates the same signal direction as the sink part of the ODUk_CTP. The Source part behaves similarly. This attribute is meaningful only on objects instantiated under ODUk_CTP, and at least	one among ODUk_CTP and the subordinate object has Directionality equal to Bidirectional. This attribute is read-only.'}}},
					gccaccess: {type: 'select' , options:['gg1','gcc2','gcc1-plus-gcc2'],group: 'gcc12tp',index: 3 , label: "GCC Access", attrs: { 'label': {'data-tooltip': 'This attribute indicates the GCC access represented	by this entity. Valid values are: 1) GCC1 2) GCC2 3) GCC1 + GCC2.This attribute is read-only.'}}},
					gccpassthrough : {type: 'toggle' ,group: 'gcc12tp',index: 4 , label: "GCC Pass Through", attrs: { 'label': {'data-tooltip': 'This attribute controls the selected GCC overhead<br> whether it is passed through or modified. Valid<br> 	values are TRUE and FALSE. The value of TRUE means<br> that the GCC overhead shall pass through unmodified<br> 	from the ODUk CTP input to the ODUk CTP output.<br> 	Otherwise shall be set to all 0s at the ODUk CTP<br> output after the extraction of the COMMS data. This<br> attribute is not meaningful on objects instantiated<br> under ODUk_TTP, and on objects with Directionality<br> equals to Source.'}}},

					k:{type:'range',group: 'gcc12tp',min:'1',max:'3',step:'1',index:1,label:'k',attrs:{'label': {'data-tooltip' : 'This attribute specifies the index k that is used<br> to represent a supported bit rate and the different<br> versions of OPUk, ODUk and OTUk. Valid values for<br> this attribute are integers 1, 2 and 3.<br>k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>k = 3 represents an approximate bit rate of 40 Gbit/s.<br>This attribute is read-only.'}}},	
					   operationalstate: {type: 'select' ,group: 'gcc12tp', options:['enabled','disabled'],index: 2 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
		               exdapi:{type:'text',group: 'gcc12tp',index:3,label:'EX DAPI',attrs:{'input':{'data-tooltip':'The Expected Source Access Point Identifier<br> (ExSAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br> position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write.'}}},
			        	acti:{type:'text',index:4,group: 'gcc12tp',label:'AC TI',attrs:{'input':{'data-tooltip':'The Trail Trace Identifier (TTI) information<br> recovered (Accepted) from the TTI overhead position<br> at the sink of a trail. This attribute is read-only.'}}}, 
			        	 timdetmode:{type:'select',group: 'gcc12tp',options:['off','dapi','sapi','both'],index:5,label:'Operational State',attrs:{'label':{'data-tooltip' : 'This attribute indicates the mode of the Trace<br> Identifier Mismatch (TIM) Detection function. Valid<br> values are: off, dapi, sapi, both. This attribute is<br> read-write.'}}},
			        	 timactdisabled:{type:'toggle',group: 'gcc12tp',index:8,label:'TIM ACT Disabled',attrs:{'label':{'data-tooltip':'This attribute provides the control capability for the<br> managing system to enable or disable the Consequent<br> Action function when detecting Trace Identifier Mismatch<br> (TIM) at the trail termination sink. The value of TRUE<br> means disabled. This attribute is read-write.'}}},

					
				},
				label: {'label':{'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}
				},
				groups: 'data',

				
				
//			inputs: {
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
					directionality: {type: 'select' , option:['sink','source','bidirectional'],group: 'gcc0',index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
			        application: {type: 'text' ,group: 'gcc0',index: 2 , label: "Application", attrs: {'label' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}}
				},
				label: {'label':{'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC0 channels.'}
				},
				groups: 'gcc0-tp',
			},
			'gcc12-tp':{
				inputs:{
					directionality: {type: 'select' , option:['sink','source','bidirectional'],group: 'gcc12tp',index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
					codirectional: {type: 'toggle',group: 'gcc12tp',index: 2 , label: "Codirectional", attrs: { 'label': {'data-tooltip': 'This attribute specifies the directionality of the GCC12_TP with respect to the associated ODUk_CTP.	The value of TRUE means that the sink part of the GCC12_TP terminates the same signal direction as the sink part of the ODUk_CTP. The Source part behaves similarly. This attribute is meaningful only on objects instantiated under ODUk_CTP, and at least	one among ODUk_CTP and the subordinate object has Directionality equal to Bidirectional. This attribute is read-only.'}}},
					gccaccess: {type: 'select' , option:['gg1','gcc2','gcc1-plus-gcc2'],group: 'gcc12tp',index: 3 , label: "GCC Access", attrs: { 'label': {'data-tooltip': 'This attribute indicates the GCC access represented	by this entity. Valid values are: 1) GCC1 2) GCC2 3) GCC1 + GCC2.This attribute is read-only.'}}},
					gccpassthrough : {type: 'toggle' ,group: 'gcc12tp',index: 4 , label: "GCC Pass Through", attrs: { 'label': {'data-tooltip': 'This attribute controls the selected GCC overhead<br> whether it is passed through or modified. Valid<br> 	values are TRUE and FALSE. The value of TRUE means<br> that the GCC overhead shall pass through unmodified<br> 	from the ODUk CTP input to the ODUk CTP output.<br> 	Otherwise shall be set to all 0s at the ODUk CTP<br> output after the extraction of the COMMS data. This<br> attribute is not meaningful on objects instantiated<br> under ODUk_TTP, and on objects with Directionality<br> equals to Source.'}}},
					application: {type: 'text',group: 'gcc12tp',index: 5 , label: "Application", attrs: {'label' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}}
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
					directionality: {type: 'select' , options:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
				},
				label:{'label':{'data-tooltip': 'This entity repreents the functions of terminating and/or originating a link connection in the OCh layer network. The combining of the unidirectional sink and source function forms a bidirectional function.'}
				}
			},
			'och-nim':{
				inputs:{
					 operationalstate: {type: 'select' , options:['enabled','disabled'],index: 1 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
	                  currentproblemlist:{type: 'number', min: '0', max: '2147483647' ,index: 2 , label: "Current Problem List", attrs: {'label' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}}},
				},
				label:{
					'label':{'data-tooltip': 'This entity represents the OCh non-intrusive monitoring function at the OCh_CTP. This function can be activated and deactivated at the OCh_CTP.'}
				}
			},
			'ochr-ctp':{
				inputs:{
					directionality: {type: 'select' , options:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
				},
				label:{
					'label':{'data-tooltip': 'This entity represents the functions of terminating and/or originating a link connection in the OCh layer network. This entity supports a reduced functionality, i.e. does not support OCh overhead. The combining of the unidirectional sink and source function forms a bidirectional function.'}
				}
				
			},
			'ochr-ttp':{
				inputs:{
					directionality: {type: 'select' , options:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
					operationalstate: {type: 'select' , options:['enabled','disabled'],index: 2 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
			        adiministrativestate: {type: 'select', options:['unlocked','locked','shutting-down'],index:3,label:'Administrative State', attrs: {'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> administrativeState in ITU-T Rec. M.3100.<br>Possible Values − Unlocked, Locked, and Shutting<br> Down. See ITU-T Recs. X.731 and M.3100.<br>Default Value − Unlocked (If there is a need that<br> can be identified for locking this resource, this<br> decision will need to be re-evaluated).<br>Constraints to Provisioning − Constrained to<br> Unlocked (If there is a need that can be identified<br> for locking this resource, this decision will need<br> to be re-evaluated).<br>Effect of Change in Value − N/A (If there is a need<br> that can be identified for locking this resource,<br> this decision will need to be re-evaluated).<br>This attribute is read-write.'}}},
					currentproblemlist:{type: 'number', min: '1', max: '63' ,index: 4 , label: "Current Problem List", attrs: {'label' : {'data-tooltip': 'This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 63, representing:<br>1) no defect;<br>2) LOS-P (Loss of Signal − Payload);<br>3) OCI (Open Connection Indicator);<br>4) SSF-P (Server Signal failure − Payload);<br>5) SSF-O (Server Signal failure − Overhead);<br>6) SSF (Server Signal failure).<br>This attribute is read-only.'}}}
				},
				label:{
					
				}
			},
			'och-ttp':{
	            inputs:{
	            	directionality: {type: 'select' , options:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
	            	operationalstate: {type: 'select' , options:['enabled','disabled'],index: 2 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
	            	adiministrativestate: {type: 'select', options:['unlocked','locked','shutting-down'],index:3,label:'Administrative State', attrs: {'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> administrativeState in ITU-T Rec. M.3100.<br>Possible Values − Unlocked, Locked, and Shutting<br> Down. See ITU-T Recs. X.731 and M.3100.<br>Default Value − Unlocked (If there is a need that<br> can be identified for locking this resource, this<br> decision will need to be re-evaluated).<br>Constraints to Provisioning − Constrained to<br> Unlocked (If there is a need that can be identified<br> for locking this resource, this decision will need<br> to be re-evaluated).<br>Effect of Change in Value − N/A (If there is a need<br> that can be identified for locking this resource,<br> this decision will need to be re-evaluated).<br>This attribute is read-write.'}}},	
	            	currentproblemlist:{type: 'number', min: '1', max: '63' ,index: 4 , label: "Current Problem List", attrs: {'label' : {'data-tooltip': 'This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 63, representing:<br>1) no defect;<br>2) LOS-P (Loss of Signal − Payload);<br>3) OCI (Open Connection Indicator);<br>4) SSF-P (Server Signal failure − Payload);<br>5) SSF-O (Server Signal failure − Overhead);<br>6) SSF (Server Signal failure).<br>This attribute is read-only.'}}}
				},
				label:{
					
				}
			},
			'octk-nim':{
	         inputs:{
				k:{type:'range',min:'1',max:'3',step:'1',index:1,label:'k',attrs:{'label': {'data-tooltip' : 'This attribute specifies the index k that is used<br> to represent a supported bit rate and the different<br> versions of OPUk, ODUk and OTUk. Valid values for<br> this attribute are integers 1, 2 and 3.<br>k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>k = 3 represents an approximate bit rate of 40 Gbit/s.<br>This attribute is read-only.'}}},	
			   operationalstate: {type: 'select' , options:['enabled','disabled'],index: 2 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
               exdapi:{type:'text',index:3,label:'EX DAPI',attrs:{'input':{'data-tooltip':'The Expected Source Access Point Identifier<br> (ExSAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br> position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write.'}}},
	        	acti:{type:'text',index:4,label:'AC TI',attrs:{'input':{'data-tooltip':'The Trail Trace Identifier (TTI) information<br> recovered (Accepted) from the TTI overhead position<br> at the sink of a trail. This attribute is read-only.'}}}, 
	        	 timdetmode:{type:'select',options:['off','dapi','sapi','both'],index:5,label:'Operational State',attrs:{'input':{'data-tooltip' : 'This attribute indicates the mode of the Trace<br> Identifier Mismatch (TIM) Detection function. Valid<br> values are: off, dapi, sapi, both. This attribute is<br> read-write.'}}},
	        	 timactdisabled:{type:'togle',index:6,label:'TIM ACT Disabled',attrs:{'input':{'data-tooltip':'This attribute provides the control capability for the<br> managing system to enable or disable the Consequent<br> Action function when detecting Trace Identifier Mismatch<br> (TIM) at the trail termination sink. The value of TRUE<br> means disabled. This attribute is read-write.'}}},
	        	 degthr:{type: 'number',min: 0,max: 2147483647,label: "DEG THR",	attrs:{"input":{'data-tooltip': 'This attribute indicates the threshold level for<br> declaring a performance monitoring (PM) Second to be<br> bad. A PM Second is declared bad if the percentage<br> of detected errored blocks in that second is greater<br> than or equal to the specified threshold level.<br> Valid values are integers in units of percentages.<br> This attribute is read-write.'}}},	
	        	 degm:{type: "number",min: 0,max: 2147483647,label: "DEG M","attrs":{"input":{"data-tooltip": "This attribute indicates the threshold level for<br> declaring a Degraded Signal defect (dDEG). A dDEG<br> shall be declared if DegM consecutive bad PM Seconds<br> are detected. This attribute is read-write."}}},
	        	 currentproblemlist:
	     		{
	     			type: "number",
	     			min: 0,
	     			max: 2147483647,
	     			label: "Current Problem List",

	     			attrs:
	     			{
	     				"input":
	     				{
	     					"data-tooltip": "This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include:<br>1) no defect;<br>(other values TBD).<br>This attribute is read-only."}}	},
	         },
	        	 label:{
					
				}
			},
			'oduk-client-ctp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'oduk-ctp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'oduk-nim':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'odukt-nim':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'odukt-nim':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'oduk-ttp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'odukt-ttp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'omsn-ctp':{
	inputs:{
					
				},
				label:{
					
				}
			}, 'omnsnp':{
	inputs:{
					
				},
				label:{
					
				}
			}, 
			'omsn-ttp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'opsn-ttp':{
	inputs:{
					
				},
				label:{
					
				}
			}, 
			'opsn-ttp':{
	inputs:{
					
				},
				label:{
					
				}
			}, 
			'otmn-entity':{
	inputs:{
					
				},
				label:{
					
				}
			}, 
			'otsn-ttp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'otuk-ctp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			'otuk-ttp':{
	inputs:{
					
				},
				label:{
					
				}
			},
			groups: CommonInspectorGroups,			
		}
};


//var gcc0tp{
//		

			
//		}	
//};