package br.com.padtec.nopen.provisioning.util;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;

import com.google.gson.Gson;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class ProvisioningUtil {

	public static String generateJSONModelRelationships() {
		
		OntModel ontModel = ProvisioningComponents.provisioningRepository.getBaseModel();
		String namespace = ProvisioningComponents.provisioningRepository.getNamespace();
		
		HashMap<String, HashMap<String, ArrayList<String>>> model = new HashMap<String, HashMap<String, ArrayList<String>>>();
		
		//Create query string
		String prefix = "PREFIX ont: <" + namespace + "> " + 
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
						"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String queryString = prefix +
				" SELECT DISTINCT ?source ?target ?predicate WHERE  { " +
					" ?predicate rdf:type owl:ObjectProperty . " +
					" ?predicate rdfs:domain ?domain . " +
					" ?predicate rdfs:range ?range . " +
					" ?source rdfs:subClassOf* ?domain . " + 
					" ?source rdf:type owl:Class . " +
					" ?target rdfs:subClassOf* ?range . " + 
					" ?target rdf:type owl:Class . " +
				" } ORDER BY ?source ?target";
		
		System.out.println(queryString);
		
		//execute query string
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
		ResultSet results = qe.execSelect();

		HashMap<String, ArrayList<String>> relations = new HashMap<String, ArrayList<String>>();
		ArrayList<String> predicates = new ArrayList<String>();
		
		String 	lastDomain = "",
				domain = "",
				lastRange = "",
				range = "";
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    domain = row.get("?source").toString().replace(namespace, "");
		    range = row.get("?target").toString().replace(namespace, "");
		    String predicate = row.get("?predicate").toString().replace(namespace, "");
		    
		    if(lastDomain.equals("")) {
		    	lastDomain = domain;
		    }
		    if(lastRange.equals("")) {
		    	lastRange = range;
		    }
		    
		    if(!range.equals(lastRange)) {
		    	relations.put(lastRange, predicates);
		    	lastRange = range;
		    	predicates = new ArrayList<String>();
		    }
		    
		    if(!domain.equals(lastDomain)) {
		    	model.put(lastDomain, relations);
		    	lastDomain = domain;
		    	relations = new HashMap<String, ArrayList<String>>();
		    }
		    
		    predicates.add(predicate);
		    
		}
		
	    if(!range.equals(lastRange)) {
	    	relations.put(lastRange, predicates);
	    }
		
		if(!domain.equals(lastDomain)) {
			model.put(lastDomain, relations);
		}
		
		String jsonModel = "";
		
		Gson gson = new Gson();
		jsonModel = gson.toJson(model);
		System.out.println(jsonModel);
		
		return jsonModel;
	}
	
}
