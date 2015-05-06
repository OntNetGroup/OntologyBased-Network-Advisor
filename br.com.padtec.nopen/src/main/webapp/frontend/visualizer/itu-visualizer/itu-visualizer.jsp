<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>ITU Visualizer</title>

        <!--<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />-->

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />
        
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/visualizer/itu-visualizer/css/dialog.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/visualizer/itu-visualizer/css/style.css" />
    </head>
<body>
		
		<div class="toolbar-container" style="display:none">
		     <button id="btn-reject" class="btn" data-tooltip="Reject and Return"><img src="/nopen/frontend/visualizer/itu-visualizer/img/reject.png" alt="Reject and Return"/> Reject and Return </button>
		     <button id="btn-zoom-to-fit" class="btn" data-tooltip="Zoom To Fit"><img src="/nopen/frontend/visualizer/itu-visualizer/img/zoomtofit.png" alt="Zoom To Fit"/></button>
		</div>
		
		<div class="stencil-container" style="display:none">
			<label>Stencil</label>
			<button class="btn-expand" title="Expand all">+</button>
			<button class="btn-collapse" title="Collapse all">-</button>
		</div>
		<div class="paper-container"></div>
		
		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->

        <script src="/nopen/frontend/visualizer/itu-visualizer/js/keyboard.js"></script>
        
		<script src="/nopen/core/rappid_api/js/joint.shapes.devs.js"></script>
        <script src="/nopen/frontend/visualizer/itu-visualizer/js/inspector.js"></script>
       	<script src="/nopen/frontend/visualizer/itu-visualizer/js/stencil.js"></script>
        <script src="/nopen/frontend/visualizer/itu-visualizer/js/main.js"></script>
		

        <script>
            var app = new Rappid;
            Backbone.history.start();
             
            //check if windows is a iframe
            if(window.self !== window.top){
	            if(!(parent.cardArray[parent.cellId] === undefined)){
	            	app.graph.fromJSON(parent.cardArray[parent.cellId]);
	            }
            }
            
            $('#btn-zoom-to-fit').click();
            
            var closeType;
            
//             $('#btn-back').click(function(){
//             	closeType = "save";
//             	parent.closeIframe();
//             });
          
            
        </script>
    </body>
</html>
