function validator(validator, graph, app) {
	
	
	
	validator.validate('remove',isRack,_.bind(function(err,command,next){
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
	    console.log(equipmentName);
	    console.log(equipmentType);
	    console.log(command);
	    var containerName= '';
	    	var containerType= '';
	    		var containerID= '';
	    
	    //var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
	    result = "success";
	    if(result === "success"){
	    	return next(err);
	    }else{
	    	return next(result);	
	    }
}, app));
	
	
	validator.validate('remove',isShelf,_.bind(function(err,command,next){
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
		    console.log(equipmentName);
		    console.log(equipmentType);
		    console.log(command);
		    console.log(command.data.attributes.parent);
		    var holderID = command.data.attributes.parent;
		    console.log('i' ,holderID);
		    var holder =  graph.getCell(holderID);
		   // console.log(container);
		    var holderName= holder.data.attributes.attrs.name.text;
//		    var holderType= holder.data.attributes.subType;
//		    var holderID= holder.data.id;
		    console.log(containerID);
		    console.log(containerName);
		    console.log(containerType);
		    //var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
		    result = "success";
		    if(result === "success"){
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
