function generateSaveProvisioningDialog(graph){
	
	var content = '<div id="save-dialog" title="Save Provisioning">'
		+ 'Name: <input type="text" id="save-filename" value="' + $('#filename').val() + '"/>'
		+ '</div>'
		+ '<div id="name-error-message">' + 'Name cannot be empty!' + '</div>';
		
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Save Provisioning',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'save', content: 'Save', position: 'left' }
		]
	});
	
	dialog.on('action:save', checkProvisioningFile);
	dialog.on('action:cancel', cancel);

	dialog.open();
	
	function cancel(){
		dialog.close();
	}
	
	function checkProvisioningFile(){
		
		if($("#save-filename").val() == ""){
			$('#name-error-message').show();
		}
		else{
			$.ajax({
			   type: "POST",
			   url: "checkProvisioningFile.htm",
			   data: {
				 'filename': $("#save-filename").val(),
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
						    saveProvisioning();
					   } 
				   }
				   else{
					   saveProvisioning();
				   }
			   },
			   error : function(e) {
				   alert("error: " + e.status);
				   dialog.close();
			   }
			});
		}
		
	};
	
	function saveProvisioning(){
		
		$('#filename').val($("#save-filename").val());
		
		$.ajax({
		   type: "POST",
		   url: "saveProvisioning.htm",
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

