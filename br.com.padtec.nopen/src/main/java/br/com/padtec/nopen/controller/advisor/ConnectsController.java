package br.com.padtec.nopen.controller.advisor;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.advisor.core.application.AdvisorService;
import br.com.padtec.advisor.core.application.GeneralConnects;

@Controller
public class ConnectsController {

	@RequestMapping(value = "/autoConnects", method = RequestMethod.POST)
	public String autoConnects(HttpServletRequest request)
	{		
		/**===========================================================
		 * General Connects: automatically connects
		 * =========================================================== */
		 ArrayList<String[]> listInstancesCreated = GeneralConnects.autoConnect();
		
		String returnMessage =  new String();
		if(listInstancesCreated.size()>0) returnMessage = "Reference Points connected:<br>";
		else returnMessage = "No reference points connected.";		
		request.getSession().setAttribute("loadOk", returnMessage);
		
		/**===========================================================
		 * General Connects: Visualization
		 * =========================================================== */
		return VisualizationController.connects(request);
			
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/do_connects")
	public @ResponseBody String do_connects(@RequestParam("rp_src") String rp_src,@RequestParam("rp_trg") String rp_trg, @RequestParam("rp_type") String rp_type, HttpServletRequest request) 
	{
			
		/**===========================================================
		 * General Connects: connects
		 * =========================================================== */
		GeneralConnects.connects(rp_src, rp_trg, rp_type);
				
		/**===========================================================
		 * General Connects: verify possible connects
		 * =========================================================== */
		return GeneralConnects.getPossibleConnects();			
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/get_possible_connections")
	public @ResponseBody String get_possible_connections(@RequestParam("rp") String rp, HttpServletRequest request)
	{
		/**===========================================================
		 * General Connects: returns possible connects tuples
		 * =========================================================== */
		ArrayList<String[]> list = AdvisorService.getPossibleConnectsTuples(rp); 

		/** [0] = RP's name, [1] = Connection type */
		String hashEquipIntIn = new String();
		for(String[] line : list) hashEquipIntIn += line[0].substring(line[0].indexOf("#")+1)+"#"+line[1]+";";
		
		return hashEquipIntIn;
	}	
}
