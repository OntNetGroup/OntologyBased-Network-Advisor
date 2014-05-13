<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@include file="../templates/header.jsp"%>

<style>

	.list-questions li
	{
		margin-top:10px;
	}
	.answer
	{
	margin-top:5px;
	}

</style>

<script type="text/javascript">

	//Variables to control specialization properties
	
	var ablePrev = false; //begnning
	var ableNext = true;  //begnning

	$(document).ready(function() {

		$(".answer").hide();

		//Previous bottom click
		$('.question').live('click', function() {		

			//$(this).parent().children(".answer").slideDown();
			if ( $(this).parent().children(".answer").is( ":hidden" ) ) {
				$(this).parent().children(".answer").slideDown();
			  } else {
				  $(this).parent().children(".answer").hide();
			  }
			
			
		}); // End - btn-prev
	});


</script>

	<h1>FAQ</h1>

	<div class="row">
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h2>
						<i class="icon-edit"></i>Questions
					</h2>
					<div class="box-icon">
						 <a	href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
					</div>
				</div>
				<div class="box-content">
					
					<ol class="list-questions">
						  <li><a class="question" href="#">Lorem ipsum dolor sit amet?</a> <div class="answer">resposta.....</div> </li>
						  <li><a class="question" href="#">Consectetur adipiscing elit?</a> <div class="answer">resposta.....</div> </li>
						  <li><a class="question" href="#">Integer molestie lorem at massa?</a> <div class="answer">resposta.....</div> </li>
						  <li><a class="question" href="#">Facilisis in pretium nisl aliquet?</a> <div class="answer">resposta.....</div> </li>
						  <li><a class="question" href="#">Nulla volutpat aliquam velit?</a> <div class="answer">resposta.....</div> </li>
						  <li><a class="question" href="#">Faucibus porta lacus fringilla vel?</a> <div class="answer">resposta.....</div> </li>
						  <li><a class="question" href="#">Aenean sit amet erat nunc?</a> <div class="answer">resposta.....</div> </li>
						  <li><a class="question" href="#">Eget porttitor lorem?</a> <div class="answer">resposta.....</div> </li>
					</ol>
				</div>
			</div>
		</div>
		<!--/col-->

	</div>
	<!--/row-->

<%@include file="../templates/footer.jsp"%>