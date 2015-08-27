<!DOCTYPE html>
<html>
<head>
	    <meta charset="utf8"/>
	    <title>ITU Visualizer</title>
	
	    <!--<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />-->
	
	    <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />
	    <link rel="stylesheet" type="text/css" href="/nopen/frontend/visualizer/itu-visualizer/css/style.css" />
</head>
<body>
		
		<div class="toolbar-container">
		     <button id="btn-back" class="btn" data-tooltip="Return"><img src="/nopen/frontend/visualizer/itu-visualizer/img/back_arrow.png" alt="Return"/> Return </button>
		     <button id="btn-zoom-in" class="btn" data-tooltip="Zoom In"><img src="/nopen/frontend/visualizer/itu-visualizer/img/zoomin.png" alt="Zoom in"/></button>
		     <button id="btn-zoom-out" class="btn" data-tooltip="Zoom Out"><img src="/nopen/frontend/visualizer/itu-visualizer/img/zoomout.png" alt="Zoom out"/></button>
		     <button id="btn-zoom-to-fit" class="btn" data-tooltip="Zoom To Fit"><img src="/nopen/frontend/visualizer/itu-visualizer/img/zoomtofit.png" alt="Zoom To Fit"/></button>
		</div>
		
<!-- 		<div class="stencil-container" style="display:none"></div> -->
		<div class="paper-container"></div>
		
		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->

		<script src="/nopen/core/rappid_api/js/joint.shapes.devs.js"></script>
		<script src="/nopen/frontend/visualizer/itu-visualizer/js/keyboard.js"></script>
        
		<script src="/nopen/core/rappid_api/js/joint.shapes.devs.js"></script>
		<script src="/nopen/frontend/visualizer/itu-visualizer/js/typeEnum.js"></script>
		<script src="/nopen/frontend/visualizer/itu-visualizer/js/util.js"></script>
        <script src="/nopen/frontend/visualizer/itu-visualizer/js/inspector.js"></script>
		<script src="/nopen/frontend/visualizer/itu-visualizer/js/layer.js"></script>
       	<script src="/nopen/frontend/visualizer/itu-visualizer/js/stencil.js"></script>
        <script src="/nopen/frontend/visualizer/itu-visualizer/js/main.js"></script>
        
        <script src="/nopen/frontend/visualizer/itu-visualizer/plugins/open-itu.js"></script>

        <script>
            var app = new Rappid;
            Backbone.history.start();
            
            if (getUrlParameter('equipment') && getUrlParameter('card') && getUrlParameter('visualizer')) {
    			var equipment = getUrlParameter('equipment');
    			var card = getUrlParameter('card');
    			var visualizer = getUrlParameter('visualizer');
//     			console.log(visualizer);
    			if(visualizer === 'true'){
    				
    				openFromURLVisualizer(equipment,card , app.graph);
    			
    			}else{
    				
    				openFromURL(equipment, card, app.graph);
    			}
    		}
            
            $('#btn-zoom-to-fit').click();
            
            $('#btn-back').click(function(){
            	parent.closeIframe();
            });
        </script>
    </body>
</html>
