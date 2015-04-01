function createTransportFunction(id, layer) {
	$.ajax({
		   type: "POST",
		   url: "createTransportFunction.htm",
		   data: {
			   'id': id,
			   'layer': layer
		   },
		   success: function(data){ 		   
			   return data;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
};

function canCreateTransportFunction(id, layer) {
	$.ajax({
		   type: "POST",
		   url: "canCreateTransportFunction.htm",
		   data: {
			   'id': id,
			   'layer': layer
		   },
		   success: function(data){ 
			   return data === "true" ? true : false;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
};

function createPort(portID, transportFunctionID) {
	$.ajax({
		   type: "POST",
		   url: "createPort.htm",
		   data: {
			   'portID': portID,
			   'transportFunctionID': transportFunctionID
		   },
		   success: function(data){ 		   
			   return data;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
};

function canCreateLink(sourceTFunctionID, targetTFunctionID) {
	$.ajax({
		   type: "POST",
		   url: "canCreateLink.htm",
		   data: {
			   'sourceTFunctionID': sourceTFunctionID,
			   'targetTFunctionID': targetTFunctionID
		   },
		   success: function(data){
			   return data === "true" ? true : false;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
};

