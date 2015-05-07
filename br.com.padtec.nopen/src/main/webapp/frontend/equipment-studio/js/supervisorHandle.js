function selectSupervisorWindow(supervisor, scards , nscards){
	//ref
//	for(var i = 0;i < elementos.length; i++){
//	var element = elementos[i];
//	var check = elementos[i].attributes.subType;


//	if(check === "supervisor"){
//	var supervi = graph.getCell(element.id);
//	console.log(supervi);
//	console.log(supervi.get('tech'));
//	console.log(supervi.get('id'));

//	}
//	}

//	supervisor.get('parent');
//	var slot = graph.getCell(supervisor.get('parent'));
//	var shelf = graph.getCell(slot.get('parent'));
//	var rack = graph.getCell(shelf.get('parent'));

//	var p = rack.get('embeds');

//	console.log(p);
//	console.log(p.length);
//	for(var i = 0; i < p.length;i++){

//	var checkit = graph.getCell(p[i]);
//	console.log(checkit);
////	if(checkit.get('embeds') === ""){
////	console.log("prateleira vazia ="  , checkit);
////	}else{
////	console.log("prateleira cheia = " , checkit);
////	}
//	};

	//ref
//	var content = '<div id="card-supervisor" title="Set Card Supervisor">'
//	+ 'Supervisor: <select>';
//	for(var i = 0; i < elementos.length; i++){
//	var element = elementos[i];
//	var checktype = elementos[i].attributes.subType;
//	if(checktype === "supervisor"){
//	var supervi=graph.getCell(element.id);

//	content += '<option value="'+elementos[i]+'">'+elementos[i]+'</option>';
//	}
//	}
//	content += '</select>';
//	+ '</div>'



//	var supervisorlist = $.get("../template/supervisorPanelList.html");
//	console.log(supervisorlist);
//	var dialog = new joint.ui.Dialog({
//		width: 450,
//		type: 'neutral',
//		title: 'Set card Supervisor',
//		content: supervisorlist,
//		buttons:[
//		         {action:'ok', content: 'OK', position:'rigth'}		        
//		         ]
//	});
//
//	dialog.open();
	

	
	$("#supervisorDialog").dialog();
     
};



function supervisorHandle(paper, graph){
   
   paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
   
	   cellId = cellView.model.id;
		
		var supervisor = graph.getCell(cellId);
		
		if((supervisor.get('tech')) === ''){
			
			showTechnologyWindow(techs , supervisor);
			
		}else{
			
//			$("supervisorDialog").dialog();
//			selectSupervisorWindow(equipment,graph);
			selectSupervisorWindow(supervisor, cards);
			
		}
		
   });
};