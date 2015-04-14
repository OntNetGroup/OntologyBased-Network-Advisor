<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>EQUIPMENT STUDIO</title>

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
		
		<!-- TEMPLATES -->
		
       	<%@include file="templates/header.jsp"%>
        <%@include file="templates/toolbar.jsp"%>

		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->
		
		<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
		<script src="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.js"></script>

        <script src="/nopen/frontend/equipment-studio/js/keyboard.js"></script>
        <script src="/nopen/frontend/equipment-studio/js/inspector.js"></script>
        <script src="/nopen/frontend/equipment-studio/js/stencil.js"></script>
        <script src="/nopen/frontend/equipment-studio/js/main.js"></script>
        
        <script src="/nopen/frontend/equipment-studio/js/resizing.js"></script>
        <script src="/nopen/frontend/equipment-studio/js/equipmentHandle.js"></script>
        
   		<!-- PLUGINS -->
        
        <script src="/nopen/frontend/equipment-studio/plugins/open-equipment.js"></script>
        <script src="/nopen/frontend/equipment-studio/plugins/save-equipment.js"></script>
         
         
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
            
            resizing(app.graph);
            
            graphHandle(app.graph);
            equipmentHandle(app.paper, app.graph);
            
           // graphHandle(app.graph);
            //paperHandle(app.paper);
            
            var uuid = joint.util.uuid();
            
            $('#btn-save').click(function(){
            generateSaveEquipmentDialog(app.graph);
            });
        
        </script>
    </body>
</html>
