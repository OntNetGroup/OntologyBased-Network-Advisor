package br.com.padtec.advisor.core.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class DtoEquipment {
	
	private String name = new String();
	
	private HashMap< ArrayList<String>, DtoEquipment> binds= new HashMap<ArrayList<String>, DtoEquipment>();
	private HashMap< ArrayList<String>, DtoEquipment> connects= new HashMap<ArrayList<String>, DtoEquipment>();
	
	public ArrayList<String> outputs= new ArrayList<String>();
	public ArrayList<String> inputs= new ArrayList<String>();
	
	private ArrayList<DtoInterfaceOutput> outputsInterfaces= new ArrayList<DtoInterfaceOutput>();
	
	public DtoEquipment(String name) 
	{
		this.name=name;
	}
	
	public boolean containsKey(Object key) 
	{
		return binds.containsKey(key);
	}
	
	public boolean containsValue(Object value) 
	{
		return binds.containsValue(value);
	}
	
	public DtoEquipment get(Object key) 
	{
		return binds.get(key);
	}
	
	public DtoEquipment putBinds(ArrayList<String> key, DtoEquipment value) 
	{
		return binds.put(key, value);
	}
	
	public DtoEquipment putConnects(ArrayList<String> key, DtoEquipment value) 
	{
		return binds.put(key, value);
	}
	
	public DtoEquipment removeBinds(Object key) 
	{
		return binds.remove(key);
	}
	
	public HashMap< ArrayList<String>,DtoEquipment> getBinds()
	{
		return binds;
	}
	
	public DtoEquipment removeConnects(Object key) 
	{
		return connects.remove(key);
	}
	
	public HashMap< ArrayList<String>,DtoEquipment> getConnects()
	{
		return connects;
	}
	
	public ArrayList<DtoInterfaceOutput> getOutputs() 
	{
		return outputsInterfaces;
	}
	
	public ArrayList<String> getInputs() 
	{
		return inputs;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}

	public void addOut(DtoInterfaceOutput element) 
	{
		outputsInterfaces.add(element);
	}
	
	public void addInp(String element) 
	{
		inputs.add( element);
	}
	
	public String toString()
	{
		return name+"\n";
	}
}
