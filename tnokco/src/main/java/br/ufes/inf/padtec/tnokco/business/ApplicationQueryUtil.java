package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.QueryUtil;
import br.ufes.inf.padtec.tnokco.controller.HomeController;
import br.ufes.inf.padtec.tnokco.controller.ManagerInstances;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class ApplicationQueryUtil {
				
 	static public ArrayList<DtoDefinitionClass> GetModelDefinitionsInInstances(String instanceURI, OntModel model, InfModel InfModel, List<DtoInstance> listAllInstances, ManagerInstances manager) {

		DtoInstance Instance = manager.getInstance(listAllInstances, instanceURI); // GET INTANCE on MODEL
		List<String> listInstancesDto = QueryUtil.getIndividualsURIFromAllClasses(InfModel);		
		for (String dto : listInstancesDto) {
			
			if(dto.equals(instanceURI))
			{				
				String nameSpace = dto.split("#")[0] + "#";
				String name = dto.split("#")[1];
				
				if (Instance == null)
				{					
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
				/*
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
				*/
			}			
			
		}	
		
		//Add to list of intances
		//listAllInstances.add(Instance);
		
		//return
		return resultListDefinitions;
	}
 	
	static public ArrayList<DtoDefinitionClass> getClassDefinitionsFromInstances(List<DtoInstance> listAllInstances,	InfModel InfModel) {

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
					/*
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
					*/
				}			
				
			}		
			
		}		
		
		return resultListDefinitions;
	}
	
	static public List<DtoInstanceRelation> GetInstanceRelations(InfModel infModel, String individualUri)
	{
		ArrayList<DtoInstanceRelation> listIndividualRelations = new ArrayList<DtoInstanceRelation>();
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"{ " + "<" + individualUri + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
			"} UNION { " +
				"<" + individualUri + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdf:type" + " owl:DatatypeProperty.\n " +		
			"}" +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		DtoInstanceRelation dtoItem = null;
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");
		    RDFNode target = row.get("target"); 
		    
		    dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = property.toString();
		    dtoItem.Target = target.toString();
		    
			listIndividualRelations.add(dtoItem);		    		    		    
		}
		
		return listIndividualRelations;
	}
	
	static public List<String[]> getDomainAndRangeURI(OntModel model, String propertyURI) 
	{		
		List<String[]> list = new ArrayList<String[]>();		
		String queryString =		
		" SELECT *" +
		" WHERE {\n" +		
			" ?source <" + propertyURI + "> ?target .\n " +	
		"}";
		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		while (results.hasNext()) 
		{
			String [] tupla = new String[2];
			QuerySolution row = results.next();		    
		    RDFNode source = row.get("source");
		    RDFNode target = row.get("target");		    
		    tupla[0] = source.toString();
		    tupla[1] = target.toString();
		    //DateTimeHelper.printout("Domain: "+source.toString()+" - Range: "+target.toString()+" - OntProperty URI: "+propertyURI);
		    list.add(tupla);
		}
		return list;		
	}
	
	static public List<DtoInstanceRelation> GetInstanceAllRelations(InfModel infModel, String individualUri)
	{
		List<DtoInstanceRelation> listIndividualRelations = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = QueryUtil.getPropertiesURI(HomeController.InfModel, individualUri);
		for(String propertyURI: propertiesURIList){
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;
		    dtoItem.Target = QueryUtil.getRangeURIs(HomeController.InfModel, propertyURI).get(0);
		    listIndividualRelations.add(dtoItem);
		}
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"{ " + " ?domain " + " ?property " + "<" + individualUri + ">" + " .\n " +
				" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
			"} " +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		DtoInstanceRelation dtoItem = null;
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    ResourceImpl property = (ResourceImpl) row.get("property");
		    String propertyUri = property.getURI();
		    
		    propertyUri = propertyUri.replace(property.getNameSpace(), "");
		    
		    if(propertyUri.startsWith("INV.")){
		    	propertyUri.replaceFirst("INV.", "");
		    }else{
		    	propertyUri = "INV." + propertyUri;
		    }
		    
		    propertyUri = property.getNameSpace() + propertyUri;
		    
		    ///////////////PAREI AQUI
		    RDFNode domain = row.get("domain"); 
		    
		    dtoItem = new DtoInstanceRelation();
		    //dtoItem.Property = property.toString();
		    dtoItem.Property = propertyUri;
		    
		    //since I change the relation name (including or removing the "INV." prefix), the domain result changes to target
		    dtoItem.Target = domain.toString();
		    
		    if(!listIndividualRelations.contains(dtoItem)){
		    	listIndividualRelations.add(dtoItem);
		    }					    		    		    
		}
		
		return listIndividualRelations;
	}
}
