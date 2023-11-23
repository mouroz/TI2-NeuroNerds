package service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
import model.Questao;
import model.Resposta;
import model.Alternativa;
import model.Resposta;
import model.Usuario;
import spark.Request;
import spark.Response;

public class Service extends ServiceParent{
	
	
	@SuppressWarnings("unchecked")
	/* Returns a very specific json array used for exercicio.js to load the exercices
	 * 
	 * A json array is used to allow for queueing of exercices instead of having to a fetch for each one
	 * Refer to exercicio.js to see the jsonArray structure
	 */
	public static Object getExercicio(Request req, Response res) throws Exception {
		String neuro = req.queryParams("neuro");
		int neuroNum = 0;
		
        switch(neuro) {
        	case "outro":
        		neuroNum = 1;
        		break;
        	case "Discalculia":
        		neuroNum = 2;
        		break;
        	case "TDAH":
        		neuroNum = 3;
        		break;
        	default:
        		break;
        }
        System.out.println(neuroNum);
        

        res.type("application/json");
        
        //Get todas as questoes de certa neurodiv
        List<Questao> questoes = QuestaoDAO.getQuestoesPorNeurodivergencia(neuroNum);
        JSONArray jsonArray = getExercicioResponse(questoes);

        
        return jsonArray.toJSONString(); //response must go as string
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray getExercicioResponse(List<Questao> questoes) {
		JSONArray jsonArray = new JSONArray();

    	for (int i = 0; i < questoes.size(); i++) {
    	    JSONObject json = new JSONObject();
    	    
    	    List<Alternativa> alternativas = QuestaoDAO.getAlternativas(questoes.get(i).getId());
    	    int indiceCorreto = QuestaoDAO.getAlternativaCorreta(alternativas);
    	    
    	    json.put("text", questoes.get(i).getEnunciado());
    	    json.put("type", questoes.get(i).getNeuro_div()); 
    	    json.put("correct", indiceCorreto); 
    	    
    	    JSONArray jsonAlternatives = new JSONArray();
    	    for (Alternativa alt : alternativas) {
    	        JSONObject jsonAlt = new JSONObject();
    	        jsonAlt.put("conteudo", alt.getConteudo());
    	        jsonAlternatives.add(jsonAlt);
    	    }
    	    json.put("alternatives", jsonAlternatives);
    	    
    	    jsonArray.add(json);
    	}  
    	
    	return jsonArray;
	}

	
	
	
	
	@SuppressWarnings("unchecked")
	/* Returns a very specific JSONArray containing multiple post preview from
	 * the forum explore section
	 * 
	 * Contains a limiter on the amount of posts sent.
	 * Refer to forum-explore.js to see the JSONArray structure
	 * 
	 * Tags has not been implemented on the database yet, and as such default values are used
	 */
	public static Object getForumHomepage(Request req, Response res) throws Exception{
    	final int postsMaxLen = 5;
    	
    	List<Pergunta> perguntas = PerguntaDAO.getMostRecentList(5);
		int postsLen = (perguntas.size() > postsMaxLen) ? postsMaxLen : perguntas.size();
		
		if (postsLen == 0) {
			res.status(400);
			System.err.println("Couldnt find any values. Exiting");
			return "failure";
		}
		
		res.type("application/json");
		String[] defaultTags = {"adhd"};
		
		
		//Build response
		JSONArray jsonArray = new JSONArray();
		
        for (int i = 0; i < postsLen; i++) {
        	JSONArray tagsArray = new JSONArray();
        	for (String s : defaultTags) {
        		tagsArray.add(s);
        	}
   
        	JSONObject json = new JSONObject(); //Contains user and content JSONObject
        		JSONObject userJson = new JSONObject();
        			//userJson.put("name", perguntas.get(i).getNome_usuario());  -> create method list user from list questao          
        			userJson.put("date", (perguntas.get(i).getData_postagem()).toString()); 
	        	JSONObject contentJson = new JSONObject();
		        	contentJson.put("title", perguntas.get(i).getTitulo());
		        	contentJson.put("text", perguntas.get(i).getConteudo());
		        	contentJson.put("likes", random.nextInt(30)); //nao possui
		        	contentJson.put("comments", random.nextInt(30));
		        	contentJson.put("tags", tagsArray);
		        	contentJson.put("id", perguntas.get(i).getId_pergunta());
        	//finish the json
        	json.put("user", userJson);
        	json.put("content", contentJson);
        	jsonArray.add(json);
        }

		res.status(200);
        return jsonArray.toJSONString();
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	/* Returns a very specific JSON, containg the main post data and a JSONArray of comments
	 * 
	 * Contains a limiter on the amount of comments sent
	 * Refer to forum-post.js to see the JSON structure
	 * 
	 * Tags has not been implemented on the database yet, and as such default values are used
	 */
	public static Object getForumPagePost(Request req, Response res) throws Exception{
    	final int maxCommentsNum = 5;
    	
		//Get id from url sent -> need to review the formats later
    	int id = 0; 
    	try {
    		id = Integer.parseInt(req.queryParams("id"));
    	} catch (NumberFormatException e) {
    		throw new Exception ("Failure to parse to int from url id");
    	}
    	
    	Pergunta pergunta = PerguntaDAO.getPergunta(id);
    	List<Resposta> respostas = PerguntaDAO.getRespostasFromId(id);
    	int cLen = (respostas.size() > maxCommentsNum) ? maxCommentsNum : respostas.size();
    	
    	
    	//Build response
    	res.type("application/json");
    	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
    	List<String> tags = Arrays.asList("adhd");
    	JSONArray postContentTags = new JSONArray(); 
    	for (String tag : tags) {
    	    postContentTags.add(tag);
    	}

		
		//-----JSON Overall Structure-----------------
		
		JSONObject responseJson = new JSONObject();
			JSONObject jsonPost = new JSONObject();
				JSONObject jsonPostUser = new JSONObject();
					//jsonPostUser.put("name", pergunta.getNome_usuario()); -> find another way to get name from pergunta
					jsonPostUser.put("date", (pergunta.getData_postagem()).toString());
				JSONObject jsonPostContent = new JSONObject(); 
					jsonPostContent.put("title", pergunta.getTitulo());
					jsonPostContent.put("text", pergunta.getConteudo());
					jsonPostContent.put("likes", random.nextInt(30));
					jsonPostContent.put("comments", random.nextInt(30));
					jsonPostContent.put("tags", postContentTags);
					//jsonPostContent.put("id", pergunta.getId_pergunta());
				
				jsonPost.put("user", jsonPostUser);	
				jsonPost.put("content", jsonPostContent);
			
			JSONArray jsonArrayComments = new JSONArray();
			//Array of jsonComment from: (JSON ARRAY BUILD 01)
			
		//----------------------------------------
		
			
		///(JSON ARRAY BUILD 01)
			
			for (Resposta resposta : respostas) {
	        	JSONObject jsonComment = new JSONObject();
	        	JSONObject jsonCommentUser = new JSONObject();
		        	jsonCommentUser.put("name", resposta.getNome_usuario());
		        	jsonCommentUser.put("date", dateFormatter.format(resposta.getData_postagem().toLocalDate()));
	        	JSONObject jsonCommentContent = new JSONObject(); 
		        	jsonCommentContent.put("text", resposta.getConteudo());
		        	jsonCommentContent.put("likes", random.nextInt(30));
		        	//jsonCommentContent.put("id", respostas[i].getId_resposta());
        	
        	jsonComment.put("user", jsonCommentUser);
        	jsonComment.put("content", jsonCommentContent);
        	jsonArrayComments.add(jsonComment);
			}

        
        ///FINISH responseJson
        responseJson.put("post", jsonPost);
        responseJson.put("comment", jsonArrayComments); 
        
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
    	//GET REQ BODY
		final String reqJsonBody = req.body();
		JSONObject reqJson = parseBody(reqJsonBody);

		String dateString = (String) reqJson.get("time");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = dateFormat.parse(dateString);
        java.sql.Date date = new java.sql.Date(parsedDate.getTime());
		
		String content = (String) reqJson.get("content");
		String email = (String) reqJson.get("sub");
		String questionId = (String) reqJson.get("id");
		
		int id = Integer.parseInt(questionId);
		
		System.out.println(String.format("Got values [content=(%s), email=(%s), question_id=(%s)]",
				content, email, questionId));
		
		///PUT ON DATABASE WITH DAO (!MISSING!)
		
		RespostaDAO.inserirResposta(content, email, id, date);
		
		
		boolean sucess = false;
		if (sucess) {
			System.out.println("Sucessfully put " + questionId + " on database");
			res.status(200);
		} else {
			System.out.println("Sucessfully put on database");
			res.status(200);
		}
		
		return "sucess";
	}
	
	
	
	
	public static Object atualizaQuestoesUser(Request req, Response res) {
	
		return null;
	}
	
}



