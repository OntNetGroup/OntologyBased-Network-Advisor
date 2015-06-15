package br.com.padtec.nopen.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;





import br.com.padtec.common.dto.CardinalityDef;
import br.com.padtec.common.dto.RelationDef;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.studio.service.StudioComponents;
import br.com.padtec.okco.core.application.OKCoUploader;

public class ContainerStructure {
	
	private OKCoUploader repository = StudioComponents.studioRepository ;
	private static ContainerStructure instance = new ContainerStructure();
	
	private static HashMap<String,String> containerStructure = new HashMap<String,String>();

	public static HashMap<String, String> getContainerStructure() {
		return containerStructure;
	}

	public static void setContainerStructure(
			HashMap<String, String> containerStructure) {
		ContainerStructure.containerStructure = containerStructure;
	}

	public OKCoUploader getRepository() {
		return repository;
	}

	public static ContainerStructure getInstance() {
		return instance;
	}
	
	public static boolean verifyContainerRelation(String sourceURI, String tipo_source, String targetURI, String tipo_target) throws Exception{
		String propertyURI = instance.repository.getNamespace() + RelationEnum.componentOf.toString();			
		Integer numberOfRelations = QueryUtil.getNumberOfOccurrences(instance.getRepository().getBaseModel(), sourceURI, propertyURI, tipo_target );
		
		String key = tipo_source + propertyURI + tipo_target;
		String cardinality = ContainerStructure.getContainerStructure().get(key);
		if(cardinality != null ){
			Integer cardinality_target = Integer.parseInt(cardinality);
			if( ((numberOfRelations < cardinality_target) || (cardinality_target == -1)) ){
				return true;
			}
		}
		
		return false;
	}
	
	public static void buildContainerStructure(String relationURI){
		//Create a mapping with all componentOf relations of the model and their cardinalities
		HashMap<String, RelationDef> possibleInstants = new HashMap<String, RelationDef>();
		possibleInstants = DtoQueryUtil.getPossibleInstantiationsOfRelation(instance.getRepository().getBaseModel(), relationURI);
		
		Iterator<Entry<String, RelationDef>> it = possibleInstants.entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        RelationDef relation = (RelationDef) pairs.getValue();
	        CardinalityDef card = relation.getCardOnRange(); //lembrete:cardinalidades invertidas
	        Integer valueUp = card.getUpperBound();
	        System.out.println("----------------------------------");
	        System.out.println("CHAVE -> " + (String) pairs.getKey());
	        System.out.println("CARDINALIDADE NO DOMAIN (UPPER BOUND) -> " + Integer.toString(valueUp));
	        System.out.println("CARDINALIDADE NO DOMAIN (LOWER BOUND) -> " + Integer.toString(card.getLowerBound()));
	        System.out.println("DOMAIN CLASS -> " + card.getDomainClass());
	        System.out.println("DOMAIN PROPERTY -> " + card.getObjectProperty());
	        System.out.println("DOMAIN RANGE TYPE -> " + card.getRangeType());
	        System.out.println("CARDINALIDADE NO RANGE (UPPER BOUND) -> " + Integer.toString(relation.getCardOnDomain().getUpperBound()));
	        System.out.println("CARDINALIDADE NO RANGE (LOWER BOUND) -> " + Integer.toString(relation.getCardOnDomain().getLowerBound()));
	        
	        ContainerStructure.containerStructure.put((String) pairs.getKey(), Integer.toString(valueUp));
	    }
	}
}
