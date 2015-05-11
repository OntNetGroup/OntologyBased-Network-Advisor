function generateSaveTemplateDialog(graph){
	
	var content = '<div id="save-dialog" title="Save Template">'
		+ 'Name: <input type="text" id="save-filename" value="' + $('#filename').val() + '"/>'
		+ '</div>'
		+ '<div id="name-error-message">' + 'Name cannot be empty!' + '</div>';
		
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Save Template',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'save', content: 'Save', position: 'left' }
		]
	});
	
	dialog.on('action:save', checkTemplateFile);
	dialog.on('action:cancel', cancel);

	dialog.open();
	
	function cancel(){
		dialog.close();
	}
	
	function checkTemplateFile(){
		
		if($("#save-filename").val() == ""){
			$('#name-error-message').show();
		}
		else{
			$.ajax({
			   type: "POST",
			   url: "checkTemplateFile.htm",
			   data: {
				 'filename': $("#save-filename").val(),
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
						    saveTemplate();
					   } 
				   }
				   else{
					   saveTemplate();
				   }
			   },
			   error : function(e) {
				   alert("error: " + e.status);
				   dialog.close();
			   }
			});
		}
		
	};
	
	function saveTemplate(){
		
		$('#filename').val($("#save-filename").val());
		
		$.ajax({
		   type: "POST",
		   url: "saveTemplate.htm",
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