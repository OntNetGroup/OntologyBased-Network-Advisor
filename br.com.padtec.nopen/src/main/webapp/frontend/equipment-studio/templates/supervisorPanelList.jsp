<div id="supervisorDialog" title="Select cards to supervise"
	style="width: 1000px; heigth: 300px">
	<table style='width: 370px;'>
		<tr>
			<td style='width: 160px;'><a>Supervised Cards:</a><br /> <select
				multiple="multiple" id='lstBox1'>
					<!-- 					<option value="CardExemplo2" class="supervised">CardExemplo2</option> -->


			</select></td>
			<td style='width: 50px; text-align: center; vertical-align: middle;'>
				<input type='button' id='btnRight' value='  >  ' /> <br /> <input
				type='button' id='btnLeft' value='  <  ' />
			</td>
			<td style='width: 160px;'><a>Cards without supervisor: </a><br />
				<select multiple="multiple" id='lstBox2'>
					<!-- 				   <option value="CardExemplo2" class="unsupervised">CardExemplo2</option> -->

			</select></td>
		</tr>
	</table>
</div>
<script>

		$("#supervisorDialog").dialog({
			  autoOpen: false,
			  modal: true,
			  closeButton: false ,
			  titlebar: 'Select the cards to supervise',
		      width: 450,
			  buttons: {
				  "Ok": function() {
					$('.supervised').remove();
					$('.unsupervised').remove();
		 			$(this).dialog('close');
				  }
			  }
			});	
	
		$("#supervisorDialog").on('dialogclose', function(){
			$('.supervised').remove();
			$('.unsupervised').remove();
		});
		
	 	$('#btnRight').on('click', function(e) {
		var selectedOpts = $('#lstBox1 option:selected');
		if (selectedOpts.length == 0) {
			new joint.ui.Dialog({
				type: 'alert',
				width: 400,
				title: 'Error',
				content: "Select something to move.",
			}).open();	
			e.preventDefault();
		}
		if (selectedOpts.length == 1){
			var card = app.graph.getCell(selectedOpts.val());
			var cardID = card.id;
			
		      //result = canUnsupervise(cardID);
		      var result = "true";
		      if (result === "true"){
		    	setSupervisornull(app.graph,selectedOpts);
		  		$('#lstBox2').append($(selectedOpts).clone());	
		  		$(selectedOpts).remove();
		  		e.preventDefault();	
		      }else{
		    	  new joint.ui.Dialog({
						type: 'alert',
						width: 400,
						title: 'Error',
						content: result,
					}).open();	
		      }
		}else{
			for(var i = 0; i < selectedOpts.length;i++ ){
				var card = app.graph.getCell(selectedOpts[i].value);
				var cardID = card.id;
				
			    //result = canUnsupervise(cardID);
			    var result = "true";
			      if (result === "true"){
			    	setSupervisornull(app.graph,selectedOpts[i]);
			  		$('#lstBox2').append($(selectedOpts[i]).clone());	
			  		$(selectedOpts).remove();
			  		e.preventDefault();	
			      }else{
			    	  new joint.ui.Dialog({
							type: 'alert',
							width: 400,
							title: 'Error',
							content: result,
						}).open();	
			      }
			}
		}
	});

	$('#btnLeft').on('click', function(e) {
		var selectedOpts = $('#lstBox2 option:selected');
		if (selectedOpts.length == 0) {
			new joint.ui.Dialog({
				type: 'alert',
				width: 400,
				title: 'Error',
				content: "Select something to move.",
			}).open();	
			e.preventDefault();
		}
		if (selectedOpts.length == 1){
			var card = app.graph.getCell(selectedOpts.val());
			var cardID = card.id;
			var supervisorID = supervisord.id;
			//result = canSupervise(cardID , supervisorID);
		      var result = "true";
		      if (result === "true"){
		    	  setSupervisor(app.graph,selectedOpts);
		      	$('#lstBox1').append($(selectedOpts).clone());
		  		$(selectedOpts).remove();
		  		e.preventDefault();
		      }else{
		    	  new joint.ui.Dialog({
						type: 'alert',
						width: 400,
						title: 'Error',
						content: result,
					}).open();	
		      }
			
		}else{
			for(var i = 0; i < selectedOpts.length;i++ ){
				var card = app.graph.getCell(selectedOpts[i].value);
				var cardID = card.id;
				var supervisorID = supervisord.id;
				
				//result = canSupervise(cardID , supervisorID);
			      var result = "true";
			      if (result === "true"){
			    	  setSupervisor(app.graph,selectedOpts[i]);
			      	$('#lstBox1').append($(selectedOpts[i]).clone());
			  		$(selectedOpts).remove();
			  		e.preventDefault();
			      }else{
			    	  new joint.ui.Dialog({
							type: 'alert',
							width: 400,
							title: 'Error',
							content: result,
						}).open();	
			      }		
			}
		}	
	});
	</script>
