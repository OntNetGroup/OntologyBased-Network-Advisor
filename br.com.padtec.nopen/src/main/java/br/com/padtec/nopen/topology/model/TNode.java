package br.com.padtec.nopen.topology.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(propOrder={"id", "name", "equipment"})
public class TNode {

	String 	id,
			name,
			equipment;
	
	public TNode(){
		
	}
	
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name="node-name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="node-equipment")
	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id + " " + name;
	}
	
}
