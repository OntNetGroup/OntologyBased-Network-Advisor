

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
			//parent position and size
			var pposition = parent.get('position');
			var psize = parent.get('size');
			//child position and size
			var cposition = cell.get('position');
			var csize = cell.get('size');

			if(parent.get('subType') === 'rack') {

				//Aumento de tamanho e re posicionamento
				var nempositiony;
				if(cell.get('subType') === 'shelf'){
					embedOrConnect(parent, cell);

					var a = parent.get('embeds');
					console.log(a);
					var i;
					var maior = 0;
					for (i = 0; i < a.length; i++) {
						var shelf = graph.getCell(a[i]);
						console.log(shelf);					
						var newpositionx = pposition.x + 15 ;

						shelf.set('position', {
							x: newpositionx ,
							y: (pposition.y + 20 + (i*(80))) 
						});
					}

					parent.set('size' , { 
						width: psize.width  ,
						height:	265 + ((parent.getEmbeddedCells().length - (2) ) * 77.5)
					});

				}else{
					new joint.ui.Dialog({
						type: 'alert',
						width: 400,
						title: 'Alert',
						content: 'O rack s&oacute; cont&eacute;m shelfs.'
					}).open();
					cell.remove();
				}

			}else{
				if(parent.get('subType') === 'shelf'){
					// selecionar o rack
					var grandparentId = parent.get('parent');
					if (!grandparentId) return;
					var grandparent = graph.getCell(grandparentId);

					var newpositiony; // = pposition.y + 7;
					var nempositionx;	
					if(cell.get('subType') === 'slot'){
						embedOrConnect(parent, cell);

						var b = parent.get('embeds');
						console.log(b);
						var i;
						var maior = 0;
						for (i = 0; i < b.length; i++) {
							var slot = graph.getCell(b[i]);
							console.log(slot);					

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
						console.log(c);
						var i;
						var maior = 0;
						for (i = 0; i < c.length; i++) {
							var shelf = graph.getCell(c[i]);
							console.log(shelf);
							if (maior < shelf.getEmbeddedCells().length) {
								maior = shelf.getEmbeddedCells().length;
								var maiorshelf = shelf;
								console.log(maiorshelf);
								console.log(maior);

							}
							grandparent.set ('size' , {
								width: maiorshelf.get('size').width + 40 ,
								height: grandparent.get('size').height
							});

						};	
					}else{
						new joint.ui.Dialog({
							type: 'alert',
							width: 400,
							title: 'Alert',
							content: 'Shelfs s&oacute; podem conter slots.'
						}).open();
						cell.remove();
					}

				}else {
					if(parent.get('subType') === 'slot'){
						var increasew = (psize.width) ;
						var increaseh = (psize.height) ;

						//reposicionamento da cell
						var newpositiony = pposition.y + 16 ;
						var newpositionx;
						if (parent.getEmbeddedCells().length === '0'){
							newpositionx = pposition.x + 6;
							console.log('nova posição da cell em x ' , newpositionx);
						}else {
							//Um card por slot
							newpositionx = pposition.x + 6 + ((filhos) * (29));
							console.log('nova posição da cell em x sendo o segundo ' , newpositionx);
						};
						// Se já possuir um card
						console.log(parent.getEmbeddedCells().length);
						if (parent.getEmbeddedCells().length === 1){
							if(!(cell.get('subType') === 'card')) {

								new joint.ui.Dialog({
									type: 'alert',
									width: 400,
									title: 'Alert',
									content: 'Apenas cards(sendo supervirsor ou n&atilde;o) podem ser inserido em um slot.'
								}).open();
								cell.remove();

							}else{
								if(!(cell.get('subType') === 'supervisor')) {

									new joint.ui.Dialog({
										type: 'alert',
										width: 400,
										title: 'Alert',
										content: 'Apenas cards(sendo supervirsor ou n&atilde;o) podem ser inserido em um slot.'
									}).open();
									cell.remove();

								}else{

									new joint.ui.Dialog({
										type: 'alert',
										width: 400,
										title: 'Alert',
										content: 'Apenas um card pode ser inserido por slot.'
									}).open();
									cell.remove();
								}
							}

						}else{
							embedOrConnect(parent, cell);

							if(cell.get('subType') === 'card') {
								parent.set('size' , { 
									width: increasew  ,
									height:	increaseh});

								cell.set('size' , {
									width: 10 ,
									height: 20							
								})
								cell.set('position' , {
									x : newpositionx ,
									y : newpositiony
								});
							}else{
								if(cell.get('subType') === 'supervisor'){
									parent.set('size' , { 
										width: increasew  ,
										height:	increaseh});

									cell.set('size' , {
										width: 10 ,
										height: 20							
									})
									cell.set('position' , {
										x : newpositionx ,
										y : newpositiony
									});
								}else{
									new joint.ui.Dialog({
										type: 'alert',
										width: 400,
										title: 'Alert',
										content: 'Apenas cards(sendo supervirsor ou n&atilde;o) podem ser inserido em um slot.'
									}).open();
									cell.remove();
								}

							}

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
//			if(!(cell.get('subType') === 'rack')){

//			(new joint.ui.Dialog({
//			type: 'alert' ,
//			width: 420,
//			draggable: false,
//			title: 'Alert ',
//			content: 'O equipamento selecionado precisa ser conectado em outro equipamento!'
//			})).open();
//			cell.remove();
//			}
		}
	}, this);

	graph.on('remove' , function (cell) {
// Mensagem de confirmação do remover
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

		var parentId = cell.get('parent');
		if (!parentId) return;
		var parent = graph.getCell(parentId);
		if(parent.get('subType') === 'rack') {

			var pposition = parent.get('position');
			var psize = parent.get('size');			
			var newpositionx = pposition.x + 15 ;

			var d = parent.get('embeds');
			var i;
			var maior = 0;
			console.log("to del: ", cell);
			for (i = 0; i < d.length-1; i++) {
				var shelf = graph.getCell(d[i]);
//				if (!(shelf === cell)){
				if (shelf){
					console.log("changed: " , shelf);
					var l = i - 1;
					console.log(l);
					if (l < 0){
						l ++;
						i++;
						console.log(l);
					}else{
						console.log(l);
					}
					shelf.set('position', {
						x: newpositionx ,
						y: (pposition.y + 20 + ((l)*(80))) 
					},{skipParentHandler: true });
				}
			}

			parent.set('size' , { 
				width: psize.width  ,
				height:	265 + ((parent.getEmbeddedCells().length - (2) ) * 77.5)
			},{skipParentHandler: true });

			var e = parent.get('embeds');
			console.log(e);
			var i;
			var maior2 = 0;
			for (i = 0; i < e.length; i++) {
				var shelf2 = graph.getCell(e[i]);
				console.log(shelf2);
				if (maior2 < shelf2.getEmbeddedCells().length) {
					maior2 = shelf2.getEmbeddedCells().length;
					var maiorshelf2 = shelf2;
					console.log(maiorshelf2);
					console.log(maior2);

				}
				parent.set ('size' , {
					width: maiorshelf2.get('size').width + 40 ,
					height: grandparent.get('size').height
				});

			};	

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

//					grandparent.set('size', {
//					width: 120 + ((parent.getEmbeddedCells().length - (1) ) * 70),
//					height: grandparent.get('size').height
//					})

				}if(parent.get('subType') === 'card' || 'supervisor' ) {

				}
			}
		};
//		}
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
// Impedir o equipamento filho de sair da area do equipamento pai
//		var parentId = cell.get('parent');
//		if (!parentId) return;

//		var parent = graph.getCell(parentId);
//		var parentBbox = parent.getBBox();
//		var cellBbox = cell.getBBox();

//		if (parentBbox.containsPoint(cellBbox.origin()) && parentBbox.containsPoint(cellBbox.topRight()) &&
//		parentBbox.containsPoint(cellBbox.corner()) &&
//		parentBbox.containsPoint(cellBbox.bottomLeft())) {

//		return;
//		}
//		cell.set('position', cell.previous('position'));

	},this);

	graph.on('change:position', function(cell, newPosition, opt) {
		if (opt.skipRemoveHandler) return;
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
						new joint.ui.Dialog({
							type: 'alert',
							width: 300,
							title: 'Alert',
							content: 'Ordem errada'
						}).open();
						console.log("nao conectou!");	
						//(childsubType === 'in' || childsubType === 'out')
					}
				}
			}
		}
	}

};
