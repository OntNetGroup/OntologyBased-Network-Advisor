<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>ITU Studio</title>
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

        <!--<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />-->

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />

        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/layout.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/paper.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/inspector.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/navigator.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/stencil.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/halo.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/selection.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/toolbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/statusbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/freetransform.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/style.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/header.css" />
        
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/dialog.css" />
    </head>
    <body>
		
		<div id="msg" style="font-size:largest;">
		<!-- you can set whatever style you want on this -->
		Loading, please wait...
		</div>
				
		<!-- TEMPLATES -->
		<div id="body" style="opacity:0"> 
       	<%@include file="templates/header.jsp"%>
        <%@include file="templates/toolbar.jsp"%>
        </div>
        
		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->
		<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
  		<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
		<script>

		$(document).ready(function() {
		    $('#body').css('opacity', 1);
 		    $('#msg').hide();
		});
		</script>
		<script src="/nopen/frontend/itu-studio/js/keyboard.js"></script>
        
		<script src="/nopen/core/rappid_api/js/joint.shapes.devs.js"></script>
		<script src="/nopen/frontend/itu-studio/js/typeEnum.js"></script>
		<script src="/nopen/frontend/itu-studio/js/subtypeEnum.js"></script>
		<script src="/nopen/frontend/itu-studio/js/util.js"></script>
        <script src="/nopen/frontend/itu-studio/js/inspector.js"></script>
		<script src="/nopen/frontend/itu-studio/js/layer.js"></script>
       	<script src="/nopen/frontend/itu-studio/js/stencil.js"></script>
        <script src="/nopen/frontend/itu-studio/js/ajax.js"></script>
        <script src="/nopen/frontend/itu-studio/js/main.js"></script>
        <script src="/nopen/frontend/itu-studio/js/graphHandler.js"></script>
        <script src="/nopen/frontend/itu-studio/js/validator.js"></script>
		
		<!-- PLUGINS -->
		<script src="/nopen/frontend/itu-studio/plugins/open-itu.js"></script>
		<script src="/nopen/frontend/itu-studio/plugins/save-itu.js"></script>

        <script>
            var app = new Rappid;
            Backbone.history.start();
             
            var equipment = undefined;
            var card = undefined;
            var discardChanges = true;
            
        	app.setCardID(parent.cellId);
        	app.setCardName('myCard'); //TODO: passar o nome do card, vindo do Equipment Studio
        	app.initializeStencil('MEF'); //TODO: passar a tecnologia do card, vinda do Equipment Studio
        	app.initializeTFunctionAttributes('874.1'); //TODO: passar a referência usada, vinda do Equipment Studio
            
            if (getUrlParameter('equipment') && getUrlParameter('card')){
            	equipment = getUrlParameter('equipment');
    			card = getUrlParameter('card');
    			openFromURL(equipment, card, app.graph, app);
    		}
			
            $('#btn-save').click( function(){
            	if(equipment != undefined && card != undefined){
            		saveITUFile(equipment, card, app.graph);
            		$('#btn-save').prop('disabled', true);
            	}
            });
            
            $('#btn-return').click( function(){
            	if(parent){
            		if(!$('#btn-save').is(':disabled')){
                		if(confirm('Discard unsaved changed?')){
                			discardChanges = true;
                			parent.closeIframe();
                			
                		}
                	}
            		else{
            			parent.closeIframe();
            		}
            	}
            });
            
            $('#btn-zoom-to-fit').click();
            
            graphHandler(app.graph, app);
            validator(app.validator, app.graph, app);
            
          	//Enable Save Button when paper change
            $('.paper-container').bind("DOMSubtreeModified",function(){
            	$('#btn-save').prop('disabled', false);
            	discardChanges = false;
           	});
            //Start with Save Button disabled
			$('#btn-save').prop('disabled', true);
            
        </script>
    </body>
</html>
