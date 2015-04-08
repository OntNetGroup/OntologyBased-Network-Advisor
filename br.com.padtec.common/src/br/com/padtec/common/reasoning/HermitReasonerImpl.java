package br.com.padtec.common.reasoning;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;

import br.com.padtec.common.persistence.BaseModelRepository;
import br.com.padtec.common.util.PerformanceUtil;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class HermitReasonerImpl extends OntologyReasoner {

	/** 
	 * Runs the inference using Pellet
	 * 
	 * @author John Guerson
	 */
	public InfModel run(BaseModelRepository baseRepository) 
	{		
		return run(baseRepository.getBaseOntModel());
	}
	
	/** 
	 * Runs the inference using Pellet
	 * 
	 * @author John Guerson
	 */
	public InfModel run(OntModel baseModel)
	{
		//long antes = System.currentTimeMillis();  
		Date beginDate = new Date();
		
		//Converting output stream from model to input stream		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        baseModel.write(out, "RDF/XML");        
        InputStream in = new ByteArrayInputStream(out.toByteArray());
		
		//------------------------------------------------------------//
		
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		OWLOntology o = null;
		try {
			o = m.loadOntologyFromOntologyDocument(in);			
		} catch (OWLOntologyCreationException e){
			e.printStackTrace();
		}
		
		//Hermit Configuration		
		Configuration config = new Configuration();
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		config.reasonerProgressMonitor = progressMonitor;		
		
		//Create Hermit        
		OWLReasoner hermit = new Reasoner.ReasonerFactory().createReasoner(o, config);
				
		//Used to read in OntModel		
		OntModel model = baseModel;		
		//model.read(in,null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        model.write(baos, "RDF/XML");
        
        InputStream bais = new ByteArrayInputStream(baos.toByteArray());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(bais));
        try {
			while ((in2.readLine()) != null) 
			{
			    //  System.out.println(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        try {
			m.loadOntologyFromOntologyDocument(bais);
		} catch (OWLOntologyCreationException e) {			
			e.printStackTrace();
		}
        
        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
        
        //InferredEntityAxiomGenerator
        if(this.inferHierarchies){
        	//gens.add(new InferredDisjointClassesAxiomGenerator());				//InferredClassAxiomGenerator
        	gens.add(new InferredEquivalentClassAxiomGenerator());					//InferredClassAxiomGenerator
    		gens.add(new InferredSubClassAxiomGenerator());							//InferredClassAxiomGenerator
    		
    		//gens.add(new InferredDataPropertyCharacteristicAxiomGenerator());		//InferredDataPropertyAxiomGenerator
    		gens.add(new InferredEquivalentDataPropertiesAxiomGenerator());			//InferredDataPropertyAxiomGenerator
    		gens.add(new InferredSubDataPropertyAxiomGenerator());					//InferredDataPropertyAxiomGenerator
    		
    		gens.add(new InferredEquivalentObjectPropertyAxiomGenerator());			//InferredObjectPropertyAxiomGenerator
    		gens.add(new InferredInverseObjectPropertiesAxiomGenerator());			//InferredObjectPropertyAxiomGenerator
    		//gens.add(new InferredObjectPropertyCharacteristicAxiomGenerator());	//InferredObjectPropertyAxiomGenerator
    		gens.add(new InferredSubObjectPropertyAxiomGenerator());				//InferredObjectPropertyAxiomGenerator
        }
        if(this.inferAssertions){
        	gens.add(new InferredClassAssertionAxiomGenerator()); 					//InferredIndividualAxiomGenerator
    		gens.add(new InferredPropertyAssertionGenerator());						//InferredIndividualAxiomGenerator
        }
		
		InferredOntologyGenerator iog = new InferredOntologyGenerator(hermit, gens);
		iog.fillOntology(m, o);

		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        try {
			m.saveOntology(o, new RDFXMLOntologyFormat(), baos2);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}    
    	
    	bais = new ByteArrayInputStream(baos2.toByteArray());
		model.read(bais, null);
		
		PerformanceUtil.printExecutionTime("Hermit reasoning finished", beginDate );
//		long tempo = System.currentTimeMillis() - antes;        
//		System.out.printf("Hermit executed in %d miliseconds.%n", tempo);
		
		Node thingNode = Node.createURI("http://www.w3.org/2002/07/owl#Thing");
		RDFNode thingRdfNode = model.getRDFNode(thingNode);
		model.removeAll(null, null, thingRdfNode);
		
		Property topOP = model.getProperty("http://www.w3.org/2002/07/owl#topObjectProperty");
		model.removeAll(null, topOP, null);
		
		Property topDP = model.getProperty("http://www.w3.org/2002/07/owl#topDataProperty");
		model.removeAll(null, topDP, null);
		return model;
	}
}
