function getEquipments(graph){
	
	$.ajax({
	   type: "GET",
	   url: "getAllEquipments.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOpenEquipmentDialog(graph, data)
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
}

function generateOpenEquipmentDialog(graph, data){
	
	var content = '<form id="open">';
	for(var i = 0; i < Object.keys(data).length; i++){
		if(i == 0){
			content = content + '<input type="radio" name="equipment" value="' + data[i].equipment + '" checked>' 
					+ '<label>' + data[i].equipment + '</label> <br>';
		}
		else{
			content = content + '<input type="radio" name="equipment" value="' + data[i].equipment + '">' 
					+ '<label>' + data[i].equipment + '</label><br>';
		}

	}
	content = content +  '</form>';
	
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Open Equipment',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'open', content: 'Open', position: 'left' }
		]
	});
	dialog.on('action:open', openEquipment);
	dialog.on('action:cancel', dialog.close);
	dialog.open();

	function openEquipment(){
		
		var filename = $('input[name=equipment]:checked', '#open').val();
		
		$.ajax({
		   type: "POST",
		   url: "openEquipment.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){
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