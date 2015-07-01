package provisioner.business;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import provisioner.domain.Interface;
import provisioner.domain.Path;
import provisioner.jenaUtil.OWLUtil;
import provisioner.jenaUtil.SPARQLQueries;
import provisioner.util.ConsoleUtil;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.OntModel;

public class Provisioner {
	HashMap<String, Interface> interfaces = new HashMap<String, Interface>();
	List<Interface> bindedInterfaces;
	List<Interface> originalBindedInterfaces = new ArrayList<Interface>();
	HashMap<String, Interface> bindedInterfacesHash;
	List<String> declaredEquip;
	List<String> possibleEquip;
	String possibleEquipFile;
	OntModel model;
	String ns;
	List<Interface> INT_SO_LIST;
	List<Interface> INT_SK_LIST;
	OKCoUploader okcoUploader = new OKCoUploader("Provisioner");
	long reasoningTimeExecPostInstances = 0;
	long createInstancesTime = 0;
	OntModel consistencyModel;
	
	public long getReasoningTimeExecPostInstances() {
		return reasoningTimeExecPostInstances;
	}	
	
	public long getCreateInstancesTime() {
		return createInstancesTime;
	}


	public Provisioner(String baseTBoxFile, String consistencyTBoxFile, String declaredInstancesFile, String possibleEquipFile, int declaredReplications, int possibleReplications) throws Exception {
			this.possibleEquipFile = possibleEquipFile;
			//#1
			OWLUtil.createTBox(this.okcoUploader, baseTBoxFile, consistencyTBoxFile);
			model = this.okcoUploader.getBaseModel();
			consistencyModel = this.okcoUploader.getConsistencyBaseModel();
			
			ns = model.getNsPrefixURI("");
			//#3
			Date beginDate = new Date();
			ArrayList<OntModel> models = new ArrayList<OntModel>();
			models.add(model);
			models.add(consistencyModel);
			OWLUtil.createInstances(models, declaredInstancesFile, declaredReplications);
			createInstancesTime = PerformanceUtil.getExecutionTime(beginDate);
			this.declaredEquip = QueryUtil.getIndividualsURI(model, ns+"Equipment");
			createInterfaceHash(this.declaredEquip);
			//#7 and #8
			verifiyMinimumEquipment();	
			
			bindedInterfaces = SPARQLQueries.getBindedInterfaces(model, interfaces);
			
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
			beginDate = new Date();
			OWLUtil.createInstances(models, possibleEquipFile, possibleReplications);
			createInstancesTime += PerformanceUtil.getExecutionTime(beginDate);
			this.possibleEquip = QueryUtil.getIndividualsURI(model, ns+"Equipment");
			this.possibleEquip.removeAll(this.declaredEquip);
			createInterfaceHash(this.possibleEquip);
			bindedInterfaces = SPARQLQueries.getBindedInterfaces(model, interfaces);
			createBindedInterfaceHash(bindedInterfaces);
			
			//#16
			verifiyMinimumEquipWithPM();
			
			originalBindedInterfaces.addAll(bindedInterfaces);
			
			//#17
			reasoningTimeExecPostInstances = OWLUtil.runReasoner(okcoUploader, true, true, true);
			System.out.println(PerformanceUtil.getExecutionMessage("createInstances", createInstancesTime));			
	}
	
	public HashMap<String, Interface> getInterfaces() {
		return interfaces;
	}
	
	public OntModel getModel() {
		return model;
	}
	
