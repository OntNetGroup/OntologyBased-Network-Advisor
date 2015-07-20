nopen.provisioning.Util = Backbone.Model.extend({
	
	initialize : function() {
		
	},
	
	//generate events to list
	prepareList : function(type) {
		$(type + ' #expList').find('li:has(ul)').click( function(event) {
			if (this == event.target) {
				$(this).toggleClass('expanded');
				$(this).children('ul').toggle('medium');
			}
			return false;
		})
		.addClass('collapsed').children('ul').hide();

		//Create the toggle 
		var toggle = false;
		$('#listToggle').unbind('click').click(function(){
			if(toggle == false){
				$('.collapsed').addClass('expanded');
				console.log($('.collapsed').children());
				$('.collapsed').children().show('medium');
				$('#listToggle').text("Collapse All");
				toggle = true;
			}else{
				$('.collapsed').removeClass('expanded');
				console.log($('.collapsed').children());
				$('.collapsed').children().hide('medium');
				$('#listToggle').text("Expand All");
				toggle = false;          
			}
		});
	},


});