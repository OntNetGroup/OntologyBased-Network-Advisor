package br.com.padtec.nopen.service.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
				RelationEnum.ComponentOf8_Technology_Layer.toString(), 
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
	
	@SuppressWarnings("unused")
	public static HashSet<String> getAllComponentOFRelations(String classID, InfModel model)
	{
		HashSet<String> result = new HashSet<String>();
		HashSet<String> x = new HashSet<String>();
		HashSet<String> y = new HashSet<String>();
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ont: <http://nemo.inf.ufes.br/NewProject.owl#> "
				+ "SELECT  ?r ?y "
				+ "WHERE { ?x rdfs:subPropertyOf ont:componentOf . "
				+ "?x rdfs:domain ont:"+ classID + ". "
				+ "?x rdfs:range ?r ."
				+ "	OPTIONAL { ?y rdfs:subClassOf ?r . } "
				+  "}";
		
		Query query = QueryFactory.create(queryString); 
  		
  		// Execute the query and obtain results
  		QueryExecution qe = QueryExecutionFactory.create(query, model);
  		ResultSet results = qe.execSelect();
  		//ResultSetFormatter.out(System.out, results, query);
  		
  		while (results.hasNext()) {
  			QuerySolution row = results.next();
  		    RDFNode rdfX = row.get("r");
  		    x.add(rdfX.toString());
  		    RDFNode rdfY = row.get("y");
  		    y.add(rdfY.toString());
  		}
  		boolean ok = false;
  		ok = result.addAll(x);
  		ok = result.addAll(y);
  		Iterator<String> i = result.iterator();
	  		while(i.hasNext()){
	  			ArrayList<String> subclasses = new ArrayList<String>();
				subclasses = QueryUtil.SubClass(model, i.next().toString());
				
	  			if(subclasses.size()>1){//se só tem uma subclasse, então a subclasse é a própria classe
	  				i.remove();
	  			}
	  		}
		return result;
	}
	
	public static boolean cardHasSupervisor(String card, InfModel model){
		String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX ont: <http://nemo.inf.ufes.br/NewlProject.owl#> "
				+ "ASK "
				+ "WHERE { ont:" + card + "rdf:type ont:Card . "
				+ "?subject rdf:type ont:Supervisor . "
				+ "ont:" + card + "ont:ComponentOf6 ?subject . "
				+ "}" ;
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean result = qe.execAsk();			
		return result;
	}
}
