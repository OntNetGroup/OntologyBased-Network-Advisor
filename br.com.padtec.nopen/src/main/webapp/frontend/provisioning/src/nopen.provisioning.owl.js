nopen.provisioning.OWL = Backbone.Model.extend({

	app : undefined,
	relationships : undefined,

	initialize : function() {

		var $this = this;

		//get model relationships from OWL
		$.ajax({
			type: "POST",
			async: false,
			url: "getModelRelationships.htm",
			dataType: 'json',
			success: function(relationships){

				//delete INV relation if normal relation exist to the same source and target. 
				$.each(relationships, function(source, sourceValue) {
					$.each(relationships[source], function(target, targetValue) {

						var relations = [];

						$.each(relationships[source][target], function(key, relation) {
							if(relation) {
								if(relation === "INV.binds.Adaptation_Function.Trail_Termination_Function" || relation === "INV.binds.Trail_Termination_Function.Adaptation_Function") {
									relationships[source][target].splice(key, 1);
								}
								else if(relation.indexOf("INV.") >= 0){
									//check if has inverse, if yes, delete it.
									if($this.hasInverse(relationships[source][target], relation)){
										relationships[source][target].splice(key, 1);
									}
								}
							}
						});
					});
				});

				$this.setRelationships(relationships);
			},
			error : function(e) {
				alert("error: " + e.status);
			}
		});

	},

	//Method to check if a relation with specific domain and range has inverse
	hasInverse : function(relations, relation) {

		var hasInverse = false;

		$.each(relations, function(key, value) {

			if(relation && value) {
				if(relation.substring(4, relation.lenght) === value.substring(0, value.lenght)) {
					hasInverse = true;
				}
			}

		});

		return hasInverse;
	},


	hasPath : function(sourcePort, targetPorts, connectionType) {

		var connection = this.app.connectionl

		var resultPorts = [];

		var predicates = [
		                  'ont:is_interface_of',
		                  'ont:componentOf',
		                  'ont:links_output',
		                  'ont:has_path',
		                  'ont:links_output',
		                  'ont:INV.componentOf',
		                  'ont:INV.is_interface_of'
		                  ];

		var subject = 'ont:' + sourcePort.id;

		$.each(targetPorts, function(index, targetPort) {
			var object = 'ont:' + targetPort.id;
			resultPorts[targetPort.id] = connection.askQuery(subject, predicates, object);
		});

		console.log('RESULT PORTS: ' + JSON.stringify(resultPorts));

		return resultPorts;

	},


	//Method to set model relationships 
	setRelationships : function(relationships) {

		this.relationships = relationships;
		console.log(JSON.stringify(relationships));

	},

	//Method to set the application variable
	setApp : function(app) {
		this.app = app;
	},

	//Method to convert elements to triples
	convertElementsToTriples : function(elements) {

		var triples = [];

		$.each(elements, function(index, element) {

			if(element.id) {

				triples.push({
					's' : 'ont:' + element.id,
					'p' : 'rdf:type',
					'o' : 'ont:' + element.type,
				})

				triples.push({
					's' : 'ont:' + element.id,
					'p' : 'rdfs:label',
					'o' : '"' + element.name + '"',
				})

			}


		});

		return triples;
	},

	//Method to convert links to triples
	convertLinksToTriples : function(links) {

		var triples = [];

		$.each(links, function(index, link) {

			if(link.subject && link.predicate && link.object) {
				triples.push({
					's' : 'ont:' + link.subject,
					'p' : 'ont:' + link.predicate,
					'o' : 'ont:' + link.object,
				})
			}

		});

		return triples;
	},

	//Method to parse card JSON file to generate OWL instances
	parseCardToOWL : function(equipment, card) {

		var $this = this;
		var connection = this.app.connection;
		var relationships = this.relationships;

		/*
		 * Supervisor > Equipment
		 * Supervisor > Card
		 * Card_Layer > TTF
		 * Card > Card_Layer
		 * Card > AF
		 * Card > Matrix
		 * Card > Input/Output
		 * TTF/AF/Matrix > Input/Output
		 */

		var elements = [];
		var links = [];

		//Equipment
		var equip = $this.createElement("Equipment", equipment.id, equipment.attributes.attrs.text.text);
		elements.push(equip);

		//Card
		var equipCard = $this.createElement(card.subType, card.id, card.attrs.name.text); 
		elements.push(equipCard);

		//Equipment (E) > Card (C)
		var linkEC = $this.createLink(equipment.id, "Equipment", card.id, card.subType); 
		links.push(linkEC);

		//ITU Elements
		var cardCells = card.attrs.data.cells;

		$.each(cardCells, function(index, element) {

			//Card_Layer
			if(element.subtype === 'Card_Layer') {
				//console.log('Layer: ' + JSON.stringify(element));
				var layer = $this.createElement(element.subtype, element.id, element.lanes.label); 
				elements.push(layer);

				//Card_Layer > Layer_Type
				var link = $this.createLink(element.id, element.subtype, element.lanes.label, "Layer_Type");
				links.push(link);

				//Card > Card_Layer
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);
			}
			//Trail_Termination_Function
			else if (element.subtype === 'Trail_Termination_Function') {

				var ttf = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(ttf);

				//Card_Layer > TTF
				var cardLayerType = $this.getElementType(cardCells, element.parent);

				var link = $this.createLink(element.parent, cardLayerType, element.id, element.subtype);
				links.push(link);
			}
			//Adaptation_Function
			else if (element.subtype === 'Adaptation_Function') {

				var af = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(af);

				//Card > AF
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);

			}
			//Matrix
			else if (element.subtype === 'Matrix') {

				var matrix = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(matrix);

				//Card > Matrix
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);

			}
			//Input_Card / Output_Card
			else if (element.subtype === 'Input_Card' || element.subtype === 'Output_Card') {

				var inOut = $this.createElement(element.subtype, element.id, element.attrs.text.text);
				elements.push(inOut);

				//Card > Input_Card/Output_Card
				var link = $this.createLink(card.id, card.subType, element.id, element.subtype);
				links.push(link);

			}

			//Links
			else if(element.type === 'link') {

				var sourceType = $this.getElementType(cardCells, element.source.id);
				var targetType = $this.getElementType(cardCells, element.target.id);

				var ttf_out = undefined, ttf_in = undefined, af_out = undefined, af_in = undefined, matrix_out = undefined, matrix_in = undefined;

				if(sourceType === 'Trail_Termination_Function') {

					if(targetType === 'Input_Card'){

						//Trail_Termination_Function_Input (TTF_IN)
						ttf_in = $this.createElement("Trail_Termination_Function_Input", joint.util.uuid(), "Trail_Termination_Function_Input");
						elements.push(ttf_in);

						//Trail_Termination_Function (TTF) > Trail_Termination_Function_Intput (TTFIN)
						var linkTTF_TTFIN = $this.createLink(element.source.id, sourceType, ttf_in.id, ttf_in.type);
						links.push(linkTTF_TTFIN);

					}else{

						//Trail_Termination_Function_Output (TTF_OUT)
						ttf_out = $this.createElement("Trail_Termination_Function_Output", joint.util.uuid(), "Trail_Termination_Function_Output"); 
						elements.push(ttf_out);

						//Trail_Termination_Function (TTF) > Trail_Termination_Function_Output (TTFOUT)
						var linkTTF_TTFOUT = $this.createLink(element.source.id, sourceType, ttf_out.id, ttf_out.type); 
						links.push(linkTTF_TTFOUT);
					}
				}

				if(targetType === 'Trail_Termination_Function') {

					//Trail_Termination_Function_Input (TTF_IN)
					ttf_in = $this.createElement("Trail_Termination_Function_Input", joint.util.uuid(), "Trail_Termination_Function_Input");
					elements.push(ttf_in);

					//Trail_Termination_Function (TTF) > Trail_Termination_Function_Intput (TTFIN)
					var linkTTF_TTFIN = $this.createLink(element.target.id, targetType, ttf_in.id, ttf_in.type);
					links.push(linkTTF_TTFIN);

				}

				if(sourceType === 'Adaptation_Function') {

					if(targetType === 'Input_Card'){

						//Adaptation_Function_Input (AF_IN)
						af_in = $this.createElement("Adaptation_Function_Input", joint.util.uuid(), "Adaptation_Function_Input");
						elements.push(af_in);

						//Adaptation_Function (AF) > Adaptation_Function_Input (AFIN)
						var linkAF_AFIN = $this.createLink(element.source.id, sourceType, af_in.id, af_in.type);
						links.push(linkAF_AFIN);

					}else{

						//Adaptation_Function_Output (AF_OUT)
						af_out = $this.createElement("Adaptation_Function_Output", joint.util.uuid(), "Adaptation_Function_Output");
						elements.push(af_out);

						//Adaptation_Function (AF) > Adaptation_Function_Output (AFOUT)
						var linkAF_AFOUT = $this.createLink(element.source.id, sourceType, af_out.id, af_out.type);
						links.push(linkAF_AFOUT);

					}
				}

				if(targetType === 'Adaptation_Function') {

					//Adaptation_Function_Input (AF_IN)
					af_in = $this.createElement("Adaptation_Function_Input", joint.util.uuid(), "Adaptation_Function_Input");
					elements.push(af_in);

					//Adaptation_Function (AF) > Adaptation_Function_Input (AFIN)
					var linkAF_AFIN = $this.createLink(element.target.id, targetType, af_in.id, af_in.type);
					links.push(linkAF_AFIN);

				}

				if(sourceType === 'Matrix') {

					if(targetType === 'Input_Card'){

						//Matrix_Input (M_IN)
						matrix_in = $this.createElement("Matrix_Input", joint.util.uuid(), "Matrix_Input");
						elements.push(matrix_in);

						//Matrix (M) > Matrix_Input (MIN)
						var linkM_MIN = $this.createLink(element.source.id, sourceType, matrix_in.id, matrix_in.type);
						links.push(linkM_MIN);

					}else{
						//Matrix_Output (M_OUT)
						matrix_out = $this.createElement("Matrix_Output", joint.util.uuid(), "Matrix_Output");
						elements.push(matrix_out);

						//Matrix (M) > Matrix_Output (MOUT)
						var linkM_MOUT = $this.createLink(element.source.id, sourceType, matrix_out.id, matrix_out.type);
						links.push(linkM_MOUT);	
					}
				}

				if(targetType === 'Matrix') {

					//Matrix_Input (M_IN)
					matrix_in = $this.createElement("Matrix_Input", joint.util.uuid(), "Matrix_Input");
					elements.push(matrix_in);

					//Matrix (M) > Matrix_Input (MIN)
					var linkM_MIN = $this.createLink(element.target.id, targetType, matrix_in.id, matrix_in.type);
					links.push(linkM_MIN);

				}


				if(sourceType === 'Trail_Termination_Function' && targetType === 'Input_Card'){

					//INPUT_CARD > TRAIL_TERMINATION_FUNCTION_INPUT(TTFIN)
					var linkTTFIN_IN = $this.createLink(element.target.id , targetType , ttf_in.id, ttf_in.type);
					links.push(linkTTFIN_IN);
				}

				if(sourceType === 'Trail_Termination_Function' && targetType === 'Output_Card'){

					//OUTPUT_CARD > TRAIL_TERMINATION_FUNCTION_OUTPUT(TTFOUT)
					var linkTTFOUT_OUT = $this.createLink(element.target.id , targetType , ttf_out.id , ttf_out.type);
					links.push(linkTTFOUT_OUT);
				}

				if(sourceType === 'Trail_Termination_Function' && (targetType === 'Adaptation_Function' || targetType === 'Matrix')) {
					//Reference_Point FEP (FEP)
					var rp = $this.createElement("FEP", element.id, "FEP");
					elements.push(rp);

					//TTF_OUT (TTFOUT) > FEP (FEP)
					var linkTTFOUT_FEP = $this.createLink(element.id, "FEP", ttf_out.id, ttf_out.type);
					links.push(linkTTFOUT_FEP);

					if(targetType === 'Adaptation_Function') {
						//FEP (FEP) > AF_IN (AFIN)
						var linkFEP_AFIN = $this.createLink(element.id, "FEP", af_in.id, af_in.type);
						links.push(linkFEP_AFIN);
					}
					else {
						//FEP (FEP) > MATRIX_IN (MIN)
						var linkFEP_MIN = $this.createLink(element.id, "FEP", matrix_in.id, matrix_in.type);
						links.push(linkFEP_MIN);
					}

				}


				if(sourceType === 'Adaptation_Function' && targetType === 'Input_Card'){

					//INPUT_CARD > Adaption_FUNCTION_INPUT(AFIN)
					var linkAFIN_IN = $this.createLink(element.target.id , targetType , af_in.id, af_in.type);
					links.push(linkAFIN_IN);
				}

				if(sourceType === 'Adaptation_Function' && targetType === 'Output_Card'){

					//OUTPUT_CARD > Adaption_FUNCTION_OUTPUT(AFOUT)
					var linkAFOUT_OUT = $this.createLink(element.target.id , targetType ,  af_out.id, af_out.type);
					links.push(linkAFOUT_OUT);
				}

				if(sourceType === 'Adaptation_Function' && targetType === 'Trail_Termination_Function') {
					//Reference_Point AP (AP)
					var rp = $this.createElement("AP", element.id, "AP");
					elements.push(rp);

					//AF_OUT (AFOUT) > AP (AP)
					var linkAFOUT_AP = $this.createLink(element.id, "AP", af_out.id, af_out.type);
					links.push(linkAFOUT_AP);

					//AP (AP) > TTF_IN (TTFIN)
					var linkAP_TTFIN = $this.createLink(element.id, "AP", ttf_in.id, ttf_in.type);
					links.push(linkAP_TTFIN);

				}

				if(sourceType === 'Matrix' && targetType === 'Input_Card'){

					//INPUT_CARD > Matrix_INPUT(MIN)
					var linkMIN_IN = $this.createLink(element.target.id , targetType ,matrix_in.id, matrix_in.type);
					links.push(linkMIN_IN);
				}

				if(sourceType === 'Matrix' && targetType === 'Output_Card'){

					//OUTPUT_CARD > Matrix_OUTPUT(MOUT)
					var linkMOUT_OUT = $this.createLink(element.target.id , targetType ,  matrix_out.id, matrix_out.type);
					links.push(linkMOUT_OUT);
				}

				if(sourceType === 'Matrix' && targetType === 'Adaptation_Function') {
					//Reference_Point FP (FP)
					var rp = $this.createElement("FP", element.id, "FP");
					elements.push(rp);

					//FP (FP) > M_OUT (MOUT)
					var linkMOUT_FP = $this.createLink(element.id, "FP", matrix_out.id, matrix_out.type);
					links.push(linkMOUT_FP);

					//FP (FP) > AFIN (AFIN)
					var linkFP_AFIN = $this.createLink(element.id, "FP", af_in.id, af_in.type);
					links.push(linkFP_AFIN);

				}

				var link = $this.createLink(element.source.id, sourceType, element.target.id, targetType);
				links.push(link);

			}

		});

		var pElements = elements;
		var pLinks = [];

		$.each(links, function(key, link){
			$.each(link, function(k, l){
				pLinks.push(l)
			});
		});

		var eTriples = $this.convertElementsToTriples(pElements);
		var lTriples = $this.convertLinksToTriples(pLinks);

		connection.save(eTriples, "http://localhost:8080/nopen/provisioning.htm");
		connection.save(lTriples, "http://localhost:8080/nopen/provisioning.htm");

