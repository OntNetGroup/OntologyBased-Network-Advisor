package br.com.padtec.common.queries;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.application.UploadApp;
import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.exceptions.OKCoNameSpaceException;
import br.com.padtec.common.types.OntCardinalityEnum;

import com.hp.hpl.jena.rdf.model.InfModel;

public class DtoQueryUtil {

	/** 
	 * Return the list of all individuals from the ontology.
	 * It returns also all the classes of an individual as well as all the other individuals different and the same as this one.
	 *  
	 * @throws OKCoNameSpaceException
	 * 
	 * @author John Guerson
	 */
	static public List<DtoInstance> getIndividuals(InfModel model) throws OKCoNameSpaceException 
	{		
		List<DtoInstance> result = new ArrayList<DtoInstance>();				
		List<String> individualsURIList = QueryUtil.getIndividualsURIFromAllClasses(model);
    	for (String indivURI : individualsURIList)
    	{    		
    		if(!indivURI.contains("#")){ throw new OKCoNameSpaceException("Entity namespace problem. The " + indivURI +" have to followed by \"#\"."); }
    		List<String> classesURIList = QueryUtil.getClassesURI(model, indivURI);
    		List<String> diffURIList = QueryUtil.getIndividualsURIDifferentFrom(model, indivURI);
    		List<String> sameAsURIList = QueryUtil.getIndividualsURISameAs(model, indivURI);
    		String nameSpace = indivURI.split("#")[0] + "#";
    		String name = indivURI.split("#")[1];
    		result.add(new DtoInstance(nameSpace, name, classesURIList, diffURIList, sameAsURIList, true));
		}		
		return result;
	}
	
	/** 
	 * Return the a particular individual from the ontology.
	 * It returns also all the classes of an individual as well as all the other individuals different and the same as this one.
 	 *
	 * @author John Guerson
	 */
	static public DtoInstance getIndividual(InfModel model, String individualURI)
	{		
		if(!individualURI.contains("#")){ throw new OKCoNameSpaceException("Entity namespace problem. The " + individualURI +" have to followed by \"#\"."); }
		List<String> classesURIList = QueryUtil.getClassesURI(model, individualURI);
		List<String> diffURIList = QueryUtil.getIndividualsURIDifferentFrom(model, individualURI);
		List<String> sameAsURIList = QueryUtil.getIndividualsURISameAs(model, individualURI);
		String nameSpace =  individualURI.split("#")[0] + "#";
		String name =  individualURI.split("#")[1];
		return new DtoInstance(nameSpace, name, classesURIList, diffURIList, sameAsURIList, true);
	}
	
