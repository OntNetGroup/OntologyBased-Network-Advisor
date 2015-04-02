package provisioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoSelector;

public class OWLUtil {
	public static void createTBox(String tBoxFile) throws Exception{
		FileInputStream inTBox = new FileInputStream(new File(tBoxFile));
		Main.okcoUploader.uploadBaseModel(inTBox, "off", "hermit");
		Main.model = Main.okcoUploader.getBaseModel();
		Main.ns = Main.model.getNsPrefixURI("");
	}	
	
	public static void saveNewOwl(String oldName){
		System.out.println("Saving OWL");
		String syntax = "RDF/XML";
		StringWriter out = new StringWriter();
		Main.model.write(out, syntax);
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
		if(!Main.runReason) return;
		Main.okcoUploader.getReasoner().inferAssertions = inferAssertions;
		Main.okcoUploader.getReasoner().inferHierarchies = inferHierarchies;
		Main.okcoUploader.getReasoner().inferRules = inferRules;
		
		OKCoSelector selector = new OKCoSelector(Main.okcoUploader);
		OKCoReasoner okcoReasoner = new OKCoReasoner(Main.okcoUploader, selector);
		//OKCoUploader.reasoner.run(m);
		DtoResult result = okcoReasoner.runReasoner();
		
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
						boolean indvExist = QueryUtil.individualExists(Main.model, Main.ns+indv);
						if(indvExist){
							indv += "_eq";
						}
					}					
					newMapping.put(oldName, indv);
					
					FactoryUtil.createInstanceIndividual(Main.model, Main.ns+indv, Main.ns+type, false);
					
					newIndividuals.add(Main.ns+indv);
				}
			}
		}
		FactoryUtil.createAllDifferent(Main.model, newIndividuals);
		
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
					FactoryUtil.createInstanceRelation(Main.model, Main.ns+newSrc, Main.ns+relation, Main.ns+newTgt, false, false, true);
					
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
					
					FactoryUtil.createInstanceAttribute(Main.model, Main.ns+indv, Main.ns+attribute, val, "http://www.w3.org/2001/XMLSchema#"+type, false);
				}
			}
		}
	}
	
	
}
