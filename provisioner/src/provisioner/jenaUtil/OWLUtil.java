package provisioner.jenaUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.OntModel;

public class OWLUtil {
	public static void createTBox(OKCoUploader okcoUploader, String baseTBoxFile, String consistencyTBoxFile) throws Exception{
		FileInputStream inBaseTBox = new FileInputStream(new File(baseTBoxFile));
		okcoUploader.uploadBaseModel(inBaseTBox, "off", "hermit");
		FileInputStream inConsistencyTBox = new FileInputStream(new File(consistencyTBoxFile));
		okcoUploader.uploadConsistencyBaseModel(inConsistencyTBox);
//		OntModel model = okcoUploader.getBaseModel();
		//ns = model.getNsPrefixURI("");
//		return model;
	}	
	
	public static void saveNewOwl(OntModel model, String path, String oldName) throws IOException{
		System.out.println("Saving OWL");
		String syntax = "RDF/XML";
		StringWriter out = new StringWriter();
		model.write(out, syntax);
		String result = out.toString();
		oldName = oldName.replace(".owl", "");
//		Date dt = new Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		Date now = new Date();
		String nowStr = DateFormat.getInstance().format(now).replace("/", "-").replace(":", ".");
		
		File arquivo = new File(path + oldName + "New " + nowStr.replaceAll(":", "") + ".owl");   
		if(arquivo.exists()){
			arquivo.delete();
		}
		FileOutputStream fos = new FileOutputStream(arquivo);  
		fos.write(result.getBytes());    
		fos.close();   
	}
	
	public static long runReasoner(OKCoUploader okcoUploader, boolean inferHierarchies, boolean inferAssertions, boolean inferRules) throws Exception{
		okcoUploader.getReasoner().inferAssertions = inferAssertions;
		okcoUploader.getReasoner().inferHierarchies = inferHierarchies;
		okcoUploader.getReasoner().inferRules = inferRules;
		
		OKCoSelector selector = new OKCoSelector(okcoUploader);
		OKCoReasoner okcoReasoner = new OKCoReasoner(okcoUploader, selector);
		//OKCoUploader.reasoner.run(m);
		DtoResult result = okcoReasoner.runReasoner();
		
		if(!result.isSucceed()){
			throw new Exception(result.getMessage());
		}
		
		if(okcoUploader.isConsistencyModelValid()){
			System.out.println("Congratulations! You have a valid model!");
		}else{
			throw new Exception("Something went wrong. Your model is inconsistent.");
		}
		
		return okcoReasoner.getLastReasoningTimeExec();
	}
	
	public static void createInstances(ArrayList<OntModel> models, String aBoxFile, int createNTimes) throws Exception{
		if(aBoxFile == null || aBoxFile.equals("")) return;
		
		FileReader reader = new FileReader(new File(aBoxFile));
        Scanner scanner = new Scanner(new BufferedReader(reader));
        String txtABox = "";
        while (scanner.hasNextLine()){
        	txtABox += scanner.nextLine()+"\n";
        }
        scanner.close();
        String[] fileBlocks = txtABox.replace("\t", "").split("\\*\\*\\*");
        
        if(fileBlocks.length < 4) throw new Exception("Incomplete ABox file.\n");
        
        String[] individualDcls = fileBlocks[1].split(";");
        String[] relationDcls = fileBlocks[2].split(";");
        String[] attributeDcls = fileBlocks[3].split(";");
        
        for (OntModel model : models) {
        	FactoryUtil factory = new FactoryUtil();
            HashMap<String, String> newMapping = createIndividualInstances(model, individualDcls, createNTimes, factory);
            
            factory.processStatements(model);
            
            createRelationInstances(model, relationDcls, newMapping, createNTimes, factory);
            createAttributeInstances(model, attributeDcls, newMapping, 1, factory);
            
            factory.processStatements(model);
            FactoryUtil.createAllDifferent(model, factory.getNewIndividuals());
		}        
        
        System.out.println();
	}
	
	public static HashMap<String, String> createIndividualInstances(OntModel model, String[] individualDcls, int createNTimes, FactoryUtil factory) throws Exception{
		List<String> existentIndividuals = QueryUtil.getIndividualsURIFromAllClasses(model);
		
		String ns = model.getNsPrefixURI("");
		HashMap<String, String> newMapping = new HashMap<String,String>();
//		List<String> newIndividuals = new ArrayList<String>();
		
		for (int j = 1; j <= createNTimes; j++) {
			for (String indvDcl : individualDcls) {
				indvDcl = indvDcl.replace(" ", "").replace("\n", "");
				String[] indvDclSplit = indvDcl.split(":");
				
				if(indvDclSplit.length >= 2){
					String type = indvDclSplit[0];
					
					String[] individuals = indvDclSplit[1].split(",");
					for (String indv : individuals) {
						if(!indv.equals("layer0") || !factory.newIndividualsContains("layer0")){
							String lowerLayer = "";
							if(createNTimes > 1 && !indv.equals("layer0")){
								if(type.equals("Layer_Network")){
									if(j > 1){
										lowerLayer  = indv + (j-1);
									}									
								}else{
									indv += "_layer";
								}
								indv += j;
							}
							String oldName = indv;
							if(!type.equals("Layer_Network")){
								//boolean indvExist = QueryUtil.individualExists(model, ns+indv);
								boolean indvExist = existentIndividuals.contains(ns+indv);
								if(indvExist){
									indv += "_eq";
								}else{
									existentIndividuals.add(ns+indv);
								}
							}					
							newMapping.put(oldName, indv);
							
//							FactoryUtil.createInstanceIndividual(model, ns+indv, ns+type, false);
							factory.createInstanceIndividualStatement(model, ns+indv, ns+type, false);
							if(type.equals("Layer_Network") && j > 1 && !indv.equals("layer0")){
								factory.createInstanceRelationStatement(model, ns+indv, ns+"client_of", ns+lowerLayer, false);
//								FactoryUtil.createInstanceRelation(model, ns+indv, ns+"client_of", ns+lowerLayer, false, false, false);
							}
							
							factory.addNewIndividual(ns+indv);
						}
						
					}					
				}
			}
		}
		
		return newMapping;
	}
	
