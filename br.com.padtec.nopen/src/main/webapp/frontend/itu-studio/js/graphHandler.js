function graphHandler(graph, app) {

	var skipOntologyRemoveHandler = false;
	
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
		
		var tFunctionID = cell.id;
		var tFunctionName = 'TF_' +this.tFunctionCounter;
		
		var tFunctionType = cell.get('subtype');
		var cardID = this.cardID;
		
		var containerName = '';
		var containerType = '';
    	        	
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
				
				var result = canCreateTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
				if(result === true) {
					result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
					
					if(result === "success") {	
						this.isAddingTransportFunction = true;
						parent.embed(cell);
						this.tFunctionCounter++;
					} else {
						alert(result);
						skipOntologyRemoveHandler = true;
						cell.remove();
						skipOntologyRemoveHandler = false;
					}
				}
									
			} else { // elemento abaixo não é um container
				alert('Please, add the transport function on the paper or a layer.');
				skipOntologyRemoveHandler = true;
				cell.remove();
				skipOntologyRemoveHandler = false;
			}
		} else { // não existe elemento abaixo
			// consultar ontologia para inserção de transport function diretamente no card
			console.log('try to insert ' +tFunctionID+ ' name: ' +tFunctionName+ ';type: ' +tFunctionType+ ';layer: ' +containerName+ ';card: ' +cardID);
			var result = canCreateTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
			
			if(result === true) {
				result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
				
				if(result === "success") {	
					this.tFunctionCounter++;
				} else {
					alert(result);
					skipOntologyRemoveHandler = true;
					cell.remove();
					skipOntologyRemoveHandler = false;
				}
				
			} else {
				alert(result);
				skipOntologyRemoveHandler = true;
				cell.remove();
				skipOntologyRemoveHandler = false;
			}
		}
	}, app);
	
	// validar a remoção de transport functions do grafo
	graph.on('remove', function(cell) {
		
		if(isLink(cell)) return;
		if(isNotTransportFunction(cell)) return;
		
		if(skipOntologyRemoveHandler) return;
		var cellID = cell.id;
		var tFunctionType = cell.get('subtype');
		var result = deleteTransportFunction(cellID, tFunctionType);
		if(result === "success") {
			console.log('removeu');
		} else {
			alert(result);
		}
	}, app);

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

	//Check if cell is a layer
	function isLayer(cell) {
		if (cell.get('type') === 'bpmn.Pool') return true;
	};
};