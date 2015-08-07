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
					directionality: {type: 'select' , options:['sink','source','bidirectional'],group: 'gcc0' , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
			        application: {type: 'text' ,group: 'gcc0' , label: "Application", attrs: {'input' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}},
               		codirectional: {type: 'toggle',group: 'gcc12tp', label: "Codirectional", attrs: { 'label': {'data-tooltip': 'This attribute specifies the directionality of the GCC12_TP with respect to the associated ODUk_CTP.	The value of TRUE means that the sink part of the GCC12_TP terminates the same signal direction as the sink part of the ODUk_CTP. The Source part behaves similarly. This attribute is meaningful only on objects instantiated under ODUk_CTP, and at least	one among ODUk_CTP and the subordinate object has Directionality equal to Bidirectional. This attribute is read-only.'}}},
					gccaccess: {type: 'select' , options:['gg1','gcc2','gcc1-plus-gcc2'],group: 'gcc12tp', label: "GCC Access", attrs: { 'label': {'data-tooltip': 'This attribute indicates the GCC access represented	by this entity. Valid values are: 1) GCC1 2) GCC2 3) GCC1 + GCC2.This attribute is read-only.'}}},
					gccpassthrough : {type: 'toggle' ,group: 'gcc12tp' , label: "GCC Pass Through", attrs: { 'label': {'data-tooltip': 'This attribute controls the selected GCC overhead<br> whether it is passed through or modified. Valid<br> 	values are TRUE and FALSE. The value of TRUE means<br> that the GCC overhead shall pass through unmodified<br> 	from the ODUk CTP input to the ODUk CTP output.<br> 	Otherwise shall be set to all 0s at the ODUk CTP<br> output after the extraction of the COMMS data. This<br> attribute is not meaningful on objects instantiated<br> under ODUk_TTP, and on objects with Directionality<br> equals to Source.'}}},

					k:{type:'range',group: 'gcc12tp',min:'1',max:'3',step:'1',label:'k',attrs:{'label': {'data-tooltip' : 'This attribute specifies the index k that is used<br> to represent a supported bit rate and the different<br> versions of OPUk, ODUk and OTUk. Valid values for<br> this attribute are integers 1, 2 and 3.<br>k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>k = 3 represents an approximate bit rate of 40 Gbit/s.<br>This attribute is read-only.'}}},	
					   operationalstate: {type: 'select' ,group: 'gcc12tp', options:['enabled','disabled'] , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
		               exdapi:{type:'text',group: 'gcc12tp',label:'EX DAPI',attrs:{'input':{'data-tooltip':'The Expected Source Access Point Identifier<br> (ExSAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br> position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write.'}}},
			        	acti:{type:'text',group: 'gcc12tp',label:'AC TI',attrs:{'input':{'data-tooltip':'The Trail Trace Identifier (TTI) information<br> recovered (Accepted) from the TTI overhead position<br> at the sink of a trail. This attribute is read-only.'}}}, 
			        	 timdetmode:{type:'select',group: 'gcc12tp',options:['off','dapi','sapi','both'],label:'Operational State',attrs:{'label':{'data-tooltip' : 'This attribute indicates the mode of the Trace<br> Identifier Mismatch (TIM) Detection function. Valid<br> values are: off, dapi, sapi, both. This attribute is<br> read-write.'}}},
			        	 timactdisabled:{type:'toggle',group: 'gcc12tp',label:'TIM ACT Disabled',attrs:{'label':{'data-tooltip':'This attribute provides the control capability for the<br> managing system to enable or disable the Consequent<br> Action function when detecting Trace Identifier Mismatch<br> (TIM) at the trail termination sink. The value of TRUE<br> means disabled. This attribute is read-write.'}}},
			        		sourceadaptactive:
			        		{
			        			type: "toggle",
			        			label: "Source Adapt Active",
			        			
			        			attrs:
			        			{
			        				'input':
			        				{
			        					"data-tooltip": 
			        						"This attribute allows for activation or deactivation<br> the source adaptation function. The value of TRUE<br> means activate. This attribute is read-write."
			        				}
			        			}			
			        		},
					
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
//					attributes: {type: 'text' , group: 'attributes' ,index: 4, label: 'Attributes' , attrs: {'input' : {'data-tooltip': 'www.' } } }, 
//				},			
			'gcc0-tp':{
				inputs: {
					directionality: {type: 'select' , option:['sink','source','bidirectional'],group: 'gcc0',index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
			        application: {type: 'text' ,group: 'gcc0',index: 2 , label: "Application", attrs: {'input' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}}
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
					application: {type: 'text',group: 'gcc12tp',index: 5 , label: "Application", attrs: {'input' : {'data-tooltip': 'This attribute indicates the applications transported by the GCC channel. Example applications	are ECC, (user data channel). Valid values are string.This attribute is read-only.'}}}
				},
				label: {'label':{'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC1 or GCC2 channels.'}}
			},
			'och-client-ctp':{
				inputs:{
					adaptativetype: {type: 'number' ,min: '5', max: '15' ,index: 1 , label: "Adaptative Type", attrs: {'input' : {'data-tooltip': 'This attribute indicates the type of client signal<br> currently supported by the OCh adaptation function.<br> Valid values are integers between 1 and 15, representing:<br>1) CBR_2G5;<br>2) CBR_10G;<br>3) CBR_40G;<br>4) RSn.<br>This attribute is read-only.'}}},
			        sinkadaptactive: {type: 'number' ,min: '5', max: '15' ,index: 2 , label: "Adaptative Type", attrs: {'input' : {'data-tooltip': 'This attribute indicates the type of client signal<br> currently supported by the OCh adaptation function.<br> Valid values are integers between 1 and 15, representing:<br>1) CBR_2G5;<br>2) CBR_10G;<br>3) CBR_40G;<br>4) RSn.<br>This attribute is read-only.'}}},
                    sourceadaptactive:{type: 'toggle',index: 3 , label: "Source Adapt Active", attrs: {'input' : {'data-tooltip': 'This attribute allows for activation or deactivation<br> the source adaptation function. The value of TRUE<br> means activate. This attribute is read-write.'}}},
                    payloadtypeac:{type: 'number',min: '0', max: '2147483647' ,index: 4 , label: "Payload Type AC", attrs: {'input' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}}},
                    directionality: {type: 'select' , option:['sink','source','bidirectional'],index: 5 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
                    operationalstate: {type: 'select' , option:['enabled','disabled'],index: 6 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
                    currentproblemlist:{type: 'number',min: '0', max: '2147483647' ,index: 7 , label: "Current Problem List", attrs: {'input' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}},

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
	                  currentproblemlist:{type: 'number', min: '0', max: '2147483647' ,index: 2 , label: "Current Problem List", attrs: {'input' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}}},
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
					currentproblemlist:{type: 'number', min: '1', max: '63' ,index: 4 , label: "Current Problem List", attrs: {'input' : {'data-tooltip': 'This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 63, representing:<br>1) no defect;<br>2) LOS-P (Loss of Signal − Payload);<br>3) OCI (Open Connection Indicator);<br>4) SSF-P (Server Signal failure − Payload);<br>5) SSF-O (Server Signal failure − Overhead);<br>6) SSF (Server Signal failure).<br>This attribute is read-only.'}}}
				},
				label:{
					
				}
			},
			'och-ttp':{
	            inputs:{
	            	directionality: {type: 'select' , options:['sink','source','bidirectional'],index: 1 , label: "Directionality", attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}},
	            	operationalstate: {type: 'select' , options:['enabled','disabled'],index: 2 , label: "Operational State", attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},
	            	adiministrativestate: {type: 'select', options:['unlocked','locked','shutting-down'],index:3,label:'Administrative State', attrs: {'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> administrativeState in ITU-T Rec. M.3100.<br>Possible Values − Unlocked, Locked, and Shutting<br> Down. See ITU-T Recs. X.731 and M.3100.<br>Default Value − Unlocked (If there is a need that<br> can be identified for locking this resource, this<br> decision will need to be re-evaluated).<br>Constraints to Provisioning − Constrained to<br> Unlocked (If there is a need that can be identified<br> for locking this resource, this decision will need<br> to be re-evaluated).<br>Effect of Change in Value − N/A (If there is a need<br> that can be identified for locking this resource,<br> this decision will need to be re-evaluated).<br>This attribute is read-write.'}}},	
	            	currentproblemlist:{type: 'number', min: '1', max: '63' ,index: 4 , label: "Current Problem List", attrs: {'input' : {'data-tooltip': 'This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 63, representing:<br>1) no defect;<br>2) LOS-P (Loss of Signal − Payload);<br>3) OCI (Open Connection Indicator);<br>4) SSF-P (Server Signal failure − Payload);<br>5) SSF-O (Server Signal failure − Overhead);<br>6) SSF (Server Signal failure).<br>This attribute is read-only.'}}}
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
	        	 degm:{type: "number",min: 0,max: 2147483647,label: "DEG M",attrs:{"input":{"data-tooltip": "This attribute indicates the threshold level for<br> declaring a Degraded Signal defect (dDEG). A dDEG<br> shall be declared if DegM consecutive bad PM Seconds<br> are detected. This attribute is read-write."}}},
	        	 currentproblemlist:{type: "number",min: 0,max: 2147483647,label: "Current Problem List",attrs:{"input":{"data-tooltip": "This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include:<br>1) no defect;<br>(other values TBD).<br>This attribute is read-only."}}	},
	         },
	         label:{}
			},
			'oduk-client-ctp':{
	            inputs:{
	            	k:{type:'range',min:'1',max:'3',step:'1',label:'k',attrs:{'label': {'data-tooltip' : 'This attribute specifies the index k that is used<br> to represent a supported bit rate and the different<br> versions of OPUk, ODUk and OTUk. Valid values for<br> this attribute are integers 1, 2 and 3.<br>k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>k = 3 represents an approximate bit rate of 40 Gbit/s.<br>This attribute is read-only.'}}},	
	            	adaptationtype:{
	        			type: "number",
	        			min: 0,
	        			max: 2147483647,
	        			label: "Adaptation Type",
	        			
	        			attrs:
	        			{
	        				"input":
	        				{
	        					"data-tooltip": 
	        						"This attribute indicates the type of the supported<br> adaptation function at the interface port. Valid<br> values of this attribute include integers between 1 and 63, representing:<br>1) CBR;<br>2) ATMvp;<br>3) GFP;<br>4) NULL;<br>5) PRBS;<br>6) RSn.<br>This attribute is read-only."
	        				}
	        			}			
	        		},
	        		sinadaptactive:
	        		{
	        			type: "toggle",
	        			label: "Sink Adapt Active",
	        						
	        			attrs:
	        			{
	        				"input":
	        				{
	        					"data-tooltip": 
	        						"This attribute allows for activation or<br> deactivation the sink adaptation function. The value<br> of TRUE means active. This attribute is read-write."
	        				}
	        			}		
	        		},
	        		sourceadaptactive:
	        		{
	        			type: "toggle",
	        			label: "Source Adapt Active",
	        			
	        			attrs:
	        			{
	        				'input':
	        				{
	        					"data-tooltip": 
	        						"This attribute allows for activation or deactivation<br> the source adaptation function. The value of TRUE<br> means activate. This attribute is read-write."
	        				}
	        			}			
	        		},
	        		payloadtypeac:
	        		{
	        			type: "number",
	        			min: 0,
	        			max: 2147483647,
	        			label: "Payload Type AC",
	        			
	        			attrs:
	        			{
	        				"input":
	        				{
	        					"data-tooltip": 
	        						"This attribute indicates the actual payload type<br> signal received. This attribute is read-only."
	        				}
	        			}			
	        		},
	        		operationalstate:
	        		{
	        			type: "select",
	        			options: ["enabled","disabled"],
	        			label:"Operational State",
	        			
	        			attrs:
	        			{
	        				"input":
	        				{
	        					"data-tooltip": 
	        						"This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br>Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br>the object is created. If there is a period of time<br>during the initialization process where the<br>operational state is unknown, then the resource will<br> be considered disabled until initialization has<br>completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only."
	        				}
	        			}			
	        		},
	        							
	        		directionality:
	        		{
	        			type: "select",
	        			options: ["sink","source","bidirectional"],
	        			label: "Directionality",
	        			
	        			attrs:
	        			{
	        				"input":
	        				{
	        					"data-tooltip": 
	        						"This attribute indicates the directionality of the<br> termination point. Valid values are sink, source,<br> and bidirectional. This attribute is read-only."
	        				}
	        			}			
	        		},
	        				
	        		currentproblemlist:
	        		{
	        			type: "number",
	        			min: 1,
	        			max: 15,
	        			label: "Current Problem List",
	        						
	        			attrs:
	        			{
	        				"input":
	        				{
	        					"data-tooltip": 
	        						"This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 15, representing:<br>1) no defect;<br>2) PLM (Payload mismatch);<br>3) LOF (for RSn client);<br>4) LSS (Loss of PRBS Lock).<br>This attribute is read-only."
	        				}
	        			}			
	        		},
				},
				attrs:{'label':{'data-tooltip':'This entity represents the ODUkP to client adaptation function, which performs the adaptation between the ODUkP layer adapted information and the client layer characteristic information. The ODUkP layer is the server layer. This entity can be inherited for defining the client layer CTP.'}
					
				}
			},
			'oduk-ctp':{
	          inputs:{
				
	        	  operationalstate:
	      		{
	      			type: "select",
	      			option: ["enabled","disabled"],
	      			label: "Operational State",
	      			
	      			attrs:
	      			{
	      				"input":
	      				{
	      					"data-tooltip": 
	      						'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br> Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br>  be considered disabled until initialization has<br>  completed and the state updated accordingly.<br> Constraints to Provisioning – N/A.<br> Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br> This attribute is read-only.'
	      				}
	      			}			
	      		},
	      							
	      		directionality:
	      		{
	      			type: "select",
	      			options: ["sink","source","bidirectional"],
	      			label: "Directionality",
	      			
	      			attrs:
	      			{
	      				"input":
	      				{
	      					"data-tooltip": 
	      						"This attribute indicates the directionality of the<br> termination point. Valid values are sink, source,<br> and bidirectional. This attribute is read-only."
	      				}
	      			}			
	      		},
	      				
	      		currentproblemlist:
	      		{
	      			type: "number",
	      			min: 1,
	      			max: 15,
	      			label: "Current Problem List",
	      						
	      			attrs:
	      			{
	      				"input":
	      				{
	      					"data-tooltip": 
                            "This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 15, representing:<br>1) no defect;<br>2) PLM (Payload mismatch);<br>3) LOF (for RSn client);<br>4) LSS (Loss of PRBS Lock).<br>This attribute is read-only."
	      				}
	      			}			
	      		},
	        	  
				},
				label:{
					
				}
			},
			'oduk-nim':{
	           inputs:{
					
	        	   k:
	       		{		 
	       			type: "range",
	       			min: 1,
	       			max: 3,
	       			step: 1,
	       			label: "k",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute specifies the index k that is used<br> to represent a supported bit rate and the different<br> versions of OPUk, ODUk and OTUk. Valid values for<br> this attribute are integers 1, 2 and 3.<br>k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>k = 3 represents an approximate bit rate of 40 Gbit/s.<br>This attribute is read-only." 
	       				}
	       			}			
	       		},		
	       		
	       		operationalstate:
	       		{
	       			type: "select",
	       			options: ["enabled","disabled"],
	       			label: "Operational State",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br>be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only." 
	       				}
	       			}			
	       		},
	       						
	       		exdapi:
	       		{
	       			type: "text",
	       			label: "EX DAPI",
	       						
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"The Expected Destination Access Point Identifier<br> (ExDAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br> position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write." 
	       				}
	       			}			
	       		},		
	       				
	       		exsapi:
	       		{
	       			type: "text",
	       			label: "EX SAPI",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"The Expected Source Access Point Identifier<br> 	       						(ExSAPI), provisioned by the managing system, to be<br> 	       						compared with the TTI accepted at the overhead<br> 	      						position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> 	       						read-write." 
	       				}
	       			}			
	       		},
	       		
	       		acti:
	       		{
	       			type: "text",
	       			label: "AC TI",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"The Trail Trace Identifier (TTI) information<br> 	       						recovered (Accepted) from the TTI overhead position<br> 	       						at the sink of a trail. This attribute is read-only." 
	       				}
	       			}			
	       		},		

	       		timdetmode:
	       		{
	       			type: "select",
	       			options: ["off","dapi","sapi","both"],
	       			label: "TIM DET Mode",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute indicates the mode of the Trace<br> 	       						Identifier Mismatch (TIM) Detection function. Valid<br> 	       						values are: off, dapi, sapi, both. This attribute is<br> 	       						read-write." 
	       				}
	       			}			
	       		},
	       		
	       		timactdisabled:
	       		{
	       			type: "toggle",
	                   label: "TIM ACT Disabled",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute provides the control capability for the<br> 	       						managing system to enable or disable the Consequent<br> 	       						Action function when detecting Trace Identifier Mismatch<br> 	       						(TIM) at the trail termination sink. The value of TRUE<br> 	       						means disabled. This attribute is read-write."
	       				}
	       			}			
	       		},
	       				
	       		degthr:
	       		{
	       			type: "number",
	       			min: 0,
	       			max: 2147483647,
	       			label: "DEG THR",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute indicates the threshold level for<br> 	       						declaring a performance monitoring (PM) Second to be<br> 	       						bad. A PM Second is declared bad if the percentage<br> 	       						of detected errored blocks in that second is greater<br> 	       						than or equal to the specified threshold level.<br> 	       						Valid values are integers in units of percentages.<br> 	       						This attribute is read-write."
	       				}
	       			}			
	       		},
	       		
	       		degm:
	       		{
	       			type: "number",
	       			min: 0,
	       			max: 2147483647,
	       			label: "DEG M",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute indicates the threshold level for<br> 	       						declaring a Degraded Signal defect (dDEG). A dDEG<br> 	       						shall be declared if DegM consecutive bad PM Seconds<br> 	       						are detected. This attribute is read-write."
	       				}
	       			}			
	       		},

	       		currentproblemlist:
	       		{
	       			type: "number",
	       			min: 1,
	       			max: 127,
	       			label: "Current Problem List",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute indicates the failure conditions of<br> 	       						the entity. Possible values of this attribute include decimal numbers between 1 and 127, representing:<br>	       						1) no defect;<br>	       						2) OCI (Open Connection Indication);<br>	       						3) LCK (Locked);<br>	       						4) TIM (Trail Trace Identifier Mismatch);<br>	       						5) DEG (Signal Degraded);<br>	       						6) BDI (Backward Defect Indication);<br>	       						7) SSF (Server Signal Fail).<br>	       						This attribute is read-only."
	       				}
	       			}
	       		},
	       		
	       		nimdirectionality:
	       		{
	       			type: "select",
	       			options: ["sink","source"],
	       			label: "NIM Directionality",
	       			
	       			attrs:
	       			{
	       				"input":
	       				{
	       					"data-tooltip": 
	       						"This attribute indicates the directionality of the ODUk<br> Path non-intrusive monitoring function. Valid values are<br> sink and source. This attribute is significant for ODUk<br> Path unidirectional non-intrusive monitoring when the<br> associated ODUk_CTP is bidirectional. This attribute is<br> read-only."
	       				}
	       			}			
	       		},
	       	}

	        	   
				},
				label:{
					
				}
			},
			'odukt-nim':{
	inputs:{
		"tcm-field":
		{
			type: "range",
			min: 1,
			max: 6,
			step: 1,
			label: "TCM Field",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the tandem connection<br> monitoring field of the ODUk OH. Valid values are<br> integers from 1 to 6. This attribute is read-only."
				}
			}			
		},
		
		operationalstate:
		{
			type: "select",
			options: ["enabled","disabled"],
			label: "Operational State",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>	Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> 						operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>	Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only."
				}
			}			
		},
							
		exdapi:
		{
			type: "text",
			label: "EX DAPI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Expected Destination Access Point Identifier<br> (ExDAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br> position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write."
				}
			}			
		},		
				
		exsapi:
		{
			type: "text",
			label: "EX SAPI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Expected Source Access Point Identifier<br> (ExSAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br> position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write."
				}
			}			
		},
		
		acti:
		{
			type: "text",
			label: "AC TI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Trail Trace Identifier (TTI) information<br> recovered (Accepted) from the TTI overhead position<br> at the sink of a trail. This attribute is read-only."
				}
			}			
		},							
					
		timdetmode:
		{
			type: "select",
			options: ["off","dapi","sapi","both"],
			label: "TIM DET Mode",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the mode of the Trace<br> Identifier Mismatch (TIM) Detection function. Valid<br> values are: off, dapi, sapi, both. This attribute is<br> read-write."
				}
			}			
		},
		
		timactdisabled:
		{
			type: "toggle",
            label: "TIM ACT Disabled",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": "This attribute provides the control capability for the<br> managing system to enable or disable the Consequent<br> Action function when detecting Trace Identifier Mismatch<br> 						(TIM) at the trail termination sink. The value of TRUE<br> 						means disabled. This attribute is read-write."
				}
			}			
		},
				
		degthr:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "DEG THR",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the threshold level for<br> 						declaring a performance monitoring (PM) Second to be<br> 						bad. A PM Second is declared bad if the percentage<br> 						of detected errored blocks in that second is greater<br> 						than or equal to the specified threshold level.<br> 						Valid values are integers in units of percentages.<br> 						This attribute is read-write."
				}
			}			
		},
		
		degm:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "DEG M",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the threshold level for<br> 						declaring a Degraded Signal defect (dDEG). A dDEG<br> 						shall be declared if DegM consecutive bad PM Seconds<br> 						are detected. This attribute is read-write."
				}
			}			
		},

		nimdirectionality:
		{
			type: "select",
			options: ["sink","source"],
			label: "NIM Directionality",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the directionality of the ODUk<br> 						Path non-intrusive monitoring function. Valid values are<br> 						sink and source. This attribute is significant for ODUk<br> 						Path unidirectional non-intrusive monitoring when the<br> 						associated ODUk_CTP is bidirectional. This attribute is<br> 						read-only."
				}
			}			
		},
		
		currentproblemlist:
		{
			type: "number",
			min: 1,
			max: 127,
			label: "Current Problem List",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the failure conditions of<br> 						the entity. Possible values of this attribute include decimal numbers between 1 and 127, representing:<br>						1) no defect;<br>						2) OCI (Open Connection Indication);<br>						3) LCK (Locked);<br>						4) TIM (Trail Trace Identifier Mismatch);<br>						5) DEG (Signal Degraded);<br>						6) BDI (Backward Defect Indication);<br>						7) SSF (Server Signal Fail).<br>						This attribute is read-only."
				}
			}			
		},
	
				},
				label:{
					
				}
			},
			
			'oduk-ttp':{
	inputs:{
				
		k:
		{		 
			type: "range",
			min: 1,
			max: 3,
			step: 1,
			label: "k",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute specifies the index k that is used<br> 						to represent a supported bit rate and the different<br> 						versions of OPUk, ODUk and OTUk. Valid values for<br> 						this attribute are integers 1, 2 and 3.<br>						k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>						k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>						k = 3 represents an approximate bit rate of 40 Gbit/s.<br>						This attribute is read-only." 
				}
			}			
		},		
		
		operationalstate:
		{
			type: "select",
			options: ["enabled","disabled"],
			label: "Operational State",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute is generally defined in ITU-T Rec.<br> 						X.731 and the behaviour description for<br> 						operationalState in ITU-T Rec. M.3100.<br>						Possible Values – Enabled and Disabled. See ITU-T<br> 						Recs. X.731 and M.3100 for details.<br>						Default Value – Actual state of resource at the time<br> 						the object is created. If there is a period of time<br> 						during the initialization process where the<br> 						operational state is unknown, then the resource will<br> 						be considered disabled until initialization has<br> 						completed and the state updated accordingly.<br>						Constraints to Provisioning – N/A.<br>						Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>						This attribute is read-only."
				}
			}			
		},
							
		directionality:
		{
			type: "select",
			options: ["sink","source","bidirectional"],
			label: "Directionality",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the directionality of the<br> 						termination point. Valid values are sink, source,<br> 						and bidirectional. This attribute is read-only."
				}
			}			
		},
		
		txti:
		{
			type: "text",			
			label: "TX TI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Trail Trace Identifier (TTI) information,<br> 						provisioned by the managing system at the<br> 						termination source, to be placed in the TTI overhead<br> 						position of the source of a trail for transmission.<br> 						This attribute is read-write."
				}
			}			
		},
		
		exdapi:
		{
			type: "text",
			label: "EX DAPI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Expected Destination Access Point Identifier<br> 						(ExDAPI), provisioned by the managing system, to be<br> 						compared with the TTI accepted at the overhead<br> 						position of the sink for the purpose of checking the<br> 						integrity of connectivity. This attribute is<br> 						read-write."
				}
			}			
		},		
		
		exsapi:
		{
			type: "text",
			label: "EX SAPI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Expected Source Access Point Identifier<br> 						(ExSAPI), provisioned by the managing system, to be<br> 						compared with the TTI accepted at the overhead<br> 						position of the sink for the purpose of checking the<br> 						integrity of connectivity. This attribute is<br> 						read-write."
				}
			}			
		},
		
		acti:
		{
			type: "text",
			label: "AC TI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Trail Trace Identifier (TTI) information<br> 						recovered (Accepted) from the TTI overhead position<br> 						at the sink of a trail. This attribute is read-only."
				}
			}			
		},		
		
		timdetmode:
		{
			type: "select",
			options: ["off","dapi","sapi","both"],
			label: "TIM DET Mode",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the mode of the Trace<br> 						Identifier Mismatch (TIM) Detection function. Valid<br> 						values are: off, dapi, sapi, both. This attribute is<br> 						read-write."
				}
			}			
		},
		
		timactdisabled:
		{
			type: "toggle",
            label: "TIM ACT Disabled",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute provides the control capability for the<br> 						managing system to enable or disable the Consequent<br> 						Action function when detecting Trace Identifier Mismatch<br> 						(TIM) at the trail termination sink. The value of TRUE<br> 						means disabled. This attribute is read-write."
				}
			}			
		},
				
		degthr:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "DEG THR",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the threshold level for<br> 						declaring a performance monitoring (PM) Second to be<br> 						bad. A PM Second is declared bad if the percentage<br> 						of detected errored blocks in that second is greater<br> 						than or equal to the specified threshold level.<br> 						Valid values are integers in units of percentages.<br> 						This attribute is read-write."
				}
			}			
		},
		
		degm:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "DEG M",
						
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the threshold level for<br> 						declaring a Degraded Signal defect (dDEG). A dDEG<br> 						shall be declared if DegM consecutive bad PM Seconds<br> 						are detected. This attribute is read-write."
				}
			}			
		},
		
		positionseg:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "Position Seg",
									
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the positions of the TCM<br> 						and GCC processing functions within the ODUk TP.<br>						The order of the position in the positionSeq<br> 						attribute together with the signal flow determine<br> 						the processing sequence of the TCM and GCC functions<br> 						within the ODUk TP. Once the positions are<br> 						determined, the signal processing sequence will<br> 						follow the signal flow for each direction of the<br> 						signal.<br>						Within the ODUk_CTP, the position order is going<br> 						from adaptation to connection function. Within the<br> 						ODUk_TTP, the order is going from connection to<br> 						adaptation function.<br>						The syntax of the PositionSeq attribute will be a<br> 						SEQUENCE OF pointers, which point to the contained<br> 						TCM and GCC function.<br>						The order of TCM and GCC access function in the<br> 						positionSeq attribute is significant only when there<br> 						are more than one TCM functions within the ODUk TP<br> 						and also at least one of them have the<br> 						TimActDisabled attribute set to FALSE (i.e. AIS is<br> 						inserted upon TIM).<br>						If a GCC12_TP is contained in an ODUk_TTP and the<br> 						GCC12_TP is not listed in the PositionSeq attribute<br> 						of the ODUk_TTP, then the GCC access is at the AP<br> 						side of the ODUk TT function.<br>						This attribute is read-only."
				}
			}			
		},
		
		currentproblemlist:
		{
			type: "number",
			min: 1,
			max: 127,
			label: "Current Problem List",
									
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the failure conditions of<br> 						the entity. Possible values of this attribute include decimal numbers between 1 and 127, representing:<br>						1) no defect;<br>						2) OCI (Open Connection Indication);<br>						3) LCK (Locked);<br>						4) TIM (Trail Trace Identifier Mismatch);<br>						5) DEG (Signal Degraded);<br>						6) BDI (Backward Defect Indication);<br>						7) SSF (Server Signal Fail).<br>						This attribute is read-only."
				}
			}			
		},
		
		tcmfieldsinuse:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "TCM Fields In Use",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the used TCM fields of the<br> 						ODUk OH. Valid values of this attribute are integers between 1 and 63 This attribute is read-only."
				}
			}			
		},
		
				},
				label:{
					
				}
			},
		
			'omsn-ctp':{
	inputs:{
					
		directionality: 
		{ 
			type: "select",
			options: ["sink","source","bidirectional"],
			label: "Directionality",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the directionality of the<br>						termination point. Valid values are sink, source,<br> 						and bidirectional. This attribute is read-only."		
				}
			}			
		},		

		
				},
				label:{
					
					
					
				}
			}, 'omnsnp':{
	inputs:{
					
		ordertype: 
		{ 
			type: "select",
			defaultValue: "1-plus-1",
			options: ["1-plus-1"],
			label: "Oper Type",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the trail protection<br> 						schemes supported by the entity. Valid value for this<br> 						attribute is:<br>						1 + 1 unidirectional.<br> 						This attribute is read-write."		
				}
			}			
		},
		
		waittorestoretime:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "Wait to Restore Time",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"If the protection systems is revertive, this<br> 						attribute specifies the amount of time, in seconds,<br>						to wait after a fault clears before restoring<br> 						traffic to the protected protectionUnit that<br> 						initiated the switching. Valid values for this<br> 						attribute are integers. This attribute is optional.<br> 						This attribute is read-write. "		
				}
			}			
		},
		
		holdofftime:
		{
			type: "number",
			min: 0,
			max: 2147483647,
			label: "Hold Off Time",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the time, in seconds,<br> 						between declaration of signal degrade or signal<br> 						fail, and the initialization of the protection<br> 						switching algorithm. Valid values are integers in<br> 						units of seconds. This attribute is read-write."	
				}
			}			
		},
		
		currentproblemlist:
		{
			type: "number",
			min: 1,
			max: 15,
			label: "Current Problem List",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the failure conditions of<br> 						the entity. Possible values of this attribute include decimal numbers between 1 and 15, representing:<br>						1) no defect;<br>						2) SSF-P (Server Signal Fail – Payload);<br>						3) SSF-O (Server Signal Fail – Overhead);<br>						4) SSF (Server Signal Fail).<br>						This attribute is read-only."	
				}
			}	
		},

		
				},
				label:{
					
				}
			}, 
			'omsn-ttp':{
	inputs:{
					
		directionality: 
		{ 
			type: "select",
			options: ["sink","source","bidirectional"],
			label: "Directionality",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the directionality of the<br>						termination point. Valid values are sink, source,<br> 						and bidirectional. This attribute is read-only."	
				}
			}			
		},
		
		operationalstate:
		{
			type: "select",
			options: ["enabled","disabled"],
			label: "Operational State",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute is generally defined in ITU-T Rec. <br>						X.731 and the behaviour description for<br> 						operationalState in ITU-T Rec. M.3100.<br>						Possible Values – Enabled and Disabled. See ITU-T<br> 						Recs. X.731 and M.3100 for details.<br>						Default Value – Actual state of resource at the time<br> 						the object is created. If there is a period of time<br> 						during the initialization process where the<br> 						operational state is unknown, then the resource will <br>						be considered disabled until initialization has <br>						completed and the state updated accordingly.<br>						Constraints to Provisioning – N/A.<br>						Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>						This attribute is read-only."
				}
			}			
		},
		
		currentproblemlist:
		{
			type: "number",
			min: 1,
			max: 255,
			label: "Current Problem List",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the failure conditions of<br> 						the entity. Possible values of this attribute include decimal numbers between 1 and 255, representing:<br>						1) no defect;<br>						2) SSF-P (Server Signal Fail – Payload);<br>						3) SSF-O (Server Signal Fail – Overhead);<br>						4) SSF (Server Signal Fail);<br>						5) BDI-P (Backward Defect Indication – Payload);<br>						6) BDI-O (Backward Defect Indication – Overhead);<br>						7) BDI (Backward Defect Indication);<br>						8) LOS-P (Loss of Signal – Payload).<br>						This attribute is read-only."
				}
			}			
		},
		
				},
				label:{
					
				}
			},
			'opsn-ttp':{
	inputs:{
				
		directionality: 
		{ 
			type: "select",
			options: ["sink","source","bidirectional"],
			label: "Directionality",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the directionality of the<br> termination point. Valid values are sink, source,<br> 	and bidirectional. This attribute is read-only."	
				}
			}			
		},
		
		operationalstate:
		{
			type: "select",
			options: ["enabled","disabled"],
			label: "Operational State",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for <br>operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T <br>Recs. X.731 and M.3100 for details.<br>	Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will <br>be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only."
				}
			}			
		},
		
		currentproblemlist:
		{
			type: "number",
			min: 1,
			max: 3,
			label: "Current Problem List",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 3, representing:<br>1) no defect;<br>2) LOS (Loss of Signal).<br>This attribute is read-only."
				}
			}			
		},
		
				},
				label:{
					
				}
			}, 
			
			'otmn-entity':{
	     inputs:{
					
		order: 
		{ 
			type: "number",
			min: 0,
			max: 2147483647,
			label: "Order",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the order of the OTM,<br> which represents the maximum number of wavelengths <br>	that can be supported at the bit rate(s) supported<br> on the interface. See ITU-T Rec. G.709/Y.1331 for<br> details. This attribute is read-only."
				}
			}			
		},

		reduced: 
		{ 
			type: "toggle",			
			label: "Reduced",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates whether a reduced or full<br> functionality is supported at the interface. A value<br> of TRUE means reduced. A value of FALSE means full.<br> See ITU-T Rec.G.709/Y.1331 for details. This<br> attribute is read-only."
				}
			}			
		},		
		
		bitrate:
		{
			type: "select",			
			options: ["1", "2", "3", "12","123","23"],
			label: "Bit Rate",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute is an index used to represent the<br> bit rate or set of bit rates supported on the<br> interface. Valid values are 1, 2, 3, 12, 123 and 23.<br> In the index, each digit k represents an approximate<br>bit rate supported by the interface. k = 1 means 2.5<br> Gbit/s, k = 2 means 10 Gbit/s, and k = 3 means 40<br>Gbit/s. Default value of this attribute is system <br>	specific. This attribute is read-only."
				}
			}		
		},
		
		interfacetype:
		{
			type: "text",			
			label: "Interface Type",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute identifies the type of interface.<br> The value of this attribute will affect the<br> behaviour of the OTM with respect to<br> presence/absence of OOS processing and TCM<br> activation. For an IrDI interface, there is no OOS<br> processing and TCM activation is limited to n levels<br> as specified by a TCM level threshold.<br> Possible Values:<br> field 1: enumeration of IrDI or IaDI;<br>field 2: 10 character string for additional information.<br> Default Value:<br>field 1: IaDI;<br> field 2: vendor and/or provider specific.<br> Constraints to Provisioning – none identified.<br> Effect of Change in Value – change in behaviour in<br> accordance with value.<br> This attribute is read-only."
				}
			}			
		},
		
		tcmmax:
		{		
			type: "range",			
			min: 1,
			max: 6,
			step: 1,
			label: "TCM Max",
						
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute identifies the maximum number of TCM<br> levels allowed for any Optical Channel contained in<br> this OTM. A new TCM activation will be rejected if<br> the requested level is greater than the threshold.<br> If InterfaceType for the OTM is IaDI, then this<br> attribute is irrelevant.<br>Possible Values – integer from 0 to 6. n (IrDI),<br> where 0 < n < 7.<br> Default Value – Value will default to 3.<br>Constraints to Provisioning – cannot be modified to<br> new value if new value does not support the number <br>	of currently activated TCM levels for any contained<br> Optical Channel.<br>Effect of Change in Value – change in behaviour in <br>	accordance with value.<br>This attribute is read-write."
				}
			}			
		},
		
		opticalreach:
		{
			type: "select",			
			options: ["intra-office", "short-haul", "long-haul"],
			label: "Bit Rate",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the length the optical<br>signal may travel before requiring termination or <br>regeneration. Valid values are:<br>	1) intraOffice;<br>	2) shortHaul;<br>3) longHaul.<br>This attribute is read-only."
				}
			}			
		},

		
				},
				label:{
					
				}
			}, 
			'otsn-ttp':{
	inputs:{
				
		directionality:
		{	
			type: "select",
			options: ["sink","source","bidirectional"],
			label: "Directionality",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the directionality of the<br> termination point. Valid values are sink, source,<br> and bidirectional. This attribute is read-only."
				}
			}			
		},
		
		operationalstate:
		{
			type: "select",
			options: ["enabled","disabled"],
			label: "Operational State",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T <br>	Recs. X.731 and M.3100 for details.<br>	Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the <br>operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>	Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only."
				}
			}			
		},
		
		aprstatus:
		{
			type: "text",			
			label: "APR Status",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the status of the<br> Automatic Power Reduction (APR) function of the <br>entity. Valid values are on and off. This <br>	attribute is read-only."
				}
			}			
		},
		
		aprcntrl:
		{
			type: "text",			
			label: "APR CNTRL",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute provides for the control of the<br> Automatic Power Reduction (APR) function of the<br> entity. The specific APR procedures and trigger<br> criteria of APR is outside the scope of this <br> Recommendation. This attribute is optional. This <br> attribute is read-write."
				}
			}			
		},		
		
		txti:
		{
			type: "text",			
			label: "TX TI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Trail Trace Identifier (TTI) information,<br> provisioned by the managing system at the<br> termination source, to be placed in the TTI overhead<br> position of the source of a trail for transmission.<br> This attribute is read-write."
				}
			}			
		},
		
		exdapi:
		{
			type: "text",			
			label: "EX DAPI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Expected Destination Access Point Identifier<br>(ExDAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br> position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write."
				}
			}			
		},
		
		exsapi:
		{
			type: "text",			
			label: "EX SAPI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Expected Source Access Point Identifier<br> (ExSAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead<br>	position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write."
				}
			}			
		},
		
		acti:
		{
			type: "text",			
			label: "AC TI",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"The Trail Trace Identifier (TTI) information<br> recovered (Accepted) from the TTI overhead position<br> at the sink of a trail. This attribute is read-only."
				}
			}			
		},
		
		timdetmode:
		{
			type: "select",
			options: ["off", "dapi", "sapi","both"],
			label: "TIM DET Mode",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the mode of the Trace<br> Identifier Mismatch (TIM) Detection function. Valid<br> values are: off, dapi, sapi, both. This attribute is<br> read-write."
				}
			}			
		},
		
		timactdisabled:
		{
			type: "toggle",
			label: "TIM ACT Disabled",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute provides the control capability for the<br> managing system to enable or disable the Consequent <br>Action function when detecting Trace Identifier Mismatch<br> (TIM) at the trail termination sink. The value of TRUE<br> means disabled. This attribute is read-write."
				}
			}			
		},
		
		currentproblemlist:
		{
		
			type: "range",			
			min: 1,
			max: 255,
			step: 1,
			label: "Current Problem List",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the failure conditions of<br> the entity. Possible values of this attribute include decimal numbers between 1 and 255, representing:<br>1) no defect;<br>2) BDI-P (Backward Defect Indication – Payload);<br>3) BDI-O (Backward Defect Indication – Overhead);<br>4) BDI (Backward Defect Indication);<br>5) TIM (Trail Trace Identifier Mismatch);<br>6) LOS-P (Loss of Signal – Payload);<br>7) LOS-O (Loss of Signal – Overhead);<br>	8) LOS (Loss of Signal).<br>This attribute is read-only."
				}
			}			
		},
		
				},
				label:{
					
				}
			},
			'otuk-ctp':{
	inputs:{
				
		k:
		{		 
			type: "range",
			min: -1,
			max: 4,
			step: 1,
			label: "k",
						
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute specifies the index k that is used<br> to represent a supported bit rate and the different<br> versions of OPUk, ODUk and OTUk. Valid values for<br> this attribute are integers 1, 2 and 3.<br>	k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>	k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>	k = 3 represents an approximate bit rate of 40 Gbit/s.<br>This attribute is read-only."  
				}
			}			  
		},		
		
		"sink-adapt-active":
		{
			type: "toggle",
            label: "Sink Adapt Active",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute allows for activation or<br> deactivation the sink adaptation function. The value<br> of TRUE means active. This attribute is read-write." 
				}
			}			
		},
		
		"source-adapt-active":
		{
			type: "toggle",
            label: "Source Adapt Active",
		
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute allows for activion or deactivation<br> the source adaptation function. The value of TRUE<br> means activate. This attribute is read-write."
				}
			}			
		},
		
		"fec-enabled":
		{
			type: "toggle",
            label: "FEC Enabled",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"If Forward Error Correction (FEC) is supported,<br> this object indicates whether FEC at the OTUk sink <br> adaptation function is enabled or not. This <br> attribute is optional. Valid values are TRUE and<br> FALSE. TRUE means FEC is enabled. This attribute is<br> read-write."
				}
			}			
		},
									
		directionality:
		{
			type: "select",
			options: ["sink","source","bidirectional"],
			label: "Directionality",
			
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the directionality of the<br> termination point. Valid values are sink, source,<br> and bidirectional. This attribute is read-only."
				}
			}			
		},
		
		currentproblemlist:
		{
			type: "number",
			min: 1,
			max: 15,
			label: "Current Problem List",
						
			attrs:
			{
				"input":
				{
					"data-tooltip": 
						"This attribute indicates the failure conditions of <br>the entity. Possible values of this attribute include decimal numbers between 1 and 15, representing:<br> 1) no defect;<br>2) LOF (Loss of Frame);<br>	3) AIS (Alarm Indication Signal);<br> 4) LOM (Loss of MultiFrame).<br> This attribute is read-only."
				}
			}			
		},
		
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
			operationalstate:
			{
				type: "select",
				options: ["enabled","disabled"],
				label: "Operational State",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>	Possible Values – Enabled and Disabled. See ITU-T <br>	Recs. X.731 and M.3100 for details.<br>	Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will <br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br> Constraints to Provisioning – N/A.<br>	Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br> This attribute is read-only."
					}
				}			
			},
								
			directionality:
			{
				type: "select",
				options: ["sink","source","bidirectional"],
				label: "Directionality",
							
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute indicates the directionality of the<br>termination point. Valid values are sink, source,<br> and bidirectional. This attribute is read-only."
					}
				}			
			},
			
			txti:
			{
				type: "text",
				label: "TX TI",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"The Trail Trace Identifier (TTI) information,<br> provisioned by the managing system at the <br>	termination source, to be placed in the TTI overhead<br> position of the source of a trail for transmission.<br> This attribute is read-write."
					}
				}			
			},
			
			exdapi:
			{
				type: "text",
				label: "EX DAPI",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"The Expected Destination Access Point Identifier<br>(ExDAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead <br>	position of the sink for the purpose of checking the <br>integrity of connectivity. This attribute is<br> read-write."
					}
				}	
			},
			
			exsapi:
			{
				type: "text",
				label: "EX SAPI",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"The Expected Source Access Point Identifier <br>(ExSAPI), provisioned by the managing system, to be<br> compared with the TTI accepted at the overhead <br>position of the sink for the purpose of checking the<br> integrity of connectivity. This attribute is<br> read-write."
					}
				}			
			},
			
			acti:
			{
				type: "text",
				label: "AC TI",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"The Trail Trace Identifier (TTI) information <br>recovered (Accepted) from the TTI overhead position <br>at the sink of a trail. This attribute is read-only."
					}
				}			
			},
			
			timdetmode:
			{
				type: "select",
				options: ["off","dapi","sapi","both"],
				label: "TIM DET Mode",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute indicates the mode of the Trace<br> Identifier Mismatch (TIM) Detection function. Valid<br> values are: off, dapi, sapi, both. This attribute is <br>read-write."
					}
				}			
			},
			
			timactdisabled:
			{
				type: "toggle",
				label: "TIM ACT Disabled",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute provides the control capability for the<br> managing system to enable or disable the Consequent <br>Action function when detecting Trace Identifier Mismatch <br>(TIM) at the trail termination sink. The value of TRUE<br> means disabled. This attribute is read-write."
					}
				}			
			},
			
			degthr:
			{		
				type: "number",
				min: 0,
				max: 2147483647,
				label: "DEG THR",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute indicates the threshold level for <br>declaring a performance monitoring (PM) Second to be<br>bad. A PM Second is declared bad if the percentage<br> of detected errored blocks in that second is greater<br> than or equal to the specified threshold level.<br> Valid values are integers in units of percentages.<br> This attribute is read-write."
					}
				}			
			},
			
			degm:
			{
				type: "number",
				min: 0,
				max: 2147483647,
				label: "DEG M",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute indicates the threshold level for<br> declaring a Degraded Signal defect (dDEG). A dDEG <br>shall be declared if DegM consecutive bad PM Seconds<br> are detected. This attribute is read-write."
					}
				}			
			},
			
			k:
			{		 
				type: "range",
				min: 1,
				max: 3,
				step: 1,
				label: "k",
				
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute specifies the index k that is used<br>to represent a supported bit rate and the different<br> versions of OPUk, ODUk and OTUk. Valid values for<br> this attribute are integers 1, 2 and 3.<br>k = 1 represents an approximate bit rate of 2.5 Gbit/s;<br>k = 2 represents an approximate bit rate of 10 Gbit/s; and<br>k = 3 represents an approximate bit rate of 40 Gbit/s.<br>This attribute is read-only." 
					}
				}			
			},		
			
			currentproblemlist:
			{
				type: "number",
				min: 1,
				max: 31,
				label: "Current Problem List",
							
				attrs:
				{
					"input":
					{
						"data-tooltip": 
							"This attribute indicates the failure conditions of <br>the entity. Possible values of this attribute include decimal numbers between 1 and 31, representing:<br>1) no defect;<br>2) TIM (Trail Trace Identifier Mismatch);<br>3) DEG (Signal Degraded);<br>4) BDI (Backward Defect Indication);<br>5) SSF (Server Signal Fail).<br>This attribute is read-only." 
					}
				}
			},		
		};