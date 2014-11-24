package br.com.padtec.common.reasoning;

import org.mindswap.pellet.jena.PelletReasoner;

import br.com.padtec.common.persistence.BaseModelRepository;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;

public class PelletReasonerImpl implements OntologyReasoner {

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
		Reasoner r = new PelletReasoner();		
		long antes = System.currentTimeMillis();  
	  	InfModel infModel = ModelFactory.createInfModel(r, baseModel);	  	  
        long tempo = System.currentTimeMillis() - antes;        
        System.out.printf("Pellet executed in %d miliseconds.%n", tempo);	  	
		return infModel;
	}
}
