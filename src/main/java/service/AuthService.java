package service;

import javax.servlet.MultipartConfigElement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;


public class AuthService {
	
	static UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	static JSONObject parseBody(String jsonBody, String apiPath) throws Exception{
		//Parses and processes all exceptions
		try {
			JSONParser parser = new JSONParser(); //parse to JSON
	        
	    	Object parsedData = parser.parse(jsonBody);
	    	if (parsedData instanceof JSONObject) {
	    		return (JSONObject) parsedData;
	    	}	
	    	else {
	    		throw new Exception ("This is not a JSON"); 
	    	}
		}
		catch (ParseException e) {throw new Exception (apiPath + "Error when trying to parse JSON");}
        catch (Exception e) {throw new Exception (apiPath + "Request body is not a proper JSON");}
	}

	//-------------------------------------------------------------------------
	///REQUEST ROUTES
		
	public static Object cadastraUsuario(Request req, Response res) throws Exception{
		//Receives json and sends JWT json. See auth.js to check the json format
		final String requestParam1 = "name";
    	final String requestParam2 = "password";
    	final String requestParam3 = "email";
    	final String apiPath = "(/insert)-> ";
    
		final String contentType = req.headers("Content-Type");
		System.out.println("Reading for " + apiPath + "contentType = " + contentType);
		
    	///PROCESS BODY (REQ)
		final String reqJsonBody = req.body(); //get Request body as String
		  									   //parseBody will try to parse it to a proper JSON
		JSONObject reqJson = parseBody(reqJsonBody, apiPath); //Exceptions handled by function
		
		System.out.println(apiPath + "body json = \n" + reqJson);
				
		String email = (String) reqJson.get(requestParam3);
		String senha = (String) reqJson.get(requestParam2);
		String nome = (String) reqJson.get(requestParam1);
		Usuario usuario = new Usuario(senha,email,nome);
		
		if(usuarioDAO.cadastraUsuario(usuario)) res.status(200);
		else res.status(409);
		
			
		return null;
	}
	
	public static Object auth(Request req, Response res) throws Exception{
		//Receives json and sends JWT json. See auth.js to check the json format
		final String requestParam1 = "username";
    	final String requestParam2 = "password";
    	final String apiPath = "(/auth)-> ";
    
    	//print request type
		final String contentType = req.headers("Content-Type");
		System.out.println("Reading for " + apiPath + "contentType = " + contentType);
		
    	///PROCESS BODY (REQ)
		final String reqJsonBody = req.body(); //get Request body as String
		  									   //parseBody will try to parse it to a proper JSON
		JSONObject reqJson = parseBody(reqJsonBody, apiPath); //Exceptions handled by function
		
		System.out.println(apiPath + "body json = \n" + reqJson);


		///AUTHENTICATE
		boolean auth = false;
    	String email = (String) reqJson.get(requestParam1);
    	String password = (String) reqJson.get(requestParam2);
		  	
		if (usuarioDAO.autenticaUsuario(password,email)) {
	    	System.out.println(apiPath + "got [(" +email+ "), (" +password+ ")] from request body");
	    	
	    	///CREATE RESPONSE (RES)
	    	res.type("application/json");
			
	    	 // Create the JSON response structure
	        JSONObject header = new JSONObject();
	        header.put("alg", "HS256");
	        header.put("typ", "JWT");
	
	        JSONObject payload = new JSONObject();
	        payload.put("sub", email); // Unique identifier, for now, email
	        payload.put("name", password);
	        payload.put("trilha", "adhd"); // Not sure if its best to include this on payload
	
	        JSONObject jsonResponse = new JSONObject();
	        jsonResponse.put("header", header);
	        jsonResponse.put("payload", payload);
	        jsonResponse.put("iat", null); // Represents the time the token was issued (set to null for now)
	        jsonResponse.put("signature", null); // Signature (set to null for now)
	        
	        System.out.println(apiPath + jsonResponse);
	        return jsonResponse.toJSONString(); //response must go as string
		}
		else {
			res.status(401); // HTTP status code 401 Unauthorized
			res.type("application/json");
			
			JSONObject errorResponse = new JSONObject();
		    errorResponse.put("error", "Authentication failed");
		    return errorResponse.toJSONString();
		}
	}
}
