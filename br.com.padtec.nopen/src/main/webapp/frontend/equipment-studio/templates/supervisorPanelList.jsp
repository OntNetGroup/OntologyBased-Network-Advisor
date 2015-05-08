<div id="supervisorDialog" title="Select cards to supervise" style=" width: 450px; heigth: 300px">
		 
		<table style='width: 370px;'>
			<tr>
				<td style='width: 160px;'><b>Supervised Cards:</b><br /> <select
					multiple="multiple" id='lstBox1'>
					<!-- <option value="cardExemplo">Card Exemplo</option> -->
					
				</select></td>
				<td style='width: 50px; text-align: center; vertical-align: middle;'>
					<input type='button' id='btnRight' value='  >  ' /> <br /> <input
					type='button' id='btnLeft' value='  <  ' />
				</td>
				<td style='width: 160px;'><b>Cards without supervisor: </b><br /> <select
					multiple="multiple" id='lstBox2'>
				   <!-- <option value="CardExemplo2">CardExemplo2</option>-->
				</select></td>
			</tr>
		</table>
		
	</div>
	
	
	<script>
	
	
	//$("#supervisorDialog").addEventListener("load", console.log("hi"));
	
	
// 	(function (ns){
		
// 	})();
	
// 	$('#btnRight').on('click', function(e) {
// 		var selectedOpts = $('#lstBox1 option:selected');
// 		if (selectedOpts.length == 0) {
// 			alert("Nothing to move.");
// 			e.preventDefault();
// 		}

// 		$('#lstBox2').append($(selectedOpts).clone());
// 		$(selectedOpts).remove();
// 		e.preventDefault();
// 	});

// 	$('#btnLeft').on('click', function(e) {
// 		var selectedOpts = $('#lstBox2 option:selected');
// 		if (selectedOpts.length == 0) {
// 			alert("Nothing to move.");
// 			e.preventDefault();
// 		}

// 		$('#lstBox1').append($(selectedOpts).clone());
// 		$(selectedOpts).remove();
// 		e.preventDefault();
// 	})
</script>
	