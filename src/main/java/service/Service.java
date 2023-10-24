package service;

import javax.servlet.MultipartConfigElement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spark.Request;
import spark.Response;

public class Service {
	 /*
	 * For the Service.java, all exceptions are thrown out to the Spark.exception route
	 * As such, to simplify and make debugging easier and more flexible, each catch will instead just
	 * throw a new exception with a custom message that also includes the apiPath (route). This drastically
	 * reduces the amount of lines for each catch and makes the code easier to read 
	 */

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
	
	@SuppressWarnings("unchecked")
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
		boolean auth = true;
		String trilha = ""; //get trilha from db
		String user = ""; //might be a better pk than email
		
		if (auth) {
	    	String email = (String) reqJson.get(requestParam1);; 
	    	String password = (String) reqJson.get(requestParam2);;
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
	        payload.put("trilha", trilha); // Not sure if its best to include this on payload
	
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
		
	@SuppressWarnings("unchecked") //JSONObject.put() receives wrong warning
	//multipart/form-data only sends <input> values
	static Object formsPostHandler(Request req, Response res) throws Exception{
		//const Json Atributes
		final String reqField1 = "value1"; //name of forms field
    	final String reqField2 = "value2";
    	final String apiPath = "(/formsButton) -> ";
    	
    	//print request type
		final String contentType = req.headers("Content-Type");
		System.out.println("Reading for " + apiPath + "contentType = " + contentType);
		
		//-----------------------
    	///PROCESS BODY (REQ)
		req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
		
		//Will return null if not params found
        String name = req.queryParams(reqField1);
        String email = req.queryParams(reqField2);
        System.out.println(apiPath + "got (" +name+ "), (" +email+ ") from request body");
  
        //------------------------
    	///CREATE RESPONSE (RES)
        
        return null;
 
	}
}


