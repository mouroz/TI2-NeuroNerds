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

    public QuestaoDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public List<Alternativa> getAlternativas(int idQuestao) {
        List<Alternativa> alternativas = new ArrayList<>();
        String sql = "SELECT * FROM BancoTI2.alternativas WHERE questao_id = ? ORDER BY id ASC";
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
