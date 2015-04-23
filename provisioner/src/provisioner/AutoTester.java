package provisioner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;

import provisioner.business.Provisioner;
import provisioner.domain.Interface;
import provisioner.domain.Path;
import provisioner.jenaUtil.OWLUtil;
import provisioner.jenaUtil.SPARQLQueries;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.PerformanceUtil;

public class AutoTester {

	private static FileOutputStream fosExecTime;

	public static void main(String[] args) {
		Provisioner provisioner = null;
		try{
			String owlTBoxFile = "resources/owl/TBox v5.2.owl";
			//String owlTBoxFile = FileUtil.chooseFile("OWL TBox", "resources/owl/", ".owl");
			String declaredFile = "resources/declared/Possível 2.1 - 1 Layer.txt";
			//String declaredFile = FileUtil.chooseFile("declared instances", "resources/declared/", ".txt");
			String possibleFile = "resources/possible/Declarado 2.1 - Base.txt";
			//String possibleFile = FileUtil.chooseFile("possible instances", "resources/possible/", ".txt");
			
			//int declaredReplications = ConsoleUtil.getOptionFromConsole("Choose the number of layer replications", 2, Integer.MAX_VALUE);
			int declaredReplications = 2;
			int possibleReplications = 1;
			
			//int qtShortPaths = ConsoleUtil.getOptionFromConsole("Choose the number of paths", 1, Integer.MAX_VALUE);
			int qtShortPaths = 10;
			//int maxPathSize = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces in a path", 1, Integer.MAX_VALUE);
			int maxPathSize = 10;
			int declaredWeight = 1;
			int possibleWeight = 1;
			
			File execTime; 
			
			boolean fewPossibleEquip = false;
			for (int i = 0; i < declaredReplications; i++) {
				
					execTime = new File("resources/output/tester-" + (i+1) + " replications-execution time.txt");   
					if(execTime.exists()){
						execTime.delete();
					}
					fosExecTime = new FileOutputStream(execTime);
					
					Date beginDate = new Date();				
					provisioner = new Provisioner(owlTBoxFile, declaredFile, possibleFile, i+1, possibleReplications);
					String ns = provisioner.getModel().getNsPrefixURI("");
					
					OntModel model = provisioner.getModel();
					List<String> afURIs = QueryUtil.getIndividualsURI(model, ns+"Adaptation_source");
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
					String reasonerExec = "Reasoning execution: " + reasoningTimeExecPostInstances + "ms\n";
					fosExecTime.write(reasonerExec.getBytes());
					
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
					String pathsExec = "Find paths execution for " + qtShortPaths + " paths with maximum size " + maxPathSize + ": " + nShortPathsTimeExec + "ms\n";
					fosExecTime.write(pathsExec.getBytes());
					print("resources/output/tester-" + (i+1) + " replications-n paths.txt", pathsShorters, i);
					
					qtShortPaths = 0;
					maxPathSize = 0;
					beginDate = new Date();
					List<Path> allPaths = provisioner.findPaths(interfaceFrom, interfaceTo, 0, 0, declaredWeight, possibleWeight, fewPossibleEquip);
	 				long allPathsTimeExec = PerformanceUtil.getExecutionTime(beginDate );
	 				pathsExec = "Find paths execution for all paths with all sizes: " + allPathsTimeExec + "ms\n";
	 				fosExecTime.write(pathsExec.getBytes());
	 				fosExecTime.close();
	 				print("resources/output/tester-" + (i+1) + " replications-all paths.txt", allPaths, i);
	 				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			OWLUtil.saveNewOwl(provisioner.getModel(), "resources/output/", "");
		}
	}

	private static void print(String fileName, List<Path> pathsShorters, int i) throws Exception {
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		for (Path path : pathsShorters) {
			String out = (i+1) + path.toString();
			fos.write(out.getBytes());
		}
		fos.close();
	}

}
