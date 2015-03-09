<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@include file="/frontend/advisor/templates/header.jsp"%>

<style>
.list-questions li {
	margin-top: 10px;
}

.answer {
	margin-top: 5px;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {

	});
</script>

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2><i class="icon-edit"></i>OOPS!</h2>
				<div class="box-icon">
					<a href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				<p>We're sorry, but something went wrong.</p>
				
				<p><a href="welcome"><u>Take me back to the homepage</u></a></p>
			</div>
		</div>
	</div>
	<!--/col-->

</div>
<!--/row-->

<%@include file="/frontend/advisor/templates/footer.jsp"%>