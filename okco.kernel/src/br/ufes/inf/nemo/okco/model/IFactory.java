package br.ufes.inf.nemo.okco.model;

import br.ufes.inf.nemo.okco.business.OntologyReasoner;
import br.ufes.inf.nemo.okco.business.Repository;

public interface IFactory {
	
	public Repository GetRepository();

	public OntologyReasoner GetReasoner(EnumReasoner reasoner);

}
