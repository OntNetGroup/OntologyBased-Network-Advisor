package br.com.padtec.nopen.service;

import java.util.HashMap;

import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

public final class ModelStructureAccessor {
	private static HashMap<String, String> relations_mapping = new HashMap<String, String>();
	private static final ModelStructureAccessor instance = new ModelStructureAccessor();

	private ModelStructureAccessor(){
		
	}

	public static ModelStructureAccessor getInstance(){
		return instance;
	}

	public static HashMap<String, String> getRelationsMapping() {
		return relations_mapping;
	}
	
	public static void initContainerStructure(String container, OKCoUploader repository){
		relations_mapping = ModelStructureAccessorController.buildContainerStructure(container, repository);
	}

}
