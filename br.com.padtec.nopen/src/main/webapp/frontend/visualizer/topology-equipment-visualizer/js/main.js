var Rappid = Backbone.Router.extend({

	routes: {
		'*path': 'home'
	},

	initialize: function(options) {

		this.options = options || {};
	},

	home: function() {

		this.initializeEditor();
	},

	initializeEditor: function() {
		$('.inspector-container').hide();
		this.inspectorClosedGroups = {};

		this.initializePaper();
//		this.initializeStencil();
		this.initializeSelection();
		this.initializeHaloAndInspector();
//		this.initializeNavigator();
		this.initializeClipboard();
		this.initializeCommandManager();
		this.initializeToolbar();
		// Intentionally commented out. See the `initializeValidator()` method for reasons.
		// Uncomment for demo purposes.
		this.initializeValidator();
		// Commented out by default. You need to run `node channelHub.js` in order to make
		// channels working. See the documentation to the joint.com.Channel plugin for details.
		//this.initializeChannel('ws://jointjs.com:4141');
		if (this.options.channelUrl) {
			this.initializeChannel(this.options.channelUrl);
		}
	},

	// Create a graph, paper and wrap the paper in a PaperScroller.
	initializePaper: function() {

		this.graph = new joint.dia.Graph;

		this.graph.on('add', function(cell, collection, opt) {
			if (opt.stencil) {
				if(cell.get('subType') === 'Card'){
					this.createInspector(cell);
				}				
				this.commandManager.stopListening();
				this.inspector.updateCell();
				this.commandManager.listen();
				this.inspector.$('[data-attribute]:first').focus();
			}
		}, this);

		this.paper = new joint.dia.Paper({
			//el: $('#paper-reparenting'),
			width: 1000,
			height: 1000,
			gridSize: 10,
			perpendicularLinks: true,
			model: this.graph,
			// RF: Permitir que nós contenham outros nós
//			embeddingMode: true,
			// RF: Ao selecionar uma porta, destacar portas disponíveis para conexão com aquela
			markAvailable: true,
			// RF: Inserir 'snap link' às conexões
			snapLinks: { radius: 75 },
			defaultLink: new joint.dia.Link({
				attrs: {
					// @TODO: scale(0) fails in Firefox
					'.marker-source': { d: 'M 10 0 L 0 5 L 10 10 z', transform: 'scale(0.001)' },
					'.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' },
					'.connection': {
						stroke: 'black'
							// filter: { name: 'dropShadow', args: { dx: 1, dy: 1, blur: 2 } }
					}
				}
			})
		});

		this.paperScroller = new joint.ui.PaperScroller({
			autoResizePaper: true,
			padding: 5,
			paper: this.paper
		});

		this.paperScroller.$el.appendTo('.paper-container');

		this.paperScroller.center();

		this.graph.on('add', this.initializeLinkTooltips, this);

		$('.paper-scroller').on('mousewheel DOMMouseScroll', _.bind(function(evt) {

			if (_.contains(KeyboardJS.activeKeys(), 'alt')) {
				evt.preventDefault();
				var delta = Math.max(-1, Math.min(1, (evt.originalEvent.wheelDelta || -evt.originalEvent.detail)));
				var offset = this.paperScroller.$el.offset();
				var o = this.paperScroller.toLocalPoint(evt.pageX - offset.left, evt.pageY - offset.top);
				this.paperScroller.zoom(delta / 10, { min: 0.2, max: 5, ox: o.x, oy: o.y });
			}

		}, this));

		this.snapLines = new joint.ui.Snaplines({ paper: this.paper });
	},


	initializeLinkTooltips: function(cell) {

		if (cell instanceof joint.dia.Link) {
//			var linkView = this.paper.findViewByModel(cell);
//			new joint.ui.Tooltip({
//			className: 'tooltip small',
//			target: linkView.$('.tool-options'),
//			content: 'Click to open Inspector for this link',
//			left: linkView.$('.tool-options'),
//			direction: 'left'

//			});
		}
	},

//	Create and populate stencil.
	initializeStencil: function() {

		this.stencil = new joint.ui.Stencil({
			graph: this.graph,
			paper: this.paper,
			width: 220,
			groups: Stencil.groups,
//			search: {
//			'*': ['subType','attrs/text/text','attrs/.label/text'],
//			'org.Member': ['attrs/.rank/text','attrs/.name/text']
//			}
		});

		$('.stencil-container').append(this.stencil.render().el);	

		this.stencil.$el.on('contextmenu', function(evt) { evt.preventDefault(); });
		$('.stencil-paper-drag').on('contextmenu', function(evt) { evt.preventDefault(); });

		var layoutOptions = {
				columnWidth: this.stencil.options.width / 2 - 10,
				columns: 2,
				rowHeight: 100,
				resizeToFit:false,
				dy: 10,
				dx: 10
		};

		_.each(Stencil.groups, function(group, name) {

			this.stencil.load(Stencil.shapes[name], name);
			joint.layout.GridLayout.layout(this.stencil.getGraph(name), layoutOptions);
			this.stencil.getPaper(name).fitToContent(5, 5, 10);

		}, this);

		this.stencil.on('filter', function(graph) {
			joint.layout.GridLayout.layout(graph, layoutOptions);
		});

		$('.stencil-container .btn-expand').on('click', _.bind(this.stencil.openGroups, this.stencil));
		$('.stencil-container .btn-collapse').on('click', _.bind(this.stencil.closeGroups, this.stencil));

		this.initializeStencilTooltips();
	},

	initializeStencilTooltips: function() {

		// Create tooltips for all the shapes in stencil.
		_.each(this.stencil.graphs, function(graph) {

			graph.get('cells').each(function(cell) {

				new joint.ui.Tooltip({
					target: '.stencil [model-id="' + cell.id + '"]',
					content: cell.get('subType').split('.').join(' '),
					left: '.stencil',
					direction: 'left'
				});
			});
		});
	},


	initializeSelection: function() {

		this.selection = new Backbone.Collection;
		this.selectionView = new joint.ui.SelectionView({ paper: this.paper, graph: this.graph, model: this.selection });
		this.selectionView.removeHandle('rotate');
		this.selectionView.removeHandle('remove');

		// Initiate selecting when the user grabs the blank area of the paper while the Shift key is pressed.
		// Otherwise, initiate paper pan.
		this.paper.on('blank:pointerdown', function(evt, x, y) {

			if (_.contains(KeyboardJS.activeKeys(), 'shift')) {
				this.selectionView.startSelecting(evt, x, y);
			} else {
				this.selectionView.cancelSelection();
				this.paperScroller.startPanning(evt, x, y);
			}
		}, this);

		this.paper.on('cell:pointerdown', function(cellView, evt) {
			// Select an element if CTRL/Meta key is pressed while the element is clicked.
			if ((evt.ctrlKey || evt.metaKey) && !(cellView.model instanceof joint.dia.Link)) {
				this.selection.add(cellView.model);
				this.selectionView.createSelectionBox(cellView);
			}
		}, this);

		this.selectionView.on('selection-box:pointerdown', function(evt) {
			// Unselect an element if the CTRL/Meta key is pressed while a selected element is clicked.
			if (evt.ctrlKey || evt.metaKey) {
				var cell = this.selection.get($(evt.target).data('model'));
				this.selection.reset(this.selection.without(cell));
				this.selectionView.destroySelectionBox(this.paper.findViewByModel(cell));
			}
		}, this);

		// Disable context menu inside the paper.
		// This prevents from context menu being shown when selecting individual elements with Ctrl in OS X.
		this.paper.el.oncontextmenu = function(evt) { evt.preventDefault(); };

		KeyboardJS.on('delete, backspace', _.bind(function(evt, keys) {

//			if (!$.contains(evt.target, this.paper.el)) {
//			// remove selected elements from the paper only if the target is the paper
//			return;
//			}

//			this.commandManager.initBatchCommand();
//			this.selection.invoke('remove');
//			this.commandManager.storeBatchCommand();
//			this.selectionView.cancelSelection();

//			// Prevent Backspace from navigating one page back (happens in FF).
//			if (_.contains(keys, 'backspace') && !$(evt.target).is("input, textarea")) {

//			evt.preventDefault();
//			}

		}, this));
	},

//	eval("createInspector: function(cellView) {" + dateFn + "},"),

	createInspector: function(cellView,opt) {


		var cell = cellView.model || cellView;
		if (!this.inspector || this.inspector.options.cell !== cell) {
//			if (this.inspector) {this.inspectorClosedGroups[this.inspector.options.cell.id] = _.map(app.inspector.$('.group.closed'), function(g) {return $(g).attr('data-name');				});						this.inspector.updateCell();this.inspector.remove();			}
//			if(cell.get('subType') === "Card"){var inspectorDefs = InspectorDefs[cell.get('subType')];}else{return;}
			   if (this.inspector) {
		            // Set unsaved changes to the model and clean up the old inspector if there was one.
		            this.inspector.updateCell();
		            this.inspector.remove();
		        }
			   if (opt === undefined){
				   var a = "myproperty: { type: 'range', min: 0, max: 30, defaultValue: 1, group: 'mydata', index: 1 }, attrs: {text: {text: { type: 'textarea', group: 'text', index: 1 },'font-size': { type: 'number', group: 'text', index: 2 }}}";
				   var b = "mydata: { label: 'My Data', index: 1 },text: { label: 'Text', index: 2 }";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+"},groups: {"+b+" },cellView: cellView})");
 
			   };
			   if(opt === "gcc0-tp-grouping"){
				   var a = "directionality: {type: 'select' , options:['sink','source','bidirectional'],group: 'gcc0tp',index: 1 , label: 'Directionality', attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of <br>the termination point. Valid values are<br> sink, source,and bidirectional.<br> This attribute is read-only.'}}}";
				   var c = ",application: {type: 'text' ,group: 'gcc0tp',index: 2 , label: 'Application', attrs: {'input' : {'data-tooltip': 'This attribute indicates the <br>applications transported by the <br>GCC channel. Example applications <br>are ECC(user data channel).<br> Valid values are string.<br>This attribute is read-only.'}}}";
				   var b = "gcc0tp: { label: 'GCC0 tp', index: 1 }";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+c+"},groups:{ "+b+" },cellView: cellView})");
 
			   };
			   if(opt === "gcc12-tp-grouping"){
				   var a = "directionality: {type: 'select' , options:['sink','source','bidirectional'],group: 'gcc12tp',index: 1 , label: 'Directionality', attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality<br> of the termination point. Valid <br>values are sink, source,and bidirectional.<br> This attribute is read-only.'}}},codirectional: {type: 'toggle',group: 'gcc12tp',index: 2 , label: 'Codirectional', attrs: { 'label': {'data-tooltip': 'This attribute specifies<br> the directionality of <br>the GCC12_TP with respect to <br>the associated ODUk_CTP.<br>The value of TRUE means that<br> the sink part <br>of the GCC12_TP terminates the same signal<br> direction as the sink part of the ODUk_CTP.<br>The Source part behaves similarly.<br>This attribute is meaningful only on objects instantiated<br> under ODUk_CTP,<br> and at least	one among ODUk_CTP and the subordinate <br>object has Directionality equal to Bidirectional.<br> This attribute is read-only.'}}},gccaccess: {type: 'select' , options:['gg1','gcc2','gcc1-plus-gcc2'],group: 'gcc12tp',index: 3 , label: 'GCC Access', attrs: { 'label': {'data-tooltip': 'This attribute indicates the GCC access <br>represented	by this entity.<br> Valid values are: <br>1) GCC1 <br>2) GCC2 <br>3) GCC1 + GCC2.<br>This attribute is read-only.'}}},gccpassthrough : {type: 'toggle' ,group: 'gcc12tp',index: 4 , label: 'GCC Pass Through', attrs: { 'label': {'data-tooltip': 'This attribute controls the<br> selected GCC overhead<br> whether it is passed through or modified. Valid<br> 	values are TRUE and FALSE.<br>The value of TRUE means<br> that the GCC overhead shall pass through unmodified<br> from the ODUk CTP input to the ODUk CTP output.<br> Otherwise shall be set to all 0s at the ODUk CTP<br> output after the extraction of the COMMS data. This<br> attribute is not meaningful on objects instantiated<br> under ODUk_TTP, and on objects with Directionality<br> equals to Source.'}}},application: {type: 'text',group: 'gcc12tp',index: 5 , label: 'Application', attrs: {'input' : {'data-tooltip': 'This attribute indicates the applications <br>transported by the GCC channel.<br> Example applications	are ECC,<br> (user data channel).<br> Valid values are string.<br>This attribute is read-only.'}}}";
				   var b = "gcc12tp: { label: 'GCC12 TP', index: 1 ,attrs: {'label':{'data-tooltip': 'This entity represents the function of terminating and/or originating of the GCC1 or GCC2 channels.'}} }";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+"},groups:{ "+b+" },cellView: cellView})");
 
			   };
			   if(opt === "och-client-ctp-grouping"){
				   var a = "adaptativetype: {type: 'number' ,min: '5', max: '15' ,index: 1 ,group: 'ochclientctp', label: 'Adaptative Type', attrs: {'input' : {'data-tooltip': 'This attribute indicates the type of client signal<br> currently supported by the OCh adaptation function.<br> Valid values are integers between 1 and 15, representing:<br>1) CBR_2G5;<br>2) CBR_10G;<br>3) CBR_40G;<br>4) RSn.<br>This attribute is read-only.'}}},sinkadaptactive: {type: 'toggle',index: 2 ,group: 'ochclientctp', label: 'Sink Adapt Active', attrs: {'input' : {'data-tooltip': 'This attribute allows for activation or<br> deactivation the sink adaptation function. The value<br> of TRUE means active. This attribute is read-write.'}}},sourceadaptactive:{type: 'toggle',index: 3 ,group: 'ochclientctp', label: 'Source Adapt Active', attrs: {'input' : {'data-tooltip': 'This attribute allows for activation or deactivation<br> the source adaptation function. The value of TRUE<br> means activate. This attribute is read-write.'}}}, payloadtypeac: {type: 'number', min: '1', max: '2147483647' , index: 4 ,group: 'ochclientctp', label: 'Payload Type AC', attrs: {'input' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}}},directionality: {type: 'select' , options:['sink','source','bidirectional'],index: 5 ,group: 'ochclientctp', label: 'Directionality', attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality<br> of the termination point. Valid <br>values are sink, source,and bidirectional.<br> This attribute is read-only.'}}},operationalstate: {type: 'select' , options:['enabled','disabled'],index: 6 ,group: 'ochclientctp', label: 'Operational State', attrs: { 'label': {'data-tooltip': 'This attribute is generally defined in ITU-T Rec.<br> X.731 and the behaviour description for<br> operationalState in ITU-T Rec. M.3100.<br>Possible Values – Enabled and Disabled. See ITU-T<br> Recs. X.731 and M.3100 for details.<br>Default Value – Actual state of resource at the time<br> the object is created. If there is a period of time<br> during the initialization process where the<br> operational state is unknown, then the resource will<br> be considered disabled until initialization has<br> completed and the state updated accordingly.<br>Constraints to Provisioning – N/A.<br>Effect of Change in Value – See ITU-T Recs. X.731 and M.3100.<br>This attribute is read-only.'}}},currentproblemlist:{type: 'number',min: '0', max: '2147483647' ,index: 7 ,group: 'ochclientctp', label: 'Current Problem List', attrs: {'input' : {'data-tooltip': 'This attribute indicates the actual payload type<br> signal received. This attribute is read-only.'}}},";
				   var b = "ochclientctp: { label: 'OCH Client CTP', index: 1}";
				   eval("this.inspector = new joint.ui.Inspector({inputs:{"+a+"},groups:{"+b+"},cellView: cellView})");
 
			   };
			   if(opt === "och-ctp-grouping"){
				   var a = "directionality: {type: 'select' , options:['sink','source','bidirectional'], index: 1 , group: 'ochctp', label: 'Directionality', attrs: { 'label': {'data-tooltip': 'This attribute indicates the directionality of the termination point. Valid values are sink, source,and bidirectional. This attribute is read-only.'}}}";
				   var b = "ochctp: {label: 'OCH CTP', index: 1}";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+"},groups:{"+b+"},cellView:cellView})");
 
			   };
			   if(opt === undefined){
				   var a = "{myproperty: { type: 'range', min: 0, max: 30, defaultValue: 1, group: 'mydata', index: 1 }, attrs: {text: {text: { type: 'textarea', group: 'text', index: 1 },'font-size': { type: 'number', group: 'text', index: 2 }}}";
				   var b = "{mydata: { label: 'My Data', index: 1 },text: { label: 'Text', index: 2 }";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+"},groups:{ "+b+" },cellView: cellView})");
 
			   };
			   if(opt === undefined){
				   var a = "{myproperty: { type: 'range', min: 0, max: 30, defaultValue: 1, group: 'mydata', index: 1 }, attrs: {text: {text: { type: 'textarea', group: 'text', index: 1 },'font-size': { type: 'number', group: 'text', index: 2 }}}";
				   var b = "{mydata: { label: 'My Data', index: 1 },text: { label: 'Text', index: 2 }";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+"},groups:{ "+b+" },cellView: cellView})");
 
			   };
			   if(opt === undefined){
				   var a = "{myproperty: { type: 'range', min: 0, max: 30, defaultValue: 1, group: 'mydata', index: 1 }, attrs: {text: {text: { type: 'textarea', group: 'text', index: 1 },'font-size': { type: 'number', group: 'text', index: 2 }}}";
				   var b = "{mydata: { label: 'My Data', index: 1 },text: { label: 'Text', index: 2 }";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+"},groups:{ "+b+" },cellView: cellView})");
 
			   };
			   if(opt === undefined){
				   var a = "{myproperty: { type: 'range', min: 0, max: 30, defaultValue: 1, group: 'mydata', index: 1 }, attrs: {text: {text: { type: 'textarea', group: 'text', index: 1 },'font-size': { type: 'number', group: 'text', index: 2 }}}";
				   var b = "{mydata: { label: 'My Data', index: 1 },text: { label: 'Text', index: 2 }";
				   eval("this.inspector = new joint.ui.Inspector({inputs: {"+a+"},groups:{ "+b+" },cellView: cellView})");
 
			   };
			   
