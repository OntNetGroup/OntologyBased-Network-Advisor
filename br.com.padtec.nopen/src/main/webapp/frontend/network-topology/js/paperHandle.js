/**
 * Procedure to handle with paper.
 * @param paper
 */
function paperHandle(paper){
	
	// Procedure to open a Equipment Template
	paper.on('cell:pointerdblclick', function(cellView, evt, x, y) {    
	    var equipment = cellView.model.attr('equipment/template');
	    window.open('/nopen/equipment-studio.htm?equipment=' + equipment, '_blank');
	});
	
};