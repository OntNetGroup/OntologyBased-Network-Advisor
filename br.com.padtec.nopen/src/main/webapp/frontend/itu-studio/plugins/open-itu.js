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
			var layerName = layer.attributes.subtype;
			var layerID = layer.id;
			insertLayer(layerID, layerName, app.cardID, app.cardName);
			app.hideLayer(layerName);
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