	public static void createRelationInstances(OntModel model, String[] relationDcls, HashMap<String, String> newMapping, int createNTimes, FactoryUtil factory) throws Exception{
		String ns = model.getNsPrefixURI("");
		for (int j = 0; j < createNTimes; j++) {
			for (String relDcl : relationDcls) {
				relDcl = relDcl.replace(" ", "").replace("\n", "");
				String[] relDclSplit = relDcl.split(":");
				
				if(relDclSplit.length >= 2){
					String relation = relDclSplit[0].replace("\t", "");
					String[] individuals = relDclSplit[1].split(",");
					for (int i = 0; i < individuals.length; i+=2) {
						String src = individuals[i].replace("(", "").replace("\t", "");
						if(i+1 == individuals.length){
							System.out.println();
						}
						String tgt = individuals[i+1].replace(")", "").replace("\t", "");
						
						if(createNTimes > 1){
							if(src.equals("layer0")){
								src = "layer" + j;
							}else {
								if(!src.equals("layer")){
									src += "_layer";
								}
								src += (j+1);
							}
							
							if(tgt.equals("layer0")){
								tgt = "layer" + j;
							}else {
								if(!tgt.equals("layer")){
									tgt += "_layer";
								}
								tgt += (j+1);
							}
							
							
//							if(!src.equals("layer0")){
//								src += (j+1);								
//							}else{
//								src = "layer" + j;
//							}
//							if(!tgt.equals("layer0")){
//								tgt += (j+1);								
//							}else{
//								src = "layer" + j;
//							}
						}
						
						String newSrc = newMapping.get(src);
						String newTgt = newMapping.get(tgt);
						
						if(newSrc == null || newSrc.equals("")){
							newSrc = src;
						}
						if(newTgt == null || newTgt.equals("")){
							newTgt = tgt;
						}					
//						FactoryUtil.createInstanceRelation(model, ns+newSrc, ns+relation, ns+newTgt, false, false, true);
						factory.createInstanceRelationStatement(model, ns+newSrc, ns+relation, ns+newTgt, true);
//						if(relation.equals("adapts_to")){
//							List<String> serverLayerURIs = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, ns+newTgt, ns+"client_of", ns+"Layer_Network");
//							for (String serverLayerURI : serverLayerURIs) {
//								List<String> serverAfURIs = QueryUtil.getIndividualsURIAtObjectPropertyDomain(model, serverLayerURI, ns+"adapts_to", ns+"Adaptation_Function");
//								for (String serverAfURI : serverAfURIs) {
//									FactoryUtil.createInstanceRelation(model, serverAfURI, ns+"adapts_from", ns+newSrc, false, false, false);
//								}
//								System.out.println();
//							}							
//						}
					}
				}
			}
		}
		
	}
	
	public static void createAttributeInstances(OntModel model, String[] attributeDcls, HashMap<String, String> newMapping, int createNTimes, FactoryUtil factory){
		String ns = model.getNsPrefixURI("");
		for (int j = 0; j < createNTimes; j++) {
			for (String attDcl : attributeDcls) {
				attDcl = attDcl.replace(" ", "").replace("\n", "");
				String[] attDclSplit = attDcl.split(":");
				
				if(attDclSplit.length >= 2){
					String attribute = attDclSplit[0];
					String[] dcl = attDclSplit[1].split(",");
					for (int i = 0; i < dcl.length; i+=3) {
						String indv = dcl[i].replace("(", "");
						if(!indv.equals("layer0") || createNTimes <= 1){
							if(createNTimes > 1){
								if(indv.equals("layer0")){
									indv = "layer" + j;
								}else {
									if(!indv.equals("layer")){
										indv += "_layer";
									}
									indv += (j+1);
								}
								
								indv += "_layer" + j;
							}
							String val = dcl[i+1].replace("", "");
							String type = dcl[i+2].replace(")", "");
							String newIndv = newMapping.get(indv);
							
//							FactoryUtil.createInstanceAttribute(model, ns+indv, ns+attribute, val, "http://www.w3.org/2001/XMLSchema#"+type, false);
							if(newIndv == null){
								System.out.println();
								Set<Entry<String, String>> entries = newMapping.entrySet();
								for (Entry <String,String> entry : entries) {
									if(entry.getKey().startsWith(indv) || entry.getValue().startsWith(indv)){
										System.out.println(entry);
										System.out.println();
									}
								}
							}
							factory.createInstanceAttributeStatement(model, ns+newIndv, ns+attribute, val, "http://www.w3.org/2001/XMLSchema#"+type);
						}
						
					}
				}
			}
		}		
	}
	
	
}
