function insertEquipmentholder( equipmentType, equipmentID , containerType , containerID) {

	var result = "error";


	var dtoEquipmentholder = {
			"id": equipmentID ,
			"type": equipmentType 
	};
	
	var dtoContainer = {
			"id": containerID ,
			"type": containerType 
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "insertEquipmentholder.htm",
		data: {
			'equipmentholder': JSON.stringify(dtoEquipmentholder),
			'container': JSON.stringify(dtoContainer)
		} ,
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result;
};


function insertCard( cardName , cardType, cardID , slotName , slotType , slotID) {

	var result = "error";


	var dtoCard = {
			"name" : cardName ,
			"id": cardID ,
			"type": cardType 
	};
	
	var dtoSlot = {
			"name": slotName ,
			"id": slotID ,
			"type": slotType 
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "insertCard.htm",
		data: {
			'card': JSON.stringify(dtoCard),
			'slot': JSON.stringify(dtoSlot)
		} ,
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result;
};


function insertSupervisor( supervisorName , supervisorType, supervisorID , slotName , slotType , slotID) {

	var result = "error";


	var dtoSupervisor = {
			"name" : supervisorName ,
			"id": supervisorID ,
			"type": supervisorType 
	};
	
	var dtoSlot = {
			"name": slotName ,
			"id": slotID ,
			"type": slotType 
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "insertSupervisor.htm",
		data: {
			'supervisor': JSON.stringify(dtoSupervisor),
			'slot': JSON.stringify(dtoSlot)
		} ,
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result;
};


function getTechnologies(){
	
	var result;
	
//	var content = '<div class ="checkbox"' 
//		
//	var dialog = new joint.ui.Dialog({
//		width: 300,
//		type: 'neutral',
//		title: 'Save Equipment',
//		content: content,
//		buttons: [
//			{ action: 'cancel', content: 'Cancel', position: 'left' },
//			{ action: 'save', content: 'Save', position: 'left' }
//		]
//	});
//	
//	dialog.on('action:save', dialog.close);
//	dialog.on('action:cancel', cancel);
//
//	dialog.open();
//	
//	function cancel(){
//		dialog.close();
//	}
	
	$.ajax({
		type: "POST",
		async: false,
		url: "getTechnologies.htm",		
		success: function(){ 		   
			result = data;
			return data;
		},
		error : function(e) {
			alert("error: " + e.status);
			
		}
	});

	return result;
	
};


function removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID) {

	var result = "error";

	var dtoContainer = {
			"name" : containerName,
			"type" : containerType ,
			"id" : containerID
	};

	var dtoEquipmentholder = {
			"id": equipmentID ,
			"name": equipmentName,
			"type" : equipmentType
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "removeEquipmentholder.htm",
		data: {
			'container': JSON.stringify(dtoContainer),
			'equipmentholder': JSON.stringify(dtoEquipmentholder)
		},
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result;
};

