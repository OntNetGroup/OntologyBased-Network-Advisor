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

        this.inspectorClosedGroups = {};

        this.initializePaper();
        this.initializeStencil();
        this.initializeSelection();
        this.initializeHaloAndInspector();
        this.initializeNavigator();
        this.initializeClipboard();
        this.initializeCommandManager();
        this.initializeToolbar();
        // Intentionally commented out. See the `initializeValidator()` method for reasons.
        // Uncomment for demo purposes.
        this.initializeValidator();
        this.graphHandle();
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

			// configuration of resizing
			var sizeMultiplier = { 	'bpmn.Pool': 5, // layers
									'basic.Path': 1.5, // transport functions
									'basic.Rect': 0.5, // out port
									'basic.Circle': 0.5 // in port
									}[type];

			if (sizeMultiplier) {
				var originalSize = cell.get('size');
				cell.set('size', {
					width: originalSize.width * sizeMultiplier,
					height: originalSize.height * sizeMultiplier
				});
			}
			
			sizeMultiplier = 0;
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
			snapLinks: { radius: 25 },
			
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
            	/*
            	 * TODO:	ADD CONNECTION RESTRICTIONS HERE
            	 * 			se elemento ITU tenta se conectar a elemento ITU, retorna false
            	 * 			se porta in tenta se conectar a in ou out se conectar a out, retorna false
            	 * 			se porta tenta se conectar a uma porta ja usada, retorna false
            	 * 			se porta de um elemento ITU tenta se conectar à porta de um elemento ITU, consulta ontologia
            	*/
            },
            
	         // RF: Inserir 'containmnet rules' aos nós
        	validateEmbedding: function(childView, parentView) {
        		 /*
        		  * TODO:	ADD EMBEDDING RESTRICTIONS HERE
        		  * 		se transport function tenta ser incluido em alguma camada, consulta ontologia
        		  */
        	}
        });

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
        
        // Listen on cell:pointerup and link to or be embedded by an element found below
        this.paper.on('cell:pointerup', function(cellView, evt, x, y) {
        	// Find the first element below that is not a link nor the dragged element itself.
            var elementBelow = this.graph.get('cells').find(function(cell) {
                if (cell instanceof joint.dia.Link) return false; // Not interested in links.
                if (cell.id === cellView.model.id) return false; // The same element as the dropped one.
                if (cell.getBBox().containsPoint(g.point(x, y))) {
                    return true;
                }
                return false;
            });
            
            // If the two elements are connected already, don't
            // connect them again (this is application specific though).
            if (elementBelow && !_.contains(this.graph.getNeighbors(elementBelow), cellView.model)) {
                
                console.log('Embed or connect!');
                this.embedOrConnect(elementBelow, cellView.model);
            }
        }, this);
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

            // As we're using the FreeTransform plugin, there is no need for an extra resize tool in Halo.
            // Therefore, remove the resize tool handle and reposition the clone tool handle to make the
            // handles nicely spread around the elements.
			// descomentar para remover a ferramenta de redimensionamento ao Halo
            //halo.removeHandle('resize');
            halo.removeHandle('fork');
            halo.removeHandle('clone');
            halo.removeHandle('rotate');
            if(cellView.model.get('type') === 'basic.Path') halo.removeHandle('link');
            
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
        
        KeyboardJS.on('ctrl + c', _.bind(function() {
            // Copy all selected elements and their associated links.
            this.clipboard.copyElements(this.selection, this.graph, { translate: { dx: 20, dy: 20 }, useLocalStorage: true });
        }, this));
        
        KeyboardJS.on('ctrl + v', _.bind(function() {

            this.selectionView.cancelSelection();

            this.clipboard.pasteCells(this.graph, { link: { z: -1 }, useLocalStorage: true });

            // Make sure pasted elements get selected immediately. This makes the UX better as
            // the user can immediately manipulate the pasted elements.
            this.clipboard.each(function(cell) {

                if (cell.get('type') === 'link') return;

                // Push to the selection not to the model from the clipboard but put the model into the graph.
                // Note that they are different models. There is no views associated with the models
                // in clipboard.
                this.selection.add(this.graph.getCell(cell.id));
		this.selectionView.createSelectionBox(cell.findView(this.paper));

            }, this);

        }, this));

        KeyboardJS.on('ctrl + x', _.bind(function() {

            var originalCells = this.clipboard.copyElements(this.selection, this.graph, { useLocalStorage: true });
            this.commandManager.initBatchCommand();
            _.invoke(originalCells, 'remove');
            this.commandManager.storeBatchCommand();
            this.selectionView.cancelSelection();
        }, this));
    },

    initializeCommandManager: function() {

        this.commandManager = new joint.dia.CommandManager({ graph: this.graph });

        KeyboardJS.on('ctrl + z', _.bind(function() {

            this.commandManager.undo();
            this.selectionView.cancelSelection();
        }, this));
        
        KeyboardJS.on('ctrl + y', _.bind(function() {

            this.commandManager.redo();
            this.selectionView.cancelSelection();
        }, this));
    },

    initializeValidator: function() {
        
        this.validator = new joint.dia.Validator({ commandManager: this.commandManager });

//         this.validator.validate('change:position change:size add', _.bind(function(err, command, next) {

            // if (command.action === 'add' && command.batch) return next();

            // var cell = command.data.attributes || this.graph.getCell(command.data.id).toJSON();
            // var area = g.rect(cell.position.x, cell.position.y, cell.size.width, cell.size.height);

            // if (_.find(this.graph.getElements(), function(e) {

				// var position = e.get('position');
                // var size = e.get('size');
				// return (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height)));

            // })) return next("Another cell in the way!");
//         }, this));

        this.validator.on('invalid',function(message) {
            
            $('.statusbar-container').text(message).addClass('error');

            _.delay(function() {

                $('.statusbar-container').text('').removeClass('error');
                
            }, 3000);
        });
        
        
        // validar inserção de transport functions
        // validar que portas sejam inseridas somente sobre elementos ITU 
        this.validator.validate('add', this.isNotLink, _.bind(function(err, command, next) {
        	
        	if (command.action === 'add' && command.batch) return next();
        	
        	var cell = command.data.attributes || this.graph.getCell(command.data.id).toJSON();
//        	console.log(cell);
        	var cellType = cell.type;
        	var position = cell.position;
			var size = cell.size;
			var area = g.rect(position.x, position.y, size.width, size.height);
			
			var parent;
			// get all elements below the added one
			_.each(this.graph.getElements(), function(e) {
			
				var position = e.get('position');
				var size = e.get('size');
				if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
					// save the most above among the elements below
					console.log('yes');
					parent = e;
				}
			});
        	
			console.log(parent);
			
			if(cellType === 'basic.Rect' || cellType === 'basic.Circle') { // elemento é uma porta
				if(parent) { // existe algum elemento abaixo
					var parentType = parent.get('type');
					console.log(parentType);
					if(parentType === 'bpmn.Pool'){ // elemento abaixo é uma camada
						return next('Please, add the port over a transport function.');
					} else {
						console.log('consulta ontologia');
						next(err);
					}
					
				} else { // nenhum elemento abaixo
					return next('Please, add the port over a transport function.');
				}
			
			} else if(cellType === 'bpmn.Pool') { // elemento é uma camada
				if(parent) { // existe algum elemento abaixo
					return next('Another element in the way!');
				}
			
			} else { // elemento é um transport function
				if(parent) { // existe algum elemento abaixo
					var parentType = parent.get('type');
					if(parentType !== 'bpmn.Pool'){ // elemento abaixo não é uma camada
						return next('Transport function or port in the way!.');
					} else {
						console.log('consulta ontologia');
						next(err);
					}
				} else { // não existe elemento abaixo
					console.log('consulta ontologia');
					next(err);
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
    },
    

    // Add event listeners to the graph
    graphHandle: function() {

        // When a cell is added on another one, it should be embedded or connected (in case of it being a port)
        this.graph.on('add', function(cell) {

    		if(cell.get('type') === 'link') return;

    		var position = cell.get('position');
    		var size = cell.get('size');
    		var area = g.rect(position.x, position.y, size.width, size.height);

    		var parent;
    		// get all elements below the added one
    		_.each(this.graph.getElements(), function(e) {

    			var position = e.get('position');
    			var size = e.get('size');
    			if (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height))) {
    				// save the most above among the elements below
    				parent = e;
    			}
    		});

    		if(parent) {		
    			this.embedOrConnect(parent, cell);
    		}
    	}, this);
    },
    
    /* ------ AUXILIAR FUNCTIONS ------- */
    // decide whether the elements should be embedded or connected
    embedOrConnect: function(parent, child) {
    	
    	var parentType = parent.get('type');
    	var childType = child.get('type');
//    	console.log('parent type: ' +parentType+ '; child type: ' +childType);
    	
    	if(parentType === 'bpmn.Pool' && childType === 'basic.Path') { // parent is a layer and child is an ITU element
    		parent.embed(child);
    	} else { // parent is an ITU element and child is a port
    		if(parentType === 'basic.Path' && (childType === 'basic.Rect' || childType === 'basic.Circle')) {

    			var newLink = new joint.dia.Link({	source: {id: parent.id}, target: {id: child.id}, attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }}});
    			this.graph.addCell(newLink);
    			
    			// Move the port a bit up (in port) or down (out port)
    			if(childType === 'basic.Circle') child.translate(0, -60);
    			else child.translate(0, 100);
    		}
    	}
    },
    
    /* ------- VALIDATION FUNCTIONS -------- */
    // Check if cell in command is not a link. Continue validating if yes, otherwise stop.
    isNotLink: function(err, command, next) {
        if (command.data.type !== 'link') return next(err);
        // otherwise stop validating (don't call next validation function)
    }
});
