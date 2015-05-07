package br.com.padtec.nopen.service;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

public final class ModelStructureAccessorController {
	private static ArrayList<String[]> relations_mapping = new ArrayList<String[]>();
	private static final ModelStructureAccessorController instance = new ModelStructureAccessorController();

	private ModelStructureAccessorController(){
		
	}

	public static ModelStructureAccessorController getInstance(){
		return instance;
	}

	public static ArrayList<String[]> getRelationsMapping() {
		return relations_mapping;
	}
	
	//fazer try/catch
	public static void initContainerStructure(String container, OKCoUploader repository) throws Exception {
		//relations_mapping = ModelStructureAccessor.buildContainerStructure(container, repository);
	}

}
