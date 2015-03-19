<%@include file="/frontend/template/index-top.jsp"%>

<style>
.list-questions li {
	margin-top:10px;
}
.answer{
	margin-top:5px;
}
</style>

<script type="text/javascript">
	//Variables to control specialization properties	
	var ablePrev = false; //begnning
	var ableNext = true;  //begnning
	
	$(document).ready(function(){
		$(".answer").hide();
		//Previous bottom click
		$('.question').live('click', function(){
			//$(this).parent().children(".answer").slideDown();
			if ( $(this).parent().children(".answer").is( ":hidden" ) ) 
			{
				$(this).parent().children(".answer").slideDown();
			} else {
				$(this).parent().children(".answer").hide();
			}			
		});//End - btn-prev
	});
</script>

<div class="row">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h2>
					<i class="icon-question-sign"></i>Frequently Asked Questions (FAQ)
				</h2>
				<div class="box-icon">
					 <a	href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content">
				
				<ol class="list-questions">
					<li><a class="question" href="#">What is the "Network Topology" functionality?</a> <div class="answer">Coming soon...</div> </li>					 
					<li><a class="question" href="#">What is the "Equipment Studio" functionality?</a> <div class="answer">Coming soon...</div> </li>					  
					<li><a class="question" href="#">What is the "Provisioning" functionality?</a> <div class="answer">Coming soon...</div> </li>
					<li><a class="question" href="#">What is the "Network Advisor" functionality?</a> <div class="answer">Coming soon...</div> </li>
				</ol>
			</div>
		</div>
	</div>
</div>

<%@include file="/frontend/template/index-bottom.jsp"%>