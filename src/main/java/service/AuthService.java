package service;

import javax.servlet.MultipartConfigElement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

//to display warning messages on log
import java.util.logging.Logger;
import java.util.logging.Level;


public class AuthService extends ServiceParent{
	
		static UsuarioDAO usuariodao = new UsuarioDAO();
	
	@SuppressWarnings("unchecked")
	public static Object cadastraUsuario(Request req, Response res) throws Exception{
    	final String path = "cadastro-user";
    	final ServiceLogger logger = new ServiceLogger(path);
		logger.log("ContentType found: " + req.headers("Content-Type")); 
		
		///GET REQ BODY
		final String reqJsonBody = req.body(); 	  									  
		JSONObject reqJson = parseBody(reqJsonBody, logger);
		logger.log("Request body JSON = " + reqJson);
		
		String email = (String) reqJson.get("email");
		String senha = (String) reqJson.get("password");
		String nome = (String) reqJson.get("name");
	
		///TRY TO PUT ON DATABASE
		Usuario usuario = new Usuario(senha,email,nome);
		logger.log(usuario.toString());
		JSONObject responseJson = new JSONObject();
		
		if(usuarioDAO.cadastraUsuario(usuario)) {
			res.status(200);
			responseJson.put("payload", "sucess");
			logger.log("Sucesfully registered email " + email);
		}
		else {
			res.status(401);
			responseJson.put("payload", "failure");
			logger.log("Couldnt register new account");
		}
		
    	return responseJson;
	}
	
	@SuppressWarnings("unchecked")
	public static Object auth(Request req, Response res) throws Exception{
		//Receives json and sends JWT json. See auth.js to check the json format
		final String path = "auth";
    	final ServiceLogger logger = new ServiceLogger(path);
		logger.log("ContentType found: " + req.headers("Content-Type")); 

    	///GET REQ BODY
		final String reqJsonBody = req.body();
		JSONObject reqJson = parseBody(reqJsonBody, logger);
		logger.log("body json =" + reqJson);

    	String email = (String) reqJson.get("username");
    	String password = (String) reqJson.get("password");
    	logger.log("got [email=(" +email+ "), password=(" +password+ ")] from request body");
    	
    	///TRY TO AUTHENTICATE WITH DATABASE REQUEST
    	res.type("application/json");
    	
		if (usuarioDAO.autenticaUsuario(password,email)) {
			JSONObject responseJson = new JSONObject();
				JSONObject header = new JSONObject();
			        header.put("alg", "HS256");
			        header.put("typ", "JWT");
		        JSONObject payload = new JSONObject();
		        
		        	
		        
		        
			        payload.put("sub", email); // Unique identifier, for now, email
			        payload.put("name", password);
			        payload.put("trilha", "adhd"); // Not sure if its best to include this on payload
			        
			//Finish building responseJson       
	        responseJson.put("header", header);
	        responseJson.put("payload", payload);
	        responseJson.put("iat", null); // Represents the time the token was issued (set to null for now)
	        responseJson.put("signature", null); // Signature (set to null for now)

	        res.status(200);
	        logger.logMethodEnd(responseJson);
	        return responseJson.toJSONString(); //response must go as string
		}
		else {
			JSONObject errorResponse = new JSONObject();
		    errorResponse.put("error", "Authentication failed");
		    
		    res.status(401); // HTTP status code 401 Unauthorized
		    logger.logMethodEnd(errorResponse);    
		    return errorResponse.toJSONString();
		}
	}
}