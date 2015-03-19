<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>Network Topology</title>

        <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />

		<!-- CSS CORE -->

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />

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
    </head>
    <body>
        <h1>Network Topology</h1>
        
        <!-- TEMPLATES -->
        
        <%@include file="templates/toolbar.jsp"%>

		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- PLUGINS -->
        
        <script src="/nopen/plugins/topology-exporter.js"></script>
        <script src="/nopen/plugins/topology-importer.js"></script>

		<!-- JS -->
		
		<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
		
        <script src="/nopen/frontend/network-topology/js/keyboard.js"></script>
        <script src="/nopen/frontend/network-topology/js/inspector.js"></script> 
        <script src="/nopen/frontend/network-topology/js/main.js"></script>
        
        <!-- STENCILS -->
        
        <script src="/nopen/stencils/network-topology/js/network-topology.js"></script>

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
            
            $('#btn-import-xml').click(function(){
            	$('#file').click();
            	//$('#fileForm').submit();
				//importTopology(app.graph);
			});

            $('#file').change(function(){
            	importTopology(app.graph);
            });
            
            $('#btn-pre').click(function(){previewTopology(app.graph)});
            $('#btn-export-xml').click(function(){exportTopology(app.graph)});
        </script>
    </body>
</html>