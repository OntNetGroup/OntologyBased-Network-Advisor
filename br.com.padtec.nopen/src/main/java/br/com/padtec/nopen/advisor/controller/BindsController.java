package br.com.padtec.nopen.advisor.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.advisor.core.application.GeneralBinds;
import br.com.padtec.advisor.core.dto.DtoResultAjax;

@Controller
public class BindsController {
	@RequestMapping(method = RequestMethod.GET, value="/connect_equip_binds")
	public @ResponseBody String connect_equip_binds(@RequestParam("equip_source") String equip_source,@RequestParam("interface_source") String interface_source,@RequestParam("equip_target") String equip_target,@RequestParam("interface_target") String interface_target , HttpServletRequest request) 
	{
		/**===========================================================
		 * General Binds: provisioning binds
		 * =========================================================== */
		DtoResultAjax dto = GeneralBinds.provisioningBinds(interface_target, interface_source, true, null);
		
		return dto.ok+"";
	}

	@RequestMapping(method = RequestMethod.GET, value="/get_input_interfaces_from")
	public @ResponseBody String get_input_interfaces_from(@RequestParam("equip") String equip, @RequestParam("interf") String interf, HttpServletRequest request) 
	{
		/**===========================================================
		 * General Binds: get candidate interfaces for connection
		 * =========================================================== */
		List<String> list = GeneralBinds.getCandidateInterfacesForConnection(interf);
		
		String hashEquipIntIn = "";
		for(String line : list) hashEquipIntIn += line;		
		return hashEquipIntIn;
	}
	
	@RequestMapping(value = "/autoBinds", method = RequestMethod.POST)
	public String autoBinds(HttpServletRequest request)
	{
		/**===========================================================
		 * General Binds: auto binds
		 * =========================================================== */		
		String returnMessage = GeneralBinds.autoBinds();
		
		request.getSession().setAttribute("loadOk", returnMessage);		
		
		/**===========================================================
		 * General Binds: Visualization
		 * =========================================================== */
		return VisualizationController.bindsV(request);
	}
	
	
}
