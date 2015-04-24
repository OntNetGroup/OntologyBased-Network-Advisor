package br.com.padtec.nopen.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import br.com.padtec.advisor.core.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class ModelStructureAccessor {

	
	public static HashMap<String, String> buildContainerStructure(String container, OKCoUploader studioRepository){
		NOpenReasoner.runInference(true);
		HashMap<String, String> mapping = new HashMap<String, String>();
		//obtendo classe do container
		List<String> tipoContainer = QueryUtil.getClassesURIFromIndividual(studioRepository.getBaseModel(), studioRepository.getNamespace()+container);
		System.out.println("--------------------");

		Iterator<String> i = tipoContainer.iterator();
		while(i.hasNext()){ //eliminando classes que tenham subclasse
			ArrayList<String> subclasses = new ArrayList<String>();
			subclasses = QueryUtil.SubClass(studioRepository.getBaseModel(), i.next().toString());
			
  			if(subclasses.size()>1){ //se só tem uma subclasse, então a subclasse é a própria classe
  				i.remove();
  			}
  		}
		String tipo = tipoContainer.get(0);
		System.out.println("--------------------");
		
		
		HashSet<String> relations = new HashSet<String>();
		//obtendo relações de componentOf
		relations = NOpenQueryUtil.getAllComponentOFRelations(tipo.substring(tipo.indexOf("#")+1), studioRepository.getBaseModel()); 
		//obtendo cardinalidades
		
		ArrayList<String[]> cardinality = new ArrayList<String[]>();
		//métodos problemáticos
		List<String[]> MaxCardinality = QueryUtil.getMaxCardinalityDefinitions(studioRepository.getBaseModel(), tipo);
		List<String[]> MaxExactCardinality = QueryUtil.getExactlyCardinalityDefinitions(studioRepository.getBaseModel(), tipo);
		
		boolean ok = false;
		ok = cardinality.addAll(MaxCardinality);
		ok = cardinality.addAll(MaxExactCardinality);
		
		
		for(String relation : relations){
			// Montando o mapeamento entre relações de componentOf de uma classe do container	
			for(String[] card : cardinality){
				//fazer uma busca no vetor pra procurar as relações entre o tipo e as relations que ja conheço pra pegar a cardinalidade
				if(card[0].equals(tipo) && card[1].equals(RelationEnum.COMPONENTOF.toString()) && card[3].equals(relation)){
					String tuple = tipo+"_"+relation;
					String c = card[2];
					mapping.put(tuple, c);
				}
			}
		}
		return mapping;
	}
}
