package br.com.padtec.nopen.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import br.com.padtec.advisor.core.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class ModelStructureAccessorController {

	
	public static HashMap<String, String> buildContainerStructure(String container, OKCoUploader studioRepository){
		HashMap<String, String> mapping = new HashMap<String, String>();
		List<String> tipoContainer = QueryUtil.getClassesURIFromIndividual(studioRepository.getBaseModel(), studioRepository.getNamespace()+container);
		String tipo = tipoContainer.get(0);
		HashSet<String> relations = new HashSet<String>();
		relations = NOpenQueryUtil.getAllComponentOFRelations(tipo, studioRepository.getBaseModel()); 
		//precisa atualizar com os filhos, caso haja, recursivamente
		boolean ok = false;
		for(String relation : relations){
			List<String[]> cardinality = null;
			List<String[]> MaxCardinality = QueryUtil.getMaxCardinalityDefinitions(studioRepository.getBaseModel(), studioRepository.getNamespace()+tipo);
			List<String[]> MaxExactCardinality = QueryUtil.getExactlyCardinalityDefinitions(studioRepository.getBaseModel(), studioRepository.getNamespace()+tipo);
			ok = cardinality.addAll(MaxCardinality);
			ok = cardinality.addAll(MaxExactCardinality);
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
