nopen.topology.Exporter = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function(){
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	exportTopologyAsYANG : function(graph) {
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
			
			/* ======= write <physical> and <interfaces> ====== */
			fileYANG = fileYANG + 
				'\tphysical {\n' +
					'\t\tmanaged-element {\n' +
						'\t\t\tracks {\n';
			
			fileInterfaces = '\tinterfaces {\n';
			
			_.each(equipGraph.getElements(), function(element) {
				if(element.attributes.subType === 'Rack') {
					writeFilePhysicalRecursively(element, equipGraph, '\t\t\t\t', equipName);
				}
			});
			
			fileYANG = fileYANG + '\t\t\t}\n' +
								'\t\t}\n' +
							'\t}\n';
			
			fileYANG = fileYANG + fileInterfaces + '\t}\n';
			/* ======= ======= ======= ====== */
			
			fileYANG = fileYANG + '}';
			
			console.log(fileYANG);
		});
		
		function writeFilePhysicalRecursively(element, graph, totalIdent, filename) {
			var ident = '\t';
			if(element === undefined) return;
			
			if(element.attributes.subType === 'Card') {
				parseITUElements(filename, element);
				//TODO: pegar os atributos desse card
				
				
				var cardGraph = loadCardGraph(filename, element.id);
				var cardElements = cardGraph.getElements();
				var cardLinks = cardGraph.getLinks();
				var inPorts = getCardInPorts(cardGraph);
				
				/* para cada porta do card */
				_.each(inPorts, function(inPort) {
					fileInterfaces = fileInterfaces + '\t\tinterface-entry ' + element.attributes.parent + ' ' + inPort.id + '{\n' +
															'\t\t\timplemented-layers {\n';
					
					fileYANG = fileYANG + totalIdent + 'interface-entry ' + inPort.id + ' {\n';
					
					var neighbors = getNeighbors(inPort.id, cardGraph);
					_.each(neighbors, function(neighbor, index) {
						if(neighbor !== inPort.id) {
							writeImplementedLayerEntryRecursively(neighbor, inPort.id, cardGraph, totalIdent + ident, filename);
						}
					});

					fileInterfaces = fileInterfaces + '\t\t\t}\n' +
													'\t\t}\n';
					
					fileYANG = fileYANG + totalIdent + ident + '}\n';
					

					
					function writeImplementedLayerEntryRecursively(element, prevElement, graph, totalIdent, filename) {
						if(graph.getCell(element).attributes.subtype === SubtypeEnum.OUTPUT) return;
						
						fileInterfaces = fileInterfaces + '\t\t\t\tlayer-entry ' + '<camada> ' + element + '-input;\n';
						fileInterfaces = fileInterfaces + '\t\t\t\tlayer-entry ' + '<camada> ' + element + '-output;\n';
						
						fileYANG = fileYANG + totalIdent + '\timplemented-layer-entry ' + '<camada> ' + element + '-input;\n';
						fileYANG = fileYANG + totalIdent + '\timplemented-layer-entry ' + '<camada> ' + element + '-output;\n';
						
						var neighbors = getNeighbors(element, cardGraph);
						_.each(neighbors, function(neighbor, index) {
							if(neighbor !== prevElement) {
								writeImplementedLayerEntryRecursively(neighbor, element, cardGraph, totalIdent, filename);
							}
						});
					}
				});
				
				return;
			}
			
			if(element.attributes.subType === 'Slot') {
				
				fileYANG = 	fileYANG + totalIdent + 'slot-entry ' + element.id + ' {\n' +
							totalIdent + ident + 'equipment {\n' +
						totalIdent + ident + ident + 'interfaces {\n';
				
				var cardID = element.get('embeds')[0];
				var card = graph.getCell(cardID);
				writeFilePhysicalRecursively(card, graph, totalIdent+ident+ident+ident, filename);
				
				fileYANG= 	fileYANG+ totalIdent + ident + ident + '}\n' +
									totalIdent + ident + ident +	'installed-equipment-objectType ' + card.attributes.attrs.name.text + ';\n' +
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
					writeFilePhysicalRecursively(slot, graph, totalIdent+ident+ident, filename);
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
					writeFilePhysicalRecursively(shelf, graph, totalIdent+ident+ident, filename);
				});
				
				fileYANG= 	fileYANG+ totalIdent + ident + '}\n' +
							totalIdent + '}\n';
				return;
			}
		}
		


		function parseITUElements(filename, card) {
			var ITUelements = [], ITUlinks = [];
			
			var cardCells = loadCardElements(filename, card.id);
			$.each(cardCells, function(index, element) {
				
				//Card_Layer
				if(element.attributes.subtype === 'Card_Layer') {
					//console.log('Layer: ' + JSON.stringify(element));
					var layer = {
							"type" : element.attributes.subtype,
							"id" : element.attributes.id,
							"name" : element.attributes.lanes.label,
					};
					ITUelements.push(layer);
					
					//Card > Card_Layer
					var link = {
							"sourceType" : card.type,
							"targetType" : element.attributes.subtype,
							"source" : card.id,
							"target" : element.attributes.id,
					};
					ITUlinks.push(link);
					
				}
				//Trail_Termination_Function
				else if (element.attributes.subtype === 'Trail_Termination_Function') {
					
					var ttf = {
							"type" : element.attributes.subtype,
							"id" : element.attributes.id,
							"name" : element.attributes.attrs.text.text,
					}
					ITUelements.push(ttf);
					
					//Layer > TTF
					var link = {
							"sourceType" : "Card_Layer",
							"targetType" : element.attributes.subtype,
							"source" : element.attributes.parent,
							"target" : element.attributes.id
					}
					ITUlinks.push(link);
				}
				//Adaptation_Function
				else if (element.attributes.subtype === 'Adaptation_Function') {
				
					var af = {
							"type" : element.attributes.subtype,
							"id" : element.attributes.id,
							"name" : element.attributes.attrs.text.text,
					}
					ITUelements.push(af);
					
					//Card_layer > AF
					var link = {
							"sourceType" : card.type,
							"targetType" : element.attributes.subtype,
							"source" : card.id,
							"target" : element.attributes.id
					};
					ITUlinks.push(link);
					
				}
				//Matrix
				else if (element.attributes.subtype === 'Matrix') {
					
					var matrix = {
							"type" : element.attributes.subtype,
							"id" : element.attributes.id,
							"name" : element.attributes.attrs.text.text,
					}
					ITUelements.push(matrix);
					
					//Card_layer > Matrix
					var link = {
							"sourceType" : card.type,
							"targetType" : element.attributes.subtype,
							"source" : card.id,
							"target" : element.attributes.id
					};
					ITUlinks.push(link);
					
				}
				//Input_Card / Output_Card
				else if (element.attributes.subtype === 'Input_Card' || element.attributes.subtype === 'Output_Card') {
					
					var inOut = {
							"type" : element.attributes.subtype,
							"id" : element.attributes.id,
							"name" : element.attributes.attrs.text.text,
					}
					ITUelements.push(inOut);
					
					//Card_layer > Input_Card/Output_Card
					var link = {
							"sourceType" : card.type,
							"targetType" : element.attributes.subtype,
							"source" : card.id,
							"target" : element.attributes.id
					};
					ITUlinks.push(link);
					
				}
				//Links
				else if(element.attributes.type === 'link') {
					
					var link = {
							"sourceType" : getElementType(cardCells, element.attributes.source),
							"targetType" : getElementType(cardCells, element.attributes.target),
							"source" : element.attributes.source,
							"target" : element.attributes.target
					}
					ITUlinks.push(link);
				}
			});
			
			console.log('Elements: ' + JSON.stringify(ITUelements));
			console.log('Links: ' + JSON.stringify(ITUlinks));
			
			//execute parse
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "parseEquipToOWL.htm",
			   data: {
				   'elements' : JSON.stringify(ITUelements),
				   'links' : JSON.stringify(ITUlinks),
			   },
			   success: function(){
				  console.log('PARSE OK!')
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		}
		

		//Method to get element type
		function getElementType(elements, elementId) {
			
			$.each(elements, function(index, element) {
				
				if(element.id == elementId) {
					if(element.subtype) {
						return element.subtype;
					}
					else if(element.subType) {
						return element.subType;
					}
				}
				
			});
			
		}

		function loadCardElements(eqName, cardID) {
			return loadCardGraph(eqName, cardID).getElements();
		}
		
		function loadCardLinks(eqName, cardID) {
			return loadCardGraph(eqName, cardID).getLinks();
		}

		function loadCardGraph(eqName, cardID) {
			var localGraph = new joint.dia.Graph;
			
			$.ajax({
				type: "POST",
				async: false,
				url: "openITUFileEquipment.htm",
				data: {
					'path' : eqName,
					'filename' : cardID
				},
				dataType: 'json',
				success: function(data){
					localGraph.fromJSON(data);
				},
				error : function(e) {
					alert("error: " + e.status);
				}
			});
			return localGraph;
		}
		
		//procedure to get the links connected to an element
		function getLinks(elementID, graph) {
			var links = graph.getLinks();
			var connectedLinks = [];
			
			_.each(links, function(link, index) {
				if(link.attributes.target.id === elementID || link.attributes.source.id === elementID) {
					connectedLinks[connectedLinks.length] = link;
				}
			});
			
			return connectedLinks;
		}
		
		//procedure to get the neighbors of an element
		function getNeighbors(elementID, graph) {
			var connectedLinks = getLinks(elementID, graph);
			var neighbors = [];
			
			_.each(connectedLinks, function(link, index) {
				neighbors[neighbors.length] = link.attributes.target.id === elementID ? link.attributes.source.id : link.attributes.target.id;
			});
			
			return neighbors;
		}
		
		//procedure to get the input ports of a card
		function getCardInPorts(graph) {
			var inPorts = [];
			
			_.each(graph.getElements(), function(element) {
				if(element.attributes.subtype === SubtypeEnum.INPUT || element.attributes.subtype === SubtypeEnum.OUTPUT) {
					inPorts[inPorts.length] = element;
				}
			});
			
			return inPorts;
		}
		
	},
	
	exportTopology : function(graph) {
		
		var $this = this;
		var model = this.app.model;
		
		var tnodeArray = $this.getNodes(graph);
		var tlinkArray = $this.getLinks(graph);
		
		$.ajax({
		   type: "POST",
		   url: "exportTopology.htm",
		   data : {
			   "tnodeArray": JSON.stringify(tnodeArray),
			   "tlinkArray": JSON.stringify(tlinkArray),
			   "uuid": model.getTopologyId(),
		   },
		   success: function(data){   	  
			   $this.openDownloadWindows(data);	   
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	previewTopology : function(graph) {
		
		var $this = this;
		var model = this.app.model;
		
		var tnodeArray = $this.getNodes(graph);
		var tlinkArray = $this.getLinks(graph);
		
		$.ajax({
		   type: "POST",
		   url: "exportTopology.htm",
		   data : {
			   "tnodeArray": JSON.stringify(tnodeArray),
			   "tlinkArray": JSON.stringify(tlinkArray),
			   "uuid": model.getTopologyId(),
		   },
		   success: function(data){   
			   $this.openXMLWindow(data);   
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	getNodes : function(graph) {
		
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
		
	},

	getLinks : function(graph){
		
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
	},


	openXMLWindow : function(content) {
		var win = window.open(
		   'data:application/xml,' + encodeURIComponent(
		     content
		   ),
		   '_blank', "resizable=yes,width=600,height=600,toolbar=0,scrollbars=yes"
		);
	},

	openDownloadWindows : function(content) {
		
		// To run Click method on Firefox
		HTMLElement.prototype.click = function() {
		   var evt = this.ownerDocument.createEvent('MouseEvents');
		   evt.initMouseEvent('click', true, true, this.ownerDocument.defaultView, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
		   this.dispatchEvent(evt);
		};
		
		var blob = new Blob([content]);
		var link = document.createElement('a');
		link.href = window.URL.createObjectURL(blob);
		//link.setAttribute("download", "topology.xml");
		link.download = "topology.xml";
		//link.trigger('click');
		link.click();
		
	},

	
});