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
	
	public static List<Interface> verifyIfEquipmentMapsOutPortsInSource() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, true, true);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Output Interface maps an Output Port from a Source component.\n");
		}
		
		return equipAndInterfaces;
	}
	
	public static List<Interface> verifyIfEquipmentMapsInPortsInSource() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, false, true);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Source component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static List<Interface> verifyIfEquipmentMapsInPortsInSink() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, false, false);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public static List<Interface> verifyIfEquipmentMapsOutPortsInSink() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(Main.model, true, false);
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
	
	public static void callAlgorithmSemiAuto(Interface interfaceFrom, Interface interfaceTo) throws Exception{
		DefaultMutableTreeNode sourceRoot;
		sourceRoot = new DefaultMutableTreeNode(interfaceFrom);
        
        List<Interface> usedInterfaces = new ArrayList<Interface>();
        usedInterfaces.add(interfaceFrom);
		List<DefaultMutableTreeNode> leafInterfaces = new ArrayList<DefaultMutableTreeNode>();
		
		int qtShortPaths = Main.getOptionFromConsole("Choose the number of paths (enter 0 to show all)", 0, Integer.MAX_VALUE);
		if(qtShortPaths == 0){
			qtShortPaths = Integer.MAX_VALUE;
		}
		int maxPathSize = Main.getOptionFromConsole("Choose the maximum number of interfaces in a path (enter 0 for no limit)", 0, Integer.MAX_VALUE);
		if(maxPathSize == 0){
			maxPathSize = Integer.MAX_VALUE;
		}
		
		Date beginDate = new Date();
		algorithmSemiAuto(sourceRoot, true, leafInterfaces, interfaceTo, usedInterfaces, qtShortPaths, maxPathSize);
		PerformanceUtil.printExecutionTime("Provisioning.callAlgorithmSemiAuto", beginDate);
		
		if(leafInterfaces.size() == 0){
			throw new Exception("Something went wrong. No paths were found from " + interfaceFrom.getInterfaceURI() + " to " + interfaceTo.getInterfaceURI() + ".");
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
		int path = Main.getOptionFromConsole(paths, "path", paths.size());
		
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
			bindsInterfaces(from, to, isSource);
		}
	}
	
	public static boolean limitExceeded(List<DefaultMutableTreeNode> leafs, List<Interface> usedInterfaces, int qtShortPaths, int maxPathSize){
		if(usedInterfaces.size() > maxPathSize){
			return true;
		}
		if(leafs.size() >= qtShortPaths){
			DefaultMutableTreeNode lastLeaf = leafs.get(leafs.size()-1);
			TreeNode[] lastLeafPath = lastLeaf.getPath();
			int lastLeafPathSize = lastLeafPath.length;
			int qtUsedInterfaces = usedInterfaces.size();
			if(qtUsedInterfaces%2 == 1){
				qtUsedInterfaces += 1;
			}
			if(lastLeafPathSize <= qtUsedInterfaces){
				return true;
			}
		}
		return false;
	}
	public static void algorithmSemiAuto(DefaultMutableTreeNode lastInputIntNode, boolean isSource, List<DefaultMutableTreeNode> leafs, Interface interfaceTo, List<Interface> usedInterfaces, int qtShortPaths, int maxPathSize) throws Exception{
		String VAR_IN = ((Interface) lastInputIntNode.getUserObject()).getInterfaceURI();
		Interface in = new Interface(VAR_IN);
		
		if(limitExceeded(leafs, usedInterfaces, qtShortPaths, maxPathSize)){
			return;
		}
		
		List<Interface> INT_LIST = algorithmPart1(in, isSource);
		for (int i = 0; i < INT_LIST.size(); i+=1) {
			String VAR_OUT = INT_LIST.get(i).getInterfaceURI();
			Interface out = new Interface(VAR_OUT);
			
			if(!usedInterfaces.contains(VAR_OUT)){
				isSource = isStillInSource(isSource, VAR_OUT);
				List<Interface> newUsedInterfaces1 = new ArrayList<Interface>(); 
				newUsedInterfaces1.addAll(usedInterfaces);
				newUsedInterfaces1.add(out);
				
				//usedInterfaces.add(VAR_OUT);
				
				DefaultMutableTreeNode outIntNode = new DefaultMutableTreeNode(out);
				lastInputIntNode.add(outIntNode);
				
				if(VAR_OUT.equals(interfaceTo.getInterfaceURI())){
					if(limitExceeded(leafs, newUsedInterfaces1, qtShortPaths, maxPathSize)){
						return;
					}
					
					if(leafs.size() >= qtShortPaths){
						TreeNode[] p = leafs.get(leafs.size()-1).getPath();
						System.out.println(p.length);
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
					TreeNode[] p = outIntNode.getPath();
					System.out.println(p.length);
					System.out.println(newUsedInterfaces1.size());
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
					List<Interface> listInterfacesTo = algorithmPart2(isSource, out);
					for (int j = 0; j < listInterfacesTo.size(); j+=1) {
						VAR_IN = listInterfacesTo.get(j).getInterfaceURI();
						in = new Interface(VAR_IN);
						isSource = isStillInSource(isSource, VAR_IN);
						
						if(!newUsedInterfaces1.contains(VAR_IN)){
							List<Interface> newUsedInterfaces2 = new ArrayList<Interface>(); 
							newUsedInterfaces2.addAll(newUsedInterfaces1);
							newUsedInterfaces2.add(in);
							
							DefaultMutableTreeNode possibleInIntNode = new DefaultMutableTreeNode(in);
							outIntNode.add(possibleInIntNode);
							
							algorithmSemiAuto(possibleInIntNode, isSource, leafs, interfaceTo, newUsedInterfaces2, qtShortPaths, maxPathSize);
						}						
					}	
				}
			}								
		}		
	}
	
	public static String callAlgorithmManual(Interface interfaceFrom, Interface interfaceTo) throws Exception{
		boolean isSource = true;
		String VAR_OUT = "";
		String VAR_IN = interfaceFrom.getInterfaceURI();
		Main.bindedInterfaces.add(interfaceFrom);
		do {
			//#19
			isSource = isStillInSource(isSource, VAR_IN);
			Interface in = new Interface(VAR_IN);
			List<Interface> INT_LIST = algorithmPart1(in, isSource);
			
			int chosenId = Main.chooseOne(INT_LIST, "Output Interfaces", "Available Output Interface");
			VAR_OUT = INT_LIST.get(chosenId).getInterfaceURI();
			
			//#20
			if(!VAR_OUT.equals(interfaceTo.getInterfaceURI())){
				//#21
				isSource = isStillInSource(isSource, VAR_OUT);
				Interface out = new Interface(VAR_OUT);
				List<Interface> listInterfacesTo = algorithmPart2(isSource, out);
				
				//#D
				int interfaceToId = Main.chooseOne(listInterfacesTo, "Input Interfaces", "Available Input Interface");
				VAR_IN = listInterfacesTo.get(interfaceToId).getInterfaceURI();
				
				//#22
				in = new Interface(VAR_IN);
				bindsInterfaces(out, in, isSource);
			}
		} while (!VAR_OUT.equals(interfaceTo.getInterfaceURI()));//#20
		
		return VAR_OUT;
	}
	
	public static boolean isStillInSource(boolean isSourceOld, String VAR){
		boolean isSource = isSourceOld;
		if((isSourceOld && !QueryUtil.isInterfaceSource(Main.model, VAR)) || (!isSourceOld && QueryUtil.isInterfaceSource(Main.model, VAR))){
			isSource  = !isSourceOld;				
		}
		return isSource;
	}
	
//	public static String getEquipWithPMFromInterface(String lastInt){
//		//#23 and #28
//		String equipWithPMRet = Queries.EquipWithPMofInterface(Main.model, lastInt);
//		if(!equipWithPMRet.equals("")){
//			return equipWithPMRet;
//		}			
//		equipWithPMRet = Queries.equipBindingEquipWithPM(Main.model, lastInt);
//		if(!equipWithPMRet.equals("")){
//			return equipWithPMRet;
//		}	
//		return "";
//	}
	
	public static List<Interface> algorithmPart1(Interface inputInterface, boolean isSource) throws Exception{
		//#A
		String mappedTF = Queries.getMappedTFFrom(Main.model, inputInterface.getInterfaceURI());
		List<String> bindedTFList = Queries.getLastBindedTFFrom(Main.model, mappedTF, isSource);
		//System.out.println();
		if(!bindedTFList.contains(mappedTF)){
			bindedTFList.add(mappedTF);
		}		
		
		List<Interface> LIST_INT = new ArrayList<Interface>();
		for (String tfURI : bindedTFList) {
			LIST_INT.addAll(Queries.getMappingInterfaceFrom(Main.model, tfURI));
		}
		return LIST_INT;
	}
	
	public static List<Interface> algorithmPart2(boolean isSource, Interface outputInterface) throws Exception{
		//#C
		List<Interface> listInterfacesTo = Queries.getInterfacesToProvision(Main.model, outputInterface.getInterfaceURI(), isSource);
		
		return listInterfacesTo;
	}
	
	public static void bindsInterfaces(Interface interfaceFrom, Interface interfaceTo, boolean isSource){
		//#E
		if(!isSource){
			Interface aux = interfaceFrom;
			interfaceFrom = interfaceTo;
			interfaceTo = aux;
		}
		String outPort = Queries.getMappedPort(Main.model, interfaceFrom.getInterfaceURI());
		String inPort = Queries.getMappedPort(Main.model, interfaceTo.getInterfaceURI());
		FactoryUtil.createInstanceRelation(Main.model, outPort, Main.ns+"binds", inPort, false, false, true);
		Main.bindedInterfaces.add(interfaceFrom);
		Main.bindedInterfaces.add(interfaceTo);
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
