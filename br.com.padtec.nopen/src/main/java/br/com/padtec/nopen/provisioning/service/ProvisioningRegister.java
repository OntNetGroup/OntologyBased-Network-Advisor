package br.com.padtec.nopen.provisioning.service;

import br.com.padtec.nopen.service.util.NOpenFactoryUtil;

public class ProvisioningRegister {

	public static void registerTechnology(String techName)
	{
		NOpenFactoryUtil.createTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
	
	public static void registerLayer(String layerName, String techName) throws Exception
	{
		NOpenFactoryUtil.createLayer(ProvisioningComponents.provisioningRepository,layerName, techName);
	}	
	
	public static void unregisterLayer(String layerName)
	{
		NOpenFactoryUtil.deleteLayer(ProvisioningComponents.provisioningRepository, layerName);
	}
	
	public static void unregisterTechnology(String techName)
	{
		NOpenFactoryUtil.deleteTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
}
