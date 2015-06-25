function generateSaveEquipmentDialog(graph){
	
	var content = '<div id="save-dialog" title="Save Equipment">'
		+ 'Input the supervisor name of the equipment that you wish to save: <input type="text" id="save-filename" value="' + $('#filename').val() + '"/>'
		+ '</div>'
		+ '<div id="name-error-message">' + 'Please input a valid supervisor name!' + '</div>';
		
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Save Equipment',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'save', content: 'Save', position: 'left' }
		]
	});
	
	dialog.on('action:save', checkEquipmentFile);
	dialog.on('action:cancel', cancel);

	dialog.open();
	
	function cancel(){
		dialog.close();
	}
	
	function checkEquipmentFile(){
		
		if($("#save-filename").val() == ""){
			$('#name-error-message').show();
		}
		else{
			$.ajax({
			   type: "POST",
			   url: "checkEquipmentFile.htm",
			   data: {
				 'filename': $("#save-filename").val(),
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
//						   	copyITUFiles();
//						   	deleteITUFiles();
						   var originalGraph = graph.toJSON();
						   equipmentGraph()
						   saveEquipment();
						   graph.fromJSON(originalGraph);
						  
					   } 
				   }
				   else{
//					   copyITUFiles();
//					   deleteITUFiles();
					   var originalGraph = graph.toJSON();
					   equipmentGraph()
					   saveEquipment();
					   graph.fromJSON(originalGraph);
					   
				   }
			   },
			   error : function(e) {
				   alert("error: " + e.status);
				   dialog.close();
			   }
			});
		}
		
	};
	
//	function copyITUFiles(){
//		
//		if($("#filename").val() != $("#save-filename").val()){
//			$.ajax({
//			   type: "POST",
//			   async: false,
//			   url: "copyITUFiles.htm",
//			   data: {
//				 'oldPath': $("#filename").val(),
//				 'newPath': $("#save-filename").val(),
//			   },
//			   success: function(){
//				   deletedITUFiles = [];
//				   index = 0;
//			   },
//			   error : function(e) {
//				   //alert("error: " + e.status);
//			   }
//			});
//		}
//		
//	}
	
//	function deleteITUFiles(){
//		
//		$.each(deletedITUFiles, function(index, value){
//			
//			$.ajax({
//			   type: "POST",
//			   async: false,
//			   url: "deleteITUFile.htm",
//			   data: {
//				 'path': $("#save-filename").val(),
//				 'filename': value,
//			   },
//			   success: function(){
//				   deletedITUFiles = [];
//				   index = 0;
//			   },
//			   error : function(e) {
//				   //alert("error: " + e.status);
//			   }
//			});
//			
//		});
//		
//	}
	
	function saveEquipment(){
		
		$('#filename').val($("#save-filename").val());
		
		
		var saveDialog = new joint.ui.Dialog({
			type: 'neutral' ,
			width: 420,
			draggable: false,
			title: 'Equipment Saved! ',
			content: 'The Equipment was saved',
			open: function() {}
		});
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "saveEquipment.htm",
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
		
	}
	
	function equipmentGraph(){
		
		//JSON.stringify(graph.toJSON())
		console.log($("#save-filename").val());
		// originalGraph = graph.toJSON();
		var equipament = graph;
		console.log('equipmanet' , equipament);
		var elementos = equipament.getElements();
		console.log('elementos' , elementos);
		var equipSupervisor;
		var c = [];
        var movingCards = [];
		
		for(var i = 0; i < elementos.length; i++){
			var check = elementos[i];
			//console.log(check);
			if((check.attributes.subType) === 'Supervisor'){
               if((check.attributes.attrs.name.text === $("#save-filename").val())){
//            	   equipsupervisor = equipament.getCell(check.id);
               }else{
            	   var parent = check.attributes.parent;
            	   var grandParent = (equipament.getCell(parent)).attributes.parent;
            	   console.log(grandParent);
            	   (equipament.getCell(check.id)).remove();
            	   (equipament.getCell(parent)).remove();
            	   if ((equipament.getCell(grandParent)).getEmbeddedCells().length === 0){
            		   (equipament.getCell(grandParent)).remove();
            		   
            	   }
               }
			}
			if((check.attributes.subType) === 'Card'){
				if((check.attributes.Supervisor === $("#save-filename").val())){
	            	   
	               }else{
	            	   var parent = check.attributes.parent;
	            	   var grandParent = (equipament.getCell(parent)).attributes.parent;
	            	   console.log(grandParent);
	            	   (equipament.getCell(check.id)).remove();
	            	   (equipament.getCell(parent)).remove();
	            	   if ((equipament.getCell(grandParent)).getEmbeddedCells().length === 0){
	            		   (equipament.getCell(grandParent)).remove();
	            		   
	            	   }
	               }
			} 
		};		
		console.log(equipament);		
	}
	
	
	
}