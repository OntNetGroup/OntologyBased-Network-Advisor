function validator(validator, graph, app) {
	
	
	
	this.validator.validate('remove',isRack,_.bind(function(err,command,next){
	//removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID)
	
		if(this.skipOntologyRemoveHandler) {
    		this.skipOntologyRemoveHandler = false;
    		return next(err);
    	}
		
		var equipmentID = command.data.id;
		var equipmentName = command.data.attributes.attrs.name.text;
	    var equipmentType = command.data.attributes.subType;
	   // var equipmentID = cell.parent;
	    console.log(equipmentID);
	    console.log(cell);
	    console.log(equipmentName);
	    console.log(equipmentType);
	    console.log(command);
	    
	    var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
	    
	    if(result === "sucess"){
	    	return next(err);
	    }else{
	    	return next(result);	
	    }
}, app));
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

//Check if cell in command is a rack. Continue validating if yes, otherwise stop.
function isRack(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'rack') return next(err);
 // otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a shelf. Continue validating if yes, otherwise stop.
function isShelf(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'shelf') return next(err);
 // otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a slot. Continue validating if yes, otherwise stop.
function isSlot(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'slot') return next(err);
 // otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a card. Continue validating if yes, otherwise stop.
function isCard(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'card') return next(err);
 // otherwise stop validating (don't call next validation function)
};

//Check if cell in command is a shelf. Continue validating if yes, otherwise stop.
function isSupervisor(err, command, next) {
	var cellSubType = command.data.attributes.subType;

	if(cellSubType === 'supervisor') return next(err);
 // otherwise stop validating (don't call next validation function)
};
