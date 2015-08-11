
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

function exportTopologyAsYANG (graph) {
	var nodes = graph.getElements();
	
	/* for each node of topology, that is, for each equipment */
	_.each(nodes, function(node) {
		var equipName = node.attributes.attrs.text.text;
		var equipGraph = new joint.dia.Graph;
		
		/* load equipment's joint */
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "openEquipment.htm",
			   data: {
				   'filename' : equipName
			   },
			   dataType: 'json',
			   success: function(data){
				   equipGraph.fromJSON(data)
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		
		/* write YANG file */
		fileYANG = 'otn-switch {\n';
		
		/* write <physical> */
		fileYANG = fileYANG + 
			'\tphysical {\n' +
				'\t\tmanaged-element {\n' +
					'\t\t\tracks {\n';
		
		_.each(equipGraph.getElements(), function(element) {
			if(element.attributes.subType === 'Rack') {
				writeFilePhysicalRecursively(element, equipGraph, '\t\t\t\t');
			}
		});
		
		fileYANG = fileYANG + '\t\t\t}\n' +
				'\t\t}\n' +
				'\t}\n';
		
		console.log(fileYANG);
	});
	
	function writeFilePhysicalRecursively(element, graph, totalIdent) {
		var ident = '\t';
		if(element === undefined) return;
		
		if(element.attributes.subType === 'Card') {
			fileYANG = fileYANG + totalIdent + 'interface-entry ' + element.id + ' {\n';
			/*
			 * TODO:	para cada porta do card
			 * 				imprimir sua camada e seu id/nome 
			 */
			fileYANG = fileYANG + totalIdent + ident + '}\n';
			return;
		}
		
		if(element.attributes.subType === 'Slot') {
			fileYANG = 	fileYANG + totalIdent + 'slot-entry ' + element.id + ' {\n' +
						totalIdent + ident + 'equipment {\n' +
					totalIdent + ident + ident + 'interfaces {\n';
			
			var cardID = element.get('embeds')[0];
			var card = graph.getCell(cardID);
			writeFilePhysicalRecursively(card, graph, totalIdent+ident+ident+ident);
			
			fileYANG= 	fileYANG+ totalIdent + ident + ident + '}\n' +
								totalIdent + ident + ident +	'installed-equipment-objectType ' + card.attributes.attrs.text.text + ';\n' +
									totalIdent + ident + '}\n' +
										totalIdent + '}\n';
			return;
		}
		
		if(element.attributes.subType === 'Shelf') {
			fileYANG= 	fileYANG+ totalIdent + 'shelf-entry ' + element.id + ' {\n' +
						totalIdent + ident + 'slots {\n';
			
			var slotIDs = element.get('embeds');
			_.each(slotIDs, function(slotID) {
				var slot = graph.getCell(slotID);
				writeFilePhysicalRecursively(slot, graph, totalIdent+ident+ident);
			});
			
			fileYANG= 	fileYANG+ totalIdent + ident + '}\n' +
							totalIdent + '}\n';
			return;
		}
		
		if(element.attributes.subType === 'Rack') {
			fileYANG= 	fileYANG+ totalIdent + 'rack-entry ' + element.id + ' {\n' +
						totalIdent + ident + 'shelves {\n';
			
			var shelfIDs = element.get('embeds');
			_.each(shelfIDs, function(shelfID) {
				var shelf = graph.getCell(shelfID);
				writeFilePhysicalRecursively(shelf, graph, totalIdent+ident+ident);
			});
			
			fileYANG= 	fileYANG+ totalIdent + ident + '}\n' +
						totalIdent + '}\n';
			return;
		}
	}
	
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