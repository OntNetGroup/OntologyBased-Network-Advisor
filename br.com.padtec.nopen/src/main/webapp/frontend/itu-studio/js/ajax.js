function insertContainer(containerName, containerType, cardID) {

	var result = "error";

//	var dtoContainer = {};	
//	dtoContainer.name = containerName;
//	dtoContainer.type = containerType;

	var dtoContainer = {
			"name" : containerName,
			"type" : containerType  
	};

	var dtoCard = {};
	dtoCard.id = cardID;

	$.ajax({
		type: "POST",
		async: false,
		url: "insertContainer.htm",
		data: {
			'container': JSON.stringify(dtoContainer),
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

function deleteContainer(containerName, containerType, cardID) {

	var result = "error";

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteContainer.htm",
		data: {
			'containerName': containerName,
			'containerType': containerType,
			'cardID': cardID
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

function createTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID) {

	var result = "error";

	$.ajax({
		type: "POST",
		async: false,
		url: "createTransportFunction.htm",
		data: {
			'tFunctionID': tFunctionID,
			'tFunctionType': tFunctionType,
			'containerName': containerName,
			'containerType': containerType,
			'cardID': cardID
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

function canCreateTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID) {

	var result = "false";

	$.ajax({
		type: "POST",
		async: false,
		url: "canCreateTransportFunction.htm",
		data: {
			'tFunctionID': tFunctionID,
			'tFunctionType': tFunctionType,
			'containerName': containerName,
			'containerType': containerType,
			'cardID': cardID
		},
		success: function(data){ 
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result === "true";
};

function deleteTransportFunction(id) {

	var result = "error";

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteTransportFunction.htm",
		data: {
			'id': id
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

function changeContainer(tFunctionID, containerName, containerType, cardID) {

	var result = "error";

	$.ajax({
		type: "POST",
		async: false,
		url: "changeContainer.htm",
		data: {
			'tFunctionID': tFunctionID,
			'containerName': containerName,
			'containerType': containerType,
			'cardID': cardID
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

function createPort(portID, transportFunctionID) {

	var result;	

	$.ajax({
		type: "POST",
		async: false,
		url: "createPort.htm",
		data: {
			'portID': portID,
			'transportFunctionID': transportFunctionID
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

function deletePort(id) {

	var result;	

	$.ajax({
		type: "POST",
		async: false,
		url: "deletePort.htm",
		data: {
			'id': id
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


function createLink(sourceTFunctionID, targetTFunctionID) {

	var result = "error";	

	$.ajax({
		type: "POST",
		async: false,
		url: "createLink.htm",
		data: {
			'sourceTFunctionID': sourceTFunctionID,
			'targetTFunctionID': targetTFunctionID
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

function canCreateLink(sourceTFunctionID, targetTFunctionID) {

	var result = "false";

	$.ajax({
		type: "POST",
		async: false,
		url: "canCreateLink.htm",
		data: {
			'sourceTFunctionID': sourceTFunctionID,
			'targetTFunctionID': targetTFunctionID
		},
		success: function(data){
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result === "true";
};


function deleteLink(id) {

	var result = "error";	

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteLink.htm",
		data: {
			'id': id
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

