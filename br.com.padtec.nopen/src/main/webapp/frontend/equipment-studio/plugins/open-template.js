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
						"sourceType" : "Shelf",
						"targetType" : "Rack",
						"source" : cell.get('id'),
						"target" : cell.get('parent'),
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
				
				//Slot (Sl) > Shelf (Sh)
				var linkSlSh = {
						"sourceType" : "Slot",
						"targetType" : "Shelf",
						"source" : cell.get('id'),
						"target" : cell.get('parent'),
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
				
				/*
				 * TODO: load ITU elements of this card on the ontology
				*/
//				instantiateITUElements(filename, cell.get('id'));

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
			
			
//			console.log('Equipment: ' + JSON.stringify(equipment));
//			console.log('Card: ' + JSON.stringify(card));
				
		});
	}
	function loadEquipments(filename){

		$.each(graph.getElements(), function(index, cell){

			if(cell.get('subType') === 'Rack'){
            //    console.log(cell.attributes);
				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;				
				app.RackCounter++
				
//				insertEquipmentholder(equipmentName , equipmentType, equipmentID);
				EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID);

			};
			if(cell.get('subType') === 'Shelf'){
				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.ShelfCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				
//				insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
				EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

			}
			if(cell.get('subType') === 'Slot'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.SlotCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				
//				insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
				EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

			}

			if(cell.get('subType')=== 'Card'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.CardCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				
//				insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
				EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
				
				/*
				 * TODO: load ITU elements of this card on the ontology
				*/
//				instantiateITUElements(filename, equipmentID);

			}
			if(cell.get('subType')=== 'Supervisor'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.SupervisorCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				 
				console.log(equipmentID , equipmentType , equipmentName , parentID , containerType , containerName);
				
//				insertSupervisor(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
				EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

				//setTechnology(equipmentName, equipmentType, equipmentID , tech);

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
				//loadEquipments();
				loadEquipmentstoOWl();
				dialog.close();
			},
			error : function(e) {
				alert("error: " + e.status);
				dialog.close();
			}
		});

		
		function loadEquipmentstoOWl(){
			
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
							"sourceType" : "Shelf",
							"targetType" : "Rack",
							"source" : cell.get('id'),
							"target" : cell.get('parent'),
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
					
					//Slot (Sl) > Shelf (Sh)
					var linkSlSh = {
							"sourceType" : "Slot",
							"targetType" : "Shelf",
							"source" : cell.get('id'),
							"target" : cell.get('parent'),
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
					
					/*
					 * TODO: load ITU elements of this card on the ontology
					*/
//					instantiateITUElements(filename, cell.get('id'));

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
				
				
//				console.log('Equipment: ' + JSON.stringify(equipment));
//				console.log('Card: ' + JSON.stringify(card));
					
			});
		}
		
		function loadEquipments(){

			$.each(graph.getElements(), function(index, cell){

				if(cell.get('subType') === 'Rack'){
	                console.log(cell.attributes);
					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;				
					app.RackCounter++
					
//					insertEquipmentholder(equipmentName , equipmentType, equipmentID);
					EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID);

				};
				if(cell.get('subType') === 'Shelf'){
					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.ShelfCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
//					insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
					EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

				}
				if(cell.get('subType') === 'Slot'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.SlotCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
//					insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
					EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

				}

				if(cell.get('subType')=== 'Card'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.CardCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
//					insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
					EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

				}
				if(cell.get('subType')=== 'Supervisor'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.SupervisorCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
//					insertSupervisor(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
					EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

					//setTechnology(equipmentType, equipmentID , tech);

				}
			});
		}

	};
}

function instantiateITUElements(cardName, cardID) {
	var localGraph = new joint.dia.Graph;
	var localApp = {
			cardName : cardName,
			cardID : cardID
	};
	
	$.ajax({
		type: "POST",
		async: false,
		url: "openITUFile.htm",
		data: {
			'path' : cardName,
			'filename' : cardID
		},
		dataType: 'json',
		success: function(data){
			localGraph.fromJSON(data);
			loadCells(localGraph, localApp);
		},
		error : function(e) {
//			alert("error: " + e.status);
		}
	});
	
	function loadCells(graph, app) {
		
		loadElements(graph, app);
		loadLinks(graph);
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
//			app.hideLayer(layerName);
		});
	}
	
	function loadTransportFunctions(transportFunctions, graph, app) {
		
		$.each(transportFunctions, function(index, transportFunction){
			
			var tFunctionID = transportFunction.attributes.id;
			var tFunctionName = transportFunction.attributes.attrs.text.text;
			var tFunctionType = transportFunction.attributes.subtype;
			
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
	
	function loadLinks(graph) {
		$.each(graph.getLinks(), function(index, link){
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
			
			performBind(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID);
		});
	}
}
