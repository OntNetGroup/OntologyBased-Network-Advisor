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
						   	copyITUFiles();
						   	deleteITUFiles();
						    saveTemplate();
					   } 
				   }
				   else{
					   //copyITUFiles();
					   //deleteITUFiles();
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
	
	function copyITUFiles(){
		
		if($("#filename").val() != $("#save-filename").val()){
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "copyITUFiles.htm",
			   data: {
				 'oldPath': $("#filename").val(),
				 'newPath': $("#save-filename").val(),
			   },
			   success: function(){
				   deletedITUFiles = [];
				   index = 0;
			   },
			   error : function(e) {
				   //alert("error: " + e.status);
			   }
			});
		}
		
	}
	
	function deleteITUFiles(){
		
		$.each(deletedITUFiles, function(index, value){
			
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "deleteITUFile.htm",
			   data: {
				 'path': $("#save-filename").val(),
				 'filename': value,
			   },
			   success: function(){
				   deletedITUFiles = [];
				   index = 0;
			   },
			   error : function(e) {
				   //alert("error: " + e.status);
			   }
			});
			
		});
		
	}
	
	function saveTemplate(){
		
		$('#filename').val($("#save-filename").val());
		
		var saveDialog = new joint.ui.Dialog({
			type: 'neutral' ,
			width: 420,
			draggable: false,
			title: 'Template Saved! ',
			content: 'The template was saved!!',
			open: function() {}
		});
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "saveTemplate.htm",
		   data: {
			 'filename': $("#save-filename").val(),
			 'graph': JSON.stringify(graph.toJSON()),
		   },
		   success: function(){ 		   
			   saveDialog.open();
			   dialog.close();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
			   dialog.close();
		   }
		});
		
	};
	
}