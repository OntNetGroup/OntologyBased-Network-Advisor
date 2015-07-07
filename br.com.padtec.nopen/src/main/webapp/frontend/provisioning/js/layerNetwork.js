// bpmn.Pool without blackbox-label
LayerNetwork = joint.dia.Element.extend({

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