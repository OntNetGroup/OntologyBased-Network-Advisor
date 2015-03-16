package provisioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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
import com.hp.hpl.jena.rdf.model.InfModel;

public class Main {
	static boolean runReason = true;
	static OntModel model;
	static String ns;
	static boolean teste = true;
	public static void main(String[] args){
		String tBoxFile = "";
		try {
			//#1
			tBoxFile = FindCertainExtension.chooseFile("TBox", ".owl");
			//#2
			String aBoxFile = FindCertainExtension.chooseFile("ABox", ".txt");
			//#11
			String possibleEquipFile = FindCertainExtension.chooseFile("available Equipment", ".txt");
			
			//#1
			createTBox(tBoxFile);
			//#3
			createInstances(aBoxFile);
						
			//#4
			runReasoner(true, true, true);
			
			//#7 and #8
			verifiyMinimumEquipment();
			
			//#9
			List<String> equipMappingOut = verifyIfEquipmentMapsOutPorts();
			List<String> equipMappingIn = verifyIfEquipmentMapsInPorts();
			
			//#10
			int srcEquip2ProvIndex = chooseOne(equipMappingOut, "Source Equipment to provision");
			String srcEquipToProv = equipMappingOut.get(srcEquip2ProvIndex);
			int tgtEquip2ProvIndex = chooseOne(equipMappingIn, "Target Equipment to provision");
			String tgtEquipToProv = equipMappingIn.get(tgtEquip2ProvIndex);
			
			//#12
			createInstances(possibleEquipFile);
			
			//#13
			verifiyMinimumEquipWithPM();
			
			//#14
			runReasoner(false, true, true);
			
//			List<String> equips = QueryUtil.getIndividualsURI(model, ns+"Equipment");
//			List<DtoInstanceRelation> equipRels = DtoQueryUtil.getRelationsFrom(model, ns+"peq_otu_so");
//			List<DtoInstanceRelation> inIntRel = DtoQueryUtil.getRelationsFrom(model, ns+"peq_otu_so_in");
//			List<String> inIntClasses = QueryUtil.getClassesURIFromIndividual(model, ns+"peq_otu_so_in");
//			List<DtoInstanceRelation> portRels = DtoQueryUtil.getRelationsFromAndTo(model, ns+"af_otu_so_in");
//			List<DtoInstanceRelation> tfRels = DtoQueryUtil.getRelationsFrom(model, ns+"af_otu_so");
//			List<String> tfClasses = QueryUtil.getClassesURIFromIndividual(model, ns+"af_otu_so");
			boolean equipHasPM;
			boolean equipBindedWithPMEquip;
			String lastEquip = srcEquipToProv;
			do {
				//#17
				lastEquip = algorithmPart1(lastEquip, true);
				//lastEquip = algorithmPart2(srcEquip, true);
				
				//#18
				runReasoner(false, true, true);
				
				//#20
				equipHasPM = QueryUtil.hasTargetIndividualFromClass(model, lastEquip, ns+"componentOf", ns+"Physical_Media");
				equipBindedWithPMEquip = Queries.isEquipBindedWithPMEquip(model, lastEquip, true);
				
			} while (!equipHasPM && !equipBindedWithPMEquip);//#20			
			
			
			lastEquip = tgtEquipToProv;
			do {
				//#22
				lastEquip = algorithmPart1(lastEquip, false);
				//lastEquip = algorithmPart2(tgtEquip, false);
				
				//#23
				runReasoner(false, true, true);
				
				//#25
				equipHasPM = QueryUtil.hasTargetIndividualFromClass(model, lastEquip, ns+"componentOf", ns+"Physical_Media");
				equipBindedWithPMEquip = Queries.isEquipBindedWithPMEquip(model, lastEquip, false);
				
			} while (!equipHasPM && !equipBindedWithPMEquip);//#25
			
			//#26
			//???
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		//#27
		saveNewOwl(tBoxFile);
		
		System.out.println();
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
	
	public static void runReasoner(boolean inferHierarchies, boolean inferAssertions, boolean inferRules) throws Exception{
		if(!runReason) return;
		OKCoUploader.reasoner.inferAssertions = inferAssertions;
		OKCoUploader.reasoner.inferHierarchies = inferHierarchies;
		OKCoUploader.reasoner.inferRules = inferRules;
		
		//OKCoUploader.reasoner.run(m);
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
					String oldName = indv;
					if(!type.equals("Layer_Network")){
						boolean indvExist = QueryUtil.individualExists(model, ns+indv);
						if(indvExist){
							indv += "_eq";
						}
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
					
					String newSrc = newMapping.get(src);
					String newTgt = newMapping.get(tgt);
					
					if(newSrc == null || newSrc.equals("")){
						newSrc = src;
					}
					if(newTgt == null || newTgt.equals("")){
						newTgt = tgt;
					}					
					FactoryUtil.createInstanceRelation(model, ns+newSrc, ns+relation, ns+newTgt);
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
		
		Collections.sort(list);
		
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
	
	public static String algorithmPart1(String equipURI, boolean isSource) throws Exception{
		String choosenEquipURI = "";
		
		//#17.1.1
		List<String> bindedEquip = Queries.getLastBindedEquipmentFrom(model, equipURI);
		
		if(bindedEquip.size() == 0){
			choosenEquipURI = equipURI;
		}else{
			//#17.1.3 and #17.1.4
			int choosen = chooseOne(bindedEquip, "Equipment to Provision");
			choosenEquipURI = bindedEquip.get(choosen);
		}
		
		//#17.1.2 and #17.1.5
		return algorithmPart2(choosenEquipURI, isSource);
		//return choosenEquipURI;
	}
	
	public static String algorithmPart2(String equipURI, boolean isSource) throws Exception{
		//#17.2.1
		List<String> availableInt = Queries.getAvailableInterfacesFromEquipment(model, equipURI, isSource);
		String type = "";
		if(isSource){
			type = "Output";
		}else{
			type = "Input";
		}
		
		//#17.2.2
		int outIntIndex = chooseOne(availableInt, type + " Interface to provision");
		
		String outInt = availableInt.get(outIntIndex);
		String layerURI = "";
		List<String> layers;
		boolean isTF;
		
		//#17.2.3
		if(Queries.isInterfaceMappedByTF(model, outInt)){
			//#17.2.4
			layers = Queries.getServerLayersFromTF(model, outInt);
			isTF = true;
		}else{
			//#17.2.5
			layers = Queries.getLayersAdaptedFromAF(model, outInt);
			isTF = false;
		}
		
		if(layers.size() > 0){
			layerURI = layers.get(0);
		}
		InfModel infModel = OKCoUploader.getInferredModel();
		if(infModel.equals(model)){
			System.out.println("teste");
		}else{
			System.out.println("teste2");
		}
		//#17.2.6
		List<String> listEquipmentTo = Queries.getCompatibleEquipment(model, layerURI, isSource, isTF);
		//List<String> listEquipmentTo2 = Queries.getCompatibleEquipment((OntModel) infModel, layerURI, isSource, isTF);
		
		//#17.2.7
		int inEquipIndex = chooseOne(listEquipmentTo, "Equipment");
		String inEquip = listEquipmentTo.get(inEquipIndex);
		
		//#17.2.8
		List<String> listInInt = Queries.getAvailableInterfacesFromEquipment(model, inEquip, !isSource);
		
		//#17.2.9
		int inIntIndex = chooseOne(listInInt, "Interface");
		String inInt = listInInt.get(inIntIndex);
		
		//#17.2.10
		List<String> outPortURIs = Queries.getMappedPort(model, outInt);
		String outPort = "";
		if(outPortURIs.size() > 0){
			outPort  = outPortURIs.get(0);
		}
		
		List<String> inPortURIs = Queries.getMappedPort(model, inInt);
		String inPort = "";
		if(inPortURIs.size() > 0){
			inPort = inPortURIs.get(0);
		}
		
		//#17.2.10
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
