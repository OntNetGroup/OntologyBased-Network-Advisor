function graphHandler(graph, app) {

    graph.on('add', function(cell, collection, opt) {
        if (opt.stencil) {
            this.createInspector(cell);
            this.commandManager.stopListening();
            this.inspector.updateCell();
            this.commandManager.listen();
            this.inspector.$('[data-attribute]:first').focus();
        }
    }, app);
    
//    graph.on('all', function(eventName, cell) {
//    	console.log(arguments);
//    });
    
	// some types of the elements need resizing after they are dropped
	graph.on('add', function(cell, collection, opt) {
		if(isLink(cell)) return;
		if (!opt.stencil) return;

		var type = cell.attributes.type;
		var subtype = cell.attributes.subtype;
		
		// configuration of resizing
		var sizeMultiplierTypeWidth = { 	'bpmn.Pool': 5, // layers
								'basic.Path': 1.3, // transport functions
								}[type];			
		var sizeMultiplierTypeHeight = { 	'bpmn.Pool': 3.5, // layers
									'basic.Path': 1.3, // transport functions
								}[type];

		var sizeMultiplierSubtypeWidth = {  'Input_Card': 0.5, // in port
										'Output_Card': 0.5, // out port
									}[subtype];
		var sizeMultiplierSubTypeHeight = {  'Input_Card': 0.5, // in port
										'Output_Card': 0.5, // out port
										'Adaptation_Function': 0.7, // AF transport function
									}[subtype];
		
		var sizeMultiplierWidth = sizeMultiplierSubtypeWidth ? sizeMultiplierSubtypeWidth : sizeMultiplierTypeWidth;
		var sizeMultiplierHeight = sizeMultiplierSubTypeHeight ? sizeMultiplierSubTypeHeight : sizeMultiplierTypeHeight;
					
		var originalSize = cell.attributes.size;
		if (sizeMultiplierWidth && sizeMultiplierHeight) {
			cell.set('size', {
				width: originalSize.width * sizeMultiplierWidth,
				height: originalSize.height * sizeMultiplierHeight
			});
		}
		
		sizeMultiplierWidth = 0;
		sizeMultiplierHeight = 0;
	}, app);
	
	// validar inserção de transport functions no grafo
	graph.on('add', function(cell, collection, opt) {
		if(isLink(cell)) return;
		if(isNotTransportFunction(cell)) return;
		if(this.skipOntologyAddHandler) {
    		this.skipOntologyAddHandler = false;
    		return;
    	}
		
		var tFunctionID = cell.id;
		var tFunctionType = cell.attributes.subtype;
		var tFunctionName = getName(tFunctionType);

		var containerID = this.cardID;
		var containerName = this.cardName;
		var containerType = TypeEnum.CARD;

		var position = cell.attributes.position;
		var size = cell.attributes.size;
		var area = g.rect(position.x, position.y, size.width, size.height);

		var parent;
		// get all elements below the added one
		_.each(graph.getElements(), function(e) {

			var position = e.attributes.position;
			var size = e.attributes.size;
			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
				parent = e;
			}
		});

		if(parent) { // existe algum elemento abaixo

			if(isLayer(parent)){ // elemento abaixo é uma camada
				containerID = parent.id;
				containerName = parent.attributes.lanes.label;
				containerType = TypeEnum.CARD_LAYER;
				// consultar ontologia para inserção de transport function no layer
				insertTransportFunction();

			} else { // elemento abaixo não é um container
				Util.generateAlertDialog('Please, add the transport function on the paper or a layer.');
				this.skipOntologyRemoveHandler = true;
				cell.remove();
			}
		} else { // não existe elemento abaixo
			// consultar ontologia para inserção de transport function diretamente no card
			insertTransportFunction();
		}
		
		function insertTransportFunction() {
			console.log('try to insert ' +tFunctionID+ ' name: ' +tFunctionName+ ';type: ' +tFunctionType+ ';layer: ' +containerName+ ';card: ' +containerID);
			
			var result = canCreateTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, containerID);
			if(result === "true") {
				result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerID, containerName, containerType);
				
				if(result === "success") {	
					if(parent) {
						Util.isAddingTransportFunction = true;
						parent.embed(cell);
					}
					cell.attr({
						text: {text: tFunctionName}
					});
					nextName(tFunctionType);
				} else {
					Util.generateAlertDialog(result);
					app.skipOntologyRemoveHandler = true;
					cell.remove();
				}
			} else {
				Util.generateAlertDialog(result);
				app.skipOntologyRemoveHandler = true;
				cell.remove();
			}
		};
	}, app);
	
	// validar inserção de camadas no grafo
	graph.on('add', function(cell) {
		if(isLink(cell)) return;
		if(isNotLayer(cell)) return;
		if(this.skipOntologyAddHandler) {
    		this.skipOntologyAddHandler = false;
    		return;
    	}
		var cardID = this.cardID;
		var cardName = this.cardName;
		var layerID = cell.id;

		var position = cell.attributes.position;
		var size = cell.attributes.size;
		var area = g.rect(position.x, position.y, size.width, size.height);
		
		var technologies = getTechnologies();
		
		var parent;
		// get all elements below the added one
		_.each(graph.getElements(), function(e) {
		
			var position = e.attributes.position;
			var size = e.attributes.size;
			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
				parent = e;
			}
		});
		
		if(parent) { // existe elemento abaixo
			Util.generateAlertDialog('Another element in the way!');
			this.skipOntologyRemoveHandler = true;
			cell.remove();
		} else {
			
			generateDialog(technologies);
			
		}
		

		// Generate a Dialog with technologies and layers
		function generateDialog(technologies){
			
			var content = '<form id="addLayer">';
			content = content + 'Technology: <select class="technology">';
			for(var i = 0; i < technologies.length; i++){
					content = content + '<option value="' + technologies[i] + '">' + technologies[i] + '</option>';
			}
			
			content = content + '</select><br>'
			content = content + 'Layer: <select class="layer"/>';
			content = content + '<option value=""></option>'
			content = content +  '</form>';
			
			var dialog = new joint.ui.Dialog({
				width: 300,
				type: 'neutral',
				modal: false,
				closeButton: false,
				title: 'Technology and Layer',
				content: content,
				buttons: [
					{ action: 'cancel', content: 'Cancel', position: 'left' },
					{ action: 'add', content: 'Add', position: 'left' }
				]
			});
			dialog.on('action:select-layer', dialog.close);
			dialog.on('action:add', addLayer);
			dialog.on('action:cancel', cancel);
			dialog.open();
			
			$('#black_overlay').show();
			

			function cancel() {
				this.skipOntologyRemoveHandler = true;
				cell.remove();
				dialog.close();
				$('#black_overlay').hide();
			}
			
			loadLayers($('.technology').val());
			$('.technology').change(function(){
				loadLayers($(this).val());
			});
			
			function loadLayers(selectedTechnology) {
				var layers = getLayerNames(selectedTechnology);
				var layerCombobox = $('.layer');

				layerCombobox.empty();
				
				var layersOnPaper = app.getLayersOnPaper();
				
				_.each(layers, function(layer, i) {
					if(!_.contains(layersOnPaper, layer)) {
						layerCombobox.append($('<option>', {
							value: layer,
							text: layer
						}));
					}
				});
				
			};
			
			function addLayer(){
				
				var layerName = $('.layer').val();
				var techName = $('.technology').val();
				
				cell.technology = techName;
				cell.attr({
					text: {text: layerName}
				});
				console.log(cell);
				
				// consultar ontologia para inserção de camada no card
				var result = insertLayer(layerID, layerName, cardID, cardName);
//				var element = '.stencil-container .viewport .element.bpmn.Pool[value="' +layerName+ '"]';
				
				if(result === "success") {
//					$(element).hide();
				} else {
					Util.generateAlertDialog(result);
					this.skipOntologyRemoveHandler = true;
					cell.remove();
				}

				dialog.close();
				$('#black_overlay').hide();
			};
			
		};
		
	}, app);
	
	// validar inserção de interfaces no grafo
    graph.on('add', function(cell) {

		if(isLink(cell)) return;
		if(isNotInterface(cell)) return;
		if(this.skipOntologyAddHandler) {
    		this.skipOntologyAddHandler = false;
    		return;
    	}
		
    	var cellSubType = cell.attributes.subtype;

    	var position = cell.attributes.position;
		var size = cell.attributes.size;
		var area = g.rect(position.x, position.y, size.width, size.height);

		var portID = cell.id;
		var portType = cellSubType;
		var portName = getName(portType);
		
		var parent;
		// get all elements below the added one
		_.each(graph.getElements(), function(e) {
		
			var position = e.attributes.position;
			var size = e.attributes.size;
			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
				parent = e;
			}
		});
		
		if(parent) { // existe algum elemento abaixo
			var parentType = parent.attributes.type;
			
			if(parentType === TypeEnum.TRANSPORT_FUNCTION){ // elemento abaixo é um transport function
				
					var transportFunctionID = parent.id;
					var tFunctionName = parent.attributes.attrs.text.text;
					var tFunctionType = parent.attributes.subtype;
					console.log('try to create port ' +portID+ ';name: ' +portName+ ';TF: ' +transportFunctionID);
					var result = canPerformBind(transportFunctionID, tFunctionName, tFunctionType, portID, portName, portType);
					
					if(result === "true") {
					
						var newLink = new joint.dia.Link({	source: {id: transportFunctionID}, target: {id: portID}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
		    			graph.addCell(newLink);
		    			
		    			// Move the port to the superior (in port) or inferior (out port) bar
		    			if(portType === SubtypeEnum.INPUT) {
		    				cell.transition('position/y', 15, {});
		    				this.barIn.attributes.embeddedPorts[this.barIn.attributes.embeddedPorts.length] = portID;
		    				this.manageEmbeddedPorts(this.barIn);
		    			}
		    			else {
		    				cell.transition('position/y', 955, {});
		    				this.barOut.attributes.embeddedPorts[this.barOut.attributes.embeddedPorts.length] = portID;
		    				this.manageEmbeddedPorts(this.barOut);
		    			}
						cell.attr({
							text: {text: portName}
						});
		    			nextName(portType);
					} else {
						Util.generateAlertDialog(result);
						this.skipOntologyRemoveHandler = true;
						cell.remove();
					}
			} else { // elemento abaixo não é um transport function
				Util.generateAlertDialog('Please, add the port over a transport function.');
				this.skipOntologyRemoveHandler = true;
				cell.remove();
			}
			
		} else { // nenhum elemento abaixo
			Util.generateAlertDialog('Please, add the port over a transport function.');
			this.skipOntologyRemoveHandler = true;
			cell.remove();
		}
		
    }, app);
	
    /* ------ AUXILIAR FUNCTIONS ------- */
	// Check if cell is not a link
	function isNotLink(cell) {
	    if (cell.attributes.type !== TypeEnum.LINK) return true;
	};

	// Check if cell is a link
	function isLink(cell) {
	    if (cell.attributes.type === TypeEnum.LINK) return true;
	};

	// Check if cell is a transport function
	function isTransportFunction(cell) {
		if (cell.attributes.type === TypeEnum.TRANSPORT_FUNCTION) return true;
	};
	
	// Check if cell is not a transport function
	function isNotTransportFunction(cell) {
		if (cell.attributes.type !== TypeEnum.TRANSPORT_FUNCTION) return true;
	};

	//Check if cell is an interface
	function isInterface(cell) {
		var cellSubType = cell.attributes.subtype;
		if(cellSubType === SubtypeEnum.OUTPUT || cellSubType === SubtypeEnum.INPUT) return true;
	};

	//Check if cell is not an interface
	function isNotInterface(cell) {
		var cellSubType = cell.attributes.subtype;
		if(cellSubType !== SubtypeEnum.OUTPUT && cellSubType !== SubtypeEnum.INPUT) return true;
	};

	//Check if cell is a layer
	function isLayer(cell) {
		if (cell.attributes.type === TypeEnum.LAYER) return true;
	};

	//Check if cell is not a layer
	function isNotLayer(cell) {
		if (cell.attributes.type !== TypeEnum.LAYER) return true;
	};
	
	// Get name for properly element being added
	function getName(elementSubtype) {
		if(elementSubtype === SubtypeEnum.INPUT) return 'in_' +app.inPortCounter;
		if(elementSubtype === SubtypeEnum.OUTPUT) return 'out_' +app.outPortCounter;
		if(elementSubtype === SubtypeEnum.ADAPTATION_FUNCTION) return 'AF_' +app.AFCounter;
		if(elementSubtype === SubtypeEnum.TRAIL_TERMINATION_FUNCTION) return 'TTF_' +app.TTFCounter;
		if(elementSubtype === SubtypeEnum.MATRIX) return 'Matrix_' +app.MatrixCounter;
	};
	
	// Increment the counter of the properly element
	function nextName(elementSubtype) {
		if(elementSubtype === SubtypeEnum.INPUT) app.inPortCounter++;
		if(elementSubtype === SubtypeEnum.OUTPUT) app.outPortCounter++;
		if(elementSubtype === SubtypeEnum.ADAPTATION_FUNCTION) app.AFCounter++;
		if(elementSubtype === SubtypeEnum.TRAIL_TERMINATION_FUNCTION) app.TTFCounter++;
		if(elementSubtype === SubtypeEnum.MATRIX) app.MatrixCounter++;
	};
};