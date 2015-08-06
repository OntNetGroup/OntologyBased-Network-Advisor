package br.com.padtec.nopen.service.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.model.RelationEnum;
import br.com.padtec.okco.core.application.OKCoUploader;

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

	public static boolean hasBinds(InfModel model, String portURI) {
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ont: <http://www.menthor.net/nOpenModel_light.owl#> "
				+ "ASK WHERE { "
				+	"?relation3 rdfs:subPropertyOf ont:binds . "
				+	"<" + portURI + "> ?relation ?w . "
				+	"?w ?relation2 ?y . "
				+	"?y ?relation3 ?x . "
				+ " }" ;
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean result = qe.execAsk();			
		return result;
	}
	
	/** @author John Guerson */
	public static String getSupervisorURI(OKCoUploader repository, String equipmentURI)
	{
		List<String> supervisors = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			equipmentURI, 
			repository.getNamespace()+RelationEnum.INV_supervises_Equipment_Supervisor.toString(), 
			repository.getNamespace()+ConceptEnum.Supervisor.toString()
		);
		if(supervisors.size()>0) return supervisors.get(0);
		else return null;
	}
	
	/** @author John Guerson */
	public static List<String> getCardsURI(OKCoUploader repository, String supervisorURI)
	{
		List<String> cards = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			supervisorURI,
			repository.getNamespace()+RelationEnum.INV_supervises_card_Card_Supervisor.toString(), 
			repository.getNamespace()+ConceptEnum.Card.toString()
		);
		return cards;
	}
	
	/** @author John Guerson */
	public static List<String> getSubslotsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> subslots = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Subslot_Card.toString(), 
			repository.getNamespace()+ConceptEnum.Subslot.toString()
		);		
		return subslots;
	}
	
	/** @author John Guerson */
	public static List<String> getSlotsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Slot_Card.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
		
	/** @author John Guerson */
	public static List<String> getSlotsURIFromSubSlot(OKCoUploader repository, String subslotURI)
	{		
		List<String> slots = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			subslotURI,
			repository.getNamespace()+RelationEnum.A_Slot_Subslot.toString(), 
			repository.getNamespace()+ConceptEnum.Slot.toString()
		);		
		return slots;
	}
	
	/** @author John Guerson */
	public static List<String> getShelfsURIFromSlot(OKCoUploader repository, String slotURI)
	{		
		List<String> shelfs = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			slotURI,
			repository.getNamespace()+RelationEnum.A_Shelf_Slot.toString(), 
			repository.getNamespace()+ConceptEnum.Shelf.toString()
		);		
		return shelfs;
	}

	/** @author John Guerson */
	public static List<String> getRacksURIFromShelf(OKCoUploader repository, String shelfURI)
	{		
		List<String> racks = QueryUtil.getIndividualsURIAtObjectPropertyDomain(
			repository.getBaseModel(), 
			shelfURI,
			repository.getNamespace()+RelationEnum.A_Rack_Shelf.toString(), 
			repository.getNamespace()+ConceptEnum.Rack.toString()
		);		
		return racks;
	}
	
	/** @author John Guerson */
	public static List<String> getTFsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.TF_Card_Element.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getPhysicalMediasURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.Physical_Media.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getMatrixURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.Matrix.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getAFsURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_TFCardElement.toString(), 
			repository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getCardLayersURIFromCard(OKCoUploader repository, String cardURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardURI,
			repository.getNamespace()+RelationEnum.A_Card_CardLayer.toString(), 
			repository.getNamespace()+ConceptEnum.Card_Layer.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getLayerTypeURIFromCardLayer(OKCoUploader repository, String cardLayerURI)
	{		
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
			repository.getBaseModel(), 
			cardLayerURI,
			repository.getNamespace()+RelationEnum.instantiates_Card_Layer_Layer_Type.toString(), 
			repository.getNamespace()+ConceptEnum.Layer_Type.toString()
		);		
		return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getTTFURIFromCardLayer(OKCoUploader repository, String cardLayerURI)
	{
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
				repository.getBaseModel(), 
				cardLayerURI,
				repository.getNamespace()+RelationEnum.A_CardLayer_TrailTerminationFunction.toString(), 
				repository.getNamespace()+ConceptEnum.Trail_Termination_Function.toString()
			);		
			return elems;
	}
		
	/** @author John Guerson */
	public static List<String> getMatrixURIFromTTF(OKCoUploader repository, String ttfURI)
	{
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
				repository.getBaseModel(), 
				ttfURI,
				repository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Matrix.toString(), 
				repository.getNamespace()+ConceptEnum.Matrix.toString()
			);		
			return elems;
	}
	
	/** @author John Guerson */
	public static List<String> getAFURIFromTTF(OKCoUploader repository, String ttfURI)
	{
		List<String> elems = QueryUtil.getIndividualsURIAtObjectPropertyRange(
				repository.getBaseModel(), 
				ttfURI,
				repository.getNamespace()+RelationEnum.binds_Trail_Termination_Function_Adaptation_Function.toString(), 
				repository.getNamespace()+ConceptEnum.Adaptation_Function.toString()
			);		
			return elems;
	}
}
