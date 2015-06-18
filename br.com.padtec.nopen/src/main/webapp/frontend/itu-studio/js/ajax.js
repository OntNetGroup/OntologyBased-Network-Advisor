/* ======================================================================================
 * Get
 * ======================================================================================*/

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



/* ======================================================================================
 * Create
 * ======================================================================================*/

function insertContainer(dtoContainer, dtoContent) {

	var result = "error";

	$.ajax({
		type: "POST",
		async: false,
		url: "insertContainerITU.htm",
		data: {
			'container': JSON.stringify(dtoContainer),
			'content': JSON.stringify(dtoContent)
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

function insertLayer(layerID, layerName, cardID, cardName) {

	var dtoLayer = Util.createDtoElement(layerID, layerName, 'Card_Layer');
	var dtoCard = Util.createDtoElement(cardID, cardName, 'Card');

	return insertContainer(dtoCard, dtoLayer);
};

function createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerID, containerName, containerType) {

	var dtoTransportFunction = Util.createDtoElement(tFunctionID, tFunctionName, tFunctionType);
	var dtoContainer = Util.createDtoElement(containerID, containerName, containerType);

	return insertContainer(dtoContainer, dtoTransportFunction);
};

function createPort(portID, portName, portType, tFunctionID, tFunctionName, tFunctionType) {

	var dtoTransportFunction = Util.createDtoElement(tFunctionID, tFunctionName, tFunctionType);
	var dtoPort = Util.createDtoElement(portID, portName, portType);

	return insertContainer(dtoTransportFunction, dtoPort);
};

function performBind(sourceID, sourceName, sourceType, targetID, targetName, targetType, linkID) {

<<<<<<< .mine
=======
//	return createLink(sourceID, sourceName, sourceType, targetID, targetName, targetType, linkID); //TODO: apagar esta linha quando o metodo performBind do controlador estiver implementado
>>>>>>> .r909
	var result = "error";

	var dtoSourceElement = Util.createDtoElement(sourceID, sourceName, sourceType);
	var dtoTargetElement = Util.createDtoElement(targetID, targetName, targetType);
	var dtoLink = Util.createDtoElement(linkID, linkID, 'link');

	$.ajax({
		type: "POST",
		async: false,
		url: "performBind.htm",
		data: {
			'sourceElement': JSON.stringify(dtoSourceElement),
			'targetElement': JSON.stringify(dtoTargetElement),
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



/* ======================================================================================
 * Delete
 * ======================================================================================*/

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
//	return "cannot";
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
//	return "cannot";
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
//	return "cannot";
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
//	return "cannot";
};



/* ======================================================================================
 * NAME
 * ======================================================================================*/

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



/* ======================================================================================
 * CHANGE
 * ======================================================================================*/

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



/* ======================================================================================
 * VERIFICATION
 * ======================================================================================*/

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

function canPerformBind(sourceID, sourceName, sourceType, targetID, targetName, targetType) {

<<<<<<< .mine
=======
//	return canCreateLink(sourceID, sourceName, sourceType, targetID, targetName, targetType); //TODO: apagar esta linha quando o metodo canPerformBind do controlador estiver implementado
>>>>>>> .r909
	var result = "false";

	var dtoSourceElement = Util.createDtoElement(sourceID, sourceName, sourceType);
	var dtoTargetElement = Util.createDtoElement(targetID, targetName, targetType);
	
	$.ajax({
		type: "POST",
		async: false,
		url: "canPerformBind.htm",
		data: {
			'sourceElement': JSON.stringify(dtoSourceElement),
			'targetElement': JSON.stringify(dtoTargetElement)
		} ,
		success: function(data){ 		   
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result === "true";
//	return "success";
};



/* ================================================
 * SAVE & LOAD
 * ================================================*/

function loadTTFAttributes() {
	var result = "";
	$.ajax({
		type: "POST",
		async: false,
		url: "loadTTFAttributes.htm",
		success: function(data){
			result = data;
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

	return result;
}
