package provisioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class Main {

	public static void main(String[] args){
		String tBoxFile = "";
		try {
			tBoxFile = FindCertainExtension.chooseFile("TBox", ".owl");
			createTBox(tBoxFile);
			
			String aBoxFile = FindCertainExtension.chooseFile("ABox", ".txt");
			createInstances(aBoxFile);
			//runReasoner();
			
			verifiyMinimumEquipment();
			List<String> equipMappingOut = verifyIfEquipmentMapsOutPorts();
			List<String> equipMappingIn = verifyIfEquipmentMapsInPorts();
			
			int srcEquip2Prov = chooseOne(equipMappingOut, "Source Equipment to provision");
			int tgtEquip2Prov = chooseOne(equipMappingIn, "Target Equipment to provision");
			
			String possibleEquipFile = FindCertainExtension.chooseFile("available Equipment", ".txt");
			createInstances(possibleEquipFile);
			
			String srcEquip = algorithmPart1(equipMappingOut.get(srcEquip2Prov));
			algorithmPart2(srcEquip, true);
			
			
			verifiyMinimumEquipWithPM();						
			//runReasoner();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		saveNewOwl(tBoxFile);
		System.out.println("Done.");
	}
	
	public static void createTBox(String tBoxFile) throws Exception{
		FileInputStream inTBox = new FileInputStream(new File(tBoxFile));
		OKCoUploader.uploadBaseModel(inTBox, "off", "hermit");
	}
	
	public static List<DtoInstance> verifiyMinimumEquipment() throws Exception{
		OntModel model = OKCoUploader.getBaseModel();
		
		List<DtoInstance> equipList = DtoQueryUtil.getIndividualsFromClass(model, "Equipment");
		if(equipList.size() < 2){
			throw new Exception("Is required a minimun of 2 Equipment.\n");
		}
		
		return equipList;
				
	}
	
	public static List<String> verifyIfEquipmentMapsOutPorts() throws Exception{
		List<String> equipMappingOut = getEquipmentMappingPorts(true);
		if(equipMappingOut.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Output Interface maps an Output Port from a Source component.\n");
		}
		
		return equipMappingOut;
	}
	
	public static List<String> verifyIfEquipmentMapsInPorts() throws Exception{
		List<String> equipMappingIn = getEquipmentMappingPorts(false);
		if(equipMappingIn.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Input Interface maps an Input Port from a Sink component.\n");
		}
		return equipMappingIn;
	}
	
	public static void verifiyMinimumEquipWithPM() throws Exception{
		List<String> equipWithPm = getEquipmentWithPhysicalMedia();
		if(equipWithPm.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment with Physical Media.\n");
		}		
	}
	
	public static void saveNewOwl(String oldName){
		OntModel model = OKCoUploader.getBaseModel();
		System.out.println("Saving OWL");
		String syntax = "RDF/XML";
		StringWriter out = new StringWriter();
		model.write(out, syntax);
		String result = out.toString();
		oldName = oldName.replace(".owl", "");
		File arquivo = new File(oldName + "New.owl");  // Chamou e nomeou o arquivo txt.  
		if(arquivo.exists()){
			arquivo.delete();
		}
		try{
			FileOutputStream fos = new FileOutputStream(arquivo);  // Perceba que estamos instanciando uma classe aqui. A FileOutputStream. Pesquise sobre ela!  
			fos.write(result.getBytes());    
			fos.close();  // Fecha o arquivo. Nunca esquecer disso.  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void runReasoner() throws Exception{
		DtoResult result = OKCoReasoner.runReasoner();
		
		if(!result.isSucceed()){
			throw new Exception(result.getMessage());
		}
	}
	
	public static void createInstances(String aBoxFile) throws Exception{
		FileReader reader = new FileReader(new File(aBoxFile));
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(new BufferedReader(reader));
        String txtABox = "";
        while (scanner.hasNextLine()){
        	txtABox += scanner.nextLine()+"\n";
        }
        
        String[] fileBlocks = txtABox.replace("\t", "").split("\\*\\*\\*");
        
        if(fileBlocks.length < 4) throw new Exception("Incomplete ABox file.\n");
        
        String[] individualDcls = fileBlocks[1].split(";");
        HashMap<String, String> newMapping = createIndividualInstances(individualDcls);
        String[] relationDcls = fileBlocks[2].split(";");
        createRelationInstances(relationDcls, newMapping);
        String[] attributeDcls = fileBlocks[3].split(";");
        createAttributeInstances(attributeDcls, newMapping);
        
        System.out.println();
	}
	
	public static HashMap<String, String> createIndividualInstances(String[] individualDcls){
		HashMap<String, String> newMapping = new HashMap<String,String>();
		List<String> newIndividuals = new ArrayList<String>();
		OntModel model = OKCoUploader.getBaseModel();
		String ns = model.getNsPrefixURI("");
		for (String indvDcl : individualDcls) {
			indvDcl = indvDcl.replace(" ", "").replace("\n", "");
			String[] indvDclSplit = indvDcl.split(":");
			
			if(indvDclSplit.length >= 2){
			
				String type = indvDclSplit[0];
				String[] individuals = indvDclSplit[1].split(",");
				for (String indv : individuals) {
					boolean indvExist = QueryUtil.individualExists(model, ns+indv);
					String oldName = indv;
					if(indvExist){
						indv += "_eq";
					}
					
					newMapping.put(oldName, indv);
					
					FactoryUtil.createInstanceIndividual(model, ns+indv, ns+type);
					
					newIndividuals.add(ns+indv);
				}
			}
		}
		FactoryUtil.createAllDifferent(model, newIndividuals);
		
		return newMapping;
	}
	
	public static void createRelationInstances(String[] relationDcls, HashMap<String, String> newMapping){
		OntModel model = OKCoUploader.getBaseModel();
		String ns = model.getNsPrefixURI("");
		for (String relDcl : relationDcls) {
			relDcl = relDcl.replace(" ", "").replace("\n", "");
			String[] relDclSplit = relDcl.split(":");
			
			if(relDclSplit.length >= 2){
				String relation = relDclSplit[0].replace("\t", "");
				String[] individuals = relDclSplit[1].split(",");
				for (int i = 0; i < individuals.length; i+=2) {
					String src = individuals[i].replace("(", "").replace("\t", "");
					String tgt = individuals[i+1].replace(")", "").replace("\t", "");
					
					src = newMapping.get(src);
					tgt = newMapping.get(tgt);
					
					FactoryUtil.createInstanceRelation(model, ns+src, ns+relation, ns+tgt);				
					
				}
			}
		}
	}
	
	public static void createAttributeInstances(String[] attributeDcls, HashMap<String, String> newMapping){
		OntModel model = OKCoUploader.getBaseModel();
		String ns = model.getNsPrefixURI("");
		for (String attDcl : attributeDcls) {
			attDcl = attDcl.replace(" ", "").replace("\n", "");
			String[] attDclSplit = attDcl.split(":");
			
			if(attDclSplit.length >= 2){
				String attribute = attDclSplit[0];
				String[] dcl = attDclSplit[1].split(",");
				for (int i = 0; i < dcl.length; i+=3) {
					String indv = dcl[i].replace("(", "");
					String val = dcl[i+1].replace("", "");
					String type = dcl[i+2].replace(")", "");
					
					indv = newMapping.get(indv);
					
					FactoryUtil.createInstanceAttribute(model, ns+indv, ns+attribute, val, "http://www.w3.org/2001/XMLSchema#"+type);
				}
			}
		}
	}
	
	public static List<String> getEquipmentMappingPorts(boolean forOutput){
		String portType = "";
		String tfType = "";
		if(forOutput){
			portType = "Output";
			tfType = "TF_Source";
		}else{
			portType = "Input";
			tfType = "TF_Sink";
		}
		
		OntModel model = OKCoUploader.getBaseModel();
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
				+ "\t?equipment rdf:type ns:Equipment .\n"
				+ "\t?equipment ns:componentOf ?interface .\n"
				+ "\t?interface rdf:type ns:" + portType +"_Interface .\n"
				+ "\t?interface ns:maps ?mappedPort .\n"
				+ "\t?mappedPort ns:INV.componentOf ?tf .\n"
				+ "\t?tf rdf:type ns:" + tfType + " .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode equipment = row.get("equipment");	
		    @SuppressWarnings("unused")
			RDFNode interface_ = row.get("interface");
		    @SuppressWarnings("unused")
			RDFNode mappedPort = row.get("mappedPort");
		    if(QueryUtil.isValidURI(equipment.toString()))
		    {
		    	System.out.println("- Class URI: "+equipment.toString()); 
		    	result.add(equipment.toString()); 
		    }
		}
		return result;
	}
	
	public static List<String> getAvailabeInterfacesFromEquipment(String equipURI, boolean forOutput){
		String portType = "";
		if(forOutput){
			portType = "Output";
		}else{
			portType = "Input";
		}
		
		OntModel model = OKCoUploader.getBaseModel();
		System.out.println("\nExecuting getEquipmentMappingPorts()...");
		List<String> toRemove = new ArrayList<String>();				
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
				+ "	?int1 ns:eq_binds ?int2 ."
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
		    	System.out.println("- Class URI: "+int1.toString()); 
		    	toRemove.add(int1.toString()); 
		    }
		}
		
		List<String> result = new ArrayList<String>();
		queryString = ""
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
		query = QueryFactory.create(queryString); 		
		qe = QueryExecutionFactory.create(query, model);
		results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    RDFNode int1 = row.get("int1");	
		   if(QueryUtil.isValidURI(int1.toString()))
		    {
		    	System.out.println("- Class URI: "+int1.toString()); 
		    	result.add(int1.toString()); 
		    }
		}
		
		result.removeAll(toRemove);
		return result;
	}
	
	public static List<String> getEquipmentWithPhysicalMedia(){
		OntModel model = OKCoUploader.getBaseModel();
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
	
	public static List<String> getLayersAdaptedFromAF(String afURI){
		OntModel model = OKCoUploader.getBaseModel();
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
				+ "<" + afURI + "> ns:adapts_to ?layer ."
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
	
	public static List<String> getServerLayersFromTF(String tfURI){
		OntModel model = OKCoUploader.getBaseModel();
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
				+ "<" + tfURI + "> ns:defines ?layer . "
				+ "?layer ns:client_of ?serverLayer . " 
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
	
	public static boolean isInterfaceMappedByTF(String interfaceURI){
		OntModel model = OKCoUploader.getBaseModel();
		System.out.println("\nExecuting isInterfaceMappedByTF()...");
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <" + model.getNsPrefixURI("") + ">\n"
				+ "ASK"
				+ "WHERE {"
				+ "	<" + interfaceURI + "> ns:maps ?port ."
				+ "	?tf ns:componentOf ?port ."
				+ "	?tf rdf:type ns:Adaptation_Function"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean results = qe.execAsk();		
		
		return results;
	}
	
	public static List<String> getLastBindedEquipmentFrom(String fromEquipURI){
		OntModel model = OKCoUploader.getBaseModel();
		System.out.println("\nExecuting getBindedEquipmentFrom()...");
		List<String> result = new ArrayList<String>();				
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX ns: <http://nemo.inf.ufes.br/NewProject.owl#>\n"
				+ "SELECT *\n"
				+ "WHERE {\n"
				+ "\t?equip1 ns:eq_binds*/ns:eq_binds ?equip2 .\n"
				+ "}\n";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		List<String> aux = new ArrayList<String>();				
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
			RDFNode equip1 = row.get("equip1");	
			RDFNode equip2 = row.get("equip2");	
			if(QueryUtil.isValidURI(equip1.toString()))
		    {
				aux.add(equip1.toString()); 
		    }if(QueryUtil.isValidURI(equip2.toString()))
		    {
		    	System.out.println("- Equipment URI: "+equip2.toString()); 
		    	result.add(equip2.toString()); 
		    }
		}
		result.removeAll(aux);
		return result;
	}
	
	public static int chooseOne(List<String> list, String message) throws Exception{
		OntModel model = OKCoUploader.getBaseModel();
		
		System.out.println();
		System.out.println("--- Choose one " + message);
		
		String ns = model.getNsPrefixURI("");
		for (int i = 0; i < list.size(); i++) {
			String elem = list.get(i).replace(ns, "");
			System.out.println((i+1) + " - " + elem);
		}
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Integer index;
		
		do {
			index = Integer.valueOf(bufferRead.readLine());
		} while (index < 1 || index > list.size());
		return index-1;
	}
	
	public static String algorithmPart1(String equipURI) throws Exception{
		String choosenEquipURI = "";
		
		List<String> bindedEquip = getLastBindedEquipmentFrom(equipURI);
		
		if(bindedEquip.size() == 0){
			choosenEquipURI = equipURI;
		}else{
			int choosen = chooseOne(bindedEquip, "Equipment to Provision");
			choosenEquipURI = bindedEquip.get(choosen);
		}
		
		return choosenEquipURI;
	}
	
	public static String algorithmPart2(String equipURI, boolean isSource) throws Exception{
		List<String> availableInt = getAvailabeInterfacesFromEquipment(equipURI, isSource);
		String type = "";
		if(isSource){
			type = "Output";
		}else{
			type = "Input";
		}
		
		int choosenInt = chooseOne(availableInt, type + " Interface to provision");
		
		String layer = "";
		List<String> layers;
		if(isInterfaceMappedByTF(availableInt.get(choosenInt))){
			layers = getServerLayersFromTF(availableInt.get(choosenInt));
		}else{
			layers = getLayersAdaptedFromAF(availableInt.get(choosenInt));			
		}
		if(layers.size() > 0){
			layer = layers.get(0);
		}
		
		
		
		return "";
	}
}
