function getTemplates(graph){
	
	$.ajax({
	   type: "GET",
	   url: "getAllTemplates.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOpenTemplateDialog(graph, data)
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
}

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
		   url: "openTemplate.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){
			   $("#filename").val(filename);
			   graph.fromJSON(data);
			   loadITUFiles(graph);
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

function generateOpenTemplateDialog(graph, data){
	
	var content = '<form id="open">';
	for(var i = 0; i < Object.keys(data).length; i++){
		if(i == 0){
			content = content + '<input type="radio" name="template" value="' + data[i].template + '" checked>' 
					+ '<label>' + data[i].template + '</label> <br>';
		}
		else{
			content = content + '<input type="radio" name="template" value="' + data[i].template + '">' 
					+ '<label>' + data[i].template + '</label><br>';
		}

	}
	content = content +  '</form>';
	
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Open Template',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'open', content: 'Open', position: 'left' }
		]
	});
	dialog.on('action:open', openTemplate);
	dialog.on('action:cancel', dialog.close);
	dialog.open();

	function openTemplate(){
		
		var filename = $('input[name=template]:checked', '#open').val();
		
		$.ajax({
		   type: "POST",
		   url: "openTemplate.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){
			   $("#filename").val(filename);
			   graph.fromJSON(data);
			   loadITUFiles(graph);
			   dialog.close();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
			   dialog.close();
		   }
		});
		
		function loadITUFiles(graph){
			
			$.each(graph.getElements(), function( index, cell ) {

				if(cell.get('subType') === 'card'){
					$.ajax({
					   type: "POST",
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
						   dialog.close();
					   }
					});
				}

				
			});
			
		}
		
	};
}
