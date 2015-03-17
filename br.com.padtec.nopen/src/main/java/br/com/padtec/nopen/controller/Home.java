package br.com.padtec.nopen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Home {
	String message = "Welcome to your 1st Maven Spring project !";

	@RequestMapping("/hello")
	public String showMessage(HttpServletRequest request) {
		System.out.println("from controller");
		return "hello";
	}

	@RequestMapping("/editor")
	public String editorRequest(HttpServletRequest request) {
		return "editor_squeleton";
	}
	
	@RequestMapping("/dashboard")
	public String editorRequest() {
		return "dashboard/dashboard";
	}
	
	@RequestMapping("/equipment-studio")
	public String equipmentStudioRequest() {
		return "equipment-studio/equipment-studio";
	}
		
	@RequestMapping("/advisor")
	public String advisorRequest() {
		return "advisor/index";
	}
	
	@RequestMapping(value = "/node_added", method = RequestMethod.GET)
	public @ResponseBody String nodeAdded(@RequestParam("id") String id, @RequestParam("stencil") String stencil) {
		System.out.println("id: "+id);
		System.out.println("stencil: "+stencil);
		return "OK";
	}
	
	@RequestMapping(value = "/printJSON", method = RequestMethod.POST)
	public @ResponseBody String printJSON(@RequestParam("json") String json) {
		System.out.println("JSON: \n"+json);
		return "OK";
	}
	
	@RequestMapping(value = "/requestSomething", method = RequestMethod.GET)
	public @ResponseBody String requestSomething() {
		String something = "var rect = new joint.shapes.basic.Rect({\r\n" + 
				"				position : {\r\n" + 
				"					x : 100,\r\n" + 
				"					y : 100\r\n" + 
				"				},\r\n" + 
				"				size : {\r\n" + 
				"					width : 70,\r\n" + 
				"					height : 30\r\n" + 
				"				},\r\n" + 
				"				attrs : {\r\n" + 
				"					text : {\r\n" + 
				"						text : 'my rectangle'\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			});\r\n" + 
				"			var rect2 = rect.clone();\r\n" + 
				"			var link = new joint.dia.Link({\r\n" + 
				"				source : {\r\n" + 
				"					id : rect.id\r\n" + 
				"				},\r\n" + 
				"				target : {\r\n" + 
				"					id : rect2.id\r\n" + 
				"				}\r\n" + 
				"			});\r\n" + 
				"			graph.addCell(rect).addCell(rect2).addCell(link);";
		
		return something;
	}
}
