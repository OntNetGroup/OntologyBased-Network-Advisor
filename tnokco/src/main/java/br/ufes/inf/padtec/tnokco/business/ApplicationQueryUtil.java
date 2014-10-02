package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;

import br.ufes.inf.nemo.okco.controller.HomeController;
import br.ufes.inf.nemo.okco.model.DtoInstanceRelation;

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
				
	static public ArrayList<DtoInstanceRelation> GetInstanceAllRelations(InfModel infModel, String individualUri)
	{
		ArrayList<DtoInstanceRelation> listIndividualRelations = HomeController.Search.GetInstanceRelations(infModel, individualUri);
		
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
