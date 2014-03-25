package UserInterface;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import Api.OKCoAPI;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;

public class Vitor {

	public static void main(String[] args) {

		String inputFileName = "C:/Users/Coras/Desktop/bateria1.owl";
		
		JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int i= file.showOpenDialog(null);
        if (i==1){
        	inputFileName = "";
        } else {
        	
        	File arquivo = file.getSelectedFile();
        	String NS = "http://www.semanticweb.org/parentesco-simples.owl#";
        	OKCoAPI okco = new OKCoAPI(arquivo,NS);
        	okco.insertIndividualInClass("Rogner", "Son");
        	//System.out.println(okco.getOWL());
        	
        }
		
	}
	
	
	public void runReasoner(OntModel model) {

		// IMPORTANT: The option to enable tracing should be turned
		// on before the ontology is loaded to the reasoner!
		PelletOptions.USE_TRACING = true;
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();
		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// get the underlying Pellet graph
		PelletInfGraph pellet = (PelletInfGraph) model.getGraph();		
		
		// create an inferencing model using Pellet reasoner
		if( !pellet.isConsistent() ) {
			System.out.println("Modelo inconsistente!");
			// create an inferencing model using Pellet reasoner
			//com.hp.hpl.jena.rdf.model.Model explanation = pellet.explainInconsistency();
			// print the explanation
			//explanation.write( System.out );
		} else {
			System.out.println("Modelo consistente!");
		}
	}

}
