

function equipmentHandle(graph){

	// when a cell is added on another one, it should be embedded
	graph.on('add', function(cell) {

		//console.log(JSON.stringify(cell));
		if(cell.get('type') === 'link') return;

		var equipmentID = cell.get('id');
		var equipmentType = cell.get('subType');
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


			if(parent.get('subType') === 'rack') {                   
				// consultar ontologia para inserção de camada no card
				//var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);
				var result = "success";
				//console.log('try to insert equipment ' +equipmentID+ ' name: ' +equipmentName+ ';type: ' +equipmentType+ ';container: ' +containerID+ ';conatainer: ' +containerType);

				if(result === "success") {
					parent.embed(cell);

					var a = parent.get('embeds');
					var i;
					var maior = 0;
					for (i = 0; i < a.length; i++) {
						var shelf = graph.getCell(a[i]);

						shelf.set('position', {
							x: ((pposition.x) + 15) ,
							y: (pposition.y + 20 + (i*(80))) 
						});
					}
					parent.set('size' , { 
						width: psize.width  ,
						height:	265 + ((parent.getEmbeddedCells().length - (2) ) * 77.5)
					});
				} else {
					cell.remove();
					return new joint.ui.Dialog({
						type: 'alert',
						width: 400,
						title: 'Alert',
						content: 'The rack can only contain a shelf.'
					}).open();		
				}
			}else{
				if(parent.get('subType') === 'shelf'){

					var grandparentId = parent.get('parent');
					if (!grandparentId) return;
					var grandparent = graph.getCell(grandparentId);

					var containerType = parent.get('subType');
					var containerID = parent.get('id');

					//var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);
					var result = "success";
					//var result = "";
					if(result === "success") {
						parent.embed(cell)
						var b = parent.get('embeds');
						var i;
						var maior = 0;
						for (i = 0; i < b.length; i++) {
							var slot = graph.getCell(b[i]);
							slot.set('position', {
								x: pposition.x + 20 + ((i) * (42.5)) ,
								y: pposition.y + 7 
							});
						}
						parent.set('size' , { 
							width: 105 + ((parent.getEmbeddedCells().length - (1) ) * 42.5) ,
							height:	parent.get('size').height
						});

						var c = grandparent.get('embeds');
						var i;
						var maior = (parent.get('size').width);
						var maiorshelf1 = parent;
						for (i = 0; i < c.length; i++) {
							var shelf1 = graph.getCell(c[i]);
							if (shelf1){							
								if (maior < (shelf1.get('size').width)) {
									maior = (shelf1.get('size').width);
									maiorshelf1 = shelf1;
									console.log('a maior shelf :', maiorshelf1);
									console.log('maior: ', maior);
								}
								grandparent.set ('size' , {
									width: maiorshelf1.get('size').width + 40 ,
									height: grandparent.get('size').height
								});
							}
						};
					}else{
						cell.remove();
						return new joint.ui.Dialog({
							type: 'alert',
							width: 400,
							title: 'Alert',
							content: 'Shelf only contains slots.'
						}).open();	
					}
				}else {
					if(parent.get('subType') === 'slot'){

						var containerType = parent.get('subType');
						var containerID = parent.get('id');

						var newpositionx;
						if (parent.getEmbeddedCells().length === '0'){
							newpositionx = pposition.x + 6;
							console.log('nova posição da cell em x ' , newpositionx);
						}else {
							//Um card por slot
							newpositionx = pposition.x + 6 + ((filhos) * (29));
							console.log('nova posição da cell em x sendo o segundo ' , newpositionx);
						};
						//var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);
						var result = "success";
						//var result = "";
						if(result === "success") {
							// Se já possuir um card ( confirmar o metodo para card e supervisor)
							if (parent.getEmbeddedCells().length === 1){
								new joint.ui.Dialog({
									type: 'alert',
									width: 400,
									title: 'Alert',
									content: 'A slot can only contain one card.'
								}).open();
								cell.remove();
                                return;
							}else{
								parent.embed(cell);
								
								var resultados = getTechnologies();
                                console.log(resultados);
                                
								cell.set('size' , {
									width: 10 ,
									height: 20							
								});
								cell.set('position' , {
									x : newpositionx ,
									y : ((pposition.y) + 16)
								});
							}
						}else{
							cell.remove();
							return 	new joint.ui.Dialog({
								type: 'alert',
								width: 400,
								title: 'Alert',
								content: 'Only cards(supervisor or not) can be insert into a slot.'
							}).open();	
						}
					}else{
						if(parent.get('subType') === 'card' || 'supervisor' ){
							// configurar as portas de input e output quando sair do itu
						}
					}
				}
			}
		}
		else{
			//Impedir o usuario de insirir um equipamento fora de um rack
			//var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID)
			//var result = "success";
//			var result = "";
//			if(result === "success") {
//			return;
//			}else{
//			return (new joint.ui.Dialog({
//			type: 'alert' ,
//			width: 420,
//			draggable: false,
//			title: 'Alert ',
//			content: 'The equipment selected must be connected in another equipment!'
//			})).open();
//			cell.remove();
//			}
		}
	}, this);

	graph.on('remove' , function (cell) {

		//		Mensagem de confirmação do remover
//		if((cell.get('parent') === ) && (!(cell.get('subType') === 'rack'))) {
//		}else {
//		var dialog = new joint.ui.Dialog({
//		type: 'warning' ,
//		width: 400,
//		draggable: false,
//		title: 'Warning ',
//		content: 'Are you sure you want to delete this equipment? Everything conected with it will be deleted.',
//		buttons: [
//		{ action: 'yes', content: 'Yes' },
//		{ action: 'no', content: 'No' }
//		]
//		});
//		dialog.on('action:yes', dialog.close, remove );
//		dialog.on('action:no', dialog.close, dialog);
//		dialog.open();

//		function remove (parent, cell) {
//		if(opt.skipRemove : true) return;

		var parentId = cell.get('parent');
		if (!parentId) return;
		var parent = graph.getCell(parentId);

		if(parent.get('subType') === 'rack') {

			var pposition = parent.get('position');
			var psize = parent.get('size');			
			var newpositionx = pposition.x + 15 ;

			console.log("to del: ", cell);
			var d = parent.get('embeds');
			var i;
			var maiorl = 0;
			var shelfw;
			var sons;

			l = 0;

			for (i = 0; i < d.length; i++) {
				var shelf = graph.getCell(d[i]);			
				if (shelf){
					console.log("changed: " , shelf);

					shelf.set('position', {
						x: newpositionx ,
						y: (pposition.y + 20 + ((l)*(80))) 
					},{skipParentHandler : true});

					var sposition = shelf.get('position');

					sons = shelf.getEmbeddedCells().length;
					console.log(sons);

					if ( maiorl < sons ){
						maiorl = sons;
						console.log(maiorl);
						shelfw = shelf.get('size').width;
						console.log(shelfw);
					}

					var reslot = shelf.get('embeds');
					var k;
					for (k=0; k < (reslot.length); k++){
						var inshelf = graph.getCell(reslot[k]);	
						if(inshelf){
							//console.log('inshelf ' , inshelf)					
							var childId = inshelf.get('embeds');	
							var inslot = graph.getCell(childId);													
//							console.log(inslot);												
							inshelf.set('position', {
								y: sposition.y + 7 ,
								x: sposition.x + 20 + ((k) * (42.5)) ,
							});

							var slotpos = inshelf.get('position');

							if(inslot){
								inslot.set('position', {
									y: slotpos.y + 16 ,
									x: slotpos.x + 6
								});
							};
							shelf.set('size', {
								width: shelf.get('size').width,
								height: 67.5 },
								{skipParentHandler : false});		
							//console.log('positon ', inshelf.get('position'));
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
					width: (shelfw) + 40  ,
					height:	265 + ((parent.getEmbeddedCells().length - (3) ) * 77.5)
				});
			} 			
		}else{
			if(parent.get('subType') === 'shelf'){
				//selecionar o rack
				var grandparentId = parent.get('parent');
				if (!grandparentId) return;
				var grandparent = graph.getCell(grandparentId);

				var pposition = parent.get('position');
				var e = parent.get('embeds');
				var j;
				var l = 0;
				for(j=0; j < e.length;j++){
					var inshelf = graph.getCell(e[j]);
					if(inshelf){              	

						inshelf.set('position', {
							x: pposition.x + 20 + ((l) * (42.5)) ,
							y: pposition.y + 7 
						});

						var childId = inshelf.get('embeds');	
						var inslot = graph.getCell(childId);	

						var slotpos = inshelf.get('position');

						if(inslot){
							inslot.set('position', {
								y: slotpos.y + 16 ,
								x: slotpos.x + 6
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


//				var i;
//				var maior = 0;
//				var maiorinrack;
//				var inrackw;
//				var c = grandparent.get('embeds');
//				for (i = 0; i < c.length; i++) {					
//				var inrack = graph.getCell(c[i]);
//				inracksons = inrack.getEmbeddedCells().length;

//				if(inrack){							
//				if(maior < inracksons) {
//				maior = inracksons;
//				maiorshelf1 = inrack;
//				inrackw = inrack.get('size').width;
//				console.log('a maior shelf do remo :', inrackw);
//				console.log('maior do remo: ', inrack);
//				}	
//				}
//				};
//				if(maior === 0){
//				grandparent.set ('size' , {
//				width: 120 ,
//				heigth: 265 + ((parent.getEmbeddedCells().length - (2) ) * 77.5)
//				});
//				}else{
//				grandparent.set ('size' , {
//				width: inrackw + 40 ,
//				heigth: 265 + ((parent.getEmbeddedCells().length - (2) ) * 77.5)
//				});
//				}


				var t = grandparent.get('embeds');
				var p;
				var k=0;
				var maiorl = 0;
				var inrackw;
				var sons=0;

				for(p=0; p < t.length; p++){
					var inrack = graph.getCell(t[p]);

					sons = inrack.getEmbeddedCells().length;

					if ( maiorl < sons ){
						maiorl = sons;
						//console.log(maiorl);
						inrackw = inrack.get('size').width;
						//console.log(shelfw);
					}   
				}	
				if(maiorl === 0){
					grandparent.set('size' , {
						width: 120 ,
						height : 265 + ((grandparent.getEmbeddedCells().length - (2) ) * 77.5)
					})
				}else{
					grandparent.set('size' , { 
						width: (inrackw) + 40  ,
						height:	265 + ((grandparent.getEmbeddedCells().length - (2) ) * 77.5)
					});	
				}


			}else{

				if(parent.get('subType') === 'slot'){

					var grandparentId = parent.get('parent');
					if (!grandparentId) return;

					var grandparent = graph.getCell(grandparentId);

					parent.set('size' , {
						width: 22.5 ,
						height: 52.5
					});

				}if(parent.get('subType') === 'card' || 'supervisor' ) {

				}
			}
		};
	},this); 

//	graph.on('all' , function(a) {
//	console.log(a);
//	},this);

	graph.on('change:size', function(cell, newPosition, opt) {

		if (opt.skipParentHandler) return;

		if (cell.get('embeds') && cell.get('embeds').length) {
			// If we're manipulating a parent element, let's store
			// it's original size to a special property so that
			// we can shrink the parent element back while manipulating
			// its children.
			cell.set('originalSize', cell.get('size'));
		}
	});


	graph.on('change' , function (cell) {
//		Impedir o equipamento filho de sair da area do equipamento pai

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

	graph.on('change:position', function(cell, newPosition, opt) {
//		Metodo para expandir o parent ao arrastar a child para a borda do parent

		//		if (opt.skipRemoveHandler) return;
//		if (opt.skipParentHandler) return;

//		if (cell.get('embeds') && cell.get('embeds').length) {
//		// If we're manipulating a parent element, let's store
//		// it's original position to a special property so that
//		// we can shrink the parent element back while manipulating
//		// its children.
//		cell.set('originalPosition', cell.get('position'));
//		}

//		var parentId = cell.get('parent');
//		if (!parentId) return;

//		var parent = graph.getCell(parentId);
//		var parentBbox = parent.getBBox();

//		if (!parent.get('originalPosition')) parent.set('originalPosition', parent.get('position'));
//		if (!parent.get('originalSize')) parent.set('originalSize', parent.get('size'));

//		var originalPosition = parent.get('originalPosition');
//		var originalSize = parent.get('originalSize');

//		var newX = originalPosition.x;
//		var newY = originalPosition.y;
//		var newCornerX = originalPosition.x + originalSize.width;
//		var newCornerY = originalPosition.y + originalSize.height;

//		_.each(parent.getEmbeddedCells(), function(child) {

//		var childBbox = child.getBBox();

//		if (childBbox.x < newX) { newX = childBbox.x; }
//		if (childBbox.y < newY) { newY = childBbox.y; }
//		if (childBbox.corner().x > newCornerX) { newCornerX = childBbox.corner().x; }
//		if (childBbox.corner().y > newCornerY) { newCornerY = childBbox.corner().y; }
//		});

//		// Note that we also pass a flag so that we know we shouldn't adjust the
//		// `originalPosition` and `originalSize` in our handlers as a reaction
//		// on the following `set()` call.
//		parent.set({
//		position: { x: newX, y: newY },
//		size: { width: newCornerX - newX, height: newCornerY - newY }
//		}, { skipParentHandler: true });
	},this);


};

function embedOrConnect (parent, child) {

	var parentsubType = parent.get('subType');
	var childsubType = child.get('subType');
	if(parentsubType === 'rack' && childsubType === 'shelf') {
		parent.embed(child);
		console.log('embedded!parent suType: ' +parentsubType+ '; child subType: ' +childsubType);
	}else{
		if((parentsubType === 'shelf') && (childsubType === 'slot')) {
			parent.embed(child);
			console.log('embedded!parent suType: ' +parentsubType+ '; child subType: ' +childsubType);
		}else{
			if((parentsubType === 'slot') && (childsubType === 'card')) {
				parent.embed(child);
				console.log('embedded! parent suType: ' +parentsubType+ '; child subType: ' +childsubType);
			}else {
				if((parentsubType === 'slot') &&  (childsubType === 'supervisor')) {
					parent.embed(child);
					console.log('embedded! parent suType: ' +parentsubType+ '; child subType: ' +childsubType);
				}else{
					if((parentsubType === 'card') && (childsubType === 'in')){
						parent.embed(child);
						console.log('embedded! parent suType: ' +parentsubType+ '; child subType: ' +childsubType);
					}else{
						new joint.ui.Dialog({
							type: 'alert',
							width: 300,
							title: 'Alert',
							content: 'Wrong Order!'
						}).open();
						console.log("nao conectou!");	
						//(childsubType === 'in' || childsubType === 'out')
					}
				}
			}
		}
	}

};
