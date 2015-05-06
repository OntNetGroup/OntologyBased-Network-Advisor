// definicao de uma forma geometrica contendo portas de input e output
MyModel = joint.shapes.basic.Generic.extend(_.extend({}, joint.shapes.basic.PortsModelInterface, {

	// definicao do svg do elemento
    markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><text class="label"/><g class="inPorts"/><g class="outPorts"/></g>',
	// definicao do svg das portas
    portMarkup: '<g class="port<%= id %>"><path class="port-body"/><text class="port-label"/></g>',

    defaults: joint.util.deepSupplement({

        type: 'devs.Model',
		// atributo que sera referencia para restricoes de conexao e 'containment'
		subType: '',
        size: { width: 1, height: 1 },
        
        inPorts: [],
        outPorts: [],

        attrs: {
            '.': { magnet: false },
            '.body': {
                width: 150, height: 250,
                stroke: '#000000'
            },
            '.port-body': {
                magnet: true,
                stroke: '#000000'
            },
            text: {
                'pointer-events': 'none'
            },
            '.inPorts .port-label': { x: 15, dy: 4, fill: '#000000' },
            '.outPorts .port-label':{ x: 15, dy: 4, fill: '#000000' },
//			'.inPorts .port-body':{ 'ref-y': -5 },
			'.outPorts .port-body':{ 'ref-x': 0 }
        }

    }, joint.shapes.basic.Generic.prototype.defaults),

	// funcao obrigatoria em classes que contenham portas
    getPortAttrs: function(portName, index, total, selector, type) {

        var attrs = {};

        var portClass = 'port' + index;
        var portSelector = selector + '>.' + portClass;
        var portLabelSelector = portSelector + '>.port-label';
        var portBodySelector = portSelector + '>.port-body';

        attrs[portLabelSelector] = { text: portName };
        attrs[portBodySelector] = { port: { id: portName || _.uniqueId(type) , type: type } };
        attrs[portSelector] = { ref: '.body', 'ref-x': (index + 0.35) * (1 / total) };

        if (selector === '.outPorts') { attrs[portSelector]['ref-dy'] = 0; }

        return attrs;
    }
}));