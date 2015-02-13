package br.com.padtec.advisor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.advisor.application.GeneralBinds;
import br.com.padtec.advisor.application.dto.DtoResultAjax;
import br.com.padtec.advisor.application.util.ApplicationQueryUtil;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

@Controller
public class BindsController {
	@RequestMapping(method = RequestMethod.GET, value="/connect_equip_binds")
	public @ResponseBody String connect_equip_binds(@RequestParam("equip_source") String equip_source,@RequestParam("interface_source") String interface_source,@RequestParam("equip_target") String equip_target,@RequestParam("interface_target") String interface_target , HttpServletRequest request) 
	{
		DtoResultAjax dto = GeneralBinds.provisioningBinds(interface_target, interface_source, request, true, null);
		return dto.ok+"";
	}

	@RequestMapping(method = RequestMethod.GET, value="/get_input_interfaces_from")
	public @ResponseBody String get_input_interfaces_from(@RequestParam("equip") String equip, @RequestParam("interf") String interf, HttpServletRequest request) 
	{
		ArrayList<String> list = GeneralBinds.getCandidateInterfacesForConnection(interf);
		String hashEquipIntIn = "";
		for(String line : list) hashEquipIntIn += line;		
		return hashEquipIntIn;
	}
	
	@RequestMapping(value = "/autoBinds", method = RequestMethod.POST)
	public String autoBinds(HttpServletRequest request){
		//pego todas as instancias de interface de output nao conectadas
		ArrayList<DtoInstance> outputInterfaces = new ArrayList<DtoInstance>(); 
		List<DtoInstance> allInstances = DtoQueryUtil.getIndividuals(OKCoUploader.getInferredModel(), false, false, false);
		for (DtoInstance instance : allInstances) {
			for (String className : instance.ListClasses) {
				if(className.equalsIgnoreCase(OKCoUploader.getNamespace()+"Output_Interface")){
					List<DtoInstanceRelation> outputInterfaceRelations = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(), instance.ns+instance.name);
					boolean alreadyConnected = false;
					for (DtoInstanceRelation outputInterfaceRelation : outputInterfaceRelations) {
						if(outputInterfaceRelation.Property.equalsIgnoreCase(OKCoUploader.getNamespace()+"interface_binds")){
							if((instance.ns+instance.name).equals(outputInterfaceRelation.Target)){
								alreadyConnected  = true;
								break;
							}
						}
					}
					
					if(!alreadyConnected){
						outputInterfaces.add(instance);
						break;
					}					
				}
			}
		}
		
		HashMap<String, ArrayList<String>> uniqueCandidatesForBinds = new HashMap<String, ArrayList<String>>();
		
		for (DtoInstance outputInterface : outputInterfaces) {
			ArrayList<String> candidatesForConnection = GeneralBinds.getCandidateInterfacesForConnection(outputInterface.ns+outputInterface.name);
			int noCandidates = 0;
			String inputCandidateName = "";
			for (String candidate : candidatesForConnection) {
				if(candidate.contains("true")){
					noCandidates++;
					inputCandidateName = candidate.split("#")[1];
				}
				
				if(noCandidates > 1){
					break;
				}
			}	
			
			if(uniqueCandidatesForBinds.containsKey(inputCandidateName)){
				uniqueCandidatesForBinds.get(inputCandidateName).add(outputInterface.name);
			}else if(noCandidates == 1){
				ArrayList<String> outs = new ArrayList<String>();
				outs.add(outputInterface.name);
				uniqueCandidatesForBinds.put(inputCandidateName, outs);
			}
		}
		
		ArrayList<String> listInstancesCreated = new ArrayList<String>();
		int bindsMade = 0;
		String returnMessage = "Interfaces binded:<br>";
		for(Entry<String, ArrayList<String>> candidates : uniqueCandidatesForBinds.entrySet()) {
			String inputInterface = candidates.getKey();
			ArrayList<String> outs = candidates.getValue();
			
			if(outs.size() == 1){
				GeneralBinds.provisioningBinds(outs.get(0), inputInterface, request, false, listInstancesCreated);
				bindsMade++;
				returnMessage += outs.get(0);
				returnMessage += " -> ";
				returnMessage += inputInterface;
				returnMessage += "<br>";
			}
			
		}
		
		if(bindsMade>0){
//			try {
////				for (String instanceUri : listInstancesCreated) {
////					HomeController.UpdateAddIntanceInLists(instanceUri);	
////				}
//				//HomeController.UpdateLists();
//			} catch (InconsistentOntologyException e) {
//				e.printStackTrace();
//			} catch (OKCoExceptionInstanceFormat e) {
//				e.printStackTrace();
//			}
		}else{
			returnMessage = "No interfaces binded.";
		}
		
		request.getSession().setAttribute("loadOk", returnMessage);
		
		return VisualizationController.bindsV(request);
	}
	
	
}
