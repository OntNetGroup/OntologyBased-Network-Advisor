<%@include file="/frontend/template/index-top.jsp"%>

<script src="frontend/template/js/jquery.nestable.min.js"></script>


<script src="/nopen/frontend/nopen-options/js/options.js"></script>

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
				<h2><i class="icon-laptop"></i>Network Topology</h2>
				<div class="box-icon">
					<a href="network-topology.htm#" class="btn-tech-plus" title="New Topology"><i class="icon-plus"></i></a>
					<a href="ui-nestable-list.html#" class="btn-minimize"><i class="icon-chevron-up"></i></a>													
				</div>
			</div>
			
			<div class="box-content clearfix" style="">
				<div class="col-sm-12">
					
					<a class="btn" id="new" title="New Topology" href="network-topology.htm"><i class="icon-plus"></i><span class="name">New Topology </span></a>
					
					<div class="btn-toolbar">
						<script type="text/javascript">
							getTopologies();
						</script>
					</div>
				</div>
			</div>
	    </div>    
	</div>
	
</div>
	<!-- ======================================================================= -->

<%@include file="/frontend/template/index-bottom.jsp"%>

