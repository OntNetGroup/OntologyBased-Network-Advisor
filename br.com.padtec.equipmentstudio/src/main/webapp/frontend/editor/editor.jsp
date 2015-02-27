<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>EquipmentStudio</title>

        <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />

		<!-- CSS CORE -->

        <link rel="stylesheet" type="text/css" href="../../core/rappid_api/css/joint.all.css" />

		<!-- CSS -->

        <link rel="stylesheet" type="text/css" href="./css/layout.css" />
        <link rel="stylesheet" type="text/css" href="./css/paper.css" />
        <link rel="stylesheet" type="text/css" href="./css/inspector.css" />
        <link rel="stylesheet" type="text/css" href="./css/navigator.css" />
        <link rel="stylesheet" type="text/css" href="./css/stencil.css" />
        <link rel="stylesheet" type="text/css" href="./css/halo.css" />
        <link rel="stylesheet" type="text/css" href="./css/selection.css" />
        <link rel="stylesheet" type="text/css" href="./css/toolbar.css" />
        <link rel="stylesheet" type="text/css" href="./css/statusbar.css" />
        <link rel="stylesheet" type="text/css" href="./css/freetransform.css" />
        <link rel="stylesheet" type="text/css" href="./css/style.css" />
    </head>
    <body>
        <h1>Equipment Studio</h1>
        
        <!-- TEMPLATES -->
        
        <%@include file="templates/toolbar.jsp"%>

		<!-- JS CORE -->

        <script src="../../core/rappid_api/js/joint.js"></script>
        <script src="../../core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->

        <script src="./js/keyboard.js"></script>
        <script src="./js/inspector.js"></script> 
        <script src="./js/main.js"></script>
        
        <!-- STENCILS -->
        
        <script src="../../stencils/transport_function/js/transport_function.js"></script>

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
        </script>
    </body>
</html>
