package provisioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Scanner;

import br.com.padtec.okco.core.application.OKCoUploader;

public class Main {

	public static void main(String[] args) throws Exception{
		String tBoxFile = FindCertainExtension.chooseFile("TBox", ".owl");
		String aBoxFile = FindCertainExtension.chooseFile("ABox", ".txt");
		//String equipFile = FindCertainExtension.chooseFile("available Equipment", ".txt");
		
		FileInputStream inTBox = new FileInputStream(new File(tBoxFile));
		OKCoUploader.uploadBaseModel(inTBox, "off", "hermit");
				
//		List<String> teste = QueryUtil.getClassesURI(OKCoUploader.getInferredModel());
//		for (String string : teste) {
//			System.out.println(string);
//		}
		
		createInstances(aBoxFile);
	}
	
	public static void createInstances(String aBoxFile) throws Exception{
		FileReader reader = new FileReader(new File(aBoxFile));
        Scanner scanner = new Scanner(new BufferedReader(reader));
        String txtABox = "";
        while (scanner.hasNext()){
        	txtABox += scanner.next();
        }
        
        String[] fileBlocks = txtABox.split("\\*\\*\\*");
        
        if(fileBlocks.length < 4) throw new Exception("Incomplete ABox file.");
        
        String[] individuals = fileBlocks[1].split(";");
        createIndividualInstances(individuals);
        String[] relations = fileBlocks[2].split(";");
        createRelationInstances(relations);
        String[] attributes = fileBlocks[3].split(";");
        createAttributeInstances(attributes);
        
        System.out.println();
	}
	
	public static void createIndividualInstances(String[] individuals){
		
	}
	
	public static void createRelationInstances(String[] relations){
		
	}
	
	public static void createAttributeInstances(String[] attributes){
		
	}
}
