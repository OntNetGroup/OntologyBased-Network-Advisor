joint.shapes.topology = {}

joint.shapes.topology.Node = joint.shapes.basic.Circle.extend({
	
	markup: '<g class="rotatable"><g class="scalable"><circle/></g><text/></g>',
	
	defaults: joint.util.deepSupplement({

		type: 'topology.Node',
		subtype: 'Node',

		size: { width: 5, height: 5 },
	    attrs: {
	    	circle: { fill: '#00c6ff' },
	    	text: { text: 'NODE', 'font-size': 8, display: '', 'ref-y': .5  },
	    	equipment: { id: '', name: ''},
	    },

	}, joint.shapes.basic.Circle.prototype.defaults)

	
});