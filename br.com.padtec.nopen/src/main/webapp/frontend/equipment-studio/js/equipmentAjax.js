/* ----- Equipament Holders  ----- */

function insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID) {

	var result = "error";

	var dtoEquipmentholder = {
			"name": equipmentName ,
			"id": equipmentID ,
			"type": equipmentType 
	};
	
	var dtoContainer = {
			"name": containerName  ,
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

function deleteEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID) {

	var result = "error";

	var dtoContainer = {
			"name" : containerName,
			"type" : containerType ,
			"id" :  containerID
	};

	var dtoEquipmentholder = {
			"name": equipmentName,
			"type" : equipmentType,
			"id": equipmentID
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteEquipmentholder.htm",
		data: {
			'equipmentholder': JSON.stringify(dtoEquipmentholder),
			'container': JSON.stringify(dtoContainer)
		},
		success: function(data){ 		   
			result = data;
			
		},
		error : function(e) {
			alert("error: " + e.responseText);

		}
	});
      return "success";
	//return result;
};

/* ----- Supervisor  ----- */

function insertSupervisor( supervisorName, supervisorType, supervisorID ,slotName, slotType , slotID) {

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
		//	console.log(data);
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

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


function setTechnology( supervisorName ,supervisorType, supervisorID , tech) {

	var result = "error";
      
    var technology = tech;
    
	var dtoSupervisor = {
			"name": supervisorName ,
			"id": supervisorID ,
			"type": supervisorType 
	};
	
	$.ajax({
		type: "POST",
		async: false,
		url: "setTechnology.htm",
		data: {
			'supervisor': JSON.stringify(dtoEquipmentholder),
			'technology': JSON.stringify(technology)
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

function superviseCard( supervisorName, supervisorType, supervisorID ,cardName, cardType , cardID) {

	var result = "error";


	var dtoSupervisor = {
			"name" : supervisorName ,
			"id": supervisorID ,
			"type": supervisorType 
	};
	
	var dtoCard = {
			"name": cardName ,
			"id": cardID ,
			"type": cardType 
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "superviseCard.htm",
		data: {
			'supervisor': JSON.stringify(dtoSupervisor),
			'card': JSON.stringify(dtoCard)
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

function unsuperviseCard( supervisorName, supervisorType, supervisorID ,cardName, cardType , cardID) {

	var result = "error";


	var dtoSupervisor = {
			"name" : supervisorName ,
			"id": supervisorID ,
			"type": supervisorType 
	};
	
	var dtoCard = {
			"name": cardName ,
			"id": cardID ,
			"type": cardType 
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "unsuperviseCard.htm",
		data: {
			'supervisor': JSON.stringify(dtoSupervisor),
			'card': JSON.stringify(dtoCard)
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


/* ----- Card  ----- */

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
			result = ("error: " + e.status);
		}
	});

	return result;
};


function getsupervisedCards(){
	
	var result;
	
	$.ajax({
		type: "POST",
		async: false,
		url: "getsupervisedCards.htm",		
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);			
		}		 
	});
 
	return result;
	
};

function getallnotsupervisedCards(){
	
	var result;
	
	$.ajax({
		type: "POST",
		async: false,
		url: "getallnotsupervisedCards.htm",		
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);			
		}		 
	});
 
	return result;
	
};


