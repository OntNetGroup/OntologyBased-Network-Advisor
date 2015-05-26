package br.com.padtec.nopen.studio.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import br.com.padtec.common.dto.CardinalityDef;
import br.com.padtec.common.dto.RelationDef;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class BuildBindStructure {
	private OKCoUploader repository = StudioComponents.studioRepository ;
	private static BuildBindStructure instance = new BuildBindStructure();
	
	private static HashMap<String, String> bindsTuple = new HashMap<String,String>();
	
	public static BuildBindStructure getInstance(){
		return instance;
	}
	
	public static void createBindStructure(String relationURI){
		//Create a mapping with all bind relations of the model and their cardinalities
		HashMap<String, RelationDef> possibleInstants = new HashMap<String, RelationDef>();
		possibleInstants = DtoQueryUtil.getPossibleInstantiationsOfRelation(instance.repository.getBaseModel(), relationURI);
		
		Iterator<Entry<String, RelationDef>> it = possibleInstants.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        RelationDef relation = (RelationDef) pairs.getValue();
	        CardinalityDef card = relation.getCardOnRange();
	        Integer valueUp = card.getUpperBound();
	        BuildBindStructure.bindsTuple.put((String) pairs.getKey(), Integer.toString(valueUp));
	    }
	}

	public OKCoUploader getRepository() {
		return repository;
	}

	public void setRepository(OKCoUploader repository) {
		this.repository = repository;
	}

	public HashMap<String, String> getBindsTuple() {
		return bindsTuple;
	}

	public void setBindsTuple(HashMap<String, String> bindsTuple) {
		BuildBindStructure.bindsTuple = bindsTuple;
	}
}
