Util = {
	/* 
	 * Variável indicando se a ação sendo executada é a de adição de um transport function.
	 * Ao adicionar um TF sobre um container, é necessário chamar a função 'embed' do container.
	 * Com isso, a aplicação chamaria a função de inserir um TF numa camada e depois a de mudar a camada do TF.
	 * Porém, com esta variável de controle isso não ocorre. 
	*/
	isAddingTransportFunction : false,

	createDtoElement : function(id, name, type) {
		var dtoElement = {
				"id" : id,
				"name" : name,
				"type": type
		};
		
		return dtoElement;
	},
    
    generateAlertDialog: function(alertMsg) {
    	new joint.ui.Dialog({
			type: 'alert',
			width: 600,
			title: 'Alert',
			content: alertMsg
		}).open();
    }
};