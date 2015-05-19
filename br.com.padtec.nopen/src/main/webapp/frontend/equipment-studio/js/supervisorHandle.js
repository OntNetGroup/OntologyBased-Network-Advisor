//Global
nscards = [];
scards =[];
supervisord = "";

function setSupervisor(graph,cards){
//    console.log('os cards' , cards);   
//    console.log('val' , cards.val());
//	console.log('grafico' , graph);
//	console.log('supervisor' , supervisord);
//	console.log('esse' , supervisord.id);
//	console.log(cards.length);
//	for(var i = 0; i < cards.length; i++){
//		console.log(cards[i].value);	
//	}
		console.log(nscards);
		
	var card;
//	card.set("supervisor" , (supervisord.id));
     
	if (cards.length === 1) {
		
		card = graph.getCell(cards.val());
		card.set("supervisorID" , (supervisord.id));
		card.set("supervisor" , (supervisord.attributes.attrs.name.text));
	}else{
		for(var i = 0; i < cards.length; i++){
			
			card = graph.getCell(cards[i].value);
			card.set("supervisorID" , (supervisord.id));
			card.set("supervisor" , (supervisord.attributes.attrs.name.text));	
		}
	}
}

function setSupervisornull(graph,cards){
//  console.log('os cards' , cards);   
//  console.log('val' , cards.val());
//	console.log('grafico' , graph);
//	console.log('supervisor' , supervisord);
//	console.log('esse' , supervisord.id);
//	console.log(cards.length);
//	for(var i = 0; i < cards.length; i++){
//		console.log(cards[i].value);	
//	}
		
	var card;
//	card.set("supervisor" , (supervisord.id));
   
	if (cards.length === 1) {
		
		card = graph.getCell(cards.val());
		card.set("supervisor" , '');
	}else{
		for(var i = 0; i < cards.length; i++){
			
			card = graph.getCell(cards[i].value);
			card.set("supervisor" , '');	
		}
	}
}

function selectSupervisorWindow(supervisor,nscards,scards, graph){

	console.log(nscards);
     console.log(scards);
     console.log(supervisor);
     
    supervisord = supervisor;
    
//     $("#supervisorDialog").dialog("open");

    	 for(var i = 0; i<scards.length;i++){    		 
 			$("#lstBox1").append('<option value="'+scards[i].id+'" class = "supervised">'+scards[i].name+'</option>');			
 		}
 			
 		for(var i = 0; i<nscards.length;i++){
 				console.log(nscards[i]);
 				$("#lstBox2").append('<option value="'+nscards[i].id+'" class = "unsupervised">'+nscards[i].attributes.attrs.name.text+'</option>');
 			};
 			
 			$("#supervisorDialog").dialog("open");	
}


function supervisorHandle(paper, graph){
   
   paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
   
	   cellId = cellView.model.id;	   
		
		var supervisor = graph.getCell(cellId);
		
		if((supervisor.get('tech')) === ''){
			
			showTechnologyWindow(techs , supervisor);
			
		}else{
			
			var elementos = graph.getElements();
			console.log(elementos);
			var c = [ ];

			
			for(var i = 0; i < elementos.length; i++){
				if((elementos[i].attributes.subType) === 'card'){
					c.push(graph.getCell(elementos[i].id));
				}
			};
			
			for(var i= 0;i < c.length; i++){
					if((c[i].attributes.supervisor) ===  (supervisor.get('id'))){
						c.push(graph.getCell(c[i].id))
					}			
			}
			if(!((supervisor.get('subType')) === 'supervisor')){
				return;
			}else{
				//selectSupervisorWindow(supervisor,getallnotsupervisedCards(),getsupervisedCards(), graph);
				selectSupervisorWindow(supervisor,nscards,scards, graph);
			}
		}
   });
};