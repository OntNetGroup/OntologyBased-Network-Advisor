package br.com.padtec.nopen.service;

import java.util.HashMap;

import br.com.padtec.okco.core.application.OKCoUploader;

public class ModelStructureAccessor {
	HashMap<String, String> relations_mapping;

	public HashMap<String, String> getRelations_mapping() {
	return relations_mapping;
}

	public void setRelations_mapping(HashMap<String, String> relations_mapping) {
		this.relations_mapping = relations_mapping;
	}

	public void initContainerStructure(String container, OKCoUploader repository){
		
	}
}
