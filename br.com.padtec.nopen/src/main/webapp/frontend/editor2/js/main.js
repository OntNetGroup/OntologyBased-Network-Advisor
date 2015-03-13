var graph = new joint.dia.Graph;

//Create a paper and wrap it in a PaperScroller.
//----------------------------------------------

var paper = new joint.dia.Paper({

	width: 1000,
	height: 1000,
	gridSize: 1,
	perpendicularLinks: true,
	model: graph,
	defaultLink: new joint.dia.Link({
		attrs: {
			// @TODO: scale(0) fails in Firefox
			'.marker-source': { d: 'M 10 0 L 0 5 L 10 10 z', transform: 'scale(0.001)' },
			'.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }
		}
	})
});

var paperScroller = new joint.ui.PaperScroller({
	autoResizePaper: true,
	paper: paper
});

$('#paper').append(paperScroller.render().el);

paperScroller.center();

//Create and populate stencil.
//----------------------------

var stencil = new joint.ui.Stencil({ 
	graph: graph,
	paper: paper, 
	width: 200, 
	height: 380, 
	groups: {
		tfs: { label: 'Transport Functions', index: 1 },
		equips: { label: 'Equipment Constructs', index: 2}
	} });

$('#stencil').append(stencil.render().el);

var r = new joint.shapes.basic.Rect({
	position: { x: 60, y: 20 },
	size: { width: 100, height: 60 },
	attrs: {
		rect: { rx: 2, ry: 2, width: 50, height: 30, fill: '#27AE60' },
		text: { text: 'rect', fill: 'white', 'font-size': 10 }
	}
});
var c = new joint.shapes.basic.Circle({
	position: { x: 60, y: 100 },
	size: { width: 100, height: 60 },
	attrs: {
		circle: { width: 50, height: 30, fill: '#E74C3C' },
		text: { text: 'ellipse', fill: 'white', 'font-size': 10 }
	}
});
var m = new joint.shapes.devs.Model({
	position: { x: 75, y: 180 },
	size: { width: 80, height: 90 },
	inPorts: ['in1','in2'],
	outPorts: ['out'],
	attrs: {
		rect: { fill: '#8e44ad', rx: 2, ry: 2 },
		'.label': { text: 'model', fill: 'white', 'font-size': 10 },
		'.inPorts circle, .outPorts circle': { fill: '#f1c40f', opacity: 0.9 },
		'.inPorts text, .outPorts text': { 'font-size': 9 },
	}
});
var i = new joint.shapes.basic.Image({
	position: { x: 85, y: 290 },
	size: { width: 50, height: 50 },
	attrs: {
		image: { width: 50, height: 50, 'xlink:href': 'data:image/png;base64,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' },
		text: { text: 'image', 'font-size': 9, display: '' }
	}
});

var myPath = new joint.shapes.basic.Path({
    position: { x: 100, y: 50 },
    size: { width: 60, height: 60 },
    attrs: {
        path: { d: 'M 10 25 L 10 75 L 60 75 L 10 25' },
        text: { text: 'Triangle', 'font-size': 9, display: ''  }
    }
});

stencil.load(CustomShape.getStencilForTransportFunctions(),'tfs');
stencil.load([myPath, r, c, i, m],'equips');


//Selection.
//----------

var selection = new Backbone.Collection;

var selectionView = new joint.ui.SelectionView({
	paper: paper,
	graph: graph,
	model: selection
});


paper.on('blank:pointerdown', selectionView.startSelecting);

paper.on('cell:pointerdown', function(cellView, evt) {
	// Select an element if CTRL/Meta key is pressed while the element is clicked.
	if ((evt.ctrlKey || evt.metaKey) && !(cellView.model instanceof joint.dia.Link)) {
		selection.add(cellView.model);
		selectionView.createSelectionBox(cellView);
	}
});

selectionView.on('selection-box:pointerdown', function(evt) {
	// Unselect an element if the CTRL/Meta key is pressed while a selected element is clicked.
	if (evt.ctrlKey || evt.metaKey) {
		var cell = selection.get($(evt.target).data('model'));
		selection.reset(selection.without(cell));
		selectionView.destroySelectionBox(paper.findViewByModel(cell));
	}
});

