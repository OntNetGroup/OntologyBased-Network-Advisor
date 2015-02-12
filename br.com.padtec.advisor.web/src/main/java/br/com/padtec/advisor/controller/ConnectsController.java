package br.com.padtec.advisor.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.advisor.application.AdvisorService;
import br.com.padtec.advisor.application.GeneralConnects;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.util.ApplicationQueryUtil;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.rdf.model.InfModel;

@Controller
public class ConnectsController {

	@RequestMapping(value = "/autoConnects", method = RequestMethod.POST)
	public String autoConnects(HttpServletRequest request){
		return "";
		
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
			
			getEquipmentsWithRPs(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace(), equipsWithRps, connectsBetweenEqsAndRps, connectsBetweenRps);

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
	
	public static void getEquipmentsWithRPs(InfModel infModel, String NS, ArrayList<String> equipsWithRps, ArrayList<String> connectsBetweenEqsAndRps, ArrayList<String> connectsBetweenRps){
		if(equipsWithRps == null){
			equipsWithRps = new ArrayList<String>();
		}
		if(connectsBetweenEqsAndRps == null){
			connectsBetweenEqsAndRps = new ArrayList<String>();
		}
		if(connectsBetweenRps == null){
			connectsBetweenRps = new ArrayList<String>();
		}
		List<DtoInstance> rpInstances = DtoQueryUtil.getIndividualsFromClass(OKCoUploader.getInferredModel(),ConceptEnum.REFERENCE_POINT.toString());
		
		for (DtoInstance rp : rpInstances) {
			List<DtoInstanceRelation> rpRelations = ApplicationQueryUtil.GetInstanceAllRelations(infModel, rp.ns+rp.name);
			String bindingNs = "";
			for (DtoInstanceRelation rel : rpRelations) {
				String propertyName = rel.Property.replace(NS, "");
				if(propertyName.equals("INV.binding_is_represented_by")){
					bindingNs = rel.Target;			
				}else if(propertyName.equals("has_forwarding")){
					String cnct = "";
					cnct += rp.name;
					cnct += "#";
					cnct += rel.Target.replace(NS, "");
					connectsBetweenRps.add(cnct);
				}
			}
			
			ArrayList<String> equips = new ArrayList<String>();
			if(!bindingNs.equals("")){
				equips = getEquipmentFromBinding(infModel, NS, bindingNs);
			}
			
			String equipmentWithRp = "";
			if(equips.size() == 1){
				equipmentWithRp += equips.get(0).replace(NS, "");
			}else if(equips.size() >= 2){
				for (String eq : equips) {
					String cnct = "";
					cnct += eq.replace(NS, "");
					cnct += "#";
					cnct += rp.name;
					
					String cnctInv = "";
					cnctInv += rp.name;
					cnctInv += "#";
					cnctInv += eq.replace(NS, "");
					
					if(!connectsBetweenEqsAndRps.contains(cnct) && !connectsBetweenEqsAndRps.contains(cnctInv)){
						connectsBetweenEqsAndRps.add(cnct);
					}
				}								
			}
			equipmentWithRp += "#";
			equipmentWithRp += rp.name;
			
			if(!equipsWithRps.contains(equipmentWithRp)){
				equipsWithRps.add(equipmentWithRp);
			}
			
		}
		
	}
	
	public static ArrayList<String> getEquipmentFromBinding(InfModel infModel, String NS, String bindingName){
		bindingName = bindingName.replace(NS, "");
		List<DtoInstanceRelation> bindingRelations = ApplicationQueryUtil.GetInstanceAllRelations(infModel, NS+bindingName);
		
		String bindedPort1Ns="";
		String bindedPort2Ns="";
		for (DtoInstanceRelation rel : bindingRelations) {
			String propertyName = rel.Property.replace(NS, "");
			if(propertyName.equals("is_binding")){
				if(bindedPort1Ns.equals("")){
					bindedPort1Ns = rel.Target;
				}else{
					bindedPort2Ns = rel.Target;
					break;
				}
			}
		}
		ArrayList<String> equips = new ArrayList<String>();
		if(!bindedPort1Ns.equals("")){
			//String equipmentNs = getEquipmentFromPort(infModel, NS, bindedPort1Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort1Ns));
			//if(!equipmentNs.equals("")){
				//equips.add(equipmentNs);
			//}			
			ArrayList<String> equipsNs = getEquipmentFromPort(infModel, NS, bindedPort1Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort1Ns));
			equips.addAll(equipsNs);
		}
		if(!bindedPort2Ns.equals("")){
			//String equipmentNs = getEquipmentFromPort(infModel, NS, bindedPort2Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort2Ns));
			//if(!equips.contains(equipmentNs)){
			//if(!equipmentNs.equals("") && !equips.contains(equipmentNs)){
			//	equips.add(equipmentNs);
			//}
			ArrayList<String> equipsNs = getEquipmentFromPort(infModel, NS, bindedPort2Ns, searchEquipmentFromPortToTop(infModel, NS, bindedPort2Ns));
			for (String eqNs : equipsNs) {
				if(!equips.contains(eqNs)){
					equips.add(eqNs);
				}
			}
		}
		return equips;
	}
	
