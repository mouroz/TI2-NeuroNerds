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
	           return postInsert(req, res);
	        });
	        post("/auth", (req, res) -> {
		           return Service.auth(req, res);
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
	
	
	static Response postInsert(Request request, Response response){
		 // Pegue os dados do formulário
        
        String name = request.queryParams("name");			  
        System.out.println(name);			            
        
        String email = request.queryParams("email");
        String password = request.queryParams("password");
        // Se você tiver outros campos, adicione-os aqui...

        // Montando o objeto Usuario
        Usuario usuario = new Usuario(name, email, password); // Ajuste conforme seu construtor
        //usuarioDAO.insert(usuario);
        //response.redirect("/cadastro.html");
        
        // Chame a função insert()
        if (usuarioDAO.insert(usuario)) {
            //response.redirect("/sucesso");
        	System.out.println("/sucesso");
        } else {
        	System.out.println("/erro");
            //response.redirect("/erro");
        }
        
        return null;
	}
}


