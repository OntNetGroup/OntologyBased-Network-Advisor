function getTemplates(graph){

	$.ajax({
		type: "GET",
		url: "getAllTemplates.htm",
		dataType: 'json',
		success: function(data){ 		   
			generateOpenTemplateDialog(graph, data)
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

}

function getUrlParameter(sParam)
{
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++) 
	{
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] == sParam) 
		{
			return sParameterName[1];
		}
	}
}      

function openFromURL(filename, graph){

	$.ajax({
		type: "POST",
		url: "openTemplate.htm",
		data: {
			'filename' : filename
		},
		dataType: 'json',
		success: function(data){
			$("#filename").val(filename);
			graph.fromJSON(data);
			loadEquipmentstoOWl(filename);
		},
		error : function(e) {
			//alert("error: " + e.status);
		}
	});

	function loadEquipmentstoOWl(filename){
		
		$.each(graph.getElements(), function(index, cell){
			
			/*
			 * Supervisor > Cards
			 * Slot > Card
			 * Slot > Supervisor
			 * Shelf > Slot
			 * Rack > Shelf
			 * Card > Matrix
			 * Card > Input/Output
			 * TTF/AF/Matrix > Input/Output
			 */
			
			var elements = [];
			var links = [];
			
			if(cell.get('subType') === 'Rack'){
				app.RackCounter++
				var rack = {
						"type" : "Rack",
						"id" : cell.id,
						"name" : cell.attributes.attrs.name.text,
				};
				console.log(rack);
				elements.push(rack);
			}
			
			else if(cell.get('subType') === 'Shelf'){
				app.ShelfCounter++;
				var shelf = {
						"type" : "Shelf",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
				};
				console.log(shelf);
				elements.push(shelf);
				
				//Rack (R) > Shelf (Sh)
				var linkShR = {
						"sourceType" : "Shelf",
						"targetType" : "Rack",
						"source" : cell.get('id'),
						"target" : cell.get('parent'),
				};
				console.log(linkShR);
				links.push(linkShR);

			}
			else if(cell.get('subType') === 'Slot'){
				app.SlotCounter++;
				var slot = {
						"type" : "Slot",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
				};
				console.log(slot);
				elements.push(slot);
				
				//Slot (Sl) > Shelf (Sh)
				var linkSlSh = {
						"sourceType" : "Slot",
						"targetType" : "Shelf",
						"source" : cell.get('id'),
						"target" : cell.get('parent'),
				};
				console.log(linkSlSh);
				links.push(linkSlSh);

			}

			else if(cell.get('subType')=== 'Card'){
				app.CardCounter++;
				var card = {
						"type" : "Card",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
						
				};
				elements.push(card);
				
				//Slot (Sl) > Card (C)
				var linkSlC = {
						"sourceType" : "Card",
						"targetType" : "Slot",
						"source" : cell.get('id'),
						"target" : cell.get('parent'),
				};
				links.push(linkSlC);
				
				//Supervisor (S) > Card (C)
				var linkSC = {
						"sourceType" : "Card",
						"targetType" : "Supervisor",
						"source" : cell.get('id'),
						"target" : cell.get('SupervisorID'),
				};
				links.push(linkSC);
				
			}
			else if(cell.get('subType')=== 'Supervisor'){
				app.SupervisorCounter++;
				var supervisor = {
						"type" : "Supervisor",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
				};
				elements.push(supervisor);
				
				//Slot (Sl) > Supervisor (S)
				var linkSlC = {
						"sourceType" : "Supervisor",
						"targetType" : "Slot",
						"source" : cell.get('id'),
						"target" : cell.get('parent'),
				};
				links.push(linkSlC);

				//setTechnology(equipmentName, equipmentType, equipmentID , tech);

			}
			
			console.log('Elements: ' + JSON.stringify(elements));
			console.log('Links: ' + JSON.stringify(links));
				
			
			//execute parse
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "parseEquipToOWL.htm",
			   data: {
				   'elements' : JSON.stringify(elements),
				   'links' : JSON.stringify(links),
			   },
			   success: function(){
				  console.log('PARSE OK!')
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
			
		});
	
		
		//ITU Elements
		$.each(graph.getElements(), function(index, cell){
			if(cell.get('subType')=== 'Card'){
				var card = {
						"type" : "Card",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
						
				};
//				instanciateITUElementsJohn(filename, card);
				instanciateITUElementsJordana(filename, card);
			}
		});
	}


}

function generateOpenTemplateDialog(graph, data){

	var content = '<form id="open">';
	for(var i = 0; i < Object.keys(data).length; i++){
		if(i == 0){
			content = content + '<input type="radio" name="template" value="' + data[i].template + '" checked>' 
			+ '<label>' + data[i].template + '</label> <br>';
		}
		else{
			content = content + '<input type="radio" name="template" value="' + data[i].template + '">' 
			+ '<label>' + data[i].template + '</label><br>';
		}

	}
	content = content +  '</form>';

	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Open Template',
		content: content,
		buttons: [
		          { action: 'cancel', content: 'Cancel', position: 'left' },
		          { action: 'open', content: 'Open', position: 'left' }
		          ]
	});
	dialog.on('action:open', openTemplate);
	dialog.on('action:cancel', dialog.close);
	dialog.open();

	function openTemplate(){

		var filename = $('input[name=template]:checked', '#open').val();

		$.ajax({
			type: "POST",
			url: "openTemplate.htm",
			data: {
				'filename' : filename
			},
			dataType: 'json',
			success: function(data){
				$("#filename").val(filename);
				graph.fromJSON(data);
				loadEquipmentstoOWl();
				dialog.close();
			},
			error : function(e) {
				alert("error: " + e.status);
				dialog.close();
			}
		});

		
		function loadEquipmentstoOWl(){
			
			var elements = [];
			var links = [];
			
			$.each(graph.getElements(), function(index, cell){
				
				/*
				 * Supervisor > Cards
				 * Slot > Card
				 * Slot > Supervisor
				 * Shelf > Slot
				 * Rack > Shelf
				 * Card > Matrix
				 * Card > Input/Output
				 * TTF/AF/Matrix > Input/Output
				 */
				
				if(cell.get('subType') === 'Rack'){
					app.RackCounter++
					var rack = {
							"type" : "Rack",
							"id" : cell.id,
							"name" : cell.attributes.attrs.name.text,
					};
					elements.push(rack);
				}
				
				if(cell.get('subType') === 'Shelf'){
					app.ShelfCounter++;
					var shelf = {
							"type" : "Shelf",
							"id" : cell.get('id'),
							"name" : cell.attributes.attrs.name.text,
					};
					elements.push(shelf);
					
					//Rack (R) > Shelf (Sh)
					var linkShR = {
							"sourceType" : "Rack",
							"targetType" : "Shelf",
							"source" : cell.get('parent'),
							"target" : cell.get('id'),
					};
					links.push(linkShR);

				}
				if(cell.get('subType') === 'Slot'){
					app.SlotCounter++;
					var slot = {
							"type" : "Slot",
							"id" : cell.get('id'),
							"name" : cell.attributes.attrs.name.text,
					};
					elements.push(slot);
					
					//Shelf (Sh) > Slot (Sl)
					var linkSlSh = {
							"sourceType" : "Shelf",
							"targetType" : "Slot",
							"source" : cell.get('parent'),
							"target" : cell.get('id'),
					};
					links.push(linkSlSh);

				}

				if(cell.get('subType')=== 'Card'){
					app.CardCounter++;
					var card = {
							"type" : "Card",
							"id" : cell.get('id'),
							"name" : cell.attributes.attrs.name.text,
							
					};
					elements.push(card);
					
					//Slot (Sl) > Card (C)
					var linkSlC = {
							"sourceType" : "Slot",
							"targetType" : "Card",
							"source" : cell.get('parent'),
							"target" : cell.get('id'),
					};
					links.push(linkSlC);
					
					//Supervisor (S) > Card (C)
					var linkSC = {
							"sourceType" : "Supervisor",
							"targetType" : "Card",
							"source" : cell.get('SupervisorID'),
							"target" : cell.get('id'),
					};
					links.push(linkSC);
					
				}
				if(cell.get('subType')=== 'Supervisor'){
					app.SupervisorCounter++;
					var supervisor = {
							"type" : "Supervisor",
							"id" : cell.get('id'),
							"name" : cell.attributes.attrs.name.text,
					};
					elements.push(supervisor);
					
					//Slot (Sl) > Supervisor (S)
					var linkSlC = {
							"sourceType" : "Slot",
							"targetType" : "Supervisor",
							"source" : cell.get('parent'),
							"target" : cell.get('id'),
					};
					links.push(linkSlC);

					//setTechnology(equipmentName, equipmentType, equipmentID , tech);

				}
				
				
			});
			
//			console.log('Elements: ' + JSON.stringify(elements));
//			console.log('Links: ' + JSON.stringify(links));

			//execute parse
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "parseEquipToOWL.htm",
			   data: {
				   'elements' : JSON.stringify(elements),
				   'links' : JSON.stringify(links),
			   },
			   success: function(){
				  console.log('PARSE OK!')
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
			
			
//			console.log('Equipment: ' + JSON.stringify(equipment));
//			console.log('Card: ' + JSON.stringify(card));
			
			//ITU Elements
			$.each(graph.getElements(), function(index, cell){
				if(cell.get('subType')=== 'Card'){
					var card = {
							"type" : "Card",
							"id" : cell.get('id'),
							"name" : cell.attributes.attrs.name.text,
							
					};
//					instanciateITUElementsJohn(filename, card);
					instanciateITUElementsJordana(filename, card);
				}
			});
		}		

	};
}

