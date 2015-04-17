package br.com.padtec.nopen.service;

import java.util.HashMap;
import br.com.padtec.nopen.studio.service.StudioComponents;

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
	
	public static void initContainerStructure(String container){
		relations_mapping = ModelStructureAccessorController.buildContainerStructure(container, StudioComponents.studioRepository);
	}

}
