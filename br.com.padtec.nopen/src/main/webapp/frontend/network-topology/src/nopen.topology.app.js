nopen.topology.App = Backbone.View.extend({
	
	file : undefined,
	model : undefined,
	util : undefined,
	exporter : undefined,
	importer : undefined,
	
	initialize : function(){
		console.log("Provisioning started!");
	},
	
	start : function(app) {
	
		//create file
		this.file = new nopen.topology.File;
		//create model
		this.model = new nopen.topology.Model;
		//create util
		this.util = new nopen.topology.Util;
		//create exporter
		this.exporter = new nopen.topology.Exporter;
		//create importer
		this.importer = new nopen.topology.Importer;
		
		//set app
		this.file.setApp(this);
		this.util.setApp(this);
		this.model.setApp(this);
		this.exporter.setApp(this);
		this.importer.setApp(this);
		
		this.initializeTopologyFileProcedures(app);
		this.initializeTopologyExporterProcedures(app);
		this.initializeTopologyImporterProcedures(app);
		this.initializeTopologyGraphProcedures(app);
		this.initializeTopologyPaperProcedures(app);
		
	},
	
	initializeTopologyFileProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		
		if(file.getUrlParameter('topology')){
        	var filename = file.getUrlParameter('topology');
        	file.openTopologyFromURL(graph, filename);
        }
		
		$('#btn-open').click(function(){	
        	file.generateOpenTopologyDialog(graph);      	
        });
		
		$('#btn-save').click(function(){
			//All equipments need to be matched
         	file.generateSaveTopologyDialog(app);
         });
		
	},
	
	initializeTopologyExporterProcedures : function(app) {
		
		var graph = app.graph;
		var exporter = this.exporter
		
		$('#btn-pre').click(function(){
			exporter.previewTopology(graph);
         });
        
         $('#btn-export-xml').click(function(){
        	 exporter.exportTopology(graph);
         });
        
         $('#btn-export-yang').click(function(){
        	 exporter.exportTopologyAsYANG(graph);
         });
		
	},
	
	initializeTopologyImporterProcedures : function(app) {
		
		var graph = app.graph;
		var importer = this.importer
		
		$('#btn-import-xml').click(function(){
         	$('#file').click();
		});

         $('#file').change(function(){
         	importer.importTopology(graph);
         });
		
	},
	
	initializeTopologyGraphProcedures : function(app) {
		
		var graph = app.graph;
		var file = this.file;
		var model = this.model;
		var util = this.util;
		
		graph.on('add', function(cell) {
			
			//Add Node
			if(cell.get('subtype') == "Node") {
				
				//generate match equipment to node dialog
				model.generetaMatchEquipmentToNodeDialog(cell);
				
			}
			
		});
		
	},
	
	initializeTopologyPaperProcedures : function(app) {
		
		var paper = app.paper;
		var file = this.file;
		var model = this.model;
		
		paper.on('cell:pointerup', function(cellView, evt) {

            if (cellView.model instanceof joint.dia.Link || app.selection.contains(cellView.model)) return;

            var halo = new joint.ui.Halo({ graph: app.graph, paper: app.paper, cellView: cellView });

            halo.addHandle({ name: 'setting', position: 'ne', icon: '/nopen/frontend/network-topology/img/setting.png' });
            halo.on('action:setting:pointerdown', function(evt) {
            	
            	//generate match equipment to node dialog
            	model.generetaMatchEquipmentToNodeDialog(cellView.model);
            	
                evt.stopPropagation();
                //alert('My custom action.');
            });
            
            halo.removeHandle('fork');
            halo.removeHandle('clone');
            halo.removeHandle('rotate');
            //halo.changeHandle('clone', { position: 'se' });
            
            //freetransform.render();
            halo.render();

            app.initializeHaloTooltips(halo);

            app.createInspector(cellView);

            app.selectionView.cancelSelection();
            app.selection.reset([cellView.model]);
            
		});
		
	},
	
});