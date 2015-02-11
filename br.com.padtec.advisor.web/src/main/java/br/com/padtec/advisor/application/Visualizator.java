package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.padtec.advisor.application.dto.DtoEquipment;
import br.com.padtec.advisor.application.dto.DtoInterfaceOutput;
import br.com.padtec.advisor.application.queries.AdvisorDtoQueryUtil;
import br.com.padtec.advisor.application.queries.AdvisorQueryUtil;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.advisor.application.util.ApplicationQueryUtil;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class Visualizator {

	protected String valuesGraph = "";
	protected String hashTypes = "";
	protected int size = 0;
	protected int width  = 1000;
	protected int height = 800;
	
	public void setAllSitesConfig()
	{
		List<String> sites = AdvisorQueryUtil.getSitesURI();
		List<String[]> sitesConnections = AdvisorService.getSiteConnectsTuples();
		String rel = RelationEnum.SITE_CONNECTS.toString();
		for (String site : sites) 
		{
			valuesGraph += "graph.addNode(\""+site.substring(site.indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"});";
			hashTypes += "hash[\""+site.substring(site.indexOf("#")+1)+"\"] = \"<b>"+site.substring(site.indexOf("#")+1)+" is an individual of classes: </b><br><ul><li>Site</li></ul>\";";
			size++;
		}
		for(String[] stCon : sitesConnections)
		{
			valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"}),graph.addNode(\""+stCon[1].substring(stCon[1].indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"}), {name:'"+rel+"'});";
			size++;
		}
		
		width  += 400 * (size / 100);
		height += 400 * (size / 100);
	}	

	public void setAllEquipmentsConfig()
	{
		List<DtoEquipment> list = AdvisorDtoQueryUtil.getAllDtoEquipments();		
		for(DtoEquipment equip : list)
		{
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>"+ConceptEnum.EQUIPMENT+"</li></ul>\";";
			for(DtoInterfaceOutput outs : equip.getOutputs())
			{
				valuesGraph += "graph.addEdge(graph.addNode(\""+outs.getName()+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'"+RelationEnum.INV_COMPONENTOF+"'});";
				hashTypes += "hash[\""+outs.getName()+"\"] = \"<b>"+outs.getName()+" is an individual of classes: </b><br><ul><li>"+ConceptEnum.OUTPUT_INTERFACE+"</li></ul>\";";
				size++;
			}
			for(Map.Entry<ArrayList<String>,DtoEquipment> entry : equip.getBinds().entrySet())
			{
				valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getKey().get(0)+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'"+RelationEnum.INTERFACE_BINDS+"'});";
				hashTypes += "hash[\""+entry.getKey().get(1)+"\"] = \"<b>"+entry.getKey().get(1)+" is an individual of classes: </b><br><ul><li>"+ConceptEnum.INPUT_INTERFACE+"</li></ul>\";";
				valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'"+RelationEnum.COMPONENTOF+"'});";
				size++;
			}
			if(equip.getBinds().isEmpty())
			{
				valuesGraph += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
				size++;
			}
		}
		width  += 400 * (size / 100);
		height += 400 * (size / 100);
	}
	
	public void setAllEquipmentsConfing(String selectedSiteName)
	{
		List<DtoEquipment> equips = AdvisorDtoQueryUtil.getDtoEquipmentsFromSite(OKCoUploader.getNamespace()+selectedSiteName);
		for(DtoEquipment equip : equips)
		{
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>"+ConceptEnum.EQUIPMENT+"</li></ul>\";";
			for(DtoInterfaceOutput outs : equip.getOutputs())
			{
				valuesGraph += "graph.addEdge(graph.addNode(\""+outs.getName()+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'"+RelationEnum.INV_COMPONENTOF+"'});";
				hashTypes += "hash[\""+outs.getName()+"\"] = \"<b>"+outs.getName()+" is an individual of classes: </b><br><ul><li>"+ConceptEnum.OUTPUT_INTERFACE+"</li></ul>\";";
				size++;
			}
			for(String in : equip.getInputs())
			{
				valuesGraph += "graph.addEdge(graph.addNode(\""+in+"\", {shape:\"INT_IN_AZUL\"}),graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'"+RelationEnum.INV_COMPONENTOF+"'});";
				hashTypes += "hash[\""+in+"\"] = \"<b>"+in+" is an individual of classes: </b><br><ul><li>"+ConceptEnum.INPUT_INTERFACE+"</li></ul>\";";
				size++;
			}
			for(Map.Entry<ArrayList<String>,DtoEquipment> entry : equip.getBinds().entrySet())
			{
				valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getKey().get(0)+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'"+RelationEnum.INTERFACE_BINDS+"'});";
				hashTypes += "hash[\""+entry.getKey().get(1)+"\"] = \"<b>"+entry.getKey().get(1)+" is an individual of classes: </b><br><ul><li>"+ConceptEnum.INPUT_INTERFACE+"</li></ul>\";";
				valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'"+RelationEnum.COMPONENTOF+"'});";
				size++;
			}
			if(equip.getBinds().isEmpty())
			{
				valuesGraph += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
				size++;
			}
		}
		width  += 400 * (size / 10);
		height += 400 * (size / 10);
	}
	
	public void setAllG800Config()
	{
		List<String> g800s = AdvisorService.getAllIndividualsFromG800();		
		List<String[]> triplas = AdvisorService.getAllG800Triples();
		HashMap<String, List<String>> hashIndv = AdvisorService.getIndividualVSClassesMap(g800s);
		for (String g800 : g800s) 
		{
			valuesGraph += "graph.addNode(\""+g800.substring(g800.indexOf("#")+1)+"\", {shape:\""+HTMLFigureMapper.getG800Image(hashIndv.get(g800))+"_AZUL\"});";
			hashTypes += "hash[\""+g800.substring(g800.indexOf("#")+1)+"\"] = \"<b>"+g800.substring(g800.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
			for(String type : hashIndv.get(g800))
			{
				if(type.contains("#")) hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";
			size++;
		}
		for(String[] stCon : triplas)
		{
			if(!hashIndv.containsKey(stCon[0]) || !hashIndv.containsKey(stCon[2])) continue;
			valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(hashIndv.get(stCon[0]))+"_AZUL\"}),";
			valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(hashIndv.get(stCon[2]))+"_AZUL\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";
			size++;
		}
		width  += 400 * (size / 100);
		height += 400 * (size / 100);
	}
	
	public void setAllG800COnfig(String selectedEquipName)
	{
		List<String> g800s = AdvisorService.getAllIndividualsFromG800(selectedEquipName);		
		List<String[]> triplas = AdvisorService.getAllG800Triples();
		HashMap<String, List<String>> hashIndv = AdvisorService.getIndividualVSClassesMap(g800s);

		for (String g800 : g800s) 
		{
			valuesGraph += "graph.addNode(\""+g800.substring(g800.indexOf("#")+1)+"\", {shape:\""+HTMLFigureMapper.getG800Image(hashIndv.get(g800))+"_AZUL\"});";
			hashTypes += "hash[\""+g800.substring(g800.indexOf("#")+1)+"\"] = \"<b>"+g800.substring(g800.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
			for(String type : hashIndv.get(g800))
			{
				if(type.contains("#")) hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";
			size++;
		}
		for(String[] stCon : triplas)
		{
			if(!hashIndv.containsKey(stCon[0]) || !hashIndv.containsKey(stCon[2]))
			{
				valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[0]))+"_AZUL\"}),";
				valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[2]))+"_AZUL\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";

				hashTypes += "hash[\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\"] = \"<b>"+stCon[0].substring(stCon[0].indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[0]))
				{
					if(type.contains("#")) hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
				}
				hashTypes += "</ul>\";";

				hashTypes += "hash[\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\"] = \"<b>"+stCon[2].substring(stCon[2].indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[2]))
				{
					if(type.contains("#")) hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
				}
				hashTypes += "</ul>\";";
				continue;
			}
			valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(hashIndv.get(stCon[0]))+"_AZUL\"}),";
			valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(hashIndv.get(stCon[2]))+"_AZUL\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";
			size++;
		}
		valuesGraph += "graph.pruneNode(\""+selectedEquipName+"\");";

		width  += 400 * (size / 10);
		height += 400 * (size / 10);
	}
	
	public void setAllElementsConfig()
	{
		List<String> allInstances =QueryUtil.getIndividualsURIFromAllClasses(OKCoUploader.getInferredModel());		
		for (String instance : allInstances) 
		{
			List<DtoInstanceRelation> targetList = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(),instance);
			List<String> classes = QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),instance);
			for (DtoInstanceRelation dtoInstanceRelation : targetList) 
			{
				valuesGraph += "graph.addEdge(graph.addNode(\""+instance.substring(instance.indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(classes)+"_AZUL\"}),";
				valuesGraph += "graph.addNode(\""+dtoInstanceRelation.Target.substring(dtoInstanceRelation.Target.indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),dtoInstanceRelation.Target))+"_AZUL\"}), ";
				valuesGraph	+= "{name:'"+dtoInstanceRelation.Property.substring(dtoInstanceRelation.Property.indexOf("#")+1)+"'});";
				size++;
			}
			if(targetList.isEmpty())
			{
				valuesGraph += "graph.addNode(\""+instance.substring(instance.indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),instance))+"_AZUL\"});";
			}
			hashTypes += "hash[\""+instance.substring(instance.indexOf("#")+1)+"\"] = \"<b>"+instance.substring(instance.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
			for(String type : classes)
			{
				if(type.contains("#")) hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";
		}
		width  += 400 * (size / 100);
		height += 400 * (size / 100);
	}
	
	public String getValuesGraph() 
	{
		return valuesGraph;
	}

	public String getHashTypes() 
	{
		return hashTypes;
	}

	public int getSize() 
	{
		return size;
	}

	public int getWidth() 
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}

	
	
}
