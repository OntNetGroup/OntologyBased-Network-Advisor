<!DOCTYPE html>
<html>
<head>
<meta charset="utf8" />
<!--        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/> -->

<title>Equipment Studio</title>

<!-- CSS CORE -->

<link rel="stylesheet" type="text/css"
	href="/nopen/core/rappid_api/css/joint.all.css" />

<!-- CSS JQUERY -->

<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.css" />

<!-- CSS -->

<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/layout.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/paper.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/inspector.css" />
<!-- <link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/navigator.css" /> -->
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/stencil.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/halo.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/selection.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/toolbar.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/statusbar.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/freetransform.css" />

<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/style.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/header.css" />
<link rel="stylesheet" type="text/css"
	href="/nopen/frontend/equipment-studio/css/dialog.css" />

</head>
<body>

	<!-- TEMPLATES -->

	<%@include file="templates/header.jsp"%>
	<%@include file="templates/toolbar.jsp"%>
	<!-- JS CORE -->

	<script src="/nopen/core/rappid_api/js/joint.js"></script>
	<script src="/nopen/core/rappid_api/js/joint.all.js"></script>

	<!-- JQUERY -->

	<script src="/nopen/frontend/common/libs/jquery/jquery.js"></script>
	<script
		src="/nopen/frontend/common/libs/jquery-ui/redmond/jquery-ui.js"></script>

	<!-- JS -->

	<script src="/nopen/frontend/equipment-studio/js/mymodel.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/keyboard.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/inspector.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/stencil.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/main.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/equipmentAjax.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/resizing.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/ituHandle.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/supervisorHandle.js"></script>
	<script src="/nopen/frontend/equipment-studio/js/equipmentHandle.js"></script>

	<!-- PLUGINS -->

	<script src="/nopen/frontend/equipment-studio/plugins/open-template.js"></script>
	<script src="/nopen/frontend/equipment-studio/plugins/save-template.js"></script>

	<%@include file="templates/supervisorPanelList.jsp"%>

	<!-- DIALOGS -->

	<input type="text" id="filename" style="display: none" />

	<div id="itu-dialog" title="ITU" style="display: none">
		<iframe id="itu-iframe" src="" scrolling="no"></iframe>
		<!--  File name: <input type="text" id="save-filename" /> -->
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
		//var script = document.createElement( 'script' );
		//script.src = '/nopen/frontend/equipment-studio/plugins/itu-iframe.js';
		//$('#itu-dialog').prepend(script);

		var app = new Rappid;
		Backbone.history.start();

		resizing(app.graph);
		equipmentHandle(app.graph);
		ituHandle(app.paper, app.graph);
        supervisorHandle(app.paper , app.graph);
        
		if (getUrlParameter('template')) {
			var template = getUrlParameter('template');
			openFromURL(template, app.graph);
		}

		//graphHandle(app.graph);
		//paperHandle(app.paper);

		var uuid = joint.util.uuid();

		$('#btn-save').click(function() {
			generateSaveTemplateDialog(app.graph);
		});

		$('#btn-open').click(function() {
			getTemplates(app.graph);
		});		
		

	</script>



</body>
</html>
