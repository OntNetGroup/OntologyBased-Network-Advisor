package br.com.padtec.nopen.studio.service;

import br.com.padtec.okco.core.application.OKCoUploader;



public class PerformBind {
	private OKCoUploader repository = StudioComponents.studioRepository ;
	private static PerformBind instance=new PerformBind();
	
	public static PerformBind getInstance(){
		return instance;
	}
	
	
	/*
	 * this method apply binds between tfs and between ports and tfs. 
	 * creates an output for the id_source 
	 * creates an input for the id_target and the component of 
	 * discover the rp for the binds and the component of
	 * 
	 */
	public boolean applyBinds(String id_source, String name_source, String id_target, String name_target, String tipo_source, String tipo_target){
		
		return true;
	}

	public OKCoUploader getRepository() {
		return repository;
	}

	public void setRepository(OKCoUploader repository) {
		this.repository = repository;
	}
	
	/*
	 * given two ports discover the rp between them.
	 */
	private String discoverRPBetweenPorts(String uri_type_output, String uri_type_input){
		
		
		return null;
	}
	
}
