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
	         
	        //ROTAS
	        post("/insert", (req, res) -> {
	           return Service.cadastraUsuario(req, res);
	        });
	        post("/auth", (req, res) -> {
		           return AuthService.auth(req, res);
		        });
	        /*
	        Spark.post("/insert", (req, res) -> {
	        	
	        	req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
	        	String name = req.queryParams("name");	
	        	System.out.println(name);
	        	return res;
	        	});
	        */    
 
		          
		      	        
	}
}


