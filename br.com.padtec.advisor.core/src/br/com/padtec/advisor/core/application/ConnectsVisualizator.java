package br.com.padtec.advisor.core.application;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

public class ConnectsVisualizator extends Visualizator {

	protected String arborStructure = new String();
	protected String hashEquipIntOut = new String();
	protected String hashTypes = new String();
	protected String hashAllowed = new String();
	protected String hashRPEquip = new String();
	
	protected GeneralConnects connects;
		
	public ConnectsVisualizator(OKCoUploader repository, GeneralConnects connects)
	{
		super(repository, connects);
		this.connects = connects;	
	}
	
	public void setConfig()
	{		
		connects.setEquipmentsWithRPs();
		
		ArrayList<String> usedRPs = new ArrayList<String>();
		ArrayList<String[]> possibleConnections;
		for (String connections : connects.getConnectsBetweenEqsAndRps()) 
		{
			String src = connections.split("#")[0];
			String trg = connections.split("#")[1];

			possibleConnections = connects.getPossibleConnectsTuples(src);
			arborStructure += "graph.addEdge(graph.addNode(\""+src+"\", {shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURIFromIndividual(repository.getInferredModel(),repository.getNamespace()+src))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}),";
			if(!possibleConnections.isEmpty() && !hashAllowed.contains(src)) hashAllowed += "hashAllowed.push(\""+src+"\");";

			possibleConnections = connects.getPossibleConnectsTuples(trg);
			arborStructure += "graph.addNode(\""+trg+"\", {shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURIFromIndividual(repository.getInferredModel(),repository.getNamespace()+trg))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}), {name:' '});";
			if(!possibleConnections.isEmpty() && !hashAllowed.contains(trg)) hashAllowed += "hashAllowed.push(\""+trg+"\");";

			size++;

			usedRPs.add(src);
			usedRPs.add(trg);

			hashTypes += "hash[\""+src+"\"] = \"<b>"+src+" is an individual of classes: </b><br><ul>";
			for(String type : QueryUtil.getClassesURIFromIndividual(repository.getInferredModel(),repository.getNamespace()+src))
			{
				if(type.contains("#")) hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";

			hashTypes += "hash[\""+trg+"\"] = \"<b>"+trg+" is an individual of classes: </b><br><ul>";
			for(String type : QueryUtil.getClassesURIFromIndividual(repository.getInferredModel(),repository.getNamespace()+trg))
			{
				if(type.contains("#")) hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";
		}

		HashMap<String, String> rpXequip = new HashMap<String, String>();

		for (String equipWithRP : connects.getEquipmentsWithRps()) 
		{
			String equip = equipWithRP.split("#")[0];
			String rp = equipWithRP.split("#")[1];

			if(!equip.isEmpty())
			{
				Boolean situation;
				if(usedRPs.contains(rp)) situation = true;
				else{
					situation = false;
					hashTypes += "hash[\""+equip+"\"] = \"<b>"+equip+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
					if(!connects.getPossibleConnectsTuples(rp).isEmpty() && !hashAllowed.contains(rp))
					{
						arborStructure += "graph.getNode(\""+equip+"\").data.shape = graph.getNode(\""+equip+"\").data.shape.split(\"_\")[0]+\"_VERDE\";";
						hashAllowed += "hashAllowed.push(\""+equip+"\");";
					}
				}
				hashRPEquip += "hashRPEquip['"+rp+"'] = \""+equip+"\";";
				hashEquipIntOut += "hashEquipIntOut['"+equip+"'] = new Array();";
				hashEquipIntOut += "hashEquipIntOut['"+equip+"']['"+rp+"'] = \""+situation.toString()+"\";";
				rpXequip.put(rp, equip);
			}
		}

		for (String rpXrp : connects.getConnectsBetweenRps()) 
		{
			String srcRP = rpXrp.split("#")[0];
			String trgRP = rpXrp.split("#")[1];
			String srcNode = srcRP;
			String trgNode = trgRP;
			//src is inside a equip
			if(rpXequip.containsKey(srcRP)) srcNode = rpXequip.get(srcRP);
			//trg is inside a equip
			if(rpXequip.containsKey(trgRP)) trgNode = rpXequip.get(trgRP);			
			possibleConnections = connects.getPossibleConnectsTuples(srcNode);
			arborStructure += "graph.addEdge(graph.addNode(\""+srcNode+"\", {shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURIFromIndividual(repository.getInferredModel(),repository.getNamespace()+srcNode))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}),";
			possibleConnections = connects.getPossibleConnectsTuples(trgNode);
			arborStructure += "graph.addNode(\""+trgNode+"\", {shape:\""+HTMLFigureMapper.getG800Image(QueryUtil.getClassesURIFromIndividual(repository.getInferredModel(),repository.getNamespace()+trgNode))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}), {name:'connects'});";
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
