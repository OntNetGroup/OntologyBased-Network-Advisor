package provisioning;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;

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
	
	public static void callAlgorithmSemiAuto(String srcEquipToProv, String srcIntToProv, String tgtEquipToProv, String tgtIntToProv) throws Exception{
		DefaultMutableTreeNode sourceRoot;
		sourceRoot = new DefaultMutableTreeNode(new Interface(srcIntToProv, srcEquipToProv));
        //DefaultTreeModel sourceTree = new DefaultTreeModel(sourceRoot);
        DefaultMutableTreeNode targetRoot;
        targetRoot = new DefaultMutableTreeNode(new Interface(tgtIntToProv, tgtEquipToProv));
        //DefaultTreeModel targetTree = new DefaultTreeModel(targetRoot);
        
		List<DefaultMutableTreeNode> sourceLeafs = new ArrayList<DefaultMutableTreeNode>();
		algorithmSemiAuto(sourceRoot, true, "", sourceLeafs);
		List<DefaultMutableTreeNode> targetLeafs = new ArrayList<DefaultMutableTreeNode>();
		algorithmSemiAuto(targetRoot, false, "", targetLeafs);
		
		ArrayList<TreeNode[]> paths = new ArrayList<TreeNode[]>(); 
		for (DefaultMutableTreeNode srcLeaf : sourceLeafs) {
			for (DefaultMutableTreeNode tgtLeaf : targetLeafs) {
				String srcEquip = ((Interface) srcLeaf.getUserObject()).getEquipmentURI();
				String tgtEquip = ((Interface) tgtLeaf.getUserObject()).getEquipmentURI();
				if(srcEquip.equals(tgtEquip)){
					TreeNode[] srcPath = srcLeaf.getPath();
					TreeNode[] tgtPath = tgtLeaf.getPath();
					paths.add(srcPath);
					paths.add(tgtPath);
				}
			}
		}
		
		File arquivo = new File("possible.txt");  // Chamou e nomeou o arquivo txt.  
		if(arquivo.exists()){
			arquivo.delete();
		}
		FileOutputStream fos = new FileOutputStream(arquivo);  // Perceba que estamos instanciando uma classe aqui. A FileOutputStream. Pesquise sobre ela!  
		
		System.out.println("--- PATHS ---");
		for(int i = 0; i < paths.size(); i+=2){
			String out = "";
			int id = (i+2)/2;
			out += id + " - ";
			//System.out.print(id + " - ");
			TreeNode[] srcPath = paths.get(i);
			TreeNode[] tgtPath = paths.get(i+1);
			for (TreeNode srcNode : srcPath) {
				out += srcNode;
				out += " -> ";
				//System.out.print(srcNode);
				//System.out.print(" -> ");
			}
			for(int j = tgtPath.length - 1; j >= 0; j--){
				out += tgtPath[j];
				//System.out.print(tgtPath[j]);
				if(j > 0){
					out += " -> ";
					//System.out.print(" -> ");
				}								
			}
			out += "\n";
			//System.out.println();
			System.out.print(out);
			try{
				fos.write(out.getBytes());   				  
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		fos.close();
		
		//System.out.print("Choose a path: ");
		int path = Main.getOptionFromConsole(paths, "path", 2);
		TreeNode[] srcPath = paths.get(path);
		TreeNode[] tgtPath = paths.get(path+1);
		
		provisionSemiAuto(srcPath, tgtPath);
	}
	
	public static void provisionSemiAuto(TreeNode[] srcPath, TreeNode[] tgtPath){
		for (int i = 1; i < srcPath.length; i+=2) {
			Interface from = (Interface)((DefaultMutableTreeNode)srcPath[i]).getUserObject();
			Interface to = (Interface)((DefaultMutableTreeNode)srcPath[i+1]).getUserObject();

			bindsInterfaces(from.getInterfaceURI(), to.getInterfaceURI());
		}
		
		for (int i = 1; i < tgtPath.length; i+=2) {
			Interface from = (Interface)((DefaultMutableTreeNode)tgtPath[i]).getUserObject();
			Interface to = (Interface)((DefaultMutableTreeNode)tgtPath[i+1]).getUserObject();

			bindsInterfaces(from.getInterfaceURI(), to.getInterfaceURI());
		}
	}
	
	public static void algorithmSemiAuto(DefaultMutableTreeNode lastInputIntNode, boolean isSource, String equipWithPM, List<DefaultMutableTreeNode> leafs) throws Exception{
		String lastInputInt = ((Interface) lastInputIntNode.getUserObject()).getInterfaceURI();
		String pmEquip = getEquipWithPMFromInterface(lastInputInt);
		if(!pmEquip.equals("")){
			leafs.add(lastInputIntNode);
			return;
		}
		
		List<String> outIntList = algorithmPart1(lastInputInt, isSource);
		for (int i = 0; i < outIntList.size(); i+=2) {
			DefaultMutableTreeNode outIntNode = new DefaultMutableTreeNode(new Interface(outIntList.get(i), outIntList.get(i+1)));
			lastInputIntNode.add(outIntNode);
			
			List<String> possibleInIntList = algorithmPart2(isSource, equipWithPM, outIntList.get(i));
			for (int j = 0; j < possibleInIntList.size(); j+=2) {
				DefaultMutableTreeNode possibleInIntNode = new DefaultMutableTreeNode(new Interface(possibleInIntList.get(j), possibleInIntList.get(j+1)));
				outIntNode.add(possibleInIntNode);
				
				algorithmSemiAuto(possibleInIntNode, isSource, equipWithPM, leafs);
			}			
		}
		//return "";
	}
	
	public static String callAlgorithmManual(String lastInt, boolean isSource, String equipWithPM) throws Exception{
		String equipWithPMRet = "";
		do {
			//#20 and #25
			lastInt = algorithmManual(lastInt, isSource, equipWithPM);
			
			//#21 and #26
			//runReasoner(false, true, true);
			
			//#23 and #28
			equipWithPMRet = getEquipWithPMFromInterface(lastInt);
//			equipWithPMRet = Queries.EquipWithPMofInterface(model, lastInt);
//			if(equipWithPMRet.equals("")){
//				intPartOfEquipPM = false;
//			}else{
//				intPartOfEquipPM = true;
//				return equipWithPMRet;
//			}			
//			equipWithPMRet = Queries.equipBindingEquipWithPM(model, lastInt);
//			if(equipWithPMRet.equals("")){
//				equipBindedWithPMEquip = false;
//			}else{
//				equipBindedWithPMEquip = true;
//				return equipWithPMRet;
//			}			
			
		//} while (!intPartOfEquipPM && !equipBindedWithPMEquip);//#23 and #28
		} while (equipWithPMRet.equals(""));//#23 and #28
		
		return equipWithPMRet;
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
		List<String> bindedTFList = Queries.getLastBindedTFFrom(Main.model, mappedTF);
		bindedTFList.add(mappedTF);
		
		List<String> LIST_INT = new ArrayList<String>();
		for (String tfURI : bindedTFList) {
			LIST_INT.addAll(Queries.getMappingInterfaceFrom(Main.model, tfURI, isSource));
		}
		return LIST_INT;
	}
	
	public static List<String> algorithmPart2(boolean isSource, String equipWithPM, String choosenInt2ProvURI) throws Exception{
		//#C
		List<String> listInterfacesTo = Queries.getInterfacesToProvision(Main.model, choosenInt2ProvURI, isSource, equipWithPM);
		
		return listInterfacesTo;
	}
	
	public static void bindsInterfaces(String interfaceURI, String inInterface){
		//#E
		String outPort = Queries.getMappedPort(Main.model, interfaceURI);
		String inPort = Queries.getMappedPort(Main.model, inInterface);
		FactoryUtil.createInstanceRelation(Main.model, outPort, Main.ns+"binds", inPort, false, false, true);
	}
	
	public static String algorithmManual(String interfaceURI, boolean isSource, String equipWithPM) throws Exception{
		List<String> LIST_INT = algorithmPart1(interfaceURI, isSource);
		
		String choosenInt2ProvURI = "";
		//#B
		String typeFrom = "";
		String typeTo = "";
		if(isSource){
			typeFrom = "Output";
			typeTo = "Input";
		}else{
			typeFrom = "Input";
			typeTo = "Input";
		}
		int choosenInt2Prov = Main.chooseOne(LIST_INT, typeFrom + " Interfaces", "Available " + typeFrom + " Interface", 2);
		choosenInt2ProvURI = LIST_INT.get(choosenInt2Prov);			
		
		List<String> listInterfacesTo = algorithmPart2(isSource, equipWithPM, choosenInt2ProvURI);
		
		String inInterface = "";
		//#D
		int inInterfaceIndex = Main.chooseOne(listInterfacesTo, typeTo + " Interfaces", "Available " + typeTo + " Interface", 2);
		inInterface = listInterfacesTo.get(inInterfaceIndex);
		
		bindsInterfaces(interfaceURI, inInterface);
	
//		for(int i = 0; i < LIST_INT.size(); i+=2){
//			FactoryUtil.createInstanceRelation(model, interfaceURI, ns+"poderia_ligar", LIST_INT.get(i));
//			for(int j = 0; j < listInterfacesTo.size(); j+=2){
//				FactoryUtil.createInstanceRelation(model, LIST_INT.get(i), ns+"poderia_ligar", listInterfacesTo.get(j));
//			}
//		}
		
		return inInterface;
	}
	
	public static void verifyIfEquipProvidedBySamePM(String srcEquipToProv, String tgtEquipToProv) throws Exception{
		List<String> lastSrcEquipList = Queries.getLastBindedTFFrom(Main.model, srcEquipToProv);
		List<String> lastTgtEquipList = Queries.getLastBindedTFFrom(Main.model, tgtEquipToProv);
		
		if(lastSrcEquipList.size() == 0 || lastTgtEquipList.size() == 0){
			throw new Exception("No equipment found.");
		}
		
		String lastSrcEquip = lastSrcEquipList.get(0);
		String lastTgtEquip = lastTgtEquipList.get(0);
		
		if(!lastSrcEquip.equals(lastTgtEquip)){
			throw new Exception("Something went wrong. Source and Target equipment were provisioned by different Physical Media.");
		}
	}
}
