function getProvisioning(graph){
	
	$.ajax({
	   type: "GET",
	   url: "getAllProvisioning.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOpenProvisioningDialog(graph, data)
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
	   url: "openProvisioning.htm",
	   data: {
		   'filename' : filename
	   },
	   dataType: 'json',
	   success: function(data){
		   $("#filename").val(filename);
		   graph.fromJSON(data);
	   },
	   error : function(e) {
		   //alert("error: " + e.status);
	   }
	});
	
}

function generateOpenProvisioningDialog(graph, data){
	
	var content = '<form id="open">';
	for(var i = Object.keys(data).length-1; i >= 0; i--){
		if(i == Object.keys(data).length-1){
			content = content + '<input type="radio" name="provisioning" value="' + data[i].provisioning + '" checked>' 
					+ '<label>' + data[i].provisioning + '</label> <br>';
		}
		else{
			content = content + '<input type="radio" name="provisioning" value="' + data[i].provisioning + '">' 
					+ '<label>' + data[i].provisioning + '</label><br>';
		}

	}
	content = content +  '</form>';
	
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Open Provisioning',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'open', content: 'Open', position: 'left' }
		]
	});
	dialog.on('action:open', openProvisioning);
	dialog.on('action:cancel', dialog.close);
	dialog.open();

	function openProvisioning(){
		
		var filename = $('input[name=provisioning]:checked', '#open').val();
		
		$.ajax({
		   type: "POST",
		   url: "openProvisioning.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){ 		   
			   graph.fromJSON(data);
			   dialog.close();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
			   dialog.close();
		   }
		});
		
	};
}