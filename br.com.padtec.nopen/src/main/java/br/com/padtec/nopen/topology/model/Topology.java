package br.com.padtec.nopen.topology.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"id", "nodes", "links"})
public class Topology {

	String id;
	
	TopologyNodes nodes;
	TopologyLinks links;
	
	public Topology(){}
	
	public Topology(String id, TopologyNodes nodes, TopologyLinks links){
		this.id = id;
		this.nodes = nodes;
		this.links = links;
	}
	
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlElement(name="nodes")
	public TopologyNodes getNodes() {
		return nodes;
	}

	public void setNodes(TopologyNodes nodes) {
		this.nodes = nodes;
	}

	@XmlElement(name="links")
	public TopologyLinks getLinks() {
		return links;
	}
	public void setLinks(TopologyLinks links) {
		this.links = links;
	}
	

	
}
