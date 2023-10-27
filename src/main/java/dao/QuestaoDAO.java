package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Questao;
import model.Usuario;


public class QuestaoDAO extends DAO{
	String TABLE = "Questao";
	String cNeuro = "neuro";
	String cEnunciado = "enunciado";
	String cHabilidade = "habilidade";
	String cDificuldade = "dificuldade";
	
	
    public QuestaoDAO() {
        super();
        conectar();
    }

    void logPStatement(String s){logPS_DAO("(UsuarioDAO) -> ", s);  }
    void log(String s) {System.out.println("(UsuarioDAO) -> " + s); }
    public void finalize() {close();}

    public boolean cadastraUsuario(Questao questao) {
        boolean status = false;
        String sql = String.format("INSERT INTO \"%s\".\"%s\" (\"%s\", \"%s\", \"%s\", \"%s\") VALUES (?, ?, ?, ?)",
        		SCHEMA,TABLE,cNeuro,cEnunciado,cHabilidade,cDificuldade);
        logPStatement(sql);
        
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
