package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import model.Resposta;
import model.Pergunta;

public class PerguntaDAO extends DAO {

	public static final String cId = "id";
	public static final String cDataPostagem = "data_postagem";
	public static final String cTitulo = "titulo";
	public static final String cConteudo = "conteudo";
	public static final String cUsuario_id = "usuario_id";
	
    
	
	
	
	public static int getPerguntaId(Pergunta pergunta) {
        int result = -1;
        String sql = "SELECT id FROM bancoti2.pergunta WHERE titulo = ? LIMIT 1";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, pergunta.getTitulo());
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                result = resultSet.getInt("id");
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        
        return result;
    }
    
    
    public static boolean postPergunta(Pergunta pergunta) {
        String sql = "INSERT INTO bancoti2.pergunta"
        		+ " (titulo, data_postagem, conteudo, usuario_id) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            LocalDate currentDate = LocalDate.now();
            pstmt.setString(1, pergunta.getTitulo());
            pstmt.setDate(2, Date.valueOf(currentDate));
            pstmt.setString(3, pergunta.getConteudo());
            pstmt.setInt(4, pergunta.getUsuarioId());
            pstmt.executeUpdate();
            
            System.out.println(pergunta.toString());
            
        } catch (SQLException u) {
            log("Não foi possível inserir a pergunta.");
            return false;
            
        }
        return true;
    }
    

    public static boolean deletePergunta(int id) {
        String sql = "DELETE FROM banconti2.pergunta WHERE id = ?";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
     
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            
        }
        return true;
    }
    
    
    
    /* List<Respostas> from a Pergunta id
     * Returns an empty list if failed
     */
    public static List<Resposta> getRespostasFromId(int id) {
        List<Resposta> respostas = new ArrayList<>();
        String sql = """
        		SELECT resposta.*, usuario.username AS nome
                FROM bancoti2.resposta
                JOIN bancoti2.usuario ON resposta.usuario_id = usuario.id
                WHERE resposta.pergunta_id = ?
                ORDER BY resposta.data_postagem ASC""";

        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Resposta resposta = new Resposta();
                resposta.setId_resposta(rs.getInt("id"));
                resposta.setConteudo(rs.getString("conteudo"));
                resposta.setData_postagem(rs.getDate("data_postagem"));
                resposta.setId_usuario(rs.getInt("usuario_id"));
                resposta.setNome_usuario(rs.getString("nome_usuario")); // Aqui você seta o nome do usuário
                respostas.add(resposta);
            }
        
        } catch (SQLException e) {
        	e.printStackTrace();
       
        }
        return respostas;
    }

    
    //List of Perguntas with its max length as parameter
    public static List<Pergunta> getMostRecentList(int limit) {
        List<Pergunta> perguntas = new ArrayList<>();
        String sql = """
        		SELECT pergunta.*, usuario.username AS nome
                FROM bancoti2.pergunta
                JOIN bancoti2.usuario ON pergunta.usuario_id = usuario.id
                ORDER BY pergunta.data_postagem DESC LIMIT """ + limit;


        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date data = resultSet.getDate("data_postagem");
                String conteudo = resultSet.getString("conteudo");
                int usuario_id = resultSet.getInt("usuario_id");
                String titulo = resultSet.getString("titulo");
                //String nomeUsuario = resultSet.getString("nome_usuario");
               
                Pergunta pergunta = new Pergunta(id, data, titulo, conteudo, usuario_id);
                perguntas.add(pergunta);
                
            }
            
        } catch (SQLException e) {
        	e.printStackTrace();
        	
        }
        return perguntas;
    }

    
    
    public static List<Pergunta> getListBetweenIds(int low, int high) {
        List<Pergunta> perguntas = new ArrayList<>();
        String sql = "SELECT * FROM bancoti2.pergunta WHERE id BETWEEN ? AND ?";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, low);
            pstmt.setInt(2, high);
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                Date data = resultSet.getDate("data_postagem");
                String conteudo = resultSet.getString("conteudo");
                int usuario_id = resultSet.getInt("usuario_id");
                
                perguntas.add(new Pergunta(id, data, titulo, conteudo, usuario_id));
            }
            
        } catch (SQLException e) {
           e.printStackTrace();
           
        }
        return perguntas;
    }
    
    
    /*
     * No idea what this one is about
     */
    public static Pergunta getPergunta(int id) {
        Pergunta pergunta = null;
        
        /*("SELECT \"Pergunta\".*, \"usuario\".\"username\" AS \"nome_usuario\" " +
            "FROM \"BancoTI2\".\"Pergunta\" " +
            "JOIN \"BancoTI2\".\"usuario\" ON \"Pergunta\".\"usuario_id\" = \"usuario\".\"id\" " +
            "WHERE \"Pergunta\".\"id\" = ?"
         */
        String sql = "";
        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)){	
        	pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Date data = resultSet.getDate("data_postagem");
                String conteudo = resultSet.getString("conteudo");
                int idUsuario = resultSet.getInt("usuario_id");
                String titulo = resultSet.getString("titulo");
                //String nomeUsuario = resultSet.getString("nome_usuario");
    
                pergunta = new Pergunta(id, data, titulo,  conteudo, id);
                System.out.println("Pergunta: " + pergunta.toString());
                
            }
        } catch (SQLException e) {
        	System.out.println("erro aqui 1");
            throw new RuntimeException(e);
            
        }
        return pergunta;
    }

    private static void logPStatement(String s){logPS_DAO("(PerguntaDAO) -> ", s);  }
    private static void log(String s) {System.out.println("(PerguntaDAO) -> " + s); }
    
}
