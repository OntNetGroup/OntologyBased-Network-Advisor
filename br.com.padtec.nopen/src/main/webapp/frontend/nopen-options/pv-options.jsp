<%@include file="/frontend/template/index-top.jsp"%>

<script src="frontend/template/js/jquery.nestable.min.js"></script>

<!-- JS -->
<script src="/nopen/frontend/nopen-options/js/pv-options.js"></script>

<!-- CSS -->
<link rel="stylesheet" type="text/css" href="/nopen/frontend/nopen-options/css/options.css" />
	
<div class="row">

	<div id="layer-error" class="box-content alerts" style="display:none;">
		<div class="alert alert-danger">
			<button type="button" class="close" data-dismiss="alert">×</button>
			<strong>Not allowed! </strong> In order to add a new layer, select first a Technology.
		</div>
	</div>

	<div id="service-error" class="box-content alerts" style="display:none;">
		<div class="alert alert-danger">
			<button type="button" class="close" data-dismiss="alert">×</button>
			<strong>Not allowed!</strong> In order to add a new Service, please select first a layer
		</div>
	</div>
	
	<!-- ======================================================================= -->	
	<div class="col-sm-12">	
		<div class="box"> 			
			<div class="box-header" >
				<h2><i class="icon-laptop"></i>Provisioning</h2>
				<div class="box-icon">
					<a href="provisioning.htm#" class="btn-tech-plus" title="New Provisioning"><i class="icon-plus"></i></a>
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>													
				</div>
			</div>
			
			<div class="box-content clearfix" style="">
				<div class="col-sm-12">
					
					<a class="btn" id="new" title="New Provisioning" href="provisioning.htm"><i class="icon-plus"></i><span class="name">New Provisioning </span></a>
					
					<div class="btn-toolbar">
						<script type="text/javascript">
							getProvisioning();
						</script>
					</div>
				</div>
			</div>
	    </div>    
	</div>
	
</div>
	<!-- ======================================================================= -->

<%@include file="/frontend/template/index-bottom.jsp"%>

