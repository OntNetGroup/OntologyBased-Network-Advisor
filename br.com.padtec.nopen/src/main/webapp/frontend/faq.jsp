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
				
				<!-- Write the content of the page here... -->
				<ol class="list-questions">
					<li><a class="question" href="#">What is Network Topology?</a> <div class="answer">Should be more of a description here...</div> </li>					 
					<li><a class="question" href="#">What is Equipment Studio?</a> <div class="answer">Should be more of a description here...</div> </li>					  
					<li><a class="question" href="#">What is Provisioning?</a> <div class="answer">Should be more of a description here...</div> </li>		
					<li><a class="question" href="#">What is Network Advisor?</a> <div class="answer">Should be more of a description here...</div> </li>			
				</ol>
				
			</div>
		</div>
	</div>
</div>

<%@include file="/frontend/template/index-bottom.jsp"%>