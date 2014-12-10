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
		$(document).ready(function(){
			adjustContent();
		}); // End - document ready	
		
		function adjustContent(){
			var docHeight = $(document).height();
			//alert("docHeight: "+docHeight);
			var footerPadTop = $("footer").css("padding-top").replace("px", "");
			//alert("footerPadTop: "+footerPadTop);
			var footerPadBottom = $("footer").css("padding-bottom").replace("px", "");
			//alert("footerPadBottom: "+footerPadBottom);
			var footerHeight = $("footer").height()
			//alert("footerHeight: "+footerHeight);
			footerHeight = footerHeight + Number(footerPadTop) + Number(footerPadBottom);
			//alert("footerHeight: "+footerHeight);
			var navbarInnerHeigth = $("#navbar-inner").height();
			//alert("navbarInnerHeigth: "+navbarInnerHeigth);
			
			var contentHeigth = $("#content").height();
			//alert("contentHeigth: "+contentHeigth);
			
			var h = navbarInnerHeigth + contentHeigth + footerHeight;
			//alert("h: "+h);
			if(docHeight > h){
				contentHeigth = docHeight - navbarInnerHeigth - footerHeight;
				//alert("contentHeigth: "+contentHeigth);
				$('#content').css({'height':contentHeigth});
			}
		}
	</script>

</body>
</html>