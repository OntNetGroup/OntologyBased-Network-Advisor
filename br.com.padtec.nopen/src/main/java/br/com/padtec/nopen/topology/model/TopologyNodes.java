package br.com.padtec.nopen.topology.model;

import javax.xml.bind.annotation.XmlElement;

public class TopologyNodes{
	
	TNode[] node;
	
	public TopologyNodes(){}
	
	public TopologyNodes(TNode[] node){
		this.node = node;
	}
	
	@XmlElement(name="node")
	public TNode[] getNode() {
		return node;
	}
	public void setNode(TNode[] node) {
		this.node = node;
	}
	
}
