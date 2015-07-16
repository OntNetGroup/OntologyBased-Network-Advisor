nopen.provisioning.Util = {
    
    generateAlertDialog: function(alertMsg) {
    	new joint.ui.Dialog({
			type: 'alert',
			width: 400,
			title: 'Alert',
			content: alertMsg
		}).open();
    }
};