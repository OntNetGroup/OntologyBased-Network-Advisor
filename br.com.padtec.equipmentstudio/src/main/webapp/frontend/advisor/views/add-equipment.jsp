<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	String sindelValue = (String)request.getSession().getAttribute("txtSindelCode");
	
%>
<%@include file="/frontend/advisor/templates/header.jsp"%>

<script>
	
	$(document).ready(function () {
		verifyIfScratchHidden();
		hideAll();
		hideScratch();
		hideEquipmentType();
		
		action = "${action}";
		if(action == "runParser"){
			runParser();
		}
		
		$('input[name=equipNameAux]').keyup(function() {
			document.getElementById("equipName").value = this.value;
		});
		
		$('#hideScratch').click(function(){
			hideScratch();
	    });
		
		$('#hideEquipmentType').click(function(){
			hideEquipmentType();
	    });
		
		$('#addFields').click(function(){
			addFields();
	    });
		
		$('.btn-load').click(function(event) {

			window.location.href = "/welcome";
				
		});	//End load sindel file

		$('.btn-clean').click(function(event) {

			$.ajax({
				url : "cleanEquipSindel",
				type : "GET",
				success : function(dto) {

					if(dto.ok == true)
						window.location.href = "new-equipment";

				},
				error:function(x,e){
					
					alert("Problem to clean sindel code");
				},					
			});
				
		});	//End load sindel file
		
		$('#sindelForm').submit(function(event) {
			loading();
			event.preventDefault();
			var sReturn = "";
			
			
			try {				
				//clean up hashs
				cleanUpHashs();			
				
				//Used to get the value of the CodeMirror editor
				parser.parse(editor.getValue());
				
				sReturn  = printHashTypeVar(hashTypeVar);								
				sReturn += printHashRelations(hashRelation);
				sReturn += printHashAttribute(hashAttribute);

				//Verify warning
				if(warning != ""){
					
					//Warning: mensagem
					//alert(warning);
					var html = "<div class=\"alert alert-danger\">" +
						"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
						"<strong>" + "Warning! " + "</strong>"+ warning + 
					"</div>";

					$("#boxerro").prepend(html);
				}
				
				var json = {
						"result" : sReturn,
						"ok" : true,
						"equipmentName": $("#equipName").val()
						
					};
				
				$.ajax({
					url : $("#sindelForm").attr("action"),
					data : JSON.stringify(json),
					type : "POST",
					contentType: "application/json; charset=utf-8",
					dataType: "json",

					success : function(dto) {

						if(dto.ok == false)
						{
							var html = "<div class=\"alert alert-danger\">" +
							"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
							"<strong>" + "</strong>"+ dto.result + 
							"</div>";
			
							$("#boxerro").prepend(html);
							
						} else {

							//load list instances
							alert("Ok! Going to OKCo..");
							window.location.href = "list";

						}

					},
					error:function(x,e){
						
						alert("Erro");
					},					
				});
				
				
			}
			catch (e) {

				var html = "<div class=\"alert alert-danger\">" +
					"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
					"<strong>" + "</strong>"+ e.message + 
				"</div>";
	
				$("#boxerro").prepend(html);
			
			}		
		});
		
// 		$('#equipTypeForm').submit(function(event) {
// 			event.preventDefault();
// 			var sReturn = "";
			
// 			for(k=1; k<=iAdd; k++){
				
// 				try {				
// 					//clean up hashs
// 					cleanUpHashs();		
// 					teste = window;
// 					file = document.getElementById("file"+k).files[0];
// 					reader = new FileReader();

// 					reader.readAsText(file);
// 					x = file.getAsBinary();
// 					var txtArea = "";
// 					while(!file.AtEndOfStream){
// 						txtArea += file.ReadAll();
// 					}
// 					alert(txtArea);
// 					document.getElementById("code").value(txtArea);
					
// 					//Used to get the value of the CodeMirror editor
// 					parser.parse(editor.getValue());
// 					sReturn = "";
// 					sReturn  = printHashTypeVar(hashTypeVar);								
// 					sReturn += printHashRelations(hashRelation);
// 					sReturn += printHashAttribute(hashAttribute);
			
// 					//Verify warning
// 					if(warning != ""){
						
// 						//Warning: mensagem
// 						//alert(warning);
// 						var html = "<div class=\"alert alert-danger\">" +
// 							"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
// 							"<strong>" + "Warning! " + "</strong>"+ warning + 
// 						"</div>";
	
// 						$("#boxerro").prepend(html);
// 					}
					
// 					var json = {
// 							"result" : sReturn,
// 							"ok" : true,
// 							"equipmentName": $("#equipName").val()
							
// 						};
	
// 					$.ajax({
// 						url : $("#equipTypeForm").attr("action"),
// 						enctype : "multipart/form-data" ,
// 						data : JSON.stringify(json),
// 						type : "POST",
// 						contentType: "application/json; charset=utf-8; multipart/form-data",
// 						dataType: "json",
	
// 						success : function(dto) {
	
// 							if(dto.ok == false)
// 							{
// 								var html = "<div class=\"alert alert-danger\">" +
// 								"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
// 								"<strong>" + "</strong>"+ dto.result + 
// 								"</div>";
				
// 								$("#boxerro").prepend(html);
								
// 							} else {
	
// 								//load list instances
// 								alert("Ok! Going to OKCo..");
// 								window.location.href = "list";
	
// 							}
	
// 						},
// 						error:function(x,e){
							
// 							alert("Erro");
// 						},					
// 					});
					
					
// 				}
// 				catch (e) {
	
// 					var html = "<div class=\"alert alert-danger\">" +
// 						"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
// 						"<strong>" + "</strong>"+ e.message + 
// 					"</div>";
		
// 					$("#boxerro").prepend(html);
				
// 				}		
// 			}
// 		});
		
        
      }); // end document read
	
      function cleanUpHashs(){
  		currentLine = 0;
  	 
  		warning = "";
  		 
  		hashVarType = new Array();
  		hashTypeVar = new Array();
  		hashUsedRelation = new Array();

  		hashUsedVariable = new Array();

  		hashComposition = new Array();

  		hashRelation = new Array();
  		hashRelation["binds"] = new Array();
  		hashRelation["connects"] = new Array();
  		hashRelation["maps"] = new Array();
  		hashRelation["client"] = new Array();
  		hashRelation["component_of"] = new Array();

  		hashAttribute = new Array();
  		hashAttribute['str_location'] = new Array();
  		hashAttribute['geo_location'] = new Array();
  		hashAttribute['tf_type'] = new Array();
  	}
  	  
  	function printHashTypeVar(hash){
  		var s = "elements"+"#";
  		
  		for (var key in hash) {					
  			s += key+":"+hash[key];
  			s = s.substring(0,s.length)+";";
  		}
  		s+="!";
  		return s;
  	}
  	
  	function printHashRelations(hash){
  		var s = "";
  				
  		for (var key in hash) {
  			if(key == "component_of"){
  				s += key+"#";
  				for(var i = 0; i < hash[key].length; i++){
  					s += hash[key][i]+";";
  				}
  				s = s.substring(0,s.length-1);
  				s += "!";
  			}else{
  				s += key+"#";
  				s += hash[key]+"!";						
  			}
  		}		
  		return s;
  	}
  	
  	function printHashAttribute(hash){
  		var s = "";
  				
  		for (var key in hash) {			
  			s += key+"#";
  			for(var i = 0; i < hash[key].length; i++){
  				s += hash[key][i]+";";
  			}
  			s = s.substring(0,s.length-1);
  			s += "!";			
  		}		
  		return s;
  	};
  	
  	var scratchHidden = true;
	var equipmentTypeHidden = true;
	
	function hideScratch(){
		document.getElementById("row").style.height="auto";
		document.getElementById("content").style.height="auto";
		
		if(scratchHidden){
			document.getElementById("scratchContainer").style.visibility = 'visible';
			document.getElementById("scratchContainer").style.height="auto";
			
			document.getElementById("sindelForm").style.visibility = 'visible';
			document.getElementById("sindelForm").style.height="auto";
		}else{
			document.getElementById("scratchContainer").style.visibility = 'hidden';
			document.getElementById("scratchContainer").style.height="0px";
			
			document.getElementById("sindelForm").style.visibility = 'hidden';
			document.getElementById("sindelForm").style.height="0px";
		}
		
		scratchHidden = !scratchHidden;
	};
	
	function hideEquipmentType(){
		document.getElementById("row").style.height="auto";
		document.getElementById("content").style.height="auto";
		
		if(equipmentTypeHidden){
			document.getElementById("equipTypeContainer").style.visibility = 'visible';
			document.getElementById("equipTypeContainer").style.height="auto";
		}else{
			document.getElementById("equipTypeContainer").style.visibility = 'hidden';
			document.getElementById("equipTypeContainer").style.height="0px";
		}
		
		equipmentTypeHidden = !equipmentTypeHidden;
	};
	
	function verifyIfScratchHidden(){
		if('${txtSindelCodeBr}' != ""){
			scratchHidden = true;
		}
	};
	
	var iAdd = 1;
	var maxElements = 10;
	
	function runParser(){
		loading();
		var equips = new Array;
		try{
  			for(i=1; i<=maxElements; i++){
  			
				var sReturn = "";
				//clean up hashs
				cleanUpHashs();			
				
				txtSindel = document.getElementById("txtSindel"+i).value;
				equipName = document.getElementById("equipName"+i).value;
				if(txtSindel != "" && equipName != ""){
					//Used to get the value of the CodeMirror editor
					parser.parse(txtSindel);
					
					sReturn  = printHashTypeVar(hashTypeVar);								
					sReturn += printHashRelations(hashRelation);
					sReturn += printHashAttribute(hashAttribute);
					
					document.getElementById("txtSindel"+i).value = sReturn;
					
					equips[i-1] = new Array;
					equips[i-1][0] = equipName;
					equips[i-1][1] = sReturn;
				}			
  			}
		}catch (e) {

			var html = "<div class=\"alert alert-danger\">" +
				"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
				"<strong>" + "</strong>"+ "On Equipment " + i + ".<br>" + e.message + 
			"</div>";

			$("#boxerro").prepend(html);
		
		}
		
		var json = {
				"ok" : true,
				"equipments": equips
				
			};
		
		$.ajax({
			url : "runEquipTypes",
			data : JSON.stringify(json),
			type : "POST",
			contentType: "application/json; charset=utf-8",
			dataType: "json",

			success : function(dto) {

				if(dto.ok == false)
				{
					var html = "<div class=\"alert alert-danger\">" +
					"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
					"<strong>" + "</strong>"+ dto.result + 
					"</div>";
	
					$("#boxerro").prepend(html);
					
				} else {

					//load list instances
					alert("Ok! Going to OKCo..");
					window.location.href = "list";

				}

			},
			error:function(x,e){
				
				alert("Erro");
			},					
		});
  	};
  	
	function hideAll(){
		for(j=2; j<=maxElements; j++){
			document.getElementById("div"+j).style.visibility = 'hidden';
			document.getElementById("div"+j).style.height="0px";
			//document.getElementById('div'+j).style.visibility = 'hidden';
			//document.getElementById('equipName'+j).style.visibility = 'hidden';
			//document.getElementById('uniform-file'+j).style.visibility = 'hidden';
			//document.getElementById('file'+j).style.visibility = 'hidden';
		}
	}
		
	function addFields(){
		iAdd++;
		
		if(iAdd == maxElements){
			document.getElementById("addFields").style.visibility = 'hidden';
		}
		if(iAdd > maxElements){
			alert("MAXIMO DE 10!!!");
			return;
		}
		document.getElementById("div"+iAdd).style.visibility = 'visible';
		document.getElementById("div"+iAdd).style.height="auto";
		
		
		
		
	};
			
	
	
