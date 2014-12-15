package br.com.padtec.okco.service.utility;

import br.com.padtec.common.dto.simple.SimpleDtoClass;
import br.com.padtec.common.dto.simple.SimpleDtoInstance;
import br.com.padtec.common.dto.simple.SimpleDtoRelation;
import br.com.padtec.okco.service.feature.OKCoFeatures;
import br.com.padtec.okco.service.feature.OKCoResult;


public class ProgramUtility {

	public static void main(String[] args) {

		String inputFileName = "C://Users//fabio_000//Desktop//OntologiasOWL//assassinato.owl";	

		OKCoFeatures o = new OKCoFeatures();
		OKCoResult dto = o.listFileIncompleteness(inputFileName, "PELLET");
		
		for (SimpleDtoInstance i : dto.ListInstances) {
			System.out.println("----------------- " + i.Name + " -----------------");
			System.out.println("- " + i.Namespace);
			System.out.println("- Classes Belong: ");
			for (String string : i.ListClassesBelong) {
				System.out.println("    - " + string);
			}
			System.out.println("- Same instances: ");
			for (String string : i.ListSameInstances) {
				System.out.println("    - " + string);
			}
			System.out.println("- Dife instances: ");
			for (String string : i.ListDiferentInstances) {
				System.out.println("    - " + string);
			}
			System.out.println("- Classes definitions: ");
			for (SimpleDtoClass def : i.ListImcompletenessClassDefinitions) {
				System.out.println("   - " + def.TopClass);
				for (String string : def.SubClassesToClassify) {
					System.out.println("      - " + string);
				}
			}
			System.out.println("- Relation definitions: ");
			for (SimpleDtoRelation def : i.ListImcompletenessRelationDefinitions) {
				System.out.println("   - " + def.SourceClass + " -> " + def.Relation + " (" + def.KindProperty + "-" + def.RelationType + ") " + def.TargetClass + " (" + def.Cardinality + ")" );
				
			}
		}
		
	}

}
