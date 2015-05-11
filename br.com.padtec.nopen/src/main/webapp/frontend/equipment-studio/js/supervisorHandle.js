//Global
nscards = [];
scards =[];
supervisord = "";

function setSupervisor(graph,card){
//    console.log(card);   
//    console.log(card.val());
//	console.log(graph);
//	console.log(supervisord);
//	console.log('esse' , supervisord.id);
	
//	var card = graph.getCell(card.val());
//	card.set("supervisor" , (supervisord.id));

}


function selectSupervisorWindow(supervisor,nscards,scards, graph){

	console.log(nscards);
     console.log(scards);
     console.log(supervisor);
     
    supervisord = supervisor;
    
     $("#supervisorDialog").dialog("open");
     
     $("#supervisorDialog").ready();
     
     $("#supervisorDialog").on("dialogopen", function(){

    	 for(var i = 0; i<scards.length;i++){    		 
 			$("#lstBox1").append('<option value="'+scards[i].id+'" class = "supervised">'+scards[i].name+'</option>');			
 		}
 			
 		for(var i = 0; i<nscards.length;i++){
 				console.log(nscards[i]);
 				$("#lstBox2").append('<option value="'+nscards[i].id+'" class = "unsupervised">'+nscards[i].id+'</option>');
 			};
 		
 	} )

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
				selectSupervisorWindow(supervisor,nscards,scards, graph);
			}
		}
   });
};