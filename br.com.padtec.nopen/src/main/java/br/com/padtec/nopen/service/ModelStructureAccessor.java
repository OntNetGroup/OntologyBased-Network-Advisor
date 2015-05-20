package br.com.padtec.nopen.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.MaxCardinalityRestriction;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;

import br.com.padtec.advisor.core.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class ModelStructureAccessor {

	
	public static ArrayList<String[]> buildContainerStructure(String container, OKCoUploader studioRepository){
		NOpenReasoner.runInference(true);
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
		
		return mapping;
	}
}
