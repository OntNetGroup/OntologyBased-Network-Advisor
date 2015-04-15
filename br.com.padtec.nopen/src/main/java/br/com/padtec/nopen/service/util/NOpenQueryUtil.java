package br.com.padtec.nopen.service.util;

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
				RelationEnum.ComponentOf5_Technology_Layer.toString(), 
				ConceptEnum.Layer.toString()
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
		+ "PREFIX ont: <http://nemo.inf.ufes.br/NewProject.owl#>" 
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
	
	public static HashSet<String> getAllComponentOFRelations(String classID, InfModel model)
	{
		HashSet<String> result = new HashSet<String>();
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX ont: <http://nemo.inf.ufes.br/NewProject.owl#>"
				+ "SELECT  *"
				+ "WHERE { ?x rdfs:subPropertyOf ont:componentOf ."
				+ "?x rdfs:domain ont:"+classID + "."
				+ "?x rdfs:range ?r"
				+  "}";
		
		Query query = QueryFactory.create(queryString); 
  		
  		// Execute the query and obtain results
  		QueryExecution qe = QueryExecutionFactory.create(query, model);
  		ResultSet results = qe.execSelect();
  		//ResultSetFormatter.out(System.out, results, query);
  		
  		while (results.hasNext()) {
  			QuerySolution row = results.next();
  		    
  		    RDFNode rdfY = row.get("r");
  	    	result.add(rdfY.toString());
  		}
  		
		return result;
	}
}
