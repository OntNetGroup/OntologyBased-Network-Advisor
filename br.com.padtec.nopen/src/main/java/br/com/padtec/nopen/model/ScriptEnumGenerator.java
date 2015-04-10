package br.com.padtec.nopen.model;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.service.util.NOpenUtilities;
import br.com.padtec.okco.core.application.OKCoUploader;

public class ScriptEnumGenerator {
	
	public static boolean contains(List<String> list, String item)
	{
		for(String str: list){ if(str.compareTo(item)==0) return true; }
		return false;		
	}
	
	public static void main(String[] args)
	{
		OKCoUploader owlRepository = new OKCoUploader();
		
		InputStream s = ScriptEnumGenerator.class.getResourceAsStream("/model/EquipStudio.owl");
				
		/** Upload */				
		NOpenUtilities.uploadTBOx(s, false, owlRepository);
		
		System.out.println(owlRepository.getBaseModelAsString());
		
		//classes
		List<String> classesNames = new ArrayList<String>();
		List<String> classesURI = QueryUtil.getClassesURI(owlRepository.getBaseModel());
		for(String class_: classesURI) { classesNames.add(class_.replace(owlRepository.getNamespace(),"")); }

		//relations
		List<String> relationsNames = new ArrayList<String>();
		List<String[]> domainRangeNames = new ArrayList<String[]>();
		
		List<String> relationsURI = QueryUtil.getObjectPropertiesURI(owlRepository.getBaseModel());
		
		for(String relURI: relationsURI) 
		{ 
			String relName = relURI.replace(owlRepository.getNamespace(),"");
			if(!contains(relationsNames,relName))
			{
				//store relation
				relationsNames.add(relName);
				
				//store domain and range
				String[] pair = new String[2];
				List<String> domainList = QueryUtil.getFirstDomainURI(owlRepository.getBaseModel(), relURI);
				List<String> rangeList = QueryUtil.getFirstRangeURI(owlRepository.getBaseModel(), relURI);
				if(domainList.size()>0) pair[0] = domainList.get(0).replace(owlRepository.getNamespace(), "");
				if(rangeList.size()>0) pair[1] = rangeList.get(0).replace(owlRepository.getNamespace(), "");;
				domainRangeNames.add(pair);
			}
		}
		
		File conceptEnumFile = NOpenFileUtil.createFile("src/main/resources/model/", "ConceptEnum.java");
		File relationEnumFile = NOpenFileUtil.createFile("src/main/resources/model/", "RelationEnum.java");
		
		String conceptEnum = generateConceptEnum(classesNames);
		String relationEnum = generateRelationEnum(relationsNames, domainRangeNames);
		
		try{
			NOpenFileUtil.writeToFile(conceptEnumFile, conceptEnum);
			NOpenFileUtil.writeToFile(relationEnumFile, relationEnum);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String generateConceptEnum(List<String> conceptNames)
	{
		String content = new String();
		
		content += "package br.com.padtec.nopen.model;\n\n";

		content += "public enum ConceptEnum {\n\n";
		
		int i=0;
		for(String name: conceptNames)
		{
			content += "\t"+name.replaceAll(" ", "_");
			content += "(\""+name+"\")";
			if(i==conceptNames.size()-1) content += ";\n\n";
			else content += ",\n";
			i++;
		}
		
		content += "\tprivate String concept;\n\n";
		
		content += "\tConceptEnum(String concept)\n";
		content += "\t{\n";
		content += "\t\tthis.concept = concept;\n";
		content += "\t}\n\n";
		
		content += "\t@Override\n";
		content += "\tpublic String toString() {\n";
		content += "\t\treturn concept();\n";
		content += "\t}\n\n";
		
		content += "\tpublic String concept() { return concept; }\n\n";  
		
		content += "\tpublic static void main (String args[])\n";
		content += "\t{\n";
		content += "\t\tfor(ConceptEnum c: ConceptEnum.values()){\n";
		content += "\t\t\tSystem.out.println(c.concept);\n";
		content += "\t\t}\n";
		content += "\t}\n";
		
		content += "}";
		
		return content;
	}
	
	public static boolean isJavaKeyword(String name)
	{
		if(name.equals("implements")) return true;
		if(name.equals("class")) return true;
		if(name.equals("static")) return true;
		if(name.equals("boolean")) return true;
		return false;
	}
	
	public static String generateRelationEnum(List<String> relationNames, List<String[]> domainRangeNames)
	{
		String content = new String();
	
		content += "package br.com.padtec.nopen.model;\n\n";

		content += "public enum RelationEnum {\n\n";
		
		int i=0;
		
		for(String name: relationNames)
		{
			//defining the enum name (customized)
			String enumName = new String(name.replaceAll("\\.","_"));
			String domainName = domainRangeNames.get(i)[0];
			if(domainName!=null) domainName = domainName.replaceAll(" ", "_").replaceAll("\\.","_");
			String rangeName = domainRangeNames.get(i)[1];
			if(rangeName!=null) rangeName = rangeName.replaceAll(" ", "_").replaceAll("\\.","_");					
			if(domainName!=null){
				if(!name.contains(domainName)) enumName += "_"+domainName;
			}
			if(rangeName!=null){
				if(!name.contains(rangeName)) enumName += "_"+rangeName;				
			}
			
			if(isJavaKeyword(name)) { enumName = enumName+"_"; }
			
			content += "\t";
			content += enumName;
			content += "(\""+name+"\")";
			if(i==relationNames.size()-1) content += ";\n\n";
			else content += ",\n";
			i++;
		}
		
		content += "\tprivate String relation;\n";
		content += "\tprivate String domain = new String();\n";
		content += "\tprivate String range = new String();\n\n";
				
		content += "\tRelationEnum(String relation)\n";
		content += "\t{\n";
		content += "\t\tthis.relation = relation;\n";
		content += "\t}\n\n";
		
		content += "\tRelationEnum(String relation, String domain, String range)\n";
		content += "\t{\n";
		content += "\t\tthis.relation = relation;\n";
		content += "\t\tthis.domain = domain;\n";
		content += "\t\tthis.range = range;\n";
		content += "\t}\n\n";
		
		content += "\t@Override\n";
		content += "\tpublic String toString() {\n";
		content += "\t\treturn relation();\n";
		content += "\t}\n\n";
		  
		content += "\tpublic String relation() { return relation; }\n\n";  
		content += "\tpublic String domain() { return domain; }\n\n";
		content += "\tpublic String range() { return range; }\n\n";
		
		content += "\tpublic static void main (String args[])\n";
		content += "\t{\n";
		content += "\t\tfor(RelationEnum r: RelationEnum.values()){\n";
		content += "\t\t\tSystem.out.println(r.relation);\n";
		content += "\t\t}\n";
		content += "\t}\n";
		
		content += "}";
		
		return content;
	}
}