//Disable context menu inside the paper.
//This prevents from context menu being shown when selecting individual elements with Ctrl in OS X.
paper.el.oncontextmenu = function(evt) { evt.preventDefault(); };

//CUSTOM
//Removing rotate action of a selection
selectionView.removeHandle('rotate');

//An example of a simple element editor.
//--------------------------------------

var elementInspector = new ElementInspector();
$('.inspector').append(elementInspector.el);

//Halo - element tools.
//---------------------

paper.on('cell:pointerup', function(cellView, evt) {

	if (cellView.model instanceof joint.dia.Link || selection.contains(cellView.model)) return;

	var halo = new joint.ui.Halo({
		graph: graph,
		paper: paper,
		cellView: cellView
	});

	halo.render();
	elementInspector.render(cellView);

	//CUSTOM
	//Removing useless actions
	halo.removeHandle('clone');
	halo.removeHandle('rotate');
	halo.removeHandle('fork');
	halo.removeHandle('unlink');

	halo.changeHandle('remove', { position: 'sw' });
	halo.changeHandle('link', { position: 's' });

});


//Clipboard.
//----------

var clipboard = new joint.ui.Clipboard;
KeyboardJS.on('ctrl + c', function() {
	// Copy all selected elements and their associated links.
	clipboard.copyElements(selection, graph, { translate: { dx: 20, dy: 20 }, useLocalStorage: true });
});
KeyboardJS.on('ctrl + v', function() {
	clipboard.pasteCells(graph);

	selectionView.cancelSelection();

	clipboard.pasteCells(graph, { link: { z: -1 }, useLocalStorage: true });

	// Make sure pasted elements get selected immediately. This makes the UX better as
	// the user can immediately manipulate the pasted elements.
	clipboard.each(function(cell) {

		if (cell.get('type') === 'link') return;

		// Push to the selection not to the model from the clipboard but put the model into the graph.
		// Note that they are different models. There is no views associated with the models
		// in clipboard.
		selection.add(graph.get('cells').get(cell.id));
	});

	selection.each(function(cell) {
		selectionView.createSelectionBox(paper.findViewByModel(cell));
	});
});

//Command Manager - undo/redo.
//----------------------------

var commandManager = new joint.dia.CommandManager({ graph: graph });

//Validator
//---------

var validator = new joint.dia.Validator({ commandManager: commandManager });

validator.validate('change:position change:size add', function (err, command, next) {

	if (command.action === 'add' && command.batch) return next();

	var cell = command.data.attributes || graph.getCell(command.data.id).toJSON();
	var area = g.rect(cell.position.x, cell.position.y, cell.size.width, cell.size.height);

	if (_.find(graph.getElements(), function (e) {

		var position = e.get('position'), size = e.get('size');
		return (e.id !== cell.id && area.intersect(g.rect(position.x, position.y, size.width, size.height)));

	})) return next("Another cell in the way!");
});

validator.on('invalid',function(message) {
	$('#message').text(message).fadeIn(0).delay(1500).fadeOut(0);
});

//Hook on toolbar buttons.
//------------------------

$('#btn-undo').on('click', _.bind(commandManager.undo, commandManager));
$('#btn-redo').on('click', _.bind(commandManager.redo, commandManager));
$('#btn-clear').on('click', _.bind(graph.clear, graph));
$('#btn-svg').on('click', function() {
	paper.openAsSVG();
	console.log(paper.toSVG()); // An exmaple of retriving the paper SVG as a string.
});

$('#btn-zoom-in').on('click', function() {
	paperScroller.zoom(.2, { max: 3 });
});
$('#btn-zoom-out').on('click', function() {
	paperScroller.zoom(-.2, { min: 0.2 });
});

//Snaplines - a.k.a Guidelines - CUSTOM
//------------------------

var snaplines = new joint.ui.Snaplines({ paper: paper });
snaplines.startListening();