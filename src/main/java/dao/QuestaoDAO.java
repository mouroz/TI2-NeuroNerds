package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public boolean cadastraUsuario(Questao questao) {
        boolean status = false;
        String sql = "INSERT INTO \"BancoTI2\".\"Questao\" (\"neuro\", \"enunciado\", \"habilidade\", \"dificuldade\") VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setLong(1, questao.getNeuro_div());
            pstmt.setString(2, questao.getEnunciado());
            pstmt.setLong(3, questao.getHabilidade());
            pstmt.setLong(4, questao.getDificuldade());


            pstmt.executeUpdate();
            status = true;

            //System.out.println(usuario.toString());

        } catch (SQLException u) {
            System.out.println("Não foi possível inserir: ");
            System.out.println("Nome ou email já cadastrado");
        }
        return status;
    }  
    
}
