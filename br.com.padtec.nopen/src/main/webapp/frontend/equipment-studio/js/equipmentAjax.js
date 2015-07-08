/* ----- Create  ----- */

function EquipStudioInsertContainer(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID) {

	var result = "error";

	var dtoContent = {
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
		url: "EquipStudioInsertContainer.htm",
		data: {
			'content': JSON.stringify(dtoContent),
			'container': JSON.stringify(dtoContainer)
		} ,
		success: function(data){ 		   
			result = data;
			console.log('data' , result);
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});
	
	
	return result;
};

function EquipStudioPerformBind(sourceID, sourceName, sourceType, targetID, targetName, targetType) {

	var result = "error";
	
	var dtoSourceElement = {
			"name": sourceName ,
			"id": sourceID ,
			"type": sourceType 
	};
	
	var dtoTargetElement = {
			"name": targetName  ,
			"id": targetID ,
			"type": targetType 
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "EquipStudioPerformBind.htm",
		data: {
			'sourceElement': JSON.stringify(dtoSourceElement),
			'targetElement': JSON.stringify(dtoTargetElement),
		},
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result;
//	return "success";
};

/* ----- Delete  ----- */

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

/* ----- Get  ----- */

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

/* ----- Link  ----- */

function setTechnology( SupervisorName ,SupervisorType, SupervisorID , tech) {

	var result = "error";
      
    var technology = tech;
    
	var dtoSupervisor = {
			"name": SupervisorName ,
			"id": SupervisorID ,
			"type": SupervisorType 
	};
	
	$.ajax({
		type: "POST",
		async: false,
		url: "setTechnology.htm",
		data: {
			'supervisor': JSON.stringify(dtoSupervisor),
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

function superviseCard( SupervisorName, SupervisorType, SupervisorID ,CardName, CardType , CardID) {

	var result = "error";


	var dtoSupervisor = {
			"name" : SupervisorName ,
			"id": SupervisorID ,
			"type": SupervisorType 
	};
	
	var dtoCard = {
			"name": CardName ,
			"id": CardID ,
			"type": CardType 
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

function unsuperviseCard( SupervisorName, SupervisorType, SupervisorID ,CardName, CardType , CardID) {

	var result = "error";


	var dtoSupervisor = {
			"name" : SupervisorName ,
			"id": SupervisorID ,
			"type": SupervisorType 
	};
	
	var dtoCard = {
			"name": CardName ,
			"id": CardID ,
			"type": CardType 
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "unsuperviseCard.htm",
		data: {
			'Supervisor': JSON.stringify(dtosupervisor),
			'Card': JSON.stringify(dtoCard)
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