	public void consoleProvisioner() throws Exception{
		ArrayList<Character> modeOptions = new ArrayList<Character>();
		modeOptions.add('A');
		modeOptions.add('M');
		modeOptions.add('a');
		modeOptions.add('m');
		ArrayList<Character> provisionAgainOptions = new ArrayList<Character>();
		provisionAgainOptions.add('Y');
		provisionAgainOptions.add('y');
		provisionAgainOptions.add('N');
		provisionAgainOptions.add('n');
		
		bindedInterfaces = new ArrayList<Interface>();
		bindedInterfaces.addAll(originalBindedInterfaces);
		Character provisionAgainOption;
		do{
			for (String intfc : interfaces.keySet()) {
				Interface in = interfaces.get(intfc);
				in.clearInterfaceTo();
			}
			//#10 and #11
			int srcInt2ProvIndex = ConsoleUtil.chooseOne(this.getINT_SO_LIST(), "Input Interfaces", "Choose the Source Input Interface to be provisioned (INT_SOURCE): ",0);
			//int srcInt2ProvIndex = 8;
			Interface interfaceFrom = this.getINT_SO_LIST().get(srcInt2ProvIndex);
			//String equipFromURI = INT_SO_LIST.get(srcInt2ProvIndex);
			
			//#12 and #13
			int tgtInt2ProvIndex = ConsoleUtil.chooseOne(this.getINT_SK_LIST(), "Output Interfaces", "Choose the Sink Output Interface to be provisioned (INT_SINK): ",0);
			//int tgtInt2ProvIndex = 8;
			Interface interfaceTo = this.getINT_SK_LIST().get(tgtInt2ProvIndex);
			//String equipToURI = INT_SK_LIST.get(tgtInt2ProvIndex+1);
			
			Character modeOption = ConsoleUtil.getCharOptionFromConsole("Choose provisioning mode: Automatically (A) or Manually (M)? ", modeOptions);
			
			
			if(modeOption.equals('M') || modeOption.equals('m')){
				callAlgorithmManual(interfaceFrom, interfaceTo);
			}else{
				callFindPaths(interfaceFrom, interfaceTo);
			}
			
			//#23
			OWLUtil.runReasoner(okcoUploader, false, true, true);
			
			provisionAgainOption = ConsoleUtil.getCharOptionFromConsole("Would you like to provision another path? Yes (Y) or No (N): ", provisionAgainOptions);
		}while(provisionAgainOption.equals('Y') || provisionAgainOption.equals('y'));
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
		this.bindedInterfacesHash = new HashMap<String, Interface>();
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
		List<Interface> equipAndInterfaces = SPARQLQueries.getInterfacesAndEquipMappingPorts(model, true, true, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimun of 1 Equipment which the Output Interface maps an Output Port from a Source component.\n");
		}
		
		return equipAndInterfaces;
	}
	
	public List<Interface> verifyIfEquipmentMapsInPortsInSource() throws Exception{
		List<Interface> equipAndInterfaces = SPARQLQueries.getInterfacesAndEquipMappingPorts(model, false, true, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Source component.\n");
		}
		return equipAndInterfaces;
	}
	
