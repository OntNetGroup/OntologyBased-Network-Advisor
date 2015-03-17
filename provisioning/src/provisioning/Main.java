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
	static boolean runReason = true;
	static OntModel model;
	static String ns;
	static boolean teste = true;
	public static void main(String[] args){
		String tBoxFile = "";
		try {
			//#1
			tBoxFile = FindCertainExtension.chooseFile("TBox", ".owl");
			createTBox(tBoxFile);
			
			//#2
			String aBoxFile = FindCertainExtension.chooseFile("ABox", ".txt");
			//#3
			createInstances(aBoxFile);
			
			//#4
			runReasoner(true, true, true);
			
			//#7 and #8
			verifiyMinimumEquipment();
			
			//#9
			verifyIfEquipmentMapsOutPortsInSource();
			List<String> INT_SO_LIST = verifyIfEquipmentMapsInPortsInSource();
			verifyIfEquipmentMapsInPortsInSink();
			List<String> INT_SK_LIST = verifyIfEquipmentMapsOutPortsInSink();
			
			//#10 and #11
			int srcInt2ProvIndex = chooseOne(INT_SO_LIST, "Input Interface that maps an Input Port from Source", 2);
			String srcIntToProv = INT_SO_LIST.get(srcInt2ProvIndex);
			//#12 and #13
			int tgtInt2ProvIndex = chooseOne(INT_SK_LIST, "Output Interface that maps an Output Port from Sink", 2);
			String tgtIntToProv = INT_SK_LIST.get(tgtInt2ProvIndex);
			
			//#14
			String possibleEquipFile = FindCertainExtension.chooseFile("available Equipment", ".txt");
			//#15
			createInstances(possibleEquipFile);
			
			//#16
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
			
			//#20 -> #23
			String equipWithPM1 = callAlgorithm(srcIntToProv, true, "");
			
			//#24????????
			
			//#25 -> #28
			String equipWithPM2 = callAlgorithm(tgtIntToProv, false, equipWithPM1);
			
			//#29????????
			if(!equipWithPM1.equals(equipWithPM2)){
				throw new Exception("Somenthing went wrong. The provisioning was made by different Physical Media.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		//#30
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
	
	public static List<String> verifyIfEquipmentMapsOutPortsInSource() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, true, true);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Output Interface maps an Output Port from a Source component.\n");
		}
		
		return equipAndInterfaces;
	}
	
	public static List<String> verifyIfEquipmentMapsInPortsInSource() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, false, true);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Source component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static List<String> verifyIfEquipmentMapsInPortsInSink() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, false, false);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static List<String> verifyIfEquipmentMapsOutPortsInSink() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, true, false);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Output Interface maps an Output Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static void verifiyMinimumEquipWithPM() throws Exception{
		List<String> equipWithPm = Queries.getEquipmentWithPhysicalMedia(model);
		if(equipWithPm.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment with Physical Media.\n");
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
		return chooseOne(list, message, 1);
	}
	
	public static int chooseOne(List<String> list, String message, int increment) throws Exception{
		System.out.println();
		System.out.println("--- Choose one " + message);
		
		if(list.size() == 0){
			throw new Exception("Something went wrong. The list is empty.");
		}
		
		//Collections.sort(list);
		myBubbleSort(list, increment);
		
		for (int i = 0; i < list.size(); i+=increment) {
			String elem = "";
			elem += list.get(i).replace(ns, "");
			
			if(increment > 1){
				elem += " [from equipment: ";
				elem += list.get(i+1).replace(ns, "");
				elem += "]";
			}
			
			int id = (i+increment)/increment;
			
			System.out.println(id + " - " + elem);
		}
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Integer index;
		
		int highestOption = list.size()/increment;
		do {
			index = Integer.valueOf(bufferRead.readLine());
		} while (index < 1 || index > highestOption);
				
		index = (index*increment)-increment;
		
		return index;
	}
	
	public static String callAlgorithm(String lastInt, boolean isSource, String equipWithPM) throws Exception{
		boolean intPartOfEquipPM;
		boolean equipBindedWithPMEquip;
		String equipWithPMRet = "";
		do {
			//#20 and #25
			lastInt = algorithm(lastInt, isSource, equipWithPM);
			
			//#21 and #26
			//runReasoner(false, true, true);
			
			//#23 and #28
			equipWithPMRet = Queries.EquipWithPMofInterface(model, lastInt);
			if(equipWithPMRet.equals("")){
				intPartOfEquipPM = false;
			}else{
				intPartOfEquipPM = true;
				return equipWithPMRet;
			}			
			equipWithPMRet = Queries.equipBindingEquipWithPM(model, lastInt);
			if(equipWithPMRet.equals("")){
				equipBindedWithPMEquip = false;
			}else{
				equipBindedWithPMEquip = true;
				return equipWithPMRet;
			}			
			
		} while (!intPartOfEquipPM && !equipBindedWithPMEquip);//#23 and #28
		
		return equipWithPMRet;
	}
	
	public static String algorithm(String interfaceURI, boolean isSource, String equipWithPM) throws Exception{
		//#A
		String mappedTF = Queries.getMappedTFFrom(model, interfaceURI);
		List<String> bindedTFList = Queries.getLastBindedTFFrom(model, mappedTF);
		bindedTFList.add(mappedTF);
		
		List<String> LIST_INT = new ArrayList<String>();
		for (String tfURI : bindedTFList) {
			LIST_INT.addAll(Queries.getMappingInterfaceFrom(model, tfURI, isSource));
		}
		
		//#B
		String type = "";
		if(isSource){
			type = "Output";
		}else{
			type = "Input";
		}
		int choosenInt2Prov = chooseOne(LIST_INT, type + " Interface to Provision", 2);
		String choosenInt2ProvURI = LIST_INT.get(choosenInt2Prov);
		
		//#C
		List<String> listInterfacesTo = Queries.getInterfacesToProvision(model, choosenInt2ProvURI, isSource, equipWithPM);
		
		//#D
		int inInterfaceIndex = chooseOne(listInterfacesTo, "Available Interface to Provision", 2);
		String inInterface = listInterfacesTo.get(inInterfaceIndex);
		
		//#E
		String outPort = Queries.getMappedPort(model, interfaceURI);
		String inPort = Queries.getMappedPort(model, inInterface);
		FactoryUtil.createInstanceRelation(model, outPort, ns+"binds", inPort);
		
		return inInterface;
	}
	
	public static void verifyIfEquipProvidedBySamePM(String srcEquipToProv, String tgtEquipToProv) throws Exception{
		List<String> lastSrcEquipList = Queries.getLastBindedTFFrom(model, srcEquipToProv);
		List<String> lastTgtEquipList = Queries.getLastBindedTFFrom(model, tgtEquipToProv);
		
		if(lastSrcEquipList.size() == 0 || lastTgtEquipList.size() == 0){
			throw new Exception("No equipment found.");
		}
		
		String lastSrcEquip = lastSrcEquipList.get(0);
		String lastTgtEquip = lastTgtEquipList.get(0);
		
		if(!lastSrcEquip.equals(lastTgtEquip)){
			throw new Exception("Something went wrong. Source and Target equipment were provisioned by different Physical Media.");
		}
	}
	
	public static void myBubbleSort(List<String> list, int increment) {
		boolean troca = true;
        String aux;
        while (troca) {
            troca = false;
            for (int i = 0; i < list.size() - increment; i += increment) {
//            	String a1 = list.get(i);
//            	String a2 = list.get(i + increment);
            	int comparison = list.get(i).compareTo(list.get(i + increment));
            	if(comparison > 0){
            		for(int j = i; j < i + increment; j++){
            			aux = list.get(j);
            			list.set(j, list.get(j + increment));
                        //list.get(j) = list.get(j + increment);
            			list.set(j + increment, aux);
                        //list[j + increment] = aux;
                        
            		}
            		troca = true;
            	}
            }
        }
    }
}
