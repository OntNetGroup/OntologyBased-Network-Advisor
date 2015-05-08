<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf8"/>
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

        <title>ITU Studio</title>

        <!--<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />-->

        <link rel="stylesheet" type="text/css" href="/nopen/core/rappid_api/css/joint.all.css" />

        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/layout.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/paper.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/inspector.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/navigator.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/stencil.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/halo.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/selection.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/toolbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/statusbar.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/freetransform.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/style.css" />
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/header.css" />
        
        <link rel="stylesheet" type="text/css" href="/nopen/frontend/itu-studio/css/dialog.css" />
    </head>
    <body>
		
		<!-- TEMPLATES -->
		
       	<%@include file="templates/header.jsp"%>
        <%@include file="templates/toolbar.jsp"%>

		<!-- JS CORE -->

        <script src="/nopen/core/rappid_api/js/joint.js"></script>
        <script src="/nopen/core/rappid_api/js/joint.all.js"></script>

		<!-- JS -->

        <script src="/nopen/frontend/itu-studio/js/keyboard.js"></script>
        
		<script src="/nopen/core/rappid_api/js/joint.shapes.devs.js"></script>
		<script src="/nopen/frontend/itu-studio/js/typeEnum.js"></script>
		<script src="/nopen/frontend/itu-studio/js/util.js"></script>
        <script src="/nopen/frontend/itu-studio/js/inspector.js"></script>
		<script src="/nopen/frontend/itu-studio/js/layer.js"></script>
       	<script src="/nopen/frontend/itu-studio/js/stencil.js"></script>
        <script src="/nopen/frontend/itu-studio/js/ajax.js"></script>
        <script src="/nopen/frontend/itu-studio/js/main.js"></script>
        <script src="/nopen/frontend/itu-studio/js/graphHandler.js"></script>
        <script src="/nopen/frontend/itu-studio/js/validator.js"></script>
		
		
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
            var app = new Rappid;
            Backbone.history.start();
             
            //check if windows is a iframe
            if(window.self !== window.top){
	            if(!(parent.cardArray[parent.cellId] === undefined)){
	            	app.graph.fromJSON(parent.cardArray[parent.cellId]);
	            }
            }
            
            $('#btn-zoom-to-fit').click();
            
        	app.setCardID(parent.cellId);
        	app.setCardName('myCard'); //TODO: passar o nome do card, vindo do Equipment Studio
        	app.setCardTech('MEF'); //TODO: passar a tecnologia do card, vinda do Equipment Studio
            graphHandler(app.graph, app);
            validator(app.validator, app.graph, app);
            
        </script>
    </body>
</html>
