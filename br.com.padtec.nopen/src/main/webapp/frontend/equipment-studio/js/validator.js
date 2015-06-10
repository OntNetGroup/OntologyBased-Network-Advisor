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
			if(cellsubType === "supervisor"){
				//procurar no grafico inteiro pelo nome da supervisor
				var elementos = graph.getElements();
				for(var i = 0; i < elementos.length; i++){
					var equipment = elementos[i];
					if((equipment.attributes.subType) === 'card'){
						if((equipment.get('supervisorID') === (cellID))){
							equipment.set("supervisor" , (cellName));
						}
					}
				}
			}
			return next(err);
		}else{
			next(result);
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
