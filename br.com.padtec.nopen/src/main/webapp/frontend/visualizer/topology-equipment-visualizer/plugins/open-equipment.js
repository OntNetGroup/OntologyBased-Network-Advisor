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
		   async: false,
		   url: "openEquipment.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){
			   graph.fromJSON(data)
			   loadITUFiles(graph);
			   loadEquipmentstoOWl();
		   },
		   error : function(e) {
			   //alert("error: " + e.status);
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
					instantiateITUElements(filename, cell.get('id'));

				}
				if(cell.get('subType')=== 'Supervisor'){
					app.SupervisorCounter++;
					var supervisor = {
							"type" : "Supervisor",
							"id" : cell.get('id'),
							"name" : cell.attributes.attrs.name.text,
							"tech" : cell.get('tech')
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
		
		function loadITUFiles(graph){
			
			$.each(graph.getElements(), function( index, cell ) {

				if(cell.get('subType') === 'card'){
					$.ajax({
					   type: "POST",
					   async: false,
					   url: "openITUFile.htm",
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
}


