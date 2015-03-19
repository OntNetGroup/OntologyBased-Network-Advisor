/*
function exportTopology (graph) {
	
	$.ajax({
	   type: "GET",
	   url: "../../exportTopology",
	   data : {
		   'json' : JSON.stringify(graph.toJSON())
	   },
	   contentType: "text/xml; charset=\"utf-8\"",
	   dataType: "xml",
	   success: function(data){   
		   
           var serializedData 	= (new XMLSerializer()).serializeToString(data);
           serializedData = !serializedData.startsWith("<?xml") ? "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + serializedData : serializedData;
		   
           openDownloadWindows(serializedData);
		   
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
};
*/

function exportTopology (graph) {
	
	$.ajax({
	   type: "POST",
	   url: "/nopen/exportTopology",
	   data : JSON.stringify(graph.toJSON()),
	   contentType: "text/xml; charset=\"utf-8\"",
	   dataType: "xml",
	   success: function(data){   
		   
           var serializedData 	= (new XMLSerializer()).serializeToString(data);
           serializedData = !serializedData.startsWith("<?xml") ? "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + serializedData : serializedData;
		   
           openDownloadWindows(serializedData);
		   
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
};

function previewTopology (graph) {
	
	$.ajax({
	   type: "POST",
	   url: "/nopen/exportTopology",
	   data : JSON.stringify(graph.toJSON()),
	   contentType: "text/xml; charset=\"utf-8\"",
	   dataType: "xml",
	   success: function(data){   
		   
           var serializedData 	= (new XMLSerializer()).serializeToString(data);
           serializedData = !serializedData.startsWith("<?xml") ? "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + serializedData : serializedData;
		   
		   openXMLWindow(serializedData);
		   
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
};

function openXMLWindow(content) {
	var win = window.open(
	   'data:application/xml,' + encodeURIComponent(
	     content
	   ),
	   '_blank', "resizable=yes,width=600,height=600,toolbar=0,scrollbars=yes"
	);
};

function openDownloadWindows(content) {
	
	var blob=new Blob([content]);
	var link=document.createElement('a');
	link.href=window.URL.createObjectURL(blob);
	//link.setAttribute("download", "topology.xml");
	link.download="topology.xml";
	//link.trigger('click');
	link.click();
};

// To run Click method on Firefox
HTMLElement.prototype.click = function() {
   var evt = this.ownerDocument.createEvent('MouseEvents');
   evt.initMouseEvent('click', true, true, this.ownerDocument.defaultView, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
   this.dispatchEvent(evt);
};