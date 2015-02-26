
function CustomShape(){
	
}

CustomShape.getStencilForTransportFunctions = function() {
	var ttf = new joint.shapes.basic.Image({
		position: { x: 10, y: 10 },
	    size: { width: 50, height: 50 },
		attrs: {
			image: { width: 50, height: 50, 'xlink:href': contextPath+'/resources/images/shapes/TFs/TTF_AZUL.png' },
			text: { text: 'TTF', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }
		}
	});

	var af = new joint.shapes.basic.Image({
		position: { x: 10, y: 90 },
	    size: { width: 50, height: 50 },
		attrs: {
	        image: { width: 50, height: 50, 'xlink:href': contextPath+'/resources/images/shapes/TFs/AF_AZUL.png' },
	        text: { text: 'AF', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }
	    }
	});

	return [ttf,af];
};

CustomShape.getStencilForEquipmentConstructs = function() {
	var rack = new joint.shapes.basic.Image({
		position: { x: 10, y: 10 },
        size: { width: 50, height: 50 },
		attrs: {
			image: { width: 50, height: 50, 'xlink:href': contextPath+'/resources/images/shapes/equipments/rack.png' },
			text: { text: 'Rack', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }
		}
	});
	
	var shelf = new joint.shapes.basic.Image({
		position: { x: 70, y: 10 },
        size: { width: 50, height: 50 },
		attrs: {
	        image: { width: 50, height: 50, 'xlink:href': contextPath+'/resources/images/shapes/equipments/shelf.png' },
	        text: { text: 'Shelf', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }
	    }
	});

	var slot = new joint.shapes.basic.Image({
		position: { x: 130, y: 10 },
        size: { width: 50, height: 50 },
		attrs: {
	        image: { width: 50, height: 50, 'xlink:href': contextPath+'/resources/images/shapes/equipments/slot.png' },
	        text: { text: 'Slot', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }
	    }
	});
	
	var subslot = new joint.shapes.basic.Image({
		position: { x: 190, y: 10 },
        size: { width: 50, height: 50 },
		attrs: {
	        image: { width: 50, height: 50, 'xlink:href': contextPath+'/resources/images/shapes/equipments/subslot.png' },
	        text: { text: 'Subslot', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }
	    }
	});
	
	var card = new joint.shapes.basic.Image({
		position: { x: 250, y: 10 },
        size: { width: 50, height: 50 },
		attrs: {
	        image: { width: 50, height: 50, 'xlink:href': contextPath+'/resources/images/shapes/equipments/card.png' },
	        text: { text: 'Card', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }
	    }
	});
	
	return [rack,shelf,slot,subslot,card];
};