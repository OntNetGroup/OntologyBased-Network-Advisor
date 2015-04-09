<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>EQUIPMENT STUDIO</title>

        <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />

        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/layout.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/paper.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/inspector.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/navigator.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/stencil.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/halo.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/selection.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/toolbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/statusbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/freetransform.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/style.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/equipment-studio/css/header.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.css"/>
    </head>
    <body>
        <h1 style="margin-top:40px;">EQUIPMENT STUDIO</h1>
		
       	<%@include file="templates/header.jsp"%>
        <div class="toolbar-container">
            <!-- <button id="btn-undo" class="btn" data-tooltip="Undo"><img src="/nopen/frontend/equipment-studio/img/undo.png" alt="Undo"/></button> -->
            <!-- <button id="btn-redo" class="btn" data-tooltip="Redo"><img src="/nopen/frontend/equipment-studio/img/redo.png" alt="Redo"/></button> -->
            <button id="btn-clear" class="btn" data-tooltip="Clear Paper"><img src="/nopen/frontend/equipment-studio/img/clear.png" alt="Clear"/></button>
            <button id="btn-svg" class="btn" data-tooltip="Open as SVG in a New Window">open as SVG</button>
            <button id="btn-png" class="btn" data-tooltip="Open as PNG in a New Window">open as PNG</button>
			<!-- <button id="btn-print-file" class="btn" data-tooltip="Save all imgs index in a file">save index to file</button>  -->
            <button id="btn-print" class="btn" data-tooltip="Open a Print Dialog"><img src="/nopen/frontend/equipment-studio/img/print.png" alt="Print"/></button>
            <button id="btn-zoom-in" class="btn" data-tooltip="Zoom In"><img src="/nopen/frontend/equipment-studio/img/zoomin.png" alt="Zoom in"/></button>
            <button id="btn-zoom-out" class="btn" data-tooltip="Zoom Out"><img src="/nopen/frontend/equipment-studio/img/zoomout.png" alt="Zoom out"/></button>
            <div class="panel">
              <span id="zoom-level">100</span>
              <span>%</span>
            </div>
            <button id="btn-zoom-to-fit" class="btn" data-tooltip="Zoom To Fit"><img src="/nopen/frontend/equipment-studio/img/zoomtofit.png" alt="Zoom To Fit"/></button>
            <button id="btn-fullscreen" class="btn" data-tooltip="Toggle Fullscreen Mode"><img src="/nopen/frontend/equipment-studio/img/fullscreen.png" alt="Fullscreen"/></button>
            <button id="btn-to-front" class="btn" data-tooltip="Bring Object to Front">to front</button>
            <button id="btn-to-back" class="btn" data-tooltip="Send Object to Back">to back</button>
            <button id="btn-layout" class="btn" data-tooltip="Auto-layout Graph">layout</button>
            <label data-tooltip="Change Grid Size">Grid size:</label>
            <input type="range" value="10" min="1" max="50" step="1" id="input-gridsize" />
            <output id="output-gridsize">10</output>
            <label data-tooltip="Enable/Disable Snaplines">Snaplines:</label>
            <input type="checkbox" id="snapline-switch" checked/>
        </div>
        <div class="stencil-container">
            <label>Stencil</label>
            <button class="btn-expand" title="Expand all">+</button>
            <button class="btn-collapse" title="Collapse all">-</button>
        </div>
        <div class="paper-container"></div>
        <div class="inspector-container"></div>
        <div class="navigator-container"></div>
        <div class="statusbar-container"><span class="rt-colab"></span></div>

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->
		
		<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
		<script src="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.js"></script>

        <script src="/nopen/frontend/equipment-studio/js/keyboard.js"></script>

		<script src="/nopen/core/rappid_api/js/joint.shapes.devs.js"></script>
        <script src="/nopen/frontend/equipment-studio/js/inspector.js"></script>
		<script src="/nopen/frontend/equipment-studio/js/mymodel.js"></script>
		<script src="/nopen/frontend/equipment-studio/js/mypool.js"></script>
        <script src="/nopen/frontend/equipment-studio/js/stencil.js"></script>
        <script src="/nopen/frontend/equipment-studio/js/main.js"></script>
        
   
         
        <div id="save-dialog" title="Save Equipment" style="display:none">
			File name: <input type="text" id="save-filename" />
		</div>
		
		
	<!--[if IE 9]>
	    <script src="./lib/base64.js"></script>
	    <script type="text/javascript">
	      // SVG Export requires window.btoa/atoa extension to convert binary data (the `b`)
	      // to base64 (ascii, the `a`). Unfortunately it is not available in IE9.
	      // To get it working under IE9 you may include compatible solution like `stringencoders`
	      // (`https://code.google.com/p/stringencoders/source/browse/trunk/javascript/base64.js`)
	      // and create a global alias `btoa`.
	      window.btoa = base64.encode
	      // `-ms-user-select: none` doesn't work in IE9
	      document.onselectstart = function() { return false; };
	    </script>
	<![endif]-->
        <script>
            // Uncomment the following line and comment the line after if you
            // want to use channels.
            //var app = new Rappid({ channelUrl: 'ws://localhost:4141' });
            var app = new Rappid;
            Backbone.history.start();
            
           // graphHandle(app.graph);
          //  paperHandle(app.paper);
            
            //var uuid = joint.util.uuid();
            
           // $('#btn-save').click(function(){
          //  generateSaveEquipmentDialog(app.graph);
          	
        </script>
    </body>
</html>
