function validator(validator, graph, app) {
    
    // validar inserção de transport functions no grafo
//    validator.validate('add', isNotLink, isTransportFunction, _.bind(function(err, command, next) {      		
//    	var cell = graph.getCell(command.data.id);
//    	var cellType = cell.get('type');
//    	
//		var tFunctionID = cell.id;
//		
//		var tFunctionName = 'TF_' +this.tFunctionCounter;
//		
//		var tFunctionType = cell.get('subtype');
//		var cardID = this.cardID;
//		
//		var containerName = '';
//		var containerType = 'card';
//    	        	
//    	var position = cell.get('position');
//		var size = cell.get('size');
//		var area = g.rect(position.x, position.y, size.width, size.height);
//		
//		var parent;
//		// get all elements below the added one
//		_.each(graph.getElements(), function(e) {
//		
//			var position = e.get('position');
//			var size = e.get('size');
//			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
//				parent = e;
//			}
//		});
//		
//		if(parent) { // existe algum elemento abaixo
//			var parentType = parent.get('type');
//			
//			if(parentType === 'bpmn.Pool'){ // elemento abaixo é uma camada
//				containerName = parent.get('subtype');
//				containerType = 'layer';
//				console.log('try to insert ' +tFunctionID+ ' name: ' +tFunctionName+ ';type: ' +tFunctionType+ ';layer: ' +containerName+ ';card: ' +cardID);
//				
//				var result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
//				
//				if(result === "success") {	
//					this.isAddingTransportFunction = true;
//					parent.embed(cell);
//					this.tFunctionCounter++;
//					return next(err);
//				} else {
//					return next(result);
//				}
//									
//			} else { // elemento abaixo não é um container
//				return next('Please, add the transport function on the paper or a layer.');
//			}
//		} else { // não existe elemento abaixo
//			// consultar ontologia para inserção de transport function diretamente no card
//			console.log('try to insert ' +tFunctionID+ ' name: ' +tFunctionName+ ';type: ' +tFunctionType+ ';layer: ' +containerName+ ';card: ' +cardID);
//			var result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
//			
//			if(result === "success") {
//				this.tFunctionCounter++;
//				return next(err);
//			} else {
//				return next(result);
//			}
//		}
//    }, app));
    
    // validar inserção de interfaces no grafo
//    validator.validate('add', isNotLink, isInterface, _.bind(function(err, command, next) {
//    	        	
//    	var cell = graph.getCell(command.data.id);
//    	var cellSubType = cell.get('subtype');
//
//    	var position = cell.get('position');
//		var size = cell.get('size');
//		var area = g.rect(position.x, position.y, size.width, size.height);
//
//		var portID = cell.id;
//		var portType = cellSubType;
//		
//		if(portType === 'in') var portName = 'in_' +this.inPortCounter;
//		else var portName = 'out_' +this.outPortCounter;
//		
//		var parent;
//		// get all elements below the added one
//		_.each(graph.getElements(), function(e) {
//		
//			var position = e.get('position');
//			var size = e.get('size');
//			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
//				parent = e;
//			}
//		});
//		
//		if(parent) { // existe algum elemento abaixo
//			var parentType = parent.get('type');
//			
//			if(parentType === 'basic.Path'){ // elemento abaixo é um transport function
//				
//					var transportFunctionID = parent.id;
//					console.log('try to create port ' +portID+ ';name: ' +portName+ ';TF: ' +transportFunctionID);
//					var result = createPort(portID, portName, portType, transportFunctionID)
//					
//					if(result === "success") {
//					
//						var newLink = new joint.dia.Link({	source: {id: transportFunctionID}, target: {id: portID}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
//		    			graph.addCell(newLink);
//		    			
//		    			// Move the port to the superior (in port) or inferior (out port) bar
//		    			if(portType === 'in') {
//		    				cell.transition('position/y', 15, {});
//		    				app.barIn.embed(cell);
//		    				this.inPortCounter++;
//		    			}
//		    			else {
//		    				cell.transition('position/y', 955, {});
//		    				app.barOut.embed(cell);
//		    				this.outPortCounter++;
//		    			}
//		    			
//						return next(err);
//					} else {
//						return next(result);
//					}
//			} else { // elemento abaixo é uma camada 
//				return next('Please, add the port over a transport function.');
//			}
//			
//		} else { // nenhum elemento abaixo
//			return next('Please, add the port over a transport function.');
//		}
//		
//    }, app));
    
    // validar inserção de camadas no grafo
    validator.validate('add', isNotLink, isLayer, _.bind(function(err, command, next) {
    	        	
    	var cell = graph.getCell(command.data.id);
    	var cellSubType = cell.get('subtype');
    	
		var containerName = cellSubType;
		var containerType = 'layer';
		var cardID = this.cardID; 

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
		
		if(parent) { // existe elemento abaixo
			return next('Another element in the way!');
		} else {
			// consultar ontologia para inserção de camada no card
			var result = insertContainer(containerName, containerType, cardID)
			
			if(result === "success") {
				// TODO: remover/desabilitar camada do stencil
				return next(err);
			} else {
				return next(result);
			}
		}
		
    }, app));
    
    
    // validar a remoção de links do grafo
    validator.validate('remove', isLink, _.bind(function(err, command, next) {
    	
    	var cellID = command.data.id;
    	
		var sourceID = command.data.attributes.source.id;
		var targetID = command.data.attributes.target.id;
		var sourceElement = graph.getCell(sourceID);
		var targetElement = graph.getCell(targetID);
		
		if(targetElement) var targetElementSubtype = targetElement.get('subtype');
		
		// se target for uma porta, remover a porta ligada à conexão (consultar ontologia)
		if(targetElementSubtype === 'in' || targetElementSubtype === 'out') {
			var result = deletePort(targetID);
			
			if(result === "success") {
				targetElement.remove();
				return next(err);
			} else {
				return next(result);
			}
    	}
		
		// se target for um transport function, consultar ontologia para remoção da conexão
		if(targetElementSubtype === 'basic.Path') {
			var result = deleteLink(cellID);
			if(result === "success") {
				return next(err);
			} else {
				return next(result);
			}
		}
		
		// o target era uma interface que ja foi removida
		return next(err);
    	
    }, app));
    

    // validar a remoção de transport functions do grafo
//    validator.validate('remove', isTransportFunction, _.bind(function(err, command, next) {
//
//    	if(this.skipOntologyHandler) return next(err);
//    	
//    	var cellID = command.data.id;
//    	var tFunctionType = command.data.attributes.subtype;
//    	
//		var result = deleteTransportFunction(cellID, tFunctionType);
//		if(result === "success") {
//			return next(err);
//		} else {
//			return next(result);
//		}
//    }, app));
    

    // validar a remoção de camadas do grafo
    validator.validate('remove', isLayer, _.bind(function(err, command, next) {

    	var containerName = command.data.attributes.subtype;
		var containerType = 'layer';
		var cardID = this.cardID;
		
		var result = deleteContainer(containerName, containerType, cardID);
		if(result === "success") {
			// TODO: retornar/reabilitar camada ao stencil
			return next(err);
		} else {
			return next(result);
		}
    }, app));
    

    // validar a remoção de interfaces do grafo
    validator.validate('remove', isInterface, _.bind(function(err, command, next) {
    	
    	var cellID = command.data.id;
    	
    	var result = deletePort(cellID);			
		if(result === "success") {
			return next(err);
		} else {
			return next(result);
		}
    }, app));

    // validar inserção de links no grafo
    validator.validate('change:target change:source', isLink, _.bind(function(err, command, next) {
    	
    	// impedir a troca de target ou source (quando o usuário arrasta uma das pontas da 'seta')
    	if(command.action === 'change:source') return next('Invalid operation!');
    	if(command.data.previous.target.id) return next('Invalid operation!');
    	
    	var linkID = command.data.id;
    	var link = graph.getCell(linkID).toJSON();
    	var sourceTFunctionID = link.source.id;
        var targetTFunctionID = link.target.id;
        
        if (sourceTFunctionID && targetTFunctionID) {
        	var targetTFunction = graph.getCell(targetTFunctionID);
        	var targetTFunctionSubtype = targetTFunction.get('subtype');
        	if(targetTFunctionSubtype === 'in' || targetTFunctionSubtype === 'out') return next(err);
        	
        	var result = createLink(sourceTFunctionID, targetTFunctionID, linkID);
        	
        	if(result === "success") {
				return next(err);
			} else {
				return next(result);
			}
        } else {
        	return next('Please, connect to a valid transport function');
        }
    }, app));
    
    
	// validate embedding of transport functions
	validator.validate('change:parent', isTransportFunction, _.bind(function(err, command, next) {

    	if(this.isAddingTransportFunction) {
    		this.isAddingTransportFunction = false;
    		return next(err);
    	}
    	
		var cell = graph.getCell(command.data.id);
		var tFunctionID = cell.id;
		
		var containerName = '';
		var containerType = '';
		
		var cardID = this.cardID;
    	        	
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
			var parentType = parent.get('type');
			
			if(parentType === 'bpmn.Pool'){ // elemento abaixo é uma camada
				// consultar ontologia para troca de camada do transport function
				containerName = parent.get('subtype');
				containerType = 'layer';
				console.log('change layer of ' +tFunctionID+ ' to layer ' +containerName+ ' inside card ' +cardID);
				
				var result = changeContainer(tFunctionID, containerName, containerType, cardID)
				
				if(result === "success") {						
					parent.embed(cell);
					return next(err);
				} else {
					return next(result);
				}
									
			} else { // elemento abaixo não é um container
				return next('Please, move the transport function to the paper or a layer.');
			}
		} else { // não existe elemento abaixo
			// consultar ontologia para remoção de camada do transport function
			console.log('remove layer of ' +tFunctionID+ ' inside card ' +cardID);
			
			var result = changeContainer(tFunctionID, containerName, containerType, cardID)
			
			if(result === "success") {
				return next(err);
			} else {
				return next(result);
			}
		}
		
	}, app));
	
	// avoid embedding of layers
	validator.validate('change:parent', isLayer, _.bind(function(err, command, next) {
		var cell = graph.getCell(command.data.id);
		
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
		
		if(parent) { // existe elemento abaixo
			return next('Another element in the way!');
		}
	}, app));
};


