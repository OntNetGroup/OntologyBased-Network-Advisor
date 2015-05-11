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
			loadElements(graph);
		},
		error : function(e) {
			//alert("error: " + e.status);
		}
	});
	
	function loadElements(graph) {
		
		$.each(graph.getElements(), function(index, element){
			console.log(element.attributes.subtype);
			
			var elementID = element.attributes.id;
			var elementType = element.attributes.type;
			var elementSubtype = element.attributes.attrs.subtype;
			
			if(elementType === TypeEnum.LAYER) {
				
			}
		});
	}
}