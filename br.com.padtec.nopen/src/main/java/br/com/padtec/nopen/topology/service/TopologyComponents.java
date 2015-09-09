package br.com.padtec.nopen.topology.service;

import br.com.padtec.okco.core.application.OKCoReasoner;
import br.com.padtec.okco.core.application.OKCoSelector;
import br.com.padtec.okco.core.application.OKCoUploader;

public class TopologyComponents {

	public static OKCoUploader topologyRepository = new OKCoUploader("Topology");
	public static OKCoSelector topologySelector = new OKCoSelector(topologyRepository);
	public static OKCoReasoner topologyReasoner = new OKCoReasoner(topologyRepository,topologySelector);	
}
