<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	String sindelValue = (String)request.getSession().getAttribute("txtSindelCode");
	
%>
<%@include file="../templates/header.jsp"%>

<script>
	
	$(document).ready(function () {
		
		verifyIfScratchHidden();
		hideAll();
		hideScratch();
		hideEquipmentType();
		
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

			window.location.href = "/tnokco/welcome";
				
		});	//End load sindel file

		$('.btn-clean').click(function(event) {

			$.ajax({
				url : "cleanEquipSindel",
				type : "GET",
				success : function(dto) {

					if(dto.ok == true)
						window.location.href = "/tnokco/newEquipment";

				},
				error:function(x,e){
					
					alert("Problem to clean sindel code");
				},					
			});
				
		});	//End load sindel file
		
// 		$('#equipTypeForm').submit(function(event) {
			
// 			event.preventDefault();
			
// 			var json = {
// 					"result" : "",
// 					"ok" : true,
// 					"equipmentName": $("#equipName").val()
					
// 				};
			
// 			$.ajax({
// 				url : $("#equipTypeForm").attr("action"),
// 				data : JSON.stringify(json),
// 				type : "POST",
// 				contentType: "application/json; charset=utf-8",
// 				dataType: "json",

// 				success : function(dto) {

// 					if(dto.ok == false)
// 					{
// 						var html = "<div class=\"alert alert-danger\">" +
// 						"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>" + 
// 						"<strong>" + "</strong>"+ dto.result + 
// 						"</div>";
		
// 						$("#boxerro").prepend(html);
						
// 					} else {

// 						//load list instances
// 						alert("Ok! Going to OKCo..");
// 						window.location.href = "list";

// 					}

// 				},
// 				error:function(x,e){
					
// 					alert("Erro");
// 				},					
// 			});
			
			
			
// 		});
		
		$('#sindelForm').submit(function(event) {
			
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
		
		$('#uploadAndRunEquipType').submit(function(event) {
			
			event.preventDefault();
			var sReturn = "";
			
			for(k=1; k<=maxElements; k++){
				try {				
					//clean up hashs
					cleanUpHashs();			
					var arquivo = dados.OpenTextFile(document.getElementById("file"+k), 1, true);
					var txtArea = "";
					while(!arquivo.AtEndOfStream){
						txtArea += arquivo.ReadAll();
					}
					alert(txtArea);
					document.getElementById("code").value(txtArea);
					
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
						url : $("#uploadAndRunEquipType").attr("action"),
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
			}
		});
		
        
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
  	}
	var scratchHidden = false;
	var equipmentTypeHidden = false;
	
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
	
	var iAdd = 2;
	var maxElements = 10;
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
		//iAdd++;
// 		var container = document.getElementById("equipTypeForm");
		
// 		// Create an <input> element, set its type and name attributes
// 		var equipName = document.createElement("input");
// 		equipName.name = "equipName"+iAdd;
// 		equipName.id = "equipName"+iAdd;
// 		container.appendChild(equipName);
		
// 		var fileDiv = document.createElement("div");
// 		fileDiv.className = "uploader";
// 		fileDiv.id = "uniform-file"+iAdd;
		
// 		var fileText = document.createElement("input");
// 		fileText.type = "file";
// 		fileText.name = "file"+iAdd;
// 		fileText.id = "file"+iAdd;
// 		fileDiv.appendChild(fileText);
		
// 		var fileSpanName = document.createElement("span");
// 		fileSpanName.className = "filename";
// 		fileSpanName.style.cssText = "-webkit-user-select: none;";
// 		var spanText = document.createTextNode("No file selected");
// 		fileSpanName.appendChild(spanText);
// 		fileDiv.appendChild(fileSpanName);
		
// 		var fileAct = document.createElement("span");
// 		fileAct.className = "action";
// 		fileAct.style.cssText = "-webkit-user-select: none;";
// 		var actText = document.createTextNode("Choose File");
// 		fileAct.appendChild(actText);
// 		fileDiv.appendChild(fileAct);
		
		
// 		container.appendChild(fileDiv);
		
		
// 		// Append a line break 
// 		container.appendChild(document.createElement("br"));
		if(iAdd > maxElements){
			alert("MAXIMO DE 10!!!");
			return;
		}
		document.getElementById("div"+iAdd).style.visibility = 'visible';
		document.getElementById("div"+iAdd).style.height="auto";
		
		iAdd++;
		
		
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
				<form action="uploadAndRunEquipType" id="equipTypeForm" class="form-horizontal" method="POST">
					<%
					int maxElements = 10;
					for(int i = 1; i <= maxElements; i++){
						String attrName = "txtSindel"+i;
						String txtSindel = (String)request.getSession().getAttribute(attrName);
						String outTxtSindel = "";
						if(txtSindel!=null){
							outTxtSindel = txtSindel;
						}
					%>
						<div id="div<%out.print(i);%>">
							<input id="equipName<%out.print(i);%>" name="equipName<%out.print(i);%>"/>
							<input name="file<%out.print(i);%>" type="file" id="file<%out.print(i);%>" >
							<input id="txtSindel<%out.print(i);%>" name="txtSindel<%out.print(i);%>" type="hidden" value="<%out.print(outTxtSindel);%>"/>
					<%
							if(i == 1){
					%>
							<input id="addFields" type="button" value="+" />
							<input type="submit" name="submit" value="Upload/Run" />
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
					
				<form action="uploadSindelEquip" class="form-horizontal" enctype="multipart/form-data" method="POST">
		 			<div class="controls">
		 				<input name="file" type="file">
						<input type="submit" name="submit" value="Upload" /><br>
					</div>
				</form>
						 					  
			</div>  
			<!-- ------------------- -->
			<!-- Content Sindel here -->
			<!-- ------------------- -->
			<form id="sindelForm" method="POST" action="runEquipScratch">

				<div style="border: 1px solid black; width: 100%;">
					<textarea id="code" name="code" style="width: 100%;"><%if(sindelValue != null){out.println(sindelValue);}%>
					</textarea>
					<input id="equipName" name="equipName"/>
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
					<h2>
						How can you use <i>Sindel?</i>
					</h2>
					<p>Description of Sindel language:</p>
					<div class="tooltip-demo well">
						<p class="muted" style="margin-bottom: 0;">
							Tight pants next level keffiyeh <a href="#" data-rel="tooltip"
								data-original-title="first tooltip">you probably</a> haven't
							heard of them. Photo booth beard raw denim letterpress vegan
							messenger bag stumptown. Farm-to-table seitan, mcsweeney's fixie
							sustainable quinoa 8-bit american appadata-rel <a href="#"
								data-rel="tooltip" data-original-title="Another tooltip">have
								a</a> terry richardson vinyl chambray. Beard stumptown, cardigans
							banh mi lomo thundercats. Tofu biodiesel williamsburg marfa, four
							loko mcsweeney's cleanse vegan chambray. A <a href="#"
								data-rel="tooltip" data-original-title="Another one here too">really
								ironic</a> artisan whatever keytar, scenester farm-to-table banksy
							Austin <a href="#" data-rel="tooltip"
								data-original-title="The last tip!">twitter handle</a> freegan
							cred raw denim single-origin coffee viral.
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

<%@include file="../templates/footer.jsp"%>