function showTechnologyWindow(techs , cell){

	var content = '<div id="tech-dialog" title="Set Supervisor Technology">'
		+ 'Technology: <select>';
	for(var i = 0; i < techs.length; i++){
		content += '<option value="'+techs[i]+'">'+techs[i]+'</option>';
	}
	content += '</select>';
	+ '</div>'

	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Set Supervisor Technology',
		content: content,
		modal: true ,
		closeButton: false ,
		close: CloseFunction,
		buttons: [
		          { action: 'save', content: 'Save', position: 'left' }
		          ],	          
	});

	function CloseFunction(){
		cell.set('tech' ,($('#tech-dialog').find(":selected").val()) );
		dialog.close();
	};

	($('#tech-dialog').on( "dialogclose", function(dialogclose) {
		cell.set('tech' ,($('#tech-dialog').find(":selected").val()));
		dialog.close();
	} ));



	dialog.on('action:save', function(){
		cell.set('tech' ,($('#tech-dialog').find(":selected").val()) );
		//var result = setTechnology(SupervisorType, SupervisorID , tech);
		var result = "success";
		if(result === "success"){
			dialog.close();
		}else{
			//cell.set('tech' ,($('#tech-dialog').find(":selected").val()));
			alert("alert");
		}
	});
	dialog.open();
}; 