	/** 
	 * Return all the relations of a particular individual from the ontology.
	 * It returns also the first range class of the relations.
	 * 
	 * @author John Guerson
	 */
	static public List<DtoInstanceRelation> getRelations(InfModel model, String individualURI)
	{		
		List<DtoInstanceRelation> result = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = QueryUtil.getPropertiesURI(model, individualURI);
		for(String propertyURI: propertiesURIList)
		{
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;
		    List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);
		    if(ranges.size()>0) dtoItem.Target = ranges.get(0);
		    else dtoItem.Target = "";
		    result.add(dtoItem);
		}
		return result;
	}
		
	/** 
	 * Return all the useful information of a every class from the ontology.
	 * It includes information about Some, Max, Min and Exact cardinalities.
	 * 
	 * @author John Guerson
	 */
	static public List<DtoDefinitionClass> getClassDefinitions(InfModel InfModel) 
	{		
		List<DtoInstance> listAllInstances = getIndividuals(InfModel);
 		System.out.println("\nSearch: Getting model definitions in instances()...");
		ArrayList<DtoDefinitionClass> resultListDefinitions = new ArrayList<DtoDefinitionClass>();		
		for (DtoInstance instance : listAllInstances) 
		{
			for (String cls : instance.ListClasses) 
			{
				//DtoDefinitionClass aux = DtoDefinitionClass.getDtoWithSourceAndRelationAndTarget(resultListDefinitions, cls);
				DtoDefinitionClass aux = null;
				if(aux == null && ! cls.contains("Thing"))	//doesn't exist yet
				{
					//relations URI some values
					ArrayList<DtoDefinitionClass> dtoSomeRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: QueryUtil.getTuplesSomeValuesFrom(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Target = triple[2];
						dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = OntCardinalityEnum.SOME;
					}
					
					//relations URI minimum cardinality values
					ArrayList<DtoDefinitionClass> dtoMinRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: QueryUtil.getTuplesMinQualifiedCardinality(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Cardinality = triple[2];
						dto.Target = triple[3];
						dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = OntCardinalityEnum.MIN;
					}
					
					//relations URI maximum cardinality values
					ArrayList<DtoDefinitionClass> dtoMaxRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: QueryUtil.getTuplesMaxQualifiedCardinality(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Cardinality = triple[2];
						dto.Target = triple[3];
						dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = OntCardinalityEnum.MAX;
					}	
					
					//relations URI exact cardinality values
					ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: QueryUtil.getTuplesQualifiedCardinality(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Cardinality = triple[2];
						dto.Target = triple[3];
						dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = OntCardinalityEnum.EXACTLY;
					}											
					resultListDefinitions.addAll(dtoSomeRelationsList);
					resultListDefinitions.addAll(dtoMinRelationsList);
					resultListDefinitions.addAll(dtoMaxRelationsList);
					resultListDefinitions.addAll(dtoExactlyRelationsList);
				}							
			}					
		}		
		return resultListDefinitions;
	}	
	
	/** 
	 * Return all the useful information of an individual's class from the ontology.
	 * It includes information about Some, Max, Min and Exact cardinalities.
	 * 
	 * @author John Guerson
	 */
	static public List<DtoDefinitionClass> getClassDefinitions(InfModel InfModel, String instanceURI) 
 	{ 		
		DtoInstance Instance = DtoQueryUtil.getIndividual(InfModel, instanceURI);
		List<String> listInstancesDto = QueryUtil.getIndividualsURIFromAllClasses(InfModel);		
		for (String dto : listInstancesDto)		{
			
			if(dto.equals(instanceURI))
			{				
				String nameSpace = dto.split("#")[0] + "#";
				String name = dto.split("#")[1];				
				if (Instance == null){					
					Instance = new DtoInstance(nameSpace, name, QueryUtil.getClassesURI(InfModel, instanceURI), QueryUtil.getIndividualsURIDifferentFrom(InfModel, dto), QueryUtil.getIndividualsURISameAs(InfModel, dto),true);					
				} else {					
					//Update classes
					Instance.ListClasses = QueryUtil.getClassesURI(InfModel, instanceURI);
				}
			}
		}	
		ArrayList<DtoDefinitionClass> resultListDefinitions = new ArrayList<DtoDefinitionClass>();
		for (String cls : Instance.ListClasses) 
		{
			DtoDefinitionClass aux = DtoDefinitionClass.getDtoWithSourceAndRelationAndTarget(resultListDefinitions, cls);
			if(aux == null && ! cls.contains("Thing"))	//don't exist yet
			{
				//relations URI some values
				ArrayList<DtoDefinitionClass> dtoSomeRelationsList = new ArrayList<DtoDefinitionClass>();
				for(String[] triple: QueryUtil.getTuplesSomeValuesFrom(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Target = triple[2];
					dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = OntCardinalityEnum.SOME;
				}
				
				//relations URI minimum cardinality values
				ArrayList<DtoDefinitionClass> dtoMinRelationsList = new ArrayList<DtoDefinitionClass>();
				for(String[] triple: QueryUtil.getTuplesMinQualifiedCardinality(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Cardinality = triple[2];
					dto.Target = triple[3];
					dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = OntCardinalityEnum.MIN;
				}
				
				//relations URI maximum cardinality values
				ArrayList<DtoDefinitionClass> dtoMaxRelationsList = new ArrayList<DtoDefinitionClass>();
				for(String[] triple: QueryUtil.getTuplesMaxQualifiedCardinality(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Cardinality = triple[2];
					dto.Target = triple[3];
					dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = OntCardinalityEnum.MAX;
				}	
				
				//relations URI exact cardinality values
				ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = new ArrayList<DtoDefinitionClass>();
				for(String[] triple: QueryUtil.getTuplesQualifiedCardinality(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Cardinality = triple[2];
					dto.Target = triple[3];
					dto.PropertyType = QueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = OntCardinalityEnum.EXACTLY;
				}	
				resultListDefinitions.addAll(dtoSomeRelationsList);
				resultListDefinitions.addAll(dtoMinRelationsList);
				resultListDefinitions.addAll(dtoMaxRelationsList);
				resultListDefinitions.addAll(dtoExactlyRelationsList);
			}		
		}
		//Add to list of intances
		//listAllInstances.add(Instance);		
		//return
		return resultListDefinitions;
	}
}
