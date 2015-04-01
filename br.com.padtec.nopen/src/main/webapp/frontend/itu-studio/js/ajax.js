function insertLayer(layer, cardID) {
	
	var result = "error";
	
	$.ajax({
		   type: "POST",
		   async: false,
		   url: "insertLayer.htm",
		   data: {
			   'layer': layer,
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

function createTransportFunction(id, layer) {
	
	var result = "error";
	
	$.ajax({
		   type: "POST",
		   async: false,
		   url: "createTransportFunction.htm",
		   data: {
			   'id': id,
			   'layer': layer
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

function canCreateTransportFunction(id, layer) {

	var result = "false";
	
	$.ajax({
		   type: "POST",
		   async: false,
		   url: "canCreateTransportFunction.htm",
		   data: {
			   'id': id,
			   'layer': layer
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

function createLink(sourceTFunctionID, targetTFunctionID) {

	var result;	
	
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

