
// decide whether the elements should be embedded or connected
function embedOrConnect(parent, child, graph) {
	
	var parentType = parent.get('type');
	var childType = child.get('type');
	
	if(parentType === 'bpmn.Pool' && childType === 'basic.Path') { // parent is a layer and child is an ITU element
		parent.embed(child);
	} else { // parent is an ITU element and child is a port
		if(parentType === 'basic.Path' && (childType === 'basic.Rect' || childType === 'basic.Circle')) {

			var newLink = new joint.dia.Link({	source: {id: parent.id}, target: {id: child.id}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
			graph.addCell(newLink);
			
			// Move the port a bit up (in port) or down (out port)
			if(childType === 'basic.Rect') child.translate(0, -70);
			else child.translate(0, 100);
		}
	}
};

// When a cell is added on another one, it should be embedded or connected (in case of it being a port)
// this function doesn't work if placed in main.js
function graphHandle(graph){
	
	graph.on('add', function(cell) {
		
		if(cell.get('type') === 'link') return;
		
		var position = cell.get('position');
		var size = cell.get('size');
        var area = g.rect(position.x, position.y, size.width, size.height);
		
		var parent;
		// get all elements below the added one
		_.each(graph.getElements(), function(e) {

			var position = e.get('position');
            var size = e.get('size');
			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
				// save the most above among the elements below
				parent = e;
			}
        });
		
		if(parent) {		
			embedOrConnect(parent, cell, graph);
		}
	});	
};