package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Calendar;

import model.Pergunta;

public class PerguntaDAO extends DAO {

    public PerguntaDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean cadastroPergunta(Pergunta pergunta) {
        boolean status = false;
        String sql = "INSERT INTO \"BancoTI2\".\"Pergunta\" (\"titulo\", \"data_postagem\", \"conteudo\", \"id_usuario\") VALUES (?, ?, ?, ?)";
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
            System.out.println("Não foi possível inserir a pergunta.");
        }
        return status;
    }

    public int fetchPerguntaId(Pergunta pergunta) {
        int result = -1;
        String sql = "SELECT id_pergunta FROM Pergunta WHERE titulo = ? LIMIT 1";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, pergunta.getConteudo());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt("id_pergunta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public boolean deletePergunta(int id) {
        boolean status = false;
        String sql = "DELETE FROM Pergunta WHERE id_pergunta = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    
    public List<Pergunta> list(int id1, int id2) {
        List<Pergunta> perguntas = new ArrayList<Pergunta>();
        String sql = "SELECT * FROM Pergunta WHERE id_pergunta BETWEEN ? AND ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id1);
            pstmt.setInt(2, id2);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Pergunta pergunta = new Pergunta();
                pergunta.setId_pergunta(rs.getInt("id_pergunta"));
                pergunta.setTitulo(rs.getString("titulo"));
                pergunta.setData_postagem(rs.getDate("data_postagem").toLocalDate());
                pergunta.setConteudo(rs.getString("conteudo"));
                pergunta.setId_usuario(rs.getInt("id_usuario"));
                perguntas.add(pergunta);
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return perguntas;
    }

}
