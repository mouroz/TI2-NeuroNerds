package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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

public class Service extends ServiceParent{
	
	///GETS
	@SuppressWarnings("unchecked")
	public static Object getExercicio(Request req, Response res) throws Exception{
		String path = "/exercicios";
		final ServiceLogger logger = new ServiceLogger(path);
    	
		///GET DATA FROM REQUEST URL
		String name = req.queryParams("user");
        logger.log("got [(" +name+ ")] from request body");

		///GET EXERCICIOS DAO --------------------------------------------------
		String[] alternativas = {"a1", "a2", "a3", "a4", "a5"};
    	String text = "enunciado";
    	String title = "Titulo";
    	byte correct = 1;
    	
    	///CREATE RESPONSE (RES) --------------------------------------------------
    	res.type("application/json");
    	 JSONArray alternativesArray = new JSONArray();
         for (String alternative : alternativas) {
             alternativesArray.add(alternative);
         }
         
        JSONObject jsonResponse = new JSONObject();
	        jsonResponse.put("title", title);
	        jsonResponse.put("text", text);
	        jsonResponse.put("type", 0); // Represents the time the type (only alternatives for now)
	        jsonResponse.put("correct", correct); // Represents the correct alternative [1-5]
	        jsonResponse.put("alternatives", alternativesArray); // Signature (set to null for now)
        
        logger.logMethodEnd(jsonResponse);
        return jsonResponse.toJSONString(); //response must go as string
	}

	@SuppressWarnings("unchecked")
	public static Object getForumHomepage(Request req, Response res) throws Exception{
		final String path = "/forum/homepage";
    	final ServiceLogger logger = new ServiceLogger(path);
    	final int postsMaxLen = 5;
    	
		///GET VALUES FROM DATABASE
    	
    	
		int postsLen = 5;
		postsLen = (postsLen > postsMaxLen) ? postsMaxLen : postsLen;
		
    	///CREATE RESPONSE (RES) --------------------------------------------------	
		res.type("application/json");
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String[] defaultTags = {"adhd"};
		
		///-------------Response JSON Builder------------------
		
		JSONObject responseJson = new JSONObject();
        	JSONArray jsonArrayData = new JSONArray();
        	//Contains multiple postJson. Inside (JSON ARRAY BUILDER 01)
        
        ///----------------------------------------------------
        	
        ///(JSON ARRAY BUILDER 01)
        for (int i = 0; i < postsLen; i++) {
        	//Structuring inner JSONArrays
        	JSONArray tagsArray = new JSONArray();
        	for (String s : defaultTags) {
        		tagsArray.add(s);
        	}
        		
        	JSONObject postJson = new JSONObject(); //Contains user and content JSONObject
        		JSONObject userJson = new JSONObject();
        			userJson.put("name", null);            
        			userJson.put("date", null); 
	        	JSONObject contentJson = new JSONObject();
		        	contentJson.put("title", null);
		        	contentJson.put("text", null);
		        	contentJson.put("likes", random.nextInt(30)); //nao possui
		        	contentJson.put("comments", random.nextInt(30));
		        	contentJson.put("tags", tagsArray);
		        	contentJson.put("id", null);
        	
        	//finish the json
        	postJson.put("user", userJson);
        	postJson.put("content", contentJson);
        	jsonArrayData.add(postJson);
        }
        
        ///FINISH responseJson
        responseJson.put("data", jsonArrayData);
        
        logger.logMethodEnd(responseJson);
        return responseJson.toJSONString(); //response must go as string
	}
	
	@SuppressWarnings("unchecked")
	public static Object getForumPagePost(Request req, Response res) throws Exception{
    	final String path = "/forum/page/load-post";	
    	final ServiceLogger logger = new ServiceLogger(path);
    	final int maxCommentsNum = 5;
    	
		///GET ID FROM URL
    	int id = 0; 
    	logger.log("URL id value: " + req.queryParams("id"));
    	try {
    		id = Integer.parseInt(req.queryParams("id"));
    	} catch (NumberFormatException e) {
    		throw new Exception (logger.err("Failure to parse to int from url id"));
    	}
    	
    	///GET VALUES FROM DATABASE (!MISSING!)
    	
    	
    	
    	int cLen = 5; //amount of comments
    	cLen = (cLen > maxCommentsNum) ? maxCommentsNum : cLen;
    	
    	///CREATE RESPONSE (RES) --------------------------------------------------
    	res.type("application/json");
    	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		//Fake default values - values not on database yet
		String[] tags = {"adhd"}; 
		
		//Structuring 1 layer JSONArrays
		JSONArray postContentTags = new JSONArray(); 
		for (String s : tags) { //add each tag
			postContentTags.add(tags);
		}
		
		//-----JSON Overall Structure-----------------
		
		JSONObject responseJson = new JSONObject();
			JSONObject jsonPost = new JSONObject();
				JSONObject jsonPostUser = new JSONObject();
					jsonPostUser.put("name", null);
					jsonPostUser.put("date", null);
				JSONObject jsonPostContent = new JSONObject(); 
					jsonPostContent.put("title", null);
					jsonPostContent.put("text", null);
					jsonPostContent.put("likes", random.nextInt(30));
					jsonPostContent.put("comments", random.nextInt(30));
					jsonPostContent.put("tags", postContentTags);
					jsonPostContent.put("id", null);
				
				jsonPost.put("user", jsonPostContent);	
				jsonPost.put("content", jsonPostContent);
			
			JSONArray jsonArrayComments = new JSONArray();
			//Array of jsonComment from: (JSON ARRAY BUILD 01)
			
		//----------------------------------------
		
			
		///(JSON ARRAY BUILD 01)
        for (int i = 0; i < cLen; i++) {
        	JSONObject jsonComment = new JSONObject();
	        	JSONObject jsonCommentUser = new JSONObject();
		        	jsonCommentUser.put("name", null);
		        	jsonCommentUser.put("date", null); 
	        	JSONObject jsonCommentContent = new JSONObject(); 
		        	jsonCommentContent.put("text", null);
		        	jsonCommentContent.put("likes", random.nextInt(30));
		        	jsonCommentContent.put("id", null);
        	
        	jsonComment.put("user", jsonCommentUser);
        	jsonComment.put("content", jsonCommentUser);
        	jsonArrayComments.add(jsonComment);
        }
        
        ///FINISH responseJson
        responseJson.put("post", jsonPost);
        responseJson.put("comment", jsonArrayComments); 
        
        logger.logMethodEnd(responseJson);
        return responseJson.toJSONString();
	}
	
	
	//----------------------------------------------------------------------------------------------
	///PUTS
	
	public static Object putForumPageComment(Request req, Response res) throws Exception{
		final String path = "/forum/page/comment";
    	final ServiceLogger logger = new ServiceLogger(path);
		logger.log("ContentType found: " + req.headers("Content-Type")); 
		
    	//GET REQ BODY
		final String reqJsonBody = req.body();
		JSONObject reqJson = parseBody(reqJsonBody, logger);
		
		String username = (String) reqJson.get("username");
		String content = (String) reqJson.get("content");
		
		///PUT ON DATABASE WITH DAO (!MISSING!)
		boolean sucess = false;
		if (sucess) {
			logger.log("Sucessfully put " + username + " on database");
			res.status(200);
		} else {
			logger.log("Sucessfully put on database");
			res.status(200);
		}
		
		logger.logMethodEnd(null);
		return null;
	}
	
}



