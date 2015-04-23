package br.com.padtec.nopen.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.service.util.NOpenFileUtil;

@Controller
public class StudioController {

	@RequestMapping("/nt-options")
	public String ntOptionRequest(HttpServletRequest request) {		
		return "nopen-options/nt-options";
	}
	
	/**
	 * Procedure to delete Topology.
	 * @param filename
	 */	
	@RequestMapping("/deleteTopology")
	public @ResponseBody void deleteTopolopgy(@RequestParam("filename") String filename) {
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.topologyJSONFolder + filename + "/");
		File dir = new File(path);
		
		for(File file : dir.listFiles()){ 
			file.delete();
		}
		
		dir.delete();
	}
	
}
