package provisioning;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import br.com.padtec.advisor.core.util.PerformanceUtil;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;

public class Provisioning {
	public static List<DtoInstance> verifiyMinimumEquipment() throws Exception{
		List<DtoInstance> equipList = DtoQueryUtil.getIndividualsFromClass(Main.model, "Equipment");
		if(equipList.size() < 2){
			throw new Exception("Is required a minimun of 2 Equipment.\n");
		}
		
		return equipList;			
	}
	
	public static List<String> verifyIfEquipmentMapsOutPortsInSource() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, true, true);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Output Interface maps an Output Port from a Source component.\n");
		}
		
		return equipAndInterfaces;
	}
	
	public static List<String> verifyIfEquipmentMapsInPortsInSource() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, false, true);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Source component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static List<String> verifyIfEquipmentMapsInPortsInSink() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, false, false);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static List<String> verifyIfEquipmentMapsOutPortsInSink() throws Exception{
		List<String> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, true, false);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Output Interface maps an Output Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static void verifiyMinimumEquipWithPM() throws Exception{
		List<String> equipWithPm = Queries.getEquipmentWithPhysicalMedia(Main.model);
		if(equipWithPm.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment with Physical Media.\n");
		}		
	}
	
	public static void callAlgorithmSemiAuto(String equipFromURI, String interfaceFromURI, String equipToURI, String interfaceToURI) throws Exception{
		DefaultMutableTreeNode sourceRoot;
		sourceRoot = new DefaultMutableTreeNode(new Interface(interfaceFromURI, equipFromURI));
        
        List<String> usedInterfaces = new ArrayList<String>();
        usedInterfaces.add(interfaceFromURI);
		List<DefaultMutableTreeNode> leafInterfaces = new ArrayList<DefaultMutableTreeNode>();
		
		int qtShortPaths = Main.getOptionFromConsole("number of shortest paths (from 1 to " + Integer.MAX_VALUE + ")", 1, Integer.MAX_VALUE);
		if(qtShortPaths == 0){
			qtShortPaths = Integer.MAX_VALUE;
		}
		
		Date beginDate = new Date();
		algorithmSemiAuto(interfaceFromURI, sourceRoot, true, "", leafInterfaces, interfaceToURI, usedInterfaces, qtShortPaths);
		PerformanceUtil.printExecutionTime("Provisioning.callAlgorithmSemiAuto", beginDate);
		
		if(leafInterfaces.size() == 0){
			throw new Exception("Something went wrong. No paths were found from " + interfaceFromURI + " to " + interfaceToURI + ".");
		}
		
		ArrayList<TreeNode[]> paths = new ArrayList<TreeNode[]>();
		for (DefaultMutableTreeNode leafInterface : leafInterfaces) {
			paths.add(leafInterface.getPath());			
		}
		
		File arquivo = new File("possible.txt");   
		if(arquivo.exists()){
			arquivo.delete();
		}
		FileOutputStream fos = new FileOutputStream(arquivo);    
		
		myBubbleSort(paths);
		
		System.out.println("--- PATHS ---");
		ArrayList<String> outs = new ArrayList<String>();
		for(int i = 0; i < paths.size(); i+=1){
			TreeNode[] path = paths.get(i);
			String out = "";
			int id = (i+1)/1;
			out += id + " - ";
			
			for (TreeNode srcNode : path) {
				out += srcNode;
				out += " -> ";
			}

			out += "\n";
			out += "\tsize: " + (path.length/*+tgtPath.length*/) + "\n";
			
			if(!outs.contains(out)){
				outs.add(out);
			}
			System.out.print(out);
			try{
				fos.write(out.getBytes());   				  
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		fos.close();
		int path = Main.getOptionFromConsole(paths, "path", 1, paths.size());
		
		provisionSemiAuto(paths.get(path));
	}
	
	public static void myBubbleSort(ArrayList<TreeNode[]> list) {
		boolean troca = true;
		TreeNode[] aux;
		int inc = 1;
        while (troca) {
            troca = false;
            for (int i = 0; i < list.size() - inc; i += inc) {
//            	String a1 = list.get(i);
//            	String a2 = list.get(i + increment);
            	int size1 = list.get(i).length;// + list.get(i+1).length;
            	int size2 = list.get(i+inc).length;// + list.get(i+3).length;
            	//int comparison = list.get(i).compareTo(list.get(i + 2));
            	if(size1 > size2){
            		for(int j = i; j < i + inc; j++){
            			aux = list.get(j);
            			list.set(j, list.get(j + inc));
                        //list.get(j) = list.get(j + increment);
            			list.set(j + inc, aux);
                        //list[j + increment] = aux;                        
            		}
            		troca = true;
            	}
            }
        }
    }
	
	public static void provisionSemiAuto(TreeNode[] path){
		boolean isSource = true;
		for (int i = 1; i < path.length-1; i+=2) {
			Interface from = (Interface)((DefaultMutableTreeNode)path[i]).getUserObject();
			Interface to = (Interface)((DefaultMutableTreeNode)path[i+1]).getUserObject();
			isSource = isStillInSource(isSource, from.getInterfaceURI());
			System.out.println(from.getInterfaceURI());
			System.out.println(to.getInterfaceURI());
			//System.out.println(isSource);
			bindsInterfaces(from.getInterfaceURI(), to.getInterfaceURI(), isSource);
		}
	}
	
	public static void algorithmSemiAuto(final String origInterfaceFromURI, DefaultMutableTreeNode lastInputIntNode, boolean isSource, String equipWithPM, List<DefaultMutableTreeNode> leafs, String interfaceToURI, List<String> usedInterfaces, int qtShortPaths) throws Exception{
		String VAR_IN = ((Interface) lastInputIntNode.getUserObject()).getInterfaceURI();
		
		if(leafs.size() >= qtShortPaths){
			DefaultMutableTreeNode lastLeaf = leafs.get(leafs.size()-1);
			TreeNode[] lastLeafPath = lastLeaf.getPath();
			int lastLeafPathSize = lastLeafPath.length;
			if(lastLeafPathSize <= usedInterfaces.size()){
				return;
			}
		}
		
		List<String> INT_LIST = algorithmPart1(VAR_IN, isSource);
		for (int i = 0; i < INT_LIST.size(); i+=2) {
			String VAR_OUT = INT_LIST.get(i);
			
			if(!usedInterfaces.contains(VAR_OUT)){
				isSource = isStillInSource(isSource, VAR_OUT);
				List<String> newUsedInterfaces1 = new ArrayList<String>(); 
				newUsedInterfaces1.addAll(usedInterfaces);
				newUsedInterfaces1.add(VAR_OUT);
				
				//usedInterfaces.add(VAR_OUT);
				
				DefaultMutableTreeNode outIntNode = new DefaultMutableTreeNode(new Interface(VAR_OUT, INT_LIST.get(i+1)));
				lastInputIntNode.add(outIntNode);
				
				if(VAR_OUT.equals(interfaceToURI)){
					if(leafs.size() >= qtShortPaths){
						leafs.remove(leafs.size()-1);
//						DefaultMutableTreeNode lastLeaf = leafs.get(leafs.size()-1);
//						int lfPtSize = lastLeaf.getPath().length;
//						int outPtSize = outIntNode.getPath().length;
//						leafs.remove(lastLeaf);
//						TreeNode[] lastLeafPath = lastLeaf.getPath();
//						int lastLeafPathSize = lastLeafPath.length;
//						System.out.println();
//						if(lastLeafsPathSize < usedInterfaces.size()){
//							return;
//						}
					}
					
					leafs.add(outIntNode);
					leafs.sort(new Comparator<DefaultMutableTreeNode>() {

						public int compare(DefaultMutableTreeNode arg0, DefaultMutableTreeNode arg1) {
							int pathSize0 = arg0.getPath().length;
							int pathSize1 = arg1.getPath().length;
							if (pathSize0 < pathSize1) {
					            return -1;
					        }
					        if (pathSize0 > pathSize1) {
					            return 1;
					        }
							return 0;
						}
					});
					//System.out.println(outIntNode.getPath().length);
					return;
				}else{
					List<String> listInterfacesTo = algorithmPart2(origInterfaceFromURI, isSource, equipWithPM, VAR_OUT);
					for (int j = 0; j < listInterfacesTo.size(); j+=2) {
						VAR_IN = listInterfacesTo.get(j);
						
						isSource = isStillInSource(isSource, VAR_IN);
						
						if(!newUsedInterfaces1.contains(VAR_IN)){
							List<String> newUsedInterfaces2 = new ArrayList<String>(); 
							newUsedInterfaces2.addAll(newUsedInterfaces1);
							newUsedInterfaces2.add(VAR_IN);
							
							DefaultMutableTreeNode possibleInIntNode = new DefaultMutableTreeNode(new Interface(VAR_IN, listInterfacesTo.get(j+1)));
							outIntNode.add(possibleInIntNode);
							
							algorithmSemiAuto(origInterfaceFromURI, possibleInIntNode, isSource, equipWithPM, leafs, interfaceToURI, newUsedInterfaces2, qtShortPaths);
						}						
					}	
				}
			}								
		}		
	}
	
	public static String callAlgorithmManual(String interfaceFromURI, String interfaceToURI, String equipWithPM) throws Exception{
		boolean isSource = true;
		String VAR_OUT = "";
		String VAR_IN = interfaceFromURI;		
		do {
			//#19
			isSource = isStillInSource(isSource, VAR_IN);
			List<String> INT_LIST = algorithmPart1(VAR_IN, isSource);
			
			int chosenId = Main.chooseOne(INT_LIST, "Output Interfaces", "Available Output Interface", 2);
			VAR_OUT = INT_LIST.get(chosenId);
			
			//#20
			if(!VAR_OUT.equals(interfaceToURI)){
				//#21
				isSource = isStillInSource(isSource, VAR_OUT);
				List<String> listInterfacesTo = algorithmPart2(interfaceFromURI, isSource, equipWithPM, VAR_OUT);
				
				//#D
				int interfaceToId = Main.chooseOne(listInterfacesTo, "Input Interfaces", "Available Input Interface", 2);
				VAR_IN = listInterfacesTo.get(interfaceToId);
				
				//#22
				bindsInterfaces(VAR_OUT, VAR_IN, isSource);
			}
		} while (!VAR_OUT.equals(interfaceToURI));//#20
		
		return VAR_OUT;
	}
	
	public static boolean isStillInSource(boolean isSourceOld, String VAR){
		boolean isSource = isSourceOld;
		if((isSourceOld && !QueryUtil.isInterfaceSource(Main.model, VAR)) || (!isSourceOld && QueryUtil.isInterfaceSource(Main.model, VAR))){
			isSource  = !isSourceOld;				
		}
		return isSource;
	}
	
	public static String getEquipWithPMFromInterface(String lastInt){
		//#23 and #28
		String equipWithPMRet = Queries.EquipWithPMofInterface(Main.model, lastInt);
		if(!equipWithPMRet.equals("")){
			return equipWithPMRet;
		}			
		equipWithPMRet = Queries.equipBindingEquipWithPM(Main.model, lastInt);
		if(!equipWithPMRet.equals("")){
			return equipWithPMRet;
		}	
		return "";
	}
	
	public static List<String> algorithmPart1(String interfaceURI, boolean isSource) throws Exception{
		//#A
		String mappedTF = Queries.getMappedTFFrom(Main.model, interfaceURI);
		List<String> bindedTFList = Queries.getLastBindedTFFrom(Main.model, mappedTF, isSource);
		//System.out.println();
		if(!bindedTFList.contains(mappedTF)){
			bindedTFList.add(mappedTF);
		}		
		
		List<String> LIST_INT = new ArrayList<String>();
		for (String tfURI : bindedTFList) {
			LIST_INT.addAll(Queries.getMappingInterfaceFrom(Main.model, tfURI));
		}
		return LIST_INT;
	}
	
	public static List<String> algorithmPart2(String originalInterfaceFromURI, boolean isSource, String equipWithPM, String choosenInt2ProvURI) throws Exception{
		//#C
		List<String> listInterfacesTo = Queries.getInterfacesToProvision(originalInterfaceFromURI, Main.model, choosenInt2ProvURI, isSource, equipWithPM);
		
		return listInterfacesTo;
	}
	
	public static void bindsInterfaces(String interfaceFromURI, String interfaceToURI, boolean isSource){
		//#E
		if(!isSource){
			String aux = interfaceFromURI;
			interfaceFromURI = interfaceToURI;
			interfaceToURI = aux;
		}
		String outPort = Queries.getMappedPort(Main.model, interfaceFromURI);
		String inPort = Queries.getMappedPort(Main.model, interfaceToURI);
		FactoryUtil.createInstanceRelation(Main.model, outPort, Main.ns+"binds", inPort, false, false, true);
		Main.bindedInterfaces.add(interfaceFromURI);
		Main.bindedInterfaces.add(interfaceToURI);
	}
	
//	public static String algorithmManual(String interfaceFromURI, boolean isSource, String equipWithPM) throws Exception{
//		List<String> INT_LIST = algorithmPart1(interfaceFromURI, isSource);
//		
//		String choosenURI = "";
//		//#B
//		String typeFrom = "";
//		String typeTo = "";
//		if(isSource){
//			typeFrom = "Output";
//			typeTo = "Input";
//		}else{
//			typeFrom = "Input";
//			typeTo = "Input";
//		}
//		int chosenId = Main.chooseOne(INT_LIST, typeFrom + " Interfaces", "Available " + typeFrom + " Interface", 2);
//		choosenURI = INT_LIST.get(chosenId);			
//		
//		List<String> listInterfacesTo = algorithmPart2(isSource, equipWithPM, choosenURI);
//		
//		String interfaceToURI = "";
//		//#D
//		int interfaceToId = Main.chooseOne(listInterfacesTo, typeTo + " Interfaces", "Available " + typeTo + " Interface", 2);
//		interfaceToURI = listInterfacesTo.get(interfaceToId);
//		
//		bindsInterfaces(interfaceFromURI, interfaceToURI, isSource);
//		
//		return interfaceToURI;
//	}
	
//	public static void verifyIfEquipProvidedBySamePM(String srcEquipToProv, String tgtEquipToProv, boolean isSource) throws Exception{
//		List<String> lastSrcEquipList = Queries.getLastBindedTFFrom(Main.model, srcEquipToProv, isSource);
//		List<String> lastTgtEquipList = Queries.getLastBindedTFFrom(Main.model, tgtEquipToProv, isSource);
//		
//		if(lastSrcEquipList.size() == 0 || lastTgtEquipList.size() == 0){
//			throw new Exception("No equipment found.");
//		}
//		
//		String lastSrcEquip = lastSrcEquipList.get(0);
//		String lastTgtEquip = lastTgtEquipList.get(0);
//		
//		if(!lastSrcEquip.equals(lastTgtEquip)){
//			throw new Exception("Something went wrong. Source and Target equipment were provisioned by different Physical Media.");
//		}
//	}
}
