package br.com.padtec.common.dto;

public class DtoResult {
	
	private boolean succeed = true;
	private String message = new String();
	
	public DtoResult(boolean succeed, String msg)
	{
		this.succeed = succeed;
		this.message = msg;
	}
	
	public DtoResult() {}	
	public void setIsSucceed(boolean value) { this.succeed = value; }
	public boolean isSucceed() { return this.succeed; }
	public void setMessage(String msg) { this.message = msg; }	
	public String getMessage()  { return this.message; }
}
