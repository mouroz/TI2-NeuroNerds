package service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import javax.servlet.MultipartConfigElement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dao.UsuarioDAO;
import dao.PerguntaDAO;
import dao.QuestaoDAO;
import dao.RespostaDAO;
import model.Pergunta;
import model.Resposta;
import model.Alternativa;
import model.Resposta;
import model.Usuario;
import spark.Request;
import spark.Response;

public class Service extends ServiceParent{
	
	static PerguntaDAO perguntaDAO = new PerguntaDAO();
	static QuestaoDAO questaoDAO = new QuestaoDAO();
	static RespostaDAO respostaDAO = new RespostaDAO();
	///GETS
	//@SuppressWarnings("unchecked")
	public static Object getExercicio(Request req, Response res) throws Exception{
		String path = "/exercicios";
		final ServiceLogger logger = new ServiceLogger(path);
    	
		///GET DATA FROM REQUEST URL
		String name = req.queryParams("user");
        logger.log("got [(" +name+ ")] from request body");

		///GET EXERCICIOS DAO --------------------------------------------------
        
        List<Alternativa> alternativas = questaoDAO.getAlternativas(1);
        //String[] alternativas = {"res1","res2","res3","res4","res5"};
        
    	///CREATE RESPONSE (RES) -----------------------------------------------
    	res.type("application/json");
    	 JSONArray alternativesArray = new JSONArray();
         
    	 int correct = -1;
    	 for(int i = 0; i < alternativas.size(); i++) {
    		 Alternativa alternativa = alternativas.get(i);
    		 alternativesArray.add(alternativa.getConteudo());
    		 if (alternativa.isCorreta()) {
    			 correct = i + 1;
    		 }
    	 }
         
    	 
        JSONObject jsonResponse = new JSONObject();
	        jsonResponse.put("title", null);
	        jsonResponse.put("text", null);
	        jsonResponse.put("type", 0); // Represents the time the type (only alternatives for now)
	        jsonResponse.put("correct", correct); // Represents the correct alternative [1-5]
	        jsonResponse.put("alternatives", alternativesArray); // Signature (set to null for now)
        
        logger.logMethodEnd(jsonResponse.toJSONString());
        return jsonResponse.toJSONString(); //response must go as string
	}

	@SuppressWarnings("unchecked")
		public static Object getForumHomepage(Request req, Response res) throws Exception{
			final String path = "/forum/homepage";
	    	final ServiceLogger logger = new ServiceLogger(path);
	    	final int postsMaxLen = 5;
	    	logger.log("Started");
			///GET VALUES FROM DATABASE
	    	
	    	List<Pergunta> perguntas = perguntaDAO.buscaUltimasCincoPerguntas();
	    	
			int postsLen = perguntas.size();
			postsLen = (postsLen > postsMaxLen) ? postsMaxLen : postsLen;
			
			if (postsLen == 0) {
				res.status(400);
				logger.log("Couldnt find any values to add to the forum. Exiting method");
				return "failure";
			}
			
	    	///CREATE RESPONSE (RES) --------------------------------------------------	
			
			res.type("application/json");
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
	        			userJson.put("name", perguntas.get(i).getNome_usuario());            
	        			userJson.put("date", (perguntas.get(i).getData_postagem()).toString()); 
		        	JSONObject contentJson = new JSONObject();
			        	contentJson.put("title", perguntas.get(i).getTitulo());
			        	contentJson.put("text", perguntas.get(i).getConteudo());
			        	contentJson.put("likes", random.nextInt(30)); //nao possui
			        	contentJson.put("comments", random.nextInt(30));
			        	contentJson.put("tags", tagsArray);
			        	contentJson.put("id", perguntas.get(i).getId_pergunta());
			        	logger.log(perguntas.get(i).getId_pergunta());
	        	//finish the json
	        	postJson.put("user", userJson);
	        	postJson.put("content", contentJson);
	        	jsonArrayData.add(postJson);
	        }
	        
	        ///FINISH responseJson
	        responseJson.put("data", jsonArrayData);
	        
	        res.type("application/json");
	        res.status(200);
	        
