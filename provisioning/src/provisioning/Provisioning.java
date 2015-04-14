package provisioning;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.OntModel;

public class Provisioning {
	HashMap<String, Interface> interfaces = new HashMap<String, Interface>();
	List<Interface> bindedInterfaces;
	HashMap<String, Interface> bindedInterfacesHash = new HashMap<String, Interface>();
	List<String> declaredEquip;
	List<String> possibleEquip;
	OntModel model;
	String ns;
	List<Interface> INT_SO_LIST;
	List<Interface> INT_SK_LIST;
	OKCoUploader okcoUploader = new OKCoUploader();
	
	public Provisioning(String tBoxFile, String declaredInstancesFile, String possibleEquipFile) throws Exception {
		//#1
		model = OWLUtil.createTBox(this.okcoUploader, tBoxFile);
		ns = model.getNsPrefixURI("");
		//#3
		OWLUtil.createInstances(model, declaredInstancesFile);
		this.declaredEquip = QueryUtil.getIndividualsURI(model, ns+"Equipment");
		createInterfaceHash(this.declaredEquip);
		//#7 and #8
		verifiyMinimumEquipment();	
		
		bindedInterfaces = Queries.getBindedInterfaces(model, interfaces);
		
		//#9
		verifyIfEquipmentMapsOutPortsInSource();
		INT_SO_LIST = verifyIfEquipmentMapsInPortsInSource();
		//removeInterfaces(INT_SO_LIST, bindedInterfaces);
		INT_SO_LIST.removeAll(bindedInterfaces);
		verifyIfEquipmentMapsInPortsInSink();
		INT_SK_LIST = verifyIfEquipmentMapsOutPortsInSink();
		//removeInterfaces(INT_SK_LIST, bindedInterfaces);
		INT_SK_LIST.removeAll(bindedInterfaces);
		
		//#15
		OWLUtil.createInstances(model, possibleEquipFile);
		this.possibleEquip = QueryUtil.getIndividualsURI(model, ns+"Equipment");
		this.possibleEquip.removeAll(this.declaredEquip);
		createInterfaceHash(this.possibleEquip);
		bindedInterfaces = Queries.getBindedInterfaces(model, interfaces);
		createBindedInterfaceHash(bindedInterfaces);
		//#16
		verifiyMinimumEquipWithPM();
		//#17
		OWLUtil.runReasoner(okcoUploader, true, true, true);
	}
	
	public void provision(Interface interfaceFrom, Interface interfaceTo, Character option) throws Exception{
		try {
			if(option.equals('M') || option.equals('m')){
				callAlgorithmManual(interfaceFrom, interfaceTo);
			}else{
				callAlgorithmSemiAuto(interfaceFrom, interfaceTo);
			}
			
			//#23
			OWLUtil.runReasoner(okcoUploader, false, true, true);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		//#25
		OWLUtil.saveNewOwl(model, "");
	}
	
	public List<Interface> getINT_SK_LIST() {
		return INT_SK_LIST;
	}
	
	public List<Interface> getINT_SO_LIST() {
		return INT_SO_LIST;
	}
	
	public List<Interface> getBindedInterfaces() {
		return bindedInterfaces;
	}
	
	private void createInterfaceHash(List<String> equips){
		for (String equipURI : equips) {
			List<String> intURIs = QueryUtil.getIndividualFromRelation(model, equipURI, ns+"componentOf", ns+"Interface");
			boolean declared = declaredEquip.contains(equipURI);
			
			for (String intURI : intURIs) {
				if(!interfaces.containsKey(intURI)){
					Interface newInt = new Interface(intURI, equipURI, declared);
					this.interfaces.put(intURI, newInt);
				}				
			}
			
		}	
	}
	
	private void createBindedInterfaceHash(List<Interface> bindedInterfaces){
		for (Interface interface1 : bindedInterfaces) {
			this.bindedInterfacesHash.put(interface1.getInterfaceURI(), interface1);
		}
	}
	
	public List<DtoInstance> verifiyMinimumEquipment() throws Exception{
		List<DtoInstance> equipList = DtoQueryUtil.getIndividualsFromClass(model, "Equipment");
		if(equipList.size() < 2){
			throw new Exception("Is required a minimun of 2 Equipment.\n");
		}
		
		return equipList;			
	}
	
	public List<Interface> verifyIfEquipmentMapsOutPortsInSource() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, true, true, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Output Interface maps an Output Port from a Source component.\n");
		}
		