// instancia os elementos ITU no OWL usando os metodos do John (parseEquipToOOWL)
function instanciateITUElementsJohn(filename, card) {

	var ITUelements = [], ITUlinks = [];

	var ITUGraph = loadCardElements(filename, card.id);

	var cardElements = ITUGraph.getElements();
	$.each(cardElements, function(index, element) {
		
		
		//Card_Layer
		if(element.attributes.subtype === 'Card_Layer') {
			
			//console.log('Layer: ' + JSON.stringify(element));
			var layer = {
					"type" : element.attributes.subtype,
					"id" : element.attributes.id,
					"name" : element.attributes.lanes.label,
			};
			ITUelements.push(layer);
			
			//Card > Card_Layer
			var link = {
					"sourceType" : card.type,
					"targetType" : element.attributes.subtype,
					"source" : card.id,
					"target" : element.attributes.id,
			};
			ITUlinks.push(link);
			
		}
		//Trail_Termination_Function
		else if (element.attributes.subtype === 'Trail_Termination_Function') {
			
			var ttf = {
					"type" : element.attributes.subtype,
					"id" : element.attributes.id,
					"name" : element.attributes.attrs.text.text,
			}
			ITUelements.push(ttf);
			
			//Layer > TTF
			var link = {
					"sourceType" : "Card_Layer",
					"targetType" : element.attributes.subtype,
					"source" : element.attributes.parent,
					"target" : element.attributes.id
			}
			ITUlinks.push(link);
		}
		//Adaptation_Function
		else if (element.attributes.subtype === 'Adaptation_Function') {
		
			var af = {
					"type" : element.attributes.subtype,
					"id" : element.attributes.id,
					"name" : element.attributes.attrs.text.text,
			}
			ITUelements.push(af);
			
			//Card_layer > AF
			var link = {
					"sourceType" : card.type,
					"targetType" : element.attributes.subtype,
					"source" : card.id,
					"target" : element.attributes.id
			};
			ITUlinks.push(link);
			
		}
		//Matrix
		else if (element.attributes.subtype === 'Matrix') {
			
			var matrix = {
					"type" : element.attributes.subtype,
					"id" : element.attributes.id,
					"name" : element.attributes.attrs.text.text,
			}
			ITUelements.push(matrix);
			
			//Card_layer > Matrix
			var link = {
					"sourceType" : card.type,
					"targetType" : element.attributes.subtype,
					"source" : card.id,
					"target" : element.attributes.id
			};
			ITUlinks.push(link);
			
		}
		//Input_Card / Output_Card
		else if (element.attributes.subtype === 'Input_Card' || element.attributes.subtype === 'Output_Card') {
			
			var inOut = {
					"type" : element.attributes.subtype,
					"id" : element.attributes.id,
					"name" : element.attributes.attrs.text.text,
			}
			ITUelements.push(inOut);
			
			//Card_layer > Input_Card/Output_Card
			var link = {
					"sourceType" : card.type,
					"targetType" : element.attributes.subtype,
					"source" : card.id,
					"target" : element.attributes.id
			};
			ITUlinks.push(link);
		}
		
	});
	
	var cardLinks = ITUGraph.getLinks();
	$.each(cardLinks, function(index, element) {
		
		var link = {
				"sourceType" : getElementType(cardElements, element.attributes.source.id),
				"targetType" : getElementType(cardElements, element.attributes.target.id),
				"source" : element.attributes.source.id,
				"target" : element.attributes.target.id
		}
		ITUlinks.push(link);
		
	});
	
	console.log('Elements: ' + JSON.stringify(ITUelements));
	console.log('Links: ' + JSON.stringify(ITUlinks));
	
	//execute parse
	$.ajax({
	   type: "POST",
	   async: false,
	   url: "parseEquipToOWL.htm",
	   data: {
		   'elements' : JSON.stringify(ITUelements),
		   'links' : JSON.stringify(ITUlinks),
	   },
	   success: function(){
		  console.log('PARSE OK!')
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
}

//Method to get element type
function getElementType(elements, elementId) {
	
	var type = undefined;
	
	$.each(elements, function(index, element) {
		
		if(element.id == elementId) {
			if(element.attributes.subtype) {
				type = element.attributes.subtype;
			}
			else if(element.subType) {
				type = element.attributes.subType;
			}
		}
		
	});
	
	return type;
}

function loadCardElements(eqName, cardID) {
	var localGraph = new joint.dia.Graph;
	
	$.ajax({
		type: "POST",
		async: false,
		url: "openITUFile.htm",
		data: {
			'path' : eqName,
			'filename' : cardID
		},
		dataType: 'json',
		success: function(data){
			localGraph.fromJSON(data);
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});
	
	return localGraph;
}

// instancia os elementos ITU no OWL usando os metodos da Jordana (insertContainer e performBind)
function instanciateITUElementsJordana(filename, card) {
	var localGraph = loadCardElements(filename, card.id);
	var localApp = {
			cardID : card.id,
			cardName : card.name
	};
	
	loadCells(localGraph, localApp);
}

function loadCells(graph, app) {
	
	loadElements(graph, app);
	loadLinks(graph, app);
}

function loadElements(graph, app) {

	var layers = [];
	var transportFunctions = [];
	
	$.each(graph.getElements(), function(index, element){
		var elementType = element.attributes.type;
		if(elementType === TypeEnum.LAYER) {
			layers[layers.length] = element;
		}
		if(elementType === TypeEnum.TRANSPORT_FUNCTION) {
			transportFunctions[transportFunctions.length] = element;
		}
	});
	
	loadLayers(layers, graph, app);
	loadTransportFunctions(transportFunctions, graph, app);
}

function loadLayers(layers, graph, app) {

	$.each(layers, function(index, layer){
		var layerName = layer.attributes.lanes.label;
		var layerID = layer.id;
		insertLayer(layerID, layerName, app.cardID, app.cardName);
//		app.hideLayer(layerName);
	});
}

function loadTransportFunctions(transportFunctions, graph, app) {
	
	$.each(transportFunctions, function(index, transportFunction){
		
		var tFunctionID = transportFunction.attributes.id;
		var tFunctionType = transportFunction.attributes.subtype;
		var tFunctionName = transportFunction.attributes.attrs.text.text;
//		transportFunction.attr({
//			text: {text: tFunctionName}
//		});
//		nextName(tFunctionType);
		
		var parentID = transportFunction.attributes.parent;
		var parent = graph.getCell(parentID);
		
		if(parent) {
			var parentSubtype = parent.attributes.subtype;
			createTransportFunction(tFunctionID, tFunctionName, tFunctionType, parentID, parentSubtype, 'Card_Layer');
		} else {
			createTransportFunction(tFunctionID, tFunctionName, tFunctionType, app.cardID, app.cardName, 'Card');
		}
	});
}

function loadLinks(graph, app) {
	var links = graph.getLinks();
	$.each(links, function(index, link){
		var linkID = link.attributes.id;
		
		var sourceID = link.attributes.source.id;
		var source = graph.getCell(sourceID);
		var sourceName = source.attributes.attrs.text.text;
		var sourceType = source.attributes.type;
		var sourceSubtype = source.attributes.subtype;
		
		var targetID = link.attributes.target.id;
		var target = graph.getCell(targetID);
		var targetName = target.attributes.attrs.text.text;
		var targetType = target.attributes.type;
		var targetSubtype = target.attributes.subtype;
		
//		if(isInterface(target)) {
//			var portName = getName(targetSubtype);
//			target.attr({
//				text: {text: portName}
//			});
//			nextName(targetSubtype);
//			if(targetSubtype === SubtypeEnum.INPUT) {
//				app.barIn.attributes.embeddedPorts[app.barIn.attributes.embeddedPorts.length] = targetID;
//			} else {
//				app.barOut.attributes.embeddedPorts[app.barOut.attributes.embeddedPorts.length] = targetID;
//			}
//		}
		
		performBind(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID);
	});
}
