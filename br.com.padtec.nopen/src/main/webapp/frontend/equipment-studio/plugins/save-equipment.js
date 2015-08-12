function generateSaveEquipmentDialog(graph){
	
	var equipaments = graph.getElements();
	var savedSupervisors = [];
	for(var i = 0; i < equipaments.length; i++){	
		if((equipaments[i].attributes.subType) === 'Supervisor'){
		  savedSupervisors.push(equipaments[i]);
		}
	}
	console.log(savedSupervisors);
	
	var content = '<div id="save-dialog" title="Save Equipment">'
		+ 'Equipment: <select>';
	for(var i = 0; i < savedSupervisors.length; i++){
		if(i == 0){
			content += '<option value="'+savedSupervisors[i].attributes.attrs.name.text +'">'+savedSupervisors[i].attributes.attrs.name.text+'</option>';
		}
		else{
			content += '<option value="'+savedSupervisors[i].attributes.attrs.name.text +'">'+savedSupervisors[i].attributes.attrs.name.text+'</option>';
		}
	}
	content += '</select>';
	+ '</div>'
		
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
	
$('#save-dialog').find(":selected").val();
	
	dialog.on('action:save', checkEquipmentFile);
	dialog.on('action:cancel', cancel);

	dialog.open();
	
	function cancel(){
		dialog.close();
	}
	
	function checkEquipmentFile(){

			$.ajax({
			   type: "POST",
			   url: "checkEquipmentFile.htm",
			   data: {
				 'filename': $('#save-dialog').find(":selected").val(),
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
						   	copyITUEquipmentFiles();
//						   	deleteITUEquipmentFiles();
						   var originalGraph = graph.toJSON();
						   equipmentGraph()
						   saveEquipment();
						   graph.fromJSON(originalGraph);
						  
					   } 
				   }
				   else{
					  copyITUEquipmentFiles();
//					  deleteITUEquipmentFiles();
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
		
	};
	
	function copyITUEquipmentFiles(){

		$.ajax({
		   type: "POST",
		   async: false,
		   url: "copyITUEquipmentFiles.htm",
		   data: {
			 'oldPath': $("#filename").val(),
			 'newPath': $('#save-dialog').find(":selected").val(),
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
//	
//	function deleteITUEquipmentFiles(){
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
			 'filename': $('#save-dialog').find(":selected").val(),
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
		console.log($('#save-dialog').find(":selected").val());
		// originalGraph = graph.toJSON();
		var equipment = graph;
	//	console.log('equipmanet' , equipament);
		var elementos = equipment.getElements();
	//	console.log('elementos' , elementos);
		var equipSupervisor;
		var c = [];
        var movingCards = [];
		
		for(var i = 0; i < elementos.length; i++){
			var check = elementos[i];
			//console.log(check);
			if((check.attributes.subType) === 'Supervisor'){
               if((check.attributes.attrs.name.text === $('#save-dialog').find(":selected").val())){
//            	   equipsupervisor = equipament.getCell(check.id);
               }else{
            	   var parent = check.attributes.parent;
            	   var grandParent = (equipment.getCell(parent)).attributes.parent;
            	   console.log(grandParent);
            	   (equipment.getCell(check.id)).remove();
            	   (equipment.getCell(parent)).remove();
            	   if ((equipment.getCell(grandParent)).getEmbeddedCells().length === 0){
            		   (equipment.getCell(grandParent)).remove();
            		   
            	   }
               }
			}
			if((check.attributes.subType) === 'Card'){
				if((check.attributes.Supervisor === $('#save-dialog').find(":selected").val())){
	            	   movingCards.push(check.id);
	               }else{
	            	   var parent = check.attributes.parent;
	            	   var grandParent = (equipment.getCell(parent)).attributes.parent;
	            	   console.log(grandParent);
	            	   (equipment.getCell(check.id)).remove();
	            	   (equipment.getCell(parent)).remove();
	            	   if ((equipment.getCell(grandParent)).getEmbeddedCells().length === 0){
	            		   (equipment.getCell(grandParent)).remove();
	            		   
	            	   }
	               }
			} 
			
			if((check.attributes.subType) === 'Slot'){
				if(check.getEmbeddedCells().length === 0){
					var parent = check.attributes.parent;
					(equipment.getCell(check.id)).remove();					
					 if ((equipment.getCell(parent)).getEmbeddedCells().length === 0){
	            		   (equipment.getCell(parent)).remove();   
	            	   }
				}
			}
			
			if((check.attributes.subType) === 'Shelf'){
				if(check.getEmbeddedCells().length === 0){
					(equipment.getCell(check.id)).remove();
				}
			}
		};		
		console.log(equipment);		
	}
	
	
	
}