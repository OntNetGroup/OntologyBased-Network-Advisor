package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;

public class Equipment {
	
	
	
	private String name;
	private ArrayList<String> outputs;
	private ArrayList<String> inputs;
	private HashMap< ArrayList<String>,Equipment> binds= new HashMap<ArrayList<String>, Equipment>();
	//private HashMap<String,Equipment> binds= new HashMap<String,Equipment>();

	public Equipment(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
	}
	
	public boolean containsKey(Object key) {
		return binds.containsKey(key);
	}
	public boolean containsValue(Object value) {
		return binds.containsValue(value);
	}
	public Equipment get(Object key) {
		return binds.get(key);
	}
	public Equipment put(ArrayList<String> key, Equipment value) {
		return binds.put(key, value);
	}
	public Equipment remove(Object key) {
		return binds.remove(key);
	}
	
	public ArrayList<String> getOutputs() {
		return outputs;
	}
	public ArrayList<String> getInputs() {
		return inputs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void addOut(String element) {
		outputs.add(element);
	}
	public void addInp(String element) {
		inputs.add( element);
	}
	
	
	
}
