function getTopologies(graph){
	
	$.ajax({
	   type: "GET",
	   url: "getAllTopologies.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOpenTopologyDialog(graph, data)
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
}

function generateOpenTopologyDialog(graph, data){
	
	var content = '<form id="open">';
	for(var i = Object.keys(data).length-1; i >= 0; i--){
		if(i == Object.keys(data).length-1){
			content = content + '<input type="radio" name="topology" value="' + data[i].topology + '" checked>' 
					+ '<label>' + data[i].topology + '</label> <br>';
		}
		else{
			content = content + '<input type="radio" name="topology" value="' + data[i].topology + '">' 
					+ '<label>' + data[i].topology + '</label><br>';
		}

	}
	content = content +  '</form>';
	
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Open Topology',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'open', content: 'Open', position: 'left' }
		]
	});
	dialog.on('action:open', openTopology);
	dialog.on('action:cancel', dialog.close);
	dialog.open();

	function openTopology(){
		
		var filename = $('input[name=topology]:checked', '#open').val();
		
		$.ajax({
		   type: "POST",
		   url: "openTopology.htm",
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