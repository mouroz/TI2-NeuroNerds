package service;

public class ServiceLogger {
	private String path;
	public ServiceLogger(String path) {
		this.path = path;
	}
	
	public void log(Object customText) {
		System.out.println("(" + path + ") -> " + customText);
	}
	
	public void logMethodEnd(Object customText) {
		System.out.println("(" + path + ") -> FINISHED SUCCESFULLY. SENDING:\n" + customText);
	}
	public String err(String customText) {
		//possibly log error messages here
		return "(" + path + ") -> " + customText; 
	}
	
	
}
