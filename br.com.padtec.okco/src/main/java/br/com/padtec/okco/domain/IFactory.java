package br.com.padtec.okco.domain;

import br.com.padtec.okco.persistence.BaseModelRepository;

public interface IFactory {
	
	public BaseModelRepository GetRepository();

	public OntologyReasoner GetReasoner(EnumReasoner reasoner);

}
