package br.com.padtec.common.reasoning;

import java.util.Date;

import org.mindswap.pellet.jena.ModelExtractor;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import br.com.padtec.common.persistence.BaseModelRepository;
import br.com.padtec.common.util.PerformanceUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;


public class PelletReasonerImpl extends OntologyReasoner {

	/** 
	 * Runs the inference using Pellet
	 * 
	 * @author John Guerson
	 */
	public InfModel run(BaseModelRepository baseRepository) 
	{		
		return run(baseRepository.getBaseOntModel());
	}
	
//	public InfModel run(OntModel baseModel)
//	{
//		// create an ontology manager
//		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//
//		//Converting output stream from model to input stream		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//        baseModel.write(out, "RDF/XML");        
//        InputStream in = new ByteArrayInputStream(out.toByteArray());
//		
//		//------------------------------------------------------------//
//		
//		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
//		OWLOntology o = null;
//		try {
//			o = m.loadOntologyFromOntologyDocument(in);			
//		} catch (OWLOntologyCreationException e){
//			e.printStackTrace();
//		}
//		
//		//Used to read in OntModel		
//		OntModel model = baseModel;		
//		//model.read(in,null);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        model.write(baos, "RDF/XML");
//        
//        InputStream bais = new ByteArrayInputStream(baos.toByteArray());
//        BufferedReader in2 = new BufferedReader(new InputStreamReader(bais));
//        try {
//			while ((in2.readLine()) != null) 
//			{
//			    //  System.out.println(line);
//			}
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//        
//        try {
//			m.loadOntologyFromOntologyDocument(bais);
//		} catch (OWLOntologyCreationException e) {			
//			e.printStackTrace();
//		}
//		
//		// create the Pellet reasoner
//		PelletReasoner reasoner = PelletReasonerFactory.getInstance().createNonBufferingReasoner( o );
//
//		// add the reasoner as an ontology change listener
//		manager.addOntologyChangeListener( reasoner );
//		
//		reasoner.flush();
//		
//		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
//        try {
//			m.saveOntology(o, new RDFXMLOntologyFormat(), baos2);
//		} catch (OWLOntologyStorageException e) {
//			e.printStackTrace();
//		}    
//    	
//    	bais = new ByteArrayInputStream(baos2.toByteArray());
//		model.read(bais, null);
//		
//		return model;
//	}
//	
	
	/** 
	 * Runs the inference using Pellet
	 * 
	 * @author John Guerson
	 */
	public InfModel run(OntModel baseModel)
	{
		
//		OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
//		ontModel.add(baseModel);
//		
		Date beginDate = new Date();
//		
//		// Create an inference extractor
//		ModelExtractor extractor = new ModelExtractor(ontModel);
//		// Extract default set of inferences
//		Model inferences = extractor.extractModel();
//		
//		ontModel.add(inferences);
		
		Reasoner pellet = (PelletReasoner) PelletReasonerFactory.theInstance().create();		
		
		InfModel infModel = ModelFactory.createInfModel(pellet, baseModel);	 
		
		// Create an inference extractor
		ModelExtractor extractor = new ModelExtractor(infModel);
		// Extract default set of inferences
		Model inferences = extractor.extractModel();
		infModel.add(inferences);
		
	  	this.reasoningTimeExec = PerformanceUtil.getExecutionTime(beginDate);
	  	PerformanceUtil.printExecutionTime("Pellet reasoning finished", beginDate);
	  	
		return infModel;
	}
}
