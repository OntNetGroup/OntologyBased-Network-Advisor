package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.UploadApp;
import br.ufes.inf.padtec.tnokco.controller.HomeController;

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
