package provisioner.jenaUtil;

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

import com.hp.hpl.jena.ontology.OntModel;

import br.com.padtec.common.dto.DtoResult;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;

public class OWLUtil {
	public static OntModel createTBox(OKCoUploader okcoUploader, String tBoxFile) throws Exception{
		FileInputStream inTBox = new FileInputStream(new File(tBoxFile));
		okcoUploader.uploadBaseModel(inTBox, "off", "hermit");
		OntModel model = okcoUploader.getBaseModel();
		//ns = model.getNsPrefixURI("");
		return model;
	}	
	
	public static void saveNewOwl(OntModel model, String path, String oldName){
		System.out.println("Saving OWL");
		String syntax = "RDF/XML";
		StringWriter out = new StringWriter();
		model.write(out, syntax);
		String result = out.toString();
		oldName = oldName.replace(".owl", "");
		File arquivo = new File(path + oldName + "New.owl");  // Chamou e nomeou o arquivo txt.  
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
		
		return okcoReasoner.getLastReasoningTimeExec();
	}
	
	public static void createInstances(OntModel model, String aBoxFile, int createNTimes) throws Exception{
		if(aBoxFile == null || aBoxFile.equals("")) return;
		
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
        HashMap<String, String> newMapping = createIndividualInstances(model, individualDcls, createNTimes);
        String[] relationDcls = fileBlocks[2].split(";");
        createRelationInstances(model, relationDcls, newMapping, createNTimes);
        String[] attributeDcls = fileBlocks[3].split(";");
        createAttributeInstances(model, attributeDcls, newMapping, 1);
        
        //System.out.println();
	}
	
	public static HashMap<String, String> createIndividualInstances(OntModel model, String[] individualDcls, int createNTimes) throws Exception{
		String ns = model.getNsPrefixURI("");
		HashMap<String, String> newMapping = new HashMap<String,String>();
		List<String> newIndividuals = new ArrayList<String>();
		for (int j = 1; j <= createNTimes; j++) {
			for (String indvDcl : individualDcls) {
				indvDcl = indvDcl.replace(" ", "").replace("\n", "");
				String[] indvDclSplit = indvDcl.split(":");
				
				if(indvDclSplit.length >= 2){
					String type = indvDclSplit[0];
					
					String[] individuals = indvDclSplit[1].split(",");
					for (String indv : individuals) {
						String lowerLayer = "";
						if(createNTimes > 1){
							if(type.equals("Layer_Network")){
								if(j > 1){
									lowerLayer  = indv + (j-1);
								}								
							}
							indv += j;
						}
						String oldName = indv;
						if(!type.equals("Layer_Network")){
							boolean indvExist = QueryUtil.individualExists(model, ns+indv);
							if(indvExist){
								indv += "_eq";
							}
						}					
						newMapping.put(oldName, indv);
						
						FactoryUtil.createInstanceIndividual(model, ns+indv, ns+type, false);
						
						if(type.equals("Layer_Network") && j > 1){
							FactoryUtil.createInstanceRelation(model, ns+indv, ns+"client_of", ns+lowerLayer, false, false, false);
						}
						
						newIndividuals.add(ns+indv);
					}
				}
			}
		}
		
		FactoryUtil.createAllDifferent(model, newIndividuals);
		
		return newMapping;
	}
	
	public static void createRelationInstances(OntModel model, String[] relationDcls, HashMap<String, String> newMapping, int createNTimes) throws Exception{
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
						String tgt = individuals[i+1].replace(")", "").replace("\t", "");
						
						if(createNTimes > 1){
							if(!src.equals("layer0")){
								src += (j+1);
							}
							if(!tgt.equals("layer0")){
								tgt += (j+1);
							}
						}
						
						String newSrc = newMapping.get(src);
						String newTgt = newMapping.get(tgt);
						
						if(newSrc == null || newSrc.equals("")){
							newSrc = src;
						}
						if(newTgt == null || newTgt.equals("")){
							newTgt = tgt;
						}					
						FactoryUtil.createInstanceRelation(model, ns+newSrc, ns+relation, ns+newTgt, false, false, true);
						
						if(relation.equals("adapts_to")){
							List<String> serverLayerURIs = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, ns+newTgt, ns+"client_of", ns+"Layer_Network");
							for (String serverLayerURI : serverLayerURIs) {
								List<String> serverAfURIs = QueryUtil.getIndividualsURIAtObjectPropertyDomain(model, serverLayerURI, ns+"adapts_to", ns+"Adaptation_Function");
								for (String serverAfURI : serverAfURIs) {
									FactoryUtil.createInstanceRelation(model, serverAfURI, ns+"adapts_from", ns+newSrc, false, false, false);
								}
								System.out.println();
							}							
						}
					}
				}
			}
		}
		
	}
	
	public static void createAttributeInstances(OntModel model, String[] attributeDcls, HashMap<String, String> newMapping, int createNTimes){
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
						if(createNTimes > 1){
							indv += "_layer" + j;
						}
						String val = dcl[i+1].replace("", "");
						String type = dcl[i+2].replace(")", "");
						
						indv = newMapping.get(indv);
						
						FactoryUtil.createInstanceAttribute(model, ns+indv, ns+attribute, val, "http://www.w3.org/2001/XMLSchema#"+type, false);
					}
				}
			}
		}		
	}
	
	
}
