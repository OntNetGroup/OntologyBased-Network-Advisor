function getUrlParameterID(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        console.log(sParameterName);
//        if (sParameterName[0] == sParam) 
//        {
        	return sParameterName[1];
            
//        }
    }
}      

function getUrlParameterName(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        console.log(sParameterName);
//        if (sParameterName[0] == sParam) 
//        {        	
        	return sParameterName[0];
            
//        }
    }
}   

function openFromURL(filename, graph){

	var elementos = parent.topology.model.equipments;
	console.log(elementos);
	$.each(parent.topology.model.equipments, function(index, value){
//		console.log( index + " : " + value);
		 if(index === filename){
//			 console.log("YES");
			 graph.fromJSON(value);
			
		 } 
	}),
	 loadEquipmentstoOWl(filename);
	
//	for(var i=0;i < elementos.length;i++){
//		console.log(elementos[i]);
//		if(elementos[i] === filename){
//			console.log(elementos[i]);
//		}
//	}
	
//	$.ajax({
//		type: "POST",
//		url: "openEquipment.htm",
//		async: false,
//		data: {
//			'filename' : filename
//		},
//		dataType: 'json',
//		success: function(data){
////		$("#filename").val(filename);
//			graph.fromJSON(data);
//			loadEquipmentstoOWl(filename);
//		},
//		error : function(e) {
//			//alert("error: " + e.status);
//		}
//	});
	


	function loadEquipmentstoOWl(filename){
		
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
//				console.log(rack);
				elements.push(rack);
			}
			
			else if(cell.get('subType') === 'Shelf'){
				app.ShelfCounter++;
				var shelf = {
						"type" : "Shelf",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
				};
//				console.log(shelf);
				elements.push(shelf);
				
				//Rack (R) > Shelf (Sh)
				var linkShR = {
						"sourceType" : "Rack",
						"targetType" : "Shelf",
						"source" : cell.get('parent'),
						"target" : cell.get('id'),
				};
//				console.log(linkShR);
				links.push(linkShR);

			}
			else if(cell.get('subType') === 'Slot'){
				app.SlotCounter++;
				var slot = {
						"type" : "Slot",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
				};
//				console.log(slot);
				elements.push(slot);
				
				//Shelf (Sl) > Slot (Sh)
				var linkSlSh = {
						"sourceType" : "Shelf",
						"targetType" : "Slot",
						"source" : cell.get('parent'),
						"target" : cell.get('id'),
				};
//				console.log(linkSlSh);
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
						"sourceType" : "Slot",
						"targetType" : "Supervisor",
						"source" : cell.get('parent'),
						"target" : cell.get('id'),
				};
				links.push(linkSlC);

				//setTechnology(equipmentName, equipmentType, equipmentID , tech);

			}
			
		});
	
		
		console.log('Elements: ' + JSON.stringify(elements));
		console.log('Links: ' + JSON.stringify(links));
			
		
//		//execute parse
//		$.ajax({
//		   type: "POST",
//		   async: false,
//		   url: "parseEquipToOWL.htm",
//		   data: {
//			   'elements' : JSON.stringify(elements),
//			   'links' : JSON.stringify(links),
//		   },
//		   success: function(){
//			  console.log('PARSE OK!')
//		   },
//		   error : function(e) {
//			   alert("error: " + e.status);
//		   }
//		});
		
		//ITU Elements
		$.each(graph.getElements(), function(index, cell){
			if(cell.get('subType')=== 'Card'){
				var card = {
						"type" : "Card",
						"id" : cell.get('id'),
						"name" : cell.attributes.attrs.name.text,
						"data" : cell.attributes.attrs.data
				};
				console.log(card);
//				instanciateITUElements(name, card);
			}
		});
	}


}

		function instanciateITUElements(name, card) {

			var ITUelements = [], ITUlinks = [];
			
			var cardCells = loadCardElements(name, card.id);
			$.each(cardCells, function(index, element) {
				
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
				//Links
				else if(element.attributes.type === 'link') {
					
					var link = {
							"sourceType" : getElementType(cardCells, element.attributes.source),
							"targetType" : getElementType(cardCells, element.attributes.target),
							"source" : element.attributes.source,
							"target" : element.attributes.target
					}
					ITUlinks.push(link);
				}
			});
			
			console.log('Elements: ' + JSON.stringify(ITUelements));
			console.log('Links: ' + JSON.stringify(ITUlinks));
			
			//execute parse
//			$.ajax({
//			   type: "POST",
//			   async: false,
//			   url: "parseEquipToOWL.htm",
//			   data: {
//				   'elements' : JSON.stringify(ITUelements),
//				   'links' : JSON.stringify(ITUlinks),
//			   },
//			   success: function(){
//				  console.log('PARSE OK!')
//			   },
//			   error : function(e) {
//				   alert("error: " + e.status);
//			   }
//			});
		}

		//Method to get element type
		function getElementType(elements, elementId) {
			
			$.each(elements, function(index, element) {
				
				if(element.id == elementId) {
					if(element.subtype) {
						return element.subtype;
					}
					else if(element.subType) {
						return element.subType;
					}
				}
				
			});
			
		}

		function loadCardElements(eqName, cardID) {
			var localGraph = new joint.dia.Graph;
			
			$.ajax({
				type: "POST",
				async: false,
				url: "openITUFileEquipment.htm",
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
			return localGraph.getElements();
		}
		
		function loadITUFiles(graph){
			
			$.each(graph.getElements(), function( index, cell ) {

				if(cell.get('subType') === 'card'){
					$.ajax({
					   type: "POST",
					   async: false,
					   url: "openITUFileEquipment.htm",
					   data: {
						   'path': filename,
						   'filename': cell.id
					   },
					   dataType: 'json',
					   success: function(data){
						   cardArray[cell.id] = data;
					   },
					   error : function(e) {
						   //alert("error: " + e.status);
					   }
					});
				}
				
			});	
			
		}	



