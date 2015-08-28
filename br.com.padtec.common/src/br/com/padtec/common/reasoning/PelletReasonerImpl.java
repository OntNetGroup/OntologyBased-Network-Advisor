package br.com.padtec.common.reasoning;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;

import br.com.padtec.common.persistence.BaseModelRepository;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;




public class PelletReasonerImpl extends OntologyReasoner {

	boolean started = true;
	
	/** 
	 * Runs the inference using Pellet
	 * 
	 * @author John Guerson
	 */
	public InfModel run(BaseModelRepository baseRepository) 
	{		
		return run(baseRepository.getBaseOntModel());
	}
	
	public InfModel run(OntModel baseModel)
	{

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
		
		// create the Pellet reasoner
		PelletReasoner pellet = PelletReasonerFactory.getInstance().createReasoner(o);
		
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
        if(this.inferHierarchies && started){
        	
    		gens.add(new InferredSubClassAxiomGenerator());							//InferredClassAxiomGenerator
    		gens.add(new InferredSubObjectPropertyAxiomGenerator());				//InferredObjectPropertyAxiomGenerator
        	gens.add(new InferredInverseObjectPropertiesAxiomGenerator());			//InferredObjectPropertyAxiomGenerator
        	
        	started = false;
        }
        if(this.inferAssertions && !started){
        	gens.add(new InferredClassAssertionAxiomGenerator()); 					//InferredIndividualAxiomGenerator
    		gens.add(new InferredPropertyAssertionGenerator());						//InferredIndividualAxiomGenerator
        }
		
		System.out.println("AQUI1");

		
		InferredOntologyGenerator iog = new InferredOntologyGenerator(pellet, gens);
		iog.fillOntology(m, o);

//		// add the reasoner as an ontology change listener
//		m.addOntologyChangeListener(pellet);
//		
//		pellet.flush();
		
		System.out.println("AQUI2");
		
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        try {
			m.saveOntology(o, new RDFXMLOntologyFormat(), baos2);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}    
    	
    	bais = new ByteArrayInputStream(baos2.toByteArray());
		model.read(bais, null);
		
		return model;
	}
	
	
	/** 
	 * Runs the inference using Pellet
	 * 
	 * @author John Guerson
	 */
//	public InfModel run(OntModel baseModel)
//	{
//		
////		OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
////		ontModel.add(baseModel);
////		
//		Date beginDate = new Date();
////		
////		// Create an inference extractor
////		ModelExtractor extractor = new ModelExtractor(ontModel);
////		// Extract default set of inferences
////		Model inferences = extractor.extractModel();
////		
////		ontModel.add(inferences);
//		
//		Reasoner pellet = (PelletReasoner) PelletReasonerFactory.theInstance().create();		
//		
//		InfModel infModel = ModelFactory.createInfModel(pellet, baseModel);	 
//		
//		// Create an inference extractor
//		ModelExtractor extractor = new ModelExtractor(infModel);
//		// Extract default set of inferences
//		Model inferences = extractor.extractModel();
//		infModel.add(inferences);
//		
//	  	this.reasoningTimeExec = PerformanceUtil.getExecutionTime(beginDate);
//	  	PerformanceUtil.printExecutionTime("Pellet reasoning finished", beginDate);
//	  	
//		return infModel;
//	}
}
