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
    
	// some types of the elements need resizing after they are dropped
	graph.on('add', function(cell, collection, opt) {
		if(isLink(cell)) return;
		if (!opt.stencil) return;

		if(cell.get('type') === 'link') return;
		var type = cell.get('type');
		var subtype = cell.get('subtype');
		
		// configuration of resizing
		var sizeMultiplierTypeWidth = { 	'bpmn.Pool': 5, // layers
								'basic.Path': 1.3, // transport functions
								}[type];			
		var sizeMultiplierTypeHeight = { 	'bpmn.Pool': 3.5, // layers
									'basic.Path': 1.3, // transport functions
								}[type];

		var sizeMultiplierSubtypeWidth = {  'in': 0.5, // in port
										'out': 0.5, // out port
									}[subtype];
		var sizeMultiplierSubTypeHeight = {  'in': 0.5, // in port
										'out': 0.5, // out port
										'AF': 0.7, // AF transport function
									}[subtype];
		
		var sizeMultiplierWidth = sizeMultiplierSubtypeWidth ? sizeMultiplierSubtypeWidth : sizeMultiplierTypeWidth;
		var sizeMultiplierHeight = sizeMultiplierSubTypeHeight ? sizeMultiplierSubTypeHeight : sizeMultiplierTypeHeight;
					
		var originalSize = cell.get('size');
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
		
		var inputText = "<div><p>Transport Function name: <input type='text' id='tFunctionName' /></p> <p id='tFunctionError' style='color:red; display:none;'>Define a name!</p></div>";
		var dialog = new joint.ui.Dialog({
		    width: 300,
		    title: 'Insert Transport Function',
		    content: inputText,
		    type: 'neutral',
		    closeButton: false,
		    buttons: [
		        { action: 'cancel', content: 'Cancel', position: 'left' },
		        { action: 'ok', content: 'OK', position: 'left' }
		    ]
		}).open();
		
		$('#tFunctionName').focus();
		
		dialog.on('action:ok', function(){
			var name = $('#tFunctionName').val();
			if($('#tFunctionName').val()){
				$('#tFunctionError').hide();
				var tFunctionID = cell.id;
				var tFunctionName = $('#tFunctionName').val();
				dialog.close();
				
				var tFunctionType = cell.get('subtype');
				var cardID = this.cardID;
				
				var containerName = '';
				var containerType = 'card';
		    	        	
		    	var position = cell.get('position');
				var size = cell.get('size');
				var area = g.rect(position.x, position.y, size.width, size.height);
				
				var parent;
				// get all elements below the added one
				_.each(graph.getElements(), function(e) {
				
					var position = e.get('position');
					var size = e.get('size');
					if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
						parent = e;
					}
				});
				
				if(parent) { // existe algum elemento abaixo
					
					if(isLayer(parent)){ // elemento abaixo é uma camada
						containerName = parent.get('subtype');
						containerType = 'layer';
						console.log('try to insert ' +tFunctionID+ ' name: ' +tFunctionName+ ';type: ' +tFunctionType+ ';layer: ' +containerName+ ';card: ' +cardID);
						
						var result = canCreateTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);
						if(result === "true") {
							result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
							
							if(result === "success") {	
								this.isAddingTransportFunction = true;
								parent.embed(cell);
								this.tFunctionCounter++;
							} else {
								this.generateAlertDialog(result);
								this.skipOntologyRemoveHandler = true;
								cell.remove();
							}
						}
											
					} else { // elemento abaixo não é um container
						this.generateAlertDialog('Please, add the transport function on the paper or a layer.');
						this.skipOntologyRemoveHandler = true;
						cell.remove();
					}
				} else { // não existe elemento abaixo
					// consultar ontologia para inserção de transport function diretamente no card
					console.log('try to insert ' +tFunctionID+ ' name: ' +tFunctionName+ ';type: ' +tFunctionType+ ';layer: ' +containerName+ ';card: ' +cardID);
					var result = canCreateTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);
					
					if(result === "true") {
						result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
						
						if(result === "success") {	
							this.tFunctionCounter++;
						} else {
							this.generateAlertDialog(result);
							this.skipOntologyRemoveHandler = true;
							cell.remove();
						}
						
					} else {
						this.generateAlertDialog(result);
						this.skipOntologyRemoveHandler = true;
						cell.remove();
					}
				}
			} else {
				$('#tFunctionError').show();
			}
		}, app);
		
		dialog.on('action:cancel', function(){
			dialog.close();
			this.skipOntologyRemoveHandler = true;
			cell.remove();
			
			this.generateAlertDialog('Operation canceled by user');
		}, app);
		
	}, app);
	
	// validar inserção de interfaces no grafo
    graph.on('add', function(cell) {

		if(isLink(cell)) return;
		if(isNotInterface(cell)) return;
		
    	var cellSubType = cell.get('subtype');

    	var position = cell.get('position');
		var size = cell.get('size');
		var area = g.rect(position.x, position.y, size.width, size.height);

		var portID = cell.id;
		var portType = cellSubType;
		
		if(portType === 'in') var portName = 'in_' +this.inPortCounter;
		else var portName = 'out_' +this.outPortCounter;
		
		var parent;
		// get all elements below the added one
		_.each(graph.getElements(), function(e) {
		
			var position = e.get('position');
			var size = e.get('size');
			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
				parent = e;
			}
		});
		
		if(parent) { // existe algum elemento abaixo
			var parentType = parent.get('type');
			
			if(parentType === 'basic.Path'){ // elemento abaixo é um transport function
				
					var transportFunctionID = parent.id;
					console.log('try to create port ' +portID+ ';name: ' +portName+ ';TF: ' +transportFunctionID);
					var result = createPort(portID, portName, portType, transportFunctionID)
					
					if(result === "success") {
					
						var newLink = new joint.dia.Link({	source: {id: transportFunctionID}, target: {id: portID}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
		    			graph.addCell(newLink);
		    			
		    			// Move the port to the superior (in port) or inferior (out port) bar
		    			if(portType === 'in') {
		    				cell.transition('position/y', 15, {});
		    				this.barIn.embed(cell);
		    				this.inPortCounter++;
		    			}
		    			else {
		    				cell.transition('position/y', 955, {});
		    				this.barOut.embed(cell);
		    				this.outPortCounter++;
		    			}
		    			
						return next(err);
					} else {
						return next(result);
					}
			} else { // elemento abaixo é uma camada 
				return next('Please, add the port over a transport function.');
			}
			
		} else { // nenhum elemento abaixo
			return next('Please, add the port over a transport function.');
		}
		
    }, app);
	
	// validar a remoção de transport functions do grafo
    graph.on('remove', function(cell) {
    	
		if(isNotTransportFunction(cell)) return;
    	
    	var cellID = cell.id;
    	var tFunctionType = cell.get('subtype');
    	
    	if(this.skipOntologyRemoveHandler) {
    		this.skipOntologyRemoveHandler = false;
    		return;
    	}
    	
		var result = deleteTransportFunction(cellID, tFunctionType);
		if(result === "success") {
			return;
		} else {
			this.generateAlertDialog(result);
			cell.set({skipOntologyRemoveHandler : true});
		}
    }, app);
    
//    graph.on('all', function(eventName, cell) {
//    	console.log(arguments);
//    }, app);
	
    /* ------ AUXILIAR FUNCTIONS ------- */
	// Check if cell is not a link
	function isNotLink(cell) {
	    if (cell.get('type') !== 'link') return true;
	};

	// Check if cell is a link
	function isLink(cell) {
	    if (cell.get('type') === 'link') return true;
	};

	// Check if cell is a transport function
	function isTransportFunction(cell) {
		if (cell.get('type') === 'basic.Path') return true;
	};
	
	// Check if cell is not a transport function
	function isNotTransportFunction(cell) {
		if (cell.get('type') !== 'basic.Path') return true;
	};

	//Check if cell is an interface
	function isInterface(cell) {
		var cellSubType = cell.get('subtype');
		if(cellSubType === 'out' || cellSubType === 'in') return true;
	};

	//Check if cell is not an interface
	function isNotInterface(cell) {
		var cellSubType = cell.get('subtype');
		if(cellSubType !== 'out' && cellSubType !== 'in') return true;
	};

	//Check if cell is a layer
	function isLayer(cell) {
		if (cell.get('type') === 'bpmn.Pool') return true;
	};
};