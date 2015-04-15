package br.com.padtec.nopen.provisioning.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.service.util.NOpenFactoryUtil;

public class ProvisioningRegister {

	public static void registerDefaultTechnologies(String[] defaultTechs, String[][] defaultLayers, String[] defaultServices) throws Exception
	{
		Date beginDate = new Date();		
				
		int i=0;
		for(String[] layers: defaultLayers)
		{
			registerTechnology(defaultTechs[i]);
			for(String l: layers)
			{
				registerLayer(l, defaultTechs[i]);
			}
			i++;
		}
		
		for(String service: defaultServices)
		{
			NOpenFactoryUtil.createService(ProvisioningComponents.provisioningRepository, service, null, null);	
		}
		
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
	
	public static void unregisterLayer(String layerName) throws Exception
	{
		NOpenFactoryUtil.deleteLayer(ProvisioningComponents.provisioningRepository, layerName);
	}
	
	public static void unregisterTechnology(String techName) throws Exception
	{
		NOpenFactoryUtil.deleteTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
}
