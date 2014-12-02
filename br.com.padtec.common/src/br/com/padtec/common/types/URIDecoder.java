package br.com.padtec.common.types;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class URIDecoder {
	
	/** Decode URI */
	static public String decodeURI(String uri)
	{
		try {
			uri = URLDecoder.decode(uri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return uri;
	}
}
