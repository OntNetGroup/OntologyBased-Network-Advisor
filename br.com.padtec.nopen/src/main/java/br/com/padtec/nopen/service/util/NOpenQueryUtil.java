package br.com.padtec.nopen.service.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.RelationEnum;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class NOpenQueryUtil {

	public static String[] getIndividualsNames(InfModel model, String className)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURI(model, model.getNsPrefixURI("")+className);
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(model.getNsPrefixURI(""),""); i++; }
		return result;
	}
	
	public static String[] getIndividualsNamesAtObjectPropertyRange(InfModel model, String sourceIndividualName, String propertyName, String rangeClassName)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			model, 
			model.getNsPrefixURI("")+sourceIndividualName, 
			model.getNsPrefixURI("")+propertyName, 
			model.getNsPrefixURI("")+rangeClassName
		);	
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(model.getNsPrefixURI(""), ""); i++; }
		return result;
	}
	
	public static String[] getAllTechnologiesNames(InfModel model)
	{
		return getIndividualsNames(model, ConceptEnum.Technology.toString());		
	}
	
	public static String[] getAllServicesNames(InfModel model)
	{
		return getIndividualsNames(model,ConceptEnum.Service.toString());		
	}
	
	public static String[] getAllLayerNames(InfModel model, String techName)
	{
		return  getIndividualsNamesAtObjectPropertyRange(model,
				techName,
				RelationEnum.A_Technology_LayerType.toString(), 
				ConceptEnum.Layer_Type.toString()
			);					
	}
	
	public static String[][] getAllLayerNames(InfModel model)
	{
		String[] techs = getAllTechnologiesNames(model);
		String[][] result = new String[techs.length][];
		int i=0;
		for(String s: techs){
			result[i]= getAllLayerNames(model,s);
			i++;
		}
		return result;
	}
	
	public static HashSet<String> getAllTemplateEquipment(InfModel model)
	{
		HashSet<String> result = new HashSet<String>();
		String queryString = ""
		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
		+ "PREFIX ont: <http://www.menthor.net/nOpenModel_light.owl#>" 
		+ "SELECT ?subject "
		+  " WHERE { ?subject rdf:type ont:Equipment }" ;
		
		Query query = QueryFactory.create(queryString); 
  		
  		// Execute the query and obtain results
  		QueryExecution qe = QueryExecutionFactory.create(query, model);
  		ResultSet results = qe.execSelect();
  		//ResultSetFormatter.out(System.out, results, query);
  		
  		while (results.hasNext()) {
  			QuerySolution row = results.next();
  		    
  		    RDFNode rdfY = row.get("subject");
  	    	result.add(rdfY.toString());
  		}
  		
		return result;
	}
	
	
	
	public static boolean cardHasSupervisor(String card, InfModel model){
		String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX ont: <http://www.menthor.net/nOpenModel_light.owl#> "
				+ "ASK "
				+ "WHERE { "
				+ "ont:" + card + "rdf:type ont:Card . "
				+ "?subject rdf:type ont:Supervisor . "
				+ "?subject ont:supervises_card ont:" + card + ". "
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean result = qe.execAsk();			
		return result;
	}
	
	public static HashSet<String> discoverRPBetweenPorts(String type_output, String type_input, InfModel model){
		HashSet<String> result = new HashSet<String>();
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ont: <http://www.menthor.net/nOpenModel.owl#> "
				+ "SELECT ?y "
				+ "WHERE { ?x rdfs:subPropertyOf ont:INV.links_output . "
				+ "?x rdfs:domain ont:" + type_output + " . "
				+ "?x rdfs:range ?y . "
				+ "?z rdfs:subPropertyOf ont:links_input . "
				+ "?z rdfs:range ont:" + type_input + " . "
				+ "?z rdfs:domain ?y . "
				+ "}";
		Query query = QueryFactory.create(queryString); 
  		
  		// Execute the query and obtain results
  		QueryExecution qe = QueryExecutionFactory.create(query, model);
  		ResultSet results = qe.execSelect();
  		
  		while (results.hasNext()) {
  			QuerySolution row = results.next();
  		    
  		    RDFNode rdfY = row.get("y");
  	    	result.add(rdfY.toString());
  		}
  		
		return result;
	}
	
	public static HashMap<String, String> getAllComponentOFRelations(String classID, InfModel model)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ont: <http://www.menthor.net/nOpenModel.owl#> "
				+ "SELECT  ?relation ?target  "
				+ "WHERE { ?relation rdfs:subPropertyOf ont:componentOf . "
				+ "?relation rdfs:domain ont:" + classID + " . "
				+ "?relation rdfs:range ?target . "
				+  "}";
		
		Query query = QueryFactory.create(queryString); 
  		
  		// Execute the query and obtain results
  		QueryExecution qe = QueryExecutionFactory.create(query, model);
  		ResultSet results = qe.execSelect();
  		
  		while (results.hasNext()) {
  			QuerySolution row = results.next();
  		    RDFNode rdfRelation = row.get("relation");
  		    RDFNode rdfTarget = row.get("target");
  		    result.put(rdfTarget.toString(), rdfRelation.toString());
  		    
  		}
		return result;
	}
}
