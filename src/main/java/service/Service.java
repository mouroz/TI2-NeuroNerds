package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.MultipartConfigElement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dao.UsuarioDAO;
import model.Pergunta;
import model.Usuario;
import spark.Request;
import spark.Response;

public class Service {
	static UsuarioDAO usuarioDAO = new UsuarioDAO();
	
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
	public static Object getExercicio(Request req, Response res) throws Exception{
		//Receives json and sends JWT json. See auth.js to check the json format
		final String requestParam1 = "user";
    	final String apiPath = "(/exercicios)-> ";
		///PROCESS URL
		String name = req.queryParams(requestParam1);
        System.out.println(apiPath + "got [(" +name+ ")] from request body");

		///GET EXERCICIOS DAO --------------------------------------------------
		String[] alternativas = {"a1", "a2", "a3", "a4", "a5"};
    	String text = "enunciado";
    	String title = "Titulo";
    	byte correct = 1;
    	
    	///CREATE RESPONSE (RES) --------------------------------------------------
    	res.type("application/json");
		
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("title", title);
        jsonResponse.put("text", text);
        jsonResponse.put("type", 0); // Represents the time the type (only alternatives for now)
        jsonResponse.put("correct", correct); // Represents the correct alternative [1-5]
        
        JSONArray alternativesArray = new JSONArray();
        for (String alternative : alternativas) {
            alternativesArray.add(alternative);
        }
        jsonResponse.put("alternatives", alternativesArray); // Signature (set to null for now)
        
        System.out.println(apiPath + jsonResponse);
        return jsonResponse.toJSONString(); //response must go as string
	}

	@SuppressWarnings("unchecked")
	public static Object getForumHomepage(Request req, Response res) throws Exception{
		//Receives json and sends JWT json. See auth.js to check the json format
    	final String apiPath = "(/forum-homepage)-> ";	
    	final int postsMaxLen = 5;
    	
		///(GET VALUES FROM DAO) --------------------------------------------------
    	Pergunta[] posts = new Pergunta[2]; //Seria ideal ter uma classe que Posts e Comentarios que armazena 
    											//tanto Pergunta quanto Usuario juntos
		LocalDate currentDate = LocalDate.now();
		for (int i = 0; i < posts.length; i++) {
			posts[i] = new Pergunta(0, "titulo", "este aqui e o texto", currentDate, 0);
		}
		int postsLen = (posts.length > postsMaxLen) ? postsMaxLen : posts.length;
		
		Usuario[] users = new Usuario[2];
		for (int i = 0; i < users.length; i++) {
			users[i] = new Usuario("12432", "email@yahoo.com", "ricardo");
		}
				
    	///CREATE RESPONSE (RES) --------------------------------------------------
		
		
    	/*
    	 * The response for this request is a List of JSONS, each containing a header and a content JSON.
    	 * Formatter was also used to convert from DateTime to String
    	 */
		res.type("application/json");
		
        JSONObject jsonResponse = new JSONObject();
        
        JSONArray dataArray = new JSONArray();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        
        for (int i = 0; i < postsLen; i++) {
        	JSONObject postJson = new JSONObject(); //Contains user and content JSONObject
        	
        	//userJson
        	JSONObject userJson = new JSONObject();
        	
        	userJson.put("name", users[i].getUsername());
        	
            String dateString = posts[i].getData_postagem().format(formatter);
        	userJson.put("date", dateString); //no post dates stored
        	
        	//contentJson
        	JSONObject contentJson = new JSONObject();
        	contentJson.put("title", posts[i].getTitulo());
        	contentJson.put("text", posts[i].getConteudo());
        	contentJson.put("likes", 5); //nao possui
        	contentJson.put("comments", 7);
        	contentJson.put("id", posts[i].getId_pergunta());
        	
        	String[] exampleTags = {"importante", "adhd"};
        	JSONArray tags = new JSONArray();
        	for (String tag : exampleTags) {
        		tags.add(tag);
        	}
        	
        	contentJson.put("tags", tags);
        	
        	//finish the json
        	postJson.put("user", userJson);
        	postJson.put("content", contentJson);
        	dataArray.add(postJson);
        }
        
        
        jsonResponse.put("data", dataArray); // Signature (set to null for now)
        
        System.out.println(apiPath + jsonResponse);
        return jsonResponse.toJSONString(); //response must go as string
	}
	
