function getUrlParameterID(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        	return sParameterName[1];
    }
};      

function getUrlParameterName(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');    	
        	return sParameterName[0];
    }
};   

function openFromURL(filename, graph){
	var elementos = parent.topology.model.equipments;
	$.each(parent.topology.model.equipments, function(index, value){
		 if(index === filename){
			 graph.fromJSON(value);
		 } 
	});
};

		