	public static ArrayList<String> getEquipmentFromPort(InfModel infModel, String NS, String bindedPortNs, Boolean searchToTop){
		ArrayList<String> ret = new ArrayList<String>();
		bindedPortNs = bindedPortNs.replace(NS, "");
		
		List<DtoInstanceRelation> portRelations = ApplicationQueryUtil.GetInstanceAllRelations(infModel, NS+bindedPortNs);
		String outIntNs = "";
		String inIntNs = "";
		String tfNs = "";
		for (DtoInstanceRelation portRel : portRelations) {
			String portRelName = portRel.Property.replace(NS, "");
			if(portRelName.equals("INV.maps_output")){
				outIntNs = portRel.Target;
			}else if(portRelName.equals("INV.maps_input")){
				inIntNs = portRel.Target;
			}else if(portRelName.equals("INV.componentOf")){
				tfNs = portRel.Target;
			}
		}
		
		if(!tfNs.equals("") && outIntNs.equals("") && inIntNs.equals("")){
			tfNs = tfNs.replace(NS, "");
			List<String> tiposPm=QueryUtil.getClassesURI(infModel,NS+tfNs);
			if(tiposPm.contains(NS+"Physical_Media")){
				ret.add(tfNs);
				return ret;
				//return tfNs;
			}
			
			ArrayList<String> nextPorts = new ArrayList<String>(); 
			List<DtoInstanceRelation> tfRelations = ApplicationQueryUtil.GetInstanceAllRelations(infModel, NS+tfNs);
			String eqNs = "";
			for (DtoInstanceRelation tfRel : tfRelations) {
				String tfRelRelName = tfRel.Property.replace(NS, "");
				if(tfRelRelName.equals("INV.componentOf")){
					eqNs = tfRel.Target;
					eqNs = eqNs.replace(NS, "");
					List<String> tiposEq=QueryUtil.getClassesURI(infModel,NS+eqNs);
					if(tiposEq.contains(NS+"Equipment")){
						ret.add(eqNs);
						return ret;
						//return eqNs;
					}
				}else if(tfRelRelName.equals("componentOf")){
					if(!tfRel.Target.equals(NS+bindedPortNs)){
						List<String> tiposTf=QueryUtil.getClassesURI(infModel,tfRel.Target);
						if(tiposTf.contains(NS+"Input") || tiposTf.contains(NS+"Output")){
							nextPorts.add(tfRel.Target);
						}
					}
				}
				
			}
			
			ArrayList<String> nextRps = getNextRpsFromTf(infModel, NS, bindedPortNs, nextPorts, searchToTop);
			ret.addAll(nextRps);
			System.out.println();
			
		}else if(!outIntNs.equals("")){
			ret.add(getEquipmentFromInterface(infModel, NS, outIntNs));
			return ret;
			//return getEquipmentFromInterface(infModel, NS, outIntNs);
		}else if(!inIntNs.equals("")){
			ret.add(getEquipmentFromInterface(infModel, NS, inIntNs));
			return ret;
			//return getEquipmentFromInterface(infModel, NS, inIntNs);
		}
		
		return ret;
	}
	
	public static Boolean searchEquipmentFromPortToTop(InfModel infModel, String NS, String portNs){
		portNs = portNs.replace(NS, "");
		List<String> tiposPort=QueryUtil.getClassesURI(infModel,NS+portNs);
		if(tiposPort.contains(NS+"Output")){
			return true;
		}
		return false;
	}
	
	public static String getEquipmentFromInterface(InfModel infModel, String NS, String interfaceNs){
		interfaceNs = interfaceNs.replace(NS, "");
		List<DtoInstanceRelation> portRelations = ApplicationQueryUtil.GetInstanceAllRelations(infModel, NS+interfaceNs);
		
		for (DtoInstanceRelation intRel : portRelations) {
			String intRelName = intRel.Property.replace(NS, "");
			if(intRelName.equals("INV.componentOf")){
				return intRel.Target;
			}
		}
		
		return "";
	}
	
	public static String getRPFromBinding(InfModel infModel, String NS, String bindingNs){
		bindingNs = bindingNs.replace(NS, "");
		List<DtoInstanceRelation> bindingRelations = ApplicationQueryUtil.GetInstanceAllRelations(infModel, NS+bindingNs);
		
		for (DtoInstanceRelation bindingRel : bindingRelations) {
			String intRelName = bindingRel.Property.replace(NS, "");
			if(intRelName.equals("binding_is_represented_by")){
				return bindingRel.Target;
			}
		}
		
		return "";
	}
	
	public static ArrayList<String> getNextRpsFromTf(InfModel infModel, String NS, String actualPort, ArrayList<String> nextPorts, Boolean searchToTop){
		ArrayList<String> nextRps = new ArrayList<String>();
		for (String portNs : nextPorts) {
			portNs = portNs.replace(NS, "");
			
			List<String> nextPortClasses = QueryUtil.getClassesURI(infModel,NS+portNs);
			List<String> actualPortClasses = QueryUtil.getClassesURI(infModel,NS+actualPort);
			
			if((nextPortClasses.contains(NS+"Output") && actualPortClasses.contains(NS+"Input")) || (nextPortClasses.contains(NS+"Input") && actualPortClasses.contains(NS+"Output"))){
				List<DtoInstanceRelation> portRelations = ApplicationQueryUtil.GetInstanceAllRelations(infModel, NS+portNs);
				
				for (DtoInstanceRelation portRel : portRelations) {
					String portRelName = portRel.Property.replace(NS, "");
					if(portRelName.equals("INV.is_binding")){
						List<DtoInstanceRelation> bindingRelations =ApplicationQueryUtil.GetInstanceAllRelations(infModel, portRel.Target);
						for (DtoInstanceRelation bindingRel : bindingRelations) {
							String bindingRelName = bindingRel.Property.replace(NS, "");
							if(bindingRelName.equals("binding_is_represented_by")){
								nextRps.add(bindingRel.Target);
							}
						}					
					}
				}	
			}
			
					
		}
		
		
		return nextRps;
	}
}
