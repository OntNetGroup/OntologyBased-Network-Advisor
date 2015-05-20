joint.shapes.equipment = {};

joint.shapes.equipment.Card = joint.shapes.basic.Generic.extend({

	markup: [
	         '<g class="rotatable">',
	         	'<g class="scalable">',
	         		'<rect class="body"/>',
	         	'</g>',
	    		'<g class="inPort">',
	    			'<circle r="4" />',
	    		'</g>',
	    		'<g class="outPort">',
	    			'<rect width="7" height="7" />',
	    		'</g>',
	         	'<text/>',
	         '</g>',
	].join(''),

    defaults: joint.util.deepSupplement({
    
        type: 'equipment.Card',
        attrs: {
            '.body': { fill: '#FFFFFF', stroke: 'black', width: 100, height: 60 },
            'text': { 'font-size': 14, text: '', 'ref-x': .5, 'ref-y': .5, ref: '.body', 'y-alignment': 'middle', 'x-alignment': 'middle', fill: 'black', 'font-family': 'Arial, helvetica, sans-serif' },
	        '.inPort': {
	            'ref': '.body',
	            'ref-x': .5,
	            fill: 'none',
	        },
	        '.outPort': {
	        	'ref': '.body',
	        	'ref-y': .8,
	        	'ref-x': .15,
	        	fill: 'none',
	        },
	        '.inPort circle': {
	        	magnet: true,
	        },
	        '.outPort rect': {
	        	magnet: true,
	        },
        }
        
    }, joint.shapes.basic.Generic.prototype.defaults)
    
});
joint.shapes.equipment.CardView = joint.shapes.basic.GenericView; 

