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

	public static final String TABLE = "Pergunta";
	public static final String cId = "id";
	public static final String cDataPostagem = "data_postagem";
	public static final String cTitulo = "titulo";
	public static final String cConteudo = "conteudo";
	public static final String cUsuario_id = "usuario_id";
	public static final UsuarioDAO usuario = new UsuarioDAO();
	public static final QuestaoDAO questao = new QuestaoDAO();
	
    public PerguntaDAO() {
        super();
        conectar();
    }

    private static void logPStatement(String s){logPS_DAO("(PerguntaDAO) -> ", s);  }
    private static void log(String s) {System.out.println("(PerguntaDAO) -> " + s); }
    
    
    public boolean cadastroPergunta(Pergunta pergunta) {
        boolean status = false;
        String sql = String.format("INSERT INTO \"%s\".\"%s\" (\"%s\", \"%s\", \"%s\", \"%s\") VALUES (?, ?, ?, ?)",
        		SCHEMA, TABLE, cTitulo, cDataPostagem, cConteudo, cUsuario_id);
        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            LocalDate currentDate = LocalDate.now();
            pstmt.setString(1, pergunta.getTitulo());
            pstmt.setDate(2, Date.valueOf(currentDate));
            pstmt.setString(3, pergunta.getConteudo());
            pstmt.setInt(4, pergunta.getId_usuario());
            pstmt.executeUpdate();
            status = true;
            System.out.println(pergunta.toString());
        } catch (SQLException u) {
            log("Não foi possível inserir a pergunta.");
        }
        return status;
    }
    

    static public Pergunta getPergunta(int idPergunta) {
    	idPergunta = 1;
        Pergunta pergunta = null;

        String sql = "SELECT \"Pergunta\".*, \"usuario\".\"username\" AS \"nome_usuario\" " +
                "FROM \"BancoTI2\".\"Pergunta\" " +
                "JOIN \"BancoTI2\".\"usuario\" ON \"Pergunta\".\"usuario_id\" = \"usuario\".\"id\" " +
                "WHERE \"Pergunta\".\"id\" = ?";
        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql))
        {	System.out.println("entrou");
        	pstmt.setInt(1, idPergunta);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Date dataPostagem = resultSet.getDate("data_postagem");
                String conteudo = resultSet.getString("conteudo");
                int idUsuario = resultSet.getInt("usuario_id");
                String nomeUsuario = resultSet.getString("nome_usuario");
                String titulo = resultSet.getString("titulo");
                System.out.println("Data de Postagem: " + dataPostagem +
                        "\nConteúdo: " + conteudo +
                        "\nID do Usuário: " + idUsuario +
                        "\nNome do Usuário: " + nomeUsuario +
                        "\nTítulo: " + titulo);

                pergunta = new Pergunta(titulo, conteudo, dataPostagem, idUsuario, nomeUsuario);
                System.out.println("Pergunta: " + pergunta.toString());
            }
        } catch (SQLException e) {
        	System.out.println("erro aqui 1");
            throw new RuntimeException(e);
        }
        return pergunta;
    }

    static public int fetchPerguntaId(Pergunta pergunta) {
        int result = -1;
        String sql = String.format("SELECT \"$s\" FROM \"%s\".\"%s\" WHERE \"%s\" = ? LIMIT 1",
        		cId, SCHEMA, TABLE, cTitulo);
        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, pergunta.getTitulo());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    static public List<Resposta> getRespostas(int id_pergunta) {
        List<Resposta> respostas = new ArrayList<>();
        String sql = "SELECT \"Resposta\".*, \"usuario\".\"username\" AS \"nome_usuario\" " +
                "FROM \"BancoTI2\".\"Resposta\" " +
                "JOIN \"BancoTI2\".\"usuario\" ON \"Resposta\".\"usuario_id\" = \"usuario\".\"id\" " +
                "WHERE \"Resposta\".\"pergunta_id\" = ? " +
                "ORDER BY \"Resposta\".\"data_postagem\" ASC";

        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id_pergunta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Resposta resposta = new Resposta();
                resposta.setId_resposta(rs.getInt("id"));
                resposta.setConteudo(rs.getString("conteudo"));
                resposta.setData_postagem(rs.getDate("data_postagem"));
                resposta.setId_usuario(rs.getInt("usuario_id"));
                resposta.setNome_usuario(rs.getString("nome")); // Aqui você seta o nome do usuário
                respostas.add(resposta);
            }
        } catch (SQLException e) {
        	System.out.println("erro aqui 2");
            throw new RuntimeException(e);
        }
        return respostas;
    }

    static public List<Pergunta> buscaUltimasCincoPerguntas() {
        List<Pergunta> perguntas = new ArrayList<>();
        String sql = "SELECT \"Pergunta\".*, \"usuario\".\"username\" AS \"nome_usuario\" " +
                "FROM \"BancoTI2\".\"Pergunta\" " +
                "JOIN \"BancoTI2\".\"usuario\" ON \"Pergunta\".\"usuario_id\" = \"usuario\".\"id\" " +
                "ORDER BY \"Pergunta\".\"data_postagem\" DESC LIMIT 5";

     
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                int idPergunta = resultSet.getInt("id");
                Date dataPostagem = resultSet.getDate("data_postagem");
                String conteudo = resultSet.getString("conteudo");
                int idUsuario = resultSet.getInt("usuario_id");
                String titulo = resultSet.getString("titulo");
                String nomeUsuario = resultSet.getString("nome_usuario");
                Pergunta pergunta = new Pergunta(titulo, conteudo, dataPostagem, idUsuario, nomeUsuario);
                perguntas.add(pergunta);
            }
        } catch (SQLException e) {
        	System.out.println("erro aqui");
            throw new RuntimeException(e);
        }
        return perguntas;
    }

    static public boolean deletePergunta(int id) {
        boolean status = false;
        String sql = String.format("DELETE FROM \"%s\".\"%s\" WHERE \"%s\" = ?", SCHEMA, TABLE, cId);
        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    
    static public List<Pergunta> list(int id1, int id2) {
        List<Pergunta> perguntas = new ArrayList<>();
        String sql = String.format("SELECT * FROM \"%s\".\"%s\" WHERE \"%s\" BETWEEN ? AND ?", SCHEMA, TABLE, cId);
        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id1);
            pstmt.setInt(2, id2);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Pergunta pergunta = new Pergunta();
                pergunta.setId_pergunta(rs.getInt("id"));
                pergunta.setTitulo(rs.getString("titulo"));
                pergunta.setData_postagem(rs.getDate("data_postagem"));
                pergunta.setConteudo(rs.getString("conteudo"));
                pergunta.setId_usuario(rs.getInt("usuario_id"));
                perguntas.add(pergunta);
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return perguntas;
    }
}
