package br.ufes.inf.nemo.okco.model;

import br.ufes.inf.nemo.okco.model.inference.OntologyReasoner;
import br.ufes.inf.nemo.okco.model.repository.BaseModelRepository;

public interface IFactory {
	
	public BaseModelRepository GetRepository();

	public OntologyReasoner GetReasoner(EnumReasoner reasoner);

}
