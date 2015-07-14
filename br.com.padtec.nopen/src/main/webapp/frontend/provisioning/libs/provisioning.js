
joint.shapes.provisioning = {}

joint.shapes.provisioning.AccessGroup = joint.shapes.basic.Circle.extend({
	
	markup: '<g class="rotatable"><g class="scalable"><circle/></g><text/></g>',
	
	defaults: joint.util.deepSupplement({

		type: 'provisioning.AccessGroup',
		subtype: 'Access_Group',

		size: { width: 30, height: 30 },
			
		attrs: {
			'.': { magnet: true }
		},
		
		id : '',
		equipment : {},

	}, joint.shapes.basic.Circle.prototype.defaults)

	
});
joint.shapes.provisioning.EquipmentView = joint.dia.ElementView;

joint.shapes.provisioning.Subnetwork = joint.shapes.basic.Circle.extend({
	
	defaults: joint.util.deepSupplement({

		type: 'provisioning.Subnetwork',
		subtype: 'Subnetwork',

		size: { width: 400, height: 150 },
	    attrs: {
			'.': { magnet: false },
	        text: { text: '', fill: '#000000', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 }
	    }
		
	}, joint.shapes.basic.Circle.prototype.defaults)

});

joint.shapes.provisioning.Layer = joint.dia.Element.extend({

	markup: ['<g class="rotatable">',
             '<g class="scalable"><rect class="body"/></g>',
             '<svg overflow="hidden" class="blackbox-wrap"/>',
             '<rect class="header"/><text class="label"/>',
             '<g class="lanes"/>',
             '</g>'].join(''),

    laneMarkup: '<g class="lane"><rect class="lane-body"/><rect class="lane-header"/><text class="lane-label"/></g>',

    defaults: joint.util.deepSupplement({

        type: 'bpmn.Pool',
        subtype: 'Layer_Network',
        technology: '',
        uppermostLayer: '',
        size: {
            width: 500,
            height: 200
        },
        attrs: {
        	'.': { magnet: false },
            '.body': {
                fill: '#ffffff',
                stroke: '#000000',
                width: 500,
                height: 200,
            },
            '.header': {
                fill:'#5799DA',
                stroke: '#000000',
                width: 20,
                ref: '.body',
                'ref-height': 1,
            },
            '.label': {
                transform: 'rotate(-90)' ,
                ref: '.header',
                'ref-x': 10,
                'ref-y': .5,
                'font-family': 'Arial',
                'font-size': 11,
                'x-alignment': 'middle',
                'text-anchor': 'middle'
            },
            '.lane-body': {
                fill:'#ffffff',
                stroke: '#000000',
            },
            '.lane-header': {
                fill:'#ffffff',
                stroke: '#000000',
            },
            '.lane-label': {
                transform: 'rotate(-90)',
                'text-anchor': 'middle',
                'font-family': 'Arial',
                'font-size': 10
            },
            '.blackbox-wrap': {
                ref: '.body',
                'ref-width': 1,
                'ref-height': 1
            }
        }

    }, joint.dia.Element.prototype.defaults)
});