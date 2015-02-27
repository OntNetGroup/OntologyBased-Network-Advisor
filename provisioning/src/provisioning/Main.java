package provisioning;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import org.semanticweb.owlapi.model.IRI;

import com.hp.hpl.jena.ontology.OntModel;

public class Main {

	public static void main(String[] args) throws Exception{
		String tBoxFile = FindCertainExtension.chooseFile("TBox", ".owl");
		String aBoxFile = FindCertainExtension.chooseFile("ABox", ".txt");
		//String equipFile = FindCertainExtension.chooseFile("available Equipment", ".txt");
		
		FileInputStream inTBox = new FileInputStream(new File(tBoxFile));
		OKCoUploader.uploadBaseModel(inTBox, "off", "hermit");
				
		createInstances(aBoxFile);
		
		OntModel model = OKCoUploader.getBaseModel();
		List<String> individuals = QueryUtil.getIndividualsURIFromAllClasses(OKCoUploader.getBaseModel());
		for (String indv : individuals) {
			List<DtoInstanceRelation> relations = DtoQueryUtil.getRelationsFromAndTo(model, indv);
			System.out.println(indv);
			for (DtoInstanceRelation dtoInstanceRelation : relations) {
				System.out.println("\t" + dtoInstanceRelation);
			}
		}
		
		String syntax = "RDF/XML";
		StringWriter out = new StringWriter();
		model.write(out, syntax);
		String result = out.toString();		
		File arquivo = new File("arquivo.owl");  // Chamou e nomeou o arquivo txt.  
		try{
			FileOutputStream fos = new FileOutputStream(arquivo);  // Perceba que estamos instanciando uma classe aqui. A FileOutputStream. Pesquise sobre ela!  
			fos.write(result.getBytes());    
			fos.close();  // Fecha o arquivo. Nunca esquecer disso.  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void createInstances(String aBoxFile) throws Exception{
		FileReader reader = new FileReader(new File(aBoxFile));
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(new BufferedReader(reader));
        String txtABox = "";
        while (scanner.hasNext()){
        	txtABox += scanner.next();
        }
        
        String[] fileBlocks = txtABox.split("\\*\\*\\*");
        
        if(fileBlocks.length < 4) throw new Exception("Incomplete ABox file.");
        
        String[] individualDcls = fileBlocks[1].split(";");
        createIndividualInstances(individualDcls);
        String[] relationDcls = fileBlocks[2].split(";");
        createRelationInstances(relationDcls);
        String[] attributeDcls = fileBlocks[3].split(";");
        createAttributeInstances(attributeDcls);
        
        System.out.println();
	}
	
	public static void createIndividualInstances(String[] individualDcls){
		OntModel model = OKCoUploader.getBaseModel();
		String ns = model.getNsPrefixURI("");
		for (String indvDcl : individualDcls) {
			String[] indvDclSplit = indvDcl.split(":");
			
			String type = indvDclSplit[0];
			String[] individuals = indvDclSplit[1].split(",");
			for (String indv : individuals) {
				FactoryUtil.createInstanceIndividual(model, ns+indv, ns+type);
			}
		}
	}
	
	public static void createRelationInstances(String[] relationDcls){
		OntModel model = OKCoUploader.getBaseModel();
		String ns = model.getNsPrefixURI("");
		for (String relDcl : relationDcls) {
			String[] relDclSplit = relDcl.split(":");
			
			String relation = relDclSplit[0];
			String[] individuals = relDclSplit[1].split(",");
			for (int i = 0; i < individuals.length; i+=2) {
				String src = individuals[i].replace("(", "");
				String tgt = individuals[i+1].replace(")", "");
				
				FactoryUtil.createInstanceRelation(model, ns+src, ns+relation, ns+tgt);
			}
		}
	}
	
	public static void createAttributeInstances(String[] attributeDcls){
		OntModel model = OKCoUploader.getBaseModel();
		String ns = model.getNsPrefixURI("");
		for (String attDcl : attributeDcls) {
			String[] attDclSplit = attDcl.split(":");
			
			String attribute = attDclSplit[0];
			String[] dcl = attDclSplit[1].split(",");
			for (int i = 0; i < dcl.length; i+=3) {
				String indv = dcl[i].replace("(", "");
				String val = dcl[i+1].replace("", "");
				String type = dcl[i+1].replace(")", "");
				
				FactoryUtil.createInstanceAttribute(model, ns+indv, ns+attribute, val, "http://www.w3.org/2001/XMLSchema#"+type);
			}
		}
	}
}
