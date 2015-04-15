package br.com.padtec.nopen.provisioning.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.util.NOpenFactoryUtil;

public class ProvisioningRegister {

	public static void registerDefaultTechnologies() throws Exception
	{
		Date beginDate = new Date();		
				
		NOpenFactoryUtil.createMEFTech(ProvisioningComponents.provisioningRepository);
		NOpenFactoryUtil.createOTNTech(ProvisioningComponents.provisioningRepository);		
		NOpenFactoryUtil.createServices(ProvisioningComponents.provisioningRepository);
		
		PerformanceUtil.printExecutionTime("Provisioning: Technologies, Layers and Services registered.", beginDate);
	}
	
	public static void registerTechnology(String techName) throws Exception
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
