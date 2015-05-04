package br.com.padtec.nopen.model;

import java.util.ArrayList;
import java.util.List;

public class DefaultRegister {
	
	public static String[] defaultTechs = new String[] {"OTN", "MEF"};
	public static String[][] defaultLayers = new String[][] {{"Subscribers", "MEN"},{"ODUk","OTUk","POUk"}};
	public static String[] defaultServices = new String[] {"SimpleConnection", "1Plus1Connection", "1To1Connection","1ToNConnection"};
	
	//==================================================================================
	
	public static List<String> getDefaultTechs()
	{
		List<String> result = new ArrayList<String>();
		for(String t: defaultTechs){
			result.add(t);
		}
		return result;		
	}
	
	public static List<String> getDefaultLayers()
	{
		List<String> result = new ArrayList<String>();
		for(String[] lArray: defaultLayers){
			for(String s: lArray) result.add(s);
		}
		return result;
	}

	public static List<String> getDefaultServices()
	{
		List<String> result = new ArrayList<String>();
		for(String t: defaultServices){
			result.add(t);
		}
		return result;		
	}
	
	//====================================================================
	
	public static boolean isDefaultTechnology(String techName)
	{
		for(String s: defaultTechs){
			if(techName.equals(s)) return true;
		}	
		return false;
	}
	
	public static void validateTechnology(String techName) throws Exception
	{
		if(isDefaultTechnology(techName)) throw new Exception("Technology \""+techName+"\" already registered.");				
	}
	
	public static boolean isDefaultLayer(String layerName)
	{
		for(String[] sA: defaultLayers){
			for(String s: sA){
				if(layerName.equals(s)) return true;
			}			
		}
		return false;
	}
	
	public static void validateLayer(String layerName) throws Exception
	{	
		if(isDefaultLayer(layerName)) throw new Exception("Layer \""+layerName+"\" already registered.");	
	}
}
