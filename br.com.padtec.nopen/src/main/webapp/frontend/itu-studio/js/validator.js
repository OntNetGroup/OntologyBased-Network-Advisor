function validator(validator, graph, app) {
    
    // validar inserção de elementos no grafo
    validator.validate('add', isNotLink, _.bind(function(err, command, next) {
    	        	
    	var cell = graph.getCell(command.data.id);
    	var cellType = cell.get('type');
    	var cellSubType = cell.get('subtype');
    	        	
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
    				
		if(cellSubType === 'out' || cellSubType === 'in') { // elemento é uma porta
			
			if(parent) { // existe algum elemento abaixo
				var parentType = parent.get('type');
				
				if(parentType === 'basic.Path'){ // elemento abaixo é um transport function
					
					var inputText = "<div><p>Interface name: <input type='text' id='interfaceName' /></p> <p id='interfaceError' style='color:red; display:none;'>Define a name!</p></div>";

					var dialog = new joint.ui.Dialog({
					    width: 300,
					    title: 'Insert Interface',
					    content: inputText,
					    type: 'neutral',
					    closeButton: false,
					    buttons: [
					        { action: 'cancel', content: 'Cancelar', position: 'left' },
					        { action: 'ok', content: 'OK', position: 'left' }
					    ]
					}).open();
					dialog.on('action:ok', function(){
						if($('#interfaceName').val()){
							console.log(this);
							getInterfaceName();
							$('#interfaceError').hide();
							dialog.close();
						} else {
							$('#interfaceError').show();
						}
					}, dialog);
					dialog.on('action:cancel', function(){
						dialog.close();
						return next('Operation canceled!');
					}, dialog);
					
					var portID = cell.id;
					var portName = $('#interfaceName').val(); // TODO: get name
					var portType = cellSubType;
					var transportFunctionID = parent.id;
					console.log('try to create port ' +portID+ ' of TF ' +transportFunctionID);
					var result = createPort(portID, portName, portType, transportFunctionID)
					
					if(result === "success") {
					
						var newLink = new joint.dia.Link({	source: {id: transportFunctionID}, target: {id: portID}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
		    			graph.addCell(newLink);
		    			
		    			// Move the port to the superior (in port) or inferior (out port) bar
		    			if(cellSubType === 'in') {
		    				cell.transition('position/y', 15, {});
		    				this.barIn.embed(cell);
		    			}
		    			else {
		    				cell.transition('position/y', 955, {});
		    				this.barOut.embed(cell);
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
		
		} else if(cellType === 'bpmn.Pool') { // elemento é uma camada
			
			if(parent) { // existe elemento abaixo
				return next('Another element in the way!');
			} else {
				// consultar ontologia para inserção de camada no card
				var containerName = cellSubType;
				var containerType = 'layer';
				var cardID = this.cardID; // TODO: get cardID 
				var result = insertContainer(containerName, containerType, cardID)
				
				if(result === "success") {
					// TODO: remover camada do stencil
					return next(err);
				} else {
					return next(result);
				}
			}
		
		} else if(cellType === 'basic.Path') { // elemento é um transport function
			
			if(parent) { // existe algum elemento abaixo
				var parentType = parent.get('type');
				
				if(parentType === 'bpmn.Pool'){ // elemento abaixo é uma camada
					var tFunctionID = cell.id;
					var tFunctionName = 'a name'; //TODO get name
					var tFunctionType = cell.get('subtype');
					var containerName = parent.get('subtype');
					var containerType = 'layer';
					var cardID = this.cardID; // TODO: get cardID
					console.log('try to insert ' +tFunctionID+ ' of type ' +tFunctionType+ ' on layer ' +containerName+ ' inside card ' +cardID);
					
					var result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
					
					if(result === "success") {	
						this.isAddingTransportFunction = true;
						parent.embed(cell);
						return next(err);
					} else {
						return next(result);
					}
										
				} else { // elemento abaixo não é um container
					return next('Please, add the transport function on the paper or a layer.');
				}
			} else { // não existe elemento abaixo
				// consultar ontologia para inserção de transport function diretamente no card
				var tFunctionID = cell.id;
				var tFunctionName = 'a name'; //TODO get name
				var tFunctionType = cell.get('subtype');
				var containerName = '';
				var containerType = '';
				var cardID = this.cardID; // TODO: get cardID
				console.log('try to insert ' +tFunctionID+ ' of type ' +tFunctionType+ 'on layer ' +containerName+ ' inside card ' +cardID);
				var result = createTransportFunction(tFunctionID, tFunctionName, tFunctionType, containerName, containerType, cardID);
				
				if(result === "success") {
					return next(err);
				} else {
					return next(result);
				}
			}
			
		}
    }, app));
    
    // valida a remoção de elementos do paper
    validator.validate('remove', _.bind(function(err, command, next) {

    	console.log(command);
    	var cellType = command.data.type;
    	var cellID = command.data.id;
    	
    	// se uma conexão for removida
    	if(cellType === 'link') {
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
    	}
    	
    	// se um transport function for removido, consultar ontologia
    	if(cellType === 'basic.Path') {
    		
    		var result = deleteTransportFunction(cellID);
    		if(result === "success") {
				return next(err);
			} else {
				return next(result);
			}
    		
    	}
    	
    	// se uma camada inteira for removida, consultar ontologia
    	if(cellType === 'bpmn.Pool') {
    		
    		var containerName = command.data.attributes.subtype;
    		var containerType = 'layer';
    		var cardID = this.cardID; // TODO: get cardID
    		
			var result = deleteContainer(containerName, containerType, cardID);
			if(result === "success") {
				// TODO: retornar camada ao stencil
				return next(err);
			} else {
				return next(result);
			}
    	}

    	// então é uma interface
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
    
    
    // validate embedding
    validator.validate('change:parent', _.bind(function(err, command, next) {

    	console.log(this.isAddingTransportFunction)
    	if(this.isAddingTransportFunction) {
    		this.isAddingTransportFunction = false;
    		return next(err);
    	}
    	
    	console.log(command);
    	var cell = graph.getCell(command.data.id);
    	var cellType = cell.get('type');
    	var cellSubType = cell.get('subtype');
    	        	
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
		
		if(cellType === 'bpmn.Pool') { // elemento é uma camada
			
			if(parent) { // existe elemento abaixo
				return next('Another element in the way!');
			}
		
		} else if(cellType === 'basic.Path') { // elemento é um transport function
			
			if(parent) { // existe algum elemento abaixo
				var parentType = parent.get('type');
				
				if(parentType === 'bpmn.Pool'){ // elemento abaixo é uma camada
					// consultar ontologia para troca de camada do transport function
					var tFunctionID = cell.id;
					var containerName = parent.get('subtype');
					var containerType = 'layer';
					var cardID = this.cardID; // TODO: get cardID
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
				var tFunctionID = cell.id;
				var containerName = '';
				var containerType = '';
				var cardID = this.cardID; // TODO: get cardID
				console.log('remove layer of ' +tFunctionID+ ' inside card ' +cardID);
				
				var result = changeContainer(tFunctionID, containerName, containerType, cardID)
				
				if(result === "success") {
					return next(err);
				} else {
					return next(result);
				}
			}
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