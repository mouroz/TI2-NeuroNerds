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
	
	//FUNCAO TEMPORARIA ENQUANTO FRONT NAO FOI IMPLEMENTADO
	public void cadastraPergunta(String titulo, String conteudo, Date data, int idUsuario) {
		
        String sql = "INSERT INTO \"BancoTI2\".\"Pergunta\" (\"titulo\", \"conteudo\", \"data_postagem\", \"id_usuario\") VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, conteudo);
            pstmt.setDate(3, data);
            pstmt.setInt(4, idUsuario);

            pstmt.executeUpdate();

        } catch (SQLException u) {
            System.out.println("Não foi possível inserir: ");
            System.out.println("Nome ou email já cadastrado");
        }
	}
	
	static UsuarioDAO usuarioDAO;
	public static void main(String args[]){	
		usuarioDAO = new UsuarioDAO();
	
		staticFiles.location("/public");
		staticFiles.externalLocation("src/main/resources/public");
        port(4567);
        
        System.out.println("Digite o titulo: ");
        String titulo = sc.nextLine();
        System.out.println("Digite o conteudo: ");
        String conteudo = sc.nextLine();
        System.out.println("Digite a data: ");
        String data = sc.nextLine();
        
        try {
            java.util.Date dataUtil = formatter.parse(data);
            java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());
            System.out.println("Data inserida: " + dataSql);
        } catch (Exception e) {
            System.out.println("Formato de data inválido!");
        }
        
        
        System.out.println("Digite o id: ");
        int id_usuario = sc.nextInt();   
        
        //cadastraPergunta(titulo,conteudo,dataSql,id_usuario);
        
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
	      sc.close();
	}
}


