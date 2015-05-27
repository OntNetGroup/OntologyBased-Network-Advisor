function verifyElementsOnCard(cardID, cardName, cardType) {

	var result = "error";
	var dtoCard = Util.createDtoElement(cardID, cardName, cardType);

	$.ajax({
		type: "POST",
		async: false,
		url: "verifyElementsOnCard.htm",
		data: {
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

function insertContainer(containerName, containerType, cardID, cardName, cardType) {

	var result = "error";

	var dtoContainer = Util.createDtoElement(containerName, containerName, containerType);
	var dtoCard = Util.createDtoElement(cardID, cardName, cardType);

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
//	return "success";
};


function deleteContainer(containerName, containerType, cardID, cardName, cardType) {

	var result = "error";

	var dtoContainer = Util.createDtoElement(containerName, containerName, containerType);
	var dtoCard = Util.createDtoElement(cardID, cardName, cardType);

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
//	return "success";
};

function createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID) {

	var result = "error";

	var dtoTransportFunction = Util.createDtoElement(tFunctionID, tFunctionName, tFunctionType);
	var dtoContainer = Util.createDtoElement(cardID, containerName, containerType);

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

	return result;
//	return "success";
};

function canCreateTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID) {

	var result = "false";

	var dtoTransportFunction = Util.createDtoElement(tFunctionID, tFunctionName, tFunctionType);
	var dtoContainer = Util.createDtoElement(cardID, containerName, containerType);

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

	return result;
//	return "true";
};

function deleteTransportFunction(id, name, type) {

	var result = "error";

	var dtoTransportFunction = Util.createDtoElement(id, name, type);

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
//	return "success";
};

function changeContainer(tFunctionID, tFunctionName, tFunctionType, sourceContainerName, sourceContainerType, targetContainerName, targetContainerType, cardID, cardName, cardType) {

	var result = "error";

	var dtoTransportFunction = Util.createDtoElement(tFunctionID, tFunctionName, tFunctionType);
	var dtoSourceContainer = Util.createDtoElement(sourceContainerName, sourceContainerName, sourceContainerType);
	var dtoTargetContainer = Util.createDtoElement(targetContainerName, targetContainerName, targetContainerType);
	var dtoCard = Util.createDtoElement(cardID, cardName, cardType);

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

	return result;
//	return "success";
};

function setTransportFunctionName(tFunctionID, tFunctionName, tFunctionType) {

	var result = "error";

	var dtoTransportFunction = Util.createDtoElement(tFunctionID, tFunctionName, tFunctionType);

	$.ajax({
		type: "POST",
		async: false,
		url: "setTransportFunctionName.htm",
		data: {
			'transportFunction': JSON.stringify(dtoTransportFunction), 
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

function createPort(portID, portName, portType, tFunctionID, tFunctionName, tFunctionType) {

	var result;

	var dtoTransportFunction = Util.createDtoElement(tFunctionID, tFunctionName, tFunctionType);
	var dtoPort = Util.createDtoElement(portID, portName, portType);

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
//	return "success";
};

function deletePort(id, name, type) {

	var result;	
	
	var dtoPort = Util.createDtoElement(id, name, type);

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
//	return "success";
};

function setPortName(portID, portName, portType) {

	var result;

	var dtoPort = Util.createDtoElement(portID, portName, portType);

	$.ajax({
		type: "POST",
		async: false,
		url: "setPortName.htm",
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
//	return "success";
};

function createLink(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType, targetTFunctionID, targetTFunctionName, targetTFunctionType, linkID) {

	var result = "error";

	var dtoSourceTFunction = Util.createDtoElement(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType);
	var dtoTargetTFunction = Util.createDtoElement(targetTFunctionID, targetTFunctionName, targetTFunctionType);
	var dtoLink = Util.createDtoElement(linkID, linkID, 'link');

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

	return result;
//	return "success";
};

function canCreateLink(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType, targetTFunctionID, targetTFunctionName, targetTFunctionType) {

	var result = "false";

	var dtoSourceTFunction = Util.createDtoElement(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType);
	var dtoTargetTFunction = Util.createDtoElement(targetTFunctionID, targetTFunctionName, targetTFunctionType);

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

	return result === "true";
//	return true;
};


function deleteLink(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType, targetTFunctionID, targetTFunctionName, targetTFunctionType, linkID) {

	var result = "error";

	var dtoSourceTFunction = Util.createDtoElement(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType);
	var dtoTargetTFunction = Util.createDtoElement(targetTFunctionID, targetTFunctionName, targetTFunctionType);
	var dtoLink = Util.createDtoElement(linkID, linkID, 'link');

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

	return result;
//	return "success";
};


function loadTTFAttributes(reference) {
	var result = "error";
	$.ajax({
		type: "POST",
		async: false,
		url: "loadTTFAttributes.htm",
		data: {
			'reference': reference
		},
		success: function(data){
			console.log(data);
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result;
}
