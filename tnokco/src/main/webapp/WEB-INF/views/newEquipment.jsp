<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	String sindelValue = (String)request.getSession().getAttribute("txtSindelCode");
%>
<%@include file="../templates/header.jsp"%>

<script>

	$(document).ready(function () {
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
		
        
      }); // end document read
	  
//       function cleanUpHashs(){
//   		currentLine = 0;
  	 
//   		warning = "";
  		 
//   		hashVarType = new Array();
//   		hashTypeVar = new Array();
//   		hashUsedRelation = new Array();

//   		hashUsedVariable = new Array();

//   		hashComposition = new Array();

//   		hashRelation = new Array();
//   		hashRelation["binds"] = new Array();
//   		hashRelation["connects"] = new Array();
//   		hashRelation["maps"] = new Array();
//   		hashRelation["client"] = new Array();
//   		hashRelation["component_of"] = new Array();

//   		hashAttribute = new Array();
//   		hashAttribute['str_location'] = new Array();
//   		hashAttribute['geo_location'] = new Array();
//   		hashAttribute['tf_type'] = new Array();
//   	}
  	  
//   	function printHashTypeVar(hash){
//   		var s = "elements"+"#";
  		
//   		for (var key in hash) {					
//   			s += key+":"+hash[key];
//   			s = s.substring(0,s.length)+";";
//   		}
//   		s+="!";
//   		return s;
//   	}
  	
//   	function printHashRelations(hash){
//   		var s = "";
  				
//   		for (var key in hash) {
//   			if(key == "component_of"){
//   				s += key+"#";
//   				for(var i = 0; i < hash[key].length; i++){
//   					s += hash[key][i]+";";
//   				}
//   				s = s.substring(0,s.length-1);
//   				s += "!";
//   			}else{
//   				s += key+"#";
//   				s += hash[key]+"!";						
//   			}
//   		}		
//   		return s;
//   	}
  	
//   	function printHashAttribute(hash){
//   		var s = "";
  				
//   		for (var key in hash) {			
//   			s += key+"#";
//   			for(var i = 0; i < hash[key].length; i++){
//   				s += hash[key][i]+";";
//   			}
//   			s = s.substring(0,s.length-1);
//   			s += "!";			
//   		}		
//   		return s;
//   	}

	// Code Mirror
	
		function addFields(){
				
			var container = document.getElementById("equipTypeForm");
			
			// Create an <input> element, set its type and name attributes
			var equipName = document.createElement("input");
			equipName.name = "equipName";
			equipName.id = "equipName";
			container.appendChild(equipName);
			
			var fileDiv = document.createElement("div");
			fileDiv.className = "uploader";
			fileDiv.id = "uniform-file";
			
			var fileText = document.createElement("input");
			fileText.type = "file";
			fileText.name = "file";
			fileText.id = "file";
			fileDiv.appendChild(fileText);
			
			var fileSpanName = document.createElement("span");
			fileSpanName.className = "filename";
			fileSpanName.style.cssText = "-webkit-user-select: none;";
			var spanText = document.createTextNode("No file selected");
			fileSpanName.appendChild(spanText);
			fileDiv.appendChild(fileSpanName);
			
			var fileAct = document.createElement("span");
			fileAct.className = "action";
			fileAct.style.cssText = "-webkit-user-select: none;";
			var actText = document.createTextNode("Choose File");
			fileAct.appendChild(actText);
			fileDiv.appendChild(fileAct);
			
			
			container.appendChild(fileDiv);
			
			
			// Append a line break 
			container.appendChild(document.createElement("br"));
			
		};
			
	
	
</script>

<div id="boxerro"></div>

<h1>Equipment from Sindel Editor</h1>

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>New Equipment from Equipment Type
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-setting"><i class="icon-wrench"></i></a> <a
						href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			
			<div class="tooltip-demo well" id="equipTypeContainer">
				<form action="runEquipType" class="form-horizontal" enctype="multipart/form-data" method="POST" id="equipTypeForm">
					<input id="equipName" name="equipName" id="equipName"/>
					<input name="file" type="file" id="file">
					<input id="addFields" type="button" value="+" />
					<input type="submit" name="submit" value="Upload" /><br>
				</form>
			</div>  
			
			<div class="box-header">
				<h2>
					<i class="icon-edit"></i>New Equipment from Scratch
				</h2>
				<div class="box-icon">
					<a href="#" class="btn-setting"><i class="icon-wrench"></i></a> <a
						href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			
			<div class="tooltip-demo well">
					
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
					<input id="equipName" name="equipName"/>
					<!-- TEXTA AREA -->
					<%
								if(sindelValue == null)
								{
									out.println("<textarea id=\"code\" name=\"code\" style=\"width: 100%; padding:5px;\">");
									out.println("</textarea>");
								}else {
									
									out.println("<textarea id=\"code\" name=\"code\" style=\"width: 100%; padding:5px;\">");
									out.println(sindelValue);
									out.println("</textarea>");
									
								}
							%>
					<!-- TEXTA AREA -->

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