package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.time.LocalDate;
import java.security.Timestamp;
import java.sql.Date;
import java.util.Calendar;
import model.Resposta;

import model.Pergunta;

public class RespostaDAO extends DAO{

    public RespostaDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public void inserirResposta(String username, String conteudo, String email, int perguntaId, Date data) {
        int usuarioId = buscarUsuarioIdPorUsername(username);

        if (usuarioId != -1) {
            String sqlInserirResposta = "INSERT INTO \"BancoTI2\".\"Resposta\" (\"conteudo\", \"usuario_id\", \"pergunta_id\", \"data_postagem\") VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conexao.prepareStatement(sqlInserirResposta)) {
                pstmt.setString(1, conteudo);
                pstmt.setInt(2, usuarioId);
                pstmt.setInt(3, perguntaId);
                pstmt.setDate(4, data);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Resposta inserida com sucesso!");
                } else {
                    System.out.println("Falha ao inserir resposta.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Usuário não encontrado!");
        }
    }
    
    private int buscarUsuarioIdPorUsername(String username) {
        String sqlBuscarUsuarioId = "SELECT \"id\" FROM \"BancoTI2\".\"usuario\" WHERE \"username\" = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sqlBuscarUsuarioId)) {
            pstmt.setString(1, username);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1; // Retorna -1 se o usuário não for encontrado
    }


}
