package provisioner.business;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import provisioner.domain.IntBinds;
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
	List<Interface> pathInterfaces;
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
	HashMap<Interface, List<Interface>> origPaths;// = new HashMap<Interface, List<Interface>>();
	
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
		
		origPaths = new HashMap<Interface, List<Interface>>();
		List<IntBinds> intBinds = SPARQLQueries.getIntBinds(model, interfaces);
		populaOrigPaths(intBinds, false);
		List<IntBinds> internalIntBinds = SPARQLQueries.getInternalIntBinds(model, interfaces);
		populaOrigPaths(internalIntBinds, true);
		
		refreshInterfaces();
		pathInterfaces = SPARQLQueries.getIntPaths(model, interfaces);
		
	}
	
	public void populaOrigPaths(List<IntBinds> intBinds, boolean internalPath){
		for (IntBinds intBind : intBinds) {
			Interface intFrom = interfaces.get(intBind.getInterfaceFrom().getInterfaceURI());
			
			boolean fromIsSource = SPARQLQueries.isInterfaceSource(model, intFrom.getInterfaceURI());
//			boolean fromIsSource = intFrom.isSource();
			Interface intTo = interfaces.get(intBind.getInterfaceTo().getInterfaceURI());
			boolean toIsSource = SPARQLQueries.isInterfaceSource(model, intTo.getInterfaceURI());
//			boolean toIsSource = intTo.isSource();
			
			if(internalPath){				
				String tfFromURI = SPARQLQueries.getMappedTFFrom(model, intFrom.getInterfaceURI());
				List<String> tfFromURIs = SPARQLQueries.getLastBindedTFFrom(model, tfFromURI, fromIsSource);
				tfFromURIs.add(tfFromURI);
//				List<String> tfFromURIs = intFrom.getAllLastMappedTfURI();
				String tfToURI = SPARQLQueries.getMappedTFFrom(model, intTo.getInterfaceURI());
//				String tfToURI = intTo.getMappedTfURI();
				
				if (!tfFromURIs.contains(tfToURI)) {
					continue;
				}				
			}else if(!fromIsSource && !toIsSource){
				Interface aux = intFrom;
				intFrom = intTo;
				intTo = aux;
			}
			
			List<Interface> listIntfcTo;
			if(origPaths.containsKey(intFrom)){
				listIntfcTo = origPaths.get(intFrom);
			}else{
				listIntfcTo = new ArrayList<Interface>();
			}
			listIntfcTo.add(intTo);
			if(!origPaths.containsKey(intFrom)){
				origPaths.put(intFrom, listIntfcTo);
			}
		}
	}
	
	public List<Path> getOrigPaths(Interface originalInterfaceFrom, Interface originalInterfaceTo){
		System.out.println("\nExecuting getOrigPaths()...");
		List<Interface> usedInterfaces = new ArrayList<Interface>();
		usedInterfaces.add(originalInterfaceFrom);
		List<Path> retPaths;
		if(originalInterfaceFrom.getEquipmentURI().equals(originalInterfaceTo.getEquipmentURI())){
			Path path = new Path();
			path.addInterface(originalInterfaceFrom);
			path.addInterface(originalInterfaceTo);
			retPaths = new ArrayList<Path>();
			retPaths.add(path);
		}else{
			retPaths = getOrigPathsRecursive(originalInterfaceFrom, originalInterfaceTo, usedInterfaces);
		}		
		
		return retPaths;
	}
	
	public List<Path> getOrigPathsRecursive(Interface currentInterfaceFrom, Interface originalInterfaceTo, List<Interface> usedInterfaces){
//		isSource = isStillInSource(isSource, from);
		System.out.println("\nExecuting getOrigPathsRecursive()...");
		List<Path> retPaths = null;
		
//		System.out.println(currentInterfaceFrom);
		List<Interface> listIntfcTo = new ArrayList<Interface>();
		if(currentInterfaceFrom.getEquipmentURI().equals(originalInterfaceTo.getEquipmentURI())){
			listIntfcTo.add(originalInterfaceTo);
		}else{
			List<Interface> aux = origPaths.get(currentInterfaceFrom);
			if(aux != null) listIntfcTo.addAll(aux);
			listIntfcTo.remove(currentInterfaceFrom);
			listIntfcTo.removeAll(usedInterfaces);			
		}
		
		for (Interface interfaceTo : listIntfcTo) {
			if(interfaceTo.equals(originalInterfaceTo)){
				Path path = new Path();
				path.addInterface(currentInterfaceFrom);
				path.addInterface(interfaceTo);
				retPaths = new ArrayList<Path>();
				retPaths.add(path);
				
//				return retPaths;
			}else{
				List<Interface> newUsedInterfaces = new ArrayList<Interface>();
				newUsedInterfaces.addAll(usedInterfaces);
				newUsedInterfaces.add(interfaceTo);
				List<Path> newRetPaths = getOrigPathsRecursive(interfaceTo, originalInterfaceTo, newUsedInterfaces);
				if(newRetPaths != null){
					if(retPaths == null) retPaths = new ArrayList<Path>();
					
					for (Path path : newRetPaths) {
//						path.addInterface(interfaceTo);
						path.addInterfaceInBegin(currentInterfaceFrom, true);
						
						retPaths.add(path);
					}
//					return retPaths;
				}
			}
		}//in_int01_so_EQ1_ODUSwitch
		return retPaths;
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
			
			System.out.println(PerformanceUtil.getExecutionMessage("reasoningTimeExecPostInstances", reasoningTimeExecPostInstances));
			System.out.println(PerformanceUtil.getExecutionMessage("createInstances", createInstancesTime));
			//#10 and #11
			int srcInt2ProvIndex = ConsoleUtil.chooseOne(this.getINT_SO_LIST(), "Input Interfaces", "Choose the Source Input Interface to be provisioned (INT_SOURCE): ",0, true, false);
//			int srcInt2ProvIndex = 6;
			Interface interfaceFrom = this.getINT_SO_LIST().get(srcInt2ProvIndex);
			//String equipFromURI = INT_SO_LIST.get(srcInt2ProvIndex);
			
			//#12 and #13
			int tgtInt2ProvIndex = ConsoleUtil.chooseOne(this.getINT_SK_LIST(), "Output Interfaces", "Choose the Sink Output Interface to be provisioned (INT_SINK): ",0, true, false);
//			int tgtInt2ProvIndex = 8;
			Interface interfaceTo = this.getINT_SK_LIST().get(tgtInt2ProvIndex);
			//String equipToURI = INT_SK_LIST.get(tgtInt2ProvIndex+1);
			
			Character modeOption = ConsoleUtil.getCharOptionFromConsole("Choose provisioning mode: Automatically (A) or Manually (M)? ", modeOptions);
//			Character modeOption = 'a';
			
			Path path;
			if(modeOption.equals('M') || modeOption.equals('m')){
				path = callAlgorithmManual(interfaceFrom, interfaceTo);
			}else{
				path = callFindPaths(interfaceFrom, interfaceTo);
			}
			
			provisionSemiAuto(path);
			
			bindedInterfaces = SPARQLQueries.getBindedInterfaces(model, interfaces);
			createBindedInterfaceHash(bindedInterfaces);
			
			//#23
			OWLUtil.runReasoner(okcoUploader, false, true, true);
			
			provisionAgainOption = ConsoleUtil.getCharOptionFromConsole("Would you like to provision another path? Yes (Y) or No (N): ", provisionAgainOptions);
			
			origPaths = new HashMap<Interface, List<Interface>>();
			List<IntBinds> intBinds = SPARQLQueries.getIntBinds(model, interfaces);
			populaOrigPaths(intBinds, false);
			List<IntBinds> internalIntBinds = SPARQLQueries.getInternalIntBinds(model, interfaces);
			populaOrigPaths(internalIntBinds, true);
			
			refreshInterfaces();
			pathInterfaces = SPARQLQueries.getIntPaths(model, interfaces);
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
	
	private void refreshInterfaces() {
//		int size = this.interfaces.size();
//		int i = 0;
		for (Interface intfcFrom : this.interfaces.values()) {
//			i++;
			String mappedTfURI = SPARQLQueries.getMappedTFFrom(model, intfcFrom.getInterfaceURI());
			
			List<String> tfTypes = SPARQLQueries.getTfTypesMappedByInterface(model, intfcFrom.getInterfaceURI());
			if(tfTypes.contains(ns+"Matrix_Source") && intfcFrom.isOutput()){
				intfcFrom.setMapsMatrixSourceOutput(true);
			}
			if(tfTypes.contains(ns+"Matrix_Sink") && !intfcFrom.isOutput()){
				intfcFrom.setMapsMatrixSinkInput(true);
			}
			List<String> lastMappedTfURI = SPARQLQueries.getLastBindedTFFrom(model, mappedTfURI, intfcFrom.isSource());
			intfcFrom.setLastMappedTfURI(lastMappedTfURI);
			intfcFrom.setMappedTfURI(mappedTfURI);
//			int j = 0;
			for (Interface intfcTo : this.interfaces.values()) {
//				j++;
				if(intfcFrom.equals(intfcTo) || intfcFrom.isOutput() || !intfcTo.isOutput()){
							continue;
				}
				
				List<Path> internalPath = getOrigPaths(intfcFrom, intfcTo);
				if(internalPath != null){
					if(internalPath.size() > 0 && internalPath.get(0).size() > 2){
						intfcFrom.addInternalPaths(intfcTo, internalPath);
					}
				}
			}			
		}
	}

	
	private void createInterfaceHash(List<String> equips){
		for (String equipURI : equips) {
			List<String> intURIs = QueryUtil.getIndividualFromRelation(model, equipURI, ns+"componentOf", ns+"Interface");
			boolean declared = declaredEquip.contains(equipURI);
			
			for (String intURI : intURIs) {
				if(!interfaces.containsKey(intURI)){
					boolean isSource = SPARQLQueries.isInterfaceSource(model, intURI);
					List<String> types = QueryUtil.getIndividualTypes(model, intURI);
					boolean isOutput;
					if(types.contains(model.getNsPrefixURI("")+"Output_Interface")){
						isOutput = true;
					}else{
						isOutput = false;
					}
					Interface newInt = new Interface(intURI, equipURI, declared, isSource, isOutput);
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
	public Path callFindPaths(Interface interfaceFrom, Interface interfaceTo) throws Exception{
//		int again = 0;
		List<Path> paths;
		int qtShortPaths = ConsoleUtil.getOptionFromConsole("Choose the maximum number of paths (-1 for no limit): ", 1, Integer.MAX_VALUE,0, true);
//		int qtShortPaths = 10;
		if(qtShortPaths == -1){
			qtShortPaths = Integer.MAX_VALUE;
		}
		int maxPathSize = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces in a path (-1 for no limit): ", 1, Integer.MAX_VALUE,0, true);
//		int maxPathSize = 30;
		if(maxPathSize == -1){
//			maxPathSize = 1000;
			maxPathSize = Integer.MAX_VALUE;
		}		
		int maxNewBindings = ConsoleUtil.getOptionFromConsole("Choose the maximum number of new bindings in a path (-1 for no limit): ", 0, Integer.MAX_VALUE,0, true);
		if(maxNewBindings == -1){
			maxNewBindings = Integer.MAX_VALUE;
		}
		int maxNewPossible = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces of possible equipment (-1 for no limit): ", 0, Integer.MAX_VALUE,0, true);
		if(maxNewPossible == -1){
			maxNewPossible = Integer.MAX_VALUE;
		}
		ArrayList<String> options = new ArrayList<String>();
		options.add("minimum number of interfaces");
		options.add("minimum number of new bindings");
		if(!this.possibleEquipFile.equals("")){
			options.add("minimum number of interfaces of possible equipment");
		}
		
		int priorityOption = ConsoleUtil.chooseOne(options, "Priority", "Choose the Priority: ", 0, false, false);
		
		if(!this.possibleEquipFile.equals("")){
//			option = 'S';
			
//			option = ConsoleUtil.getCharOptionFromConsole(""
//					+ "Choose the Path Selection Type:\n"
//					+ "S - Paths are displayed in descending order with relation to its number of interfaces\n"
//					+ "P - Paths are displayed in descending order with relation to its number of interfaces of possible equipment\n"
//					+ "W - Paths are displayed in descending order with relation to a weighted function\n", options);
		}
		
//		int declaredWeight = 1;
//		int possibleWeight = 1;
//		boolean fewPossibleEquip = false;
		
//		if(option.equals('W') || option.equals('w') || this.possibleEquipFile.equals("")){
//			if(this.possibleEquipFile.equals("")){
//				System.out.println("The paths will be displayed in descending order considering its number of interfaces.");
//			}else{
//				System.out.println("The paths will be selected according the function: X*NumOfDeclaredInterfaces + Y*NumOfPossibleInterfaces (in descending order).");
//				declaredWeight = ConsoleUtil.getOptionFromConsole("Choose the value of X: ", 0, Integer.MAX_VALUE,0);
//				possibleWeight = ConsoleUtil.getOptionFromConsole("Choose the value of Y: ", 0, Integer.MAX_VALUE,0);
//			}
//		}else if(option.equals('P') || option.equals('p')){
//			fewPossibleEquip = true;
//		}
		
		Date beginDate = new Date();
		//List<Path> paths = new ArrayList<Path>();
		paths = findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, priorityOption, maxNewBindings, maxNewPossible);
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
		
		int path = ConsoleUtil.getOptionFromConsole(paths, "Choose path from list to be provisioned: ", paths.size(), false);
		
		return paths.get(path);
		
	}
	
	public void provisionSemiAuto(Path path) throws Exception{
		boolean isSource = true;
		for (int i = 0; i < path.size()-1; i++) {
			Interface actualInt = path.getInterfaceList().get(i);
			Interface nextInt = path.getInterfaceList().get(i+1);
//			int size = path.size();
			if(i ==  path.size()-2){
				System.out.println();
			}
			
			isSource = isStillInSource(isSource, actualInt);
			if(i%2==1){
				bindsInterfaces(actualInt, nextInt, isSource);		
			}			
			
			createPath(actualInt, nextInt);
			
			if(!bindedInterfacesHash.containsKey(actualInt.getInterfaceURI())){
				bindedInterfacesHash.put(actualInt.getInterfaceURI(), actualInt);
			}
			if(!bindedInterfacesHash.containsKey(nextInt.getInterfaceURI())){
				bindedInterfacesHash.put(nextInt.getInterfaceURI(), nextInt);
			}
		}
	}
	
	public boolean limitExceeded(List<Path> paths, Path actualPath, int qtShortPaths, int maxPathSize, int priorityOption, int maxNewBindings, int maxNewPossible){
		if(actualPath.size() > maxPathSize || actualPath.newBinds() > maxNewBindings || actualPath.getQtPossible() > maxNewPossible){
			return true;
		}
		
		if(paths.size() >= qtShortPaths){
			Path lastPath = paths.get(paths.size()-1);
			int qtUsedInterfaces = actualPath.size();
			if(qtUsedInterfaces%2 == 1){
				qtUsedInterfaces += 1;
			}
			switch (priorityOption) {
			case 0:
				if(lastPath.size() <= qtUsedInterfaces && lastPath.size() <= actualPath.size()){
					return true;
				}
				break;
			case 1:
				if(lastPath.newBinds() <= actualPath.newBinds()){
					return true;
				}
				break;
			case 2:
				if(lastPath.getQtPossible() <= actualPath.getQtPossible()){
					return true;
				}
				break;
			}
//			if(lastPath.size() <= qtUsedInterfaces){
//				return true;
//			}
		}
		
		return false;
	}
	
//	public List<Path> findPaths(Interface interfaceFrom, Interface interfaceTo, int qtShortPaths, int maxPathSize, int declaredWeight, int possibleWeight, boolean fewPossibleEquip) throws Exception{
	public List<Path> findPaths(Interface interfaceFrom, Interface interfaceTo, int qtShortPaths, int maxPathSize, int priorityOption, int maxNewBindings, int maxNewPossible) throws Exception{
		
		DefaultMutableTreeNode sourceRoot;
		sourceRoot = new DefaultMutableTreeNode(interfaceFrom);
		
		ArrayList<Path> paths = new ArrayList<Path>();
		
		List<Interface> usedInterfaces = new ArrayList<Interface>();
        usedInterfaces.add(interfaceFrom);
        
        Date beginDate = new Date();
		findPaths(sourceRoot, true, paths, interfaceTo, usedInterfaces, qtShortPaths, maxPathSize, priorityOption, maxNewBindings, maxNewPossible);
        PerformanceUtil.printExecutionTime("findPaths", beginDate );
        
        return paths;
	}
	
//	public void findPaths(DefaultMutableTreeNode lastInputIntNode, boolean isSource, List<Path> paths, Interface interfaceTo, List<Interface> usedInterfaces, int qtShortPaths, int maxPathSize, int declaredWeight, int possibleWeight, boolean fewPossibleEquip) throws Exception{
	public void findPaths(DefaultMutableTreeNode lastInputIntNode, boolean isSource, List<Path> paths, Interface interfaceTo, List<Interface> usedInterfaces, int qtShortPaths, int maxPathSize, int priorityOption, int maxNewBindings, int maxNewPossible) throws Exception{
		System.out.println("\nExecuting algorithmSemiAuto()...");
		String VAR_IN = ((Interface) lastInputIntNode.getUserObject()).getInterfaceURI();
//		String var_in_original = VAR_IN;
		Interface in = interfaces.get(VAR_IN);
		Interface in_orig = interfaces.get(VAR_IN);
		Path auxPath = new Path(lastInputIntNode.getPath());
		auxPath.setQtBindedInterfaces(this.bindedInterfaces);
		if(limitExceeded(paths, auxPath, qtShortPaths, maxPathSize, priorityOption, maxNewBindings, maxNewPossible)){
			return;
		}		
		
		List<Interface> INT_LIST = algorithmPart1(in, isSource);
		for (int i = 0; i < INT_LIST.size(); i+=1) {
			DefaultMutableTreeNode newLastInputIntNode = lastInputIntNode;
			
			String VAR_OUT = INT_LIST.get(i).getInterfaceURI();
			Interface out = interfaces.get(VAR_OUT);
			
			if(!usedInterfaces.contains(out)){
				isSource = isStillInSource(isSource, out);
				List<Interface> newUsedInterfaces1 = new ArrayList<Interface>(); 
				newUsedInterfaces1.addAll(usedInterfaces);
//				if(in_orig.getInterfaceURI().contains("in_int_Source_EQ1_CIC_01") && out.getInterfaceURI().contains("out_int_Sink_EQ4_CIC_01")){
//					System.out.println();
//				}
				
				List<Path> internalPaths = in_orig.getInternalPaths().get(out);
				int loopsForInternalPaths = 0;
				if(internalPaths == null || internalPaths.size() == 0){
					loopsForInternalPaths  = 1;
				}else{
					loopsForInternalPaths = internalPaths.size();
				}
				for (int k = 0; k < loopsForInternalPaths; k++) {
					DefaultMutableTreeNode newLastInputIntNode2 = newLastInputIntNode;
					List<Interface> newUsedInterfaces2 = new ArrayList<Interface>(); 
					newUsedInterfaces2.addAll(newUsedInterfaces1);
					if(internalPaths != null && internalPaths.size() > 0){
						if(internalPaths.size() > 1){
							System.out.println();
						}
						List<Interface> intfcList = internalPaths.get(k).getInterfaceList();
						boolean jump = false;
						for (int j = 1; j < intfcList.size()-1; j++) {
							Interface internalIntfc = intfcList.get(j);
							if(internalIntfc.isAlreadyProvisioned() && (internalIntfc.isMapsMatrixSinkInput() || internalIntfc.isMapsMatrixSourceOutput())){
								jump = true;
								break;							
							}
							newUsedInterfaces2.add(intfcList.get(j));
							DefaultMutableTreeNode internalNode = new DefaultMutableTreeNode(internalIntfc);
							newLastInputIntNode2.add(internalNode);
							newLastInputIntNode2 = internalNode;
						}
						if(jump){
							continue;
						}
					}
					
					newUsedInterfaces2.add(out);
					
					DefaultMutableTreeNode outIntNode = new DefaultMutableTreeNode(out);
					newLastInputIntNode2.add(outIntNode);
					
					if(VAR_OUT.equals(interfaceTo.getInterfaceURI())){
						Path path = new Path(outIntNode.getPath());
						path.setQtBindedInterfaces(this.bindedInterfaces);
//						int qtBinds = path.newBinds();
						if(limitExceeded(paths, path, qtShortPaths, maxPathSize, priorityOption, maxNewBindings, maxNewPossible)){
//							return;
							continue;
						}
						
						if(paths.size() >= qtShortPaths){
							paths.remove(paths.size()-1);
						}
						
						int j = getOrderedIndex(paths, path, priorityOption);
						
						paths.add(j, path);

//						return;
					}else{
						List<Interface> listInterfacesTo = algorithmPart2(isSource, out);
						for (int j = 0; j < listInterfacesTo.size(); j+=1) {
							VAR_IN = listInterfacesTo.get(j).getInterfaceURI();
							//in = new Interface(VAR_IN);
							in = interfaces.get(VAR_IN);
							isSource = isStillInSource(isSource, in);
							
							if(!newUsedInterfaces2.contains(in)){
								List<Interface> newUsedInterfaces3 = new ArrayList<Interface>(); 
								newUsedInterfaces3.addAll(newUsedInterfaces2);
								newUsedInterfaces3.add(in);
								
								DefaultMutableTreeNode possibleInIntNode = new DefaultMutableTreeNode(in);
								outIntNode.add(possibleInIntNode);
								
								findPaths(possibleInIntNode, isSource, paths, interfaceTo, newUsedInterfaces3, qtShortPaths, maxPathSize, priorityOption, maxNewBindings, maxNewPossible);
							}						
						}	
					}
				}
			}								
		}		
	}
	
//	public int getOrderedIndex(List<Path> paths, Path path, int declaredWeight, int possibleWeight, boolean fewPossibleEquip){
	public int getOrderedIndex(List<Path> paths, Path path, int priorityOption){
		int i;
		
		int valueToCompare=0;
		switch (priorityOption) {
		case 0:
			valueToCompare = path.size();
			break;
		case 1:
			valueToCompare = path.newBinds();
			break;
		case 2:
			valueToCompare = path.getQtPossible();
			break;
		}
		
//		int pathSize = declaredWeight * path.getQtDeclared() + possibleWeight * path.getQtPossible();
		for (i = 0; i < paths.size(); i++) {
//			int pathISize = declaredWeight * paths.get(i).getQtDeclared() + possibleWeight * paths.get(i).getQtPossible();
			int vetValueToCompare=0;
			switch (priorityOption) {
			case 0:
				vetValueToCompare = paths.get(i).size();
				break;
			case 1:
				vetValueToCompare = paths.get(i).newBinds();
				break;
			case 2:
				vetValueToCompare = paths.get(i).getQtPossible();
				break;
			}
			
			if(valueToCompare < vetValueToCompare) break;
			
//			if(fewPossibleEquip){
//				//int x = paths.get(i).getQtPossible();
//				if(path.getQtPossible() < paths.get(i).getQtPossible()){
//					break;
//				}else if(path.getQtPossible() == paths.get(i).getQtPossible() && pathSize < pathISize){
//					break;
//				}				
//			}else{
//				if(pathSize < pathISize){
//					break;
//				}
//			}			
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
	public Path callAlgorithmManual(Interface interfaceFrom, Interface interfaceTo) throws Exception{
		boolean isSource = true;
		String VAR_OUT = "";
		String VAR_IN = interfaceFrom.getInterfaceURI();
		bindedInterfaces.add(interfaceFrom);
		Interface out = null;
		
		Path path = new Path();
				
		do {
			//#19
			//Interface in = new Interface(VAR_IN);
			Interface in = interfaces.get(VAR_IN);
			path.addInterface(in);
			
//			if(out != null){
//				createPath(out, in);
//			}
			
			isSource = isStillInSource(isSource, in);
			List<Interface> INT_LIST = algorithmPart1(in, isSource);
			
			System.out.print("\nCurrent Path: "+path);
			int chosenId = ConsoleUtil.chooseOne(INT_LIST, "Output Interfaces", "Choose an available Output Interface (VAR_OUT): ",0, true, false);
			VAR_OUT = INT_LIST.get(chosenId).getInterfaceURI();
			out = interfaces.get(VAR_OUT);

			List<Path> internalPaths = getOrigPaths(in, out);
			if(internalPaths.size() > 1){
				int internalPathId = ConsoleUtil.chooseOne(internalPaths, "Internal Paths", "Choose an available internal path: ",0, true, false);
				List<Interface> intfcList = internalPaths.get(internalPathId).getInterfaceList();
				for (int i = 1; i < intfcList.size()-1; i++) {
					path.addInterface(intfcList.get(i), true);
				}				
			}else if(internalPaths.get(0).size() > 2){
				List<Interface> intfcList = internalPaths.get(0).getInterfaceList();
				for (int i = 1; i < intfcList.size()-1; i++) {
					path.addInterface(intfcList.get(i), true);
				}
			}
			
			//#20
			if(!VAR_OUT.equals(interfaceTo.getInterfaceURI())){
				//#21
				//Interface out = new Interface(VAR_OUT);
				out = interfaces.get(VAR_OUT);
				path.addInterface(out);
//				List<Path> x = getOrigPaths(in, out);
				
//				createPath(in, out);
				
				isSource = isStillInSource(isSource, out);
				List<Interface> listInterfacesTo = algorithmPart2(isSource, out);
				
				//#D
				System.out.print("\nCurrent Path: "+path);
				int interfaceToId = ConsoleUtil.chooseOne(listInterfacesTo, "Input Interfaces", "Choose an available Input Interface (VAR_IN): ",0, true, false);
				VAR_IN = listInterfacesTo.get(interfaceToId).getInterfaceURI();
				
				//#22
				//in = new Interface(VAR_IN);
				in = interfaces.get(VAR_IN);
				
//				bindsInterfaces(out, in, isSource);
			}
		} while (!VAR_OUT.equals(interfaceTo.getInterfaceURI()));//#20
		
//		path.addInterface(interfaceTo);
				
		return path;
	}
	
	private void createPath(Interface int1, Interface int2) throws Exception {
		String int1Ns = int1.getInterfaceURI();
		String int2Ns = int2.getInterfaceURI();
		
		FactoryUtil.createInstanceRelation(model, int1Ns, ns+"path", int2Ns, false, false, true);
	}


	public boolean isStillInSource(boolean isSourceOld, Interface intfc){
		boolean isSource = isSourceOld;
//		boolean isInterfaceSource = SPARQLQueries.isInterfaceSource(model, intfc.getInterfaceURI());
		boolean isInterfaceSource = intfc.isSource();
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
//			String mappedTF = SPARQLQueries.getMappedTFFrom(model, inputInterface.getInterfaceURI());
//			List<String> bindedTFList = SPARQLQueries.getLastBindedTFFrom(model, mappedTF, isSource);
			List<String> bindedTFList = inputInterface.getAllLastMappedTfURI();
			//System.out.println();
//			if(!bindedTFList.contains(mappedTF)){
//				bindedTFList.add(mappedTF);
//			}		
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
