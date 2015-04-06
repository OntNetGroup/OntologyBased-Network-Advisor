<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>Provisioning</title>

		<!-- CSS CORE -->

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />

		<!-- CSS -->


        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/layout.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/paper.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/inspector.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/provisioning/css/navigator.css" />
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

        
		<h1 style="margin-top:40px;">Provisioning</h1>
		
       	<%@include file="templates/header.jsp"%>
        <%@include file="templates/toolbar.jsp"%>

		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- PLUGINS -->
        
        <script src="/nopen/frontend/network-topology/plugins/save-topology.js"></script>
        <script src="/nopen/frontend/provisioning/plugins/open-topology.js"></script>

		<!-- JS -->
		
		<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
		<script src="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.js"></script>
		
        <script src="/nopen/frontend/provisioning/js/keyboard.js"></script>
        <script src="/nopen/frontend/provisioning/js/inspector.js"></script> 
        <script src="/nopen/frontend/provisioning/js/main.js"></script>
        <script src="/nopen/frontend/provisioning/js/graphHandle.js"></script>
        
        <!-- STENCILS -->
        
        <script src="/nopen/stencils/provisioning/js/provisioning.js"></script>
         
        <!-- DIALOGS -->
		
		<div id="save-dialog" title="Save Topology" style="display:none">
			File name: <input type="text" id="save-filename" />
		</div>

		<div id="open-dialog" title="Open Topology" style="display:none"></div>

	
        <script>
            // Uncomment the following line and comment the line after if you
            // want to use channels.
            //var app = new Rappid({ channelUrl: 'ws://localhost:4141' });
            var app = new Rappid;
            Backbone.history.start();

            graphHandle(app.graph);
            
            var uuid = joint.util.uuid();

            $('#btn-save').click(function(){
            	if(checkNodeEquipments(app.graph)){
            		generateSaveTopologyDialog(app.graph);
            	}
            });
            
            $('#btn-open').click(function(){	
            	getTopologies(app.graph);      	
            });

        </script>
    </body>
</html>
