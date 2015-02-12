
package br.com.padtec.advisor.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;

import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

@Controller
public class ProvisioningController {
	
	public static ArrayList<DtoInstance> getInstancesFromClass(ConceptEnum inputInterface){
		ArrayList<String> classNamesWithoutNameSpace = new ArrayList<String>();
		classNamesWithoutNameSpace.add(String.valueOf(inputInterface));
		
		ArrayList<DtoInstance> instances = getInstancesFromClasses(classNamesWithoutNameSpace);
		
		return instances;
	}
	
	public static ArrayList<DtoInstance> getInstancesFromClasses(ArrayList<String> classNamesWithoutNameSpace){
		ArrayList<DtoInstance> instances = new ArrayList<DtoInstance>();
		List<DtoInstance> allInstances = DtoQueryUtil.getIndividuals(OKCoUploader.getInferredModel(), false, false, false);
		for (DtoInstance instance : allInstances) {
			for (String classNameWithoutNameSpace : classNamesWithoutNameSpace) {
				Boolean foundInstance = false;
				if(instance.ListClasses.contains(OKCoUploader.getNamespace()+classNameWithoutNameSpace)){
					if(!instances.contains(instance)){
						instances.add(instance);
					}
					foundInstance  = true;
					break;
				}
				if(foundInstance){
					break;
				}
			}			
//			for (String className : instance.ListClasses) {
//				className = className.replace(OKCoUploader.getNamespace(), "");
//				if(className.equalsIgnoreCase(classNameWithoutNameSpace)){
//					
//				}
//			}
		}
		return instances;
	}
	
	
}
