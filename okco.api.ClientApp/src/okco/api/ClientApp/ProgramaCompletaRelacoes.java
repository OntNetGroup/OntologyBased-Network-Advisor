package okco.api.ClientApp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.padtec.okco.domain.FactoryInstances;
import br.com.padtec.okco.domain.HermitReasonerImpl;
import br.com.padtec.okco.domain.ManagerInstances;
import br.com.padtec.okco.domain.OntologyReasoner;
import br.com.padtec.okco.domain.PelletReasonerImpl;
import br.com.padtec.okco.domain.Search;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;


public class ProgramaCompletaRelacoes {

	public static void main(String[] args) {
		
		OntologyReasoner Reasoner = null;
		String reasonerOption = "PELLET";
		
		String pathOwlFile = "C://Users//fabio_000//Desktop//OntologiasOWL//subProperty.owl";
		String pathOut = "C://Users//fabio_000//Desktop//subPropertyOut.owl";
		
		//Select reasoner
		if(reasonerOption.equals("HERMIT"))
		{
			Reasoner = new HermitReasonerImpl();
			  
		} else if(reasonerOption.equals("PELLET")) {
			
			Reasoner = new PelletReasonerImpl();
			
		} else if(reasonerOption.equals("NONE")) {
			
			Reasoner = null;
			
		} else {
			
			  System.out.println("Selecione reasoner válido");
		}		
		
		InputStream in = FileManager.get().open(pathOwlFile);
		if (in == null) {
			System.out.println("Arquivo não encontrado");
		}
		
		//Create model
		OntModel model = null;
		model = ModelFactory.createOntologyModel();
		
		model.read(in,null);		
		String ns = model.getNsPrefixURI("");		  
		if(ns == null)
		{
			System.out.println("Namespace não definido");
		}
		
		Search search = new Search();
	  	FactoryInstances factoryInstance = new FactoryInstances(search);
	  	ManagerInstances managerInstances = new ManagerInstances(search, factoryInstance, model);
	  	
	  	//Call reasoner
	  	InfModel infModel;
	  	if(Reasoner == null)
	  	{
	  		infModel = model;
	  	} else {
	  		infModel = Reasoner.run(model);	
	  	}
	  	
	  	// --------------------------------------- AQUI ---------------------------------- //
	  	
	  	ManagerRelations mRelations = new ManagerRelations(search, managerInstances);
	  	model = mRelations.EnforceSubRelation(model, infModel, ns);
	  	
	  	// --------------------------------------- AQUI ---------------------------------- //
	  	
	  	OutputStream output = null;
		try {
			output = new FileOutputStream(pathOut);
			model.write(output,"RDF/XML");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
