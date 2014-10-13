package br.com.padtec.okco.domain;
import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.InfModelQueryUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;

public class Search 
{	 	
 	public ArrayList<DtoDefinitionClass> GetModelDefinitionsInInstances(ArrayList<Instance> listAllInstances,	InfModel InfModel) {

 		System.out.println("\nSearch: Getting model definitions in instances()...");
		ArrayList<DtoDefinitionClass> resultListDefinitions = new ArrayList<DtoDefinitionClass>();
		
		for (Instance instance : listAllInstances) 
		{
			for (String cls : instance.ListClasses) 
			{
				//DtoDefinitionClass aux = DtoDefinitionClass.getDtoWithSourceAndRelationAndTarget(resultListDefinitions, cls);
				
				DtoDefinitionClass aux = null;
				if(aux == null && ! cls.contains("Thing"))	//doesn't exist yet
				{
					//relations URI some values
					ArrayList<DtoDefinitionClass> dtoSomeRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: InfModelQueryUtil.getTuplesSomeValuesFrom(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Target = triple[2];
						dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = EnumRelationTypeCompletness.SOME;
					}
					
					//relations URI minimum cardinality values
					ArrayList<DtoDefinitionClass> dtoMinRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: InfModelQueryUtil.getTuplesMinQualifiedCardinality(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Cardinality = triple[2];
						dto.Target = triple[3];
						dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = EnumRelationTypeCompletness.MIN;
					}
					
					//relations URI maximum cardinality values
					ArrayList<DtoDefinitionClass> dtoMaxRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: InfModelQueryUtil.getTuplesMaxQualifiedCardinality(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Cardinality = triple[2];
						dto.Target = triple[3];
						dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = EnumRelationTypeCompletness.MAX;
					}	
					
					//relations URI exact cardinality values
					ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = new ArrayList<DtoDefinitionClass>();
					for(String[] triple: InfModelQueryUtil.getTuplesQualifiedCardinality(InfModel,cls)){
						DtoDefinitionClass dto = new DtoDefinitionClass();
						dto.Source = triple[0];
						dto.Relation = triple[1];
						dto.Cardinality = triple[2];
						dto.Target = triple[3];
						dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
						dto.TypeCompletness = EnumRelationTypeCompletness.EXACTLY;
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
	
 	public ArrayList<DtoDefinitionClass> GetModelDefinitionsInInstances(String instanceURI, OntModel model, InfModel InfModel, ArrayList<Instance> listAllInstances, ManagerInstances manager) {

		Instance Instance = manager.getInstance(listAllInstances, instanceURI); // GET INTANCE on MODEL
		List<String> listInstancesDto = InfModelQueryUtil.getIndividualsURIFromAllClasses(InfModel);		
		for (String dto : listInstancesDto) {
			
			if(dto.equals(instanceURI))
			{				
				String nameSpace = dto.split("#")[0] + "#";
				String name = dto.split("#")[1];
				
				if (Instance == null)
				{					
					Instance = new Instance(nameSpace, name, InfModelQueryUtil.getClassesURI(InfModel, instanceURI), InfModelQueryUtil.getIndividualsURIDifferentFrom(InfModel, dto), InfModelQueryUtil.getIndividualsURISameAs(InfModel, dto),true);
					
				} else {
					
					//Update classes
					Instance.ListClasses = InfModelQueryUtil.getClassesURI(InfModel, instanceURI);
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
				for(String[] triple: InfModelQueryUtil.getTuplesSomeValuesFrom(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Target = triple[2];
					dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = EnumRelationTypeCompletness.SOME;
				}
				
				//relations URI minimum cardinality values
				ArrayList<DtoDefinitionClass> dtoMinRelationsList = new ArrayList<DtoDefinitionClass>();
				for(String[] triple: InfModelQueryUtil.getTuplesMinQualifiedCardinality(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Cardinality = triple[2];
					dto.Target = triple[3];
					dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = EnumRelationTypeCompletness.MIN;
				}
				
				//relations URI maximum cardinality values
				ArrayList<DtoDefinitionClass> dtoMaxRelationsList = new ArrayList<DtoDefinitionClass>();
				for(String[] triple: InfModelQueryUtil.getTuplesMaxQualifiedCardinality(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Cardinality = triple[2];
					dto.Target = triple[3];
					dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = EnumRelationTypeCompletness.MAX;
				}	
				
				//relations URI exact cardinality values
				ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = new ArrayList<DtoDefinitionClass>();
				for(String[] triple: InfModelQueryUtil.getTuplesQualifiedCardinality(InfModel,cls)){
					DtoDefinitionClass dto = new DtoDefinitionClass();
					dto.Source = triple[0];
					dto.Relation = triple[1];
					dto.Cardinality = triple[2];
					dto.Target = triple[3];
					dto.PropertyType = InfModelQueryUtil.getPropertyURIType(InfModel, dto.Relation);
					dto.TypeCompletness = EnumRelationTypeCompletness.EXACTLY;
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
