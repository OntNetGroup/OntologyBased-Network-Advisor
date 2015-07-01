package br.com.padtec.nopen.model;

import java.util.ArrayList;
import java.util.List;

public class Service {

	private String name = new String();
	private String description = new String();
	private Integer numEquipments = 0;
	private String tech = new String();
	private List<String> ports = new ArrayList<String>();
	private Algorithm algorithm;
	
	public Service(String name, String description){
		this.name = name;
		this.description = description;
	}
		
	public Service(){}
	
	public void addPorts(List<String> ports)
	{
		for(String port: ports){
			if(!ports.contains(port)) ports.add(port);
		}		
	}
	
	public List<String> getPorts() { return ports; }	
	public String getName() { return name; }
	public String getDescription() { return description; }	
	public String getTech() { return tech; }	
	public Algorithm getAlgorithm() { return algorithm; }	
	public Integer getNumEquipments() { return numEquipments; }
	
	public void setPorts(List<String> ports) { this.ports = ports; }
	public void setTech(String tech) { this.tech = tech; }	
	public void setNumEquipments(Integer n) { this.numEquipments = n; }	
	public void setAlgorithm(Algorithm algorithm) { this.algorithm = algorithm; }	
}
