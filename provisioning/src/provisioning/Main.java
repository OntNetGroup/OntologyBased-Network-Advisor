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

public class Main {
	static OntModel model;
	static String ns;
	static boolean teste = true;
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
			
			int srcEquip2ProvIndex = chooseOne(equipMappingOut, "Source Equipment to provision");
			String srcEquipToProv = equipMappingOut.get(srcEquip2ProvIndex);
			int tgtEquip2ProvIndex = chooseOne(equipMappingIn, "Target Equipment to provision");
			String tgtEquipToProv = equipMappingIn.get(tgtEquip2ProvIndex);
			
			String possibleEquipFile = FindCertainExtension.chooseFile("available Equipment", ".txt");
			createInstances(possibleEquipFile);
			
			verifiyMinimumEquipWithPM();						
			//runReasoner();
			boolean equipHasPM;
			String lastEquip = srcEquipToProv;
			do {
				String srcEquip = algorithmPart1(lastEquip);
				lastEquip = algorithmPart2(srcEquip, true);
				//runReasoner();
				equipHasPM = QueryUtil.isTargetIndividualFromClass(model, lastEquip, ns+"componentOf", ns+"Physical_Media");
			} while (!equipHasPM);			
			
			lastEquip = tgtEquipToProv;
			do {
				String tgtEquip = algorithmPart1(lastEquip);
				lastEquip = algorithmPart2(tgtEquip, false);
				//runReasoner();
				equipHasPM = QueryUtil.isTargetIndividualFromClass(model, lastEquip, ns+"componentOf", ns+"Physical_Media");
			} while (!equipHasPM);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		saveNewOwl(tBoxFile);
		System.out.println("Done.");
	}
	
	public static void createTBox(String tBoxFile) throws Exception{
		FileInputStream inTBox = new FileInputStream(new File(tBoxFile));
		OKCoUploader.uploadBaseModel(inTBox, "off", "hermit");
		model = OKCoUploader.getBaseModel();
		ns = model.getNsPrefixURI("");
	}
	
	public static List<DtoInstance> verifiyMinimumEquipment() throws Exception{
		List<DtoInstance> equipList = DtoQueryUtil.getIndividualsFromClass(model, "Equipment");
		if(equipList.size() < 2){
			throw new Exception("Is required a minimun of 2 Equipment.\n");
		}
		
		return equipList;
				
	}
	
	public static List<String> verifyIfEquipmentMapsOutPorts() throws Exception{
		List<String> equipMappingOut = Queries.getEquipmentMappingPorts(model, true);
		if(equipMappingOut.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Output Interface maps an Output Port from a Source component.\n");
		}
		
		return equipMappingOut;
	}
	
	public static List<String> verifyIfEquipmentMapsInPorts() throws Exception{
		List<String> equipMappingIn = Queries.getEquipmentMappingPorts(model, false);
		if(equipMappingIn.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Input Interface maps an Input Port from a Sink component.\n");
		}
		return equipMappingIn;
	}
	
	public static void verifiyMinimumEquipWithPM() throws Exception{
		List<String> equipWithPm = Queries.getEquipmentWithPhysicalMedia(model);
		if(equipWithPm.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment with Physical Media.\n");
		}		
	}
	
	public static void saveNewOwl(String oldName){
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
	
	public static int chooseOne(List<String> list, String message) throws Exception{
		System.out.println();
		System.out.println("--- Choose one " + message);
		
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
		
		List<String> bindedEquip = Queries.getLastBindedEquipmentFrom(model, equipURI);
		
		if(bindedEquip.size() == 0){
			choosenEquipURI = equipURI;
		}else{
			int choosen = chooseOne(bindedEquip, "Equipment to Provision");
			choosenEquipURI = bindedEquip.get(choosen);
		}
		
		return choosenEquipURI;
	}
	
	public static String algorithmPart2(String equipURI, boolean isSource) throws Exception{
		List<String> availableInt = Queries.getAvailabeInterfacesFromEquipment(model, equipURI, isSource);
		String type = "";
		if(isSource){
			type = "Output";
		}else{
			type = "Input";
		}
		
		int outIntIndex = chooseOne(availableInt, type + " Output Interface to provision");
		String outInt = availableInt.get(outIntIndex);
		String layerURI = "";
		List<String> layers;
		boolean isTF;
		if(Queries.isInterfaceMappedByTF(model, outInt)){
			layers = Queries.getServerLayersFromTF(model, outInt);
			isTF = true;
		}else{
			layers = Queries.getLayersAdaptedFromAF(model, outInt);
			isTF = false;
		}
		if(layers.size() > 0){
			layerURI = layers.get(0);
		}
		
		List<String> listEquipmentTo = Queries.getCompatibleEquipment(model, layerURI, isSource, isTF);
		
		int inEquipIndex = chooseOne(listEquipmentTo, " Equipment");
		String inEquip = listEquipmentTo.get(inEquipIndex);
		
		List<String> outPortURIs = Queries.getMappedPort(model, outInt);
		String outPort = "";
		if(outPortURIs.size() > 0){
			outPort  = outPortURIs.get(0);
		}
		
		List<String> listInInt = Queries.getAvailabeInterfacesFromEquipment(model, inEquip, !isSource);
		
		List<String> inPortURIs = new ArrayList<String>();
		
		int inIntIndex = chooseOne(listInInt, " Interface");
		String inInt = listInInt.get(inIntIndex);
		inPortURIs = Queries.getMappedPort(model, inInt);
		
		String inPort = "";
		if(inPortURIs.size() > 0){
			inPort = inPortURIs.get(0);
		}
		
		FactoryUtil.createInstanceRelation(model, outPort, ns+"binds", inPort);
		
		return inEquip;
	}
	
	public static void verifyIfEquipProvidedBySamePM(String srcEquipToProv, String tgtEquipToProv) throws Exception{
		List<String> lastSrcEquipList = Queries.getLastBindedEquipmentFrom(model, srcEquipToProv);
		List<String> lastTgtEquipList = Queries.getLastBindedEquipmentFrom(model, tgtEquipToProv);
		
		if(lastSrcEquipList.size() == 0 || lastTgtEquipList.size() == 0){
			throw new Exception("No equipment found.");
		}
		
		String lastSrcEquip = lastSrcEquipList.get(0);
		String lastTgtEquip = lastTgtEquipList.get(0);
		
		if(!lastSrcEquip.equals(lastTgtEquip)){
			throw new Exception("Something went wrong. Source and Target equipment were provisioned by different Physical Media.");
		}
	}
}
