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
			loadElements(graph, app);
		},
		error : function(e) {
			//alert("error: " + e.status);
		}
	});

	var cardID = app.cardID;
	var cardName = app.cardName;
	var cardTech = app.cardTech;
	
	function loadElements(graph, app) {
		
		$.each(graph.getElements(), function(index, element){
			console.log(element.attributes.subtype);
			
			var elementID = element.attributes.id;
			var elementType = element.attributes.type;
			var elementSubtype = element.attributes.attrs.subtype;
			var elementParentID = element.attributes.parent;
			
			if(elementType === TypeEnum.LAYER) {
				insertContainer(elementSubtype, 'layer', cardID, cardName, cardTech);
				//TODO: hide layer from stencil
			}
			if(elementType === TypeEnum.TRANSPORT_FUNCTION) {
				var elementParent = graph.getCell(elementParentID);
				var elementName = element.attributes.attrs.text.text;
				if(elementParent) {
					var elementParentSubtype = elementParent.attributes.subtype;
					createTransportFunction(elementID, elementName, elementSubtype, elementParentSubtype, 'layer', cardID);
				} else {
					createTransportFunction(elementID, elementName, elementSubtype, cardName, cardTech, cardID);
				}
			}
			if(elementSubtype === 'in' || elementSubtype === 'out') {
				var elementName = element.attributes.attrs.text.text;
				createPort(elementID, elementName, elementSubtype, tFunctionID, tFunctionName, tFunctionType);
			}
		});
	}
}














