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

	function loadCells(graph, app) {
		
		loadElements(graph, app);
		loadLinks(graph);
	}
	
	function loadElements(graph, app) {

		var layers = [];
		var transportFunctions = [];
		
		$.each(graph.getElements(), function(element){
			var elementType = element.attributes.type;
			if(elementType === TypeEnum.LAYER) {
				layers.append(element);
			}
			if(elementType === TypeEnum.TRANSPORT_FUNCTION) {
				transportFunctions.append(element);
			}
		});
		
		loadLayers(layers, graph, app);
		loadTransportFunctions(transportFunctions, graph, app);
	}
	
	function loadLayers(layers, graph, app) {

		$.each(layers, function(layer){
			var layerSubtype = element.attributes.attrs.subtype;
			insertContainer(layerSubtype, 'layer', app.cardID, app.cardName, app.cardTech);
			app.hideLayer(layerSubtype);
		});
	}
	
	function loadTransportFunctions(transportFunctions, graph, app) {
		
		$.each(transportFunctions, function(transportFunction){
			
			var tFunctionID = transportFunction.attributes.id;
			var tFunctionName = transportFunction.attributes.attrs.text.text;
			var tFunctionType = transportFunction.attributes.attrs.subtype;
			
			var parentID = transportFunction.attributes.parent;
			var parent = graph.getCell(parentID);
			
			if(parent) {
				var parentSubtype = parent.attributes.subtype;
				createTransportFunction(tFunctionID, tFunctionName, tFunctionType, parentSubtype, 'layer', app.cardID);
			} else {
				createTransportFunction(tFunctionID, tFunctionName, tFunctionType, app.cardName, app.cardTech, app.cardID);
			}
		});
	}
	
	function loadLinks(graph) {
		$.each(graph.getLinks(), function(link){
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
			
			if(targetType === TypeEnum.TRANSPORT_FUNCTION) {
				createLink(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID);
			} else {
				createPort(targetID, targetName, targetSubtype, sourceID, sourceName, sourceSubtype);
			}
		});
	}
}
