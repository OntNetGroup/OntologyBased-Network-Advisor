<script type="text/javascript">
$(document).ready(function() {
	$("#maskforloading").hide();
	$(".btnload").click(function(){
		loading();
	});	
	$("#dialog").dialog({
	   autoOpen: false,
	   modal: true,
	   buttons : {
	        "Confirm" : function() {  
	            window.location.href = "home.htm";
	        },
	        "Cancel" : function() {
	          $(this).dialog("close");
	        }
	      }
	    });
	$(".callConfirm").on("click", function(e) {		
	    e.preventDefault();
	    $("#dialog").dialog("open");
	});	
}); 
</script>
	
<!-- Nao remova o div mask, pois ele e necessario para preencher toda a janela -->
<div id="mask"></div>

<!-- Na remova o div mask forloading, pois ele e necessario para preencher toda a janela -->
<div id="maskforloading">
	<img src="/nopen/frontend/template/img/loading.gif" height="100px">
</div>

<div id="dialog" title="Confirmation Required">
  All loaded information will be lost. Do you want to continue?
</div>

<!-- start: Header -->
<div class="navbar">
	<div class="navbar-inner">
		<div class="container">
			<button class="navbar-toggle" type="button" data-toggle="collapse"
				data-target=".sidebar-nav.nav-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a id="main-menu-toggle" class="hidden-xs open"><i class="icon-reorder"></i></a>
			<div class="row">
				<a class="navbar-brand col-lg-2 col-sm-1 col-xs-12" href="home.htm"> <span><img style="margin-top:-9px" src="/nopen/frontend/common/img/logo.png"/></span> </a>
			</div>
			<!-- start: Header Menu -->
			<div class="nav-no-collapse header-nav"></div>
			<!-- end: Header Menu -->
		</div>
	</div>
</div>
<!-- end: Header -->	