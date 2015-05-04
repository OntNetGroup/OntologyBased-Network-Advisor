package br.com.padtec.nopen.service;

import br.com.padtec.nopen.provisioning.service.ProvisioningRegister;
import br.com.padtec.nopen.studio.service.StudioRegister;

public class NOpenRegister {
	
	public static void registerDefaultTechnologies() throws Exception
	{
		ProvisioningRegister.registerDefaultTechnologies();
		
		StudioRegister.registerDefaultTechnologies();		
	}
	
	public static void registerLayer(String layerName, String techName) throws Exception
	{
		ProvisioningRegister.registerLayer(layerName, techName);

		StudioRegister.registerLayer(layerName, techName);
	}
	
	public static void unregisterLayer(String layerName) throws Exception
	{	
		ProvisioningRegister.unregisterLayer(layerName);
		
		StudioRegister.unregisterLayer(layerName);
	}
	
	public static void registerTechnology(String techName) throws Exception
	{	
		ProvisioningRegister.registerTechnology(techName);
		
		StudioRegister.registerTechnology(techName);
	}
	
	public static void unregisterTechnology(String techName) throws Exception
	{	
		ProvisioningRegister.unregisterTechnology(techName);
		
		StudioRegister.unregisterTechnology(techName);
	}
}
