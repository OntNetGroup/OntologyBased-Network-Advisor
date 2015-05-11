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

function openFromURL(equipment, filename, graph){

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
		},
		error : function(e) {
			//alert("error: " + e.status);
		}
	});
}