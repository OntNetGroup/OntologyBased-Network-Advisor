package br.com.padtec.nopen.topology.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"id", "source", "target"})
public class TLink {

	String	id,
			source,
			target;
	
	public TLink(){
	}
	
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name="source")
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	@XmlElement(name="destination")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id + "\nS: " + source + "\nT: " + target;
	}
	
}
