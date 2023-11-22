package dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class UsuarioDAO extends DAO{
	public static final String TABLE = "usuario";
	public static final String cId = "id";
	public static final String cUsername = "username";
	public static final String cSenha = "senha";
	public static final String cEmail = "email";
	
    public UsuarioDAO() {
        super();
        conectar();
    }

    static public void logPStatement(String s){logPS_DAO("(UsuarioDAO) -> ", s);  }
    static public void log(String s) {System.out.println("(UsuarioDAO) -> " + s); }

    static public Usuario getUsuarioFrom(){return null;}
    
    static public String getNomeFromEmail(String email) {
        String sql = String.format("SELECT username FROM bancoti2.usuario WHERE email = ?", 
        		cUsername, SCHEMA, TABLE, cEmail);
        
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
        	
            pstmt.setString(1, email);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(cUsername);
                } else {
                    log("Usuário não está cadastrado");
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public boolean cadastraUsuario(Usuario usuario) throws IllegalStateException{
        boolean status = false;
        String sql = "INSERT INTO bancoti2.usuario (senha, email, username) VALUES (?, ?, ?)";
        logPStatement(sql);
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getSenha());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getUsername());

            pstmt.executeUpdate();
            status = true;

            System.out.println(usuario.toString());

        } catch (SQLException u) {
            System.out.println(u);
        }
        return status;
    }


    static public int fetchUsuarioId(Usuario usuario) {
        int result = -1;
        String sql = String.format("SELECT \"%s\" FROM \"%s\" WHERE \"%s\" = ? LIMIT 1", 
        		cId, TABLE, cEmail);
        logPStatement(sql);
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getEmail());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(cId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    
    public static boolean autenticaUsuario(String email, String senha) {    
        boolean status = false;
        // Use o nome completo da tabela, incluindo o esquema
        String sql = "SELECT username FROM bancoti2.usuario WHERE email = ? AND senha = ?";
        logPStatement(sql);
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);
            
            ResultSet resultSet = pstmt.executeQuery();
            // Se resultSet tem pelo menos uma linha, o usuário foi autenticado
            if (resultSet.next()) {
                status = true;
                log("Usuario esta cadastrado");    
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }
 

    static public boolean updateEmail(Usuario usuario) {
        boolean status = false;
        String sql = String.format("UPDATE \"%s\" SET \"%s\" = ? WHERE \"%s\" = ?", 
        		TABLE, cEmail, cId);
        logPStatement(sql);
        
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

    static public boolean updateUsername(Usuario usuario) {
        boolean status = false;
        String sql = String.format("UPDATE \"%s\" SET \"%s\" = ? WHERE \"%s\" = ?", 
        		TABLE, cUsername, cId);
        logPStatement(sql);
        
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

    
    
    static public boolean updateSenha(Usuario usuario) { 
        boolean status = false;
        String sql = String.format("UPDATE \"%s\" SET \"%s\" = ? WHERE \"%d\" = ?", 
        		TABLE, cSenha, cId);
        logPStatement(sql);
        
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
    
    static public boolean update(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s' WHERE %d = %d",
            	    TABLE,
            		cSenha, usuario.getSenha(), 
            	    cEmail, usuario.getEmail(), 
            	    cUsername, usuario.getUsername(), 
            	    cId, usuario.getId_usuario());
            logPStatement(sql);
            
            st.executeUpdate(sql);
            st.close();
            status = true;

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    static public boolean delete(int id) {
        boolean status = false;
        String sql = String.format("DELETE FROM \"%s\" WHERE \"%s\" = ?",TABLE,cId);
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
    
    public List<Usuario> list(int id1, int id2) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        String sql = String.format("SELECT * FROM \"%s\" WHERE \"%s\" BETWEEN ? AND ?", TABLE, cId);
        logPStatement(sql);
        
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
