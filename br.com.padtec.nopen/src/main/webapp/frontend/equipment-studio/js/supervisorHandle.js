//Global
supervisord = "";

function setSupervisor(graph,cards){
    
	var card;
     
	if (cards.length === 1) {
		
		card = graph.getCell(cards.val());
//		var result=superviseCard( supervisord.attributes.attrs.name.text, supervisord.id ,card.attributes.attrs.name.text, card.id);
//		if(result="success"){
			card.set("supervisorID" , (supervisord.id));
			card.set("supervisor" , (supervisord.attributes.attrs.name.text));
//		}else{
//			//msg de erro
//			return;
//		}
		
		
	}else{
		for(var i = 0; i < cards.length; i++){			
			card = graph.getCell(cards[i].value);
			
//			var result=superviseCard( supervisord.attributes.attrs.name.text, supervisord.id ,card.attributes.attrs.name.text, card.id);
//			if(result="success"){
				card.set("supervisorID" , (supervisord.id));
				card.set("supervisor" , (supervisord.attributes.attrs.name.text));
//			}else{
//				//msg de erro
//				return;
//			}	
		}
	}
}

function setSupervisornull(graph,cards){
//  console.log(cards);   
//  console.log(cards.val());
//	console.log(graph);
//	console.log(supervisord);
//	console.log(supervisord.id);
//	console.log(cards.length);
//	for(var i = 0; i < cards.length; i++){
//		console.log(cards[i].value);	
//	}
		
	var card;
//	card.set("supervisor" , (supervisord.id));
   
	if (cards.length === 1) {
		
		card = graph.getCell(cards.val());
		card.set("supervisor" , '');
		card.set("supervisorID" , '');
	}else{
		for(var i = 0; i < cards.length; i++){
			
			card = graph.getCell(cards[i].value);
			card.set("supervisor" , '');	
			card.set("supervisorID" , '');
		}
	}
}

function selectSupervisorWindow(supervisor,nscards,scards, graph){
     
    supervisord = supervisor;
       
    	 for(var i = 0; i<scards.length;i++){    		 
 			$("#lstBox1").append('<option value="'+scards[i].id+'" class = "supervised">'+scards[i].attributes.attrs.name.text+'</option>');			
 		}
 			
 		for(var i = 0; i<nscards.length;i++){
 			//	console.log(nscards[i]);
 				$("#lstBox2").append('<option value="'+nscards[i].id+'" class = "unsupervised">'+nscards[i].attributes.attrs.name.text+'</option>');
 			};
 			
 			$("#supervisorDialog").dialog("open");	
}


function supervisorHandle(paper, graph){
   
   paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
   
	   cellId = cellView.model.id;	   
		
		var supervisor = graph.getCell(cellId);
		
		if((supervisor.get('tech')) === ''){
			
			showTechnologyWindow(getTechnologies() , supervisor);
			
		}else{
			
			var elementos = graph.getElements();
			//console.log('elementos' , elementos);
			var c = [ ];
            var nscards = [];
            var scards = [];
			
			for(var i = 0; i < elementos.length; i++){
				var check = elementos[i];
				if((check.attributes.subType) === 'card'){
                   if((check.get('supervisorID') === (supervisor.id))){
                	   scards.push(check);
                   }else{
                	   if((check.get('supervisorID') === '')){
                    	   nscards.push(check);
                       }
                   }
				}
			};
			
			if(!((supervisor.get('subType')) === 'supervisor')){
				return;
			}else{
				//selectSupervisorWindow(supervisor,getallnotsupervisedCards(),getsupervisedCards(), graph);
				selectSupervisorWindow(supervisor,nscards,scards, graph);
			}
		}
   });
};