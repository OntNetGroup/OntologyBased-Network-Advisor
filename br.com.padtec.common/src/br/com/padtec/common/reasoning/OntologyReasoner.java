package br.com.padtec.common.reasoning;

import br.com.padtec.common.persistence.BaseModelRepository;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.reasoner.ValidityReport;

public abstract class OntologyReasoner {
	public boolean inferHierarchies = true;
	public boolean inferAssertions = true;
	public boolean inferRules = true;
	long reasoningTimeExec = 0;
	
	public abstract InfModel run(BaseModelRepository baseRepository);
	public abstract InfModel run(OntModel baseModel);
	public long getReasoningTimeExec(){
		return this.reasoningTimeExec;
	}
	
	public boolean validadeModel(OntModel model) {
		ValidityReport validityReport = model.validate();
		return validityReport.isValid();
	}	
}