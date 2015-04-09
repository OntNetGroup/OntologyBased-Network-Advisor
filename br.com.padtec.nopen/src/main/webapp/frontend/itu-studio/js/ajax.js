function insertContainer(containerName, containerType, cardID) {

	var result = "error";

	var dtoContainer = {
			"name" : containerName,
			"type" : containerType  
	};

	var dtoCard = {
			"id": cardID
	};

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

	var dtoContainer = {
			"name" : containerName,
			"type" : containerType  
	};

	var dtoCard = {
			"id": cardID
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteContainer.htm",
		data: {
			'container': JSON.stringify(dtoContainer),
			'card': JSON.stringify(dtoCard)
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

	var dtoTransportFunction = {
			"id" : tFunctionID,
			"type" : tFunctionType
	}
	
	var dtoContainer = {
			"name" : containerName,
			"type" : containerType  
	};

	var dtoCard = {
			"id": cardID
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "createTransportFunction.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction), 
			'container': JSON.stringify(dtoContainer),
			'card': JSON.stringify(dtoCard)
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

	var dtoTransportFunction = {
			"id" : tFunctionID,
			"type" : tFunctionType
	}
	
	var dtoContainer = {
			"name" : containerName,
			"type" : containerType  
	};

	var dtoCard = {
			"id": cardID
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "canCreateTransportFunction.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction), 
			'container': JSON.stringify(dtoContainer),
			'card': JSON.stringify(dtoCard)
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

	var dtoTransportFunction = {
			"id" : id
	}

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteTransportFunction.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction)
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

	var dtoTransportFunction = {
			"id" : tFunctionID
	}
	
	var dtoContainer = {
			"name" : containerName,
			"type" : containerType  
	};

	var dtoCard = {
			"id": cardID
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "changeContainer.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction), 
			'container': JSON.stringify(dtoContainer),
			'card': JSON.stringify(dtoCard)
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

function createPort(portID, portName, portType, transportFunctionID) {

	var result;

	var dtoTransportFunction = {
			"id" : tFunctionID
	}
	
	var dtoPort = {
			"id" : portID,
			"name" : portName,
			"type" : portType  
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "createPort.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction), 
			'port': JSON.stringify(dtoPort)
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
	
	var dtoPort = {
			"id" : id
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "deletePort.htm",
		data: {
			'port': JSON.stringify(dtoPort)
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


function createLink(sourceTFunctionID, targetTFunctionID, linkID) {

	var result = "error";

	var dtoSourceTFunction = {
			"id" : sourceTFunctionID
	}

	var dtoTargetTFunction = {
			"id" : targetTFunctionID
	}
	
	var dtoLink = {
			"id" : linkID
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "createLink.htm",
		data: {
			'sourceTFunction': dtoSourceTFunction,
			'targetTFunction': dtoTargetTFunction,
			'link': dtoLink
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

	var dtoSourceTFunction = {
			"id" : sourceTFunctionID
	}

	var dtoTargetTFunction = {
			"id" : targetTFunctionID
	}

	$.ajax({
		type: "POST",
		async: false,
		url: "canCreateLink.htm",
		data: {
			'sourceTFunction': dtoSourceTFunction,
			'targetTFunction': dtoTargetTFunction
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
	
	var dtoLink = {
			"id" : id
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteLink.htm",
		data: {
			'link': dtoLink
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

