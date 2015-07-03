function validator(validator, graph, app) {


	validator.validate('change:attrs', _.bind(function(err, command, next) {
		var previousName = command.data.previous.attrs.text.text;

		if(_.contains(['Rack', 'Shelf', 'Slot', 'Card', 'Supervisor'], previousName)) { // verify if it's the first change of name (element being added)
			return next(err);
		}

		var cellID = command.data.id;
		//console.log('id' , cellID);
		var cell = this.graph.getCell(cellID);
		//console.log('cell' , cell);
		var cellName = command.data.next.attrs.name.text;
		//console.log('name' , cellName);
		var cellsubType = cell.attributes.subType;
		//console.log('type', cellsubType);

		//var result = setEquipmentName(cellID,cellName,cellsubType);
		var result = "success";
		if (result === "success"){			
			if(cellsubType === "Supervisor"){
				//procurar no grafico inteiro pelo nome da Supervisor
				var elementos = graph.getElements();
				for(var i = 0; i < elementos.length; i++){
					var equipment = elementos[i];
					if((equipment.attributes.subType) === 'Card'){
						if((equipment.get('SupervisorID') === (cellID))){
							equipment.set("Supervisor" , (cellName));
						}
					}
				}
			}
			return next(err);
		}else{
			next(result);
		}
	}, app));

//validator.validate('add change:target change:source', isLink, _.bind(function(err, command, next) {
//    	
//    	// impedir a troca de target ou source (quando o usuário arrasta uma das pontas da 'seta')
//    	if(command.action === 'change:source') return next('Invalid operation!');
//    	if(command.data.previous.target) 
//    		if(command.data.previous.target.id) return next('Invalid operation!');
//    	
//    	var linkID = command.data.id;
//    	var link = graph.getCell(linkID).toJSON();
//    	var sourceID = link.source.id;
//        var targetID = link.target.id;
//        
//        if (sourceID && targetID) {
//        	var targetElement = graph.getCell(targetID);
//        	var targetName = targetElement.attributes.attrs.name.text;
//        	var targetSubtype = targetElement.attributes.subType;
//
//        	var sourceElement = graph.getCell(sourceID);
//        	var sourceName = sourceElement.attributes.attrs.name.text;
//        	var sourceSubtype = sourceElement.attributes.subType;
//        	
////        	var result = createLink(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID);
//        	var result = performBind(sourceID, sourceName, sourceSubtype, targetID, targetName, targetSubtype, linkID);
//        	
//        	if(result === "success") {
//				return next(err);
//			} else {
//				return next(result);
//			}
//        } else {
//        	return next('erro');
//        }
//    }, app));

}


/* ------- VALIDATION FUNCTIONS -------- */
//Check if cell in command is not a link. Continue validating if yes, otherwise stop.
function isNotLink(err, command, next) {
	if (command.data.type !== 'link') {
		return next(err);
	}
	// otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a link. Continue validating if yes, otherwise stop.
function isLink(err, command, next) {
	if (command.data.type === 'link') return next(err);
	// otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a Rack. Continue validating if yes, otherwise stop.
function isRack(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'Rack') return next(err);
	// otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a Shelf. Continue validating if yes, otherwise stop.
function isShelf(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'Shelf') return next(err);
	// otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a Slot. Continue validating if yes, otherwise stop.
function isSlot(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'Slot') return next(err);
	// otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a Card. Continue validating if yes, otherwise stop.
function isCard(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'Card') return next(err);
	// otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a Shelf. Continue validating if yes, otherwise stop.
function isSupervisor(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'Supervisor') return next(err);
	// otherwise stop validating (don't call next validation function)
};
