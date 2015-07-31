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
			   $("#filename").val(filename);
			   graph.fromJSON(data)
//			   loadITUFiles(graph);
		   },
		   error : function(e) {
			   //alert("error: " + e.status);
		   }
		});
		
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


