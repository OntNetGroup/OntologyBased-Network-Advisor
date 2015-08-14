<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>Network Topology</title>

		<!-- CSS CORE -->

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/dialog.css" />

		<!-- CSS -->

        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/layout.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/paper.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/inspector.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/navigator.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/stencil.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/halo.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/selection.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/toolbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/statusbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/freetransform.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/style.css" />  
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/header.css" />
<!-- 		<link rel="stylesheet" type="text/css" href="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.css"/> -->	
		<link rel="stylesheet" type="text/css" href="/nopen/frontend/network-topology/css/iframe.css" />
    </head>
    <body>
        
        
        <!-- TEMPLATES -->
		
       	<%@include file="templates/header.jsp"%>
        <%@include file="templates/toolbar.jsp"%>

		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

<!-- 		<!-- PLUGINS --> -->
        
<!--         <script src="/nopen/frontend/network-topology/plugins/topology-exporter.js"></script> -->
<!--         <script src="/nopen/frontend/network-topology/plugins/topology-importer.js"></script> -->
<!--         <script src="/nopen/frontend/network-topology/plugins/save-topology.js"></script> -->
<!--         <script src="/nopen/frontend/network-topology/plugins/open-topology.js"></script> -->

		<!-- JS -->
		
		<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
		<script src="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.js"></script>
		<script src="/nopen/frontend/template/js/jquery.nestable.min.js"></script>
		
        <script src="/nopen/frontend/network-topology/js/keyboard.js"></script>
        <script src="/nopen/frontend/network-topology/js/inspector.js"></script> 
        <script src="/nopen/frontend/network-topology/js/main.js"></script>
        <script src="/nopen/frontend/network-topology/js/graphHandle.js"></script>
        <script src="/nopen/frontend/network-topology/js/paperHandle.js"></script>
        
        <!-- SOURCE -->

		<script src="/nopen/frontend/network-topology/libs/topology.js"></script>
        
        <script src="/nopen/frontend/common/libs/nopen/nopen.js"></script>
        <script src="/nopen/frontend/network-topology/src/nopen.topology.util.js"></script>
        <script src="/nopen/frontend/network-topology/src/nopen.topology.model.js"></script>
        <script src="/nopen/frontend/network-topology/src/nopen.topology.file.js"></script>
        <script src="/nopen/frontend/network-topology/src/nopen.topology.exporter.js"></script>
        <script src="/nopen/frontend/network-topology/src/nopen.topology.importer.js"></script>
        <script src="/nopen/frontend/network-topology/src/nopen.topology.app.js"></script>
        
        <!-- STENCILS -->
        
        <script src="/nopen/frontend/network-topology/js/stencil.js"></script>
        
		 <!-- DIALOGS -->

		<input type="text" id="filename" style="display:none"/>

		<div id="equipment-dialog" title="ITU" style="display: none">
			<iframe id="equipment-iframe" src="" scrolling="no"></iframe>
		</div>
		
        <script>
            // Uncomment the following line and comment the line after if you
            // want to use channels.
            //var app = new Rappid({ channelUrl: 'ws://localhost:4141' });
            var app = new Rappid;
            Backbone.history.start();

          	//Start provisioning
            var topology = new nopen.topology.App();
            topology.start(app);
            
        </script>
    </body>
</html>
