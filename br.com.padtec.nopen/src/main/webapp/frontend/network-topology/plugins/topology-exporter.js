
function exportTopology (graph, uuid) {

	var tnodeArray = getNodes(graph);
	var tlinkArray = getLinks(graph);
	
	$.ajax({
	   type: "POST",
	   url: "exportTopology.htm",
	   data : {
		   "tnodeArray": JSON.stringify(tnodeArray),
		   "tlinkArray": JSON.stringify(tlinkArray),
		   "uuid": uuid
	   },
	   success: function(data){   	  
           openDownloadWindows(data);	   
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
};

function previewTopology (graph, uuid) {
	
	var tnodeArray = getNodes(graph);
	var tlinkArray = getLinks(graph);
	
	$.ajax({
	   type: "POST",
	   url: "exportTopology.htm",
	   data : {
		   "tnodeArray": JSON.stringify(tnodeArray),
		   "tlinkArray": JSON.stringify(tlinkArray),
		   "uuid": uuid
	   },
	   success: function(data){   
		   openXMLWindow(data);   
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
};

function getNodes(graph) {
	
	var tnodeArray = new Array();
	
	$.each(graph.getElements(), function( index, value ) {
		
		var tnode = {
				id : value.id,
				name : value.attr('text/text'),
				equipment : value.attr('equipment/template')
		};
		
		tnodeArray.push(tnode);
		
	});
	
	return tnodeArray;
	
}

function getLinks(graph){
	
	var tlinkArray = new Array();
	
	$.each(graph.getLinks(), function( index, value ) {
		 
		var tlink = {
				id : value.id,
				source : value.get('source').id,
				target : value.get('target').id
		};
		
		tlinkArray.push(tlink);
		
	});
	
	return tlinkArray;
}


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

/*
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
	
};*/