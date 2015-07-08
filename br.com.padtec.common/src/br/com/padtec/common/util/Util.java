package br.com.padtec.common.util;

import java.util.UUID;

public class Util {

	public static String generateUUID(){
		UUID id = UUID.randomUUID();
        
		return String.valueOf(id);
		
	}
}
