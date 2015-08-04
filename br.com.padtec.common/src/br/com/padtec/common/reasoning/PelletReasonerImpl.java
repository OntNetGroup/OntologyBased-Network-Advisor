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
	
	/** 
	 * Runs the inference using Pellet
	 * 
	 * @author John Guerson
	 */
	public InfModel run(OntModel baseModel)
	{
		Reasoner r = (PelletReasoner)PelletReasonerFactory.theInstance().create();		
//		long antes = System.currentTimeMillis();  
		Date beginDate = new Date();
		
		InfModel infModel = ModelFactory.createInfModel(r, baseModel);	 
		
		// Create an inference extractor
		ModelExtractor extractor = new ModelExtractor(infModel);
		// Extract default set of inferences
		Model inferences = extractor.extractModel();
		infModel.add(inferences);
		
	  	this.reasoningTimeExec = PerformanceUtil.getExecutionTime(beginDate);
	  	PerformanceUtil.printExecutionTime("Pellet reasoning finished", beginDate);
//        long tempo = System.currentTimeMillis() - antes;        
//        System.out.printf("Pellet executed in %d miliseconds.%n", tempo);	  	
		return infModel;
	}
}