//            eval("this.inspector = new joint.ui.Inspector({inputs: {myproperty: { type: 'range', min: 0, max: 30, defaultValue: 1, group: 'mydata', index: 1 }, attrs: {text: {text: { type: 'textarea', group: 'text', index: 1 },'font-size': { type: 'number', group: 'text', index: 2 }}}},groups: {mydata: { label: 'My Data', index: 1 },text: { label: 'Text', index: 2 } },cellView: cellView})");
//			eval("this.inspector = new joint.ui.Inspector({inputs: inspectorDefs ? inspectorDefs.inputs : CommonInspectorInputs,groups: inspectorDefs ? inspectorDefs.groups : CommonInspectorGroups,cell: cell})");
//			this.inspector = new joint.ui.Inspector({    				inputs: inspectorDefs ? inspectorDefs.inputs : CommonInspectorInputs,    						groups: inspectorDefs ? inspectorDefs.groups : CommonInspectorGroups,    								cell: cell    			});    				
			this.initializeInspectorTooltips();this.inspector.render();$('.inspector-container').html(this.inspector.el); 	if (this.inspectorClosedGroups[cell.id]) {_.each(this.inspectorClosedGroups[cell.id], this.inspector.closeGroup, this.inspector);} else {this.inspector.$('.group:not(:first-child)').addClass('closed');}		    
		}


