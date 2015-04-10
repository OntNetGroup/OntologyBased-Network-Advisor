function generateSaveTopologyDialog(graph){
	
	var content = '<div id="save-dialog" title="Save Topology">'
		+ 'Name: <input type="text" id="save-filename" value="' + $('#filename').val() + '"/>'
		+ '</div>'
		+ '<div id="name-error-message">' + 'Name cannot be empty!' + '</div>';
		
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Save Topology',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'save', content: 'Save', position: 'left' }
		]
	});
	
	dialog.on('action:save', checkTopologyFile);
	dialog.on('action:cancel', cancel);

	dialog.open();
	
	function cancel(){
		dialog.close();
	}
	
	function checkTopologyFile(){
		
		if($("#save-filename").val() == ""){
			$('#name-error-message').show();
		}
		else{
			$.ajax({
			   type: "POST",
			   url: "checkTopologyFile.htm",
			   data: {
				 'filename': $("#save-filename").val(),
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
						    saveTopology();
					   } 
				   }
				   else{
					   saveTopology();
				   }
			   },
			   error : function(e) {
				   alert("error: " + e.status);
				   dialog.close();
			   }
			});
		}
		
	};
	
	function saveTopology(){
		
		$('#filename').val($("#save-filename").val());
		
		$.ajax({
		   type: "POST",
		   url: "saveTopology.htm",
		   data: {
			 'filename': $("#save-filename").val(),
			 'graph': JSON.stringify(graph.toJSON()),
		   },
		   success: function(){ 		   
			   alert($("#save-filename").val() + ' saved successfully!');
			   dialog.close();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
			   dialog.close();
		   }
		});
		
		
	};
	
}

function checkNodeEquipments(graph){
	
	var elements = graph.getElements();
	
	for(var i = 0; i < elements.length; i++){
		if(elements[i].attr('equipment/template') == ""){
			alert("all nodes need to be matched to an equipment.")
			return false;
		}
	}
	
	return true;
}