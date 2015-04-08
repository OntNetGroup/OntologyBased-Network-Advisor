package br.com.padtec.nopen.topology.model;

import javax.xml.bind.annotation.XmlElement;

public class TopologyLinks {

	TLink[] link;
	
	public TopologyLinks(){}
	
	public TopologyLinks(TLink[] link){
		this.link = link;
	}
	
	@XmlElement(name="link")
	public TLink[] getLink() {
		return link;
	}
	public void setLink(TLink[] link) {
		this.link = link;
	}
}
