

function equipmentHandle(graph){

	// when a cell is added on another one, it should be embedded
	graph.on('add', function(cell) {

		//console.log(JSON.stringify(cell));
		if(cell.get('type') === 'link') return;

		var subtype = cell.get('subType');
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

		// See if the cell can connect with the parent : true or false


		if(parent) {
			//outros equipamentos conectados
			var filhos = parent.getEmbeddedCells().length;
			var soa = parent.getEmbeddedCells() ;
			console.log(filhos);
			//parent position and size
			var pposition = parent.get('position');
			var psize = parent.get('size');

			//child position and size
			var cposition = cell.get('position');
			var csize = cell.get('size');

			if(parent.get('subType') === 'rack') {

				//Aumento de tamanho e re posicionamento
				var newpositionx = pposition.x + 15 ;
				var nempositiony;
				if (parent.getEmbeddedCells().length === '0'){
					nempositiony = pposition.y + 20 ;
				}else {
					nempositiony = pposition.y + 20 + ((filhos) * (80));
				};
				// Prototype : check order
				if(cell.get('subType') === 'shelf'){
					parent.embed(cell);
					embedOrConnect(parent, cell);

					parent.set('size' , { 
						width: psize.width  ,
						height:	240 + ((parent.getEmbeddedCells().length - (1) ) * 70)
					});
					cell.set('position', {
						x: newpositionx  ,
						y: nempositiony 
					});
				}else{
					console.log('A ordem está errada');
				}

			}else{
				if(parent.get('subType') === 'shelf'){
					// selecionar o rack
					var grandparentId = parent.get('parent');
					if (!grandparentId) return;

					var grandparent = graph.getCell(grandparentId);

					var newpositiony = pposition.y + 7;
					var nempositionx;
					if (parent.getEmbeddedCells().length === '0'){
						nempositionx = pposition.x + 10;
					}else {
						nempositionx = pposition.x + 20 + ((filhos) * (75));
					};
					if(cell.get('subType') === 'slot'){
						parent.embed(cell);
						embedOrConnect(parent, cell);

						parent.set('size' , { 
							width: 100 + ((parent.getEmbeddedCells().length - (1) ) * 75) ,
							height:	parent.get('size').height});

						cell.set('position', {
							x: nempositionx,
							y: newpositiony
						});

						if (grandparent.get('size').width < parent.get('size').width){
							grandparent.set ('size' , {
								width: grandparent.get('size').width + 100 ,
								height: grandparent.get('size').height
							});
						};
					}else{
						console.log("A ordem está errada");
					}

				}else {
					if(parent.get('subType') === 'slot'){
						var increasew = (psize.width + 20 ) ;
						var increaseh = (psize.height) ;

						//reposicionamento da cell
						var newpositiony = pposition.y + 16 ;
						var newpositionx;
						if (parent.getEmbeddedCells().length === '0'){
							newpositionx = pposition.x + 15;
							console.log('nova posição da cell em x ' , newpositionx);
						}else {
							newpositionx = pposition.x + 15 + ((filhos) * (29));
							console.log('nova posição da cell em x sendo o segundo ' , newpositionx);
						};

						// Se já possuir um card cancela;
						console.log(parent.getEmbeddedCells().length);
						if (parent.getEmbeddedCells().length === 1){
							console.log("O slot já possui um card");
						}else{
							parent.embed(cell);
							embedOrConnect(parent, cell);

							parent.set('size' , { 
								width: increasew  ,
								height:	increaseh});
							cell.set('size' , {
								width: 12.5 ,
								height: 20							
							})
							cell.set('position' , {
								x : newpositionx ,
								y : newpositiony
							});
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
			if(!(cell.get('subType') === 'rack')){

//				*		//		var graph1 = new joint.dia.Graph;
//				*		//		var paper1 = new joint.dia.Paper({ width: 400, height: 200, model: graph1, gridSize: 1 });

				(new joint.ui.Dialog({
					type: 'alert' ,
					width: 420,
					draggable: false,
					title: 'Alert ',
					//*		//content: paper1.$el
					content: 'The order is wrong!'
				})).open();

				//mostrando como funciona a conexão em um paper *
//				(new joint.shapes.basic.Rect({
//				id: 'rack',
//				position: { x: 0, y: 50 },
//				size: { width: 80, height: 100 },
//				attrs: { text: { text: 'rack' } }
//				})).addTo(graph1);
//				(new joint.shapes.basic.Rect({
//				id: 'shelf',
//				position: { x: 140, y: 50 },
//				size: { width: 60, height: 40 },
//				attrs: { text: { text: 'shelf' } }
//				})).addTo(graph1);				
//				(new joint.shapes.basic.Rect({
//				id: 'slot',
//				position: { x:230, y: 50 },
//				size: { width: 15, height: 22 },
//				attrs: { text: { text: 'slot' } }
//				})).addTo(graph1);
//				(new joint.shapes.basic.Rect({
//				id: 'card',
//				position: { x: 350 , y: 50 },
//				size: { width: 5, height: 10 },
//				attrs: { text: { text: 'card/supervisor' } }
//				})).addTo(graph1);

				//por link
//				(new joint.dia.Link({
//				source: { id: 'rack' },
//				target: { id: 'shelf' },
//				attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' } }
//				})).addTo(graph1);
//				(new joint.dia.Link({
//				source: { id: 'shelf' },
//				target: { id: 'slot' },
//				attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' } }
//				})).addTo(graph1);
//				(new joint.dia.Link({
//				source: { id: 'slot' },
//				target: { id: 'card' },
//				attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' } }
//				})).addTo(graph1);

				//conectados
//				(graph1.getCell(rack)).embed(graph1.getCell(shelf));
//				(graph1.getCell(shelf)).embed(graph1.getCell(slot));
//				(graph1.getCell(slot)).embed(graph1.getCell(card));

				//por html
//				alert('Error!');
				cell.remove();
//				return;
			}
		}


	}, this);

	graph.on('remove' , function (cell) {

//		if((cell.get('parent') === null) && (!(cell.get('subType') === 'rack'))) {
//
//		}else {
//			var dialog = new joint.ui.Dialog({
//				type: 'warning' ,
//				width: 400,
//				draggable: false,
//				title: 'Warning ',
//				content: 'Are you sure you want to delete this equipment? Everything conected with it will be deleted.',
//				buttons: [
//				          { action: 'yes', content: 'Yes' },
//				          { action: 'no', content: 'No' }
//				          ]
//			});
//			dialog.on('action:yes', dialog.close, remove );
//			dialog.on('action:no', dialog.close, dialog);
//			dialog.open();
//
//			function remove (parent, cell) {

				var parentId = cell.get('parent');
				if (!parentId) return;

				var parent = graph.getCell(parentId);


				if(parent.get('subType') === 'rack') {

					//console.log(parent.getEmbeddedCells().length);

					parent.set('size' , { 
						width: parent.get('size').width  ,
						height:	240 + ((parent.getEmbeddedCells().length - (2) ) * 70)
					});
					//console.log(parent.get('size'));

				}else{
					if(parent.get('subType') === 'shelf'){

						var grandparentId = parent.get('parent');
						if (!grandparentId) return;

						var grandparent = graph.getCell(grandparentId);

						parent.set('size' , { 
							width: 120 + ((parent.getEmbeddedCells().length - (2) ) * 70) ,
							height:	parent.get('size').height});

						grandparent.set('size', {
							width: grandparent.get('size').width ,
							height: grandparent.get('size').height
						});				


					}else{
						if(parent.get('subType') === 'slot'){
							var grandparentId = parent.get('parent');
							if (!grandparentId) return;

							var grandparent = graph.getCell(grandparentId);

							parent.set('size' , {
								width: 22.5 ,
								height: 52.5
							});

//							grandparent.set('size', {
//							width: 120 + ((parent.getEmbeddedCells().length - (1) ) * 70),
//							height: grandparent.get('size').height
//							})



						}if(parent.get('subType') === 'card' || 'supervisor' ) {

						}
					}
				};
//			}
//		}	
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

		if (opt.skipParentHandler) return;

		if (cell.get('embeds') && cell.get('embeds').length) {
			// If we're manipulating a parent element, let's store
			// it's original position to a special property so that
			// we can shrink the parent element back while manipulating
			// its children.
			cell.set('originalPosition', cell.get('position'));
		}

		var parentId = cell.get('parent');
		if (!parentId) return;

		var parent = graph.getCell(parentId);
		var parentBbox = parent.getBBox();

		if (!parent.get('originalPosition')) parent.set('originalPosition', parent.get('position'));
		if (!parent.get('originalSize')) parent.set('originalSize', parent.get('size'));

		var originalPosition = parent.get('originalPosition');
		var originalSize = parent.get('originalSize');

		var newX = originalPosition.x;
		var newY = originalPosition.y;
		var newCornerX = originalPosition.x + originalSize.width;
		var newCornerY = originalPosition.y + originalSize.height;

		_.each(parent.getEmbeddedCells(), function(child) {

			var childBbox = child.getBBox();

			if (childBbox.x < newX) { newX = childBbox.x; }
			if (childBbox.y < newY) { newY = childBbox.y; }
			if (childBbox.corner().x > newCornerX) { newCornerX = childBbox.corner().x; }
			if (childBbox.corner().y > newCornerY) { newCornerY = childBbox.corner().y; }
		});

		// Note that we also pass a flag so that we know we shouldn't adjust the
		// `originalPosition` and `originalSize` in our handlers as a reaction
		// on the following `set()` call.
		parent.set({
			position: { x: newX, y: newY },
			size: { width: newCornerX - newX, height: newCornerY - newY }
		}, { skipParentHandler: true });
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
						parent.unembed(child);
						console.log("nao conectou!");	
						//(childsubType === 'in' || childsubType === 'out')
					}
				}
			}
		}
	}

};

