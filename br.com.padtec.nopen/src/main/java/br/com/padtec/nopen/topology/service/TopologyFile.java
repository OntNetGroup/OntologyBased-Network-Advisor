package br.com.padtec.nopen.topology.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class TopologyFile {

	
	public void saveTopology(String filename, String graph, HttpServletRequest request) {
		
		ServletContext sc = request.getSession().getServletContext();
		
		
		try {
			
			URL resourceContent = Thread.currentThread().getContextClassLoader().getResource("model/");
						
			File f = new File(resourceContent.toURI()+"/teste.json");
			System.out.println(f.getAbsolutePath());
			
			PrintStream printStream = new PrintStream(f);
			printStream.print(graph);
			printStream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public HashSet<String> getAllTopologies(HttpServletRequest request){
		
		ServletContext sc = request.getSession().getServletContext();
		String path =  "/backend/topology/" ;
		String fname =  sc.getRealPath(path);
		
		File folder = new File(fname);
		File[] listOfFiles = folder.listFiles();
		HashSet<String> topologies = new HashSet<String>();
		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	    	  
	    	  String filename = listOfFiles[i].getName();
	    	  String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

	    	  if(extension.equals("json")){
	    		  String name = filename.substring(0, filename.lastIndexOf("."));
	    		  topologies.add(name);
	    	  }
	    	  
	      } else if (listOfFiles[i].isDirectory()) {

	      }
	    }
		
		return topologies;
	}
	
	public String openTopology(String filename, HttpServletRequest request){
		
		ServletContext sc = request.getSession().getServletContext();
		String path =  "/backend/topology/" ;
		String fname =  sc.getRealPath(path) + "/" + filename + ".json" ;

		String graph = "";
		
		BufferedReader br = null;
		 
		try {
 
			String line;
 
			br = new BufferedReader(new FileReader(fname));
 
			while ((line = br.readLine()) != null) {
				graph = graph + line;
			}
			
			br.close();
			
			return graph;
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
