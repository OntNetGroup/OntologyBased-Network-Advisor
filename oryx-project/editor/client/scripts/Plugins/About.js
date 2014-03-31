if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.About = Clazz.extend({
        
  // Defines the facade
  facade                : undefined,
    
  // Defines the undo/redo Stack
  undoStack     : [],
  redoStack     : [],
        
  // Constructor 
  construct: function(facade){
    
    this.facade = facade;     
                
    // Offers the functionality of undo                
    this.facade.offer({
     name              : ORYX.I18N.About.name,
     description       : ORYX.I18N.About.desc,
	 'functionality': this.openAbout.bind(this),
     icon              : ORYX.PATH + "images/information.png",
      
     group             : ORYX.I18N.About.group,
     index             : 0
                      }); 
                
    // Register on event for executing commands-->store all commands in a stack          
    this.facade.registerOnEvent(ORYX.CONFIG.EVENT_EXECUTE_COMMANDS,
      this.handleExecuteCommands.bind(this) );
        
  },
  
  openAbout: function(){
        
        // Create the panel
        var dialog = new Ext.Window({
            autoCreate: true,
            layout: 'fit',
            plain: true,
            bodyStyle: 'padding:5px;',
            title: "About",
            height: 350,
            width: 500,
            modal: true,
            fixedcenter: true,
            shadow: true,
            proxyDrag: true,
            resizable: true,
			items: [{html: "OntoUML Web Editor - beta!"}],
        });
        
        // Show the panel
        dialog.show();
    },
        
  handleExecuteCommands: function( evt ){
                
    //...
                
  },
        
  doUndo: function(){
        
    //...
        
  },
        
  doRedo: function(){
                
    //...
  }
        
});