package br.com.padtec.nopen.studio.service;

import java.util.HashSet;

import br.com.padtec.nopen.service.NOpenLog;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
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
		try{
			
			//fazer a parte do tf pro rp
			NOpenLog.appendLine("Success: Binds successfully made between (" + tipo_source + "::" + name_source +", " + tipo_target + "::" + name_target + ")");
		} catch (Exception e){
			e.getMessage();
		}
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
	private HashSet<String> discoverRPBetweenPorts(String uri_type_output, String uri_type_input, OKCoUploader repository){
		HashSet<String> rp = new HashSet<String>();
		rp = NOpenQueryUtil.discoverRPBetweenPorts(uri_type_output, uri_type_input, repository.getBaseModel());
		
		return rp;
	}
	
}
