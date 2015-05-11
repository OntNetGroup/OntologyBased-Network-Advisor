<div id="supervisorDialog" title="Select cards to supervise" style=" width: 1000px; heigth: 300px">
		 
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
				<td style='width: 160px;'><a>Cards without supervisor: </a><br /> <select
					multiple="multiple" id='lstBox2'>
<!-- 				   <option value="CardExemplo2" class="unsupervised">CardExemplo2</option> -->
	
				</select></td>
			</tr>
		</table>	
<!-- 		<button id='dosomething' value="click me"></button>		 -->
	</div>	
	<script>

// $("#dosomething").on("click", function(){
// 	$('.supervised').remove()
// });

		$("#supervisorDialog").dialog({
			  autoOpen: false,
			  modal: true,
			  title: 'Select the cards to supervise',
		      width: 450,
			  buttons: {
				  "Ok": function() {
					$('.supervised').remove();
					$('.unsupervised').remove();
		 			$(this).dialog('close');
				  }
			  }
			});	
	
	 	$('#btnRight').on('click', function(e) {
		var selectedOpts = $('#lstBox1 option:selected');
		if (selectedOpts.length == 0) {
			alert("Nothing to move.");
			e.preventDefault();
		}
      //result = IsCardempty();
      var result = "success";
      if (result === "success"){
      	setSupervisor(app.graph,selectedOpts,supervisord);
  		$('#lstBox2').append($(selectedOpts).clone());
  		$(selectedOpts).remove();
  		e.preventDefault();	
      }else{
      	//erro
      }
		
	});

	$('#btnLeft').on('click', function(e) {
		var selectedOpts = $('#lstBox2 option:selected');
		if (selectedOpts.length == 0) {
			alert("Nothing to move.");
			e.preventDefault();
		}
		 //result = IsCardempty();
      var result = "success";
      if (result === "success"){
      	setSupervisor(app.graph,selectedOpts);
      	$('#lstBox1').append($(selectedOpts).clone());
  		$(selectedOpts).remove();
  		e.preventDefault();
      }else{
      	//erro
      }
		
	});
	</script>
	