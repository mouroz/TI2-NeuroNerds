package app;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import dao.UsuarioDAO;
import service.AuthService;
import service.Service;
import spark.Spark;

public class App extends dao.DAO{
	
	static Scanner sc = new Scanner(System.in);
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static Teste teste = new Teste();
	
	static UsuarioDAO usuarioDAO;
	public static void main(String args[]){	
		usuarioDAO = new UsuarioDAO();
	
		staticFiles.location("/public");
		staticFiles.externalLocation("src/main/resources/public");
        port(4567);
        
        //LOCAL PARA INSERIR EXERCICIOS
        
        //teste.chamaCadastro();
        teste.chamaCadastroAlternativa();
        
        
        	//Comecando em index.html
	        Spark.get("/", (req, res) -> {
		       	System.out.println("Received POST request to /");
		
		        return App.class.getResourceAsStream("index.html");
	        });
	        
	        //Exceptions dos services
	        Spark.exception(Exception.class, (e, req, res) -> {
		    	System.err.println(e.getMessage());
		        res.body(""); // | res.body(e.getMessage())
		        res.status(400);
		    }); 	
	        
	        
	        //ROTAS
	        
	        Spark.post("/auth", (req, res) -> {//100% functional
	        	return AuthService.auth(req, res);
	        }); 
	        Spark.get("/exercicios", (req,res) -> {//Missing Database Integration
	        	return Service.getExercicio(req, res);
	        });
	        Spark.get("/forum/homepage", (req,res) -> {//Missing Database Integration
	        	return Service.getForumHomepage(req, res);
	        });
	        Spark.get("/forum/page/load-post", (req,res) -> {//Missing Database Integration
	        	return Service.getForumPagePost(req, res);
	        });
	        Spark.put("/forum/page/comment", (req, res) -> {
	        	return Service.putForumPageComment(req, res);
	        });
	        
	        
	        Spark.post("/cadastro-user", (req, res) -> {
	        	/*
	        	 * Integration partially complete - The request is sucessfully received,
	        	 * the code is executed and there are no errors on the database Integration.
	        	 * However, after the return null call, eclipse gives a mapping error for 
	        	 * unknown reasons. Even updating mavens build, clearing and rebuilding did no good
	        	 */
	        	return AuthService.cadastraUsuario(req, res);
	        });
	      //sc.close();
	}
}


