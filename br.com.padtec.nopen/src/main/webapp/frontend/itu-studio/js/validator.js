function validator(validator, graph, app) {
    
    
    // validar inserção de camadas no grafo
    validator.validate('add', isNotLink, isLayer, _.bind(function(err, command, next) {
    	        	
    	var cell = command.data.attributes;
    	var cellSubType = cell.subtype;
    	
		var containerName = cellSubType;
		var containerType = 'layer';
		var cardID = this.cardID; 

		var position = cell.position;
		var size = cell.size;
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
		
		if(parent) { // existe elemento abaixo
			return next('Another element in the way!');
		} else {
			// consultar ontologia para inserção de camada no card
			var result = insertContainer(containerName, containerType, cardID);
			var element = '.stencil-container .viewport .element.bpmn.Pool[value="' +containerName+ '"]';
			
			if(result === "success") {
				$(element).hide();
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
		var sourceElement = graph.getCell(sourceID);
		var sourceElementSubtype = sourceElement.attributes.subtype;
		var sourceElementName = sourceElement.attributes.attrs.text.text;

		var targetID = command.data.attributes.target.id;
		var targetElement = graph.getCell(targetID);
		
		if(targetElement) {
			var targetElementSubtype = targetElement.attributes.subtype;
			var targetElementName = targetElement.attributes.attrs.text.text;
			var targetElementType = targetElement.attributes.type;
		}
		
		// se target for uma interface, remover a interface ligada à conexão (consultar ontologia)
		if(targetElementSubtype === 'in' || targetElementSubtype === 'out') {
			var result = deletePort(targetID, targetElementName, targetElementSubtype);
			
			if(result === "success") {
				targetElement.remove();
				return next(err);
			} else {
				return next(result);
			}
    	}
		
		// se target for um transport function, consultar ontologia para remoção da conexão
		if(targetElementType === TypeEnum.TRANSPORT_FUNCTION) {
			var result = deleteLink(sourceID, sourceElementName, sourceElementSubtype, targetID, targetElementName, targetElementSubtype, cellID);
			if(result === "success") {
				return next(err);
			} else {
				return next(result);
			}
		}
		
		// o target era uma interface que ja foi removida (usuário removeu a interface)
		return next(err);
    	
    }, app));
    

    // validar a remoção de transport functions do grafo
    validator.validate('remove', isTransportFunction, _.bind(function(err, command, next) {

    	if(this.skipOntologyRemoveHandler) {
    		this.skipOntologyRemoveHandler = false;
    		return next(err);
    	}
    	
    	var cellID = command.data.id;
    	var tFunctionName = command.data.attributes.attrs.text.text;
    	var tFunctionType = command.data.attributes.subtype;
    	
		var result = deleteTransportFunction(cellID, tFunctionName, tFunctionType);
		if(result === "success") {
			return next(err);
		} else {
			return next(result);
		}
    }, app));
    

    // validar a remoção de camadas do grafo
    validator.validate('remove', isLayer, _.bind(function(err, command, next) {
    	
    	if(this.skipOntologyRemoveHandler) {
    		this.skipOntologyRemoveHandler = false;
    		return next(err);
    	}
    	var containerName = command.data.attributes.subtype;
		var containerType = 'layer';
		var cardID = this.cardID;
		var element = '.stencil-container .viewport .element.bpmn.Pool[value="' +containerName+ '"]';
		
		var result = deleteContainer(containerName, containerType, cardID);
		if(result === "success") {
			$(element).show();
			return next(err);
		} else {
			return next(result);
		}
    }, app));
    

    // validar a remoção de interfaces do grafo
    validator.validate('remove', isInterface, _.bind(function(err, command, next) {

    	if(this.skipOntologyRemoveHandler) {
    		this.skipOntologyRemoveHandler = false;
    		return next(err);
    	}
    	
    	var cellID = command.data.id;
    	var name = command.data.attributes.attrs.text.text;
    	var type = command.data.attributes.subtype;
    	
    	var result = deletePort(cellID, name, type);			
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
        	var targetTFunctionName = targetTFunction.attributes.attrs.text.text;
        	var targetTFunctionType = targetTFunction.attributes.subtype;

        	var sourceTFunction = graph.getCell(sourceTFunctionID);
        	var sourceTFunctionName = sourceTFunction.attributes.attrs.text.text;
        	var sourceTFunctionType = sourceTFunction.attributes.subtype;
        	
        	// se target for uma interface, cria a conexão
        	if(targetTFunctionSubtype === 'in' || targetTFunctionSubtype === 'out') return next(err);
        	
        	var result = createLink(sourceTFunctionID, sourceTFunctionName, sourceTFunctionType, targetTFunctionID, targetTFunctionName, targetTFunctionType, linkID);
        	
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
		var tFunctionName = cell.attributes.attrs.text.text;
		var sourceContainerName = '';
		var sourceContainerType = '';
		var targetContainerName = '';
		var targetContainerType = '';
		
		var sourceContainer = graph.getCell(command.data.previous.parent);
		if(sourceContainer) {
			sourceContainerName = sourceContainer.attributes.subtype;
		}
		
		var targetContainer = graph.getCell(command.data.next.parent);
		if(targetContainer) {
			targetContainerName = targetContainer.attributes.subtype;
		}
		
		// se apenas moveu o elemento dentro da camada
		if(sourceContainer === targetContainer) return next(err);
		
		var cardID = this.cardID;
    	        	
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
			var parentType = parent.attributes.type;
			
			if(parentType === TypeEnum.LAYER){ // elemento abaixo é uma camada
				// consultar ontologia para troca de camada do transport function
				setContainer();
									
			} else { // elemento abaixo não é um container
				return next('Please, move the transport function to the paper or a layer.');
			}
		} else { // não existe elemento abaixo
			// consultar ontologia para remoção de camada do transport function
			setContainer();
		}
		
		function setContainer() {
			console.log('move TF: ' +tFunctionName+ '; from layer: ' +sourceContainerName+ '; to layer: ' +targetContainerName+ '; inside card ' +cardID);
			var result = changeContainer(tFunctionID, tFunctionName, sourceContainerName, sourceContainerType, targetContainerName, targetContainerType, cardID);
			if(result === "success") {						
				if(parent) parent.embed(cell);
				return next(err);
			} else {
				return next(result);
			}
		}
		
	}, app));
	
	// avoid embedding of layers
	validator.validate('change:parent', isLayer, _.bind(function(err, command, next) {
		var cell = graph.getCell(command.data.id).attributes;
		
    	var position = cell.position;
		var size = cell.size;
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
	if (command.data.type === TypeEnum.TRANSPORT_FUNCTION) return next(err);
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
	if (command.data.type === TypeEnum.LAYER) return next(err);
    // otherwise stop validating (don't call next validation function)
};

//Check if the cell is being added to the graph. Continue validating if no, otherwise stop.
function isLayer(err, command, next) {
	if (command.data.type === TypeEnum.LAYER) return next(err);
  // otherwise stop validating (don't call next validation function)
};