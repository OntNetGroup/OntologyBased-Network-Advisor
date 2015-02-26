/*
* Author: Freddy Brasileiro
*/
function resizeCanvas() {
	var canvas = document.getElementById('viewport'),
    canvasDiv = document.getElementById('canvas-div');
	canvas.width = canvasDiv.offsetWidth - 40;
    canvas.height = 630;
}

function initCanvas(){
	// resize the canvas to fill browser window dynamically
	window.addEventListener('resize', resizeCanvas, false);

	$('#main-menu-toggle').click();
	resizeCanvas();
}

