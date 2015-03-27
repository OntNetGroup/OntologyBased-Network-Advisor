package br.com.padtec.nopen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.core.application.Initializator;
import br.com.padtec.nopen.core.queries.NOpenQueryUtil;

@Controller
public class HomeController {
		
	@RequestMapping(method = RequestMethod.GET, value="/init")
	public String index(HttpServletRequest request) throws Exception 
	{		
		String errorMsg = Initializator.run();
		if(!errorMsg.isEmpty()) { request.getSession().removeAttribute("errorMensage"); throw new Exception(errorMsg); }
		
		request.getSession().setAttribute("techs", NOpenQueryUtil.getTechnologiesNames());
		request.getSession().setAttribute("layers", NOpenQueryUtil.getLayerNames());
		
		return "welcome";
	}
	
	@RequestMapping("/home")
	public String homeRequest(HttpServletRequest request) {		
		return "welcome";
	}
	
	@RequestMapping("/info")
	public String aboutRequest(HttpServletRequest request) {		
		return "about";
	}
	
	@RequestMapping("/questions")
	public String faqRequest(HttpServletRequest request) {		
		return "faq";
	}		
	
	@RequestMapping("/options")
	public String configRequest(HttpServletRequest request) {	
		
		return "options";
	}
		
	@RequestMapping("/advisor")
	public String advisorRequest() {
		return "advisor/index";
	}
	
	//==========================================================================
			
	@RequestMapping("/dashboard")
	public String editorRequest() {
		return "dashboard/dashboard";
	}

	@RequestMapping("/hello")
	public String showMessage(HttpServletRequest request) {
		System.out.println("from controller");
		return "hello";
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
