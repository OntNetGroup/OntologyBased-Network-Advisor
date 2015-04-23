package provisioner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;

import provisioner.business.Provisioner;
import provisioner.domain.Interface;
import provisioner.domain.Path;
import provisioner.jenaUtil.OWLUtil;
import provisioner.jenaUtil.SPARQLQueries;
import provisioner.util.ConsoleUtil;
import provisioner.util.FileUtil;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.PerformanceUtil;

import com.hp.hpl.jena.ontology.OntModel;

public class AutoTester {

	private static FileOutputStream fosExecTime;

	public static void main(String[] args) {
		FileUtil.createDirs();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoTester();
			}
		});	
	}

	private static void autoTester(){
		try{
			
			File outTestDir = new File("resources/output/tester/");
			if(outTestDir.exists()){
				File[] filesList = outTestDir.listFiles();
				for (File file : filesList) {
					file.delete();
				}
			}else{
				outTestDir.mkdirs();
			}
			
			//String owlTBoxFile = "resources/owl/TBox v5.2.owl";
			String owlTBoxFile = FileUtil.chooseFile("OWL TBox", "resources/owl/", ".owl");
			//String declaredFile = "resources/declared/Possível 2.1 - 1 Layer.txt";
			String declaredFile = FileUtil.chooseFile("declared instances", "resources/declared/", ".txt");
			//String possibleFile = "resources/possible/Declarado 2.1 - Base.txt";
			String possibleFile = FileUtil.chooseFile("possible instances", "resources/possible/", ".txt");
			
			int declaredReplications = ConsoleUtil.getOptionFromConsole("Choose the number of layer replications", 2, Integer.MAX_VALUE);
			//int declaredReplications = 5;
			int possibleReplications = 1;
			
			int qtShortPaths = ConsoleUtil.getOptionFromConsole("Choose the number of paths (enter 0 to show all)", 0, Integer.MAX_VALUE);
			//int qtShortPaths = 10;
			int maxPathSize = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces in a path (enter 0 for no limit)", 0, Integer.MAX_VALUE);
			//int maxPathSize = 10;
			int declaredWeight = 1;
			int possibleWeight = 1;
			
			File execTime; 
			
			File executionTimes = new File("resources/output/tester/executionTimes.txt");
			FileOutputStream fosExecutionTimes = new FileOutputStream(executionTimes);
			String execTimes = "Include instances and reasoning execution\tFinding paths\n";
			fosExecutionTimes.write(execTimes.getBytes());
			
			boolean fewPossibleEquip = false;
			for (int i = 0; i < declaredReplications; i++) {
				
					execTime = new File("resources/output/tester/" + (i+1) + " replications-execution time.txt");   
					if(execTime.exists()){
						execTime.delete();
					}
					fosExecTime = new FileOutputStream(execTime);
					
					Date beginDate = new Date();				
					Provisioner provisioner = new Provisioner(owlTBoxFile, declaredFile, possibleFile, i+1, possibleReplications);
					String ns = provisioner.getModel().getNsPrefixURI("");
					
					OntModel model = provisioner.getModel();
					List<String> afURIs = QueryUtil.getIndividualsURI(model, ns+"Adaptation_Function");
					for (String afURI : afURIs) {
						List<String> serverLayerURIS = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, afURI, ns+"adapts_to", ns+"Layer_Network");
						for (String serverLayerURI : serverLayerURIS) {
							List<String> clientLayerURIs = QueryUtil.getIndividualsURIAtObjectPropertyDomain(model, serverLayerURI, ns+"client_of", ns+"Layer_Network");
							for (String clientLayerURI : clientLayerURIs) {
								FactoryUtil.createInstanceRelation(model, afURI, ns+"adapts_from", clientLayerURI, false, false, false);
							}
						}
					}
					
					long reasoningTimeExecPostInstances = provisioner.getReasoningTimeExecPostInstances();
					//String reasonerExec = "Reasoning execution: " + reasoningTimeExecPostInstances + "ms\n";
					String aux1 = reasoningTimeExecPostInstances + "\t";
					fosExecutionTimes.write(aux1.getBytes());
					String reasonerExec = PerformanceUtil.getExecutionMessage("Reasoning", reasoningTimeExecPostInstances) + "\n";
					fosExecTime.write(reasonerExec.getBytes());
					
//					OWLUtil.saveNewOwl(model, "resources/output/tester/", "");
					
					String layer = ns+"layer";
					if(i>0){
						layer+=i;
					}
					List<String> inInterfacesSrc = SPARQLQueries.getInterfacesFromLayer(provisioner.getModel(), ns+"Input_Interface", "Source", layer);
					String inInterfaceSrcURI = inInterfacesSrc.get(0);
					List<String> outInterfacesSk = SPARQLQueries.getInterfacesFromLayer(provisioner.getModel(), ns+"Output_Interface", "Sink", layer);
					String outInterfaceSrcURI = outInterfacesSk.get(0);
					
					Interface interfaceFrom = provisioner.getInterface(inInterfaceSrcURI);
					Interface interfaceTo = provisioner.getInterface(outInterfaceSrcURI);
					
					beginDate = new Date();
					List<Path> pathsShorters = provisioner.findPaths(interfaceFrom, interfaceTo, qtShortPaths, maxPathSize, declaredWeight, possibleWeight, fewPossibleEquip);
					long nShortPathsTimeExec = PerformanceUtil.getExecutionTime(beginDate);
					//String pathsExec = "Find paths execution for " + qtShortPaths + " paths with maximum size " + maxPathSize + ": " + nShortPathsTimeExec + "ms\n";
					String aux2 = nShortPathsTimeExec + "\n";
					fosExecutionTimes.write(aux2.getBytes());
					String pathsExec = PerformanceUtil.printExecutionTime("Finding paths for " + qtShortPaths + " paths with maximum size " + maxPathSize, beginDate) + "\n";
					fosExecTime.write(pathsExec.getBytes());
					print("resources/output/tester/" + (i+1) + " replications-n paths.txt", pathsShorters);
					
//					qtShortPaths = 0;
//					maxPathSize = 0;
//					beginDate = new Date();
//					List<Path> allPaths = provisioner.findPaths(interfaceFrom, interfaceTo, 0, 0, declaredWeight, possibleWeight, fewPossibleEquip);
//	 				//long allPathsTimeExec = PerformanceUtil.getExecutionTime(beginDate );
//	 				pathsExec = PerformanceUtil.printExecutionTime("Findinding paths for all paths with all sizes", beginDate) + "\n";
//	 				//pathsExec = "Find paths execution for all paths with all sizes: " + allPathsTimeExec + "ms\n";
//	 				fosExecTime.write(pathsExec.getBytes());
//	 				fosExecTime.close();
//	 				print("resources/output/tester/" + (i+1) + " replications-all paths.txt", allPaths);
	 				
	 				OWLUtil.saveNewOwl(provisioner.getModel(), "resources/output/tester/", (i+1) + " ");
			}
			fosExecutionTimes.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	private static void print(String fileName, List<Path> pathsShorters) throws Exception {
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		for (int j = 0; j < pathsShorters.size(); j++) {
			String out = (j+1) + " - " + pathsShorters.get(j).toString();
			fos.write(out.getBytes());
		}
		fos.close();
	}

}
