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

