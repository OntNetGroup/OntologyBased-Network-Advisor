package br.com.padtec.nopen.studio.service;

import java.util.HashMap;

import br.com.padtec.nopen.service.NOpenReasoner;
import br.com.padtec.okco.core.application.OKCoUploader;

public class BuildBindStructure {
	private OKCoUploader repository = StudioComponents.studioRepository ;
	private static BuildBindStructure instance = new BuildBindStructure();
	
	private HashMap<String, String> bindsTuple = new HashMap<String,String>();
	
	public static BuildBindStructure getInstance(){
		return instance;
	}
	
	public static void createBindStructure(String relation){
		//Create a mapping with all bind relations of the model and their cardinalities
		NOpenReasoner.runInference(true);
		
		/*
		 * 
		ArrayList<String[]> mapping = new ArrayList<String[]>();
		//primeira coisa: pegar o subtipo da classe e colocar numa lista de coisas, mais especificos possiveis
		//ver se essas coisas tem componentOf
		//segunda:se os subtipos dele tem componentOf e assim sucessivamente
		//container é classe
		//pegar as subclasses da container
		//se basear na classe especifica
		
		ArrayList<String> subclass = new ArrayList<String>();
		subclass = QueryUtil.SubClass(studioRepository.getBaseModel(), container);
			
		for(String sub : subclass){
			HashSet<String> ranges = new HashSet<String>();
			//obtendo relações de componentOf
			//ranges = NOpenQueryUtil.getAllComponentOFRelations(sub.substring(sub.indexOf("#")+1), studioRepository.getBaseModel());
			String domain, relation;
			domain = sub;
			relation = "componentOf";
			for(String range : ranges){
				String tripla[] = new String[3];
				tripla[0] = domain;
				tripla[1] = relation;
				tripla[2] = range;
				boolean ok = false;
				ok = mapping.add(tripla);
				mapping = buildContainerStructure(range, studioRepository);
			}
			
			
		}
		 * */
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
		this.bindsTuple = bindsTuple;
	}
}
