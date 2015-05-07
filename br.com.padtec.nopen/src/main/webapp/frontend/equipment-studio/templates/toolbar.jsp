<div class="toolbar-container">
     <!-- <button id="btn-undo" class="btn" data-tooltip="Undo"><img src="/nopen/frontend/equipment-studio/img/undo.png" alt="Undo"/></button> -->
     <!-- <button id="btn-redo" class="btn" data-tooltip="Redo"><img src="/nopen/frontend/equipment-studio/img/redo.png" alt="Redo"/></button> -->
     <button id="btn-save" class="btn" data-tooltip="Save"><img src="/nopen/frontend/equipment-studio/img/save.png" alt="Save"/></button>
	 <button id="btn-open" class="btn" data-tooltip="Open"><img src="/nopen/frontend/equipment-studio/img/open.png" alt="Open"/></button>
     <button id="btn-png" class="btn" data-tooltip="Open as PNG in a New Window"><img src="/nopen/frontend/equipment-studio/img/pngfile.png" alt="PNG"/></button>
<!-- <button id="btn-print-file" class="btn" data-tooltip="Save all imgs index in a file">save index to file</button>  -->
<!-- <button id="btn-print" class="btn" data-tooltip="Open a Print Dialog"><img src="/nopen/frontend/equipment-studio/img/print.png" alt="Print"/></button> -->     
     <button id="btn-zoom-in" class="btn" data-tooltip="Zoom In"><img src="/nopen/frontend/equipment-studio/img/zoomin.png" alt="Zoom in"/></button>
     <button id="btn-zoom-out" class="btn" data-tooltip="Zoom Out"><img src="/nopen/frontend/equipment-studio/img/zoomout.png" alt="Zoom out"/></button>
     <div class="panel">
       <span id="zoom-level">100</span>
       <span>%</span>
     </div>
     <button id="btn-zoom-to-fit" class="btn" data-tooltip="Zoom To Fit"><img src="/nopen/frontend/equipment-studio/img/zoomtofit.png" alt="Zoom To Fit"/></button>
<!-- <button id="btn-fullscreen" class="btn" data-tooltip="Toggle Fullscreen Mode"><img src="/nopen/frontend/equipment-studio/img/fullscreen.png" alt="Fullscreen"/></button> -->
     <button id="btn-to-front" class="btn" data-tooltip="Bring Object to Front">to front</button>
     <button id="btn-to-back" class="btn" data-tooltip="Send Object to Back">to back</button>
<!-- <button id="btn-layout" class="btn" data-tooltip="Auto-layout Graph">layout</button> -->
<!--     <label data-tooltip="Change Grid Size">Grid size:</label>
     <input type="range" value="10" min="1" max="50" step="1" id="input-gridsize" />
     <output id="output-gridsize">10</output> -->
     <label data-tooltip="Enable/Disable Snaplines">Snaplines:</label>
     <input type="checkbox" id="snapline-switch" checked/>
 </div>
 <div class="stencil-container">
     <label>Stencil</label>
     <button class="btn-expand" title="Expand all">+</button>
     <button class="btn-collapse" title="Collapse all">-</button>
 </div>
 <div class="paper-container"></div>
 <div class="inspector-container"></div>
 <div class="navigator-container"></div>
 <div class="statusbar-container"><span class="rt-colab"></span></div>