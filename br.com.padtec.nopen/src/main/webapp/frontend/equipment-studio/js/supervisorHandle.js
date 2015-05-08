function selectSupervisorWindow(supervisor,nscards,scards, graph){

	console.log(nscards);
     console.log(scards);
     console.log(supervisor);
     
    
    
     $("#supervisorDialog").dialog("open");
     
     $("#supervisorDialog").on("dialogopen", function(){

    	 for(var i = 0; i<scards.length;i++){    		 
 			$("#lstBox1").append('<option value="'+scards[i].id+'">'+scards[i].name+'</option>');			
 		}
 			
 		for(var i = 0; i<nscards.length;i++){
 				console.log(nscards[i]);
 				$("#lstBox2").append('<option value="'+nscards[i].id+'">'+nscards[i].id+'</option>');
 			};
 		
 	} )

	//	$("#supervisorDialog").dialog(
//			modal:true,
//			 height: $(window).height(),
//				width: $(window).width(),
//              title: 'Select the cards to supervise',
//              open: function(){},
//              close: function(){},
//      )};
}


function supervisorHandle(paper, graph){
   
   paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
   
	   cellId = cellView.model.id;
		graph;
	   
		
		var supervisor = graph.getCell(cellId);
		
		
		
		if((supervisor.get('tech')) === ''){
			
			showTechnologyWindow(techs , supervisor);
			
		}else{
			
			var elementos = graph.getElements();
			console.log(elementos);
			var c = [ ];
			var nscards = [];
			var scards =[];
			
			for(var i = 0; i < elementos.length; i++){
				if((elementos[i].attributes.subType) === 'card'){
					c.push(graph.getCell(elementos[i].id));
				}
			};
			
			for(var i= 0;i < c.length; i++){
				if((c[i].attributes.supervisor) === ''){
					nscards.push(graph.getCell(c[i].id))
				}else{
					if((c[i].attributes.supervisor) ===  (supervisor.get('id'))){
						c.push(graph.getCell(c[i].id))
					}
					
				}
			}
			
			if(!((supervisor.get('subType')) === 'supervisor')){
				return;
			}else{
				selectSupervisorWindow(supervisor,nscards,scards, graph);
			}
//			document.getElementById("#supervisorDialog").style.display = "block";
//			$("supervisorDialog").dialog();
//			selectSupervisorWindow(equipment,graph);
		//	selectSupervisorWindow(supervisor,nscards,scards, graph);
			
		}
		
   });
};