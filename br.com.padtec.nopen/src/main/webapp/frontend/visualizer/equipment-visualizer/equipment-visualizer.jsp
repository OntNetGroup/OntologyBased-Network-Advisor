<!DOCTYPE html>
<html>
<head>
<meta charset="utf8" />
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

<title>Equipment Visualizer</title>

<!-- CSS CORE -->

<link rel="stylesheet" type="text/css"
	href="/nopen/core/rappid_api/css/joint.all.css" />

<!-- CSS JQUERY -->

<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.css" />

<!-- CSS -->

<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/visualizer/equipment-visualizer/css/dialog.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/visualizer/equipment-visualizer/css/style.css" />
	
</head>
<body>

	<div class="toolbar-container">
	     <button id="btn-back" class="btn" data-tooltip="Close"><img src="/nopen/frontend/visualizer/itu-visualizer/img/reject.png" alt="Close"/> Close </button>
	     <button id="btn-zoom-in" class="btn" data-tooltip="Zoom In"><img src="/nopen/frontend/itu-studio/img/zoomin.png" alt="Zoom in"/></button>
	     <button id="btn-zoom-out" class="btn" data-tooltip="Zoom Out"><img src="/nopen/frontend/itu-studio/img/zoomout.png" alt="Zoom out"/></button>
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

	<!-- JQUERY -->

	<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
	<script src="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.js"></script>

	<!-- JS -->

	<script src="/nopen/frontend/visualizer/equipment-visualizer/js/mymodel.js"></script>
	<script src="/nopen/frontend/visualizer/equipment-visualizer/js/keyboard.js"></script>
	<script src="/nopen/frontend/visualizer/equipment-visualizer/js/inspector.js"></script>
	<script src="/nopen/frontend/visualizer/equipment-visualizer/js/stencil.js"></script>
	<script src="/nopen/frontend/visualizer/equipment-visualizer/js/main.js"></script>
	<script src="/nopen/frontend/visualizer/equipment-visualizer/js/ituHandle.js"></script>

	<!-- PLUGINS -->

	<script src="/nopen/frontend/visualizer/equipment-visualizer/plugins/open-equipment.js"></script>

	<!-- DIALOGS -->

	<div id="itu-dialog" title="ITU" style="display: none">
		<iframe id="itu-iframe" src="" scrolling="no"></iframe>
		<!--  File name: <input type="text" id="save-filename" /> -->
	</div>

	<script>
            
            var app = new Rappid;
            Backbone.history.start();
            
            if(getUrlParameter('equipment')){
            	var equipment = getUrlParameter('equipment');
            	openFromURL(equipment, app.graph);
            }
            
            $('#btn-zoom-to-fit').click();
			
            $('#btn-back').click(function(){
            	parent.closeIframe();
            });
            
            ituHandle(app.paper, app.graph);
            

        
        </script>
</body>
</html>