</script>

<div id="boxerro"></div>

<h1>Equipment from Sindel Editor</h1>

<div class="row" id="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>New Equipment from Equipment Type
				</h2>
				<div class="box-icon"> 
					<a href="#" class="btn-minimize"><i class="icon-chevron-up" id="hideEquipmentType"></i></a>
				</div>
			</div>
			
			<div class="tooltip-demo well" id="equipTypeContainer">
				<form action="uploadEquipTypes.htm" id="equipTypeForm" class="form-horizontal" method="POST" enctype="multipart/form-data" >
					<%
					int maxElements = 10;
					for(int i = 1; i <= maxElements; i++){
						String txtSindel = (String)request.getSession().getAttribute("txtSindel"+i);
						String outTxtSindel = "";
						if(txtSindel!=null){
							outTxtSindel = txtSindel;
						}
						String equipName = (String)request.getSession().getAttribute("equipName"+i);
						String outEquipName = "";
						if(equipName!=null){
							outEquipName = equipName;
						}
						
						String fileName = (String)request.getSession().getAttribute("filename"+i);
						String outFileName = "";
						if(fileName!=null){
							outFileName = fileName;
						}
						
						
					%>
						<div id="div<%out.print(i);%>">
							<input id="equipName<%out.print(i);%>" name="equipName<%out.print(i);%>" value="<%out.print(outEquipName);%>"/>
							<input name="file<%out.print(i);%>" type="file" id="file<%out.print(i);%>" value="<%out.print(outFileName);%>">
							<input id="txtSindel<%out.print(i);%>" name="txtSindel<%out.print(i);%>" type="hidden" value="<%out.print(outTxtSindel);%>"/>
					<%
							if(i == 1){
					%>
							<input type="submit" name="submit" value="Upload/Run" />
							<input id="addFields" type="button" value="+" />
					<%		
							}
					%>
						</div>
					<%
					}
					%>
					
					
				</form>
			</div>  
			
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>New Equipment from Scratch
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-minimize"><i class="icon-chevron-up" id="hideScratch"></i></a>
				</div>
			</div>
			
			<div class="tooltip-demo well" id="scratchContainer">
					
				<form action="uploadSindelEquip.htm" class="form-horizontal" enctype="multipart/form-data" method="POST">
		 			<div class="controls">
		 				<%
						String equipNameAux = (String)request.getSession().getAttribute("equipNameAux");
						String outEquipNameAux = "";
						if(equipNameAux!=null){
							outEquipNameAux = equipNameAux;
						}
						%>	
						<input id="equipNameAux" name="equipNameAux" value="<%out.print(outEquipNameAux);%>"/>
		 				<input name="file" type="file">
						<input type="submit" name="submit" value="Upload" /><br>
					</div>
				</form>
						 					  
			</div>  
			<!-- ------------------- -->
			<!-- Content Sindel here -->
			<!-- ------------------- -->
			<form id="sindelForm" method="POST" action="runEquipScratch.htm">

				<div style="border: 1px solid black; width: 100%;">
					<textarea id="code" name="code" style="width: 100%;"><%if(sindelValue != null){out.println(sindelValue);}%>
					</textarea>
					<%
					String equipName = (String)request.getSession().getAttribute("equipName");
					String outEquipName = "";
					if(equipName!=null){
						outEquipName = equipName;
					}
					%>	
					<input id="equipName" name="equipName" type="hidden" value="<%out.print(outEquipName);%>"/>
					<input type="button" class="btn btn-pre btn-clean" value="Clean" />
					<input type="submit" class="btn btn-pre" value="Run" />

					<script>
								CodeMirror.commands.autocomplete = function(cm) {
							        CodeMirror.showHint(cm, CodeMirror.hint.sindel_hint);
							      };
							      var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
							        lineNumbers: true,
							        matchBrackets: true,
							        mode: "text/sindel",
									extraKeys: {"Ctrl-Space": "autocomplete"}
							      });
							      editor.setOption("theme", "neat");
							    </script>
					<div id="res"></div>
					<div id="res2" style="border: 2px solid black;"></div>

					<!-- ------------------------ -->
					<!-- END -Content Sindel here -->
					<!-- ------------------------ -->

				</div>

			</form>

			<div class="row">
				<div class="col-lg-12">
					<br>
					<p>Instructions:</p>
					<div class="tooltip-demo well">
						<p class="muted" style="margin-bottom: 0;">
							In this page, you can add one or more equipment to the already loaded network. There are two different ways to add the equipment: (1) adding them from already defined equipment types or (2) editing an already defined equipment type.
 							<br><br>
							<b>New Equipment from Equipment Type:</b>
							<br>
							First, write the equipment name in the empty box and then select the equipment type from an already saved file containing an equipment type description in Sindel. You can add at most 10 equipment clicking at the + button.
							<br>
							Once defined the equipment names and types, press Upload/Run.
							<br><br> 
							<b>New Equipment from Scratch:</b>
							<br>
							First, write the equipment name in the empty box and then select the equipment type from an already saved file containing an equipment type description in Sindel. The equipment definition will be loaded to the editor, where you can make modifications.
							<br>
							Once the equipment is defined, press Run.
						</p>
					</div>
				</div>
			</div>

		</div>

	</div>

</div>
<!--/col-->

</div>
<!--/row-->

<%@include file="/frontend/advisor/templates/footer.jsp"%>