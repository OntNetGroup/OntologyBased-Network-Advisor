function getUrlParameter(sParam)
{
	var sPageURL = window.location.search.substring(1);
	console.log(sPageURL);
	var sURLVariables = sPageURL.split('&');
	console.log(sURLVariables);
	for (var i = 0; i < sURLVariables.length; i++) 
	{
		var sParameterName = sURLVariables[i].split('=');
		console.log(sParameterName);
			if (sParameterName[0] == sParam) 
			{
				console.log(sParam);
				console.log(sParameterName[0]);
				console.log(sParameterName[1]);
				return sParameterName[1];
			}
		
		
	}
}      

function openFromURL(equipment, filename, graph){

	console.log('equipment: ' + equipment);
	console.log('filename: ' + filename);

	var elementos = parent.app.graph.attributes.cells.models;
	console.log(elementos);
	$.each(elementos, function(index, value){
		console.log( index + " : " + value);
		if(value.id === filename){
			console.log('found card!');
			graph.fromJSON(value.attributes.attrs.data);
		}
//		if(index === filename){
//		console.log("YES");
////		graph.fromJSON(value);
//		} 
	})
}

function openFromURLVisualizer(equipment, filename , graph){

	console.log('equipment: ' + equipment);
	console.log('filename: ' + filename);

//	var elementos = parent.app.graph.attributes.cells.models;
//	console.log(elementos);
//	$.each(elementos, function(index, value){
//		console.log( index + ": " + value);
//		if(value.id === filename){
//			console.log('found card!');
//			graph.fromJSON(value.attributes.attrs.data);

//		}
//	})

	$.ajax({
	type: "POST",
	async: false,
	url: "openITUFileEquipment.htm",
	data: {
	'path' : equipment,
	'filename' : filename
	},
	dataType: 'json',
	success: function(data){
	graph.fromJSON(data);
	},
	error : function(e) {
	//alert("error: " + e.status);
	}
	});

}
