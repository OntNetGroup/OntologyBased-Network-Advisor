//Global
var Supervisord = "";

function setSupervisor(graph, cards){
    
	var card;
     
	if (cards.length === 1) {
		
		card = graph.getCell(cards.val());

		var result=superviseCard( Supervisord.attributes.attrs.name.text, Supervisord.attributes.subType , Supervisord.id ,card.attributes.attrs.name.text, card.attributes.subType,card.id);
		if(result="success"){
			card.set("SupervisorID", Supervisord.id);
			card.set("Supervisor", Supervisord.attributes.attrs.name.text);
		}else{
			console.log(result);
		}
		
	}else{
		for(var i = 0; i < cards.length; i++){			
			card = graph.getCell(cards[i].value);
			
			var result=superviseCard( Supervisord.attributes.attrs.name.text, Supervisord.attributes.subType , Supervisord.id ,card.attributes.attrs.name.text, card.attributes.subType,card.id);
			if(result="success"){
				card.set("SupervisorID" , (Supervisord.id));
				card.set("Supervisor" , (Supervisord.attributes.attrs.name.text));
		}
	}
}
};

function setSupervisorNull(graph,cards){
		
	var card;
//	Card.set("Supervisor" , (Supervisord.id));
   
	if (cards.length === 1) {
		
		card = graph.getCell(cards.val());
		card.set("Supervisor" , '');
		card.set("SupervisorID" , '');
	}else{
		for(var i = 0; i < cards.length; i++){
			
			card = graph.getCell(cards[i].value);
			card.set("Supervisor" , '');	
			card.set("SupervisorID" , '');
		}
	}
}

function selectSupervisorWindow(Supervisor, nsCards, sCards, graph){
     
	Supervisord = Supervisor;

	for(var i = 0; i<sCards.length;i++){    		 
		$("#lstBox1").append('<option value="'+sCards[i].id+'" class = "supervised">'+sCards[i].attributes.attrs.name.text+'</option>');			
	}

	for(var i = 0; i<nsCards.length;i++){
			console.log(nsCards[i]);
			
		$("#lstBox2").append('<option value="'+nsCards[i].id+'" class = "unsupervised">'+nsCards[i].attributes.attrs.name.text+'</option>');
	};

	$("#supervisorDialog").dialog("open");	
}


function supervisorHandle(paper, graph){
   
   paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
   
	   cellId = cellView.model.id;	   
		
		var Supervisor = graph.getCell(cellId);		 
			
			var elementos = graph.getElements();
			//console.log('elementos' , elementos);
			var c = [];
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
			
			if(Supervisor.get('subType') === 'Supervisor'){
				selectSupervisorWindow(Supervisor, nsCards, sCards, graph);
			}
		
   })
   
}
