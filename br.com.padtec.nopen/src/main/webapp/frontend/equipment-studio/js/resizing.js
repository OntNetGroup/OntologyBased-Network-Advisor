function resizing(graph){
	
	// some types of the elements need resizing after they are dropped
	graph.on('add', function(cell, collection, opt) {

		if (!opt.stencil) return;
		if(cell.get('type') === 'link') return;
		
		var subType = cell.get('subType');
		
		// configuration of resizing
		var sizeMultiplier = { 	
				'rack': 3,
				'shelf': 1.5,
				'slot': 1.5
		}[subType];

		if (sizeMultiplier) {
			var originalSize = cell.get('size');
			cell.set('size', {
				width: originalSize.width * sizeMultiplier,
				height: originalSize.height * sizeMultiplier
			});
		}

		sizeMultiplier = 0;
	}, this);
	
};