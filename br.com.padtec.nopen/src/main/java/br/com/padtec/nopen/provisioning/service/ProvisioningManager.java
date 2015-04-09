package br.com.padtec.nopen.provisioning.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.padtec.nopen.service.util.NOpenFileUtil;

public class ProvisioningManager {

	
	public String openProvisioning(String filename){
		
		String json = NOpenFileUtil.openTopologyJSONFileAsString(filename + ".json").replaceAll("basic.Circle", "basic.Rect");
		
		JsonParser crunhifyParser = new JsonParser();
		JsonObject jsonObject = crunhifyParser.parse(json).getAsJsonObject();
		JsonArray jsonArray = jsonObject.getAsJsonArray("cells");
		
		for(int i = 0; i < jsonArray.size(); i++){
			
			// Create NODES array
			if(jsonArray.get(i).getAsJsonObject().get("type").getAsString().equals("basic.Rect")){
			
				String equipmentTemplate = jsonArray.get(i).getAsJsonObject().getAsJsonObject("attrs").getAsJsonObject("equipment").get("template").getAsString();
				String nodeId = jsonArray.get(i).getAsJsonObject().get("id").getAsString();
				
				System.out.println("TEMPLATE: " + equipmentTemplate);
				System.out.println("ID: " + nodeId);
				
			}
			// Create LINKS array
			else{


			}
		}
		
		
		return json;
	}
	
}