		return equipAndInterfaces;
	}
	
	public List<Interface> verifyIfEquipmentMapsInPortsInSource() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, false, true, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Source component.\n");
		}
		return equipAndInterfaces;
	}
	
	public List<Interface> verifyIfEquipmentMapsInPortsInSink() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, false, false, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public List<Interface> verifyIfEquipmentMapsOutPortsInSink() throws Exception{
		List<Interface> equipAndInterfaces = Queries.getInterfacesAndEquipMappingPorts(model, true, false, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Output Interface maps an Output Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public void verifiyMinimumEquipWithPM() throws Exception{
		List<String> equipWithPm = Queries.getEquipmentWithPhysicalMedia(model);
		if(equipWithPm.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment with Physical Media.\n");
		}		
	}
	
	public void callAlgorithmSemiAuto(Interface interfaceFrom, Interface interfaceTo) throws Exception{
		DefaultMutableTreeNode sourceRoot;
		sourceRoot = new DefaultMutableTreeNode(interfaceFrom);
        
        List<Interface> usedInterfaces = new ArrayList<Interface>();
        usedInterfaces.add(interfaceFrom);
		
		int qtShortPaths = Main.getOptionFromConsole("Choose the number of paths (enter 0 to show all)", 0, Integer.MAX_VALUE);
		if(qtShortPaths == 0){
			qtShortPaths = Integer.MAX_VALUE;
		}
		int maxPathSize = Main.getOptionFromConsole("Choose the maximum number of interfaces in a path (enter 0 for no limit)", 0, Integer.MAX_VALUE);
		if(maxPathSize == 0){
			maxPathSize = 1000;
		}
		
		Date beginDate = new Date();
		List<Path> paths = new ArrayList<Path>();
		algorithmSemiAuto(sourceRoot, true, paths , interfaceTo, usedInterfaces, qtShortPaths, maxPathSize);
		
		String semiAutoExecTime = PerformanceUtil.printExecutionTime("callAlgorithmSemiAuto", beginDate);
		
		if(paths.size() == 0){
			throw new Exception("Something went wrong. No paths were found from " + interfaceFrom.getInterfaceURI() + " to " + interfaceTo.getInterfaceURI() + ".");
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
			//TreeNode[] path = paths.get(i);
			List<Interface> path = paths.get(i).getInterfaceList();
			String out = "";
			int id = (i+1)/1;
			
			for (Interface intfc : path) {
				out += intfc;
				out += " -> ";
			}

			out += "\n";
			out += "\tsize: " + (path.size()/*+tgtPath.length*/) + "\n";
			
			if(!outs.contains(out)){
				outs.add(out);
			}else{
				System.out.println();
			}
			
			out = id + " - " + out;
			
			System.out.print(out);
			try{
				fos.write(out.getBytes());   				  
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		fos.close();
		
		System.out.println(semiAutoExecTime);
		int path = Main.getOptionFromConsole(paths, "path", paths.size());
		
		provisionSemiAuto(paths.get(path));
	}
	
	public void myBubbleSort(List<Path> paths) {
		boolean troca = true;
		Path aux;
		int inc = 1;
        while (troca) {
            troca = false;
            for (int i = 0; i < paths.size() - inc; i += inc) {
//            	String a1 = list.get(i);
//            	String a2 = list.get(i + increment);
            	int size1 = paths.get(i).size();// + list.get(i+1).length;
            	int size2 = paths.get(i+inc).size();// + list.get(i+3).length;
            	//int comparison = list.get(i).compareTo(list.get(i + 2));
            	if(size1 > size2){
            		for(int j = i; j < i + inc; j++){
            			aux = paths.get(j);
            			paths.set(j, paths.get(j + inc));
                        //list.get(j) = list.get(j + increment);
            			paths.set(j + inc, aux);
                        //list[j + increment] = aux;                        
            		}
            		troca = true;
            	}
            }
        }
    }
	
	public void provisionSemiAuto(Path path) throws Exception{
		boolean isSource = true;
		for (int i = 1; i < path.size()-1; i+=2) {
			Interface from = path.getInterfaceList().get(i);
			Interface to = path.getInterfaceList().get(i+1);
			isSource = isStillInSource(isSource, from);
			System.out.println(from.getInterfaceURI());
			System.out.println(to.getInterfaceURI());
			//System.out.println(isSource);
			bindsInterfaces(from, to, isSource);
		}
	}
	
	public boolean limitExceeded(List<Path> paths, List<Interface> usedInterfaces, int qtShortPaths, int maxPathSize){
		if(usedInterfaces.size() > maxPathSize){
			return true;
		}
		
		if(paths.size() >= qtShortPaths){
			Path lastPath = paths.get(paths.size()-1);
			int qtUsedInterfaces = usedInterfaces.size();
			if(qtUsedInterfaces%2 == 1){
				qtUsedInterfaces += 1;
			}
			if(lastPath.size() <= qtUsedInterfaces){
				return true;
			}
		}
		return false;
	}
	
	public void algorithmSemiAuto(DefaultMutableTreeNode lastInputIntNode, boolean isSource, List<Path> paths, Interface interfaceTo, List<Interface> usedInterfaces, int qtShortPaths, int maxPathSize) throws Exception{
		System.out.println("\nExecuting algorithmSemiAuto()...");
		String VAR_IN = ((Interface) lastInputIntNode.getUserObject()).getInterfaceURI();
		Interface in = interfaces.get(VAR_IN);
		//Interface in = new Interface(VAR_IN);
		
		if(limitExceeded(paths, usedInterfaces, qtShortPaths, maxPathSize)){
			return;
		}
		
		List<Interface> INT_LIST = algorithmPart1(in, isSource);
		for (int i = 0; i < INT_LIST.size(); i+=1) {
			String VAR_OUT = INT_LIST.get(i).getInterfaceURI();
			//Interface out = new Interface(VAR_OUT);
			Interface out = interfaces.get(VAR_OUT);
			
			if(!usedInterfaces.contains(out)){
				isSource = isStillInSource(isSource, out);
				List<Interface> newUsedInterfaces1 = new ArrayList<Interface>(); 
				newUsedInterfaces1.addAll(usedInterfaces);
				newUsedInterfaces1.add(out);
				
				DefaultMutableTreeNode outIntNode = new DefaultMutableTreeNode(out);
				lastInputIntNode.add(outIntNode);
				
//				if(containsInLeafs(leafs, outIntNode)){
//					return;
//				}
				
				if(VAR_OUT.equals(interfaceTo.getInterfaceURI())){
					if(limitExceeded(paths, newUsedInterfaces1, qtShortPaths, maxPathSize)){
						return;
					}
					
					if(paths.size() >= qtShortPaths){
						paths.remove(paths.size()-1);
					}

					Path path = new Path(outIntNode.getPath());
					int j = getOrderedIndex(paths, path, 0, 0, qtShortPaths, maxPathSize);
					
					paths.add(j, path);

					return;
				}else{
					List<Interface> listInterfacesTo = algorithmPart2(isSource, out);
					for (int j = 0; j < listInterfacesTo.size(); j+=1) {
						VAR_IN = listInterfacesTo.get(j).getInterfaceURI();
						//in = new Interface(VAR_IN);
						in = interfaces.get(VAR_IN);
						isSource = isStillInSource(isSource, in);
						
						if(!newUsedInterfaces1.contains(in)){
							List<Interface> newUsedInterfaces2 = new ArrayList<Interface>(); 
							newUsedInterfaces2.addAll(newUsedInterfaces1);
							newUsedInterfaces2.add(in);
							
							DefaultMutableTreeNode possibleInIntNode = new DefaultMutableTreeNode(in);
							outIntNode.add(possibleInIntNode);
							
							algorithmSemiAuto(possibleInIntNode, isSource, paths, interfaceTo, newUsedInterfaces2, qtShortPaths, maxPathSize);
						}						
					}	
				}
			}								
		}		
	}
	
	public int getOrderedIndex(List<Path> paths, Path path, int declaredWeight, int possibleWeight, int qtShortPaths, int maxPathSize){
		int i;
		for (i = 0; i < paths.size(); i++) {
			if(path.size() < paths.get(i).size()){
				break;
			}
		}
		return i;
	}
	
	public boolean containsInLeafs(List<DefaultMutableTreeNode> leafs, DefaultMutableTreeNode outIntNode){
		TreeNode[] p1 = outIntNode.getPath();
//		for (int j = 0; j < leafs.size()-1; j++) {
//			boolean isBreak = false;
//			TreeNode[] p1 = leafs.get(j).getPath();
			for (int k = 0; k < leafs.size(); k++) {
				TreeNode[] p2 = leafs.get(k).getPath();
				if(!p1.equals(p2)){
					int t1 = p1.length;
					int t2 = p2.length;
					//System.out.println(t1 + " - " + t2);
					if(t1 == t2){
						int l;
						boolean isBreak = false;
						for (l = 0; l < p2.length; l++) {
							DefaultMutableTreeNode n1 = (DefaultMutableTreeNode) p1[l];
							Interface i1 = (Interface) n1.getUserObject();
							DefaultMutableTreeNode n2 = (DefaultMutableTreeNode) p2[l];
							Interface i2 = (Interface) n2.getUserObject();
							if(!i1.equals(i2)){
								isBreak  = true;
								break;
							}
						}
						
						if(!isBreak){
						
							for (int i = 0; i < p1.length; i++) {
								System.out.print(p1[i] + " -> ");
							}
							System.out.println();
							for (int i = 0; i < p2.length; i++) {
								System.out.print(p2[i] + " -> ");
							}
							return true;
						}
					}else if(t1 > t2){
						break;
					}
				}
				
//				if(isBreak) break;
//			}						
		}
		return false;
	}
	public String callAlgorithmManual(Interface interfaceFrom, Interface interfaceTo) throws Exception{
		boolean isSource = true;
		String VAR_OUT = "";
		String VAR_IN = interfaceFrom.getInterfaceURI();
		bindedInterfaces.add(interfaceFrom);
		do {
			//#19
			//Interface in = new Interface(VAR_IN);
			Interface in = interfaces.get(VAR_IN);
			isSource = isStillInSource(isSource, in);
			List<Interface> INT_LIST = algorithmPart1(in, isSource);
			
			int chosenId = Main.chooseOne(INT_LIST, "Output Interfaces", "Available Output Interface");
			VAR_OUT = INT_LIST.get(chosenId).getInterfaceURI();
			
			//#20
			if(!VAR_OUT.equals(interfaceTo.getInterfaceURI())){
				//#21
				//Interface out = new Interface(VAR_OUT);
				Interface out = interfaces.get(VAR_OUT);
				
				isSource = isStillInSource(isSource, out);
				List<Interface> listInterfacesTo = algorithmPart2(isSource, out);
				
				//#D
				int interfaceToId = Main.chooseOne(listInterfacesTo, "Input Interfaces", "Available Input Interface");
				VAR_IN = listInterfacesTo.get(interfaceToId).getInterfaceURI();
				
				//#22
				//in = new Interface(VAR_IN);
				in = interfaces.get(VAR_IN);
				bindsInterfaces(out, in, isSource);
			}
		} while (!VAR_OUT.equals(interfaceTo.getInterfaceURI()));//#20
		
		return VAR_OUT;
	}
	
	public boolean isStillInSource(boolean isSourceOld, Interface intfc){
		boolean isSource = isSourceOld;
		boolean isInterfaceSource = QueryUtil.isInterfaceSource(model, intfc.getInterfaceURI());
		if((isSourceOld && !isInterfaceSource) || (!isSourceOld && isInterfaceSource)){
			isSource  = !isSourceOld;				
		}
		return isSource;
	}
	
	public Interface getInterface(String interfaceURI){
		Interface equipInterface = interfaces.get(interfaceURI);
		return equipInterface;
	}
	
	public List<Interface> algorithmPart1(Interface inputInterface, boolean isSource) throws Exception{
		List<Interface> LIST_INT = inputInterface.getCandidateInterfacesTo();
		
		if(LIST_INT.size() == 0){
			//#A
			String mappedTF = Queries.getMappedTFFrom(model, inputInterface.getInterfaceURI());
			List<String> bindedTFList = Queries.getLastBindedTFFrom(model, mappedTF, isSource);
			//System.out.println();
			if(!bindedTFList.contains(mappedTF)){
				bindedTFList.add(mappedTF);
			}		
			LIST_INT = new ArrayList<Interface>();
			for (String tfURI : bindedTFList) {
				LIST_INT.addAll(Queries.getMappingInterfaceFrom(model, tfURI, interfaces, bindedInterfacesHash));
			}
			
			inputInterface.setCandidateInterfacesTo(LIST_INT);
		}
//		if(interfaces.containsKey(inputInterface.getInterfaceURI())){
//			//Interface out = interfaces.get(inputInterface.getInterfaceURI());
//			LIST_INT = inputInterface.getCandidateInterfacesTo();
//			LIST_INT.removeAll(bindedInterfaces);
//		}else{
//			
//			interfaces.put(inputInterface.getInterfaceURI(), inputInterface);
//		}
		
		return LIST_INT;
	}
	
	public List<Interface> algorithmPart2(boolean isSource, Interface outputInterface) throws Exception{
		List<Interface> listInterfacesTo = outputInterface.getCandidateInterfacesTo();
		listInterfacesTo.removeAll(bindedInterfaces);
		if(listInterfacesTo.size() == 0){
			listInterfacesTo = Queries.getInterfacesToProvision(model, outputInterface.getInterfaceURI(), isSource, interfaces, bindedInterfacesHash);
			outputInterface.setCandidateInterfacesTo(listInterfacesTo);
		}
//		if(interfaces.containsKey(outputInterface.getInterfaceURI())){
//			Interface in = interfaces.get(outputInterface.getInterfaceURI());
//			listInterfacesTo = in.getCandidateInterfacesTo();
//			listInterfacesTo.removeAll(bindedInterfaces);
//		}else{
//			//#C
//			listInterfacesTo = Queries.getInterfacesToProvision(model, outputInterface.getInterfaceURI(), isSource, interfaces, bindedInterfacesHash);
//			outputInterface.setCandidateInterfacesTo(listInterfacesTo);
//			interfaces.put(outputInterface.getInterfaceURI(), outputInterface);
//		}
		
		return listInterfacesTo;
	}
	
	public void bindsInterfaces(Interface interfaceFrom, Interface interfaceTo, boolean isSource) throws Exception{
		//#E
		if(!isSource){
			Interface aux = interfaceFrom;
			interfaceFrom = interfaceTo;
			interfaceTo = aux;
		}
		String outPort = Queries.getMappedPort(model, interfaceFrom.getInterfaceURI());
		String inPort = Queries.getMappedPort(model, interfaceTo.getInterfaceURI());
		FactoryUtil.createInstanceRelation(model, outPort, ns+"binds", inPort, false, false, true);
		bindedInterfaces.add(interfaceFrom);
		bindedInterfaces.add(interfaceTo);
	}
}
