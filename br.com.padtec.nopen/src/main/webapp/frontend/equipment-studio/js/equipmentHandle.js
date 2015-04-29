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
		buttons: [
		          { action: 'save', content: 'Save', position: 'left' }
		          ],	          
	});

	($('#btn-close')).hide();
	
	dialog.on('action:save', function(){
		//$('#selected').val($("#tech-dialog").val());
		//console.log($('#selected').val($("#tech-dialog").val()));
		//console.log($('#tech-dialog').find(":selected").val());
		cell.set('tech' ,($('#tech-dialog').find(":selected").val()) );
		
		
		result = setTechnology(supervisorType, supervisorID , tech);
		if(result === "sucess"){
			dialog.close();
		}else{
			dialog.close();
		}
		setTechnology(supervisorType, supervisorID , tech);
		//
        
		dialog.close();
	});
	dialog.open();
}; 

function selectSupervisorWindow(){
	var content;
	
	var dialog = new joint.ui.Dialog({
		width: 450,
		type: 'neutral',
		title: 'Set card Supervisor',
		content: content,
		buttons:[
		        {action:'save', content: 'Save', position:'left'} 
		         ]
	});
	
	
	
};

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
				//equipamento em um rack
				// consultar ontologia para inserção 
				var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);
				console.log(result);
				//var result = "success";
				//console.log('try to insert equipment ' +equipmentID+ ' name: ' +equipmentName+ ';type: ' +equipmentType+ ';container: ' +containerID+ ';conatainer: ' +containerType);
				if(result === "success") {
					parent.embed(cell);

					var a = parent.get('embeds');
					var maior = 0;
					for (var i = 0; i < a.length; i++) {
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
                    //equipamento em uma shelf
					var grandparentId = parent.get('parent');
					if (!grandparentId) return;
					var grandparent = graph.getCell(grandparentId);

					var containerType = parent.get('subType');
					var containerID = parent.get('id');

					var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);
					//var result = "success";
					//var result = "";
					console.log(result);
					if(result === "success") {
						parent.embed(cell)
						var b = parent.get('embeds');
						var maior = 0;
						for (var i = 0; i < b.length; i++) {
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
						var maior = (parent.get('size').width);
						var maiorshelf1 = parent;
						for (var i = 0; i < c.length; i++) {
							var shelf1 = graph.getCell(c[i]);
							if (shelf1){							
								if (maior < (shelf1.get('size').width)) {
									maior = (shelf1.get('size').width);
									maiorshelf1 = shelf1;
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
                       //equipamento em um slot
 						var containerType = parent.get('subType');
 						var containerID = parent.get('id');
 
 						var newpositionx;
 						if (parent.getEmbeddedCells().length === '0'){
 							newpositionx = pposition.x + 6;
 						}else {
 							//Um card por slot
 							newpositionx = pposition.x + 6 + ((filhos) * (29));
 						};
						var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);
 						//var result = "success";
 						//var result = "";
 						if(result === "success") {
 							// Se já possuir um card (confirmar o metodo para card e supervisor)
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
 								if (cell.get('subType') === 'supervisor'){									
 									parent.embed(cell);
 									showTechnologyWindow(getTechnologies() , cell);
 									
 									
// 									
// 									if(cell.get('tech') === ""){
// 										showTechnologyWindow(getTechnologies() , cell);
//									};
									cell.set('size' , {
	 									width: 10 ,
	 									height: 20							
	 								});
	 								cell.set('position' , {
	 									x : newpositionx ,
	 									y : ((pposition.y) + 16)
	 								});
 								};
 							    
 								if (cell.get('subType') === 'card'){									
 									parent.embed(cell);	
 								
 									cell.set('size' , {
 	 									width: 10 ,
 	 									height: 20							
 	 								});
 	 								cell.set('position' , {
 	 									x : newpositionx ,
 	 									y : ((pposition.y) + 16)
 	 								});
 	 								
 								};
 							
 								
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
		}else{
			//Only the rack can be inserted into the graph without an equipment holder	
//			 var containerType;
//			 var containerID;
//			 
//			var result = insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);
//			//var result = "success";
//			//var result = "";
//			if(result === "success") {
//			     
//				return;
//			}else{
//			   new joint.ui.Dialog({
//			type: 'alert' ,
//			width: 420,
//			draggable: false,
//			title: 'Alert ',
//			content: 'The equipment selected must be connected in another equipment!'
//			}).open();
//			cell.remove();
//			}
		}
	}, this);

	graph.on('remove' , function (cell) {

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

			var l = 0;

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

					if ( maiorl < sons ){
						maiorl = sons;
						shelfw = shelf.get('size').width;
					}

					var reslot = shelf.get('embeds');
					var k;
					for (k=0; k < (reslot.length); k++){
						var inshelf = graph.getCell(reslot[k]);	
						if(inshelf){			
							var childId = inshelf.get('embeds');	
							var inslot = graph.getCell(childId);																								
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
				var grandparentId = parent.get('parent');
				if (!grandparentId) return;
				var grandparent = graph.getCell(grandparentId);

				var pposition = parent.get('position');
				var e = parent.get('embeds');
				var l = 0;
				for(var j=0; j < e.length;j++){
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

				var t = grandparent.get('embeds');
				var k=0;
				var maiorl = 0;
				var inrackw;
				var sons=0;

				for(var p=0; p < t.length; p++){
					var inrack = graph.getCell(t[p]);

					sons = inrack.getEmbeddedCells().length;

					if ( maiorl < sons ){
						maiorl = sons;
						inrackw = inrack.get('size').width;
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

	graph.on('change:size', function(cell, newPosition, opt) {

//		if (opt.skipParentHandler) return;
//
//		if (cell.get('embeds') && cell.get('embeds').length) {
//			// If we're manipulating a parent element, let's store
//			// it's original size to a special property so that
//			// we can shrink the parent element back while manipulating
//			// its children.
//			cell.set('originalSize', cell.get('size'));
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

};
