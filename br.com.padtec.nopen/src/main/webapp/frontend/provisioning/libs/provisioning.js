
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
	        text: { text: '', fill: '#BABABA', 'font-size': 14, stroke: '#000000', 'stroke-width': 0 }
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
            width: 500,
            height: 200
        },
        attrs: {
        	'.': { magnet: false },
        	'.header': {
                fill:'#5799DA',
            },
            '.blackbox-wrap': {
            	text: '',
            }
        }

    }, joint.shapes.bpmn.Pool.prototype.defaults)
});
joint.shapes.provisioning.LayerView = joint.shapes.bpmn.PoolView;