function equipmentHandle(graph){

//	graph.on('all' , function(a){
//	console.log(a);
//	});

	// when a cell is added on another one, it should be embedded
	graph.on('add', function(cell) {

		//console.log(JSON.stringify(cell));
		if(cell.get('type') === 'link') return;

		//	console.log(cell);
		//	console.log(cell.attr);
		var equipmentID = cell.get('id');
		var equipmentType = cell.get('subType');
		var equipmentName = getName(equipmentType);

		var position = cell.get('position');
		var size = cell.get('size');
		var area = g.rect(position.x, position.y, size.width, size.height);

		var parent;			
		_.each(graph.getElements(), function(e) {

			var position = e.get('position');
			var size = e.get('size');
			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
				parent = e;
			}
		});
		if(parent) {

			var filhos = parent.getEmbeddedCells().length;
			var soa = parent.getEmbeddedCells() ;

			var pposition = parent.get('position');
			var psize = parent.get('size');

			var containerType = parent.get('subType');
			var containerID = parent.get('id');
			var containerName = parent.attributes.attrs.name.text;

			if(parent.get('subType') === 'Rack') {                   
				//equipamento em um Rack
				// consultar ontologia para inserção 

//				if(this.skipOntologyAddHandler === true){
//				//	console.log("WORKED");
//				this.skipOntologyAddHandler = false;
//				return;
//				};

				var result = EquipStudioInsertContainer(equipmentName , equipmentType, equipmentID ,containerName , containerType , containerID);
				//console.log('try to insert equipment ' +equipmentID+ ' name: ' +equipmentName+ ';type: ' +equipmentType+ ';container: ' +containerID+ ';conatainer: ' +containerType);
				if(result === "success") {

					parent.embed(cell);
					cell.attr({
						name: { text: equipmentName},
					});
					nextName(equipmentType);

					var a = parent.get('embeds');
					var maior = 0;
					for (var i = 0; i < a.length; i++) {
						var Shelf = graph.getCell(a[i]);

						Shelf.set('position', {
							x: ((pposition.x) + 15) ,
							y: (pposition.y + 20 + (i*(80))) 
						});


					}
					parent.set('size' , { 
						width: psize.width  ,
						height:	265 + ((parent.getEmbeddedCells().length - (2) ) * 77.5)
					});
				} else {
					this.skipOntologyRemoveHandler = true;
					cell.remove();
					return new joint.ui.Dialog({
						type: 'alert',
						width: 400,
						title: 'Alert',
						content: result,
					}).open();		
				}
			}else{
				if(parent.get('subType') === 'Shelf'){
					//equipamento em uma Shelf
					var grandparentId = parent.get('parent');
					if (!grandparentId) return;
					var grandparent = graph.getCell(grandparentId);

					var containerType = parent.get('subType');
					var containerID = parent.get('id');

					var result = EquipStudioInsertContainer( equipmentName , equipmentType, equipmentID ,containerName, containerType , containerID);
					if(result === "success") {
						parent.embed(cell)

						cell.attr({
							name: { text: equipmentName},
						});
						nextName(equipmentType);

						var b = parent.get('embeds');
						var maior = 0;
						for (var i = 0; i < b.length; i++) {
							var Slot = graph.getCell(b[i]);
							Slot.set('position', {
								x: pposition.x + 20 + ((i) * (42.5)) ,
								y: pposition.y + 7 
							});

						}
						parent.set('size' , { 
							width: 105 + ((parent.getEmbeddedCells().length - (1) ) * 42.5) ,
							height:	parent.get('size').height
						});

						var c = grandparent.get('embeds');
						var maior = (parent.get('size').width);
						var maiorShelf1 = parent;
						for (var i = 0; i < c.length; i++) {
							var Shelf1 = graph.getCell(c[i]);
							if (Shelf1){							
								if (maior < (Shelf1.get('size').width)) {
									maior = (Shelf1.get('size').width);
									maiorShelf1 = Shelf1;
								}
								grandparent.set ('size' , {
									width: maiorShelf1.get('size').width + 40 ,
									height: grandparent.get('size').height
								});
							}
						};
					}else{
						this.skipOntologyRemoveHandler = true;
						cell.remove();
//						return new joint.ui.Dialog({
//						type: 'alert',
//						width: 400,
//						title: 'Alert',
//						content: result,
//						}).open();	
					}
				}else {
					if(parent.get('subType') === 'Slot'){
						//equipamento em um Slot
						var containerType = parent.get('subType');
						var containerID = parent.get('id');


						var newpositionx;
						if (parent.getEmbeddedCells().length === '0'){
							newpositionx = pposition.x + 6;
						}else {
							//Um Card por Slot
							newpositionx = pposition.x + 6 + ((filhos) * (29));
						};
						var result = EquipStudioInsertContainer( equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
						if(result === "success") {
							// Se já possuir um Card (confirmar o metodo para Card e Supervisor)


							if (parent.getEmbeddedCells().length === 1){	
								new joint.ui.Dialog({
									type: 'alert',
									width: 400,
									title: 'Alert',
									content: 'A Slot can only contain one Card.'
								}).open();
								this.skipOntologyRemoveHandler = true;
								cell.remove();
								return;
							}else{									
//								if (cell.get('subType') === 'Supervisor'){									

//								parent.embed(cell);
//								showTechnologyWindow(getTechnologies() , cell);									

////								if(cell.get('tech') === ""){
////								showTechnologyWindow(getTechnologies() , cell);
////								};
//								cell.set('size' , {
//								width: 10 ,
//								height: 20							
//								});
//								cell.set('position' , {
//								x : newpositionx ,
//								y : ((pposition.y) + 16)
//								});
//								};

								if (cell.get('subType') === 'Card'){									
									parent.embed(cell);	
									//nsCards.push(cell);
									//console.log(nsCards);
									cell.set('size' , {
										width: 10 ,
										height: 20							
									});
									cell.set('position' , {
										x : newpositionx ,
										y : ((pposition.y) + 16)
									});

									cell.attr({
										name: { text: equipmentName},
									});
									nextName(equipmentType);

								};
							}
						}else{

							var result = EquipStudioInsertContainer( equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);
							if (result === "success"){
								parent.embed(cell);
								showTechnologyWindow(getTechnologies() , cell);

								cell.set('size' , {
									width: 10 ,
									height: 20							
								});
								cell.set('position' , {
									x : newpositionx ,
									y : ((pposition.y) + 16)
								});

								cell.attr({
									name: { text: equipmentName},
								});
								nextName(equipmentType);

							}else{

								new joint.ui.Dialog({
									type: 'alert',
									width: 400,
									title: 'Alert',
									content: result,
								}).open();
								this.skipOntologyRemoveHandler = true;
								cell.remove();

							}
						}
					}else{
						if(parent.get('subType') === 'Card' || 'Supervisor' ){
							// configurar as portas de input e output quando sair do itu
						}
					}
				}
			}
		}else{
			//Only the Rack can be inserted into the graph without an equipment holder	
			var containerType;
			var containerID;

//			if(this.skipOntologyAddHandler === true){
//			console.log("WORKED");
//			this.skipOntologyAddHandler = false;
//			return;
//			};
			//console.log("failed");
			var result = EquipStudioInsertContainer(equipmentName , equipmentType, equipmentID);
			if(result === "success") {    

				cell.attr({
					name: { text: equipmentName},
				});
				nextName(equipmentType);


			}else{
				//	console.log(result);
				new joint.ui.Dialog({
					type: 'alert',
					width: 400,
					title: 'Error',
					content: (result),
				}).open();
				cell.remove();
			}

		}

	}, this);

	graph.on('remove' , function (cell) {

		//     var copy = cell;
		//       console.log(cell);
		var parentId = cell.get('parent');
		var parent = graph.getCell(parentId);

		if (parent){


			if(parent.get('subType') === 'Rack'){

				var pposition = parent.get('position');
				var psize = parent.get('size');			
				var newpositionx = pposition.x + 15 ;

				//	console.log("to del: ", cell);
				var d = parent.get('embeds');
				var maiorl = 0;
				var Shelfw;
				var sons;

				var l = 0;

				for (var i = 0; i < d.length; i++) {
					var Shelf = graph.getCell(d[i]);			
					if (Shelf){
						//	console.log("changed: " , Shelf);

						Shelf.set('position', {
							x: newpositionx ,
							y: (pposition.y + 20 + ((l)*(80))) 
						});

						var sposition = Shelf.get('position');

						sons = Shelf.getEmbeddedCells().length;

						if ( maiorl < sons ){
							maiorl = sons;
							Shelfw = Shelf.get('size').width;
						}

						var reSlot = Shelf.get('embeds');
						var k;
						for (k=0; k < (reSlot.length); k++){
							var inShelf = graph.getCell(reSlot[k]);	
							if(inShelf){			
								var childId = inShelf.get('embeds');	
								var inSlot = graph.getCell(childId);																								
								inShelf.set('position', {
									y: sposition.y + 7 ,
									x: sposition.x + 20 + ((k) * (42.5)) ,
								});

								var Slotpos = inShelf.get('position');

								if(inSlot){
									inSlot.set('position', {
										y: Slotpos.y + 16 ,
										x: Slotpos.x + 6
									});
								};
								Shelf.set('size', {
									width: Shelf.get('size').width,
									height: 67.5 },
									{skipParentHandler : false});		
							}
						}
						l++;
					}
				}
				if (maiorl === 0){
					parent.set('size' , { 
						width: 120   ,
						height:	265 + ((parent.getEmbeddedCells().length - (3) ) * 77.5)
					});
				}else{
					parent.set('size' , { 
						width: (Shelfw) + 40  ,
						height:	265 + ((parent.getEmbeddedCells().length - (3) ) * 77.5)
					});
				} 			


			};

			if(parent.get('subType') === 'Shelf'){

				var grandparentId = parent.get('parent');
				if (!grandparentId) return;
				var grandparent = graph.getCell(grandparentId);

				var pposition = parent.get('position');
				var e = parent.get('embeds');
				var l = 0;
				for(var j=0; j < e.length;j++){
					var inShelf = graph.getCell(e[j]);
					if(inShelf){              	

						inShelf.set('position', {
							x: pposition.x + 20 + ((l) * (42.5)) ,
							y: pposition.y + 7 
						});

						var childId = inShelf.get('embeds');	
						var inSlot = graph.getCell(childId);	

						var Slotpos = inShelf.get('position');

						if(inSlot){
							inSlot.set('position', {
								y: Slotpos.y + 16 ,
								x: Slotpos.x + 6
							});
						};
						l++;
					}
				}

				if( parent.getEmbeddedCells().length === 1){
					parent.set('size' , { 
						width: 105 + ((parent.getEmbeddedCells().length - (1) ) * 42.5) ,
						height:	parent.get('size').height});
				}else{
					parent.set('size' , { 
						width: 105 + ((parent.getEmbeddedCells().length - (2) ) * 42.5) ,
						height:	parent.get('size').height});
				}

				var t = grandparent.get('embeds');
				var k=0;
				var maiorl = 0;
				var inRackw;
				var sons=0;

				for(var p=0; p < t.length; p++){
					var inRack = graph.getCell(t[p]);

					sons = inRack.getEmbeddedCells().length;

					if ( maiorl < sons ){
						maiorl = sons;
						inRackw = inRack.get('size').width;
					}   
				}	
				if(maiorl === 0){
					grandparent.set('size' , {
						width: 120 ,
						height : 265 + ((grandparent.getEmbeddedCells().length - (2) ) * 77.5)
					})
				}else{
					grandparent.set('size' , { 
						width: (inRackw) + 40  ,
						height:	265 + ((grandparent.getEmbeddedCells().length - (2) ) * 77.5)
					});	
				}

			};

			if(parent.get('subType') === 'Slot'){

				var grandparentId = parent.get('parent');
				if (!grandparentId) return;

				var grandparent = graph.getCell(grandparentId);

				if(cell.get('subType') === 'Supervisor'){
					var cellID = cell.get('id');
					//procurar no grafico inteiro pelo nome da Supervisor
					var elementos = graph.getElements();
					for(var i = 0; i < elementos.length; i++){
						var equipment = elementos[i];
						if((equipment.attributes.subType) === 'Card'){
							if((equipment.get('SupervisorID') === (cellID))){
								equipment.set("Supervisor" , '');
								equipment.set("SupervisorID" , '');
							}
						}
					}
				};
				parent.set('size' , {
					width: 22.5 ,
					height: 52.5
				});

			};

		};
	},this); 

	graph.on('change:size', function(cell, newPosition, opt) {

//		if (opt.skipParentHandler) return;

//		if (cell.get('embeds') && cell.get('embeds').length) {
//		// If we're manipulating a parent element, let's store
//		// it's original size to a special property so that
//		// we can shrink the parent element back while manipulating
//		// its children.
//		cell.set('originalSize', cell.get('size'));
//		}
	});

	graph.on('change' , function (cell) {
		//The equipment cant be removed from inside the equipmentholder
		var parentId = cell.get('parent');
		if (!parentId) return;

		var parent = graph.getCell(parentId);
		var parentBbox = parent.getBBox();
		var cellBbox = cell.getBBox();

		if (parentBbox.containsPoint(cellBbox.origin()) && parentBbox.containsPoint(cellBbox.topRight()) &&
				parentBbox.containsPoint(cellBbox.corner()) &&
				parentBbox.containsPoint(cellBbox.bottomLeft())) {

			return;
		}
		cell.set('position', cell.previous('position'));

	},this);

	function getName(equipmentSubtype) {
		if(equipmentSubtype === 'Rack') return 'Rack_' +app.RackCounter;
		if(equipmentSubtype === 'Shelf') return 'Shelf_' +app.ShelfCounter;
		if(equipmentSubtype === 'Slot') return 'Slot_' +app.SlotCounter;
		if(equipmentSubtype === 'Card') return 'Card_' +app.CardCounter;
		if(equipmentSubtype === 'Supervisor') return 'Supervisor_' +app.SupervisorCounter;
	};

	function nextName(equipmentSubtype) {
		if(equipmentSubtype === 'Rack') app.RackCounter++;
		if(equipmentSubtype === 'Shelf') app.ShelfCounter++;
		if(equipmentSubtype === 'Slot') app.SlotCounter++;
		if(equipmentSubtype === 'Card') app.CardCounter++;
		if(equipmentSubtype === 'Supervisor') app.SupervisorCounter++;
	};

};
