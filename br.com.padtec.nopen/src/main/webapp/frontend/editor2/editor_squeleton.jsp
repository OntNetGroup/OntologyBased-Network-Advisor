
<!DOCTYPE html>
<html>
<head>
<meta charset="utf8" />

<title>Equipment Studio - Editor</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/core/rappid_api/css/joint.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/core/rappid_api/css/joint.ui.stencil.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/core/rappid_api/css/joint.ui.halo.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/core/rappid_api/css/joint.ui.selectionView.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/core/rappid_api/css/joint.ui.paperScroller.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/core/rappid_api/css/joint.ui.snaplines.css" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/frontend/editor/css/style.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/frontend/editor/css/theme-dark.css" />

</head>
<body>
	<script type="text/javascript">
		//To be used in javascript files
		contextPath = "${pageContext.request.contextPath}";
	</script>
	
	<%@include file="templates/toolbar.jsp"%>
	<%@include file="templates/center.jsp"%>
	
	<script
		src="${pageContext.request.contextPath}/core/rappid_api/js/joint.js"></script>
		
	<script
		src="${pageContext.request.contextPath}/core/rappid_api/js/joint.all.js"></script>

	<script
		src="${pageContext.request.contextPath}/stencils/transport_function/js/transport_function.js"></script>

	<script
		src="${pageContext.request.contextPath}/frontend/editor/js/keyboard.js"></script>

	<!-- <script src="${pageContext.request.contextPath}/resources/js/pages/LightLinkView.js"></script>  -->
	<script
		src="${pageContext.request.contextPath}/frontend/editor/js/ElementInspector.js"></script>
	<script
		src="${pageContext.request.contextPath}/frontend/editor/js/main.js"></script>
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
