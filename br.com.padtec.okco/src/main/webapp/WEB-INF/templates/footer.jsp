			</div><!-- end: Content -->
					
		</div><!--/row-->		
		
	</div><!--/container-->
	
	<div class="clearfix"></div>
	
	<footer>
		<p>
			<span style="text-align:left;float:left">&copy; 2014 <a href="#" alt="Nemo">Nemo OKCo</a></span>
			<span class="hidden-phone" style="text-align:right;float:right">Developed by: <a href="#" alt="Fábio Coradini">Fábio Coradini</a> and <a href="#" alt="Fábio Coradini">Pedro Paulo Barcelos</a></span>
		</p>

	</footer>
	
	<script>
		//readjusting the content when the content is short and the footer appears in the middle of the page 
		$(window).bind("load", function() {
			var docHeight = $(document).height();
			var footerPadTop = $("footer").css("padding-top").replace("px", "");
			var footerPadBottom = $("footer").css("padding-bottom").replace("px", "");
			var footerHeight = $("footer").height()
			footerHeight = footerHeight + Number(footerPadTop) + Number(footerPadBottom);
			var navbarInnerHeigth = $("#navbar-inner").height();
			var contentHeigth = $("#content").height();
			var h = navbarInnerHeigth + contentHeigth + footerHeight;
			if(docHeight > h){
				contentHeigth = docHeight - navbarInnerHeigth - footerHeight;
				$('#content').css({'height':contentHeigth});
			}
		}); // End - document ready	
	</script>

</body>
</html>