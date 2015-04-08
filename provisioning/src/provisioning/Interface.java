package provisioning;

import java.util.ArrayList;
import java.util.List;

public class Interface implements Comparable<Interface>{
	private String interfaceURI;
	private String equipmentURI;
	
	private List<Interface> candidateInterfacesTo = new ArrayList<Interface>();
	
	public Interface(String interfaceURI, String equipmentURI) {
		this.interfaceURI = interfaceURI;
		this.equipmentURI = equipmentURI;
	}
	
	public Interface(String interfaceURI) {
		this.interfaceURI = interfaceURI;
		this.equipmentURI = null;
	}

	public String getInterfaceURI() {
		return interfaceURI;
	}

	public String getEquipmentURI() {
		return equipmentURI;
	}
	
	
	public List<Interface> getCandidateInterfacesTo() {
		return candidateInterfacesTo;
	}

	public void setCandidateInterfacesTo(List<Interface> candidateInterfacesTo) {
		this.candidateInterfacesTo = candidateInterfacesTo;
	}

	public void addCandidateInterfacesTo(Interface candidateInterfaceTo) {
		this.candidateInterfacesTo.add(candidateInterfaceTo);
	}

	@Override
	public String toString() {
		String interfaceURI;
		String[] interfaceURISplit = this.interfaceURI.split("#");
		if(interfaceURISplit.length > 1){
			interfaceURI = interfaceURISplit[1];
		}else{
			interfaceURI = this.interfaceURI;
		}
		
		String ret = interfaceURI;
				
		if(this.equipmentURI != null){
			String equipmentURI;
			String[] equipmentURISplit = this.equipmentURI.split("#");
			if(equipmentURISplit.length > 1){
				equipmentURI = equipmentURISplit[1];
			}else{
				equipmentURI = this.equipmentURI;
			}
			ret += " [from: " + equipmentURI + "]";
		}			
		
		return ret;
	}

	public int compareTo(Interface obj) {
		int comparison;
		if(this.equipmentURI == null || obj.equipmentURI == null){
			comparison = (this.interfaceURI).compareTo(obj.interfaceURI);
		}else{
			comparison = (this.interfaceURI + this.equipmentURI).compareTo(obj.interfaceURI + obj.equipmentURI);
		}
		return comparison;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Interface){
			int compareTo = this.compareTo((Interface)obj);
			if(compareTo == 0){
				return true;
			}
		}
		return false;
	}
}
