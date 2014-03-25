<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	String sindelValue = (String)request.getSession().getAttribute("txtSindelCode");
%>
<%@include file="../templates/header.jsp"%>
	
<script>

	$(document).ready(function () {

		$('.btn-load').click(function(event) {

			window.location.href = "/tnokco/welcome";
				
		});	//End load sindel file

		$('.btn-clean').click(function(event) {

			$.ajax({
				url : "cleanSindelCode",
				type : "GET",
				success : function(dto) {

					if(dto.ok == true)
						window.location.href = "/tnokco/sindel";

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
				
				hashVar = new Array();
				hashElement = new Array();
				hashAux = new Array();
				hashComposition = new Array();
				hashUseVar = new Array();
				hashRelationConnects = new Array();
				hashRelationBinds = new Array();
				hashRelationClient = new Array();
				hashInvElement = new Array();
				hashLocationTFStr = new Array();
				hashLocationTFGeo = new Array();
				hashTypeTTF = new Array();
				document.getElementById('res').innerHTML="";
				warning = "";

				//Used to get the value of the CodeMirror editor
				var result = parser.parse(editor.getValue());
				
				//document.getElementById('res2').innerHTML="";
				sReturn += printHash(hashElement, "element");
				sReturn += printHash(hashComposition, "composition");
				sReturn += printHash(hashRelationConnects, "connects");
				sReturn += printHash(hashRelationBinds, "binds");
				sReturn += printHash(hashRelationClient, "client");
				sReturn += printHash(hashLocationTFStr, "tf_location_str");
				sReturn += printHash(hashLocationTFGeo, "tf_location_geo");
				sReturn += printHash(hashTypeTTF, "ttf_type");

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
						"ok" : true
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
					"<strong>" + "</strong>"+ String(e) + 
				"</div>";
	
				$("#boxerro").prepend(html);
			
			}		
		});
		
        
      }); // end document read
	  
	function printHash(hash,type){
		
		var s = type+"#";
		for (var key in hash) {					
			s += key+":"+hash[key];
			s = s.substring(0,s.length)+";";
		}
		s+="!";
		return s;
	}

	// Code Mirror
	
</script>

	<div id="boxerro">
	</div>

	<h1>Sindel Editor</h1>

	<div class="row">
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h2>
						<i class="icon-edit"></i>Sindel input
					</h2>
					<div class="box-icon">
						<a href="#" class="btn-setting"><i class="icon-wrench"></i></a> 
						<a href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
					</div>
				</div>
				<div class="box-content">
				
					<div class="tooltip-demo well">
					
						Load Owl model.
				 		<form action="uploadOwlTnokco" class="form-horizontal" enctype="multipart/form-data" method="POST">
				 			<label class="control-label">Select Reasoner:</label>
							<div class="controls">

								  <label class="radio">
									<span class=""><input type="radio" name="optionsReasoner" id="optionsRadios1" value="pellet" ></span>
									Pellet
								  </label>
								  <div style="clear:both"></div>
								  <label class="radio">
									<span class="checked"><input type="radio" name="optionsReasoner" id="optionsRadios2" value="hermit" checked="checked"></span>
									Hermit
								  </label>						

							</div>
				 			<div class="controls">
								<input name="file" type="file">
								<input type="submit" name="submit" value="Upload" />
							</div>
						</form>
								 					  
					</div>  
		
					<!--		
					<div class="col-lg-5 col-md-5 col-sm-5">					
						<div class="message-view" id="white-area">
							<div class="message">				
								<form class="sindelForm"method="post" action="">		
											<fieldset>
												<textarea tabindex="3" class="input-lg col-xs-12" id="message" name="body" rows="12" placeholder="Click here to reply"></textarea>
												
												<br/><br/>
			
												<div class="actions">
			
													<button tabindex="3" type="submit" class="btn btn-success">Send message</button>
			
												</div>
			
											</fieldset>
			
									</form>
							</div>						
						</div>					
					</div>	-->
					
					<!-- ------------------- -->
					<!-- Content Sindel here -->
					<!-- ------------------- -->
					<form id="sindelForm" method="POST" action="runSindel">
					
						<div style="border: 1px solid black; width:100%;">
						
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
						 	  
						 	  <input type="button" class="btn btn-pre btn-load" value="Load file" />
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
							<div id="res">	
							</div>
							<div id="res2" style="border: 2px solid black;">					
							</div>	
							  
							<!-- ------------------------ -->
							<!-- END -Content Sindel here -->
							<!-- ------------------------ -->
		 
							</div>
					
					</form>
					
					<div class="row">
					  <div class="col-lg-12">
						<h2>How can you use <i>Sindel?</i></h2>
						<p>Description of Sindel language:</p>
						<div class="tooltip-demo well">
						  <p class="muted" style="margin-bottom: 0;">Tight pants next level keffiyeh <a href="#" data-rel="tooltip" data-original-title="first tooltip">you probably</a> haven't heard of them. Photo booth beard raw denim letterpress vegan messenger bag stumptown. Farm-to-table seitan, mcsweeney's fixie sustainable quinoa 8-bit american appadata-rel <a href="#" data-rel="tooltip" data-original-title="Another tooltip">have a</a> terry richardson vinyl chambray. Beard stumptown, cardigans banh mi lomo thundercats. Tofu biodiesel williamsburg marfa, four loko mcsweeney's cleanse vegan chambray. A <a href="#" data-rel="tooltip" data-original-title="Another one here too">really ironic</a> artisan whatever keytar, scenester farm-to-table banksy Austin <a href="#" data-rel="tooltip" data-original-title="The last tip!">twitter handle</a> freegan cred raw denim single-origin coffee viral.
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