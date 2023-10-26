package service;

import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dao.UsuarioDAO;

public class ServiceParent {
	static UsuarioDAO usuarioDAO = new UsuarioDAO();
	static Random random = new Random(); 

	static JSONObject parseBody(String jsonBody, ServiceLogger logger) throws Exception{
		//Parses and processes all exceptions
		try {
			JSONParser parser = new JSONParser(); //parse to JSON
	        
	    	Object parsedData = parser.parse(jsonBody);
	    	if (parsedData instanceof JSONObject) {
	    		return (JSONObject) parsedData;
	    	}	
	    	else {
	    		throw new Exception (logger.err("This is not a JSON")); 
	    	}
		}
		catch (ParseException e) {throw new Exception (logger.err("Error when trying to parse JSON")); }
        catch (Exception e) {throw new Exception (logger.err("Request body is not a proper JSON")); }
	}
}
