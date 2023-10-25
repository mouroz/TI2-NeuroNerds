package dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class UsuarioDAO extends DAO {

    public UsuarioDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean cadastraUsuario(Usuario usuario) {
        boolean status = false;
        String sql = "INSERT INTO \"BancoTI2\".\"usuario\" (\"senha\", \"email\", \"username\") VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getSenha());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getUsername());

            pstmt.executeUpdate();
            status = true;

            System.out.println(usuario.toString());

        } catch (SQLException u) {
            System.out.println("Não foi possível inserir: ");
            System.out.println("Nome ou email já cadastrado");
        }
        return status;
    }

    public int fetchUsuarioId(Usuario usuario) {
        int result = -1;
        String sql = "SELECT id_usuario FROM usuario WHERE email = ? LIMIT 1";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getEmail());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    
    public boolean autenticaUsuario(String senha, String email) {	
    	boolean status = false;
    	String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
    	try(PreparedStatement pstmt = conexao.prepareStatement(sql)){
    		pstmt.setString(1, email);
    		pstmt.setString(2, senha);
    		
    		pstmt.executeQuery();
    		status = true;
    	}
    	catch(SQLException e) {
    		throw new RuntimeException(e);
    	}
    	return status;
    }

    public boolean updateEmail(Usuario usuario) {
        boolean status = false;
        String sql = "UPDATE usuario SET email = ? WHERE id_usuario = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getEmail());
            pstmt.setInt(2, usuario.getId_usuario());

            pstmt.executeUpdate();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean updateUsername(Usuario usuario) {
        boolean status = false;
        String sql = "UPDATE usuario SET username = ? WHERE id_usuario = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getUsername());
            pstmt.setInt(2, usuario.getId_usuario());

            pstmt.executeUpdate();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    
    
    public boolean updateSenha(Usuario usuario) { 
        boolean status = false;
        String sql = "UPDATE usuario SET senha = ? WHERE id_usuario = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getSenha());
            pstmt.setInt(2, usuario.getId_usuario());

            pstmt.executeUpdate();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    
    public boolean update(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE usuario SET senha = " + usuario.getSenha() + ", email = '"  
                    + usuario.getEmail() + "', username = '" + usuario.getUsername() + "'"
                    + " WHERE id_usuario = " + usuario.getId_usuario();
            System.out.println(sql);
            st.executeUpdate(sql);
            st.close();
            status = true;

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    
    public List<Usuario> list(int id1, int id2) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        String sql = "SELECT * FROM usuario WHERE id_usuario BETWEEN ? AND ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id1);
            pstmt.setInt(2, id2);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setUsername(rs.getString("username"));
                usuarios.add(usuario);
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return usuarios;
    }

}
