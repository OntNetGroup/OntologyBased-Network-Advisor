package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.padtec.advisor.application.dto.DtoEquipment;
import br.com.padtec.advisor.application.dto.DtoInterfaceOutput;
import br.com.padtec.advisor.application.queries.AdvisorDtoQueryUtil;
import br.com.padtec.advisor.controller.BindsController;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class BindsVisualizator extends Visualizator {
	
	protected String arborStructure = new String();
	protected String hashEquipIntOut = new String();
	protected String hashTypes = new String();
	protected String hashAllowed = new String();
	protected String hashRPEquip = new String();
	
	public void setConfig()
	{
		List<DtoEquipment> list = AdvisorDtoQueryUtil.getAllDtoEquipments();
		
		for(DtoEquipment equip : list)
		{
			if(equip.getName().equalsIgnoreCase("skeq2")){
				System.out.println();
			}
			hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"'] = new Array();";
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
			for(DtoInterfaceOutput outs : equip.getOutputs())
			{
				hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"']['"+outs.getName()+"'] = \""+outs.isConnected()+"\";";
				if(hashAllowed.contains(equip.getName())) continue;
				ArrayList<String> possibleList = BindsController.getCandidateInterfacesForConnection(outs.getName());
				for(String possibleConnection : possibleList)
				{
					if(possibleConnection.contains("true"))
					{
						hashAllowed += "hashAllowed[\""+equip.getName()+"\"] = \"VERDE\";";
						break;
					}
				}
			}
			for(Map.Entry<ArrayList<String>,DtoEquipment> entry : equip.getBinds().entrySet())
			{
				arborStructure += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_ROXO\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+entry.getValue().getName()))+"_ROXO\"}), {name:'binds:";
				arborStructure += entry.getKey().get(0)+"-"+entry.getKey().get(1);
				arborStructure += "'});";
				size++;
			}
			
			if(equip.getBinds().isEmpty()){
				arborStructure += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_ROXO\"});";
				size++;
			}
		}

		ArrayList<String[]> pms = AdvisorService.getAllPhysicalMediaTriples();
		for(String[] pm : pms)
		{
			hashEquipIntOut += "hashEquipIntOut['"+pm[3].substring(pm[3].indexOf("#")+1)+"'] = new Array();";
			hashTypes += "hash[\""+pm[3].substring(pm[3].indexOf("#")+1)+"\"] = \"<b>"+pm[3].substring(pm[3].indexOf("#")+1)+" is an individual of classes: </b><br><ul><li>Physical Media</li></ul>\";";

			if(pm[0] != null)
			{
				arborStructure += "graph.addEdge(graph.addNode(\""+pm[3].substring(pm[3].indexOf("#")+1)+"\", {shape:\"PM_ROXO\"}),graph.addNode(\""+pm[2].substring(pm[2].indexOf("#")+1)+"\", {shape:\"Equip_ROXO\"}), {name:'binds:"+pm[0].substring(pm[0].indexOf("#")+1)+"-"+pm[1].substring(pm[1].indexOf("#")+1)+"'});";
				size++;
			}
			if(pm[5] != null)
			{
				arborStructure += "graph.addEdge(graph.addNode(\""+pm[3].substring(pm[3].indexOf("#")+1)+"\", {shape:\"PM_ROXO\"}),graph.addNode(\""+pm[6].substring(pm[6].indexOf("#")+1)+"\", {shape:\"Equip_ROXO\"}), {name:'binds:"+pm[4].substring(pm[4].indexOf("#")+1)+"-"+pm[5].substring(pm[5].indexOf("#")+1)+"'});";
				hashEquipIntOut += "hashEquipIntOut['"+pm[3].substring(pm[3].indexOf("#")+1)+"']['"+pm[5]+"'] = \"true\";";
				size++;
			}
			if(pm[0] == null && pm[4] == null)
			{
				arborStructure += "graph.addNode(\""+pm[3].substring(pm[3].indexOf("#")+1)+"\", {shape:\"PM_ROXO\"});";
			}
		}		
		width  += 400 * (size / 10);
		height += 400 * (size / 10);
	}

	public String getArborStructure() 
	{
		return arborStructure;
	}

	public String getHashEquipIntOut() 
	{
		return hashEquipIntOut;
	}

	public String getHashTypes() 
	{		
		return hashTypes;
	}

	public String getHashAllowed() 
	{
		return hashAllowed;
	}

	public String getHashRPEquip()
	{		
		return hashRPEquip;
	}
}
