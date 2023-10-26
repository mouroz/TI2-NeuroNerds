package app;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Teste extends dao.DAO{
	
    public Teste() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

	
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	//FUNCAO TEMPORARIA ENQUANTO FRONT NAO FOI IMPLEMENTADO
	public void cadastraPergunta(String titulo, String conteudo, Date data, int idUsuario) {
		
        String sql = "INSERT INTO \"BancoTI2\".\"Pergunta\" (\"titulo\", \"conteudo\", \"data_postagem\", \"id_usuario\") VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, conteudo);
            pstmt.setDate(3, data);
            pstmt.setInt(4, idUsuario);

            pstmt.executeUpdate();

        } catch (SQLException u) {
            System.out.println(u);
        }
	}
	
}
