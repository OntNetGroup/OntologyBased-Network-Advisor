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
			console.log(data);
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});
    console.log(result);
	return result;
};


function getTechnologies(){
	
	var result;
	
	$.ajax({
		type: "POST",
		async: false,
		url: "getTechnologies.htm",		
		success: function(data){ 		   
			result = data;
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

