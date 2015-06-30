function validator(validator, graph, app) {
    
    
    // validar inserção de camadas no grafo
    validator.validate('add', isNotLink, isLayer, _.bind(function(err, command, next) {
    	        	
    	var cell = command.data.attributes;
    	var cellSubType = cell.subtype;
    	
		var layerID = command.data.id;
//    	var layerName = cellSubType;
		var layerName = cell.lanes.label;
		var cardID = this.cardID;
		var cardName = this.cardName;

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
			var result = insertLayer(layerID, layerName, cardID, cardName);
			var element = '.stencil-container .viewport .element.bpmn.Pool[value="' +layerName+ '"]';
			
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
		if(sourceElement) {
			var sourceElementSubtype = sourceElement.attributes.subtype;
			var sourceElementName = sourceElement.attributes.attrs.text.text;
		}

		var targetID = command.data.attributes.target.id;
		var targetElement = graph.getCell(targetID);
		if(targetElement) {
			var targetElementSubtype = targetElement.attributes.subtype;
			var targetElementName = targetElement.attributes.attrs.text.text;
			var targetElementType = targetElement.attributes.type;
		}
		
		// se target for uma interface, remover a interface ligada à conexão (consultar ontologia)
		if(targetElementSubtype === SubtypeEnum.OUTPUT || targetElementSubtype === SubtypeEnum.INPUT) {
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
//    	var containerName = command.data.attributes.subtype;
    	var containerName = command.data.attributes.lanes.label;
		var containerType = TypeEnum.CARD_LAYER;
		var cardID = this.cardID;
		var cardName = this.cardName;
		var cardType = this.cardType;
		
		var element = '.stencil-container .viewport .element.bpmn.Pool[value="' +containerName+ '"]';
		
		var result = deleteContainer(containerName, containerType, cardID, cardName, cardType);
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
			if(type === SubtypeEnum.INPUT) {
				var index = this.barIn.attributes.embeddedPorts.indexOf(cellID);
				this.barIn.attributes.embeddedPorts.splice(index, 1);
			} else {
				var index = this.barOut.attributes.embeddedPorts.indexOf(cellID);
				this.barOut.attributes.embeddedPorts.splice(index, 1);
			}
			return next(err);
		} else {
			return next(result);
		}
    }, app));

    // validar inserção de links no grafo
    validator.validate('add change:target change:source', isLink, _.bind(function(err, command, next) {
    	
    	// impedir a troca de target ou source (quando o usuário arrasta uma das pontas da 'seta')
    	if(command.action === 'change:source') return next('Invalid operation!');
    	if(command.data.previous.target) 
    		if(command.data.previous.target.id) return next('Invalid operation!');
    	
    	var linkID = command.data.id;
    	var link = graph.getCell(linkID).toJSON();
    	var sourceID = link.source.id;
        var targetID = link.target.id;
        
        if (sourceID && targetID) {
        	var targetElement = graph.getCell(targetID);
        	var targetName = targetElement.attributes.attrs.text.text;
        	var targetSubtype = targetElement.attributes.subtype;

        	var sourceElement = graph.getCell(sourceID);
        	var sourceName = sourceElement.attributes.attrs.text.text;
        	var sourceSubtype = sourceElement.attributes.subtype;
        	
//        	var result = createLink(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID);
        	var result = performBind(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID);
        	
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

    	if(Util.isAddingTransportFunction) {
    		Util.isAddingTransportFunction = false;
    		return next(err);
    	}
    	
		var cell = graph.getCell(command.data.id);
		var tFunctionID = cell.id;
		var tFunctionType = cell.attributes.subtype;
		var tFunctionName = cell.attributes.attrs.text.text;
		var sourceContainerName = '';
		var sourceContainerType = '';
		var targetContainerName = '';
		var targetContainerType = '';
		
		var sourceContainer = graph.getCell(command.data.previous.parent);
		if(sourceContainer) {
//			sourceContainerName = sourceContainer.attributes.subtype;
			sourceContainerName = sourceContainer.attributes.lanes.label;
		}
		
		var targetContainer = graph.getCell(command.data.next.parent);
		if(targetContainer) {
//			targetContainerName = targetContainer.attributes.subtype;
			targetContainerName = targetContainer.attributes.lanes.label;
		}
		
		// se apenas moveu o elemento dentro da camada
		if(sourceContainer === targetContainer) return next(err);
		
		var cardID = this.cardID;
		var cardName = this.cardName;
		var cardType = this.cardType;
    	        	
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
			var result = changeContainer(tFunctionID, tFunctionName, tFunctionType, sourceContainerName, sourceContainerType, targetContainerName, targetContainerType, cardID, cardName, cardType);
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
    
    // when a transport function or interface have their name changed
    validator.validate('change:attrs', _.bind(function(err, command, next) {
    	var previousName = command.data.previous.attrs.text.text;
    	
    	if(_.contains(['AF', 'TTF', 'Matrix', 'IN', 'OUT'], previousName)) { // verify if it's the first change of name (element being added)
    		return next(err);
    	}
    	
    	var cellID = command.data.id;
    	var cell = this.graph.getCell(cellID);
    	var cellName = command.data.next.attrs.text.text;
    	var cellType = cell.attributes.type;
    	var cellSubtype = cell.attributes.subtype;
    	
    	if(cellType === TypeEnum.TRANSPORT_FUNCTION) {
    		var result = setTransportFunctionName(cellID, cellName, cellSubtype);
    	} else {
    		var result = setPortName(cellID, cellName, cellSubtype);
    	}
    	if(result === "success") {
    		return next(err);
    	} else {
    		next(result);
    	}
    }, app));
};


/* ------- VALIDATION FUNCTIONS -------- */
// Check if cell in command is not a link. Continue validating if yes, otherwise stop.
function isNotLink(err, command, next) {
    if (command.data.type !== TypeEnum.LINK) {
    	return next(err);
    }
    // otherwise stop validating (don't call next validation function)
};

// Check if cell in command is a link. Continue validating if yes, otherwise stop.
function isLink(err, command, next) {
    if (command.data.type === TypeEnum.LINK) return next(err);
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

	if(cellSubType === SubtypeEnum.OUTPUT || cellSubType === SubtypeEnum.INPUT) return next(err);
    // otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a layer. Continue validating if yes, otherwise stop.
function isLayer(err, command, next) {
	if (command.data.type === TypeEnum.LAYER) return next(err);
    // otherwise stop validating (don't call next validation function)
};