	@SuppressWarnings("unchecked")
	public static Object getForumPost(Request req, Response res) throws Exception{
		//Receives json and sends JWT json. See auth.js to check the json format
    	final String apiPath = "(/forum-post)-> ";	
    	final int commentsMaxLen = 5;
    	
		///(GET VALUES FROM DAO) --------------------------------------------------
    	int id = 0; //Use esse id pra saber qual pergunta pegar
    	System.out.println(apiPath + "URL id value: " + req.queryParams("id"));
    	try {
    		id = Integer.parseInt(req.queryParams("id"));
    	} catch (NumberFormatException e) {throw new Exception ("Failure to parse to int from url id");}
    	
    	LocalDate currentDate = LocalDate.now();
    	//For this test ill already write the values for post directly inside the JSON Building
    	//But you also need to gather the information about the owner of the post
    	
    	Pergunta[] comments = new Pergunta[2]; //Comentarios vai ter o usuario Dentro ou nao? Ele seria o mesmo da pergunta?
    											  //Neste teste eu estou os tratando separado, mas seria ideal ter uma classe unificada
		for (int i = 0; i < comments.length; i++) {
			comments[i] = new Pergunta(0, "titulo", "este aqui e o texto", currentDate, 0);
		}
		int commentsLen = (comments.length > commentsMaxLen) ? commentsMaxLen : comments.length;
		
		Usuario[] commentUsers = new Usuario[2];
		for (int i = 0; i < commentUsers.length; i++) {
			commentUsers[i] = new Usuario("12432", "email@yahoo.com", "ricardo");
		}
				
    	///CREATE RESPONSE (RES) --------------------------------------------------
    	/*
    	 * The response for this request is a JSON with user (name, date) and content (title, text, likes, comments, tags, id)
    	 * and a List of JSONS, each containing a user (name, date) and a shortened content (text, likes, id)
    	 * Formatter was also used to convert from DateTime to String
    	 */
		
		///MANUALLY CREATING POST
		res.type("application/json");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
        JSONObject jsonResponse = new JSONObject();
        JSONObject pPost = new JSONObject();
        
        JSONObject pUser = new JSONObject();
        pUser.put("name", "Jose");
        pUser.put("date", currentDate.format(formatter));
        
        JSONObject pContent = new JSONObject();
        pContent.put("title", "titulo");
        pContent.put("text", "Esse e o texto teste do post");
        pContent.put("likes", "32");
        pContent.put("comments", "23");
        pContent.put("tags", new JSONArray() {{
        	add("Important");
        	add("Adhd");
        }});
        pContent.put("id", "0");
        
        pPost.put("user", pUser);
        pPost.put("content", pContent);
        jsonResponse.put("post", pPost); //finished establishing first hierarchy
        
        
        ///CREATING THE COMMENTS
        JSONArray commentJsonArr = new JSONArray();      
        for (int i = 0; i < commentsLen; i++) {
        	JSONObject comment = new JSONObject();
        	
        	//User
        	JSONObject userJson = new JSONObject();
        	userJson.put("name", commentUsers[i].getUsername());
        	userJson.put("date", ( comments[i].getData_postagem() ).format(formatter)); 
        	
        	//Content
        	JSONObject contentJson = new JSONObject(); 
        	contentJson.put("text", comments[i].getConteudo());
        	contentJson.put("likes", 5); //nao possui
    		contentJson.put("id", comments[i].getId_pergunta());
        	
        	//finish the json
        	comment.put("user", userJson);
        	comment.put("content", contentJson);
        	commentJsonArr.add(comment);
        }
        
        
        jsonResponse.put("comment", commentJsonArr); // Signature (set to null for now)
        
        System.out.println(apiPath + jsonResponse);
        return jsonResponse.toJSONString(); //response must go as string
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//NÃO ESTA SENDO EXCECUTADO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
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
	
	//NÃO ESTA SENDO EXCECUTADO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ^^^^
}


