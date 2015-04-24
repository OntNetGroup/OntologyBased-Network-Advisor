package br.com.padtec.nopen.service;

import java.util.HashMap;

import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

public final class ModelStructureAccessorController {
	private static HashMap<String, String> relations_mapping = new HashMap<String, String>();
	private static final ModelStructureAccessorController instance = new ModelStructureAccessorController();

	private ModelStructureAccessorController(){
		
	}

	public static ModelStructureAccessorController getInstance(){
		return instance;
	}

	public static HashMap<String, String> getRelationsMapping() {
		return relations_mapping;
	}
	
	//fazer try/catch
	public static void initContainerStructure(String container, OKCoUploader repository) throws Exception {
		relations_mapping = ModelStructureAccessor.buildContainerStructure(container, repository);
	}

}
