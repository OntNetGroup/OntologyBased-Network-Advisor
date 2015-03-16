MyPool = joint.dia.Element.extend({

    markup: ['<g class="rotatable">',
             '<g class="scalable"><rect class="body"/></g>',
             '<svg overflow="hidden" class="blackbox-wrap"><text class="blackbox-label"/></svg>',
             '<rect class="header"/><text class="label"/>',
             '<g class="lanes"/>',
             '</g>'].join(''),

    laneMarkup: '<g class="lane"><rect class="lane-body"/><rect class="lane-header"/><text class="lane-label"/></g>',

    defaults: joint.util.deepSupplement({

        type: 'bpmn.Pool',
        size: {
            width: 600,
            height: 300
        },
        attrs: {
            '.body': {
                fill: '#ffffff',
                stroke: '#000000',
                width: 500,
                height: 200,
                'pointer-events': 'stroke'
            },
            '.header': {
                fill:'#ffffff',
                stroke: '#000000',
                width: 20,
                ref: '.body',
                'ref-height': 1,
                'pointer-events': 'visiblePainted'
            },
            '.label': {
                transform: 'rotate(-90)' ,
                ref: '.header',
                'ref-x': 10,
                'ref-y': .5,
                'font-family': 'Arial',
                'font-size': 14,
                'x-alignment': 'middle',
                'text-anchor': 'middle'
            },
            '.lane-body': {
                fill:'#ffffff',
                stroke: '#000000',
                'pointer-events': 'stroke'
            },
            '.lane-header': {
                fill:'#ffffff',
                stroke: '#000000',
                'pointer-events': 'visiblePainted'
            },
            '.lane-label': {
                transform: 'rotate(-90)',
                'text-anchor': 'middle',
                'font-family': 'Arial',
                'font-size': 13
            },
            '.blackbox-wrap': {
                ref: '.body',
                'ref-width': 1,
                'ref-height': 1
            },
            '.blackbox-label': {
                text: 'Black Box',
                dx: '50%',
                dy: '50%',
                'text-anchor': 'middle',
                transform: 'translate(0,-7)'
            }
        }

    }, joint.dia.Element.prototype.defaults)
});