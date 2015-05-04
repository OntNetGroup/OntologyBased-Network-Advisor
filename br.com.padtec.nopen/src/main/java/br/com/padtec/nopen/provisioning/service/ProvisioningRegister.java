package br.com.padtec.nopen.provisioning.service;

import java.util.Date;

import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.nopen.model.DefaultRegister;
import br.com.padtec.nopen.model.InstanceFabricator;

public class ProvisioningRegister extends DefaultRegister {

	public static void registerDefaultTechnologies() throws Exception
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
			InstanceFabricator.createService(ProvisioningComponents.provisioningRepository, service, null, null);	
		}
		
		PerformanceUtil.printExecutionTime("Provisioning: Technologies, Layers and Services registered.", beginDate);
	}
	
	public static void registerTechnology(String techName) throws Exception
	{
		/** validation */
		validateTechnology(techName);
		
		InstanceFabricator.createTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
	
	public static void registerLayer(String layerName, String techName) throws Exception
	{
		validateLayer(layerName);
		
		InstanceFabricator.createLayer(ProvisioningComponents.provisioningRepository,layerName, techName);
	}	
	
	public static void unregisterLayer(String layerName) throws Exception
	{
		InstanceFabricator.deleteLayer(ProvisioningComponents.provisioningRepository, layerName);
	}
	
	public static void unregisterTechnology(String techName) throws Exception
	{
		InstanceFabricator.deleteTechnology(ProvisioningComponents.provisioningRepository, techName);
	}
}