//		connection.testQuery();

//		console.log('Elements: ' + JSON.stringify(pElements));
//		console.log('Links: ' + JSON.stringify(pLinks));

		//execute parse
//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "parseCardToOWL.htm",
//		data: {
//		'elements' : JSON.stringify(pElements),
//		'links' : JSON.stringify(pLinks),
//		},
//		success: function(){
//		//console.log('PARSE OK!')
//		},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});


	},

	parseConnectedPortsToOWL : function(graph) {

		var connection = this.app.connection;
		var relationships = this.relationships;
		
		var $this = this;
		var model = this.app.model;

		var elements = [];
		var links = [];
		var portf = {};

		$.each(graph.getElements(), function(index, value){

			var equipment = graph.getCell(value.id);
			var cards = model.getCardsInPreProvisioning(equipment);


			// popular uma hash com as ids de tfout->outpor e tfin->inport
			$.each(cards, function(key, card){
				var icount = 0;
				var jcount = 0;

				$.each(card.inPorts, function(inportId,inportName){ 
					portf[inportId]= card.TFIn[icount].id;
					icount++;
				});

				$.each(card.outPorts, function(outportId,outportName){ 
					portf[outportId]= card.TFOut[jcount].id;
					jcount++;
				});	

			});

//			console.log(portf);

			$.each(cards, function(key, card){

				//Input_Card/Output_Card > Input_Card/Output_Card
				$.each(card.connectedPorts, function(portId, connectedPort) {

					if(card.outPorts[portId]) {
						if(connectedPort.edge === "target") {
							var link = $this.createLink(portId, "Output_Card", connectedPort.id, connectedPort.type);
							links.push(link);

							if(connectedPort.type === "Input_Card"){
								var tfout = connection.selectTFOutFromTF(portf[portId], portId);
								var tfin = connection.selectTFInFromTF(portf[connectedPort.id], connectedPort.id);
								var tfouttype = connection.getTFType(portf[portId]);
								var tfintype = connection.getTFType(portf[connectedPort.id]);

//								console.log(tfouttype);
//								console.log(tfintype);

								var idlink = joint.util.uuid();
								if(tfintype === 'Adaptation_Function' && tfouttype === 'Matrix') {
									console.log('saci');
									//Reference_Point FP (FP)
									var rp = $this.createElement("FP", idlink, "FP");
									elements.push(rp);

									//FP (FP) > M_OUT (MOUT)
									var linkMOUT_FP = $this.createLink(idlink, "FP", tfin, 'Adaptation_Function_Input');
									links.push(linkMOUT_FP);

									//FP (FP) > AFIN (AFIN)
									var linkFP_AFIN = $this.createLink(idlink, "FP", tfout, 'Matrix_Output');
									links.push(linkFP_AFIN);

								}

								if(tfintype === 'Adaptation_Function' && tfouttype === 'Trail_Termination_Function') {
									console.log('saci');
									//Reference_Point AP (AP)
									var rp = $this.createElement("AP", idlink, "AP");
									elements.push(rp);

									//AF_OUT (AFOUT) > AP (AP)
									var linkAFOUT_AP = $this.createLink(idlink, "AP", tfout, 'Trail_Termination_Function_Output');
									links.push(linkAFOUT_AP);

									//AP (AP) > TTF_IN (TTFIN)
									var linkAP_TTFIN = $this.createLink(idlink, "AP", tfin, 'Adaptation_Function_Input');
									links.push(linkAP_TTFIN);

								}

								if(tfintype === 'Matrix' && tfouttype === 'Trail_Termination_Function') {
									console.log('saci');
									//Reference_Point FEP (FEP)
									var rp = $this.createElement("FEP", idlink, "FEP");
									elements.push(rp);

									//TTF_OUT (TTFOUT) > FEP (FEP)
									var linkTTFOUT_FEP = $this.createLink(idlink, "FEP", tfin, 'Matrix_Input');
									links.push(linkTTFOUT_FEP);

									//FEP (FEP) > MATRIX_IN (MIN)
									var linkFEP_MIN = $this.createLink(idlink, "FEP", tfout, 'Trail_Termination_Function_Output');
									links.push(linkFEP_MIN);

								}

//								console.log(links);
							}



							var tfSource = model.getTFElementConnectedToPortInPreProvisioning(equipment, portId);
							var tfTarget = model.getTFElementConnectedToPortInPreProvisioning(equipment, connectedPort.id);

							var connections = $this.createVerticalConnection(tfSource, tfTarget);

							//add elements and links in hash
							//DEBUGAR PROCURAR TF IN E TF OUT
							$.each(connections.elements, function(key, element){
								elements.push(element)
							});
							$.each(connections.links, function(key, link){
								links.push(link)
							});

						}
					}

				});

			});

		});

		var pElements = elements;
		var pLinks = [];

		$.each(links, function(key, link){
			$.each(link, function(k, l){
				pLinks.push(l)
			});
		});

		var eTriples = $this.convertElementsToTriples(pElements);
		var lTriples = $this.convertLinksToTriples(pLinks);

		connection.save(eTriples, "http://localhost:8080/nopen/provisioning.htm");
		connection.save(lTriples, "http://localhost:8080/nopen/provisioning.htm");

		//execute parse
		//execute parse
//		$.ajax({
//			type: "POST",
//			async: false,
//			url: "savetojena.htm",
//			data: {
//				'elements' : JSON.stringify(pElements),
//				'links' : JSON.stringify(pLinks),
//			},
//			success: function(){
//				console.log('saved!')
//			},
//			error : function(e) {
//				alert("error: " + e.status);
//			}
//		});

	},
	
	generateHasPaths: function(){
		
		var connection = this.app.connection;
		
		//querry para retornar todos os reference points
		var has_paths = connection.getRPInAndRPOut();
		
		connection.insertHasPath(has_paths);
		
		
	},

	addHorizontalConnection : function(tfSource, tfTarget , sourcePort , targetPort) {

		var connection = this.app.connection;

		var $this = this;
		var connections = this.createHorizontalConnection(tfSource, tfTarget , sourcePort , targetPort);

		var pElements = [];
		var pLinks = [];

		$.each(connections.elements, function(key, element){
			pElements.push(element)
		});

		$.each(connections.links, function(key, link){
			$.each(link, function(k, l){
				pLinks.push(l)
			});
		});

		var eTriples = $this.convertElementsToTriples(pElements);
		var lTriples = $this.convertLinksToTriples(pLinks);

		connection.save(eTriples,"http://localhost:8080/nopen/provisioning.htm");
		connection.save(lTriples,"http://localhost:8080/nopen/provisioning.htm");

		
		
//		//execute parse
//		$.ajax({
//			type: "POST",
//			async: false,
//			url: "savetojena.htm",
//			data: {
//				'elements' : JSON.stringify(pElements),
//				'links' : JSON.stringify(pLinks),
//			},
//			success: function(){
//				console.log('saved!')
//			},
//			error : function(e) {
//				alert("error: " + e.status);
//			}
//		});

	},

	addVerticalConnection : function(tfSource, tfTarget) {

		var connection = this.app.connection;

		var $this = this;
		var connections = this.createVerticalConnection(tfSource, tfTarget);

		var pElements = [];
		var pLinks = [];

		$.each(connections.elements, function(key, element){
			pElements.push(element)
		});

		$.each(connections.links, function(key, link){
			$.each(link, function(k, l){
				pLinks.push(l)
			});
		});

		var eTriples = $this.convertElementsToTriples(pElements);
		var lTriples = $this.convertLinksToTriples(pLinks);

		connection.save(eTriples, "http://localhost:8080/nopen/provisioning.htm");
		connection.save(lTriples, "http://localhost:8080/nopen/provisioning.htm");

		//execute parse
//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "parseCardToOWL.htm",
//		data: {
//		'elements' : JSON.stringify(pElements),
//		'links' : JSON.stringify(pLinks),
//		},
//		success: function(){
//		//console.log('PARSE OK!')
//		},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});

	},

	createHorizontalConnection : function(tfSource, tfTarget , sourcePort , targetPort ) {

		var $this = this;
		var connection = this.app.connection;

		var elements = [];
		var links = [];

		//Physical_Media PM (PM)
		var pm = $this.createElement("Physical_Media", joint.util.uuid(), "PM_" + tfSource.name + "_" + tfTarget.name);
		elements.push(pm);

		//Physical_Media PM_So (PMSO)
		var pm_so = $this.createElement("PM_Input_So", joint.util.uuid(), "PM_SO_" + tfSource.name + "_" + tfTarget.name);
		elements.push(pm);

		//Physical_Media PM_Sk (PMSK)
		var pm_sk = $this.createElement("PM_Input_Sk", joint.util.uuid(), "PM_SK_" + tfSource.name + "_" + tfTarget.name);
		elements.push(pm);


		//Physical_Media (PM) > PM_Input_So (PMSO) 
		var link_PM_PMSO = $this.createLink(pm.id, pm.type, pm_so.id, pm_so.type);
		links.push(link_PM_PMSO);

		//Physical_Media (PM) > PM_Input_Sk (PMSK)
		var link_PM_PMSK = $this.createLink(pm.id, pm.type, pm_sk.id, pm_sk.type);
		links.push(link_PM_PMSK);


		//Reference_Point FEP (FEP)
		var fep_pmso = $this.createElement("FEP", joint.util.uuid(), "FEP");
		elements.push(fep_pmso);

		//Trail_Termination_Function_Output (TTF_OUT)
		var ttf_out_source_id = connection.selectTFOutFromTF(tfSource.id , sourcePort.id);
		var ttf_out_source_type = "Trail_Termination_Function_Output";

		//FEP > PM_Input_So (PMSO)
		var link_FEP_PMSO = $this.createLink(fep_pmso.id, fep_pmso.type, pm_so.id, pm_so.type);
		links.push(link_FEP_PMSO);

		//FEP > Trail_Termination_Function_Output (TTFOUT)
		var link_FEP_TTFOUT = $this.createLink(fep_pmso.id, fep_pmso.type, ttf_out_source_id, ttf_out_source_type);
		links.push(link_FEP_TTFOUT);

		//Reference_Point FEP (FEP)
		var fep_pmsk = $this.createElement("FEP", joint.util.uuid(), "FEP");
		elements.push(fep_pmsk);

		//Trail_Termination_Function_Output (TTF_OUT)
		var ttf_out_target_id = connection.selectTFOutFromTF(tfTarget.id , targetPort.id);
		var ttf_out_target_type = "Trail_Termination_Function_Output";

		//FEP > PM_Input_Sk (PMSK)
		var link_FEP_PMSK = $this.createLink(fep_pmsk.id, fep_pmsk.type, pm_sk.id, pm_sk.type);
		links.push(link_FEP_PMSK);

		//FEP > Trail_Termination_Function_Output (TTFOUT)
		var link_FEP_TTFOUT = $this.createLink(fep_pmsk.id, fep_pmsk.type, ttf_out_target_id, ttf_out_target_type);
		links.push(link_FEP_TTFOUT);

		connection.createHasPathfromPM(fep_pmso.id , fep_pmsk.id);
		
		var horizontalConnections = {
				'elements' : elements,
				'links' : links,
		}

		return horizontalConnections;

	},

	createVerticalConnection : function(tfSource, tfTarget) {

		var $this = this;

		var elements = [];
		var links = [];

		var model = this.app.model;

		var sourceType = tfSource.type;
		var targetType = tfTarget.type;

		var ttf_out = undefined, ttf_in = undefined, af_out = undefined, af_in = undefined, matrix_out = undefined, matrix_in = undefined;

		if(sourceType === 'Trail_Termination_Function') {

			//Trail_Termination_Function_Output (TTF_OUT)
			ttf_out = $this.createElement("Trail_Termination_Function_Output", joint.util.uuid(), "Trail_Termination_Function_Output"); 
			elements.push(ttf_out);

			//Trail_Termination_Function (TTF) > Trail_Termination_Function_Output (TTFOUT)
			var linkTTF_TTFOUT = $this.createLink(tfSource.id, sourceType, ttf_out.id, ttf_out.type); 
			links.push(linkTTF_TTFOUT);

		}

		if(targetType === 'Trail_Termination_Function') {

			//Trail_Termination_Function_Input (TTF_IN)
			ttf_in = $this.createElement("Trail_Termination_Function_Input", joint.util.uuid(), "Trail_Termination_Function_Input");
			elements.push(ttf_in);

			//Trail_Termination_Function (TTF) > Trail_Termination_Function_Intput (TTFIN)
			var linkTTF_TTFIN = $this.createLink(tfTarget.id, targetType, ttf_in.id, ttf_in.type);
			links.push(linkTTF_TTFIN);

		}

		if(sourceType === 'Adaptation_Function') {

			//Adaptation_Function_Output (AF_OUT)
			af_out = $this.createElement("Adaptation_Function_Output", joint.util.uuid(), "Adaptation_Function_Output");
			elements.push(af_out);

			//Adaptation_Function (AF) > Adaptation_Function_Output (AFOUT)
			var linkAF_AFOUT = $this.createLink(tfSource.id, sourceType, af_out.id, af_out.type);
			links.push(linkAF_AFOUT);

		}

		if(targetType === 'Adaptation_Function') {

			//Adaptation_Function_Input (AF_IN)
			af_in = $this.createElement("Adaptation_Function_Input", joint.util.uuid(), "Adaptation_Function_Input");
			elements.push(af_in);

			//Adaptation_Function (AF) > Adaptation_Function_Input (AFIN)
			var linkAF_AFIN = $this.createLink(tfTarget.id, targetType, af_in.id, af_in.type);
			links.push(linkAF_AFIN);

		}

		if(sourceType === 'Matrix') {

			//Matrix_Output (M_OUT)
			matrix_out = $this.createElement("Matrix_Output", joint.util.uuid(), "Matrix_Output");
			elements.push(matrix_out);

			//Matrix (M) > Matrix_Output (MOUT)
			var linkM_MOUT = $this.createLink(tfSource.id, sourceType, matrix_out.id, matrix_out.type);
			links.push(linkM_MOUT);

		}

		if(targetType === 'Matrix') {

			//Matrix_Input (M_IN)
			matrix_in = $this.createElement("Matrix_Input", joint.util.uuid(), "Matrix_Input");
			elements.push(matrix_in);

			//Matrix (M) > Matrix_Input (MIN)
			var linkM_MIN = $this.createLink(tfTarget.id, targetType, matrix_in.id, matrix_in.type);
			links.push(linkM_MIN);

		}

		if(sourceType === 'Trail_Termination_Function' && (targetType === 'Adaptation_Function' || targetType === 'Matrix')) {
			//Reference_Point FEP (FEP)
			var fep = $this.createElement("FEP", joint.util.uuid(), "FEP");
			elements.push(fep);

			//TTF_OUT (TTFOUT) > FEP (FEP)
			var linkTTFOUT_FEP = $this.createLink(fep.id, fep.type, ttf_out.id, ttf_out.type);
			links.push(linkTTFOUT_FEP);

			if(targetType === 'Adaptation_Function') {
				//FEP (FEP) > AF_IN (AFIN)
				var linFEP_AFIN = $this.createLink(fep.id, fep.type, af_in.id, af_in.type);
				links.push(linFEP_AFIN);
			}
			else {
				//FEP (FEP) > MATRIX_IN (MIN)
				var linkFEP_MIN = $this.createLink(fep.id, fep.type, matrix_in.id, matrix_in.type);
				links.push(linkFEP_MIN);
			}

		}

		if(sourceType === 'Adaptation_Function' && targetType === 'Trail_Termination_Function') {
			//Reference_Point AP (AP)
			var ap = $this.createElement("AP", joint.util.uuid(), "AP");
			elements.push(ap);

			//AF_OUT (AFOUT) > AP (AP)
			var linAFOUT_AP = $this.createLink(ap.id, ap.type, af_out.id, af_out.type);
			links.push(linAFOUT_AP);

			//AP (AP) > TTF_IN (TTFIN)
			var linkAP_TTFIN = $this.createLink(ap.id, ap.type, ttf_in.id, ttf_in.type);
			links.push(linkAP_TTFIN);
		}

		if(sourceType === 'Matrix' && targetType === 'Adaptation_Function') {
			//Reference_Point FP (FP)
			var fp = $this.createElement("FP", joint.util.uuid(), "FP");
			elements.push(fp);

			//FP (FP) > M_OUT (MOUT)
			var linkMOUT_FP = $this.createLink(fp.id, fp.type, matrix_out.id, matrix_out.type);
			links.push(linkMOUT_FP);

			//FP (FP) > AFIN (AFIN)
			var linkFP_AFIN = $this.createLink(fp.id, fp.type, af_in.id, af_in.type);
			links.push(linkFP_AFIN);
		}

		var verticalConnections = {
				'elements' : elements,
				'links' : links,
		}

		return verticalConnections;

	},



	//create a JOSN link.
	createLink : function(subject, subjectType, object, objectType) {

		var links = [];
		var relationships = this.relationships;

		$.each(relationships[subjectType][objectType], function(key, predicate) {

			links.push({
				"subjectType" : subjectType,
				"subject" : subject,
				"predicate" : predicate,
				"objectType" : objectType,
				"object" : object,
			})

		});

		return links;

	},

	//create a JSON element.
	createElement : function(type, id, name) {

		return element = {
				"type" : type,
				"id" : id,
				"name" : name,
		}

	},

	//create connection between ports in OWL
	connectPorts : function(sourcePort, targetPort) {

		var connection = this.app.connection;

		var $this = this;
		var relationships = this.relationships;

		var connection = this.app.connection;

		var links = [];
		var elements = [];

		elements.push(sourcePort);
		elements.push(targetPort);

		links.push({
			"subjectType" : sourcePort.type,
			"subject" : sourcePort.id,
			"predicate" : relationships[sourcePort.type][targetPort.type][0],
			"objectType" : targetPort.type,
			"object" : targetPort.id,
		})

		var eTriples = $this.convertElementsToTriples(elements);
		var lTriples = $this.convertLinksToTriples(links);

		connection.save(eTriples, "http://localhost:8080/nopen/provisioning.htm");
		connection.save(lTriples, "http://localhost:8080/nopen/provisioning.htm");

//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "connectPortsInOWL.htm",
//		data: {
//		'elements' : JSON.stringify(elements),
//		'links' : JSON.stringify(links),
//		},
//		success: function(){
//		//execute reasoning
//		$this.executeReasoning();
//		},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});

	},

	//create connection between ports in OWL
	connectPortsWithoutReasoning : function(sourcePort, targetPort) {

		var connection = this.app.connection;

		var $this = this;
		var relationships = this.relationships;

		var links = [];
		var elements = [];

		elements.push(sourcePort);
		elements.push(targetPort);

		links.push({
			"subjectType" : sourcePort.type,
			"subject" : sourcePort.id,
			"predicate" : relationships[sourcePort.type][targetPort.type][0],
			"objectType" : targetPort.type,
			"object" : targetPort.id,
		})

		var eTriples = $this.convertElementsToTriples(elements);
		var lTriples = $this.convertLinksToTriples(links);

		connection.save(eTriples, "http://localhost:8080/nopen/provisioning.htm");
		connection.save(lTriples, "http://localhost:8080/nopen/provisioning.htm");

//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "connectPortsInOWL.htm",
//		data: {
//		'elements' : JSON.stringify(elements),
//		'links' : JSON.stringify(links),
//		},
//		success: function(){},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});

	},

	//execute reasoning
	executeReasoning : function() {

		$('.ajax-loader-message label').append(' Executing Reasoner ... ');

		//execute parse
		$.ajax({
			type: "POST",
			//async: false,
			url: "executeReasoning.htm",
			success: function(){
				//console.log('PARSE OK!')
				$('.ajax-loader-message label').empty();
			},
			error : function(e) {
				alert("error: " + e.status);
			}
		});

	},

	//Methos to get the layer by layer id
	getLayerName : function(elements, layerId) {

		var layerName = undefined;

		$.each(elements, function(index, element) {

			if(element.id === layerId) {
				if(element.subtype) {
					layerName = element.lanes.label;
				}
			}

		});

		return layerName;

	},

	//Method to get element type
	getElementType : function(elements, elementId) {

		var type = undefined;

		$.each(elements, function(index, element) {

			if(element.id === elementId) {
				if(element.subtype) {
					type = element.subtype;
				}
				else if(element.subType) {
					type = element.subType;
				}
			}

		});

		return type;

	},

	//Method to get inputs from OWL file
	getConnectionTypeFromOWL : function(equipmentSourceId, equipmentTargetId) {

		var connectionType = undefined;
		var $this = this;

		connectionType = 'Horizontal';

//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "getConnectionTypeFromOWL.htm",
//		data: {
//		'equipmentSourceId' : equipmentSourceId,
//		'equipmentTargetId' : equipmentTargetId,
//		},
//		success: function(data){
//		connectionType = data;
//		console.log('connectionType: ' + connectionType)
//		},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});

		return connectionType;
	},

	getPortsByLayerFromOWL : function(equipmentId, portType, connectionType, checkConnectedPorts) {

		var resultPorts = {};

		var namespace = "http://www.menthor.net/provisioning.owl#";

		var connection = this.app.connection;
		var $this = this;

		var predicates = [
		                  'ont:A_Equipment_Card',
		                  'ont:A_Card_CardLayer'
		                  ];

		var subject = 'ont:' + equipmentId;
		var layers = connection.selectObjectsQuery(subject, predicates);

		var possibleLayers = undefined;

		if(connectionType) {
			if(connectionType === 'Vertical' || connectionType === 'Horizontal_ST') {
				possibleLayers = connection.selectTopLayers();
			}
			else if(connectionType === 'Horizontal' ){
				possibleLayers = connection.selectBottomLayers();
			}
			else {
				possibleLayers = connection.selectAllLayers();
			}
		}
		else {
			possibleLayers = connection.selectAllLayers();
		}

		$.each(layers, function(index, layer) {

			var layerLabel = connection.selectLabelQuery(layer); 

			existLayer = false;

			$.each(possibleLayers, function(key, possibleLayer) {
				if(layerLabel === possibleLayer) {
					existLayer = true;
				}
			})

			if(existLayer) {

				predicates = [];
				if(portType === 'Output_Card') {
					predicates = [
					              'ont:INV.intermediates_up',
					              'ont:INV.is_interface_of.Output_Card.Transport_Function'
					              ];
				}
				else if(portType === 'Input_Card') {
					predicates = [
					              'ont:INV.intermediates_down',
					              'ont:INV.is_interface_of.Input_Card.Transport_Function'
					              ];
				}

				layer = layer.replace(namespace, 'ont:');

				if(connectionType === 'Horizontal_ST'){
					var ports = connection.selectPortsOfMatrixType(layer);

					$.each(ports, function(key, port) {
						var portLabel = connection.selectLabelQuery(port); 
						var portId = port.replace(namespace, '');

						if(!resultPorts[layerLabel]) {
							resultPorts[layerLabel] = [];
						}

						var gambiarra = false;

						$.each(resultPorts[layerLabel], function(lLabel, p) {
							if(p.id === portId){
								gambiarra = true;
							}

						});

						if(!gambiarra){
							resultPorts[layerLabel].push({
								'id': portId,
								'name': connection.selectCardLabelFromPort(portId)+": "+portLabel,
								'type': portType,
							});
						}
					});

				}else{
					var ports = connection.selectObjectsQuery(layer, predicates);

					$.each(ports, function(key, port) {

						var portLabel = connection.selectLabelQuery(port); 

						var portId = port.replace(namespace, '');
						var containsPort = false;

						var isConnectedPort = false;

						if(checkConnectedPorts) {
							isConnectedPort = connection.askIsConnectedPort(portId);
						}

						if(!isConnectedPort) {

							if(!resultPorts[layerLabel]) {
								resultPorts[layerLabel] = [];
							}

							$.each(resultPorts[layerLabel], function(key, resultPort) {
								if(resultPort.id === portId) {
									containsPort = true;
								}
							});
							connection.selectCardLabelFromPort(portId);
							if(!containsPort) {
								resultPorts[layerLabel].push({
									'id': portId,
									'name': connection.selectCardLabelFromPort(portId)+": "+portLabel,
									'type': portType,
								});
							}
						}

					});
				}
			}

		});

		return resultPorts;


	},

	//Method do get possible connection ports (interfaces) from OWL
	getPossibleConnectionsFromOWL : function(connectionType, equipmentSourceId, equipmentTargetId) {

		var $this = this;
		var connections = [];

		if(connectionType === 'Horizontal' || connectionType === 'Horizontal_ST') {
			connections[equipmentSourceId] = this.getPortsByLayerFromOWL(equipmentSourceId, 'Output_Card', connectionType, true);
			connections[equipmentTargetId] = this.getPortsByLayerFromOWL(equipmentTargetId, 'Output_Card', connectionType, true);
		}
		else {
			connections[equipmentSourceId] = this.getPortsByLayerFromOWL(equipmentSourceId, 'Output_Card', connectionType, true);
			connections[equipmentTargetId] = this.getPortsByLayerFromOWL(equipmentTargetId, 'Input_Card', connectionType, true);
		}



//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "getPossibleConnectionsFromOWL.htm",
//		data: {
//		'equipmentSourceId' : equipmentSourceId,
//		'equipmentTargetId' : equipmentTargetId,
//		'connectionType' : connectionType,
//		},
//		dataType: 'json',
//		success: function(data){
//		console.log('connections: ' + JSON.stringify(data))
//		connections = data;
//		},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});



		return connections;

	},

	//Method to get inputs from OWL file
	getInputsFromOWL : function(equipmentId) {

		var inputs = this.getPortsByLayerFromOWL(equipmentId, 'Input_Card', 'All', false);

//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "getInputsFromOWL.htm",
//		data: {
//		'equipmentId' : equipmentId,
//		},
//		dataType: 'json',
//		success: function(data){
//		inputs = data;
//		},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});

		return inputs;
	},

	//Method to get outputs from OWL file
	getOutputsFromOWL : function(equipmentId) {

		var outputs = this.getPortsByLayerFromOWL(equipmentId, 'Output_Card', 'All', false);

//		$.ajax({
//		type: "POST",
//		async: false,
//		url: "getOutputsFromOWL.htm",
//		data: {
//		'equipmentId' : equipmentId,
//		},
//		dataType: 'json',
//		success: function(data){
//		outputs = data;
//		},
//		error : function(e) {
//		alert("error: " + e.status);
//		}
//		});

		return outputs;
	},



});