	        res.body(responseJson.toJSONString());
	        logger.logMethodEnd(responseJson);
	        return res.body();
		}
	
	@SuppressWarnings("unchecked")
	public static Object getForumPagePost(Request req, Response res) throws Exception {
	    final String path = "/forum/page/load-post";    
	    final ServiceLogger logger = new ServiceLogger(path);
	    final int maxCommentsNum = 5;
	    
	    ///GET ID FROM URL
	    int id = 0; 
	    logger.log("URL id value: " + req.queryParams("id"));
	    try {
	        id = Integer.parseInt(req.queryParams("id"));
	    } catch (NumberFormatException e) {
	        throw new Exception(logger.err("Failure to parse to int from url id"));
	    }
	    
	    ///GET VALUES FROM DATABASE 
	    
	    Pergunta pergunta = perguntaDAO.getPergunta(id);
	    List<Resposta> respostas = perguntaDAO.getRespostas(id);

	    
	    int cLen = respostas.size(); // Isso irá pegar o tamanho correto da lista, mesmo que ela esteja vazia
	    cLen = (cLen > maxCommentsNum) ? maxCommentsNum : cLen;
	    
	    ///CREATE RESPONSE (RES) --------------------------------------------------
	    res.type("application/json");
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    
	    //Fake default values - values not on database yet
	    String[] tags = {"adhd"}; 
	    
	    //Structuring 1 layer JSONArrays
	    JSONArray postContentTags = new JSONArray(); 
	    for (String s : tags) { //add each tag
	        postContentTags.add(s);
	    }
	    
	    //-----JSON Overall Structure-----------------
	    
	    JSONObject responseJson = new JSONObject();
	        JSONObject jsonPost = new JSONObject();
	            JSONObject jsonPostUser = new JSONObject();
	                jsonPostUser.put("name", pergunta.getNome_usuario());
	                jsonPostUser.put("date", (pergunta.getData_postagem()).toString());
	            JSONObject jsonPostContent = new JSONObject(); 
	                jsonPostContent.put("title", pergunta.getTitulo());
	                jsonPostContent.put("text", pergunta.getConteudo());
	                jsonPostContent.put("likes", random.nextInt(30));
	                jsonPostContent.put("comments", random.nextInt(30));
	                jsonPostContent.put("tags", postContentTags);
	                jsonPostContent.put("id", pergunta.getId_pergunta());
	            
	            jsonPost.put("user", jsonPostUser);    
	            jsonPost.put("content", jsonPostContent);
	        
	        JSONArray jsonArrayComments = new JSONArray();
	        //Array of jsonComment from: (JSON ARRAY BUILD 01)
	        
	    //----------------------------------------
	    
        if (respostas != null) { //If there are comments to be added
        	
        	///(JSON ARRAY BUILD 01)
    	    for (int i = 0; i < cLen; i++) {
    	        JSONObject jsonComment = new JSONObject();
    	            JSONObject jsonCommentUser = new JSONObject();
    	                jsonCommentUser.put("name", respostas.get(i).getNome_usuario());
    	                jsonCommentUser.put("date", (respostas.get(i).getData_postagem()).toString()); 
    	            JSONObject jsonCommentContent = new JSONObject(); 
    	                jsonCommentContent.put("text", respostas.get(i).getConteudo());
    	                jsonCommentContent.put("likes", random.nextInt(30));
    	                //jsonCommentContent.put("id", respostas.get(i).getId_resposta());
    	        
    	        jsonComment.put("user", jsonCommentUser);
    	        jsonComment.put("content", jsonCommentContent); // Correção feita aqui
    	        jsonArrayComments.add(jsonComment);
    	    }
	   
	    }
	    
	    ///FINISH responseJson
	    responseJson.put("post", jsonPost);
	    responseJson.put("comments", jsonArrayComments); // Modificado de "comment" para "comments"
	    
	    logger.logMethodEnd(responseJson);
	    return responseJson.toJSONString();
	}

	
	@SuppressWarnings("unchecked")
	public static Object getProfileDetails(Request req, Response res) throws Exception{
		final String path = "/forum/page/load-post";	
    	final ServiceLogger logger = new ServiceLogger(path);
    	
    	//GET USUARIO ID
    	int id;
    	try {
    		id = Integer.parseInt(req.queryParams("id"));
    	}catch(NumberFormatException e) {
    		throw new Exception (logger.err("Failure to parse to int from url id"));
    	}
    	
    	//GET USUARIO THROUGH ID ON DATABASE
    	
    	//RETURN JSON
    	res.type("application/json");
    	JSONObject returnJson = new JSONObject();
    		returnJson.put("username", null);
    		returnJson.put("name", null);
    		returnJson.put("email" , null);
    		
    	res.status(200);
    	return returnJson;
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
		logger.log(reqJson);

		String dateString = (String) reqJson.get("time");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = dateFormat.parse(dateString);
        java.sql.Date date = new java.sql.Date(parsedDate.getTime());
		
		String content = (String) reqJson.get("content");
		String email = (String) reqJson.get("sub");
		String questionId = (String) reqJson.get("id");
		
		int id = Integer.parseInt(questionId);
		
		logger.log(String.format("Got values [content=(%s), email=(%s), question_id=(%s)]",
				content, email, questionId));
		
		///PUT ON DATABASE WITH DAO (!MISSING!)
		
		respostaDAO.inserirResposta(content, email, id, date);
		
		
		boolean sucess = false;
		if (sucess) {
			logger.log("Sucessfully put " + questionId + " on database");
			res.status(200);
		} else {
			logger.log("Sucessfully put on database");
			res.status(200);
		}
		
		logger.logMethodEnd("sucess");
		return "sucess";
	}
	
}



