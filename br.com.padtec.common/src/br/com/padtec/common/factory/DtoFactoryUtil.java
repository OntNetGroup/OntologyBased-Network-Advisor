package br.com.padtec.common.factory;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DataPropertyValue;
import br.com.padtec.common.dto.DtoCompleteClass;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.dto.DtoPropertyAndSubProperties;
import br.com.padtec.common.queries.QueryUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class DtoFactoryUtil {

	static public OntModel createIndividual(OntModel model, DtoInstance newIndividual, String srcIndividualURI, String relationURI, String rangeClassURI)
	{	
		newIndividual.existInModel=true;
		newIndividual.name = newIndividual.name.trim().replaceAll(" ","");
		return FactoryUtil.createIndividual(model, newIndividual.ns+newIndividual.name, newIndividual.ListSameInstances, newIndividual.ListDiferentInstances, srcIndividualURI, relationURI, rangeClassURI);
	}
	
	static public OntModel createIndividual(OntModel model, DtoInstance newIndividual, String srcIndividualURI, DtoDefinitionClass dtoDefinitionClass)
	{	
		newIndividual.existInModel=true;
		newIndividual.name = newIndividual.name.trim().replaceAll(" ","");
		return FactoryUtil.createIndividual(model, newIndividual.ns+newIndividual.name, newIndividual.ListSameInstances, newIndividual.ListDiferentInstances, srcIndividualURI, dtoDefinitionClass.Relation, dtoDefinitionClass.Target);
	}
	
	static public OntModel createIndividuals(OntModel model, List<DtoInstance> newIndividualList, String srcIndividualURI, String relationURI, String rangeClassURI)	
	{		
		for (DtoInstance newIndividual : newIndividualList) 
		{
			if(newIndividual.existInModel == false)
			{
				newIndividual.existInModel=true;
				model = createIndividual(model,newIndividual,srcIndividualURI, relationURI, rangeClassURI);
			}
		}		
		return model;		
	}
	
	static public OntModel updateIndividual(OntModel model, DtoInstance individual)
	{	
		return FactoryUtil.updateIndividual(model, individual.ns+individual.name, individual.ListSameInstances, individual.ListDiferentInstances);
	}
	
	static public OntModel deleteIndividual(OntModel model, DtoInstance individual)
	{
		return FactoryUtil.deleteIndividual(model, individual.ns+individual.name);
	}
	
	static public List<DtoInstance> intersection(List<DtoInstance> individualsList, List<String> individualsURIs) 
	{
		List<DtoInstance> list = new ArrayList<DtoInstance>();		
		for (String iName : individualsURIs) 
		{			
			for (DtoInstance instance : individualsList) 
			{				
				if(iName.equals(instance.ns + instance.name))
				{
					list.add(instance);
					break;
				}				
			}
		}		
		return list;
	}
	
	static public void removeIndividualFrom(List<DtoInstance> individualsList, String individualURI) {

		for (DtoInstance instance : individualsList) {
			
			if(instance.uri.equals(individualURI))
			{
				individualsList.remove(instance);
				break;
			}
		}		
	}
	
	static public void removeDataValueFrom(List<DataPropertyValue> dataValuesList, String individualURI) 
	{
		for (DataPropertyValue data : dataValuesList) 
		{			
			if(data.classValue.equals(individualURI))
			{
				dataValuesList.remove(data);
				break;
			}
		}		
	}
	
	static public DtoInstance getIndividualFrom(List<DtoInstance> individualsList, String individualURI) {		
		
		for (DtoInstance instance : individualsList) 
		{
			if((instance.ns + instance.name).equals(individualURI)) return instance;
		}		
		return null;
	}

	static public DtoPropertyAndSubProperties getPropertyFrom(List<DtoPropertyAndSubProperties> propertiesList, String propertyURI) 
	{
		for (DtoPropertyAndSubProperties dto : propertiesList) 
		{
			if(propertyURI.contains(dto.Property)) return dto;
			//if(dto.Property.equals(propertyURI)) return dto;
		}		
		return null;
	}
	
	static public DtoDefinitionClass getDefinitionFrom(List<DtoDefinitionClass> relationsList, String relationURI)
	{		
		for (DtoDefinitionClass aux : relationsList) 
		{	
			if(aux.Relation.equals(relationURI)) return aux;
		}
		return null;
	}
	
	static public void createAndClassifyIndividualAutomatically(OntModel model, InfModel inferredModel, DtoInstance dtoIndividual) 
	{		
		// Check if the subclasses are disjoint and complete
		for (DtoCompleteClass dto : dtoIndividual.ListCompleteClasses) 
		{
			boolean isDisjoint = true;
			for (String subCls : dto.Members)
			{
				for (String subCls2 : dto.Members) 
				{
					if(!subCls.equals(subCls2))
					{
						boolean result = QueryUtil.isClassesURIDisjoint(inferredModel, subCls, subCls2);
						if(result == true) isDisjoint = false;
					}
				}				
				if(isDisjoint == false) break;
			}			
			if(isDisjoint == true && dto.Members.size() > 0)
			{
				//Classify randomly
				FactoryUtil.createIndividualOfClass(model, dtoIndividual.ns + dtoIndividual.name, dto.Members.get(0));
			}
		}
	}
	
	public static void createIndividual(OntModel model, DtoInstance individual){
		setSuperClassesOfIndividual(model, individual);
		
		for (String classURI : individual.getListClasses()) {
			FactoryUtil.createIndividual(model, individual.uri, classURI);
		}
		
		setSuperRelationsOfIndividual(model, individual, true);
		setSuperRelationsOfIndividual(model, individual, false);
		setInverseRelationsOfIndividual(model, individual, true);
		setInverseRelationsOfIndividual(model, individual, false);
	}
	
	/**
	 * Set all relations' inverses from an individual. 
	 * E.g., if R1 is inverse of R2 and R2(x,y), this function asserts R1(y,x).
	 * 
	 * @param model: OntModel
	 * @param individual: DtoInstance
	 * @param asSource: assert in relations where the individual is the source
	 * 
	 * @author Freddy Brasileiro
	 */
	public static void setInverseRelationsOfIndividual(OntModel model, DtoInstance individual, boolean asSource){
		List<DtoInstanceRelation> relationsToRefresh;
		List<DtoInstanceRelation> relationsToVerify;
		if(asSource){
			relationsToVerify = individual.getRelationsAsSource();
			relationsToRefresh = individual.getRelationsAsTarget();			
		}else{
			relationsToVerify = individual.getRelationsAsTarget();
			relationsToRefresh = individual.getRelationsAsSource();
			
		}
		
		//search for all relation's (as source) inverses
		ArrayList<DtoInstanceRelation> newRelations = new ArrayList<DtoInstanceRelation>();
		for (DtoInstanceRelation relation : relationsToVerify) {
			List<String> inverses = QueryUtil.getAllInverseOfURIs(model, relation.Property);
			
			for (String inverse : inverses) {
				DtoInstanceRelation newRelation = new DtoInstanceRelation(relation.Target, inverse, relation.Source);
				
				if(!newRelations.contains(newRelation) && !relationsToRefresh.contains(newRelation)){
					newRelations.add(newRelation);
				}
			}			 
		}
		
		relationsToRefresh.addAll(newRelations);	
	}
	
	/**
	 * Set all super relations from an individual. 
	 * E.g., if R1 is super relation of R2 and R2(x,y), this function asserts R1(x,y).
	 * 
	 * @param model: OntModel
	 * @param individual: DtoInstance
	 * @param asSource: assert in relations where the individual is the source
	 * 
	 * @author Freddy Brasileiro
	 */
	public static void setSuperRelationsOfIndividual(OntModel model, DtoInstance individual, boolean asSource){
		List<DtoInstanceRelation> relationsToRefresh;
		if(asSource){
			relationsToRefresh = individual.getRelationsAsSource();
		}else{
			relationsToRefresh = individual.getRelationsAsTarget();
		}
		
		//search for all super relations
		ArrayList<DtoInstanceRelation> newSuperRelations = new ArrayList<DtoInstanceRelation>();
		for (DtoInstanceRelation relation : relationsToRefresh) {
			List<String> superRelations = QueryUtil.getAllSuperObjectProperties(model, relation.Property);
			
			for (String superRel : superRelations) {
				DtoInstanceRelation newSuperRelation = new DtoInstanceRelation(relation.Source, superRel, relation.Target);
				
				if(!newSuperRelations.contains(newSuperRelation) && !relationsToRefresh.contains(newSuperRelation)){
					newSuperRelations.add(newSuperRelation);
				}
			}
		}
		relationsToRefresh.addAll(newSuperRelations);
	}
	
	/**
	 * Set all super classes from an individual. 
	 * E.g., if A is super type of B and an individual x is from B, this function asserts x as A.
	 * 
	 * @param model: OntModel
	 * @param individual: DtoInstance
	 * 
	 * @author Freddy Brasileiro
	 */
	public static void setSuperClassesOfIndividual(OntModel model, DtoInstance individual){
		//search for all super types of instance's classes 
		ArrayList<String> newTypes = new ArrayList<String>();
		for (String classURI : individual.getListClasses()) {
			List<String> superTypes = QueryUtil.getSupertypesURIs(model, classURI);
			
			for (String superType : superTypes) {
				superType = superType.replace(individual.ns, "");
				if(!newTypes.contains(superType) && !individual.getListClasses().contains(superType)){
					newTypes.add(superType);
				}
			}
		}
		individual.getListClasses().addAll(newTypes);
	}
}
