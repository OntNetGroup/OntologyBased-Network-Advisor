package br.ufes.inf.nemo.okco.model;

import br.ufes.inf.nemo.okco.model.inference.OntologyReasoner;
import br.ufes.inf.nemo.okco.model.repository.Repository;

public interface IFactory {
	
	public Repository GetRepository();

	public OntologyReasoner GetReasoner(EnumReasoner reasoner);

}
