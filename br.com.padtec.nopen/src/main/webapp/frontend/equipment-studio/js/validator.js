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

	    var containerName= '';
	   	var containerType= '';
	    var containerID= '';
	    
	 //  var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
	    result = "successss!";
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

		    var holderID = command.data.attributes.parent;
		    var holder =  graph.getCell(holderID);

		    var containerName = holder.attributes.attrs.name.text;
		    var containerType = holder.get('subType');
		    var containerID = holder.get('id');

		   // var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
		    result = "success";
		    if(result === "success"){
		    	return next(err);
		    }else{
		    	return next(result);	
		    }
	}, app));
	
	
	validator.validate('remove',isSlot,_.bind(function(err,command,next){
		//removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID)
		
			if(this.skipOntologyRemoveHandler) {
	    		this.skipOntologyRemoveHandler = false;
	    		return next(err);
	    	}
			
			var equipmentID = command.data.id;
			var equipmentName = command.data.attributes.attrs.name.text;
		    var equipmentType = command.data.attributes.subType;
	
		    var holderID = command.data.attributes.parent;
		    var holder =  graph.getCell(holderID);

		    var containerName = holder.attributes.attrs.name.text;
		    var containerType = holder.get('subType');
		    var containerID = holder.get('id');

		  //  var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
		    result = "success";
		    if(result === "success"){
		    	return next(err);
		    }else{
		    	return next(result);	
		    }
	}, app));
	
	
	validator.validate('remove',isCard,_.bind(function(err,command,next){
		//removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID)
		
			if(this.skipOntologyRemoveHandler) {
	    		this.skipOntologyRemoveHandler = false;
	    		return next(err);
	    	}
			
			var equipmentID = command.data.id;
			var equipmentName = command.data.attributes.attrs.name.text;
		    var equipmentType = command.data.attributes.subType;

		    var holderID = command.data.attributes.parent;
		    var holder =  graph.getCell(holderID);

		    var containerName = holder.attributes.attrs.name.text;
		    var containerType = holder.get('subType');
		    var containerID = holder.get('id');

		  //  var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
		    result = "success";
		    if(result === "success"){
		    	return next(err);
		    }else{
		    	return next(result);	
		    }
	}, app));
	
	
	validator.validate('remove',isSupervisor,_.bind(function(err,command,next){
		//removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID)
		
			if(this.skipOntologyRemoveHandler) {
	    		this.skipOntologyRemoveHandler = false;
	    		return next(err);
	    	}
			
			var equipmentID = command.data.id;
			var equipmentName = command.data.attributes.attrs.name.text;
		    var equipmentType = command.data.attributes.subType;
	
		    var holderID = command.data.attributes.parent;
		    var holder =  graph.getCell(holderID);

		    var containerName = holder.attributes.attrs.name.text;
		    var containerType = holder.get('subType');
		    var containerID = holder.get('id');

		  //  var result = removeEquipmentholder(equipmentName, equipmentType, equipmentID , containerName , containerType , containerID);
		    result = "success";
		    if(result === "success"){
		    	return next(err);
		    }else{
		    	return next(result);	
		    }
	}, app));

	 validator.validate('change:attrs', _.bind(function(err, command, next) {
	    	var previousName = command.data.previous.attrs.text.text;
	    	
	    	if(_.contains(['Rack', 'Shelf', 'Slot', 'Card', 'Supervisor'], previousName)) { // verify if it's the first change of name (element being added)
	    		return next(err);
	    	}
	    	
//	    	var cellID = command.data.id;
//	    	var cell = this.graph.getCell(cellID);
//	    	var cellName = command.data.next.attrs.text.text;
//	    	var cellType = cell.attributes.type;
//	    	var cellSubtype = cell.attributes.subtype;
//	    	
//	    	if(cellType === TypeEnum.TRANSPORT_FUNCTION) {
//	    		var result = setTransportFunctionName(cellID, cellName, cellSubtype);
//	    	} else {
//	    		var result = setPortName(cellID, cellName, cellSubtype);
//	    	}
//	    	if(result === "success") {
//	    		return next(err);
//	    	} else {
//	    		next(result);
//	    	}
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
