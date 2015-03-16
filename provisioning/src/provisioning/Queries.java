package provisioning;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class Queries {
	public static List<String> getInterfacesAndEquipMappingPorts(OntModel model, boolean forOutputInterface, boolean forSourceComponent){
		String interfaceType = "";
		String componentType = "";
		if(forSourceComponent){
			componentType = "Source";
		}else{
			componentType = "Sink";
		}
		if(forOutputInterface){
			interfaceType = "Output";
		}else{
			interfaceType = "Input";
		}
		
		System.out.println("\nExecuting getEquipmentMappingPorts()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "\t{\n"
				+ "\t\t?equipment rdf:type ns:Equipment .\n"
				+ "\t\t?equipment ns:componentOf ?interface .\n"
				+ "\t\t?interface rdf:type ns:" + interfaceType +"_Interface .\n"
				+ "\t\t?interface ns:maps ?mappedPort .\n"
				+ "\t\t?mappedPort ns:INV.componentOf ?tf .\n"
				+ "\t\t?tf rdf:type ns:AF_" + componentType + " .\n"
				+ "\t}\n"
				+ "\tUNION\n"
				+ "\t{\n"
				+ "\t\t?equipment rdf:type ns:Equipment .\n"
				+ "\t\t?equipment ns:componentOf ?interface .\n"
				+ "\t\t?interface rdf:type ns:" + interfaceType +"_Interface .\n"
				+ "\t\t?interface ns:maps ?mappedPort .\n"
				+ "\t\t?mappedPort ns:INV.componentOf ?tf .\n"
				+ "\t\t?tf rdf:type ns:TF_" + componentType + " .\n"
				+ "\t}\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode equipment = row.get("equipment");	
		    RDFNode interface_ = row.get("interface");
		    @SuppressWarnings("unused")
			RDFNode mappedPort = row.get("mappedPort");
		    if(QueryUtil.isValidURI(equipment.toString()) && QueryUtil.isValidURI(interface_.toString()))
		    {
		    	System.out.println("- Class URI: "+equipment.toString()); 
		    	System.out.println("- interface_ URI: "+interface_.toString()); 
		    	result.add(interface_.toString());
		    	result.add(equipment.toString());		    	 
		    }
		}
		return result;
	}
	
	public static boolean isInterfacePartOfEquipWithPM(OntModel model, String interfaceURI){
		System.out.println("\nExecuting isEquipBindedWithPMEquip()...");
		
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "ASK\n"
				+ "WHERE {\n"
				+ " ?equip ns:componentOf <" + interfaceURI + "> .\n"
				+ "	?equip ns:componentOf ?pm .\n"
				+ "	?pm rdf:type ns:Physical_Media .\n"
				+ "}";
		
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean results = qe.execAsk();	
		
		return results;
	}
	
	public static boolean isEquipBindedWithPMEquip(OntModel model, String interfaceFromURI){
		System.out.println("\nExecuting isEquipBindedWithPMEquip()...");
		
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "ASK\n"
				+ "WHERE {\n"
				+ "?equip1 ns:componentOf <" + interfaceFromURI + "> .\n"
				+ "?equip1 ns:componentOf ?int2 .\n"
				+ "?int2 ns:maps ?tf_port .\n"
				+ "?tf_port ns:binds ?pm_port .\n"
				+ "?pm ns:componentOf ?pm_port .\n"
				+ "?pm rdf:type ns:Physical_Media .\n"
				+ "}";
		
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean results = qe.execAsk();	
		
		return results;
	}
	
	public static List<String> getAvailableInterfacesFromEquipment(OntModel model, String equipURI, boolean forOutput){
		String portType = "";
		if(forOutput){
			portType = "Output";
		}else{
			portType = "Input";
		}
		
		System.out.println("\nExecuting getEquipmentMappingPorts()...");
		
		List<String> bindedInterfaces = getBindedInterfaces(model);
		List<String> result = new ArrayList<String>();
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "\t<" + equipURI + "> ns:componentOf ?int1 .\n"
				+ "\t?int1 rdf:type ns:" + portType + "_Interface ."
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode int1 = row.get("int1");	
		   if(QueryUtil.isValidURI(int1.toString()) && !bindedInterfaces.contains(int1.toString()))
		    {
		    	System.out.println("- Class URI: "+int1.toString()); 
		    	result.add(int1.toString()); 
		    }
		}
		
		return result;
	}
	
	public static List<String> getEquipmentWithPhysicalMedia(OntModel model){
		System.out.println("\nExecuting getEquipmentWithPhysicalMedia()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "\t?equipment rdf:type ns:Equipment .\n"
				+ "\t?equipment ns:componentOf ?pm .\n"
				+ "\t?pm rdf:type ns:Physical_Media .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode equipment = row.get("equipment");	
		    @SuppressWarnings("unused")
			RDFNode pm = row.get("pm");
		    if(QueryUtil.isValidURI(equipment.toString()))
		    {
		    	System.out.println("- Class URI: "+equipment.toString()); 
		    	result.add(equipment.toString()); 
		    }
		}
		return result;
	}
	
	public static List<String> getMappingInterfaceFrom(OntModel model, String tfURI, boolean isSource){
		String tf_type = "";
		if(isSource){
			tf_type  = "Output";
		}else{
			tf_type = "Input";
		}
		System.out.println("\nExecuting getMappedPort()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ " <" + tfURI + "> ns:componentOf ?port .\n"
				+ "	?interface ns:maps ?port .\n"
				+ "	?interface rdf:type ns:" + tf_type + "_Interface.\n"
				+ "?equip ns:componentOf ?interface .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
			RDFNode interface_ = row.get("interface");	
			RDFNode equip = row.get("equip");	
		    if(QueryUtil.isValidURI(interface_.toString()) && QueryUtil.isValidURI(equip.toString()))
		    {
		    	System.out.println("- interface URI: "+interface_.toString()); 
		    	result.add(interface_.toString());
		    	System.out.println("- equip URI: "+equip.toString()); 
		    	result.add(equip.toString());
		    }
		}
		return result;
	}
	
	public static String getMappedPort(OntModel model, String interfaceURI){
		System.out.println("\nExecuting getMappedPort()...");
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "\t<" + interfaceURI + "> ns:maps ?port .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode port = row.get("port");	
		    if(QueryUtil.isValidURI(port.toString()))
		    {
		    	System.out.println("- port URI: "+port.toString()); 
		    	return port.toString(); 
		    }
		}
		return "";
	}
	
	public static String getMappedTFFrom(OntModel model, String interfaceURI){
		System.out.println("\nExecuting getMappedTF()...");
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "\t<" + interfaceURI + "> ns:maps ?port .\n"
				+ "\t?tf ns:componentOf ?port .\n"
				+ "?tf rdf:type ns:Transport_Function .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode tf = row.get("tf");	
		    if(QueryUtil.isValidURI(tf.toString()))
		    {
		    	System.out.println("- tf URI: "+tf.toString()); 
		    	return tf.toString(); 
		    }
		}
		return "";
	}
	
	public static List<String> getLayersAdaptedFromAF(OntModel model, String interfaceURI){
		System.out.println("\nExecuting getEquipmentWithPhysicalMedia()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ " <" + interfaceURI + "> ns:maps ?port .\n"
				+ "	?tf ns:componentOf ?port .\n"
				+ "	?tf ns:adapts_to ?layer .\n "
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode layer = row.get("layer");	
		    if(QueryUtil.isValidURI(layer.toString()))
		    {
		    	System.out.println("- layer URI: "+layer.toString()); 
		    	result.add(layer.toString()); 
		    }
		}
		return result;
	}
	
	public static List<String> getInterfacesToProvision(OntModel model, String interfaceFromURI, boolean isSource){
		System.out.println("\nExecuting getInterfacesToProvision()...");
		
		//String intType = "";
		String relName1 = "";
		String relName2 = "";
		String tgtTFtype = "";
		
		boolean isMappedByTF = isInterfaceMappedByTF(model, interfaceFromURI);
		
		if(isMappedByTF){
			relName1 = "adapts_from";
			relName2 = "defines";
			tgtTFtype += "AF_";
		}else{
			relName1 = "defines";
			relName2 = "adapts_from";
			tgtTFtype += "TF_";
		}
		if(isSource){
			//intType = "Input";
			tgtTFtype += "Source";
		}else{
			//intType = "Output";
			tgtTFtype += "Sink";
		}
		
		List<String> result = new ArrayList<String>();				
		String queryString = "";
		if(isInterfaceInTheLastLayer(model, interfaceFromURI)){
			queryString = ""
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
					+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
					+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
					+ "PREFIX ns: <http://nemo.inf.ufes.br/NewProject.owl#>\n"
					+ "SELECT DISTINCT *\n"
					+ "WHERE {\n"
					+ "	?eq ns:componentOf ?int .\n"
					//+ "	?int rdf:type ns:" + intType + "_Interface .\n"
					+ "	?int ns:maps ?port .\n"
					+ "	?pm ns:componentOf ?port .\n"
					+ "	?pm rdf:type ns:Physical_Media .\n"
					+ "}";
		}else{
			queryString = ""
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
					+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
					+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
					+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
					+ "SELECT DISTINCT *\n"
					+ "WHERE {\n"
					+ "	<" + interfaceFromURI + "> ns:maps ?portFrom .\n"
					+ "	?tfFrom ns:componentOf ?portFrom .\n"
					+ "	?tfFrom ns:" + relName2 + " ?layer .\n"
					+ "	?tfTo ns:" + relName1 + " ?layer .\n"
					+ " ?tfTo ns:componentOf ?portTo .\n"
					+ "	?tfTo rdf:type ns:" + tgtTFtype + " .\n"
					+ "	?intTo ns:maps ?portTo .\n"
					+ " ?equipTo ns:componentOf ?intTo . 	\n"
					+ "}";
		}
		
		List<String> bindedInterfaces = getBindedInterfaces(model);
		
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode equipTo = row.get("equipTo");	
		    RDFNode intTo = row.get("intTo");
		    if(QueryUtil.isValidURI(equipTo.toString()) && QueryUtil.isValidURI(intTo.toString()) && !bindedInterfaces.contains(intTo.toString()))
		    {
		    	System.out.println("- intTo URI: "+intTo.toString()); 
		    	result.add(intTo.toString());
		    	System.out.println("- equipTo URI: "+equipTo.toString()); 
		    	result.add(equipTo.toString());		
		    }
		}
		
		return result;
	}
	
	public static List<String> getBindedInterfaces(OntModel model){
		System.out.println("\nExecuting getBindedInterfaces()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "?int1 ns:maps ?port1 .\n"
				+ "	?int2 ns:maps ?port2 .\n"
				+ "	?port1 ns:binds ?port2 .\n" 
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode int1 = row.get("int1");	
		    if(QueryUtil.isValidURI(int1.toString()))
		    {
		    	System.out.println("- int1 URI: "+int1.toString()); 
		    	result.add(int1.toString()); 
		    }
		    RDFNode int2 = row.get("int2");	
		    if(QueryUtil.isValidURI(int2.toString()))
		    {
		    	System.out.println("- int2 URI: "+int2.toString()); 
		    	result.add(int2.toString()); 
		    }
		}
		return result;
	}
	
	public static List<String> getServerLayersFromTF(OntModel model, String interfaceURI){
		System.out.println("\nExecuting getEquipmentWithPhysicalMedia()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "<" + interfaceURI + "> ns:maps ?port .\n"
				+ "	?tf ns:componentOf ?port .\n"
				+ "	?tf ns:defines ?serverLayer . \n" 
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode serverLayer = row.get("serverLayer");	
		    if(QueryUtil.isValidURI(serverLayer.toString()))
		    {
		    	System.out.println("- serverLayer URI: "+serverLayer.toString()); 
		    	result.add(serverLayer.toString()); 
		    }
		}
		return result;
	}
	
	public static boolean isInterfaceMappedByTF(OntModel model, String interfaceURI){
		System.out.println("\nExecuting isInterfaceMappedByTF()...");
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "ASK\n"
				+ "WHERE {\n"
				+ "	<" + interfaceURI + "> ns:maps ?port .\n"
				+ "	?tf ns:componentOf ?port .\n"
				+ "	?tf rdf:type ns:Termination_Function . \n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean results = qe.execAsk();		
		
		return results;
	}
	
	public static boolean isInterfaceInTheLastLayer(OntModel model, String interfaceURI){
		System.out.println("\nExecuting isInterfaceInTheLastLayer()...");
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "ASK\n"
				+ "WHERE {\n"
				+ "	<" + interfaceURI + "> ns:maps ?port .\n"
				+ "	?tf ns:componentOf ?port .\n"
				+ "	?tf ns:defines ?layer . \n"
				+ "	?layer ns:Layer_Network.isLast \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> . \n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean results = qe.execAsk();		
		
		return results;
	}
	
	public static List<String> getLastBindedTFFrom(OntModel model, String tfURI){
		System.out.println("\nExecuting getLastBindedTFFrom()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "\t{\n"
				+ "\t\t<" + tfURI + "> ns:tf_binds*/ns:tf_binds ?tf2 .\n"
				+ "\t}\n"
				+ "\tUNION\n"
				+ "\t{\n"
				+ "\t\t?tf2 ns:tf_binds*/ns:tf_binds <" + tfURI + "> .\n"
				+ "\t}\n"
				+ "}\n";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
			RDFNode tf2 = row.get("tf2");	
			if(QueryUtil.isValidURI(tf2.toString()))
		    {
		    	System.out.println("- tf2 URI: "+tf2.toString()); 
		    	result.add(tf2.toString()); 
		    }
		}
		return result;
	}
}