	public List<Interface> verifyIfEquipmentMapsInPortsInSink() throws Exception{
		List<Interface> equipAndInterfaces = SPARQLQueries.getInterfacesAndEquipMappingPorts(model, false, false, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Input Interface maps an Input Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public List<Interface> verifyIfEquipmentMapsOutPortsInSink() throws Exception{
		List<Interface> equipAndInterfaces = SPARQLQueries.getInterfacesAndEquipMappingPorts(model, true, false, interfaces);
		if(equipAndInterfaces.size() < 1){
			throw new Exception("Is required a minimum of 1 Equipment which the Output Interface maps an Output Port from a Sink component.\n");
		}
		return equipAndInterfaces;
	}
	
	public void verifiyMinimumEquipWithPM() throws Exception{
		List<String> equipWithPm = SPARQLQueries.getEquipmentWithPhysicalMedia(model);
		if(equipWithPm.size() < 1){
			throw new Exception("A minimum of 1 Equipment with Physical Media Is required.\n");
		}		
	}
	
	@SuppressWarnings("resource")
	public void callFindPaths(Interface interfaceFrom, Interface interfaceTo) throws Exception{
//		int again = 0;
		List<Path> paths;
		int qtShortPaths = ConsoleUtil.getOptionFromConsole("Choose the maximum number of paths (0 for no limit): ", 0, Integer.MAX_VALUE,0);
		int maxPathSize = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces in a path (0 for no limit): ", 0, Integer.MAX_VALUE,0);
					
		ArrayList<Character> options = new ArrayList<Character>();
		options.add('W');
		options.add('w');
		options.add('P');
		options.add('p');
		options.add('S');
		options.add('s');
		
		Character option = ' ';
		if(!this.possibleEquipFile.equals("")){
			option = ConsoleUtil.getCharOptionFromConsole(""
					+ "Choose the Path Selection Type:\n"
					+ "S - Paths are displayed in descending order with relation to its number of interfaces\n"
					+ "P - Paths are displayed in descending order with relation to its number of interfaces of possible equipment\n"
					+ "W - Paths are displayed in descending order with relation to a weighted function\n", options);
		}
		
		int declaredWeight = 1;
		int possibleWeight = 1;
		boolean fewPossibleEquip = false;
		
		if(option.equals('W') || option.equals('w') || this.possibleEquipFile.equals("")){
			if(this.possibleEquipFile.equals("")){
				System.out.println("The paths will be displayed in descending order considering its number of interfaces.");
			}else{
				System.out.println("The paths will be selected according the function: X*NumOfDeclaredInterfaces + Y*NumOfPossibleInterfaces (in descending order).");
				declaredWeight = ConsoleUtil.getOptionFromConsole("Choose the value of X: ", 0, Integer.MAX_VALUE,0);
				possibleWeight = ConsoleUtil.getOptionFromConsole("Choose the value of Y: ", 0, Integer.MAX_VALUE,0);
			}
		}else if(option.equals('P') || option.equals('p')){
			fewPossibleEquip = true;
		}
		
		Date beginDate = new Date();
		//List<Path> paths = new ArrayList<Path>();
		paths = findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
		//algorithmSemiAuto(sourceRoot, true, paths , interfaceTo, usedInterfaces, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
		long semiAutoExecTimeLong = PerformanceUtil.getExecutionTime(beginDate);
//			String semiAutoExecTime = PerformanceUtil.printExecutionTime("findPaths", beginDate);
		
		if(paths.size() == 0){
			throw new Exception("Something went wrong. No paths were found from " + interfaceFrom.getInterfaceURI() + " to " + interfaceTo.getInterfaceURI() + ".");
		}			
		
		File arquivo = new File("resources/output/possible.txt");   
		if(arquivo.exists()){
			arquivo.delete();
		}
		FileOutputStream fos = new FileOutputStream(arquivo);    

		String reasonerExec = "Reasoning execution: " + this.reasoningTimeExecPostInstances + "ms\n";
		String pathsExec = "Find paths execution: " + semiAutoExecTimeLong + "ms\n";
		
		fos.write(reasonerExec.getBytes());
		fos.write(pathsExec.getBytes());
		System.out.println("--- PATHS ---");
		ArrayList<String> outs = new ArrayList<String>();
		for(int i = 0; i < paths.size(); i+=1){
			String out = "";
			int id = (i+1)/1;
			if(!outs.contains(out)){
				outs.add(paths.get(i).toString());
			}else{
				throw new Exception("Something went wrong. A duplicated path was found.");
			}
			
			out = id + " - " + paths.get(i);
			
			System.out.print(out);
			fos.write(out.getBytes());   				  

		}
		fos.close();
		
		int path = ConsoleUtil.getOptionFromConsole(paths, "Choose path from list to be provisioned: ", paths.size());
		provisionSemiAuto(paths.get(path));
		
		bindedInterfaces = SPARQLQueries.getBindedInterfaces(model, interfaces);
		createBindedInterfaceHash(bindedInterfaces);
	}
	
	public void provisionSemiAuto(Path path) throws Exception{
		boolean isSource = true;
		for (int i = 1; i < path.size()-1; i+=2) {
			Interface previousTo = path.getInterfaceList().get(i-1);
			Interface from = path.getInterfaceList().get(i);
			Interface to = path.getInterfaceList().get(i+1);
			isSource = isStillInSource(isSource, from);
			bindsInterfaces(from, to, isSource);
			
			createPath(previousTo, from);
			createPath(from, to);
			
			if(!bindedInterfacesHash.containsKey(from.getInterfaceURI())){
				bindedInterfacesHash.put(from.getInterfaceURI(), from);
			}
			if(!bindedInterfacesHash.containsKey(to.getInterfaceURI())){
				bindedInterfacesHash.put(to.getInterfaceURI(), to);
			}
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
	
	public List<Path> findPaths(Interface interfaceFrom, Interface interfaceTo, int qtShortPaths, int maxPathSize, int declaredWeight, int possibleWeight, boolean fewPossibleEquip) throws Exception{
		if(qtShortPaths == 0){
			qtShortPaths = Integer.MAX_VALUE;
		}
		if(maxPathSize == 0){
			maxPathSize = 1000;
		}
		DefaultMutableTreeNode sourceRoot;
		sourceRoot = new DefaultMutableTreeNode(interfaceFrom);
		
		ArrayList<Path> paths = new ArrayList<Path>();
		
		List<Interface> usedInterfaces = new ArrayList<Interface>();
        usedInterfaces.add(interfaceFrom);
        
        Date beginDate = new Date();
		findPaths(sourceRoot, true, paths, interfaceTo, usedInterfaces, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
        PerformanceUtil.printExecutionTime("findPaths", beginDate );
        
        return paths;
	}
	
	public void findPaths(DefaultMutableTreeNode lastInputIntNode, boolean isSource, List<Path> paths, Interface interfaceTo, List<Interface> usedInterfaces, int qtShortPaths, int maxPathSize, int declaredWeight, int possibleWeight, boolean fewPossibleEquip) throws Exception{
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
				
				if(VAR_OUT.equals(interfaceTo.getInterfaceURI())){
					if(limitExceeded(paths, newUsedInterfaces1, qtShortPaths, maxPathSize)){
						return;
					}
					
					if(paths.size() >= qtShortPaths){
						paths.remove(paths.size()-1);
					}

					Path path = new Path(outIntNode.getPath());
					int j = getOrderedIndex(paths, path, declaredWeight, possibleWeight, fewPossibleEquip);
					
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
							
							findPaths(possibleInIntNode, isSource, paths, interfaceTo, newUsedInterfaces2, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
						}						
					}	
				}
			}								
		}		
	}
	
	public int getOrderedIndex(List<Path> paths, Path path, int declaredWeight, int possibleWeight, boolean fewPossibleEquip){
		int i;
		int pathSize = declaredWeight * path.getQtDeclared() + possibleWeight * path.getQtPossible();
		for (i = 0; i < paths.size(); i++) {
			int pathISize = declaredWeight * paths.get(i).getQtDeclared() + possibleWeight * paths.get(i).getQtPossible();
			if(fewPossibleEquip){
				//int x = paths.get(i).getQtPossible();
				if(path.getQtPossible() < paths.get(i).getQtPossible()){
					break;
				}else if(path.getQtPossible() == paths.get(i).getQtPossible() && pathSize < pathISize){
					break;
				}				
			}else{
				if(pathSize < pathISize){
					break;
				}
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
		Interface out = null;
		do {
			//#19
			//Interface in = new Interface(VAR_IN);
			Interface in = interfaces.get(VAR_IN);
			
			if(out != null){
				createPath(out, in);
			}
			
			isSource = isStillInSource(isSource, in);
			List<Interface> INT_LIST = algorithmPart1(in, isSource);
			
			int chosenId = ConsoleUtil.chooseOne(INT_LIST, "Output Interfaces", "Choose an available Output Interface (VAR_OUT): ",0);
			VAR_OUT = INT_LIST.get(chosenId).getInterfaceURI();
			
			//#20
			if(!VAR_OUT.equals(interfaceTo.getInterfaceURI())){
				//#21
				//Interface out = new Interface(VAR_OUT);
				out = interfaces.get(VAR_OUT);
				
				createPath(in, out);
				
				isSource = isStillInSource(isSource, out);
				List<Interface> listInterfacesTo = algorithmPart2(isSource, out);
				
				//#D
				int interfaceToId = ConsoleUtil.chooseOne(listInterfacesTo, "Input Interfaces", "Choose an available Input Interface (VAR_IN): ",0);
				VAR_IN = listInterfacesTo.get(interfaceToId).getInterfaceURI();
				
				//#22
				//in = new Interface(VAR_IN);
				in = interfaces.get(VAR_IN);
				bindsInterfaces(out, in, isSource);
			}
		} while (!VAR_OUT.equals(interfaceTo.getInterfaceURI()));//#20
		
		return VAR_OUT;
	}
	
	private void createPath(Interface int1, Interface int2) throws Exception {
		String int1Ns = int1.getInterfaceURI();
		String int2Ns = int2.getInterfaceURI();
		
		FactoryUtil.createInstanceRelation(model, int1Ns, ns+"path", int2Ns, false, false, true);
	}


	public boolean isStillInSource(boolean isSourceOld, Interface intfc){
		boolean isSource = isSourceOld;
		boolean isInterfaceSource = SPARQLQueries.isInterfaceSource(model, intfc.getInterfaceURI());
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
		List<Interface> LIST_INT = inputInterface.getCandidateInterfacesTo(this.bindedInterfaces);
		
		if(LIST_INT.size() == 0){
			//#A
			String mappedTF = SPARQLQueries.getMappedTFFrom(model, inputInterface.getInterfaceURI());
			List<String> bindedTFList = SPARQLQueries.getLastBindedTFFrom(model, mappedTF, isSource);
			
			//System.out.println();
			if(!bindedTFList.contains(mappedTF)){
				bindedTFList.add(mappedTF);
			}		
			LIST_INT = new ArrayList<Interface>();
			for (String tfURI : bindedTFList) {
				List<Interface> list = SPARQLQueries.getMappingInterfaceFrom(model, tfURI, interfaces, bindedInterfacesHash);
				
				list.removeAll(SPARQLQueries.getInterfacesMappingMatrixes(model, isSource, interfaces));
				
				LIST_INT.addAll(list);
			}
			
			inputInterface.setCandidateInterfacesTo(LIST_INT, this.bindedInterfaces);
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
		List<Interface> listInterfacesTo = outputInterface.getCandidateInterfacesTo(this.bindedInterfaces);
		listInterfacesTo.removeAll(bindedInterfaces);
		if(listInterfacesTo.size() == 0){
			listInterfacesTo = SPARQLQueries.getInterfacesToProvision(model, outputInterface.getInterfaceURI(), isSource, interfaces, bindedInterfacesHash);
			listInterfacesTo.removeAll(SPARQLQueries.getInterfacesMappingMatrixes(model, isSource, interfaces));
			outputInterface.setCandidateInterfacesTo(listInterfacesTo, this.bindedInterfaces);
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
		String outPort = SPARQLQueries.getMappedPort(model, interfaceFrom.getInterfaceURI());
		String inPort = SPARQLQueries.getMappedPort(model, interfaceTo.getInterfaceURI());
		FactoryUtil.createInstanceRelation(model, outPort, ns+"binds", inPort, false, false, true);
		this.bindedInterfaces.add(interfaceFrom);
		this.bindedInterfaces.add(interfaceTo);
	}
}
