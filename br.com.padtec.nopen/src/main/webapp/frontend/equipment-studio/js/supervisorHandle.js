//Global
Supervisord = "";

function setSupervisor(graph,Cards){
    
	var Card;
     
	if (Cards.length === 1) {
		
		Card = graph.getCell(Cards.val());
//		var result=superviseCard( Supervisord.attributes.attrs.name.text, Supervisord.id ,Card.attributes.attrs.name.text, Card.id);
//		if(result="success"){
			Card.set("SupervisorID" , (Supervisord.id));
			Card.set("Supervisor" , (Supervisord.attributes.attrs.name.text));
//		}else{
//			//msg de erro
//			return;
//		}
		
		
	}else{
		for(var i = 0; i < Cards.length; i++){			
			Card = graph.getCell(Cards[i].value);
			
//			var result=superviseCard( Supervisord.attributes.attrs.name.text, Supervisord.id ,Card.attributes.attrs.name.text, Card.id);
//			if(result="success"){
				Card.set("SupervisorID" , (Supervisord.id));
				Card.set("Supervisor" , (Supervisord.attributes.attrs.name.text));
//			}else{
//				//msg de erro
//				return;
//			}	
		}
	}
}

function setSupervisornull(graph,Cards){
		
	var Card;
//	Card.set("Supervisor" , (Supervisord.id));
   
	if (Cards.length === 1) {
		
		Card = graph.getCell(Cards.val());
		Card.set("Supervisor" , '');
		Card.set("SupervisorID" , '');
	}else{
		for(var i = 0; i < Cards.length; i++){
			
			Card = graph.getCell(Cards[i].value);
			Card.set("Supervisor" , '');	
			Card.set("SupervisorID" , '');
		}
	}
}

function selectSupervisorWindow(Supervisor,nsCards,sCards, graph){
     
    Supervisord = Supervisor;
       
    	 for(var i = 0; i<sCards.length;i++){    		 
 			$("#lstBox1").append('<option value="'+sCards[i].id+'" class = "supervised">'+sCards[i].attributes.attrs.name.text+'</option>');			
 		}
 			
 		for(var i = 0; i<nsCards.length;i++){
 			//	console.log(nsCards[i]);
 				$("#lstBox2").append('<option value="'+nsCards[i].id+'" class = "unsupervised">'+nsCards[i].attributes.attrs.name.text+'</option>');
 			};
 			
 			$("#SupervisorDialog").dialog("open");	
}


function supervisorHandle(paper, graph){
   
   paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
   
	   cellId = cellView.model.id;	   
		
		var Supervisor = graph.getCell(cellId);
		
		if((Supervisor.get('tech')) === ''){
			
			showTechnologyWindow(getTechnologies() , Supervisor);
			
		}else{
			
			var elementos = graph.getElements();
			//console.log('elementos' , elementos);
			var c = [ ];
            var nsCards = [];
            var sCards = [];
			
			for(var i = 0; i < elementos.length; i++){
				var check = elementos[i];
				if((check.attributes.subType) === 'Card'){
                   if((check.get('SupervisorID') === (Supervisor.id))){
                	   sCards.push(check);
                   }else{
                	   if((check.get('SupervisorID') === '')){
                    	   nsCards.push(check);
                       }
                   }
				}
			};
			
			if(!((Supervisor.get('subType')) === 'Supervisor')){
				return;
			}else{
				//selectSupervisorWindow(Supervisor,getallnotsupervisedCards(),getsupervisedCards(), graph);
				selectSupervisorWindow(Supervisor,nsCards,sCards, graph);
			}
		}
   });
};