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
	        CardinalityDef card = relation.getCardOnRange();
	        Integer valueUp = card.getUpperBound();
	        /*System.out.println("----------------------------");
	        System.out.println("CARDINALITY ON DOMAIN (lower bound) -> " + Integer.toString(card.getLowerBound()));
	        System.out.println("CARDINALITY ON DOMAIN (upper bound) -> " + Integer.toString(card.getUpperBound()));
	        System.out.println(" DOMAIN -> " + relation.getPossibleDomain());
	        System.out.println(" RANGE -> " + relation.getPossibleRange());
	        System.out.println("CARDINALITY ON RANGE (lower bound) -> " + Integer.toString(relation.getCardOnRange().getLowerBound()));
	        System.out.println("CARDINALITY ON RANGE (upper bound) -> " + Integer.toString(relation.getCardOnRange().getUpperBound()));
	        System.out.println();*/
	        ContainerStructure.containerStructure.put((String) pairs.getKey(), Integer.toString(valueUp));
	    }
	}
}
