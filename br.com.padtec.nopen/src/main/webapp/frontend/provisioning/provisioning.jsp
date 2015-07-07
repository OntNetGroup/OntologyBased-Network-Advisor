<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>Provisioning</title>

		<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
		

		<!-- CSS CORE -->

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />
		<link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/dialog.css" />

		<!-- CSS -->

        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/layout.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/paper.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/inspector.css" />
<!--         <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/navigator.css" /> -->
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/stencil.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/halo.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/selection.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/toolbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/statusbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/freetransform.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/style.css" />  
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/header.css" />

		<link rel="stylesheet" type="text/css" href="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.css"/>
    </head>
    <body>
        
        <!-- TEMPLATES -->
		<div class="template">
       		<%@include file="templates/header.jsp"%>
        	<%@include file="templates/toolbar.jsp"%>
		</div>
		<div id="black_overlay"></div>
		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->
		
		<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
		<script src="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.js"></script>
		
        <script src="/nopen/frontend/provisioning/js/keyboard.js"></script>
        <script src="/nopen/frontend/provisioning/js/inspector.js"></script> 
        <script src="/nopen/frontend/provisioning/js/layerNetwork.js"></script> 
        <script src="/nopen/frontend/provisioning/js/stencil.js"></script> 
        <script src="/nopen/frontend/provisioning/js/main.js"></script>
        
        <!-- SOURCE -->
        
        <script src="/nopen/frontend/common/libs/nopen/nopen.js"></script>
        <script src="/nopen/frontend/provisioning/src/nopen.provisioning.model.js"></script>
        <script src="/nopen/frontend/provisioning/src/nopen.provisioning.owl.js"></script>
        <script src="/nopen/frontend/provisioning/src/nopen.provisioning.file.js"></script>
        <script src="/nopen/frontend/provisioning/src/nopen.provisioning.app.js"></script>
        
        <!-- STENCILS -->
        
        <script src="/nopen/frontend/provisioning/js/stencil.js"></script>
         
        <!-- DIALOGS -->
		
		<input type="text" id="filename" style="display:none"/>
	
        <script>
            // Uncomment the following line and comment the line after if you
            // want to use channels.
            //var app = new Rappid({ channelUrl: 'ws://localhost:4141' });
            var app = new Rappid;
            Backbone.history.start();
            
            //Start provisioning
            var provisioning = new nopen.provisioning.App();
            provisioning.start(app);
            
            //var uuid = joint.util.uuid();

        </script>
    </body>
</html>
