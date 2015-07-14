
joint.shapes.provisioning = {}

joint.shapes.provisioning.Equipment = joint.shapes.basic.Circle.extend({
	
	initialize: function() {
		
	},
	
});
joint.shapes.provisioning.EquipmentView = joint.dia.ElementView;

joint.shapes.provisioning.Subnetwork = joint.shapes.basic.Circle;
joint.shapes.provisioning.SubnetworkView = joint.dia.ElementView.extend({

    pointerdown: function(evt, x, y) {
        var position = this.model.get('position');
        var size = this.model.get('size');
        var center = g.rect(position.x, position.y, size.width, size.height).center();
        var intersection = constraint.intersectionWithLineFromCenterToPoint(center);
        joint.dia.ElementView.prototype.pointerdown.apply(this, [evt, intersection.x, intersection.y]);
    },
    pointermove: function(evt, x, y) {
        var intersection = constraint.intersectionWithLineFromCenterToPoint(g.point(x, y));
        joint.dia.ElementView.prototype.pointermove.apply(this, [evt, intersection.x, intersection.y]);
    }
    
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
            '.body': {
                fill: '#ffffff',
                stroke: '#000000',
                width: 500,
                height: 200,
            },
            '.header': {
                fill:'#ffffff',
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