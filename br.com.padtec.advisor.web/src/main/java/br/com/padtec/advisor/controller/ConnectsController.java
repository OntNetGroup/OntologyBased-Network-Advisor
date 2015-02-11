package br.com.padtec.advisor.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.advisor.application.AdvisorService;
import br.com.padtec.advisor.application.GeneralConnects;
import br.com.padtec.advisor.application.dto.DtoResultAjax;
import br.com.padtec.okco.core.application.OKCoUploader;

@Controller
public class ConnectsController {

	@RequestMapping(method = RequestMethod.GET, value="/connect_equip_binds")
	public @ResponseBody String connect_equip_binds(@RequestParam("equip_source") String equip_source,@RequestParam("interface_source") String interface_source,@RequestParam("equip_target") String equip_target,@RequestParam("interface_target") String interface_target , HttpServletRequest request) 
	{
		DtoResultAjax dto = ProvisioningController.provisioningBinds(interface_target, interface_source, request, true, null);
		return dto.ok+"";
	}

	@RequestMapping(method = RequestMethod.GET, value="/get_input_interfaces_from")
	public @ResponseBody String get_input_interfaces_from(@RequestParam("equip") String equip, @RequestParam("interf") String interf, HttpServletRequest request) 
	{
		ArrayList<String> list = ProvisioningController.getCandidateInterfacesForConnection(interf);
		String hashEquipIntIn = "";
		for(String line : list) hashEquipIntIn += line;		
		return hashEquipIntIn;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/do_connects")
	public @ResponseBody String do_connects(@RequestParam("rp_src") String rp_src,@RequestParam("rp_trg") String rp_trg, @RequestParam("rp_type") String rp_type, HttpServletRequest request) 
	{
		try {
			GeneralConnects.connects(rp_src, rp_trg, rp_type);
			
			/*
			 * Verify the new possible connects
			 * */			
			String hashAllowed = new String();
			ArrayList<String> equipsWithRps = new ArrayList<String>();
			ArrayList<String> connectsBetweenEqsAndRps = new ArrayList<String>();
			ArrayList<String> connectsBetweenRps = new ArrayList<String>();
			ArrayList<String[]> possibleConnections;
			
			ProvisioningController.getEquipmentsWithRPs(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace(), equipsWithRps, connectsBetweenEqsAndRps, connectsBetweenRps);

			for (String connections : connectsBetweenEqsAndRps) {
				String src = connections.split("#")[0];
				String trg = connections.split("#")[1];

				possibleConnections = AdvisorService.getPossibleConnectsTuples(src);
				if(!possibleConnections.isEmpty() && !hashAllowed.contains(src))
					hashAllowed += src+"#";

				possibleConnections = AdvisorService.getPossibleConnectsTuples(trg);
				if(!possibleConnections.isEmpty() && !hashAllowed.contains(trg))
					hashAllowed += trg+"#";
			}
			
			for (String equipWithRP : equipsWithRps) {
				String equip = equipWithRP.split("#")[0];
				String rp = equipWithRP.split("#")[1];

				if(!equip.isEmpty()){
					if(!AdvisorService.getPossibleConnectsTuples(rp).isEmpty() && !hashAllowed.contains(rp)){
						hashAllowed += equip+"#";
					}
				}
			}
			
			return hashAllowed;
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/get_possible_connections")
	public @ResponseBody String get_possible_connections(@RequestParam("rp") String rp, HttpServletRequest request) {
		/*
		 * [0] = RP's name
		 * [1] = Connection type
		 * */
		ArrayList<String[]> list = AdvisorService.getPossibleConnectsTuples(rp);

		String hashEquipIntIn = "";

		for(String[] line : list){
			hashEquipIntIn += line[0].substring(line[0].indexOf("#")+1)+"#"+line[1]+";";
		}

		return hashEquipIntIn;
	}
}
