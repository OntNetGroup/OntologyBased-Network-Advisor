package provisioner.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Interface implements Comparable<Interface>{
	private String interfaceURI;
	private String equipmentURI;
	private boolean declared;
	private String mappedTfURI;
	private List<String> lastMappedTfURI;
	private boolean isSource;
	private boolean isOutput;
	private boolean alreadyProvisioned = false;//means that an interface has the relation path
	private boolean mapsMatrixSinkInput = false;
	private boolean mapsMatrixSourceOutput = false;
	
	private List<Interface> candidateInterfacesTo = new ArrayList<Interface>();
	private HashMap<Interface, List<Path>> internalPaths = new HashMap<Interface, List<Path>>();
		
	public Interface(String interfaceURI, String equipmentURI, boolean declared, boolean isSource, boolean isOutput) {
		this.interfaceURI = interfaceURI;
		this.equipmentURI = equipmentURI;
		
		this.declared = declared;
		
		this.isSource = isSource;
		this.isOutput = isOutput;
		
	}
	public boolean isAlreadyProvisioned() {
		return alreadyProvisioned;
	}
	public boolean isMapsMatrixSinkInput() {
		return mapsMatrixSinkInput;
	}
	public boolean isMapsMatrixSourceOutput() {
		return mapsMatrixSourceOutput;
	}
	public void setMapsMatrixSinkInput(boolean mapsMatrixSinkInput) {
		this.mapsMatrixSinkInput = mapsMatrixSinkInput;
	}
	public void setMapsMatrixSourceOutput(boolean mapsMatrixSourceOutput) {
		this.mapsMatrixSourceOutput = mapsMatrixSourceOutput;
	}
	public HashMap<Interface, List<Path>> getInternalPaths() {
		return internalPaths;
	}
	public void addInternalPaths(Interface intTo, List<Path> paths) {
		this.internalPaths.put(intTo, paths);
	}
	public boolean isSource() {
		return isSource;
	}
	public boolean isOutput() {
		return isOutput;
	}
	public void setLastMappedTfURI(List<String> lastMappedTfURI) {
		this.lastMappedTfURI = lastMappedTfURI;
	}
	public void setMappedTfURI(String mappedTfURI) {
		this.mappedTfURI = mappedTfURI;
	}
	public List<String> getAllLastMappedTfURI() {
		List<String> allLastMappedTfURI = new ArrayList<String>();
		if(!lastMappedTfURI.contains(mappedTfURI)){
			allLastMappedTfURI.add(mappedTfURI);
		}		
		allLastMappedTfURI.addAll(lastMappedTfURI);
		return allLastMappedTfURI;
	}
	public List<String> getLastMappedTfURI() {
		return lastMappedTfURI;
	}
	public String getMappedTfURI() {
		return mappedTfURI;
	}
	public void clearInterfaceTo(){
		this.candidateInterfacesTo.clear();
	}
	
	public boolean isDeclared() {
		return declared;
	}
	
	public String getInterfaceURI() {
		return interfaceURI;
	}

	public String getEquipmentURI() {
		return equipmentURI;
	}
	
	
	public List<Interface> getCandidateInterfacesTo(List<Interface> bindedInterfaces) {
		if(bindedInterfaces != null && bindedInterfaces.size() > 0){
			candidateInterfacesTo.removeAll(bindedInterfaces);
		}
		return candidateInterfacesTo;
	}

	public void setCandidateInterfacesTo(List<Interface> candidateInterfacesTo, List<Interface> bindedInterfaces) {
		if(bindedInterfaces != null && bindedInterfaces.size() > 0){
			candidateInterfacesTo.removeAll(bindedInterfaces);
		}
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
			
		if(alreadyProvisioned){
			ret += " (already provisioned)";
		}
		
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
	public void setAlreadyProvisioned(boolean alreadyProvisioned) {
		this.alreadyProvisioned = alreadyProvisioned;
	}
}
