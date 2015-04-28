function getLayerNames(techName) {

	var result = "error";

	$.ajax({
		type: "POST",
		async: false,
		url: "techLayers.htm",
		data: {
			'techName': techName
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

function insertContainer(containerName, containerType, cardID) {

	var result = "error";

	var dtoContainer = {
			"id" : containerName,
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

//	return result;
	return "success";
};


function deleteContainer(containerName, containerType, cardID) {

	var result = "error";

	var dtoContainer = {
			"id" : containerName,
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

function createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID) {

	var result = "error";

	var dtoTransportFunction = {
			"id" : tFunctionID,
			"name": tFunctionName,
			"type" : tFunctionType
	}
	
	var dtoContainer = {
			"id": cardID,
			"name" : containerName,
			"type" : containerType  
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "createTransportFunction.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction), 
			'container': JSON.stringify(dtoContainer)
		},
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

//	return result;
	return "success";
};

function canCreateTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID) {

	var result = "false";

	var dtoTransportFunction = {
			"id" : tFunctionID,
			"name" : tFunctionName,
			"type" : tFunctionType
	}
	
	var dtoContainer = {
			"id": cardID,
			"name" : containerName,
			"type" : containerType  
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "canCreateTransportFunction.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction), 
			'container': JSON.stringify(dtoContainer)
		},
		success: function(data){ 
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

//	return result;
	return "true";
};

function deleteTransportFunction(id, name, type) {

	var result = "error";

	var dtoTransportFunction = {
			"id" : id,
			"name" : name,
			"type" : type
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

function changeContainer(tFunctionID, tFunctionName, sourceContainerName, sourceContainerType, targetContainerName, targetContainerType, cardID) {

	var result = "error";

	var dtoTransportFunction = {
			"id" : tFunctionID,
			"name" : tFunctionName
	};
	
	var dtoSourceContainer = {
			"id" : sourceContainerName,
			"name" : sourceContainerName,
			"type" : sourceContainerType  
	};
	
	var dtoTargetContainer = {
			"id" : targetContainerName,
			"name" : targetContainerName,
			"type" : targetContainerType  
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
			'sourceContainer': JSON.stringify(dtoSourceContainer), 
			'targetContainer': JSON.stringify(dtoTargetContainer),
			'card': JSON.stringify(dtoCard)
		},
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

//	return result;
	return "success";
};

function createPort(portID, portName, portType, transportFunctionID, tFunctionName, tFunctionType) {

	var result;

	var dtoTransportFunction = {
			"id" : transportFunctionID,
			"name" : tFunctionName,
			"type" : tFunctionType
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

//	return result;
	return "success";
};

function deletePort(id, name, type) {

	var result;	
	
	var dtoPort = {
			"id" : id,
			"name" : name,
			"type" : type
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

//	return result;
	return "success";
};


function createLink(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType, targetTFunctionID, targetTFunctionName, targetTFunctionType, linkID) {

	var result = "error";

	var dtoSourceTFunction = {
			"id" : sourceTFunctionID,
			"name" : sourceTFunctionName,
			"type" : sourceTFunctionType
	}

	var dtoTargetTFunction = {
			"id" : targetTFunctionID,
			"name" : targetTFunctionName,
			"type" : targetTFunctionType
	}
	
	var dtoLink = {
			"id" : linkID,
			"name" : linkID,
			"type": 'link'
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "createLink.htm",
		data: {
			'sourceTFunction': JSON.stringify(dtoSourceTFunction),
			'targetTFunction': JSON.stringify(dtoTargetTFunction),
			'link': JSON.stringify(dtoLink)
		},
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

//	return result;
	return "success";
};

function canCreateLink(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType, targetTFunctionID, targetTFunctionName, targetTFunctionType) {

	var result = "false";

	var dtoSourceTFunction = {
			"id" : sourceTFunctionID,
			"name" : sourceTFunctionName,
			"type" : sourceTFunctionType
	}

	var dtoTargetTFunction = {
			"id" : targetTFunctionID,
			"name" : targetTFunctionName,
			"type" : targetTFunctionType
	}

	$.ajax({
		type: "POST",
		async: false,
		url: "canCreateLink.htm",
		data: {
			'sourceTFunction': JSON.stringify(dtoSourceTFunction),
			'targetTFunction': JSON.stringify(dtoTargetTFunction)
		},
		success: function(data){
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

//	return result === "true";
	return true;
};


function deleteLink(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType, targetTFunctionID, targetTFunctionName, targetTFunctionType, linkID) {

	var result = "error";

	var dtoSourceTFunction = {
			"id" : sourceTFunctionID,
			"name" : sourceTFunctionName,
			"type" : sourceTFunctionType
	}

	var dtoTargetTFunction = {
			"id" : targetTFunctionID,
			"name" : targetTFunctionName,
			"type" : targetTFunctionType
	}
	
	var dtoLink = {
			"id" : linkID,
			"name" : linkID,
			"type": 'link'
	};

	$.ajax({
		type: "POST",
		async: false,
		url: "deleteLink.htm",
		data: {
			'sourceTFunction': JSON.stringify(dtoSourceTFunction),
			'targetTFunction': JSON.stringify(dtoTargetTFunction),
			'link': JSON.stringify(dtoLink)
		},
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

//	return result;
	return "success";
};

