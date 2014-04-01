/**
 * Copyright (c) 2009
 * Kai Schlichting
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

/**
 * Enables exporting and importing current model in JSON.
 */
ORYX.Plugins.JSONSupport = ORYX.Plugins.AbstractPlugin.extend({
    construct: function(){
        // Call super class constructor
        arguments.callee.$.construct.apply(this, arguments);
        
        this.facade.offer({
            'name': ORYX.I18N.JSONSupport.exp.name,
            'functionality': this.exportJSON.bind(this),
            'group': ORYX.I18N.JSONSupport.exp.group,
            dropDownGroupIcon: ORYX.PATH + "images/disk.png",
			'icon': ORYX.PATH + "images/page_white_javascript.png",
            'description': ORYX.I18N.JSONSupport.exp.desc,
            'index': 1,
            'minShape': 0,
            'maxShape': 0
        });
        
        this.facade.offer({
            'name': ORYX.I18N.JSONSupport.imp.name,
            'functionality': this.showImportDialog.bind(this),
            'group': ORYX.I18N.JSONSupport.imp.group,
            'icon': ORYX.PATH + "images/folder_page.png",
            'description': ORYX.I18N.JSONSupport.imp.desc,
            'index': 1,
            'minShape': 0,
            'maxShape': 0
        });
    },
    
    exportJSON: function(){
        var json = this.facade.getSerializedJSON();
		
			var name;
		
			name = prompt("Filename");
			
			if(name==null)
			  return;
			if(name=="")
				name="default";
		
        this.openDownloadWindow(name + ".json", json);
    },
    
    /**
     * Opens a upload dialog.
     *
     */
    showImportDialog: function(successCallback){
    
        var form = new Ext.form.FormPanel({
            baseCls: 'x-plain',
            labelWidth: 50,
            defaultType: 'textfield',
            items: [{
                text: ORYX.I18N.JSONSupport.imp.selectFile,
                style: 'font-size:12px;margin-bottom:10px;display:block;',
                anchor: '100%',
                xtype: 'label'
            }, {
                fieldLabel: ORYX.I18N.JSONSupport.imp.file,
                name: 'subject',
                inputType: 'file',
                style: 'margin-bottom:10px;display:block;',
                itemCls: 'ext_specific_window_overflow'
            }, {
                xtype: 'textarea',
                hideLabel: true,
                name: 'msg',
                anchor: '100% -63'
            }]
        });
        
        // Create the panel
        var dialog = new Ext.Window({
            autoCreate: true,
            layout: 'fit',
            plain: true,
            bodyStyle: 'padding:5px;',
            title: ORYX.I18N.JSONSupport.imp.name,
            height: 350,
            width: 500,
            modal: true,
            fixedcenter: true,
            shadow: true,
            proxyDrag: true,
            resizable: true,
            items: [form],
            buttons: [{
                text: ORYX.I18N.JSONSupport.imp.btnImp,
                handler: function(){
                
                    var loadMask = new Ext.LoadMask(Ext.getBody(), {
                        msg: ORYX.I18N.JSONSupport.imp.progress
                    });
                    loadMask.show();
                    
                    window.setTimeout(function(){
                        var json = form.items.items[2].getValue();
                        try {
                            this.facade.importJSON(json, true);
                            dialog.close();
                        } 
                        catch (error) {
                            Ext.Msg.alert(ORYX.I18N.JSONSupport.imp.syntaxError, error.message);
                        }
                        finally {
                            loadMask.hide();
                        }
                    }.bind(this), 100);
                    
                }.bind(this)
            }, {
                text: ORYX.I18N.JSONSupport.imp.btnClose,
                handler: function(){
                    dialog.close();
                }.bind(this)
            }]
        });
        
        // Show the panel
        dialog.show();
		
// Adds the change event handler to 
        form.items.items[1].getEl().dom.addEventListener('change', function(evt){
           
		var f = evt.target.files[0]; 
		
		var r = new FileReader();
		r.onload = function(e){
		 var contents = e.target.result;
		 form.items.items[2].setValue(contents); //setValue parameter needs to be a String
		}
		r.readAsText(f);     
		}, true)
        
    }
    
});
