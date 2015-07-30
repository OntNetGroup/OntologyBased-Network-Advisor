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

function openFromURL(equipment, filename, graph, app){

	$.ajax({
		type: "POST",
		async: false,
		url: "openITUFile.htm",
		data: {
			'path' : equipment,
			'filename' : filename
		},
		dataType: 'json',
		success: function(data){
			graph.fromJSON(data);
			loadCells(graph, app);
		},
		error : function(e) {
			//alert("error: " + e.status);
		}
	});

	// actually, now this procedure is used only to hide from the stencil the layers already on canvas and increase the element counters
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
//			insertLayer(layerID, layerName, app.cardID, app.cardName); all instatiation was made when EquipStudio was started
//			app.hideLayer(layerName);
		});
	}
	
	function loadTransportFunctions(transportFunctions, graph, app) {
		
		$.each(transportFunctions, function(index, transportFunction){
			
			var tFunctionID = transportFunction.attributes.id;
			var tFunctionType = transportFunction.attributes.subtype;
			var tFunctionName = getName(tFunctionType);
			transportFunction.attr({
				text: {text: tFunctionName}
			});
			nextName(tFunctionType);
			
//			var parentID = transportFunction.attributes.parent;
//			var parent = graph.getCell(parentID);
			
			
			
//			if(parent) { all instatiation was made when EquipStudio was started
//				var parentSubtype = parent.attributes.subtype;
//				createTransportFunction(tFunctionID, tFunctionName, tFunctionType, parentID, parentSubtype, 'Card_Layer');
//			} else {
//				createTransportFunction(tFunctionID, tFunctionName, tFunctionType, app.cardID, app.cardName, 'Card');
//			}
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
			
			if(isInterface(target)) {
				var portName = getName(targetSubtype);
				target.attr({
					text: {text: portName}
				});
    			nextName(targetSubtype);
    			if(targetSubtype === SubtypeEnum.INPUT) {
    				app.barIn.attributes.embeddedPorts[app.barIn.attributes.embeddedPorts.length] = targetID;
    			} else {
    				app.barOut.attributes.embeddedPorts[app.barOut.attributes.embeddedPorts.length] = targetID;
    			}
			}
			
//			performBind(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID); all instatiation was made when EquipStudio was started
		});
	}
	
	// Get name for properly element being added
	function getName(elementSubtype) {
		if(elementSubtype === SubtypeEnum.INPUT) return 'in_' +app.inPortCounter;
		if(elementSubtype === SubtypeEnum.OUTPUT) return 'out_' +app.outPortCounter;
		if(elementSubtype === SubtypeEnum.ADAPTATION_FUNCTION) return 'AF_' +app.AFCounter;
		if(elementSubtype === SubtypeEnum.TRAIL_TERMINATION_FUNCTION) return 'TTF_' +app.TTFCounter;
		if(elementSubtype === SubtypeEnum.MATRIX) return 'Matrix_' +app.MatrixCounter;
	};
	
	// Increment the counter of the properly element
	function nextName(elementSubtype) {
		if(elementSubtype === SubtypeEnum.INPUT) app.inPortCounter++;
		if(elementSubtype === SubtypeEnum.OUTPUT) app.outPortCounter++;
		if(elementSubtype === SubtypeEnum.ADAPTATION_FUNCTION) app.AFCounter++;
		if(elementSubtype === SubtypeEnum.TRAIL_TERMINATION_FUNCTION) app.TTFCounter++;
		if(elementSubtype === SubtypeEnum.MATRIX) app.MatrixCounter++;
	};
	
	//Check if cell is an interface
	function isInterface(cell) {
		var cellSubType = cell.attributes.subtype;
		if(cellSubType === SubtypeEnum.OUTPUT || cellSubType === SubtypeEnum.INPUT) return true;
	};
}
