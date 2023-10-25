package app;
import static spark.Spark.*;

import java.util.List;
import java.util.Scanner;

import javax.servlet.MultipartConfigElement;

import spark.Request;
import spark.Response;
import spark.Spark;

import dao.UsuarioDAO;
import model.Usuario;
import service.Service;
import service.AuthService;

public class App{
	static UsuarioDAO usuarioDAO;
	public static void main(String args[]){	
		usuarioDAO = new UsuarioDAO();
	
		staticFiles.location("/public");
		staticFiles.externalLocation("src/main/resources/public");
        port(4567);
		    
        
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
	        post("/cadastro", (req, res) -> {return AuthService.cadastraUsuario(req, res);});
	        post("/auth", (req, res) -> {return AuthService.auth(req, res);});
	        get("/exercicios", (req,res) -> {return Service.getExercicio(req, res);});
	        get("/forum-homepage", (req,res) -> {return Service.getForumHomepage(req, res);});
	        get("/forum-post", (req,res) -> {return Service.getForumPost(req, res);});
	      
	}
}


