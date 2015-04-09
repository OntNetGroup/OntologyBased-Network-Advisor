package br.com.padtec.nopen.service.util;

import java.util.HashSet;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.nopen.studio.service.StudioComponents;

public class NOpenQueries {

	public static String[] getIndividualsNames(String classURI)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURI(StudioComponents.studioRepository.getInferredModel(), classURI);
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(StudioComponents.studioRepository.getNamespace(), ""); i++; }
		return result;
	}
	
	public static String[] getIndividualsNamesAtObjectPropertyRange(String techName, String opURI, String classURI)
	{		
		List<String> individualsURI = QueryUtil.getIndividualsURIAtObjectPropertyRange(
				StudioComponents.studioRepository.getInferredModel(), 
				StudioComponents.studioRepository.getNamespace()+techName, 
				StudioComponents.studioRepository.getNamespace()+RelationEnum.COMPONENTOF_TECH_LAYER.toString(), 
				StudioComponents.studioRepository.getNamespace()+ConceptEnum.LAYER.toString()
		);	
		String[] result = new String[individualsURI.size()];
		int i=0;
		for(String s: individualsURI){ result[i] = s.replace(StudioComponents.studioRepository.getNamespace(), ""); i++; }
		return result;
	}
	
	public static String[] getTechnologiesNames()
	{
		return getIndividualsNames(StudioComponents.studioRepository.getNamespace()+ConceptEnum.TECHNOLOGY.toString());		
	}
	
	public static String[] getServicesNames()
	{
		return getIndividualsNames(StudioComponents.studioRepository.getNamespace()+ConceptEnum.SERVICE.toString());		
	}
	
	public static String[] getLayerNames(String techName)
	{
		return  getIndividualsNamesAtObjectPropertyRange(
				techName,
				StudioComponents.studioRepository.getNamespace()+RelationEnum.COMPONENTOF_TECH_LAYER.toString(), 
				StudioComponents.studioRepository.getNamespace()+ConceptEnum.LAYER.toString()
			);					
	}
	
	public static String[][] getLayerNames()
	{
		String[] techs = NOpenQueries.getTechnologiesNames();
		String[][] result = new String[techs.length][];
		int i=0;
		for(String s: techs){
			result[i]= getLayerNames(s);
			i++;
		}
		return result;
	}
	
	public static HashSet<String> getAllTemplateEquipment(InfModel model){
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
}
