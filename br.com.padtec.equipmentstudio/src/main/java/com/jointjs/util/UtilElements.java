package com.jointjs.util;

public class UtilElements {

	public static String getImageShape(String varName, String text, String imgPath){
		String st = "	var "+varName+" = new joint.shapes.basic.Image({\n" + 
				"		position: { x: 10, y: 10 },\n" + 
				"	    size: { width: 50, height: 50 },\n" + 
				"		attrs: {\n" + 
				"			image: { width: 50, height: 50, 'xlink:href': contextPath+'"+imgPath+"' },\n" + 
				"			text: { text: '"+text+"', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }\n" + 
				"		}\n" + 
				"	});";
		return st;
	}
	
	public static String getImageShape(String varName, String text, String imgPath, int x, int y){
		String st = "	var "+varName+" = new joint.shapes.basic.Image({\n" + 
				"		position: { x: "+x+", y: "+y+" },\n" + 
				"	    size: { width: 50, height: 50 },\n" + 
				"		attrs: {\n" + 
				"			image: { width: 50, height: 50, 'xlink:href': contextPath+'"+imgPath+"' },\n" + 
				"			text: { text: '"+text+"', 'font-size': 12, display: '', stroke: '#000000', 'stroke-width': 0 }\n" + 
				"		}\n" + 
				"	});";
		return st;
	}
}
