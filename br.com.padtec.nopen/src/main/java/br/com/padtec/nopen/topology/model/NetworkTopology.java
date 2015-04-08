package br.com.padtec.nopen.topology.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="network-topology")
public class NetworkTopology {

	Topology topology;

	public NetworkTopology(){}
	
	public NetworkTopology(Topology topology){
		this.topology = topology;
	}
	
	@XmlElement(name="topology")
	public Topology getTopology() {
		return topology;
	}

	public void setTopology(Topology topology) {
		this.topology = topology;
	}
	
	
	
}
