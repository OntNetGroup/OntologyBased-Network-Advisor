
joint.shapes.provisioning = {}

joint.shapes.provisioning.AccessGroup = joint.shapes.basic.Circle.extend({
	
	markup: '<g class="rotatable"><g class="scalable"><circle/></g><text/></g>',
	
	defaults: joint.util.deepSupplement({

		type: 'provisioning.AccessGroup',
		subtype: 'Access_Group',

		size: { width: 30, height: 30 },
			
		attrs: {
			'.': { magnet: true },
			'.rotatable': {
            	display: 'none',
            }
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
	        text: { text: '', fill: '#BABABA', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 },
	        '.rotatable': {
            	display: 'none',
            }
	    }
		
	}, joint.shapes.basic.Circle.prototype.defaults)

});
joint.shapes.provisioning.SubnetworkView = joint.dia.ElementView;

joint.shapes.provisioning.Layer = joint.shapes.bpmn.Pool.extend({

    defaults: joint.util.deepSupplement({

        type: 'provisioning.Layer',
        subtype: 'Layer_Network',
        technology: '',
        uppermostLayer: '',
        size: {
            width: 520,
            height: 220
        },
        attrs: {
        	'.': { magnet: false },
        	'.header': {
                fill:'#5799DA',
            },
            '.blackbox-wrap': {
            	text: '',
            },
            '.rotatable': {
            	display: 'none',
            }
        }

    }, joint.shapes.bpmn.Pool.prototype.defaults)
});
joint.shapes.provisioning.LayerView = joint.shapes.bpmn.PoolView;

joint.shapes.provisioning.Matrix = joint.shapes.devs.Model.extend({

    defaults: joint.util.deepSupplement({

        type: 'provisioning.Matrix',
        size: { width: 200, height: 300 },
        attrs: {
            '.body': { fill: '#808080' },
            '.label': { text: 'Matrix', fill: '#FFFFFF' },
            '.inPorts .port-body': { fill: 'PaleGreen' },
            '.outPorts .port-body': { fill: 'Tomato' },
        }

    }, joint.shapes.devs.Model.prototype.defaults)
});
joint.shapes.provisioning.MatrixView = joint.shapes.devs.ModelView;
