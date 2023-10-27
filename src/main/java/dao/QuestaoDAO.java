package dao;
import java.sql.*;
import model.Alternativa;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Questao;
import model.Usuario;


public class QuestaoDAO extends DAO{
	
	public static final String TABLE = "alternativas";
	public static final String cId = "id";
	public static final String cQuestaoId = "questao_id";
	
    public QuestaoDAO() {
        super();
        conectar();
    }

    static void logPStatement(String s){logPS_DAO("(QuestaoDAO) -> ", s);  }
    static void log(String s) {System.out.println("(QuestaoDAO) -> " + s); }
    
    static public List<Alternativa> getAlternativas(int idQuestao) {
        List<Alternativa> alternativas = new ArrayList<>();
        String sql = String.format("SELECT * FROM \"%s\".\"%s\" WHERE \"%s\" = ? ORDER BY \"%s\" ASC",
        		SCHEMA,TABLE,cQuestaoId,cId);
        logPStatement(sql);
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, idQuestao);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Alternativa alternativa = new Alternativa();
                alternativa.setId(rs.getInt("id"));
                alternativa.setConteudo(rs.getString("conteudo"));
                alternativa.setCorreta(rs.getBoolean("correta"));
                alternativa.setQuestao_id(rs.getInt("questao_id"));
                // Adicione aqui outros campos conforme necessário
                alternativas.add(alternativa);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return alternativas;
    }
    
}
