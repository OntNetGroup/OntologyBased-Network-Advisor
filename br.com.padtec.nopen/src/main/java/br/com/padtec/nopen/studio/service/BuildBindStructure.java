package br.com.padtec.nopen.studio.service;

import java.util.HashMap;

import br.com.padtec.okco.core.application.OKCoUploader;

public class BuildBindStructure {
	private OKCoUploader repository = StudioComponents.studioRepository ;
	private static BuildBindStructure instance = new BuildBindStructure();
	
	private HashMap<String, String> bindsTuple = new HashMap<String,String>();
	
	public static BuildBindStructure getInstance(){
		return instance;
	}
	
	public static void createBindStructure(){
		
	}
}
