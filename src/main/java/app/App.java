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
import model.Usuario;
import service.AuthService;
import service.ExerciciosService;
import service.ForumService;
import service.PostService;
import spark.Spark;

public class App extends dao.DAO{
	
	static Scanner sc = new Scanner(System.in);
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static Teste teste = new Teste();
	
	static UsuarioDAO usuarioDAO;
	public static void main(String args[]){	
		staticFiles.location("/public");
		staticFiles.externalLocation("src/main/resources/public");
        port(4567);
        
        Usuario user = UsuarioDAO.getUsuarioByUsername("johndoe1");
        user.quickPrint();
        //LOCAL PARA INSERIR EXERCICIOS
        
        //teste.chamaCadastro();
        
        //teste.chamaCadastroAlternativa();

        //teste.chamaCadastroPergunta();

        
        
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

	        Spark.get("/forum/explore", (req,res) -> {//Missing Database Integration
	        	return ForumService.getForumHomepage(req, res);
	        });
	        
	        
	        Spark.get("/forum/page/explore", (req,res) -> {//Missing Database Integration
	        	return PostService.getForumPagePost(req, res);
	        });
	        Spark.put("/forum/page/comment", (req, res) -> {
	        	return PostService.putForumPageComment(req, res);
	        });
	        
	        
	        Spark.get("/exercicios/load", (req,res) -> {//Missing Database Integration
	        	return ExerciciosService.getExercicio(req, res);
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
	        sc.close();
	
	}
}


