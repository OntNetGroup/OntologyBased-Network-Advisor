var Rappid = Backbone.Router.extend({
	
	/* 
	 * Variável indicando se a ação sendo executada é a de adição de um transport function.
	 * Ao adicionar um TF sobre um container, é necessário chamar a função 'embed' do container.
	 * Com isso, a aplicação chamaria a função de inserir um TF numa camada e depois a de mudar a camada do TF.
	 * Porém, com esta variável de controle isso não ocorre. 
	*/
	isAddingTransportFunction: false,
	
    routes: {
        '*path': 'home'
    },

    initialize: function(options) {
        
        this.options = options || {};
    },

    home: function() {

        this.initializeEditor();
        this.cardID = joint.util.uuid();
    },

    initializeEditor: function() {

        this.inspectorClosedGroups = {};

        this.initializePaper();
        this.initializeStencil();
        this.initializeSelection();
        this.initializeHaloAndInspector();
        this.initializeNavigator();
        this.initializeClipboard();
        this.initializeCommandManager();
        this.initializeToolbar();
        this.graphHandle();
        this.initializeValidator();
        this.initializePortsBar();
    },

    // Create a graph, paper and wrap the paper in a PaperScroller.
    initializePaper: function() {
        
        this.graph = new joint.dia.Graph;

        this.graph.on('add', function(cell, collection, opt) {
            if (opt.stencil) {
                this.createInspector(cell);
                this.commandManager.stopListening();
                this.inspector.updateCell();
                this.commandManager.listen();
                this.inspector.$('[data-attribute]:first').focus();
            }
        }, this);
        
		// some types of the elements need resizing after they are dropped
		this.graph.on('add', function(cell, collection, opt) {
			if (!opt.stencil) return;

			if(cell.get('type') === 'link') return;
			var type = cell.get('type');
			var subtype = cell.get('subtype');
			
			// configuration of resizing
			var sizeMultiplierTypeWidth = { 	'bpmn.Pool': 5, // layers
									'basic.Path': 1.3, // transport functions
									}[type];			
			var sizeMultiplierTypeHeight = { 	'bpmn.Pool': 5, // layers
										'basic.Path': 1.3, // transport functions
									}[type];

			var sizeMultiplierSubtypeWidth = {  'in': 0.5, // in port
											'out': 0.5, // out port
										}[subtype];
			var sizeMultiplierSubTypeHeight = {  'in': 0.5, // in port
											'out': 0.5, // out port
											'AF': 0.7, // AF transport function
										}[subtype];
			
			var sizeMultiplierWidth = sizeMultiplierSubtypeWidth ? sizeMultiplierSubtypeWidth : sizeMultiplierTypeWidth;
			var sizeMultiplierHeight = sizeMultiplierSubTypeHeight ? sizeMultiplierSubTypeHeight : sizeMultiplierTypeHeight;
						
			var originalSize = cell.get('size');
			if (sizeMultiplierWidth && sizeMultiplierHeight) {
				cell.set('size', {
					width: originalSize.width * sizeMultiplierWidth,
					height: originalSize.height * sizeMultiplierHeight
				});
			}
			
			sizeMultiplierWidth = 0;
			sizeMultiplierHeight = 0;
		}, this);
		

        this.paper = new joint.dia.Paper({
            width: 1000,
            height: 1000,
            gridSize: 10,
            perpendicularLinks: true,
            model: this.graph,
            
			// RF: Permitir que nós contenham outros nós
			embeddingMode: true,
            
			// RF: Ao selecionar uma porta, destacar portas disponíveis para conexão com aquela
			markAvailable: true,
			
			// RF: Inserir 'snap link' às conexões
			snapLinks: { radius: 50 },
			
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
            }),

        	// RF: Inserir restrições de conexão entre os nós
            validateConnection: function(cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
            	console.log('validate connection');
            	if(!linkView) return false;
            	
            	/* Prevent linking to ports already being used */
        		var portUsed = _.find(this.model.getLinks(), function(link) {

    				return ((link.id !== linkView.model.id &&
    						link.get('target').id === cellViewT.model.id));
        		});
        		
        		// if (portUsed) return false; -> doesn't work!
        		if (!portUsed) {} else return false;
        		
        		// Prevent loop linking
        		if(cellViewS === cellViewT) return false;
        		
        		// Conexão entre dois transport functions
        		var sourceTFunctionID = cellViewS.model.id;
        		var targetTFunctionID = cellViewT.model.id;
        		console.log('try to connect ' +sourceTFunctionID+ ' and ' +targetTFunctionID);
        		
        		return canCreateLink(sourceTFunctionID, targetTFunctionID);
            },
            
	         // RF: Inserir 'containmnet rules' aos nós
        	validateEmbedding: function(childView, parentView) {
        		console.log('validate embedding');
        		
        		// se alguma interface tenta ser colocada sobre algum elemento
        		if(parentView.model === this.barIn) {
        			if(childView.model.get('subtype') !== 'in') return false;
        		}
        		if(parentView.model === this.barOut) {
        			if(childView.model.get('subtype') !== 'out') return false;
        		}
        		
        		return true;
        	}
        });

        // Garantir que as interfaces de entrada e saída permaneçam contidas em suas respectivas barras
        this.paper.on('cell:pointerup', function(cellView, evt) {
        	var cell = cellView.model;
        	var cellSubType = cell.get('subtype');
        	
            if (cellSubType === 'in' || cellSubType === 'out') {
	            
	            if(cellSubType === 'in') {
	            	cell.transition('position/y', 15, {});
	            	this.barIn.embed(cell);
	            } else {
	            	cell.transition('position/y', 955, {});
	            	this.barOut.embed(cell);
	            }
            }
        }, this);
        
        this.paperScroller = new joint.ui.PaperScroller({
            autoResizePaper: true,
            padding: 50,
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

            var linkView = this.paper.findViewByModel(cell);
            new joint.ui.Tooltip({
                className: 'tooltip small',
                target: linkView.$('.tool-options'),
                content: 'Click to open Inspector for this link',
                left: linkView.$('.tool-options'),
                direction: 'left'
            });
        }
    },

    // Create and populate stencil.
    initializeStencil: function() {

        this.stencil = new joint.ui.Stencil({
            graph: this.graph,
            paper: this.paper,
            width: 240,
            groups: Stencil.groups,
            search: {
                '*': ['type','attrs/text/text','attrs/.label/text', 'subtype'],
                'org.Member': ['attrs/.rank/text','attrs/.name/text']
            }
        });

        $('.stencil-container').append(this.stencil.render().el);

        this.stencil.$el.on('contextmenu', function(evt) { evt.preventDefault(); });
        $('.stencil-paper-drag').on('contextmenu', function(evt) { evt.preventDefault(); });

        var layoutOptions = {
            columnWidth: this.stencil.options.width / 2 - 10,
            columns: 2,
            rowHeight: 80,
            resizeToFit: true,
            dy: 10,
            dx: 10
        };

        _.each(Stencil.groups, function(group, name) {
            
            this.stencil.load(Stencil.shapes[name], name);
            joint.layout.GridLayout.layout(this.stencil.getGraph(name), layoutOptions);
            this.stencil.getPaper(name).fitToContent(1, 1, 10);

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
                    content: cell.get('subtype'),
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

            if (!$.contains(evt.target, this.paper.el)) {
                // remove selected elements from the paper only if the target is the paper
                return;
            }

            this.commandManager.initBatchCommand();
            this.selection.invoke('remove');
            this.commandManager.storeBatchCommand();
            this.selectionView.cancelSelection();

            // Prevent Backspace from navigating one page back (happens in FF).
            if (_.contains(keys, 'backspace') && !$(evt.target).is("input, textarea")) {

                evt.preventDefault();
            }

        }, this));
    },

    createInspector: function(cellView) {

        var cell = cellView.model || cellView;

        // No need to re-render inspector if the cellView didn't change.
        if (!this.inspector || this.inspector.options.cell !== cell) {
            
            if (this.inspector) {

                this.inspectorClosedGroups[this.inspector.options.cell.id] = _.map(app.inspector.$('.group.closed'), function(g) {
		    return $(g).attr('data-name');
		});
                
                // Clean up the old inspector if there was one.
                this.inspector.updateCell();
                this.inspector.remove();
            }

            var inspectorDefs = InspectorDefs[cell.get('type')];

            this.inspector = new joint.ui.Inspector({
                inputs: inspectorDefs ? inspectorDefs.inputs : CommonInspectorInputs,
                groups: inspectorDefs ? inspectorDefs.groups : CommonInspectorGroups,
                cell: cell
            });

            this.initializeInspectorTooltips();
            
            this.inspector.render();
            $('.inspector-container').html(this.inspector.el);

            if (this.inspectorClosedGroups[cell.id]) {

		_.each(this.inspectorClosedGroups[cell.id], this.inspector.closeGroup, this.inspector);

            } else {
                this.inspector.$('.group:not(:first-child)').addClass('closed');
            }
        }
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
            var halo = new joint.ui.Halo({ 	graph: this.graph,
            								paper: this.paper, cellView: cellView,
            								// tooltip shows only the subtype
            								boxContent: function(cellView) {
            									return cellView.model.get('subtype');
            								}
            });

            var cellSubtype = cellView.model.get('subtype');
            
            // As we're using the FreeTransform plugin, there is no need for an extra resize tool in Halo.
            // Therefore, remove the resize tool handle and reposition the clone tool handle to make the
            // handles nicely spread around the elements.
			// descomentar para remover a ferramenta de redimensionamento ao Halo
            //halo.removeHandle('resize');
            halo.removeHandle('fork');
            halo.removeHandle('clone');
            halo.removeHandle('rotate');
            
            if(_.contains(['in', 'out'], cellSubtype)) {
            	halo.removeHandle('resize');
            	halo.removeHandle('unlink');
            	halo.removeHandle('link');
            }
            
			// descomentar para inserir a borda de redimensionamento
            // freetransform.render();
            halo.render();

            this.initializeHaloTooltips(halo);

            this.createInspector(cellView);

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
         new joint.ui.Tooltip({
             className: 'tooltip small',
             target: halo.$('.fork'),
             content: 'Click and drag to clone and connect the object in one go',
             direction: 'left',
             left: halo.$('.fork'),
             padding: 15
         });
         new joint.ui.Tooltip({
             className: 'tooltip small',
             target: halo.$('.clone'),
             content: 'Click and drag to clone the object',
             direction: 'left',
             left: halo.$('.clone'),
             padding: 15
         });
        new joint.ui.Tooltip({
            className: 'tooltip small',
            target: halo.$('.unlink'),
            content: 'Click to break all connections to other objects',
            direction: 'right',
            right: halo.$('.unlink'),
            padding: 15
        });
        new joint.ui.Tooltip({
            className: 'tooltip small',
            target: halo.$('.link'),
            content: 'Click and drag to connect the object',
            direction: 'left',
            left: halo.$('.link'),
            padding: 15
        });
         new joint.ui.Tooltip({
             className: 'tooltip small',
             target: halo.$('.rotate'),
             content: 'Click and drag to rotate the object',
             direction: 'right',
             right: halo.$('.rotate'),
             padding: 15
         });
    },

    initializeClipboard: function() {

        this.clipboard = new joint.ui.Clipboard;
        
    },

    initializeCommandManager: function() {

        this.commandManager = new joint.dia.CommandManager({ graph: this.graph });

    },

    initializeValidator: function() {
        
        this.validator = new joint.dia.Validator({ commandManager: this.commandManager });

        this.validator.on('invalid',function(message) {
            
            $('.statusbar-container').text(message).addClass('error');

            _.delay(function() {

                $('.statusbar-container').text('').removeClass('error');
                
            }, 3000);
        });
        
        // valida a remoção de elementos do paper
        this.validator.validate('remove', _.bind(function(err, command, next) {

        	console.log(command);
        	var cellType = command.data.type;
        	var cellID = command.data.id;
        	
        	// se uma conexão for removida
        	if(cellType === 'link') {
        		var sourceID = command.data.attributes.source.id;
        		var targetID = command.data.attributes.target.id;
        		var sourceElement = this.graph.getCell(sourceID);
        		var targetElement = this.graph.getCell(targetID);
        		if(targetElement) var targetElementSubtype = targetElement.get('subtype');
    			
    			
    			// se target for uma porta, remover a porta ligada à conexão (consultar ontologia)
    			if(targetElementSubtype === 'in' || targetElementSubtype === 'out') {
    				var result = deletePort(targetID);
    				
    				if(result === "success") {
    					targetElement.remove();
    					return next(err);
    				} else {
    					return next(result);
    				}
            	}
    			
    			// se target for um transport function, consultar ontologia para remoção da conexão
    			if(targetElementSubtype === 'basic.Path') {
	    			var result = deleteLink(cellID);
	    			if(result === "success") {
	    				return next(err);
	    			} else {
						return next(result);
					}
    			}
    			
    			// o target era uma interface que ja foi removida
    			return next(err);
        	}
        	
        	// se um transport function for removido, consultar ontologia
        	if(cellType === 'basic.Path') {
        		
        		var result = deleteTransportFunction(cellID);
        		if(result === "success") {
    				return next(err);
    			} else {
					return next(result);
				}
        		
        	}
        	
        	// se uma camada inteira for removida, consultar ontologia
        	if(cellType === 'bpmn.Pool') {
        		
        		var containerName = command.data.attributes.subtype;
        		var containerType = 'layer';
        		var cardID = this.cardID; // TODO: get cardID
        		
				var result = deleteContainer(containerName, containerType, cardID);
				if(result === "success") {
					// TODO: retornar camada ao stencil
    				return next(err);
    			} else {
					return next(result);
				}
        	}

        	// então é uma interface
			var result = deletePort(cellID);			
			if(result === "success") {
				return next(err);
			} else {
				return next(result);
			}
			
        }, this));
        
        
        // validar inserção de elementos no grafo
        this.validator.validate('add', this.isNotLink, _.bind(function(err, command, next) {
        	        	
        	var cell = this.graph.getCell(command.data.id);
        	var cellType = cell.get('type');
        	var cellSubType = cell.get('subtype');
        	        	
        	var position = cell.get('position');
			var size = cell.get('size');
			var area = g.rect(position.x, position.y, size.width, size.height);
			
			var parent;
			// get all elements below the added one
			_.each(this.graph.getElements(), function(e) {
			
				var position = e.get('position');
				var size = e.get('size');
				if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
					parent = e;
				}
			});
        				
			if(cellSubType === 'out' || cellSubType === 'in') { // elemento é uma porta
				
				if(parent) { // existe algum elemento abaixo
					var parentType = parent.get('type');
					
					if(parentType === 'basic.Path'){ // elemento abaixo é um transport function

						var portID = cell.id;
						var transportFunctionID = parent.id;
						console.log('try to create port ' +portID+ ' of TF ' +transportFunctionID);
						var result = createPort(portID, transportFunctionID);
						
						if(result === "success") {
						
							var newLink = new joint.dia.Link({	source: {id: parent.id}, target: {id: cell.id}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
			    			this.graph.addCell(newLink);
			    			
			    			// Move the port to the superior (in port) or inferior (out port) bar
			    			if(cellType === 'basic.Circle') {
			    				cell.transition('position/y', 15, {});
			    				this.barIn.embed(cell);
			    			}
			    			else {
			    				cell.transition('position/y', 955, {});
			    				this.barOut.embed(cell);
			    			}
			    			
							return next(err);
						} else {
							return next(result);
						}
					} else { // elemento abaixo é uma camada 
						return next('Please, add the port over a transport function.');
					}
					
				} else { // nenhum elemento abaixo
					return next('Please, add the port over a transport function.');
				}
			
			} else if(cellType === 'bpmn.Pool') { // elemento é uma camada
				
				if(parent) { // existe elemento abaixo
					return next('Another element in the way!');
				} else {
					// consultar ontologia para inserção de camada no card
					var containerName = cellSubType;
					var containerType = 'layer';
					var cardID = this.cardID; // TODO: get cardID 
					var result = insertContainer(containerName, containerType, cardID)
					
					if(result === "success") {
						// TODO: remover camada do stencil
						return next(err);
					} else {
						return next(result);
					}
				}
			
			} else if(cellType === 'basic.Path') { // elemento é um transport function
				
				if(parent) { // existe algum elemento abaixo
					var parentType = parent.get('type');
					
					if(parentType === 'bpmn.Pool'){ // elemento abaixo é uma camada
						var tFunctionID = cell.id;
						var tFunctionType = cell.get('subtype');
						var containerName = parent.get('subtype');
						var containerType = 'layer';
						var cardID = this.cardID; // TODO: get cardID
						console.log('try to insert ' +tFunctionID+ ' of type ' +tFunctionType+ ' on layer ' +containerName+ ' inside card ' +cardID);
						
						var result = createTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);
						
						if(result === "success") {	
							this.isAddingTransportFunction = true;
							parent.embed(cell);
							return next(err);
						} else {
							return next(result);
						}
											
					} else { // elemento abaixo não é um container
						return next('Please, add the transport function on the paper or a layer.');
					}
				} else { // não existe elemento abaixo
					// consultar ontologia para inserção de transport function diretamente no card
					var tFunctionID = cell.id;
					var tFunctionType = cell.get('subtype');
					var containerName = '';
					var containerType = '';
					var cardID = this.cardID; // TODO: get cardID
					console.log('try to insert ' +tFunctionID+ ' of type ' +tFunctionType+ 'on layer ' +containerName+ ' inside card ' +cardID);
					var result = createTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);
					
					if(result === "success") {
						return next(err);
					} else {
						return next(result);
					}
				}
				
			}
        }, this));
        
        // validar inserção de links no grafo
        this.validator.validate('change:target change:source add', this.isLink, _.bind(function(err, command, next) {
        	
        	// TODO: validar a troca de target e de source (quando o usuário arrasta uma das pontas da 'seta')
        	var link = this.graph.getCell(command.data.id).toJSON();
        	var sourceTFunctionID = link.source.id;
            var targetTFunctionID = link.target.id;
            
            if (sourceTFunctionID && targetTFunctionID) {
            	var result = createLink(sourceTFunctionID, targetTFunctionID);
            	
            	if(result === "success") {
					return next(err);
				} else {
					return next(result);
				}
            } else {
            	return next('Please, connect to a valid transport function');
            }
        }, this));
        
        // validate embedding
        this.validator.validate('change:parent', _.bind(function(err, command, next) {

        	console.log(this.isAddingTransportFunction)
        	if(this.isAddingTransportFunction) {
        		this.isAddingTransportFunction = false;
        		return next(err);
        	}
        	
        	console.log(command);
        	var cell = this.graph.getCell(command.data.id);
        	var cellType = cell.get('type');
        	var cellSubType = cell.get('subtype');
        	        	
        	var position = cell.get('position');
			var size = cell.get('size');
			var area = g.rect(position.x, position.y, size.width, size.height);
			
			var parent;
			// get all elements below the added one
			_.each(this.graph.getElements(), function(e) {
			
				var position = e.get('position');
				var size = e.get('size');
				if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
					parent = e;
				}
			});
			
			if(cellSubType === 'out' || cellSubType === 'in') { // elemento é uma porta
				
//				if(parent) { // existe algum elemento abaixo
//					var parentType = parent.get('type');
//					
//					if(parentType === 'basic.Path'){ // elemento abaixo é um transport function
//
//						var portID = cell.id;
//						var transportFunctionID = parent.id;
//						console.log('try to create port ' +portID+ ' of TF ' +transportFunctionID);
//						var result = createPort(portID, transportFunctionID);
//						
//						if(result === "success") {
//						
//							var newLink = new joint.dia.Link({	source: {id: parent.id}, target: {id: cell.id}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
//			    			this.graph.addCell(newLink);
//			    			
//			    			// Move the port to the superior (in port) or inferior (out port) bar
//			    			if(cellType === 'basic.Circle') {
//			    				cell.transition('position/y', 15, {});
//			    				this.barIn.embed(cell);
//			    			}
//			    			else {
//			    				cell.transition('position/y', 955, {});
//			    				this.barOut.embed(cell);
//			    			}
//			    			
//							return next(err);
//						} else {
//							return next(result);
//						}
//					} else { // elemento abaixo é uma camada 
//						return next('Please, add the port over a transport function.');
//					}
//					
//				} else { // nenhum elemento abaixo
//					return next('Please, add the port over a transport function.');
//				}
			
			} else if(cellType === 'bpmn.Pool') { // elemento é uma camada
				
				if(parent) { // existe elemento abaixo
					return next('Another element in the way!');
				}
			
			} else if(cellType === 'basic.Path') { // elemento é um transport function
				
				if(parent) { // existe algum elemento abaixo
					var parentType = parent.get('type');
					
					if(parentType === 'bpmn.Pool'){ // elemento abaixo é uma camada
						// consultar ontologia para troca de camada do transport function
						var tFunctionID = cell.id;
						var containerName = parent.get('subtype');
						var containerType = 'layer';
						var cardID = this.cardID; // TODO: get cardID
						console.log('change layer of ' +tFunctionID+ ' to layer ' +containerName+ ' inside card ' +cardID);
						
						var result = changeContainer(tFunctionID, containerName, containerType, cardID)
						
						if(result === "success") {						
							parent.embed(cell);
							return next(err);
						} else {
							return next(result);
						}
											
					} else { // elemento abaixo não é um container
						return next('Please, move the transport function to the paper or a layer.');
					}
				} else { // não existe elemento abaixo
					// consultar ontologia para remoção de camada do transport function
					var tFunctionID = cell.id;
					var containerName = '';
					var containerType = '';
					var cardID = this.cardID; // TODO: get cardID
					console.log('remove layer of ' +tFunctionID+ ' inside card ' +cardID);
					
					var result = changeContainer(tFunctionID, containerName, containerType, cardID)
					
					if(result === "success") {
						return next(err);
					} else {
						return next(result);
					}
				}
				
			}
			
        }, this));
    },

    initializeToolbar: function() {

        this.initializeToolbarTooltips();
        
        $('#btn-undo').on('click', _.bind(this.commandManager.undo, this.commandManager));
        $('#btn-redo').on('click', _.bind(this.commandManager.redo, this.commandManager));
        $('#btn-clear').on('click', _.bind(this.graph.clear, this.graph));
        $('#btn-svg').on('click', _.bind(this.paper.openAsSVG, this.paper));
        $('#btn-png').on('click', _.bind(this.paper.openAsPNG, this.paper));
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
    
    // inicializa as barras superior e inferior das portas de entrada e saída, respectivamente
    initializePortsBar: function() {
    	// barra superior das portas de entrada
    	this.barIn = new joint.shapes.basic.Rect({
						subtype: 'barIn',
						embeddedPorts: 0, // qntd de portas contidas nesta barra
						position: {x: 0, y: 0},
						size: {width: 900, height: 60},
					    attrs: {
							'.': { magnet: false },
					        rect: {
					            fill: '#ffffff',
					            'pointer-events': 'none'
					        }
					    }
					});
    	this.barIn.on('change:embeds', this.manageEmbeddedPorts, this.barIn);
    	
    	// barra inferior das portas de saída
    	this.barOut = new joint.shapes.basic.Rect({
					subtype: 'barOut',
					embeddedPorts: 0, // qntd de portas contidas nesta barra
					magnet: false,
					position: {x: 0, y: 940},
					size: {width: 900, height: 60},
				    attrs: {
						'.': { magnet: false },
				        rect: {
				            fill: '#ffffff',
				            'pointer-events': 'none'
				        }
				    }
				});
    	this.barOut.on('change:embeds', this.manageEmbeddedPorts, this.barOut);
    	
    	// rótulo da barra superior
    	var labelIn = new joint.shapes.basic.Rect({
						subtype: 'labelIn',
						position: {x: 900, y: 0},
						size: {width: 100, height: 60},
					    attrs: {
							'.': { magnet: false },
					        rect: {
					            fill: '#f1c40f',
					            'pointer-events': 'none'
					        },
					    	text: { text: 'I N', fill: '#000000', 'font-size': 24, stroke: '#000000', 'stroke-width': 2, 'pointer-events': 'none' }
					    },
					});
    	// rótulo da barra inferior    	
    	var labelOut = new joint.shapes.basic.Rect({
						subtype: 'labelOut',
						position: {x: 900, y: 940},
						size: {width: 100, height: 60},
					    attrs: {
							'.': { magnet: false },
					        rect: {
					            fill: '#e9967a',
					            'pointer-events': 'none'
					        },
					    	text: { text: 'O U T', fill: '#000000', 'font-size': 24, stroke: '#000000', 'stroke-width': 2, 'pointer-events': 'none' }
					    },
					});
    	
    	
    	
    	this.graph.addCells([this.barIn, labelIn, this.barOut, labelOut]);
    },
    

    // Add event listeners to the graph
    graphHandle: function() {

        
    },
    
    /* ------ AUXILIAR FUNCTIONS ------- */
    // reorganiza as portas contidas na barra
    manageEmbeddedPorts: function(port) {
    	// para cada porta contida na barra
    	var embeddedPorts = this.getEmbeddedCells();
    	var positionMultiplier = 10;
    	
    	_.each(embeddedPorts, function(p){
			p.transition('position/x', positionMultiplier, {});
			positionMultiplier += 40;
    	});
    },
    
    /* ------- VALIDATION FUNCTIONS -------- */
    // Check if cell in command is not a link. Continue validating if yes, otherwise stop.
    isNotLink: function(err, command, next) {
        if (command.data.type !== 'link') {
        	return next(err);
        }
        // otherwise stop validating (don't call next validation function)
    },
    
    // Check if cell in command is a link. Continue validating if yes, otherwise stop.
    isLink: function(err, command, next) {
        if (command.data.type === 'link') return next(err);
        // otherwise stop validating (don't call next validation function)
    }
    
});