//		var cell = cellView.model || cellView;

//		// No need to re-render inspector if the cellView didn't change.
//		if (!this.inspector || this.inspector.options.cell !== cell) {

//		if (this.inspector) {

//		this.inspectorClosedGroups[this.inspector.options.cell.id] = _.map(app.inspector.$('.group.closed'), function(g) {
//		return $(g).attr('data-name');
//		});

//		// Clean up the old inspector if there was one.
//		this.inspector.updateCell();
////		this.inspector.remove();
//		}
//		if(cell.get('subType') === "Card"){
//		var inspectorDefs = InspectorDefs[cell.get('subType')];
//		//changed type(model) to subType
//		this.inspector = new joint.ui.Inspector({
//		inputs: inspectorDefs ? inspectorDefs.inputs : CommonInspectorInputs,
//		groups: inspectorDefs ? inspectorDefs.groups : CommonInspectorGroups,
//		cell: cell
//		});

//		this.initializeInspectorTooltips();

//		this.inspector.render();
//		$('.inspector-container').html(this.inspector.el);
//		}else{
//		return;
//		}   


//		if (this.inspectorClosedGroups[cell.id]) {

//		_.each(this.inspectorClosedGroups[cell.id], this.inspector.closeGroup, this.inspector);

//		} else {
//		this.inspector.$('.group:not(:first-child)').addClass('closed');
//		}
//		}
	},

	initializeInspectorTooltips: function() {

		this.inspector.on('render', function() {

			this.inspector.$('[data-tooltip]').each(function() {

				var $label = $(this);
				new joint.ui.Tooltip({
					target: $label,
					content: $label.data('tooltip'),
					right: '.inspector',
					direction: 'right'
				});
			});

		}, this);
	},

	initializeHaloAndInspector: function() {

		this.paper.on('cell:pointerup', function(cellView, evt) {

			if (cellView.model instanceof joint.dia.Link || this.selection.contains(cellView.model)) return;

			// In order to display halo link magnets on top of the freetransform div we have to create the
			// freetransform first. This is necessary for IE9+ where pointer-events don't work and we wouldn't
			// be able to access magnets hidden behind the div.
			// descomentar para inserir a borda de redimensionamento
			//var freetransform = new joint.ui.FreeTransform({ graph: this.graph, paper: this.paper, cell: cellView.model });
			var halo = new joint.ui.Halo({ graph: this.graph, paper: this.paper, cellView: cellView });

			// As we're using the FreeTransform plugin, there is no need for an extra resize tool in Halo.
			// Therefore, remove the resize tool handle and reposition the clone tool handle to make the
			// handles nicely spread around the elements.
			// descomentar para remover a ferramenta de redimensionamento ao Halo
			halo.removeHandle('resize');

			// descomentar para inserir a borda de redimensionamento
			//freetransform.render();
			halo.removeHandle('fork');
			halo.removeHandle('clone');
			halo.removeHandle('rotate');
			halo.removeHandle('link');
			halo.removeHandle('unlink');
			halo.removeHandle('remove');
			halo.render();

//			this.initializeHaloTooltips(halo);
			// Verificar atributos da cell nessa parte! e usar o eval


//			this.createInspector(cellView);
//			console.log(cellView.model.attributes.subType);
			if(cellView.model.attributes.subType === 'Card'){

				var ITUelements = [], ITUlinks = [];



				$('.inspector-container').show();

				var cellId = cellView.model.id;
				var card = app.graph.getCell(cellId);
				console.log(card);
				console.log(card.attributes.subType);
				console.log(card.attributes.attrs.data.cells);

//				card.prop('directionality','sink');
				console.log(card);
				console.log(cellView);

				$.ajax({
					type: "POST",
					async: false,
					url: "getCardAttributes.htm",
					data: {
						'card' : cellId,
						'supervisor' : name
					},
//					dataType: 'json',
					success: function(data){
						console.log(data);
//						atributte.fromJSON(data);
					},
					error : function(e) {
						alert("error: " + e.status);
					}
				});
//                var opt = undefined;
//				var opt = "gcc0-tp-grouping";
//				var opt = "gcc12-tp-grouping";
//				var opt = "och-client-ctp-grouping";
				var opt = "och-ctp-grouping";
				this.createInspector(cellView,opt);
			}else{
				$('.inspector-container').hide();
			}

			this.selectionView.cancelSelection();
			this.selection.reset([cellView.model]);

		}, this);

		this.paper.on('link:options', function(evt, cellView, x, y) {

			this.createInspector(cellView);
		}, this);
	},

	initializeNavigator: function() {

		var navigator = this.navigator = new joint.ui.Navigator({
			width: 240,
			height: 115,
			paperScroller: this.paperScroller,
			zoomOptions: { max: 5, min: 0.2 }
		});

		navigator.$el.appendTo('.navigator-container');
		navigator.render();
	},

	initializeHaloTooltips: function(halo) {

		new joint.ui.Tooltip({
			className: 'tooltip small',
			target: halo.$('.remove'),
			content: 'Click to remove the object',
			direction: 'right',
			right: halo.$('.remove'),
			padding: 15
		});
		// new joint.ui.Tooltip({
		// className: 'tooltip small',
		// target: halo.$('.fork'),
		// content: 'Click and drag to clone and connect the object in one go',
		// direction: 'left',
		// left: halo.$('.fork'),
		// padding: 15
		// });
		// new joint.ui.Tooltip({
		// className: 'tooltip small',
		// target: halo.$('.clone'),
		// content: 'Click and drag to clone the object',
		// direction: 'left',
		// left: halo.$('.clone'),
		// padding: 15
		// });
//		new joint.ui.Tooltip({
//		className: 'tooltip small',
//		target: halo.$('.unlink'),
//		content: 'Click to break all connections to other objects',
//		direction: 'right',
//		right: halo.$('.unlink'),
//		padding: 15
//		});
//		new joint.ui.Tooltip({
//		className: 'tooltip small',
//		target: halo.$('.link'),
//		content: 'Click and drag to connect the object',
//		direction: 'left',
//		left: halo.$('.link'),
//		padding: 15
//		});
		// new joint.ui.Tooltip({
		// className: 'tooltip small',
		// target: halo.$('.rotate'),
		// content: 'Click and drag to rotate the object',
		// direction: 'right',
		// right: halo.$('.rotate'),
		// padding: 15
		// });
	},

	initializeClipboard: function() {

		this.clipboard = new joint.ui.Clipboard;

//		KeyboardJS.on('ctrl + c', _.bind(function() {
//		// Copy all selected elements and their associated links.
//		this.clipboard.copyElements(this.selection, this.graph, { translate: { dx: 20, dy: 20 }, useLocalStorage: true });
//		}, this));

//		KeyboardJS.on('ctrl + v', _.bind(function() {

//		this.selectionView.cancelSelection();

//		this.clipboard.pasteCells(this.graph, { link: { z: -1 }, useLocalStorage: true });

//		// Make sure pasted elements get selected immediately. This makes the UX better as
//		// the user can immediately manipulate the pasted elements.
//		this.clipboard.each(function(cell) {

//		if (cell.get('type') === 'link') return;

//		// Push to the selection not to the model from the clipboard but put the model into the graph.
//		// Note that they are different models. There is no views associated with the models
//		// in clipboard.
//		this.selection.add(this.graph.getCell(cell.id));
//		this.selectionView.createSelectionBox(cell.findView(this.paper));

//		}, this);

//		}, this));

//		KeyboardJS.on('ctrl + x', _.bind(function() {

//		var originalCells = this.clipboard.copyElements(this.selection, this.graph, { useLocalStorage: true });
//		this.commandManager.initBatchCommand();
//		_.invoke(originalCells, 'remove');
//		this.commandManager.storeBatchCommand();
//		this.selectionView.cancelSelection();
//		}, this));
	},

	initializeCommandManager: function() {

		this.commandManager = new joint.dia.CommandManager({ graph: this.graph });

	},

	initializeValidator: function() {

		// This is just for demo purposes. Every application has its own validation rules or no validation
		// rules at all.

		this.validator = new joint.dia.Validator({ commandManager: this.commandManager });


		// this.validator.validate('change:position change:size add', _.bind(function(err, command, next) {

		// if (command.action === 'add' && command.batch) return next();

		// var cell = command.data.attributes || this.graph.getCell(command.data.id).toJSON();
		// var area = g.rect(cell.position.x, cell.position.y, cell.size.width, cell.size.height);

		// if (_.find(this.graph.getElements(), function(e) {

		// var position = e.get('position');
		// var size = e.get('size');
		// return (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height)));

		// })) return next("Another cell in the way!");
		// }, this));

		this.validator.on('invalid',function(message) {

			$('.statusbar-container').text(message).addClass('error');

			_.delay(function() {

				$('.statusbar-container').text('').removeClass('error');

			}, 1500);
		});
	},

	initializeToolbar: function() {

		this.initializeToolbarTooltips();

		$('#btn-undo').on('click', _.bind(this.commandManager.undo, this.commandManager));
		$('#btn-redo').on('click', _.bind(this.commandManager.redo, this.commandManager));
		$('#btn-clear').on('click', _.bind(this.graph.clear, this.graph));
		$('#btn-svg').on('click', _.bind(this.paper.openAsSVG, this.paper));
		$('#btn-png').on('click', _.bind(this.paper.openAsPNG, this.paper));
		$('#btn-print-file').on('click', _.bind(this.testFunction, this));
		$('#btn-zoom-in').on('click', _.bind(function() { this.paperScroller.zoom(0.2, { max: 5, grid: 0.2 }); }, this));
		$('#btn-zoom-out').on('click', _.bind(function() { this.paperScroller.zoom(-0.2, { min: 0.2, grid: 0.2 }); }, this));
		$('#btn-zoom-to-fit').on('click', _.bind(function() {
			this.paperScroller.zoomToFit({
				padding: 20,
				scaleGrid: 0.2,
				minScale: 0.2,
				maxScale: 5
			});
		}, this));
		$('#btn-fullscreen').on('click', _.bind(this.toggleFullscreen, this));
		$('#btn-print').on('click', _.bind(this.paper.print, this.paper));

		// toFront/toBack must be registered on mousedown. SelectionView empties the selection
		// on document mouseup which happens before the click event. @TODO fix SelectionView?
		$('#btn-to-front').on('mousedown', _.bind(function(evt) { this.selection.invoke('toFront'); }, this));
		$('#btn-to-back').on('mousedown', _.bind(function(evt) { this.selection.invoke('toBack'); }, this));

		$('#btn-layout').on('click', _.bind(this.layoutDirectedGraph, this));

		$('#input-gridsize').on('change', _.bind(function(evt) {
			var gridSize = parseInt(evt.target.value, 10);
			$('#output-gridsize').text(gridSize);
			this.setGrid(gridSize);
		}, this));

		$('#snapline-switch').change(_.bind(function(evt) {
			if (evt.target.checked) {
				this.snapLines.startListening();
			} else {
				this.snapLines.stopListening();
			}
		}, this));

		var $zoomLevel = $('#zoom-level');
		this.paper.on('scale', function(scale) {
			$zoomLevel.text(Math.round(scale * 100));
		});
	},

	initializeToolbarTooltips: function() {

		$('.toolbar-container [data-tooltip]').each(function() {

			new joint.ui.Tooltip({
				target: $(this),
				content: $(this).data('tooltip'),
				top: '.toolbar-container',
				direction: 'top'
			});
		});
	},

	testFunction: function(){
		var modelo = paper-container.getGraph();
		console.log(modelo);
	},

	toggleFullscreen: function() {

		var el = document.body;

		function prefixedResult(el, prop) {

			var prefixes = ['webkit', 'moz', 'ms', 'o', ''];
			for (var i = 0; i < prefixes.length; i++) {
				var prefix = prefixes[i];
				var propName = prefix ? (prefix + prop) : (prop.substr(0, 1).toLowerCase() + prop.substr(1));
				if (!_.isUndefined(el[propName])) {
					return _.isFunction(el[propName]) ? el[propName]() : el[propName];
				}
			}
		}

		if (prefixedResult(document, 'FullScreen') || prefixedResult(document, 'IsFullScreen')) {
			prefixedResult(document, 'CancelFullScreen');
		} else {
			prefixedResult(el, 'RequestFullScreen');
		}
	},

	setGrid: function(gridSize) {

		this.paper.options.gridSize = gridSize;

		var backgroundImage = this.getGridBackgroundImage(gridSize);
		this.paper.$el.css('background-image', 'url("' + backgroundImage + '")');
	},

	getGridBackgroundImage: function(gridSize, color) {

		var canvas = $('<canvas/>', { width: gridSize, height: gridSize });

		canvas[0].width = gridSize;
		canvas[0].height = gridSize;

		var context = canvas[0].getContext('2d');
		context.beginPath();
		context.rect(1, 1, 1, 1);
		context.fillStyle = color || '#AAAAAA';
		context.fill();

		return canvas[0].toDataURL('image/png');
	},

	layoutDirectedGraph: function() {

		this.commandManager.initBatchCommand();

		_.each(this.graph.getLinks(), function(link) {

			// Reset vertices.
			link.set('vertices', []);

			// Remove all the non-connected links.
			if (!link.get('source').id || !link.get('target').id) {
				link.remove();
			}
		});

		var pad = 50; // padding for the very left and very top element.
		joint.layout.DirectedGraph.layout(this.graph, {
			setLinkVertices: false,
			rankDir: 'LR',
			rankDir: 'TB',
			setPosition: function(cell, box) {
				cell.position(box.x - box.width / 2 + pad, box.y - box.height / 2 + pad);
			}
		});

		// Scroll to the top-left corner as this is the initial position for the DirectedGraph layout.
		this.paperScroller.el.scrollLeft = 0;
		this.paperScroller.el.scrollTop = 0;

		this.commandManager.storeBatchCommand();
	},

	initializeChannel: function(url) {
		// Example usage of the Channel plugin. Note that this assumes the `node channelHub` is running.
		// See the channelHub.js file for furhter instructions.

		var room = (location.hash && location.hash.substr(1));
		if (!room) {
			room = joint.util.uuid();
			this.navigate('#' + room);
		}

		var channel = this.channel = new joint.com.Channel({ graph: this.graph, url: url || 'ws://localhost:4141', query: { room: room } });
		console.log('room', room, 'channel', channel.id);

		var roomUrl = location.href.replace(location.hash, '') + '#' + room;
		$('.statusbar-container .rt-colab').html('Send this link to a friend to <b>collaborate in real-time</b>: <a href="' + roomUrl + '" target="_blank">' + roomUrl + '</a>');
	}

});