/* ------- VALIDATION FUNCTIONS -------- */
// Check if cell in command is not a link. Continue validating if yes, otherwise stop.
function isNotLink(err, command, next) {
    if (command.data.type !== 'link') {
    	return next(err);
    }
    // otherwise stop validating (don't call next validation function)
};

// Check if cell in command is a link. Continue validating if yes, otherwise stop.
function isLink(err, command, next) {
    if (command.data.type === 'link') return next(err);
    // otherwise stop validating (don't call next validation function)
};

// Check if cell in command is a transport function. Continue validating if yes, otherwise stop.
function isTransportFunction(err, command, next) {
	if (command.data.type === 'basic.Path') return next(err);
    // otherwise stop validating (don't call next validation function)
};

//Check if cell in command is an interface. Continue validating if yes, otherwise stop.
function isInterface(err, command, next) {
	var cellSubType = command.data.attributes.subtype;

	if(cellSubType === 'out' || cellSubType === 'in') return next(err);
    // otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a layer. Continue validating if yes, otherwise stop.
function isLayer(err, command, next) {
	if (command.data.type === 'bpmn.Pool') return next(err);
    // otherwise stop validating (don't call next validation function)
};

//Check if the cell is being added to the graph. Continue validating if no, otherwise stop.
function isLayer(err, command, next) {
	if (command.data.type === 'bpmn.Pool') return next(err);
  // otherwise stop validating (don't call next validation function)
};