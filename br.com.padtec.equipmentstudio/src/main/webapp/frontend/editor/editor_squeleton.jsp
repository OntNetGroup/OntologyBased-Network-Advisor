<!DOCTYPE html>
<html>
<head>
<meta charset="utf8" />

<title>Equipment Studio - Editor</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/rappid_api/joint.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/rappid_api/joint.ui.stencil.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/rappid_api/joint.ui.halo.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/rappid_api/joint.ui.selectionView.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/rappid_api/joint.ui.paperScroller.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/rappid_api/joint.ui.snaplines.css" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/pages/style.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/pages/theme-dark.css" />

</head>
<body>
	<script type="text/javascript">
		//To be used in javascript files
		contextPath = "${pageContext.request.contextPath}";
	</script>
	
	<%@include file="templates/toolbar.jsp"%>
	<%@include file="templates/center.jsp"%>

	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.shapes.devs.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.ui.halo.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.ui.selectionView.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.ui.clipboard.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.ui.stencil.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.ui.paperScroller.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.format.svg.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.dia.command.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.dia.validator.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.layout.ForceDirected.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_api/joint.ui.snaplines.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/js/rappid_custom/CustomShape.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/js/pages/keyboard.js"></script>

	<!-- <script src="${pageContext.request.contextPath}/resources/js/pages/LightLinkView.js"></script>  -->
	<script
		src="${pageContext.request.contextPath}/resources/js/pages/ElementInspector.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/pages/main.js"></script>
	<!--[if IE 9]>
	    <script src="${pageContext.request.contextPath}/resources/js/pages/base64.js"></script>
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
</body>
</html>
